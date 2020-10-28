package decor;

import it.unisa.jDECAF_ML.metrics.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.metrics.parser.bean.ComponentBean;
import it.unisa.jDECAF_ML.metrics.parser.bean.MethodBean;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

public class LongParameterListRule implements DetectionRule {

    private List<MethodBean> candidateLongParameterList;
    private List<ClassBean> projectClasses;
    private HashMap<MethodBean, Integer> methodNoP;

    public LongParameterListRule() {
        methodNoP = new HashMap<>();
    }

    public void setProjectClasses(List<ClassBean> classes) {
        this.projectClasses = classes;
        this.candidateLongParameterList = searchLongParameterListAntipattern();
    }

    public boolean isLongParameterList(MethodBean pMethod) {
        return candidateLongParameterList.contains(pMethod);
    }

    public List<MethodBean> searchLongParameterListAntipattern() {
        candidateLongParameterList = new ArrayList<>();
        for (ClassBean cu : projectClasses) {
            if (cu.getName().contains(".java")) {
                for (MethodBean method : cu.getMethods()) {
                    methodNoP.put(method, method.getParameters().size());
                }
            }
        }
        int max = this.max();
        if (max != -1) {
            try {
                double upperHinge = this.upperHinge();

                for (Map.Entry<MethodBean, Integer> value : methodNoP.entrySet()) {
                    if ((value.getValue() > upperHinge) && (value.getValue() < max)) {
                        if (value.getValue().intValue() > 8) {
                            candidateLongParameterList.add(value.getKey());
                        }
                    }
                }
            } catch (Exception e) {
                return candidateLongParameterList;
            }
        }
        return candidateLongParameterList;
    }

    private int max() {
        if (methodNoP.values().size() > 0) {
            return Collections.max(methodNoP.values());
        } else {
            return -1;
        }
    }

    private Double upperHinge() {
        ArrayList<Integer> values = new ArrayList<Integer>();
        ArrayList<Integer> valuesForUpperHinge = new ArrayList<Integer>();

        for (Integer i : this.methodNoP.values()) {
            values.add(i);
        }

        int max = this.max();
        double median = this.median(values);

        for (Map.Entry<MethodBean, Integer> value : methodNoP.entrySet()) {
            if ((value.getValue() > median) && (value.getValue() < max)) {
                valuesForUpperHinge.add(value.getValue());
            }
        }

        return this.median(valuesForUpperHinge);
    }

    private Double median(ArrayList<Integer> values) {
        Collections.sort(values);
        if ((values.size() % 2 == 0)) {
            return (double) ((values.get((values.size() / 2) - 1) + (values.get(values.size() / 2)))) / 2;
        } else {
            return (double) (values.get(values.size() / 2));
        }
    }

    @Override
    public boolean isSmelly(ComponentBean cb) {
        return isLongParameterList((MethodBean) cb);
    }
}
