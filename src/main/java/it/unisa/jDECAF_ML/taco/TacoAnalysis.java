package it.unisa.jDECAF_ML.taco;

import it.unisa.jDECAF_ML.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.taco.detectors.SmellDetector;
import it.unisa.jDECAF_ML.taco.presenters.TacoAnalysisPresenter;

import java.util.List;

public class TacoAnalysis {


    private final List<ClassBean> allProjectClasses;
    private final SmellDetector detector;
    private final TacoAnalysisPresenter presenter;

    public TacoAnalysis(List<ClassBean> allProjectClasses, SmellDetector detector, TacoAnalysisPresenter presenter) {
        this.allProjectClasses = allProjectClasses;
        this.detector = detector;
        this.presenter = presenter;
    }

    public void execute(){
        presenter.present(detector.detectSmells(allProjectClasses));
    }

}
