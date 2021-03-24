package it.unisa.jDECAF_ML.hist;

import it.unisa.jDECAF_ML.parser.bean.ClassBean;
import org.repodriller.domain.Commit;
import org.repodriller.domain.Modification;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.SCMRepository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ClassHistoricalMetricsExtractorVisitor implements CommitVisitor {

    private Map<String, Integer> fileModificationMap;
    private AtomicInteger commitCount;
    private final List<ClassBean> classesUnderAnalysis;

    public ClassHistoricalMetricsExtractorVisitor(List<ClassBean> classesUnderAnalysis) {
        this.classesUnderAnalysis = classesUnderAnalysis;
        fileModificationMap = new ConcurrentHashMap<>();
        commitCount = new AtomicInteger(0);
    }

    @Override
    public void initialize(SCMRepository repo, PersistenceMechanism writer) {
        writer.write("ClassQualifiedName","ChangePercentage");
    }

    @Override
    public void process(SCMRepository scmRepository, Commit commit, PersistenceMechanism persistenceMechanism) {
        for(Modification m : commit.getModifications()){
            String modifiedFileName = m.getFileName();
            if(m.fileNameEndsWith(".java")) {
                if (!fileModificationMap.containsKey(modifiedFileName)) {
                    fileModificationMap.put(modifiedFileName, 0);
                }

                int currentModifications = fileModificationMap.get(modifiedFileName);
                fileModificationMap.put(modifiedFileName, currentModifications + 1);
                commitCount.incrementAndGet();
            }
        }
    }

    @Override
    public void finalize(SCMRepository repo, PersistenceMechanism writer) {
        classesUnderAnalysis.forEach(clazz -> {
            int numOfChanges = fileModificationMap.get(clazz.getContainingFileName());
            double changePercentage = ((double) numOfChanges / commitCount.get()) * 100;

            writer.write(clazz.getQualifiedName(), changePercentage);
        });
    }
}
