/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.jDECAF_ML.metrics.classmetrics;

import it.unisa.jDECAF_ML.parser.bean.ClassBean;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author fabiano
 */
public class FanIn implements ClassMetric {

    @Override
    public String getName() {
        return "FanIn";
    }

    @Override
    public double evaluate(ClassBean cb, ArrayList<ClassBean> Sys) {
        return fanIn(cb,Sys);
    }
    
    private int fanIn(ClassBean pType, List<ClassBean> pOthers) {
		int counter=0;
		Matcher matcher;
		for(ClassBean type: pOthers){
			String code="";
			if(type!=pType){
				Pattern pattern=Pattern.compile(pType.getName());
				try {
					code=type.getTextContent();
					matcher=pattern.matcher(code);
					while(matcher.find()) {
						counter++;
					}
				} catch (Exception e) {
					return counter;
				}			
			}
		}

		return counter;
	}

}
