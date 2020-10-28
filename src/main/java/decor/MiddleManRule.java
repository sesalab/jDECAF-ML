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
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class MiddleManRule implements DetectionRule {

    private List<ClassBean> candidateMiddleMen;
    private List<ClassBean> projectClasses;
    private Vector<ClassBean> types;

    public MiddleManRule() {
        System.out.println();
        this.types = new Vector<ClassBean>();
    }

    public void setProjectClasses(List<ClassBean> classes) {
        this.projectClasses = classes;
        this.candidateMiddleMen = searchMiddleMen();
    }

    public boolean isMiddleMan(ClassBean cb) {
        return this.candidateMiddleMen.contains(cb);
    }

    public List<ClassBean> searchMiddleMen() {
        candidateMiddleMen = new ArrayList<ClassBean>();

        for (ClassBean cu : projectClasses) {
            if (cu.getName().contains(".java")) {
                if (!cu.getTextContent().contains(" interface ")) {
                    types.add(cu);
                }
            }
        }

        for (ClassBean t : types) {
            double counterDelegation = 0.0;
                for (MethodBean m : t.getMethods()) {
                    if (this.isDelegation(m)) {
                        counterDelegation++;
                    }
                }

                double percentage = counterDelegation / t.getMethods().size();
                if (percentage > 0.7) {
                    candidateMiddleMen.add(t);
                }
        }

        return candidateMiddleMen;
    }

    private boolean isDelegation(MethodBean pMethodToAnalyze) {

        if (this.LOC(pMethodToAnalyze) < 10) {

            if (pMethodToAnalyze.getMethodCalls().size() == 1) {
                return true;
            }
        }
        return false;
    }

    private int LOC(MethodBean pMethod) {
        int loc = 0;
        String source = pMethod.getTextContent();
        String regex = "[\n]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            loc++;
        }
        return loc + 1;
    }

    @Override
    public boolean isSmelly(ComponentBean cb) {
        return isMiddleMan((ClassBean) cb);
    }

}
