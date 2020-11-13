package it.unisa.jDECAF_ML.parser.bean;

public class MethodBlockBean extends ComponentBean{

    private MethodBean belongingMethod;

    public MethodBlockBean() {
        super("BLOCK");
    }

    public void setBelongingMethod(MethodBean belongingMethod) {
        this.belongingMethod = belongingMethod;
    }

    @Override
    public String getQualifiedName() {
        return belongingMethod.getQualifiedName() + "." + name;
    }
}
