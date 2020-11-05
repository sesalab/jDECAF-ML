package it.unisa.jDECAF_ML.taco.detectors;

import it.unisa.jDECAF_ML.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.parser.bean.MethodBean;
import org.apache.commons.text.similarity.CosineDistance;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class BlobDetector implements SmellDetector {

    private List<ClassBean> projectClasses;

    public BlobDetector(List<ClassBean> projectClasses) {
        this.projectClasses = projectClasses;
    }

    @Override
    public List<AnalyzedComponent> detectSmells() {
        return projectClasses.stream().map(this::computeClassSmelliness).collect(Collectors.toList());
    }

    private AnalyzedComponent computeClassSmelliness(ClassBean classBean) {
        return new AnalyzedComponent(classBean, 1 - classCohesion(classBean));
    }

    private Double classCohesion(ClassBean classBean) {
        List<Double> similaritiesBetweenMethods = new LinkedList<>();
        CosineDistance distance = new CosineDistance();

        for(MethodBean method: classBean.getMethods()){
            for(MethodBean otherMethod: classBean.getMethods()){
                if(!method.equals(otherMethod)){
                    Double methodsSimilarity = 1 - distance.apply(method.getTextContent(), otherMethod.getTextContent());
                    similaritiesBetweenMethods.add(methodsSimilarity);
                }
            }
        }
        Double sum = similaritiesBetweenMethods.stream().reduce(0.0, Double::sum);
        int nOfComparisons = similaritiesBetweenMethods.size();
        return nOfComparisons == 0 ? 1 : sum/ nOfComparisons;
    }


}
