package it.unisa.jDECAF_ML.metrics.parser;

import it.unisa.jDECAF_ML.metrics.parser.bean.InstanceVariableBean;
import it.unisa.jDECAF_ML.metrics.parser.bean.MethodBean;


class FieldAccessParser {

    public static InstanceVariableBean parse(String fieldAccessName) {
        InstanceVariableBean ivb = new InstanceVariableBean();
        ivb.setName(fieldAccessName);
        return ivb;
    }

}
