package it.unisa.jDECAF_ML.taco.detectors;

import it.unisa.jDECAF_ML.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.parser.bean.MethodBean;
import org.apache.commons.text.similarity.CosineDistance;

public class ApacheTextComponentSimilarity implements ComponentSimilarity {

    private final CosineDistance distance;

    public ApacheTextComponentSimilarity(CosineDistance distance) {
        this.distance = distance;
    }

    @Override
    public Double similarity(MethodBean method, MethodBean otherMethod) {
        return textualSimilarity(method.getTextContent(), otherMethod.getTextContent());
    }

    @Override
    public Double similarity(MethodBean method, ClassBean clazz) {
        return textualSimilarity(method.getTextContent(),clazz.getTextContent());
    }

    private double textualSimilarity(String textContent, String otherTextContent) {
        double value = 1 - distance.apply(textContent, otherTextContent);
        return twoDecimalDigitsApproximation(value);
    }

    private double twoDecimalDigitsApproximation(double value) {
        return Math.floor(value * 100) / 100;
    }
}
