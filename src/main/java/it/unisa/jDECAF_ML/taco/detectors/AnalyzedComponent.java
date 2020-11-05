package it.unisa.jDECAF_ML.taco.detectors;

import it.unisa.jDECAF_ML.parser.bean.ComponentBean;

public final class AnalyzedComponent {

    private final ComponentBean component;
    private final Double smellinessProbability;

    public AnalyzedComponent(ComponentBean component, Double smellinessProbability) {
        this.component = component;
        this.smellinessProbability = smellinessProbability;
    }

    public ComponentBean getComponent() {
        return component;
    }

    public Double getSmellinessProbability() {
        return smellinessProbability;
    }
}
