/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.jDECAF_ML.metrics.classmetrics;

import it.unisa.jDECAF_ML.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.parser.bean.MethodBean;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author fabiano
 */
public class PMMM implements ClassMetric {

    @Override
    public String getName() {
        return "PMMM";
    }

    @Override
    public double evaluate(ClassBean cb, ArrayList<ClassBean> Sys) {
        double counterDelegation = 0.0;
        for (MethodBean m : cb.getMethods()) {
            if (this.isDelegation(m)) {
                counterDelegation++;
            }
        }
        return (cb.getMethods().size() == 0)? 0:(counterDelegation / cb.getMethods().size());

    }

    private boolean isDelegation(MethodBean pMethodToAnalyze) {

        if (this.LOC(pMethodToAnalyze) < 10) {

            if (pMethodToAnalyze.getMethodCalls().size() == 1) {
                return true;
            }
        }
        return false;
    }

    private int LOC(MethodBean pMethod) {
        int loc = 0;
        String source = pMethod.getTextContent();
        String regex = "[\n]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            loc++;
        }
        return loc + 1;
    }
}
