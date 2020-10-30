package it.unisa.jDECAF_ML.decor;

import it.unisa.jDECAF_ML.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.parser.bean.ComponentBean;
import it.unisa.jDECAF_ML.parser.bean.MethodBean;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LazyClassRule implements DetectionRule {

    private List<ClassBean> candidateLazyClasses;
    private List<ClassBean> projectClasses;
    private HashMap<ClassBean, Integer> classFanOut;
    private HashMap<ClassBean, Integer> classFanIn;

    public LazyClassRule() {
        System.out.println();
        this.classFanIn = new HashMap<ClassBean, Integer>();
        this.classFanOut = new HashMap<ClassBean, Integer>();
    }

    public void setProjectClasses(List<ClassBean> classes) {
        this.projectClasses = classes;
        this.candidateLazyClasses = searchLazyClasses();
    }

    public boolean isLazyClass(ClassBean cb) {
        return this.candidateLazyClasses.contains(cb);
    }

    public List<ClassBean> searchLazyClasses() {
        candidateLazyClasses = new Vector<ClassBean>();
        Vector<ClassBean> candidatefanOut = new Vector<ClassBean>();
        Vector<ClassBean> candidatefanIn = new Vector<ClassBean>();

        for (ClassBean type : this.projectClasses) {
            if (!type.getName().contains("Stemmer")) {
                int fanOut = this.fanOut(type);
                this.classFanOut.put(type, fanOut);
                int fanIn = this.fanIn(type, this.projectClasses);
                this.classFanIn.put(type, fanIn);
            }
        }

        int minFanOut = this.min(classFanOut);
        int minFanIn = this.min(classFanIn);
        if ((minFanIn != -1) && (minFanOut != -1)) {
            double lowerHingeFanOut = this.lowerHinge(classFanOut);
            double lowerHingeFanIn = this.lowerHinge(classFanIn);

            for (Map.Entry<ClassBean, Integer> valueOut : classFanOut.entrySet()) {
                if ((valueOut.getValue() < lowerHingeFanOut) && (valueOut.getValue() > minFanOut)) {
                    candidatefanOut.add(valueOut.getKey());
                }
            }

            for (Map.Entry<ClassBean, Integer> valueIn : classFanIn.entrySet()) {
                if ((valueIn.getValue() < lowerHingeFanIn) && (valueIn.getValue() > minFanIn)) {
                    candidatefanIn.add(valueIn.getKey());
                }
            }

            for (ClassBean cb : candidatefanOut) {
                for (ClassBean cb2 : candidatefanIn) {
                    if (cb2.getName().equals(cb.getName())) {
                        candidateLazyClasses.add(cb);
                    }
                }
            }
        }
        return candidateLazyClasses;
    }

    private int fanOut(ClassBean pType) {
        int counter = 0;
        try {
            for (MethodBean m : pType.getMethods()) {
                if (!m.getName().equals("cloneDate")) {
                    if (m.getMethodCalls().size() != 0) {
                        for (@SuppressWarnings("unused") MethodBean mi : m.getMethodCalls()) {
                            counter++;
                        }
                    }
                }
            }
        } catch (Exception e) {
            return counter;
        }
        return counter;
    }

    private int fanIn(ClassBean pType, List<ClassBean> pOthers) {
        int counter = 0;
        Matcher matcher;
        for (ClassBean type : pOthers) {
            String code = "";
            if (type != pType) {
                Pattern pattern = Pattern.compile(pType.getName());
                try {
                    code = type.getTextContent();
                    matcher = pattern.matcher(code);
                    while (matcher.find()) {
                        counter++;
                    }
                } catch (Exception e) {
                    return counter;
                }
            }
        }

        return counter;
    }

    private int min(HashMap<ClassBean, Integer> pMap) {
        if (pMap.values().size() > 0) {
            return Collections.min(pMap.values());
        } else {
            return -1;
        }
    }

    private Double lowerHinge(HashMap<ClassBean, Integer> pMap) {
        ArrayList<Integer> values = new ArrayList<Integer>();
        ArrayList<Integer> valuesForLowerHinge = new ArrayList<Integer>();

        for (Integer i : pMap.values()) {
            values.add(i);
        }

        int min = this.min(pMap);
        double median = this.median(values);

        for (Map.Entry<ClassBean, Integer> value : pMap.entrySet()) {
            if ((value.getValue() < median) && (value.getValue() > min)) {
                valuesForLowerHinge.add(value.getValue());
            }
        }

        return this.median(valuesForLowerHinge);
    }

    private Double median(ArrayList<Integer> values) {
        Collections.sort(values);
        if ((values.size() % 2 == 0)) {
            try {
                return (double) ((values.get((values.size() / 2) - 1) + (values.get(values.size() / 2)))) / 2;
            } catch (Exception e) {
                return 0.0;
            }
        } else {
            return (double) (values.get(values.size() / 2));
        }
    }

    @Override
    public boolean isSmelly(ComponentBean cb) {
        return isLazyClass((ClassBean) cb);
    }

}
