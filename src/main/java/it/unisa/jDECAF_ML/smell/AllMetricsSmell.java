package it.unisa.jDECAF_ML.smell;

import it.unisa.jDECAF_ML.metrics.Metric;
import it.unisa.jDECAF_ML.metrics.classmetrics.*;
import it.unisa.jDECAF_ML.parser.bean.ComponentBean;

import java.util.Arrays;
import java.util.List;

public class AllMetricsSmell extends CodeSmell {

    public AllMetricsSmell() {
        super("all", "");
    }

    @Override
    public boolean affectsComponent(ComponentBean cb) {
        return false;
    }

    @Override
    public List<Metric> getMetrics() {
        return Arrays.asList(
            new CBO(),
            new CYCLO(),
            new DIT(),
            new ELOC(),
            new FanIn(),
            new FanOut(),
            new LCOM2(),
            new LOC(),
            new LOCNAMM(),
            new NOA(),
            new NOC(),
            new NOM(),
            new NOMNAMM(),
            new NOPA(),
            new PMMM(),
            new PRB(),
            new WLOCNAMM(),
            new WMC(),
            new WMCNAMM()
        );
    }
}
