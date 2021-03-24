/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.jDECAF_ML.execution;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.attributeSelection.CfsSubsetEval;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.Instance;
import weka.filters.Filter;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.supervised.instance.SMOTE;

/**
 *
 * @author fabiano
 */
public class WekaEvaluator {

    public WekaEvaluator(String predictorsFilePath, String outputFilePath, Classifier classifier, int rep) {

        String filePath = predictorsFilePath;
        try {
            DataSource source = new DataSource(filePath);
            Instances instances = source.getDataSet();
            instances.setClassIndex(instances.numAttributes() - 1);
          //  System.out.println("Numero istanze: " + instances.size());
            evaluateModel(predictorsFilePath, outputFilePath, classifier, instances, rep);
        } catch (Exception ex) {
            Logger.getLogger(WekaEvaluator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void evaluateModel(String predictorsFilePath, String outputFilePath, Classifier pClassifier, Instances pInstances, int rep) throws FileNotFoundException {

        // other options
        int folds = 10;
        double accuracy = 0, precision = 0, recall = 0, MCC = 0, fmeasure = 0, auc = 0;
        boolean failed = false;
        // randomize data
        for (int r = 0; r < rep; r++) {
            Random rand = new Random();
            Instances randData = new Instances(pInstances);
            randData.randomize(rand);
            // if (randData.classAttribute().isNominal()) {
            randData.stratify(folds);
            //  }
//        for(Instance i : randData){
//            if(i.attribute(count).isNominal())
//        }
            try {
                Evaluation eval = new Evaluation(randData);

                int positiveValueIndexOfClassFeature = 0;
                for (int n = 0; n < folds; n++) {
                    Instances trainingSet = randData.trainCV(folds, n, rand);
                    Instances testSet = randData.testCV(folds, n);

                    Filter featureSelectionFilter = createFeatureSelectionFilter(trainingSet);
                    trainingSet = Filter.useFilter(trainingSet, featureSelectionFilter);
                    testSet = Filter.useFilter(testSet, featureSelectionFilter);

                    Filter dataBalancingFilter = createBalancingFilter(trainingSet);
                    trainingSet = Filter.useFilter(trainingSet, dataBalancingFilter);

                    int classFeatureIndex = 0;
                    for (int i = 0; i < trainingSet.numAttributes(); i++) {
                        if (trainingSet.attribute(i).name().equals("isSmelly")) {
                            classFeatureIndex = i;
                            break;
                        }
                    }

                    Attribute classFeature = trainingSet.attribute(classFeatureIndex);
                    for (int i = 0; i < classFeature.numValues(); i++) {
                        if (classFeature.value(i).equals("true")) {
                            positiveValueIndexOfClassFeature = i;
                        }
                    }

                    trainingSet.setClassIndex(classFeatureIndex);
                    testSet.setClassIndex(classFeatureIndex);

                    pClassifier.buildClassifier(trainingSet);
                    eval.evaluateModel(pClassifier, testSet);

                }
              //  System.out.println("\nConfusion Matrix:");
                double tp = eval.numTruePositives(positiveValueIndexOfClassFeature);
                double tn = eval.numTrueNegatives(positiveValueIndexOfClassFeature);
                double fp = eval.numFalsePositives(positiveValueIndexOfClassFeature);
                double fn = eval.numFalseNegatives(positiveValueIndexOfClassFeature);
                /*System.out.println(eval.numTruePositives(positiveValueIndexOfClassFeature) + "\t" + eval.numFalsePositives(positiveValueIndexOfClassFeature) + "\n"
                        + eval.numFalseNegatives(positiveValueIndexOfClassFeature) + "\t" + eval.numTrueNegatives(positiveValueIndexOfClassFeature) + "\n\n");
*/
                accuracy += (tp + tn) / (tp + fp + fn + tn);
                precision += eval.precision(positiveValueIndexOfClassFeature);
                recall += eval.recall(positiveValueIndexOfClassFeature);
                auc += eval.areaUnderROC(positiveValueIndexOfClassFeature);
                MCC += ((tp * tn) - (fp * fn)) / (Math.sqrt(((tp + fp) * (tp + fn) * (tn + fp) * (tn + fn))));
                failed = false;
            } catch (Exception e) {
                e.printStackTrace();
                failed = true;
            }
        }
        if (!failed) {
            accuracy /= rep;
            precision /= rep;
            recall /= rep;
            fmeasure = 2 * ((precision * recall) / (precision + recall));
            auc /= rep;
            MCC /= rep;
        }
        else {
            accuracy = -1;
            precision = -1;
            recall = -1;
            fmeasure = -1;
            auc = -1;
            MCC = -1;
        }
        File wekaOutput = new File(outputFilePath);
        PrintWriter pw1 = new PrintWriter(wekaOutput);
        pw1.write("Accuracy; Precision; Recall; fmeasure; areaUnderROC; MCC\n");
        pw1.write(accuracy + ";" + precision + ";" + recall + ";" + fmeasure + ";" + auc + ";" + MCC + "\n");
        pw1.flush();
    //    System.out.println("DATA WRITTEN IN " + outputFilePath);
        pw1.close();

//        System.out.println(pClassifierName + ";" + pModelName + ";" + eval.numTruePositives(positiveValueIndexOfClassFeature) + ";"
//                + eval.numFalsePositives(positiveValueIndexOfClassFeature) + ";" + eval.numFalseNegatives(positiveValueIndexOfClassFeature) + ";"
//                + eval.numTrueNegatives(positiveValueIndexOfClassFeature) + ";" + accuracy + ";" + eval.precision(positiveValueIndexOfClassFeature) + ";"
//                + eval.recall(positiveValueIndexOfClassFeature) + ";" + fmeasure + ";" + eval.areaUnderROC(positiveValueIndexOfClassFeature) + "\n");
    }

    private static Filter createFeatureSelectionFilter(Instances instances) throws Exception {
        AttributeSelection filter = new AttributeSelection();
        filter.setEvaluator(new CfsSubsetEval());
        filter.setInputFormat(instances);

        return filter;
    }

    private static Filter createBalancingFilter(Instances instances) throws Exception {
        SMOTE filter = new SMOTE();
        filter.setInputFormat(instances);

        return filter;
    }
}
