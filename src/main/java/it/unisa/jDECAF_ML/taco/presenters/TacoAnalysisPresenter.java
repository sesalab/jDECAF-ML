package it.unisa.jDECAF_ML.taco.presenters;

import it.unisa.jDECAF_ML.taco.detectors.AnalyzedComponent;

import java.util.Collection;

public interface TacoAnalysisPresenter {
    void present(Collection<AnalyzedComponent> analyzedComponents);
}
