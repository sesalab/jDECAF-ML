/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.jDECAF_ML.metrics.parser.bean;

/**
 *
 * @author fabiano
 */
public abstract class ComponentBean {

    String name;

    public ComponentBean(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String pName) {
        name = pName;
    }
}
