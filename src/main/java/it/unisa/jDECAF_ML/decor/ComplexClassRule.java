package it.unisa.jDECAF_ML.decor;

import it.unisa.jDECAF_ML.metrics.CKMetrics;
import it.unisa.jDECAF_ML.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.parser.bean.ComponentBean;



public class ComplexClassRule implements DetectionRule{

	public boolean isComplexClass(ClassBean pClass) {

		if(CKMetrics.getMcCabeMetric(pClass) > 200)
				return true;

		return false;
	}

    @Override
    public boolean isSmelly(ComponentBean cb) {
        return isComplexClass((ClassBean)cb);
    }
}
