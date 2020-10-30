/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.jDECAF_ML.smell;

import it.unisa.jDECAF_ML.metrics.Metric;
import it.unisa.jDECAF_ML.metrics.classmetrics.ELOC;
import it.unisa.jDECAF_ML.metrics.classmetrics.NMNOPARAM;

import java.util.ArrayList;
import java.util.List;

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
