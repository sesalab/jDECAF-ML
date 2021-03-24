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
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.pmml.consumer.SupportVectorMachineModel;
import weka.classifiers.rules.JRip;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.Instance;
import weka.filters.Filter;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.supervised.instance.SMOTE;
import weka.gui.beans.TrainingSetEvent;

/**
 *
 * @author fabiano
 */
public class CompareClassifiers {

    public CompareClassifiers(String predictorsFilePath, String outputFilePath, int rep) {

        String filePath = predictorsFilePath;
        try {
            DataSource source = new DataSource(filePath);
            Instances instances = source.getDataSet();
            instances.setClassIndex(instances.numAttributes() - 1);
            // System.out.println("Numero istanze: " + instances.size());
            evaluateModel(predictorsFilePath, outputFilePath, instances, rep);
        } catch (Exception ex) {
            Logger.getLogger(CompareClassifiers.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void evaluateModel(String predictorsFilePath, String outputFilePath, Instances pInstances, int rep) throws FileNotFoundException, Exception {

        List<Classifier> classifiers = new ArrayList<>();
        classifiers.add(new NaiveBayes());
        classifiers.add(new J48());
        classifiers.add(new RandomForest());
        classifiers.add(new JRip());
        classifiers.add(new LibSVM());
        
        //classifiers.add(new OptimizedSVM().searchParams(pInstances));
        double[] mcc = new double[4];
        int count = 0;
Instances temp = pInstances;
        List<Instance> toRemove = new ArrayList<>();
        // System.out.println("BEFORE: " + pInstances.size());
        for (int i = 0; i < temp.size(); i++) {
            for (int j = 0; j < temp.size(); j++) {
                if (i != j && temp.get(i).stringValue(temp.attribute(0)).equals(temp.get(j).stringValue(temp.attribute(0)))) {
                    // System.out.println("REMOVING: " + temp.get(j).stringValue(temp.attribute(0)));
                    toRemove.add(temp.get(j));
                }
            }
        }
        pInstances.removeAll(toRemove);
        String output = "Classifier; TP;FP;FN;TN;Accuracy;Precision;Recall;fmeasure;areaUnderROC;MCC\n";
        for (Classifier c : classifiers) {
            // other options
            int folds = 10;
            double accuracy = 0, precision = 0, recall = 0, MCC = 0, fmeasure = 0, auc = 0, tp = 0, fp = 0, tn = 0, fn = 0;
            boolean failed = false;
            // randomize dataF
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

                    trainingSet.deleteAttributeAt(0);
                    Filter featureSelectionFilter = createFeatureSelectionFilter(trainingSet);
                    trainingSet = Filter.useFilter(trainingSet, featureSelectionFilter);
                    testSet.deleteAttributeAt(0);
                    testSet = Filter.useFilter(testSet, featureSelectionFilter);

                 //   Filter dataBalancingFilter = createBalancingFilter(trainingSet);
                  //  trainingSet = Filter.useFilter(trainingSet, dataBalancingFilter);

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

                    c.buildClassifier(trainingSet);
                    eval.evaluateModel(c, testSet);

                }
                //System.out.println("\nConfusion Matrix:");
                tp = eval.numTruePositives(positiveValueIndexOfClassFeature);
                tn = eval.numTrueNegatives(positiveValueIndexOfClassFeature);
                fp = eval.numFalsePositives(positiveValueIndexOfClassFeature);
                fn = eval.numFalseNegatives(positiveValueIndexOfClassFeature);
                /* System.out.println(eval.numTruePositives(positiveValueIndexOfClassFeature) + "\t" + eval.numFalsePositives(positiveValueIndexOfClassFeature) + "\n"
                            + eval.numFalseNegatives(positiveValueIndexOfClassFeature) + "\t" + eval.numTrueNegatives(positiveValueIndexOfClassFeature) + "\n\n");
                 */
                accuracy = (tp + tn) / (tp + fp + fn + tn);
                precision = eval.precision(positiveValueIndexOfClassFeature);
                recall = eval.recall(positiveValueIndexOfClassFeature);
                auc = eval.areaUnderROC(positiveValueIndexOfClassFeature);
                MCC = eval.matthewsCorrelationCoefficient(positiveValueIndexOfClassFeature);//(tp * tn) - (fp * fn)) / (Math.sqrt(((tp + fp) * (tp + fn) * (tn + fp) * (tn + fn))));
                failed = false;
            } catch (Exception e) {
                e.printStackTrace();
                failed = true;

                tp = Double.NaN;
                fp = Double.NaN;
                fn = Double.NaN;
                tn = Double.NaN;
                accuracy = Double.NaN;
                precision = Double.NaN;
                recall = Double.NaN;
                fmeasure = Double.NaN;
                auc = Double.NaN;
                MCC = Double.NaN;
            }
            String name = c.getClass().getName();
            String[] split = name.split("\\.");
            name = split[split.length - 1];
            output += name + ";" + tp + ";" + fp + ";" + fn + ";" + tn + ";" + accuracy + ";" + precision + ";" + recall + ";" + fmeasure + ";" + auc + ";" + MCC + "\n";
            failed = false;
        }
//        System.out.println(pClassifierName + ";" + pModelName + ";" + eval.numTruePositives(positiveValueIndexOfClassFeature) + ";"
//                + eval.numFalsePositives(positiveValueIndexOfClassFeature) + ";" + eval.numFalseNegatives(positiveValueIndexOfClassFeature) + ";"
//                + eval.numTrueNegatives(positiveValueIndexOfClassFeature) + ";" + accuracy + ";" + eval.precision(positiveValueIndexOfClassFeature) + ";"
//                + eval.recall(positiveValueIndexOfClassFeature) + ";" + fmeasure + ";" + eval.areaUnderROC(positiveValueIndexOfClassFeature) + "\n");
//        
//            // other options
//            int folds = 10;
//            double accuracy = 0, precision = 0, recall = 0, MCC = 0, fmeasure = 0, auc = 0, tp = 0, fp = 0, tn = 0, fn = 0;
//            boolean failed = false;
//            // randomize dataF
//            Random rand = new Random();
//            Instances randData = new Instances(pInstances);
//            randData.randomize(rand);
//            // if (randData.classAttribute().isNominal()) {
//            randData.stratify(folds);
//            //  }
////        for(Instance i : randData){
////            if(i.attribute(count).isNominal())
////        }
//            try {
//                Evaluation eval = new Evaluation(randData);
//
//                int positiveValueIndexOfClassFeature = 0;
//                for (int n = 0; n < folds; n++) {
//                    Instances trainingSet = randData.trainCV(folds, n, rand);
//                    Instances testSet = randData.testCV(folds, n);
//
//                    Filter featureSelectionFilter = createFeatureSelectionFilter(trainingSet);
//                    trainingSet = Filter.useFilter(trainingSet, featureSelectionFilter);
//                    testSet = Filter.useFilter(testSet, featureSelectionFilter);
//
//                    Filter dataBalancingFilter = createBalancingFilter(trainingSet);
//                    trainingSet = Filter.useFilter(trainingSet, dataBalancingFilter);
//
//                    int classFeatureIndex = 0;
//                    for (int i = 0; i < trainingSet.numAttributes(); i++) {
//                        if (trainingSet.attribute(i).name().equals("isSmelly")) {
//                            classFeatureIndex = i;
//                            break;
//                        }
//                    }
//
//                    Attribute classFeature = trainingSet.attribute(classFeatureIndex);
//                    for (int i = 0; i < classFeature.numValues(); i++) {
//                        if (classFeature.value(i).equals("true")) {
//                            positiveValueIndexOfClassFeature = i;
//                        }
//                    }
//
//                    trainingSet.setClassIndex(classFeatureIndex);
//                    testSet.setClassIndex(classFeatureIndex);
//
//                    Classifier c = OptimizedSVM.searchParams(trainingSet);
//                    eval.evaluateModel(c, testSet);
//
//                }
//                //System.out.println("\nConfusion Matrix:");
//                tp = eval.numTruePositives(positiveValueIndexOfClassFeature);
//                tn = eval.numTrueNegatives(positiveValueIndexOfClassFeature);
//                fp = eval.numFalsePositives(positiveValueIndexOfClassFeature);
//                fn = eval.numFalseNegatives(positiveValueIndexOfClassFeature);
//                /* System.out.println(eval.numTruePositives(positiveValueIndexOfClassFeature) + "\t" + eval.numFalsePositives(positiveValueIndexOfClassFeature) + "\n"
//                            + eval.numFalseNegatives(positiveValueIndexOfClassFeature) + "\t" + eval.numTrueNegatives(positiveValueIndexOfClassFeature) + "\n\n");
//                 */
//                accuracy += (tp + tn) / (tp + fp + fn + tn);
//                precision += eval.precision(positiveValueIndexOfClassFeature);
//                recall += eval.recall(positiveValueIndexOfClassFeature);
//                auc += eval.areaUnderROC(positiveValueIndexOfClassFeature);
//                MCC += ((tp * tn) - (fp * fn)) / (Math.sqrt(((tp + fp) * (tp + fn) * (tn + fp) * (tn + fn))));
//                failed = false;
//            } catch (Exception e) {
//                failed = true;
//
//                tp = Double.NaN;
//                fp = Double.NaN;
//                fn = Double.NaN;
//                tn = Double.NaN;
//                accuracy = Double.NaN;
//                precision = Double.NaN;
//                recall = Double.NaN;
//                fmeasure = Double.NaN;
//                auc = Double.NaN;
//                MCC = Double.NaN;
//            }
//
//            output += "SVM;" + tp + ";" + fp + ";" + fn + ";" + tn + ";" + accuracy + ";" + precision + ";" + recall + ";" + fmeasure + ";" + auc + ";" + MCC + "\n";
//            failed = false;
//        System.out.println(pClassifierName + ";" + pModelName + ";" + eval.numTruePositives(positiveValueIndexOfClassFeature) + ";"
//                + eval.numFalsePositives(positiveValueIndexOfClassFeature) + ";" + eval.numFalseNegatives(positiveValueIndexOfClassFeature) + ";"
//                + eval.numTrueNegatives(positiveValueIndexOfClassFeature) + ";" + accuracy + ";" + eval.precision(positiveValueIndexOfClassFeature) + ";"
//                + eval.recall(positiveValueIndexOfClassFeature) + ";" + fmeasure + ";" + eval.areaUnderROC(positiveValueIndexOfClassFeature) + "\n");

        File wekaOutput = new File(outputFilePath);
        PrintWriter pw1 = new PrintWriter(wekaOutput);
        pw1.write(output);
        pw1.flush();
//            System.out.println("DATA WRITTEN IN " + outputFilePath);
        pw1.close();

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
