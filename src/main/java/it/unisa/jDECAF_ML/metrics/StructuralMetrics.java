package it.unisa.jDECAF_ML.metrics;


import it.unisa.jDECAF_ML.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.parser.bean.InstanceVariableBean;
import it.unisa.jDECAF_ML.parser.bean.MethodBean;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class StructuralMetrics {

    public static int getLOC(ClassBean cb) {
        return cb.getLOC();
    }

    public static int getWMC(ClassBean cb) {

        int WMC = 0;

        ArrayList<MethodBean> methods = (ArrayList<MethodBean>) cb.getMethods();
        for (MethodBean m : methods) {
            WMC += getMcCabeCycloComplexity(m);
        }

        return WMC;

    }

    private static int getDIT(ClassBean cb, ArrayList<ClassBean> System, int inizialization) {
        int DIT = inizialization;
        if (cb.getSuperclass() != null) {
            DIT++;
            for (ClassBean cb2 : System) {
                if (cb2.getName().equals(cb.getSuperclass())) {
                    getDIT(cb2, System, DIT);
                }
            }
        } else {
            return DIT;
        }

        return DIT;
    }

    public static int getNOC(ClassBean cb, ArrayList<ClassBean> System) {
        int NOC = 0;
        for (ClassBean c : System) {
            if (c.getSuperclass() != null && c.getSuperclass().equals(cb.getName())) {
                NOC++;
            }
        }

        return NOC;
    }

    public static int getRFC(ClassBean cb) {
        int RFC = 0;
        ArrayList<MethodBean> methods = (ArrayList<MethodBean>) cb.getMethods();
        for (MethodBean m : methods) {
            RFC += m.getMethodCalls().size();
        }
        return RFC;
    }

    public static int getCBO(ClassBean cb) {
        ArrayList<String> imports = (ArrayList<String>) cb.getImports();
        return imports.size();
    }

    public static int getLCOM(ClassBean cb) {
        int share = 0;
        int notShare = 0;
        ArrayList<MethodBean> methods = (ArrayList<MethodBean>) cb.getMethods();
        for (int i = 0; i < methods.size(); i++) {
            for (int j = i + 1; j < methods.size(); j++) {
                if (shareAnInstanceVariable(methods.get(i), methods.get(j))) {
                    share++;
                } else {
                    notShare++;
                }
            }
        }

        if (share > notShare) {
            return 0;
        } else {
            return notShare - share;
        }
    }

    public static int getNOM(ClassBean cb) {
        return cb.getMethods().size();
    }

    // Number of operations added by a subclass
    public static int getNOA(ClassBean cb, ArrayList<ClassBean> System) {

        int NOA = 0;

        for (ClassBean c : System) {
            if (c.getName().equals(cb.getSuperclass())) {
                ArrayList<MethodBean> subClassMethods = (ArrayList<MethodBean>) cb
                        .getMethods();
                ArrayList<MethodBean> superClassMethods = (ArrayList<MethodBean>) c
                        .getMethods();
                for (MethodBean m : subClassMethods) {
                    if (!superClassMethods.contains(m)) {
                        NOA++;
                    }
                }
                break;
            }
        }

        return NOA;
    }

    // Number of operations overridden by a subclass
    public static int getNOO(ClassBean cb, ArrayList<ClassBean> System) {

        int NOO = 0;

        if (cb.getSuperclass() != null) {

            for (ClassBean c : System) {
                if (c.getName().equals(cb.getSuperclass())) {
                    ArrayList<MethodBean> subClassMethods = (ArrayList<MethodBean>) cb
                            .getMethods();
                    ArrayList<MethodBean> superClassMethods = (ArrayList<MethodBean>) c
                            .getMethods();
                    for (MethodBean m : subClassMethods) {
                        if (superClassMethods.contains(m)) {
                            NOO++;
                        }
                    }
                    break;
                }
            }
        }

        return NOO;
    }

    private static int getMcCabeCycloComplexity(MethodBean mb) {

        int mcCabe = 0;
        String code = mb.getTextContent();

        String regex = "return";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(code);

        if (matcher.find()) {
            mcCabe++;
        }

        regex = "if";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(code);

        if (matcher.find()) {
            mcCabe++;
        }

        regex = "else";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(code);

        if (matcher.find()) {
            mcCabe++;
        }

        regex = "case";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(code);

        if (matcher.find()) {
            mcCabe++;
        }

        regex = "for";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(code);

        if (matcher.find()) {
            mcCabe++;
        }

        regex = "while";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(code);

        if (matcher.find()) {
            mcCabe++;
        }

        regex = "break";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(code);

        if (matcher.find()) {
            mcCabe++;
        }

        regex = "&&";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(code);

        if (matcher.find()) {
            mcCabe++;
        }

        regex = "||";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(code);

        if (matcher.find()) {
            mcCabe++;
        }

        regex = "catch";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(code);

        if (matcher.find()) {
            mcCabe++;
        }

        regex = "throw";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(code);

        if (matcher.find()) {
            mcCabe++;
        }

        return mcCabe;
    }

    private static boolean shareAnInstanceVariable(MethodBean m1, MethodBean m2) {

        ArrayList<InstanceVariableBean> m1Variables = (ArrayList<InstanceVariableBean>) m1
                .getUsedInstanceVariables();
        ArrayList<InstanceVariableBean> m2Variables = (ArrayList<InstanceVariableBean>) m2
                .getUsedInstanceVariables();

        for (InstanceVariableBean i : m1Variables) {
            if (m2Variables.contains(i)) {
                return true;
            }
        }

        return false;

    }
}
