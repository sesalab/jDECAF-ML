package it.unisa.jDECAF_ML.taco.detectors;

import it.unisa.jDECAF_ML.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.parser.bean.MethodBean;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SpaghettiCodeDetectorTest {

    @Test
    public void when_all_methods_are_long_methods_then_probability_is_one() {
        ClassBean spaghettiClass = new ClassBean();
        spaghettiClass.setName("SpaghettiClass");

        spaghettiClass.addMethod(dummyMethodBean("first"));
        spaghettiClass.addMethod(dummyMethodBean("second"));
        spaghettiClass.addMethod(dummyMethodBean("third"));

        SpaghettiCodeDetector detector = new SpaghettiCodeDetector(new AllLongMethodsDetector());
        List<AnalyzedComponent> analyzedComponents = detector.detectSmells(Collections.singletonList(spaghettiClass));
        assertThat(analyzedComponents.size()).isEqualTo(1);

        AnalyzedComponent onlyResult = analyzedComponents.get(0);
        assertThat(onlyResult.getComponent().getName()).isEqualTo("SpaghettiClass");
        assertThat(onlyResult.getSmellinessProbability()).isEqualTo(1.0);
    }

    @Test
    public void given_fixed_smelliness_probability_it_returns_the_expected_probability() {
        ClassBean spaghettiClass = new ClassBean();
        spaghettiClass.setName("SpaghettiClass");

        spaghettiClass.addMethod(dummyMethodBean("first"));
        spaghettiClass.addMethod(dummyMethodBean("second"));
        spaghettiClass.addMethod(dummyMethodBean("third"));

        SpaghettiCodeDetector detector = new SpaghettiCodeDetector(new FixedProbabilitiesDetector());
        List<AnalyzedComponent> analyzedComponents = detector.detectSmells(Collections.singletonList(spaghettiClass));
        assertThat(analyzedComponents.size()).isEqualTo(1);

        AnalyzedComponent onlyResult = analyzedComponents.get(0);
        assertThat(onlyResult.getComponent().getName()).isEqualTo("SpaghettiClass");
        assertThat(onlyResult.getSmellinessProbability()).isEqualTo(0.875);
    }

    private MethodBean dummyMethodBean(String name){
        MethodBean dummy = new MethodBean();
        dummy.setName(name);
        return dummy;
    }

    private static class FixedProbabilitiesDetector extends LongMethodDetector {
        @Override
        public List<AnalyzedComponent> detectSmells(List<ClassBean> classesUnderAnalysis) {
            List<AnalyzedComponent> result = new ArrayList<>();
            for(ClassBean c: classesUnderAnalysis){
                int i = 0;
                double smellinessProb = 0.5;
                for (MethodBean m : c.getMethods()) {
                    result.add(new AnalyzedComponent(m, smellinessProb, SmellType.LONG_METHOD));
                    i++;
                }
            }
            return result;
        }
    }

    private static class AllLongMethodsDetector extends LongMethodDetector{
        @Override
        public List<AnalyzedComponent> detectSmells(List<ClassBean> classesUnderAnalysis) {
            List<AnalyzedComponent> result = new ArrayList<>();
            for(ClassBean c: classesUnderAnalysis){
                for (MethodBean m: c.getMethods()){
                    result.add(new AnalyzedComponent(m,1.0,SmellType.LONG_METHOD));
                }
            }
            return result;
        }
    }

}
