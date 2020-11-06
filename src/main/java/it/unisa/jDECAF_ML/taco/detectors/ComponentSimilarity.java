package it.unisa.jDECAF_ML.taco.detectors;

import it.unisa.jDECAF_ML.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.parser.bean.MethodBean;

public interface ComponentSimilarity {
    Double similarity(MethodBean method, MethodBean otherMethod);

    Double similarity(MethodBean method, ClassBean clazz);
}
