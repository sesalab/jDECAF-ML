/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.jDECAF_ML.metrics.classmetrics;

import it.unisa.jDECAF_ML.metrics.CKMetrics;
import it.unisa.jDECAF_ML.metrics.parser.bean.ClassBean;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fabiano
 */
public class DIT implements ClassMetric{

    @Override
    public String getName() {
        return "DIT";
    }

    @Override
    public double evaluate(ClassBean cb, ArrayList<ClassBean> System) {
        return CKMetrics.getDIT(cb, System, 1);
        
    }
}
