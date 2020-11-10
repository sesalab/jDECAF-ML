package it.unisa.jDECAF_ML.taco.detectors;

import it.unisa.jDECAF_ML.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.parser.bean.MethodBean;
import org.apache.commons.text.similarity.CosineDistance;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BlobDetectorTest {

    private final ComponentSimilarity componentSimilarity = new ApacheTextComponentSimilarity(new CosineDistance());

    @Test
    public void when_class_has_one_method_smelliness_probability_is_zero() {
        ClassBean aClass = new ClassBean();
        aClass.setName("aClass");
        MethodBean onlyMethod = new MethodBean();
        onlyMethod.setName("onlyMethod");
        onlyMethod.setTextContent("detect smell similarity");
        aClass.setMethods(Collections.singletonList(onlyMethod));

        SmellDetector blobDetector = new BlobDetector(Collections.singletonList(aClass), componentSimilarity);
        List<AnalyzedComponent> analyzedComponents = blobDetector.detectSmells();

        assertThat(analyzedComponents.size()).isEqualTo(1);
        AnalyzedComponent actualAnalyzedComponent = analyzedComponents.get(0);
        assertThat(actualAnalyzedComponent.getComponent().getName()).isEqualTo(aClass.getName());
        assertThat(actualAnalyzedComponent.getSmellinessProbability()).isEqualTo(0.0);
        assertThat(actualAnalyzedComponent.getAnalyzedSmell()).isEqualTo(SmellType.BLOB);
    }

    @Test
    public void when_cass_has_two_methods_with_no_common_term_probability_is_one() {
        ClassBean aClass = new ClassBean();
        aClass.setName("aClass");

        MethodBean firstMethod = new MethodBean();
        firstMethod.setName("firstMethod");
        firstMethod.setTextContent("detect smell similarity");

        MethodBean secondMethod = new MethodBean();
        secondMethod.setName("secondMethod");
        secondMethod.setTextContent("sell book");

        aClass.setMethods(Arrays.asList(firstMethod,secondMethod));

        SmellDetector blobDetector = new BlobDetector(Collections.singletonList(aClass), componentSimilarity);
        List<AnalyzedComponent> analyzedComponents = blobDetector.detectSmells();

        assertThat(analyzedComponents.size()).isEqualTo(1);
        AnalyzedComponent actualAnalyzedComponent = analyzedComponents.get(0);
        assertThat(actualAnalyzedComponent.getComponent().getName()).isEqualTo(aClass.getName());
        assertThat(actualAnalyzedComponent.getSmellinessProbability()).isEqualTo(1.0);
        assertThat(actualAnalyzedComponent.getAnalyzedSmell()).isEqualTo(SmellType.BLOB);
    }
}
