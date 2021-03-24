/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.jDECAF_ML.metrics.classmetrics;

import it.unisa.jDECAF_ML.metrics.CKMetrics;
import it.unisa.jDECAF_ML.parser.bean.ClassBean;

import java.util.ArrayList;

/**
 *
 * @author fably
 */
public class LCOM implements ClassMetric {

    @Override
    public String getName() {
        return "LCOM";
    }

    @Override
    public double evaluate(ClassBean cb, ArrayList<ClassBean> Sys) {
        return CKMetrics.getLCOM(cb);
    }

}
