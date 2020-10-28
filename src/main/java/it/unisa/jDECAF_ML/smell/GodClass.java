/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.jDECAF_ML.smell;

import it.unisa.jDECAF_ML.metrics.Metric;
import it.unisa.jDECAF_ML.metrics.methodmetrics.ATFD;
import it.unisa.jDECAF_ML.metrics.classmetrics.CBO;
import it.unisa.jDECAF_ML.metrics.classmetrics.ClassMetric;
import it.unisa.jDECAF_ML.metrics.classmetrics.DIT;
import it.unisa.jDECAF_ML.metrics.classmetrics.ELOC;
import it.unisa.jDECAF_ML.metrics.classmetrics.LCOM;
import it.unisa.jDECAF_ML.metrics.classmetrics.LCOM2;
import it.unisa.jDECAF_ML.metrics.classmetrics.LOC;
import it.unisa.jDECAF_ML.metrics.classmetrics.LOCNAMM;
import it.unisa.jDECAF_ML.metrics.classmetrics.NOA;
import it.unisa.jDECAF_ML.metrics.classmetrics.NOM;
import it.unisa.jDECAF_ML.metrics.classmetrics.NOMNAMM;
import it.unisa.jDECAF_ML.metrics.classmetrics.WMC;
import it.unisa.jDECAF_ML.metrics.classmetrics.WMCNAMM;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fably
 */
public class GodClass extends ClassSmell{

    public GodClass(String name, String oraclePath) {
        super(name, oraclePath);
    }

    @Override
    public List<Metric> getMetrics() {
        List<Metric> toReturn = new ArrayList<Metric>();
        toReturn.add(new WMC());
        toReturn.add(new NOA());
        toReturn.add(new LCOM2());
        toReturn.add(new ELOC());
        return toReturn;
    }
    
}
