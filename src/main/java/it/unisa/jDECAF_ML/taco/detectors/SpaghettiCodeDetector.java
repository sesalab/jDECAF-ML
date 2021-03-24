package it.unisa.jDECAF_ML.taco.detectors;

import it.unisa.jDECAF_ML.parser.bean.ClassBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SpaghettiCodeDetector implements CodeSmellDetector {

    private final LongMethodDetector longMethodDetector;

    public SpaghettiCodeDetector(LongMethodDetector longMethodDetector) {

        this.longMethodDetector = longMethodDetector;
    }


    @Override
    public List<AnalyzedComponent> detectSmells(List<ClassBean> classesUnderAnalysis) {
        return classesUnderAnalysis.stream().map(this::analyzeClass).collect(Collectors.toList());
    }

    private AnalyzedComponent analyzeClass(ClassBean clazz) {

        List<AnalyzedComponent> longMethodsCandidates = longMethodDetector.detectSmells(Collections.singletonList(clazz));
        List<Double> smellinessProbabilities = longMethodsCandidates.stream().map(AnalyzedComponent::getSmellinessProbability).collect(Collectors.toList());
        List<List<Double>> probabilitiesPowerSet = getPowerSet(smellinessProbabilities);

        double result = 0;
        for (List<Double> subset : probabilitiesPowerSet){
            Double probabilityOfIntersection = subset.stream().reduce(1.0, ((aDouble, aDouble2) -> aDouble * aDouble2));
            result += subset.size() % 2 == 0 ? -probabilityOfIntersection : probabilityOfIntersection;
        }

        return new AnalyzedComponent(clazz,result,SmellType.SPAGHETTI_CODE);
    }

    private List<List<Double>> getPowerSet(List<Double> itemList) {
        List<List<Double>> ps = new ArrayList<>();
        ps.add(new ArrayList<Double>()); // add the empty set

        // for every item in the original list
        for (Double i : itemList) {
            List<List<Double>> newPs = new ArrayList<>();

            for (List<Double> subset : ps) {
                // copy all of the current powerset's subsets
                newPs.add(subset);

                // plus the subsets appended with the current item
                List<Double> newSubset = new ArrayList<>(subset);
                newSubset.add(i);
                newPs.add(newSubset);
            }

            // powerset is now powerset of list.subList(0, list.indexOf(item)+1)
            ps = newPs;
        }
        ps.remove(0); // remove the emptyset
        return ps;
    }
}
