package it.unisa.jDECAF_ML.hist.feature_envy;

import it.unisa.jDECAF_ML.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.parser.bean.MethodBean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class MethodChangeRecord {
    private MethodBean method;
    private final Map<ClassBean, Integer> coChangesWithClasses;

    public MethodChangeRecord(MethodBean method) {
        this.method = method;
        coChangesWithClasses = new ConcurrentHashMap<>();
    }

    public void incrementChangesOf(ClassBean clazz){
        coChangesWithClasses.merge(clazz, 1, Integer::sum);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MethodChangeRecord{");
        sb.append("method=").append(method);
        sb.append(", coChangesWithClasses=").append(coChangesWithClasses);
        sb.append('}');
        return sb.toString();
    }
}
