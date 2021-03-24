package it.unisa.jDECAF_ML.hist;

import it.unisa.jDECAF_ML.parser.bean.ComponentBean;

public final class AnalyzedComponent {
    private final ComponentBean component;
    private final double changePercentage;

    public AnalyzedComponent(ComponentBean component, double changePercentage) {
        this.component = component;
        this.changePercentage = changePercentage;
    }

    public ComponentBean getComponent() {
        return component;
    }

    public double getChangePercentage() {
        return changePercentage;
    }

    @Override
    public String toString() {
        return "AnalyzedComponent{" + "component=" + component +
                ", changePercentage=" + changePercentage +
                '}';
    }
}
