package it.unisa.jDECAF_ML.taco.detectors;

import it.unisa.jDECAF_ML.parser.bean.ClassBean;

import java.util.List;
import java.util.stream.Collectors;

public class ComplexClassDetector implements SmellDetector {

    private final List<ClassBean> projectClasses;

    public ComplexClassDetector(List<ClassBean> projectClasses) {
        this.projectClasses = projectClasses;
    }

    @Override
    public List<AnalyzedComponent> detectSmells() {
        return projectClasses.stream().map(this::analyzeClass).collect(Collectors.toList());
    }

    private AnalyzedComponent analyzeClass(ClassBean clazz) {
        return new AnalyzedComponent(clazz,clazz.wordsEntropy(),SmellType.COMPLEX_CLASS);
    }
}
