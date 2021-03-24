/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.jDECAF_ML.smell;

import it.unisa.jDECAF_ML.metrics.Metric;
import it.unisa.jDECAF_ML.metrics.methodmetrics.ATFD;
import it.unisa.jDECAF_ML.metrics.classmetrics.LOC;
import it.unisa.jDECAF_ML.metrics.classmetrics.NMNOPARAM;
import it.unisa.jDECAF_ML.metrics.methodmetrics.LOC_METHOD;
import it.unisa.jDECAF_ML.metrics.methodmetrics.MC;
import it.unisa.jDECAF_ML.metrics.methodmetrics.NP;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fably
 */
public class LongMethod extends MethodSmell{

    public LongMethod(String name, String oraclePath) {
        super(name, oraclePath);
    }

    @Override
    public List<Metric> getMetrics() {
        List<Metric> toReturn = new ArrayList<Metric>();        
        toReturn.add(new LOC_METHOD());
        toReturn.add(new NP());
        return toReturn;
    }
    
}
