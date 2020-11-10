package it.unisa.jDECAF_ML.taco.detectors;

import it.unisa.jDECAF_ML.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.parser.bean.MethodBean;
import it.unisa.jDECAF_ML.parser.bean.MethodBlockBean;
import it.unisa.jDECAF_ML.taco.normalizer.IRNormalizer;
import org.apache.commons.text.similarity.CosineDistance;

public class ApacheTextComponentSimilarity implements ComponentSimilarity {

    private final CosineDistance distance;
    private final IRNormalizer normalizer;

    public ApacheTextComponentSimilarity(CosineDistance distance, IRNormalizer normalizer) {
        this.distance = distance;
        this.normalizer = normalizer;
    }

    @Override
    public Double similarity(MethodBean method, MethodBean otherMethod) {
        return textualSimilarity(method.getTextContent(), otherMethod.getTextContent());
    }

    @Override
    public Double similarity(MethodBean method, ClassBean clazz) {
        return textualSimilarity(method.getTextContent(),clazz.getTextContent());
    }

    @Override
    public Double similarity(MethodBlockBean blockBean, MethodBlockBean otherBlockBean) {
        return textualSimilarity(blockBean.getContent(),otherBlockBean.getContent());
    }

    private double textualSimilarity(String textContent, String otherTextContent) {
        double value = 1 - distance.apply(normalizer.normalizeText(textContent), normalizer.normalizeText(otherTextContent));
        return twoDecimalDigitsApproximation(value);
    }

    private double twoDecimalDigitsApproximation(double value) {
        return Math.floor(value * 100) / 100;
    }
}
