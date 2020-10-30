/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.jDECAF_ML.smell;

import it.unisa.jDECAF_ML.metrics.Metric;
import it.unisa.jDECAF_ML.metrics.classmetrics.MaxFanIn;
import it.unisa.jDECAF_ML.metrics.classmetrics.MaxFanOut;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fably
 */
public class InappropriateIntimacy extends ClassSmell{

    public InappropriateIntimacy(String name, String oraclePath) {
        super(name, oraclePath);
    }

    @Override
    public List<Metric> getMetrics() {
        List<Metric> toReturn = new ArrayList<Metric>();
        toReturn.add(new MaxFanIn());
        toReturn.add(new MaxFanOut());
        return toReturn;
    }
    
}
