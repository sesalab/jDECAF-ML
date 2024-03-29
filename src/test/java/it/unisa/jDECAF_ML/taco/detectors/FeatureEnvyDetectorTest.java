package it.unisa.jDECAF_ML.taco.detectors;


import it.unisa.jDECAF_ML.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.parser.bean.MethodBean;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FeatureEnvyDetectorTest {

    @Test
    public void when_there_is_one_class_should_return_0_probability() {
        ClassBean aClass = new ClassBean();
        aClass.setName("aClass");
        aClass.setTextContent("detect smell similarity");
        MethodBean onlyMethod = new MethodBean();
        onlyMethod.setName("onlyMethod");
        onlyMethod.setTextContent("detect smell similarity");
        aClass.setMethods(Collections.singletonList(onlyMethod));

        CodeSmellDetector detector = new FeatureEnvyDetector(Collections.singletonList(aClass));
        List<AnalyzedComponent> analyzedComponents = detector.detectSmells(Collections.singletonList(aClass));

        assertThat(analyzedComponents.size()).isEqualTo(1);
        AnalyzedComponent actualAnalyzedComponent = analyzedComponents.get(0);
        assertThat(actualAnalyzedComponent.getComponent().getName()).isEqualTo(onlyMethod.getName());
        assertThat(actualAnalyzedComponent.getSmellinessProbability()).isEqualTo(0.0);
        assertThat(actualAnalyzedComponent.getAnalyzedSmell()).isEqualTo(SmellType.FEATURE_ENVY);
    }

    @Test
    public void when_class_has_no_term_in_common_with_method_probability_is_one() {
        ClassBean firstClass = new ClassBean();
        firstClass.setName("FirstClass");
        firstClass.setTextContent("apple juice create");

        MethodBean smellyMethod = new MethodBean();
        smellyMethod.setTextContent("convert local time");
        smellyMethod.setName("smellyMethod");
        firstClass.addMethod(smellyMethod);

        ClassBean secondClass = new ClassBean();
        secondClass.setName("SecondClass");
        secondClass.setTextContent("convert local time");

        CodeSmellDetector detector = new FeatureEnvyDetector(Arrays.asList(firstClass,secondClass));
        List<AnalyzedComponent> analyzedComponents = detector.detectSmells(Arrays.asList(firstClass,secondClass));

        assertThat(analyzedComponents.size()).isEqualTo(1);
        AnalyzedComponent smellyComponent = analyzedComponents.get(0);
        assertThat(smellyComponent.getComponent().getName()).isEqualTo(smellyMethod.getName());
        assertThat(smellyComponent.getSmellinessProbability()).isEqualTo(1.0);
        assertThat(smellyComponent.getAnalyzedSmell()).isEqualTo(SmellType.FEATURE_ENVY);

    }
}
