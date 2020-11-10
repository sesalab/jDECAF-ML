package it.unisa.jDECAF_ML.taco.detectors;

import it.unisa.jDECAF_ML.parser.bean.ComponentBean;

public final class AnalyzedComponent {

    private final ComponentBean component;
    private final Double smellinessProbability;
    private final SmellType analyzedSmell;

    public AnalyzedComponent(ComponentBean component, Double smellinessProbability, SmellType analyzedSmell) {
        this.component = component;
        this.smellinessProbability = smellinessProbability;
        this.analyzedSmell = analyzedSmell;
    }

    public ComponentBean getComponent() {
        return component;
    }

    public Double getSmellinessProbability() {
        return smellinessProbability;
    }

    public SmellType getAnalyzedSmell() {
        return analyzedSmell;
    }
}
