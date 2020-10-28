/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.jDECAF_ML.smell;

import it.unisa.jDECAF_ML.metrics.Metric;
import it.unisa.jDECAF_ML.metrics.classmetrics.ClassMetric;
import it.unisa.jDECAF_ML.metrics.parser.bean.ComponentBean;
import java.util.List;

/**
 *
 * @author fabiano
 */
public abstract class CodeSmell {
    protected String name;
    protected String oraclePath;
    
    public CodeSmell(String name, String oraclePath){
        this.name = name;
        this.oraclePath = oraclePath;
    }

    public String getName(){
        return name;
    }
    
    public String getOraclePath(){
        return oraclePath;
    }
    
    abstract public boolean affectsComponent(ComponentBean cb);
    
    public abstract List<Metric> getMetrics();
}
