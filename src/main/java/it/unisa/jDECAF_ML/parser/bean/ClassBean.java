package it.unisa.jDECAF_ML.parser.bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class ClassBean extends ComponentBean implements Comparable {

    private Collection<InstanceVariableBean> instanceVariables;
    private Collection<MethodBean> methods;
    private Collection<String> imports;
    private int LOC;
    private int CLOC;
    private String superclass;
    private String belongingPackage;

    public ClassBean() {
        super(null);
        instanceVariables = new ArrayList<>();
        methods = new ArrayList<>();
        setImports(new ArrayList<String>());
    }

    public int getLOC() {
        return LOC;
    }

    public void setLOC(int lOC) {
        LOC = lOC;
    }
    
    public int getCLOC() {
        return CLOC;
    }

    public void setCLOC(int ClOC) {
        CLOC = ClOC;
    }

    public Collection<InstanceVariableBean> getInstanceVariables() {
        return instanceVariables;
    }

    public void setInstanceVariables(Collection<InstanceVariableBean> pInstanceVariables) {
        instanceVariables = pInstanceVariables;
    }

    public void addInstanceVariables(InstanceVariableBean pInstanceVariable) {
        instanceVariables.add(pInstanceVariable);
    }

    public void removeInstanceVariables(InstanceVariableBean pInstanceVariable) {
        instanceVariables.remove(pInstanceVariable);
    }

    public Collection<MethodBean> getMethods() {
        return methods;
    }

    public void setMethods(Collection<MethodBean> pMethods) {
        pMethods.forEach(this::addMethod);
    }

    public void addMethod(MethodBean pMethod) {
        methods.add(pMethod);
        pMethod.setBelongingClass(this);
    }

    public void removeMethod(MethodBean pMethod) {
        methods.remove(pMethod);
        pMethod.setBelongingClass(null);
    }

    @Override
    public String toString() {
        return "name = " + name + "\n"
                + "instanceVariables = " + instanceVariables + "\n"
                + "methods = " + methods + "\n";
    }

    @Override
    public int compareTo(Object pClassBean) {
        return this.getName().compareTo(((ClassBean) pClassBean).getName());
    }

    public Collection<String> getImports() {
        return imports;
    }

    public void setImports(Collection<String> imports) {
        this.imports = imports;
    }

    @Override
    public String getQualifiedName() {
        return belongingPackage + "." + name;
    }

    public String getSuperclass() {
        return superclass;
    }

    public void setSuperclass(String superclass) {
        this.superclass = superclass;
    }

    public String getBelongingPackage() {
        return belongingPackage;
    }

    public void setBelongingPackage(String belongingPackage) {
        this.belongingPackage = belongingPackage;
    }

    @Override
    public boolean equals(Object arg) {
        if (arg instanceof ClassBean) {
            return this.getName().equals(((ClassBean) arg).getName())
                    && this.getBelongingPackage().equals(((ClassBean) arg).getBelongingPackage());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.name);
        hash = 41 * hash + Objects.hashCode(this.belongingPackage);
        return hash;
    }

}
