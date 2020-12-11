package it.unisa.jDECAF_ML.hist.feature_envy;

import it.unisa.jDECAF_ML.hist.AnalyzedComponent;
import it.unisa.jDECAF_ML.hist.CodeSmellDetector;
import it.unisa.jDECAF_ML.hist.HistStudy;
import it.unisa.jDECAF_ML.parser.bean.ClassBean;
import org.repodriller.RepoDriller;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class FeatureEnvyDetector implements CodeSmellDetector {

    private final String projectPath;

    public FeatureEnvyDetector(String projectPath) {
        this.projectPath = projectPath;
    }


    @Override
    public List<AnalyzedComponent> detectSmells(List<ClassBean> classesUnderAnalysis) {
        FeatureEnvyDetectionCommitVisitor featureEnvyVisitor = new FeatureEnvyDetectionCommitVisitor(classesUnderAnalysis);
        HistStudy study = new HistStudy(featureEnvyVisitor,projectPath);
        new RepoDriller().start(study);

        System.out.println("All Methods analyzed");
        Collection<MethodChangeRecord> values = featureEnvyVisitor.getMethodChangeRegistry().values();
        //TODO: Do something with these

        return Collections.emptyList();
    }
}