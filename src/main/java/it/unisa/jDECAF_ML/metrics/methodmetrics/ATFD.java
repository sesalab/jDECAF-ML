/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.jDECAF_ML.metrics.methodmetrics;

import it.unisa.jDECAF_ML.metrics.methodmetrics.MethodMetric;
import it.unisa.jDECAF_ML.metrics.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.metrics.parser.bean.InstanceVariableBean;
import it.unisa.jDECAF_ML.metrics.parser.bean.MethodBean;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author fabiano
 */
public class ATFD implements MethodMetric {

    @Override
    public String getName() {
        return "ATFD";
    }

//    @Override
//    public double evaluate(ClassBean cb, ArrayList<ClassBean> Sys) {
//        double ATFD = 0;
//
//        for (MethodBean mb : cb.getMethods()) {
//           
//            ATFD += mb.getForeignAccessedFields().size();
//            for (MethodBean called : mb.getMethodCalls()) {
//                if (called.isAccessor() && !cb.getInstanceVariables().contains(called.getUsedInstanceVariables().iterator().next())) {
//                    ATFD++;
//                }
//            }
//        }
//        return ATFD;
//    }

    @Override
    public double evaluate(MethodBean mb) {
        double ATFD = mb.getForeignAccessedFields().size();
            for (MethodBean called : mb.getMethodCalls()) {
                if (called.isAccessor() && !mb.getBelongingClass().getInstanceVariables().contains(called.getUsedInstanceVariables().iterator().next())) {
                    ATFD++;
                }
            }
        return ATFD;
    }

}
