package it.unisa.jDECAF_ML.decor;

import it.unisa.jDECAF_ML.metrics.CKMetrics;
import it.unisa.jDECAF_ML.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.parser.bean.ComponentBean;

public class ClassDataShouldBePrivateRule implements DetectionRule{

    public boolean isClassDataShouldBePrivate(ClassBean pClass) {
        if (CKMetrics.getNOPA(pClass) > 10) {
            return true;
        }

        return false;
    }

    @Override
    public boolean isSmelly(ComponentBean cb) {
        return isClassDataShouldBePrivate((ClassBean) cb);
    }
}
