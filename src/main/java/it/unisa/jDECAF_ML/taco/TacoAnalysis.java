package it.unisa.jDECAF_ML.taco;

import it.unisa.jDECAF_ML.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.taco.detectors.AnalyzedComponent;
import it.unisa.jDECAF_ML.taco.detectors.SmellDetector;
import it.unisa.jDECAF_ML.taco.detectors.SmellDetectorFactory;
import it.unisa.jDECAF_ML.taco.normalizer.IRNormalizer;

import java.util.List;

public class TacoAnalysis {

    private SmellDetectorFactory detectorFactory;
    private IRNormalizer normalizer;

    public TacoAnalysis(SmellDetectorFactory detectorFactory, IRNormalizer normalizer) {
        this.detectorFactory = detectorFactory;
        this.normalizer = normalizer;
    }

    public List<AnalyzedComponent> execute(List<ClassBean> projectClasses){
        List<ClassBean > normalizedClasses = normalizer.normalizeClasses(projectClasses);
        SmellDetector detector = detectorFactory.create(normalizedClasses);
        return detector.detectSmells();
    }

}
