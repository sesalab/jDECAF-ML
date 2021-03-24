package it.unisa.jDECAF_ML.parser;

import it.unisa.jDECAF_ML.parser.bean.MethodBean;


class InvocationParser {

    public static MethodBean parse(String pInvocationName) {
        MethodBean methodBean = new MethodBean();
        methodBean.setName(pInvocationName);
        return methodBean;
    }

}
