package it.unisa.jDECAF_ML.taco.detectors;

import it.unisa.jDECAF_ML.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.parser.bean.MethodBean;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class BlobDetector implements SmellDetector {

    private final List<ClassBean> projectClasses;
    private final ComponentSimilarity componentSimilarity;

    public BlobDetector(List<ClassBean> projectClasses, ComponentSimilarity componentSimilarity) {
        this.projectClasses = projectClasses;
        this.componentSimilarity = componentSimilarity;
    }

    @Override
    public List<AnalyzedComponent> detectSmells() {
        return projectClasses.stream().map(this::computeClassSmelliness).collect(Collectors.toList());
    }

    private AnalyzedComponent computeClassSmelliness(ClassBean classBean) {
        return new AnalyzedComponent(classBean, 1 - classCohesion(classBean), SmellType.BLOB);
    }

    private Double classCohesion(ClassBean classBean) {
        List<Double> similaritiesBetweenMethods = new LinkedList<>();

        for(MethodBean method: classBean.getMethods()){
            for(MethodBean otherMethod: classBean.getMethods()){
                if(!method.equals(otherMethod)){
                    Double methodsSimilarity = componentSimilarity.similarity(method, otherMethod);
                    similaritiesBetweenMethods.add(methodsSimilarity);
                }
            }
        }
        Double sum = similaritiesBetweenMethods.stream().reduce(0.0, Double::sum);
        int nOfComparisons = similaritiesBetweenMethods.size();
        return nOfComparisons == 0 ? 1 : sum/ nOfComparisons;
    }


}
