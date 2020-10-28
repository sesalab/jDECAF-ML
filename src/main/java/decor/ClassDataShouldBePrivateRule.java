package decor;

import it.unisa.jDECAF_ML.metrics.CKMetrics;
import it.unisa.jDECAF_ML.metrics.classmetrics.NOPA;
import it.unisa.jDECAF_ML.metrics.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.metrics.parser.bean.ComponentBean;

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
