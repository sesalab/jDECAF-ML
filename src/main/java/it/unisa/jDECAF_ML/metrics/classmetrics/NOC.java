/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.jDECAF_ML.metrics.classmetrics;

import it.unisa.jDECAF_ML.metrics.CKMetrics;
import it.unisa.jDECAF_ML.metrics.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.metrics.parser.bean.MethodBean;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fabiano
 */
public class NOC implements ClassMetric {

    @Override
    public String getName() {
        return "NOC";
    }

    @Override
    public double evaluate(ClassBean cb, ArrayList<ClassBean> Sys) {
        return CKMetrics.getNOC(cb,Sys);
    }

}
