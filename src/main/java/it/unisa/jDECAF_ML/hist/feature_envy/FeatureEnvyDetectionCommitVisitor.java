package it.unisa.jDECAF_ML.hist.feature_envy;

import it.unisa.jDECAF_ML.hist.CommitParsingUtils;
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

    private final CommitParsingUtils commitParsingUtils = new CommitParsingUtils();
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
        List<MethodBean> projectMethods = commitParsingUtils.getMethodsFrom(projectClasses);

        Set<MethodBean> changedMethods = commitParsingUtils.getChangedMethods(commit);
        Set<ClassBean> changedClasses = changedMethods.stream().map(MethodBean::getBelongingClass).collect(Collectors.toSet());

        for(MethodBean method : projectMethods){
            if(changedMethods.contains(method)) {
                updateMethodChangesRecord(method, changedClasses);
            }
        }
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


}


