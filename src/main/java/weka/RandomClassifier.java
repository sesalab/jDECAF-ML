/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weka;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.RandomizableClassifier;
import weka.core.Capabilities;
import weka.core.Instance;
import weka.core.Instances;
import java.util.Random;
/**
 *
 * @author fably
 */
public class RandomClassifier extends RandomizableClassifier {

    private Random r;

    @Override
    public void buildClassifier(Instances data) throws Exception {
        getCapabilities().testWithFail(data);
        data = new Instances(data);
        data.deleteWithMissingClass();
        r = new Random();
    }

    @Override
    public double classifyInstance(Instance i) {
        return r.nextBoolean()? 1:0;
    }

}
