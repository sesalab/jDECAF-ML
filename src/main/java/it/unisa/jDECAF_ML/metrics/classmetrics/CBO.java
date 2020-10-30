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
 * @author fabiano
 */
public class CBO implements ClassMetric {

    @Override
    public String getName() {
        return "CBO";
    }

    @Override
    public double evaluate(ClassBean cb, ArrayList<ClassBean> Sys) {
        return CKMetrics.getCBO(cb);
    }

}
