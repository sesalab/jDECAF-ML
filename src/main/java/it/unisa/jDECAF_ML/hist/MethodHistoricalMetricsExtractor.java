package it.unisa.jDECAF_ML.hist;

import it.unisa.jDECAF_ML.parser.bean.MethodBean;
import org.repodriller.domain.Commit;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.SCMRepository;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MethodHistoricalMetricsExtractor implements CommitVisitor {

    Map<MethodBean, Integer> methodChanges;
    AtomicInteger commitCount;
    CommitParsingUtils commitParsingUtils;

    public MethodHistoricalMetricsExtractor() {
        methodChanges = new ConcurrentHashMap<>();
        commitCount = new AtomicInteger(0);
        commitParsingUtils = new CommitParsingUtils();
    }

    @Override
    public void process(SCMRepository scmRepository, Commit commit, PersistenceMechanism persistenceMechanism) {
        Set<MethodBean> changedMethods = commitParsingUtils.getChangedMethods(commit);
        changedMethods.forEach(this::updateChangeMethods);
    }

    @Override
    public void finalize(SCMRepository repo, PersistenceMechanism writer) {
        methodChanges.keySet().forEach(methodBean -> {
            double changePercentage = (double) methodChanges.get(methodBean) / commitCount.get();
            writer.write(methodBean.getQualifiedName(), changePercentage);
        });
    }


    private void updateChangeMethods(MethodBean methodBean) {
        if (!methodChanges.containsKey(methodBean)) {
            methodChanges.put(methodBean, 0);
        }

        int currentModifications = methodChanges.get(methodBean);
        methodChanges.put(methodBean, currentModifications + 1);
        commitCount.incrementAndGet();
    }
}
