package it.unisa.jDECAF_ML.hist;

import it.unisa.jDECAF_ML.metrics.ReadSourceCode;
import it.unisa.jDECAF_ML.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.parser.bean.MethodBean;
import org.repodriller.domain.*;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.SCMRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class FeatureEnvyDetectionCommitVisitor implements CommitVisitor {

    List<ClassBean> projectClasses;
    Map<MethodBean, MethodChangeRecord> methodChangeRegistry;

    public FeatureEnvyDetectionCommitVisitor(List<ClassBean> projectClasses) {
        this.projectClasses = projectClasses;
        methodChangeRegistry = new ConcurrentHashMap<>();
    }

    public Map<MethodBean, MethodChangeRecord> getMethodChangeRegistry() {
        return methodChangeRegistry;
    }

    @Override
    public void process(SCMRepository scmRepository, Commit commit, PersistenceMechanism persistenceMechanism) {
        List<MethodBean> projectMethods = getMethodsFrom(projectClasses);

        Set<MethodBean> changedMethods = getChangedMethods(commit);
        Set<ClassBean> changedClasses = changedMethods.stream().map(MethodBean::getBelongingClass).collect(Collectors.toSet());

        for(MethodBean method : projectMethods){
            if(changedMethods.contains(method)) {
                updateMethodChangesRecord(method, changedClasses);
            }
        }
    }

    private List<MethodBean> getMethodsFrom(List<ClassBean> classes) {
        return classes.stream().flatMap(clazz -> clazz.getMethods().stream()).collect(Collectors.toList());
    }

    private void updateMethodChangesRecord(MethodBean methodUnderAnalysis, Set<ClassBean> changedClasses) {
        MethodChangeRecord methodChangeRecord = methodChangeRegistry.get(methodUnderAnalysis);

        if(null == methodChangeRecord){
            methodChangeRecord = new MethodChangeRecord(methodUnderAnalysis);
        }

        for(ClassBean changedClass: changedClasses){
            methodChangeRecord.incrementChangesOf(changedClass);
        }

        methodChangeRegistry.put(methodUnderAnalysis,methodChangeRecord);
    }

    private Set<MethodBean> getChangedMethods(Commit commit) {
        Set<MethodBean> result = new HashSet<>();

        for(Modification m : commit.getModifications()){
            if(!m.fileNameEndsWith(".java")){
                continue;
            }

            List<ClassBean> classesInFile = ReadSourceCode.readSourceCodeFromString(m.getSourceCode());
            List<MethodBean> methodsInFile = getMethodsFrom(classesInFile);

            DiffParser diffParser = new DiffParser(m.getDiff());
            List<DiffBlock> diffBlocks = diffParser.getBlocks();

            for(DiffBlock block : diffBlocks){
                List<DiffLine> linesInNewFile = block.getLinesInNewFile();
                int firstLineNumber = linesInNewFile.get(0).getLineNumber();
                int lastLineNumber = linesInNewFile.get(linesInNewFile.size() -1).getLineNumber();

                for(MethodBean method: methodsInFile){
                    if((firstLineNumber <= method.getEndLine()) && (lastLineNumber >= method.getStartLine())){
                        result.add(method);
                    }
                }

            }

        }

        return result;
    }


    protected static class MethodChangeRecord {
        private MethodBean method;
        private final Map<ClassBean, Integer> coChangesWithClasses;

        public MethodChangeRecord(MethodBean method) {
            this.method = method;
            coChangesWithClasses = new ConcurrentHashMap<>();
        }

        public void incrementChangesOf(ClassBean clazz){
            coChangesWithClasses.merge(clazz, 1, Integer::sum);
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("MethodChangeRecord{");
            sb.append("method=").append(method);
            sb.append(", coChangesWithClasses=").append(coChangesWithClasses);
            sb.append('}');
            return sb.toString();
        }
    }
}


