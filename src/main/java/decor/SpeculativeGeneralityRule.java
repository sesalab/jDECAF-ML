package decor;

import it.unisa.jDECAF_ML.metrics.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.metrics.parser.bean.ComponentBean;
import it.unisa.jDECAF_ML.metrics.parser.bean.MethodBean;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

public class SpeculativeGeneralityRule implements DetectionRule {

    private List<ClassBean> candidateSpeculativeGeneralities;
    private List<ClassBean> projectClasses;
    private Vector<ClassBean> types;
    private Vector<ClassBean> abstractTypes;

    public SpeculativeGeneralityRule() {
        System.out.println();
        this.types = new Vector<ClassBean>();
        this.abstractTypes = new Vector<ClassBean>();
    }

    public void setProjectClasses(List<ClassBean> classes) {
        this.projectClasses = classes;
        this.candidateSpeculativeGeneralities = searchSpeculativeGeneralities();
    }

    public boolean isSpeculativeGenerality(ClassBean cb) {
        return this.candidateSpeculativeGeneralities.contains(cb);
    }

    public List<ClassBean> searchSpeculativeGeneralities() {
        candidateSpeculativeGeneralities = new ArrayList<ClassBean>();
        for (ClassBean cu : this.projectClasses) {
            this.types.add(cu);
            int index = 0;
            index = cu.getTextContent().indexOf("{");
            if (index != -1) {
                String signature = cu.getTextContent().substring(0, index - 1);
                if (signature.contains(" abstract class")) {
                    this.abstractTypes.add(cu);
                }
            }
        }
        for (ClassBean type : this.abstractTypes) {
            int counterOfChildren = 0;
            for (ClassBean check : this.types) {
                if (check.getTextContent().contains("extends " + type.getName())) {
                    counterOfChildren++;
                }
            }

            if (counterOfChildren < 2) {
                ClassBean newClass = type;
                if (!this.isIn(newClass)) {
                    this.candidateSpeculativeGeneralities.add(newClass);
                }
            }
        }

        return candidateSpeculativeGeneralities;
    }

    private boolean isIn(ClassBean pClassBean) {
        for (ClassBean cb : this.candidateSpeculativeGeneralities) {
            if (cb.getName().equals(pClassBean.getName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isSmelly(ComponentBean cb) {
        return isSpeculativeGenerality((ClassBean) cb);
    }

}
