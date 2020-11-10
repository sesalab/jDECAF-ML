package it.unisa.jDECAF_ML.taco;

import it.unisa.jDECAF_ML.taco.detectors.SmellDetector;
import it.unisa.jDECAF_ML.taco.presenters.TacoAnalysisPresenter;

public class TacoAnalysis {


    private final SmellDetector detector;
    private final TacoAnalysisPresenter presenter;

    public TacoAnalysis(SmellDetector detector, TacoAnalysisPresenter presenter) {
        this.detector = detector;
        this.presenter = presenter;
    }

    public void execute(){
        presenter.present(detector.detectSmells());
    }

}
