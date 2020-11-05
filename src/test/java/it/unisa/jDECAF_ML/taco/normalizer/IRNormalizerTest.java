package it.unisa.jDECAF_ML.taco.normalizer;

import it.unisa.jDECAF_ML.parser.bean.ClassBean;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class IRNormalizerTest {

    @Test
    public void normalizeClasses() {
        ClassBean testClass = new ClassBean();
        testClass.setTextContent("public class IRNormalizer {\n private int wordCount;\n public abstract String countWords();}");

        IRNormalizer normalizer = new IRNormalizer();
        List<ClassBean> normalizedClasses = normalizer.normalizeClasses(Collections.singletonList(testClass));
        assertThat(normalizedClasses.size()).isEqualTo(1);
        ClassBean actualClassBean = normalizedClasses.get(0);
        assertThat(actualClassBean.getTextContent()).isEqualTo("ir normalizer word count count words");
    }
}
