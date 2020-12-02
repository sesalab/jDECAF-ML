package it.unisa.jDECAF_ML.taco.detectors;

import it.unisa.jDECAF_ML.parser.bean.ClassBean;

import java.util.List;

public interface CodeSmellDetector {
    List<AnalyzedComponent> detectSmells(List<ClassBean> classesUnderAnalysis);
}
