/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.jDECAF_ML.metrics.classmetrics;

import it.unisa.jDECAF_ML.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.parser.bean.MethodBean;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author fabiano
 */
public class WLOCNAMM implements ClassMetric{

    @Override
    public String getName() {
        return "WLOCNAMM";
    }

    @Override
    public double evaluate(ClassBean cb, ArrayList<ClassBean> System) {
        double WLOCNAMM = 0;
        int NOMNAMM = 0;
        Collection<MethodBean> classMehtods = cb.getMethods();
        for (MethodBean mb: classMehtods){
            if (!mb.isAccessor() && !mb.isMutator()){
                NOMNAMM++;
                WLOCNAMM += mb.getLOC();
            }
        }
        WLOCNAMM /= NOMNAMM;
        return WLOCNAMM;
    }
    
}
