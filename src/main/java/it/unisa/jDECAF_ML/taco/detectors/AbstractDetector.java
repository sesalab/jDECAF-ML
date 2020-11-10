package it.unisa.jDECAF_ML.taco.detectors;

import it.unisa.jDECAF_ML.parser.bean.ClassBean;

import java.util.List;

public abstract class AbstractDetector implements SmellDetector {
    protected final List<ClassBean> projectClasses;
    protected final ComponentSimilarity componentSimilarity;

    public AbstractDetector(List<ClassBean> projectClasses, ComponentSimilarity componentSimilarity) {
        this.projectClasses = projectClasses;
        this.componentSimilarity = componentSimilarity;
    }
}
