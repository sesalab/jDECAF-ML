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

/**
 *
 * @author fabiano
 */
public class PRB implements ClassMetric {

    @Override
    public String getName() {
        return "PRB";
    }

    @Override
    public double evaluate(ClassBean cb, ArrayList<ClassBean> System) {
        int refusedCounter = 0;
        ClassBean superclass = this.findSuperclass(cb, System);
        if ((superclass != null) && (!superclass.getTextContent().contains("abstract"))) {
            int numberOfSuperclassMethods = superclass.getMethods().size();
            for (MethodBean method : cb.getMethods()) {
                for (MethodBean superMethod : superclass.getMethods()) {
                    if ((method.getName().equals(superMethod.getName()))) {
                        refusedCounter++;
                    }
                }
            }
            if (numberOfSuperclassMethods > 0) {
                return (double) (refusedCounter / numberOfSuperclassMethods);
            }
        }
        return 0;
    }

    private ClassBean findSuperclass(ClassBean cb, ArrayList<ClassBean> projectClasses) {
        for (ClassBean c : projectClasses) {
            if (cb.getTextContent().contains("extends " + c.getName())) {
                return c;
            }
        }
        return null;
    }
}
