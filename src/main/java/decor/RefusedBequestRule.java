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

public class RefusedBequestRule implements DetectionRule {

    private List<ClassBean> candidateRefusedBequests;
    private List<ClassBean> projectClasses;
    private Vector<ClassBean> types;
    private Vector<ClassBean> abstractTypes;

    public RefusedBequestRule() {
        System.out.println();
        this.types = new Vector<ClassBean>();
        this.abstractTypes = new Vector<ClassBean>();
    }

    public void setProjectClasses(List<ClassBean> classes) {
        this.projectClasses = classes;
        this.candidateRefusedBequests = searchRefusedBequests();
    }

    public boolean isRefusedBequest(ClassBean cb) {
        return this.candidateRefusedBequests.contains(cb);
    }

    public List<ClassBean> searchRefusedBequests() {
        double refusedCounter = 0.0;
        double numberOfSuperclassMethods = 0.0;
        candidateRefusedBequests = new ArrayList<ClassBean>();
        for (ClassBean cu : this.projectClasses) {
            if (cu.getName().contains(".java")) {
                types.add(cu);

            }
        }

        for (ClassBean type : types) {
            if (this.LOC(type) > 300) {
                    if (type.getTextContent().contains("extends")) {
                        ClassBean superclass = this.findSuperclass(type);
                        if ((superclass != null) && (!superclass.getTextContent().contains("abstract"))) {
                            numberOfSuperclassMethods = superclass.getMethods().size();
                            for (MethodBean method : type.getMethods()) {
                                for (MethodBean superMethod : superclass.getMethods()) {
                                    if ((method.getName().equals(superMethod.getName()))) {
                                        refusedCounter++;
                                    }
                                }
                            }

                            if ((refusedCounter / numberOfSuperclassMethods) > 0.7) {
                                candidateRefusedBequests.add(type);
                            }
                        }
                    }
            }
        }

        return candidateRefusedBequests;
    }

    private ClassBean findSuperclass(ClassBean cb){
        for (ClassBean c : projectClasses){
            if (cb.getTextContent().contains("extends "+c.getName())){
                return c;
            }
        }
        return null;
    }
    private int LOC(ClassBean pClass) {
        int loc = 0;
        String source = pClass.getTextContent();
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
        return isRefusedBequest((ClassBean) cb);
    }

}
