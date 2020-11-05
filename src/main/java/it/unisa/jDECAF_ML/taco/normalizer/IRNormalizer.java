package it.unisa.jDECAF_ML.taco.normalizer;

import it.unisa.jDECAF_ML.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.parser.bean.MethodBean;

import java.util.List;
import java.util.stream.Collectors;

public class IRNormalizer {

    public List<ClassBean> normalizeClasses(List<ClassBean> projectClasses) {
        return projectClasses.stream().map(this::normalizeClass).collect(Collectors.toList());
    }

    private ClassBean normalizeClass(ClassBean classBean) {
        classBean.setTextContent(normalizeText(classBean.getTextContent()));
        for(MethodBean classMethod : classBean.getMethods()){
            classMethod.setTextContent(normalizeText(classMethod.getTextContent()));
        }
        return classBean;
    }

    private String normalizeText(String classTextContent) {
        return applyPorterStemmer(removeStopWords(allWordsToLowercase(splitIdentifiers(classTextContent))));
    }

    private String applyPorterStemmer(String classBean) {
        return null;
    }

    private String removeStopWords(String classBean) {
        return null;
    }

    private String allWordsToLowercase(String classBean) {
        return null;
    }

    private String splitIdentifiers(String classBean) {
        return null;
    }
}
