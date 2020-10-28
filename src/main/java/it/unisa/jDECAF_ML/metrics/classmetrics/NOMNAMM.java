/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.jDECAF_ML.metrics.classmetrics;

import it.unisa.jDECAF_ML.metrics.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.metrics.parser.bean.MethodBean;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fabiano
 */
public class NOMNAMM implements ClassMetric{

    @Override
    public String getName() {
        return "NOMNAMM";
    }

    @Override
    public double evaluate(ClassBean cb, ArrayList<ClassBean> System) {
        double NOMNAMM = cb.getMethods().size();
        for (MethodBean mb : cb.getMethods()){
            if (mb.isAccessor() || mb.isMutator())
                NOMNAMM--;
        }
        return NOMNAMM;
    }
    
    
}
