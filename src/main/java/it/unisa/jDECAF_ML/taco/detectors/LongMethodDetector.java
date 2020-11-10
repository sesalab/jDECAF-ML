package it.unisa.jDECAF_ML.taco.detectors;

import it.unisa.jDECAF_ML.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.parser.bean.MethodBean;
import it.unisa.jDECAF_ML.parser.bean.MethodBlockBean;

import java.util.List;
import java.util.stream.Collectors;

public class LongMethodDetector extends AbstractDetector {

    public LongMethodDetector(List<ClassBean> projectClasses, ComponentSimilarity similarity) {
        super(projectClasses,similarity);
    }

    @Override
    public List<AnalyzedComponent> detectSmells() {
        List<MethodBean> allMethods = projectClasses.stream().flatMap(clazz -> clazz.getMethods().stream()).collect(Collectors.toList());
        return allMethods.stream().map(this::analyzeMethod).collect(Collectors.toList());
    }

    private AnalyzedComponent analyzeMethod(MethodBean methodBean) {
        Double methodCohesion = computeCohesion(methodBean);
        return new AnalyzedComponent(methodBean,1-methodCohesion, SmellType.LONG_METHOD);
    }

    private Double computeCohesion(MethodBean methodBean) {
        Double methodsSimilarities = 0.0;
        int comparisons = 0;

        for (MethodBlockBean currentBlock: methodBean.getBlocks()){
            for (MethodBlockBean otherBlock: methodBean.getBlocks()){
                if(!currentBlock.equals(otherBlock)){
                    methodsSimilarities += componentSimilarity.similarity(currentBlock,otherBlock);
                    comparisons++;
                }
            }
        }
        return comparisons == 0 ? 1 : methodsSimilarities/comparisons;
    }
}
