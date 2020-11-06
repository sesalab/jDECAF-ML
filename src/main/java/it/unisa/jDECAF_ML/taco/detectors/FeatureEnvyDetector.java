package it.unisa.jDECAF_ML.taco.detectors;

import it.unisa.jDECAF_ML.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.parser.bean.MethodBean;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class FeatureEnvyDetector implements SmellDetector {

    private final List<ClassBean> projectClasses;
    private final ComponentSimilarity componentSimilarity;

    public FeatureEnvyDetector(List<ClassBean> projectClasses, ComponentSimilarity componentSimilarity) {
        this.projectClasses = projectClasses;
        this.componentSimilarity = componentSimilarity;
    }

    @Override
    public List<AnalyzedComponent> detectSmells() {
        List<AnalyzedComponent> result = new LinkedList<>();
        for(ClassBean clazz: projectClasses) {
            for (MethodBean method: clazz.getMethods()){
                result.add(analyzeMethod(method));
            }
        }
        return result;
    }

    private AnalyzedComponent analyzeMethod(MethodBean method) {
        ClassBean closestClass = findMostSimilarClass(method);
        Double smellinessProbability = similarity(method,closestClass) - similarity(method,method.getBelongingClass());
        return new AnalyzedComponent(method,smellinessProbability);
    }

    private Double similarity(MethodBean method, ClassBean clazz) {
        return componentSimilarity.similarity(method,clazz);
    }

    private ClassBean findMostSimilarClass(MethodBean method) {
        Optional<ClassBean> closestClassOptional = projectClasses
                .stream()
                .max(Comparator.comparing(classBean -> similarity(method, classBean)));
        return closestClassOptional.get();
    }
}
