package it.unisa.jDECAF_ML.taco.detectors;

import it.unisa.jDECAF_ML.parser.bean.ClassBean;

import java.util.List;
import java.util.stream.Collectors;

public class BlobDetector implements CodeSmellDetector {

    @Override
    public List<AnalyzedComponent> detectSmells(List<ClassBean> classesUnderAnalysis) {
        return classesUnderAnalysis.stream().map(this::computeClassSmelliness).collect(Collectors.toList());
    }

    private AnalyzedComponent computeClassSmelliness(ClassBean classBean) {
        return new AnalyzedComponent(classBean, 1 - classBean.textualClassCohesion(), SmellType.BLOB);
    }


}
