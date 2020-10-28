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
import it.unisa.jDECAF_ML.metrics.classmetrics.FanIn;
import it.unisa.jDECAF_ML.metrics.classmetrics.FanOut;
import it.unisa.jDECAF_ML.metrics.classmetrics.LOC;
import it.unisa.jDECAF_ML.metrics.classmetrics.LOCNAMM;
import it.unisa.jDECAF_ML.metrics.classmetrics.NOC;
import it.unisa.jDECAF_ML.metrics.classmetrics.NOM;
import it.unisa.jDECAF_ML.metrics.classmetrics.NOMNAMM;
import it.unisa.jDECAF_ML.metrics.classmetrics.WMC;
import it.unisa.jDECAF_ML.metrics.classmetrics.WMCNAMM;
import it.unisa.jDECAF_ML.metrics.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.metrics.parser.bean.ComponentBean;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fably
 */
public class SpeculativeGenerality extends ClassSmell{

    public SpeculativeGenerality(String name, String oraclePath) {
        super(name, oraclePath);
    }

    @Override
    public List<Metric> getMetrics() {
        List<Metric> toReturn = new ArrayList<Metric>();
        toReturn.add(new NOC());
        return toReturn;
    }
    
}
