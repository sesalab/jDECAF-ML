package it.unisa.jDECAF_ML.hist;

import org.repodriller.domain.Commit;
import org.repodriller.domain.Modification;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.SCMRepository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class BlobDetectionCommitVisitor implements CommitVisitor {

    private Map<String, Integer> fileModificationMap;
    private AtomicInteger commitCount;

    public BlobDetectionCommitVisitor() {
        fileModificationMap = new ConcurrentHashMap<>();
        commitCount = new AtomicInteger(0);
    }

    public Map<String, Integer> getFileModificationMap() {
        return fileModificationMap;
    }

    public int getCommitCount(){
        return commitCount.get();
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
}
