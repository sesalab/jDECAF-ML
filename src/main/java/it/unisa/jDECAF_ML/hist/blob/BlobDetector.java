package it.unisa.jDECAF_ML.hist.blob;

import it.unisa.jDECAF_ML.hist.AnalyzedComponent;
import it.unisa.jDECAF_ML.hist.CodeSmellDetector;
import it.unisa.jDECAF_ML.hist.HistStudy;
import it.unisa.jDECAF_ML.parser.bean.ClassBean;
import org.repodriller.RepoDriller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BlobDetector implements CodeSmellDetector {
    private final String projectPath;

    public BlobDetector(String projectPath) {
        this.projectPath = projectPath;
    }

    @Override
    public List<AnalyzedComponent> detectSmells(List<ClassBean> classesUnderAnalysis) {
        BlobDetectionCommitVisitor commitVisitor = new BlobDetectionCommitVisitor();
        HistStudy study = new HistStudy(commitVisitor, projectPath);
        new RepoDriller().start(study);

        Map<String, Integer> fileModificationMap = commitVisitor.getFileModificationMap();
        int commitCount = commitVisitor.getCommitCount();


        return classesUnderAnalysis.stream().map(clazz -> {
            int numOfChanges = fileModificationMap.get(clazz.getContainingFileName());
            double changePercentage = ((double) numOfChanges / commitCount) * 100;
            return new AnalyzedComponent(clazz,changePercentage);
        }).collect(Collectors.toList());

    }
}
