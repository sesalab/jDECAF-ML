package it.unisa.jDECAF_ML.taco.detectors;

import it.unisa.jDECAF_ML.parser.bean.ClassBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ComplexClassDetector implements SmellDetector {

    private final List<ClassBean> projectClasses;

    public ComplexClassDetector(List<ClassBean> projectClasses) {
        this.projectClasses = projectClasses;
    }

    @Override
    public List<AnalyzedComponent> detectSmells() {
        return projectClasses.stream().map(this::analyzeClass).collect(Collectors.toList());
    }

    private AnalyzedComponent analyzeClass(ClassBean clazz) {
        return null;
    }

    private  Double calculateShannonEntropy(List<String> values) {
        Map<String, Integer> map = new HashMap<>();
        // count the occurrences of each value
        for (String sequence : values) {
            if (!map.containsKey(sequence)) {
                map.put(sequence, 0);
            }
            map.put(sequence, map.get(sequence) + 1);
        }

        // calculate the entropy
        Double result = 0.0;
        for (String sequence : map.keySet()) {
            Double frequency = (double) map.get(sequence) / values.size();
            result -= frequency * (Math.log(frequency) / Math.log(2));
        }

        return result;
    }
}
