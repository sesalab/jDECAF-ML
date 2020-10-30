/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.jDECAF_ML.smell;

import it.unisa.jDECAF_ML.metrics.Metric;
import it.unisa.jDECAF_ML.metrics.classmetrics.NOPA;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fably
 */
public class ClassDataShouldBePrivate extends ClassSmell{

    public ClassDataShouldBePrivate(String name, String oraclePath) {
        super(name, oraclePath);
    }

    @Override
    public List<Metric> getMetrics() {
        List<Metric> toReturn = new ArrayList<Metric>();
        toReturn.add(new NOPA());
        return toReturn;
    }
    
}
