package it.unisa.jDECAF_ML.taco.detectors;

import it.unisa.jDECAF_ML.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.parser.bean.MethodBean;
import it.unisa.jDECAF_ML.parser.bean.MethodBlockBean;

public interface ComponentSimilarity {
    Double similarity(MethodBean method, MethodBean otherMethod);

    Double similarity(MethodBean method, ClassBean clazz);

    Double similarity(MethodBlockBean blockBean, MethodBlockBean otherBlockBean);
}
