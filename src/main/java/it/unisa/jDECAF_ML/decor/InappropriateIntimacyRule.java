package it.unisa.jDECAF_ML.decor;

import it.unisa.jDECAF_ML.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.parser.bean.ComponentBean;
import it.unisa.jDECAF_ML.parser.bean.MethodBean;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InappropriateIntimacyRule implements DetectionRule {

    private List<ClassBean> candidateInappropriateIntimacies;
    private List<ClassBean> projectClasses;
    private HashMap<Vector<ClassBean>, Vector<Integer>> classCoupling;
    private HashMap<ClassBean, Integer> classFanOut;
    private HashMap<ClassBean, Integer> classFanIn;
    int i = 1;
    int counterSecondInFirst = 0;
    int counterFirstInSecond = 0;

    public InappropriateIntimacyRule() {
        System.out.println();
        this.classCoupling = new HashMap<Vector<ClassBean>, Vector<Integer>>();
        this.classFanIn = new HashMap<ClassBean, Integer>();
        this.classFanOut = new HashMap<ClassBean, Integer>();
    }

    public void setProjectClasses(List<ClassBean> classes) {
        this.projectClasses = classes;
        this.candidateInappropriateIntimacies = searchInappropriateIntimacies();
    }

    public boolean isInappropriateIntimacy(ClassBean cb) {
        return this.candidateInappropriateIntimacies.contains(cb);
    }

    public List<ClassBean> searchInappropriateIntimacies() {
        candidateInappropriateIntimacies = new ArrayList<ClassBean>();
        Vector<ClassBean> analyzedClasses;

        for (ClassBean type : this.projectClasses) {
            for (ClassBean secondType : this.projectClasses) {
                analyzedClasses = new Vector<ClassBean>();
                analyzedClasses.add(type);
                analyzedClasses.add(secondType);
                Vector<ClassBean> test = new Vector<ClassBean>();
                test.add(secondType);
                test.add(type);
                if ((!this.classCoupling.containsKey(analyzedClasses)) && (!this.classCoupling.containsKey(test) && (type != secondType))) {
                    Vector<Integer> fanIn = this.fanIn(type, secondType);
                    this.classCoupling.put(analyzedClasses, fanIn);
                }
            }
        }

        int maxFanIn = this.max();
        if (maxFanIn != -1) {
            double upperHinge = this.upperHinge();

            for (Map.Entry<Vector<ClassBean>, Vector<Integer>> value : this.classCoupling.entrySet()) {
                if ((value.getValue().get(0) > upperHinge) && (value.getValue().get(0) < maxFanIn)) {
                    Vector<ClassBean> couple = new Vector<ClassBean>();
                    if (value.getValue().get(0).intValue() > 150) {
                        for (ClassBean t : value.getKey()) {
                            candidateInappropriateIntimacies.add(t);
                        }

                    }
                    i++;
                }
            }
        }
        return candidateInappropriateIntimacies;
    }

    private Vector<Integer> fanIn(ClassBean pType, ClassBean pSecondType) {
        Vector<Integer> results = new Vector<Integer>();
        i++;
        Matcher matcher;
        String codeSecondType = "";
        String codeFirstType = "";
        if (pType != pSecondType) {
            // Cerco una dichiarazione di pType in pSecondType
            Pattern pattern = Pattern.compile("[^A-Za-z0-9]" + pType.getName() + "[^A-Za-z0-9]");
            codeSecondType = pSecondType.getTextContent();
            matcher = pattern.matcher(codeSecondType);
            while (matcher.find()) {
                counterFirstInSecond++;
            }

            // Cerco una dichiarazione di pSecondType in pType
            Pattern searchSecondType = Pattern.compile("[^A-Za-z0-9]" + pSecondType.getName() + "[^A-Za-z0-9]");
            codeFirstType = pType.getTextContent();
            matcher = searchSecondType.matcher(codeFirstType);
            while (matcher.find()) {
                counterSecondInFirst++;
            }

            if (counterSecondInFirst > 0 || counterFirstInSecond > 0) {
                codeFirstType = pType.getTextContent();
                for (MethodBean method : pSecondType.getMethods()) {
                    Pattern methodCall = Pattern.compile(method.getName());
                    Matcher matcherCall = methodCall.matcher(codeFirstType);
                    while (matcherCall.find()) {
                        counterSecondInFirst++;
                    }
                }

                for (MethodBean method : pType.getMethods()) {
                    Pattern methodCall = Pattern.compile(method.getName());
                    Matcher matcherCall = methodCall.matcher(codeSecondType);
                    while (matcherCall.find()) {
                        counterFirstInSecond++;
                    }
                }
            }
        }

        results.add(counterFirstInSecond + counterSecondInFirst);
        results.add(counterFirstInSecond);
        results.add(counterSecondInFirst);
        counterFirstInSecond = 0;
        counterSecondInFirst = 0;
        return results;
    }

    private int max() {
        Vector<Integer> valuesForAnalysis = new Vector<Integer>();
        ArrayList<Integer> values = new ArrayList<Integer>();

        for (Vector<Integer> v : this.classCoupling.values()) {
            valuesForAnalysis.add(v.get(0).intValue());
        }

        for (Integer i : valuesForAnalysis) {
            values.add(i);
        }

        if (values.size() > 0) {
            return Collections.max(values);
        } else {
            return -1;
        }
    }

    private Double upperHinge() {
        ArrayList<Integer> values = new ArrayList<Integer>();
        ArrayList<Integer> valuesForUpperHinge = new ArrayList<Integer>();

        Vector<Integer> valuesForAnalysis = new Vector<Integer>();

        for (Vector<Integer> v : this.classCoupling.values()) {
            valuesForAnalysis.add(v.get(0).intValue());
        }

        for (Integer i : valuesForAnalysis) {
            values.add(i);
        }

        int max = this.max();
        double median = this.median(values);

        for (Integer value : valuesForAnalysis) {
            if ((value > median) && (value < max)) {
                valuesForUpperHinge.add(value);
            }
        }

        return this.median(valuesForUpperHinge);
    }

    private Double median(ArrayList<Integer> values) {
        Collections.sort(values);

        if ((values.size() % 2 == 0)) {
            return (double) ((values.get((values.size() / 2) - 1) + (values.get(values.size() / 2)))) / 2;
        } else {
            return (double) (values.get(values.size() / 2));
        }
    }

    @Override
    public boolean isSmelly(ComponentBean cb) {
        return isInappropriateIntimacy((ClassBean) cb);
    }

}
