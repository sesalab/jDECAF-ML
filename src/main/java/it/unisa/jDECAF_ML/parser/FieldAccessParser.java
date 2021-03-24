package it.unisa.jDECAF_ML.parser;

import it.unisa.jDECAF_ML.parser.bean.InstanceVariableBean;


class FieldAccessParser {

    public static InstanceVariableBean parse(String fieldAccessName) {
        InstanceVariableBean ivb = new InstanceVariableBean();
        ivb.setName(fieldAccessName);
        return ivb;
    }

}
