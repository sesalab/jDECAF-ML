package it.unisa.jDECAF_ML.parser.bean;

public class MethodBlockBean {

    private MethodBean belongingMethod;
    private String content;

    public MethodBean getBelongingMethod() {
        return belongingMethod;
    }

    public void setBelongingMethod(MethodBean belongingMethod) {
        this.belongingMethod = belongingMethod;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodBlockBean blockBean = (MethodBlockBean) o;

        return content.equals(blockBean.content);
    }

    @Override
    public int hashCode() {
        return content.hashCode();
    }
}
