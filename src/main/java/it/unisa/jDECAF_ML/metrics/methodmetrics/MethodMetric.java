/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.jDECAF_ML.metrics.methodmetrics;

import it.unisa.jDECAF_ML.metrics.Metric;
import it.unisa.jDECAF_ML.metrics.classmetrics.*;
import it.unisa.jDECAF_ML.metrics.parser.bean.MethodBean;

/**
 *
 * @author fabiano
 */
public interface MethodMetric extends Metric{
    double evaluate(MethodBean mb);
}
