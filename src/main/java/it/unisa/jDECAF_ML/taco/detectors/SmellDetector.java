package it.unisa.jDECAF_ML.taco.detectors;

import it.unisa.jDECAF_ML.parser.bean.ClassBean;

import java.util.List;

public interface SmellDetector {
    List<AnalyzedComponent> detectSmells(List<ClassBean> classesUnderAnalysis);
}
