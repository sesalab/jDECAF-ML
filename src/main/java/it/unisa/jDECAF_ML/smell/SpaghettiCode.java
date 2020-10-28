/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.jDECAF_ML.smell;

import it.unisa.jDECAF_ML.metrics.Metric;
import it.unisa.jDECAF_ML.metrics.classmetrics.CBO;
import it.unisa.jDECAF_ML.metrics.classmetrics.ClassMetric;
import it.unisa.jDECAF_ML.metrics.classmetrics.DIT;
import it.unisa.jDECAF_ML.metrics.classmetrics.ELOC;
import it.unisa.jDECAF_ML.metrics.classmetrics.LOC;
import it.unisa.jDECAF_ML.metrics.classmetrics.NMNOPARAM;
import it.unisa.jDECAF_ML.metrics.classmetrics.NOM;
import it.unisa.jDECAF_ML.metrics.classmetrics.WLOCNAMM;
import it.unisa.jDECAF_ML.metrics.classmetrics.WMC;
import it.unisa.jDECAF_ML.metrics.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.metrics.parser.bean.ComponentBean;
import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fabiano
 */
public class SpaghettiCode extends ClassSmell {

    public SpaghettiCode(String name, String oraclePath) {
        super(name, oraclePath);
    }

    @Override
    public List<Metric> getMetrics() {
        List<Metric> toReturn = new ArrayList<Metric>();        
        toReturn.add(new ELOC());
        toReturn.add(new NMNOPARAM());
        return toReturn;
    }


}
