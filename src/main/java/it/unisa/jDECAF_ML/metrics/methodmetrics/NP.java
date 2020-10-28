/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.jDECAF_ML.metrics.methodmetrics;

import it.unisa.jDECAF_ML.metrics.classmetrics.*;
import it.unisa.jDECAF_ML.metrics.CKMetrics;
import it.unisa.jDECAF_ML.metrics.parser.bean.MethodBean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author fabiano
 */
public class NP implements MethodMetric{

    @Override
    public String getName() {
        return "NP";
    }

    @Override
    public double evaluate(MethodBean mb) {
        return mb.getParameters().size();
    }
}
