/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.jDECAF_ML.metrics.classmetrics;

import it.unisa.jDECAF_ML.metrics.CKMetrics;
import it.unisa.jDECAF_ML.metrics.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.metrics.parser.bean.MethodBean;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author fabiano
 */
public class FanOut implements ClassMetric {

    @Override
    public String getName() {
        return "FanIn";
    }

    @Override
    public double evaluate(ClassBean cb, ArrayList<ClassBean> Sys) {
        return fanOut(cb);
    }
    
    private int fanOut(ClassBean pType) {
		int counter=0;
		try {
			for(MethodBean m: pType.getMethods()){
				if(! m.getName().equals("cloneDate")){
					if(m.getMethodCalls().size()!=0) {
						for(@SuppressWarnings("unused") MethodBean mi: m.getMethodCalls()){
							counter++;
						}
					}	
				}
			}
		} catch (Exception e) {
			return counter;
		}
		return counter;
	}

}
