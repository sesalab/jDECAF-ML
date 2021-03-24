/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.jDECAF_ML.smell;

import it.unisa.jDECAF_ML.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.parser.bean.ComponentBean;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fably
 */
public abstract class ClassSmell extends CodeSmell{

    public ClassSmell(String name, String oraclePath) {
        super(name, oraclePath);
    }

    @Override
    public boolean affectsComponent(ComponentBean cb) {
        ClassBean clb = (ClassBean) cb;
        BufferedReader br = null;
        String line = "";
        String separator = ";";
        String className = clb.getName() + ".java";
        String classPackage = clb.getBelongingPackage();
        try {
            br = new BufferedReader(new FileReader(oraclePath));
            while ((line = br.readLine()) != null) {
                String[] classInfo = line.split(separator);
                if(classInfo[0].startsWith(" ")){
                    classInfo[0] = classInfo[0].substring(1);
                }
                if(classInfo[1].startsWith(" ")){
                    classInfo[1] = classInfo[1].substring(1);
                }
                if (classPackage.equals(classInfo[1]) && className.equals(classInfo[0])) {
                 //   System.out.println(classInfo[0] + " - " + className);
                  //  System.out.println(classInfo[1] + " - " + classPackage);
                    return true;
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GodClass.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GodClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
}
