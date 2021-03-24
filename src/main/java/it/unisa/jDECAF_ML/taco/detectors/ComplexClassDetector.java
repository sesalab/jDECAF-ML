package it.unisa.jDECAF_ML.taco.detectors;

import it.unisa.jDECAF_ML.parser.bean.ClassBean;

import java.util.List;
import java.util.stream.Collectors;

public class ComplexClassDetector implements CodeSmellDetector {

    @Override
    public List<AnalyzedComponent> detectSmells(List<ClassBean> projectClasses) {
        return projectClasses.stream().map(this::analyzeClass).collect(Collectors.toList());
    }

    private AnalyzedComponent analyzeClass(ClassBean clazz) {
        return new AnalyzedComponent(clazz,clazz.wordsEntropy(),SmellType.COMPLEX_CLASS);
    }
}
