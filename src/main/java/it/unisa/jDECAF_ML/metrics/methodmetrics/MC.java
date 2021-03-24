/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.jDECAF_ML.metrics.methodmetrics;

import it.unisa.jDECAF_ML.parser.bean.MethodBean;

/**
 *
 * @author fabiano
 */
public class MC implements MethodMetric{

    @Override
    public String getName() {
        return "MC";
    }

    @Override
    public double evaluate(MethodBean mb) {
        return mb.getMethodCalls().size();
    }
}
