package it.unisa.jDECAF_ML.taco.detectors;

import it.unisa.jDECAF_ML.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.parser.bean.MethodBean;
import it.unisa.jDECAF_ML.parser.bean.MethodBlockBean;
import it.unisa.jDECAF_ML.taco.normalizer.IRNormalizer;
import org.apache.commons.text.similarity.CosineDistance;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LongMethodDetectorTest {

    private ApacheTextComponentSimilarity similarity = new ApacheTextComponentSimilarity(new CosineDistance(), new IRNormalizer());

    @Test
    public void when_there_is_one_fragment_probability_is_zero() {
        MethodBlockBean onlyBlock = new MethodBlockBean();
        onlyBlock.setTextContent("detect smell class project");

        MethodBean blockContainer = new MethodBean();
        blockContainer.setName("blockContainer");
        blockContainer.addMethodBlock(onlyBlock);

        ClassBean onlyClass = new ClassBean();
        onlyClass.setName("onlyClass");
        onlyClass.addMethod(blockContainer);

        SmellDetector longMethodDetector = new LongMethodDetector(Collections.singletonList(onlyClass), similarity);
        List<AnalyzedComponent> analyzedComponents = longMethodDetector.detectSmells();
        assertThat(analyzedComponents.size()).isEqualTo(1);

        AnalyzedComponent analyzedMethod = analyzedComponents.get(0);
        assertThat(analyzedMethod.getComponent().getName()).isEqualTo("blockContainer");
        assertThat(analyzedMethod.getSmellinessProbability()).isEqualTo(0.0);
        assertThat(analyzedMethod.getAnalyzedSmell()).isEqualTo(SmellType.LONG_METHOD);

    }

    @Test
    public void when_method_has_two_blocks_with_no_term_in_common_probability_is_one() {
        MethodBlockBean firstBlock = new MethodBlockBean();
        firstBlock.setTextContent("detect smell similarity");

        MethodBlockBean secondBlock = new MethodBlockBean();
        secondBlock.setTextContent("sell book");

        MethodBean longMethodBean = new MethodBean();
        longMethodBean.setName("longMethod");
        longMethodBean.addMethodBlock(firstBlock);
        longMethodBean.addMethodBlock(secondBlock);

        ClassBean onlyClass = new ClassBean();
        onlyClass.setName("onlyClass");
        onlyClass.addMethod(longMethodBean);

        SmellDetector longMethodDetector = new LongMethodDetector(Collections.singletonList(onlyClass), similarity);
        List<AnalyzedComponent> analyzedComponents = longMethodDetector.detectSmells();
        assertThat(analyzedComponents.size()).isEqualTo(1);

        AnalyzedComponent analyzedMethod = analyzedComponents.get(0);
        assertThat(analyzedMethod.getComponent().getName()).isEqualTo("longMethod");
        assertThat(analyzedMethod.getSmellinessProbability()).isEqualTo(1.0);
        assertThat(analyzedMethod.getAnalyzedSmell()).isEqualTo(SmellType.LONG_METHOD);
    }
}
