/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.jDECAF_ML.execution;

import java.io.File;
import java.io.PrintWriter;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.Instance;
import weka.core.converters.ConverterUtils.DataSource;

/**
 *
 * @author fabiano
 */
public class MekaEvaluator {

    public MekaEvaluator(String predictorsFilePath, String outputFilePath, Classifier classifier) {

        String filePath = predictorsFilePath;
        try {
            DataSource source = new DataSource(filePath);
            Instances instances = source.getDataSet();
            instances.setClassIndex(instances.numAttributes() - 1);
            System.out.println("Numero istanze: " + instances.size());
            evaluateModel(predictorsFilePath, outputFilePath, classifier, instances);
        } catch (Exception ex) {
            Logger.getLogger(MekaEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void evaluateModel(String predictorsFilePath, String outputFilePath, Classifier pClassifier, Instances pInstances) throws Exception {

        // other options
        int folds = 10;

        // randomize data
        Random rand = new Random(42);
        Instances randData = new Instances(pInstances);
        randData.randomize(rand);
        
//        for(Instance i : randData){
//            if(i.attribute(count).isNominal())
//        }
        Evaluation eval = new Evaluation(randData);

        int positiveValueIndexOfClassFeature = 0;
        for (int n = 0; n < folds; n++) {
            Instances train = randData.trainCV(folds, n);
            Instances test = randData.testCV(folds, n);

            int classFeatureIndex = 0;
            for (int i = 0; i < train.numAttributes(); i++) {
                if (train.attribute(i).name().equals("isSmelly")) {
                    classFeatureIndex = i;
                    break;
                }
            }

            Attribute classFeature = train.attribute(classFeatureIndex);
            for (int i = 0; i < classFeature.numValues(); i++) {
                if (classFeature.value(i).equals("true")) {
                    positiveValueIndexOfClassFeature = i;
                }
            }

            train.setClassIndex(classFeatureIndex);
            test.setClassIndex(classFeatureIndex);

            pClassifier.buildClassifier(train);
            eval.evaluateModel(pClassifier, test);

        }
        System.out.println("\nConfusion Matrix:");
        System.out.println(eval.numTruePositives(positiveValueIndexOfClassFeature) + "\t" + eval.numFalsePositives(positiveValueIndexOfClassFeature) + "\n"
                + eval.numFalseNegatives(positiveValueIndexOfClassFeature) + "\t" + eval.numTrueNegatives(positiveValueIndexOfClassFeature) + "\n\n");
        double accuracy
                = (eval.numTruePositives(positiveValueIndexOfClassFeature)
                + eval.numTrueNegatives(positiveValueIndexOfClassFeature))
                / (eval.numTruePositives(positiveValueIndexOfClassFeature)
                + eval.numFalsePositives(positiveValueIndexOfClassFeature)
                + eval.numFalseNegatives(positiveValueIndexOfClassFeature)
                + eval.numTrueNegatives(positiveValueIndexOfClassFeature));

        double fmeasure = 2 * ((eval.precision(positiveValueIndexOfClassFeature) * eval.recall(positiveValueIndexOfClassFeature))
                / (eval.precision(positiveValueIndexOfClassFeature) + eval.recall(positiveValueIndexOfClassFeature)));
        File wekaOutput = new File(outputFilePath);
        PrintWriter pw1 = new PrintWriter(wekaOutput);
        pw1.write("Accuracy; Precision; Recall; fmeasure; areaUnderROC;\n");
        pw1.write(accuracy + ";" + eval.precision(positiveValueIndexOfClassFeature) + ";"
                + eval.recall(positiveValueIndexOfClassFeature) + ";" + fmeasure + ";" + eval.areaUnderROC(positiveValueIndexOfClassFeature));
        pw1.flush();
        System.out.println("DATA WRITTEN IN "+outputFilePath);
        pw1.close();

//        System.out.println(pClassifierName + ";" + pModelName + ";" + eval.numTruePositives(positiveValueIndexOfClassFeature) + ";"
//                + eval.numFalsePositives(positiveValueIndexOfClassFeature) + ";" + eval.numFalseNegatives(positiveValueIndexOfClassFeature) + ";"
//                + eval.numTrueNegatives(positiveValueIndexOfClassFeature) + ";" + accuracy + ";" + eval.precision(positiveValueIndexOfClassFeature) + ";"
//                + eval.recall(positiveValueIndexOfClassFeature) + ";" + fmeasure + ";" + eval.areaUnderROC(positiveValueIndexOfClassFeature) + "\n");
    }
}
