package it.unisa.jDECAF_ML.smell;

import it.unisa.jDECAF_ML.metrics.Metric;
import it.unisa.jDECAF_ML.metrics.methodmetrics.ATFD;
import it.unisa.jDECAF_ML.metrics.methodmetrics.LOC_METHOD;
import it.unisa.jDECAF_ML.metrics.methodmetrics.MC;
import it.unisa.jDECAF_ML.metrics.methodmetrics.NP;
import it.unisa.jDECAF_ML.parser.bean.ComponentBean;

import java.util.Arrays;
import java.util.List;

public class AllMethodMetricsSmell extends CodeSmell{
    public AllMethodMetricsSmell() {
        super("all","");
    }

    @Override
    public boolean affectsComponent(ComponentBean cb) {
        return false;
    }

    @Override
    public List<Metric> getMetrics() {
        return Arrays.asList(
             new ATFD(),
             new LOC_METHOD(),
             new MC(),
             new NP()
        );
    }
}
