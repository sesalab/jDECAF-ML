package it.unisa.jDECAF_ML.taco.detectors;

import it.unisa.jDECAF_ML.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.parser.bean.MethodBean;

import java.util.List;
import java.util.stream.Collectors;

public class LongMethodDetector implements CodeSmellDetector {

    @Override
    public List<AnalyzedComponent> detectSmells(List<ClassBean> classesUnderAnalysis) {
        List<MethodBean> allMethods = classesUnderAnalysis.stream().flatMap(clazz -> clazz.getMethods().stream()).collect(Collectors.toList());
        return allMethods.stream().map(this::analyzeMethod).collect(Collectors.toList());
    }

    private AnalyzedComponent analyzeMethod(MethodBean methodBean) {
        Double methodCohesion = methodBean.textualMethodCohesion();
        return new AnalyzedComponent(methodBean,1-methodCohesion, SmellType.LONG_METHOD);
    }

}
