/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.jDECAF_ML.metrics.classmetrics;

import it.unisa.jDECAF_ML.parser.bean.ClassBean;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author fabiano
 */
public class MaxFanOut implements ClassMetric {

    @Override
    public String getName() {
        return "MaxFanOut";
    }

    @Override
    public double evaluate(ClassBean cb, ArrayList<ClassBean> Sys) {
        return maxFanOut(cb, Sys);
    }

    private int maxFanOut(ClassBean pType, List<ClassBean> pOthers) {
        int counter = 0;
        int max = 0;
        Matcher matcher;
        String code = pType.getTextContent();
        for (ClassBean type : pOthers) {
            if (type != pType) {
                Pattern pattern = Pattern.compile(type.getName());
                try {
                    matcher = pattern.matcher(code);
                    while (matcher.find()) {
                        counter++;
                    }
                    max = (counter > max) ? counter : max;
                } catch (Exception e) {
                    return max;
                }
            }
        }

        return max;
    }

}
