package it.unisa.jDECAF_ML.taco.detectors;

import it.unisa.jDECAF_ML.parser.bean.ClassBean;

import java.util.List;

public abstract class SmellDetectorFactory {
    public abstract SmellDetector create(List<ClassBean> normalizedClasses);
}
