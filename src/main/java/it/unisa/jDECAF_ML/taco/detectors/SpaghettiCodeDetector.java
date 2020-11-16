package it.unisa.jDECAF_ML.taco.detectors;

import it.unisa.jDECAF_ML.parser.bean.ClassBean;

import java.util.List;
import java.util.stream.Collectors;

public class SpaghettiCodeDetector implements SmellDetector {

    private final LongMethodDetector longMethodDetector;

    public SpaghettiCodeDetector(LongMethodDetector longMethodDetector) {

        this.longMethodDetector = longMethodDetector;
    }


    @Override
    public List<AnalyzedComponent> detectSmells(List<ClassBean> classesUnderAnalysis) {
        return classesUnderAnalysis.stream().map(this::analyzeClass).collect(Collectors.toList());
    }

    private AnalyzedComponent analyzeClass(ClassBean clazz) {
        return null;
    }
}
