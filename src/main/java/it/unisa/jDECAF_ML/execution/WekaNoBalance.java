/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.jDECAF_ML.execution;

import decor.DetectionRule;
import it.unisa.jDECAF_ML.metrics.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.metrics.parser.bean.MethodBean;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.Set;
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
import weka.filters.supervised.instance.ClassBalancer;
import weka.filters.supervised.instance.SMOTE;
import weka.filters.unsupervised.instance.RemoveDuplicates;

/**
 *
 * @author fabiano
 */
public class WekaNoBalance {

    public WekaNoBalance(String predictorsFilePath, String outputFilePath, String predictionsFilePath, Classifier classifier, int rep, List<ClassBean> classes, List<MethodBean> methods, boolean classSmell, DetectionRule dr) {

        String filePath = predictorsFilePath;
        try {
            DataSource source = new DataSource(filePath);
            Instances instances = source.getDataSet();
            instances.setClassIndex(instances.numAttributes() - 1);
            //  System.out.println("Numero istanze: " + instances.size());

            evaluateModel(outputFilePath, predictionsFilePath, classifier, instances, rep, classes, methods, classSmell, dr);

        } catch (Exception ex) {
            Logger.getLogger(WekaNoBalance.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void evaluateModel(String outputFilePath, String predictionsFilePath, Classifier pClassifier, Instances pInstances, int rep, List<ClassBean> classes, List<MethodBean> methods, boolean classSmell, DetectionRule detector) throws FileNotFoundException {

        // other options
        Hashtable<String, String> hash = new Hashtable<>();
        int folds = 10;
        double accuracy = 0, precision = 0, recall = 0, MCC = 0, fmeasure = 0, auc = 0, tp = 0, fp = 0, tn = 0, fn = 0;
        boolean failed = false;
        String output = "TP;FP;FN;TN;Accuracy;Precision;Recall;fmeasure;areaUnderROC;MCC\n";
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
        //  System.out.println("AFTER: " + pInstances.size());
        //String 
        // randomize data
        for (int r = 0; r < rep; r++) {
            List<String> names = new ArrayList<>();
            Random rand = new Random();

            Instances randData = new Instances(pInstances);

            randData.randomize(rand);

            // if (randData.classAttribute().isNominal()) {
            //  }
//        for(Instance i : randData){
//            if(i.attribute(count).isNominal())
//        }
            try {
                Evaluation eval = new Evaluation(randData);

                int positiveValueIndexOfClassFeature = 0;
                int offset = 0;
                for (int n = 0; n < folds; n++) {
                    Instances trainingSet = randData.trainCV(folds, n, rand);
                    Instances testSet = randData.testCV(folds, n);

                    trainingSet.deleteAttributeAt(0);
                    Filter featureSelectionFilter = createFeatureSelectionFilter(trainingSet);
                    trainingSet = Filter.useFilter(trainingSet, featureSelectionFilter);

                    for (Instance tsi : testSet) {
                        names.add(tsi.stringValue(tsi.attribute(0)));
                    }
                    testSet.deleteAttributeAt(0);
//                    System.out.println("NUM ATTRIBUTES = " + testSet.numAttributes());
//                    for (int ind = 0; ind < testSet.size(); ind++) {
//                        String toPrint = "";
//                        toPrint += testSet.get(ind).stringValue(testSet.attribute(0));
//                        for (int i = 1; i < testSet.numAttributes(); i++) {
//
//                            toPrint += " - " + testSet.get(ind).value(testSet.attribute(i));
//                        }
//
//                        System.out.println(toPrint);
//                    }
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

//                    for (int i = 0; i < testSet.numAttributes(); i++) {
//                        System.out.println(testSet.attribute(i).name());
//                    }
                    pClassifier.buildClassifier(trainingSet);
                    eval.evaluateModel(pClassifier, testSet);

                    offset += testSet.size();
                    // System.out.println(eval.predictions().get(0).toString());
                    //System.out.println(eval.predictions().get(0).actual() + " - " + eval.predictions().get(0).predicted());
                }
                int k = 0;
                if (r == 0) {
                    for (String s : names) {
                        hash.put(s, eval.predictions().get(k++).predicted() + "");
                    }
                } else if (r == rep - 1) {
                    for (String s : names) {
                        hash.replace(s, hash.get(s) + ";" + eval.predictions().get(k).predicted() + ";" + eval.predictions().get(k).actual());
                        k++;
                    }
                } else {
                    for (String s : names) {
                        hash.replace(s, hash.get(s) + ";" + eval.predictions().get(k++).predicted());
                    }
                }
                //System.out.println("SIZE COMPARISON: " + eval.predictions().size() + " - " + names.size());
                //  System.out.println("\nConfusion Matrix:");
                tp = eval.numTruePositives(positiveValueIndexOfClassFeature);
                tn = eval.numTrueNegatives(positiveValueIndexOfClassFeature);
                fp = eval.numFalsePositives(positiveValueIndexOfClassFeature);
                fn = eval.numFalseNegatives(positiveValueIndexOfClassFeature);
                /*System.out.println(eval.numTruePositives(positiveValueIndexOfClassFeature) + "\t" + eval.numFalsePositives(positiveValueIndexOfClassFeature) + "\n"
                        + eval.numFalseNegatives(positiveValueIndexOfClassFeature) + "\t" + eval.numTrueNegatives(positiveValueIndexOfClassFeature) + "\n\n");
                 */
                accuracy = (tp + tn) / (tp + fp + fn + tn);
                precision = eval.precision(positiveValueIndexOfClassFeature);
                recall = eval.recall(positiveValueIndexOfClassFeature);
                auc = eval.areaUnderROC(positiveValueIndexOfClassFeature);
                fmeasure = 2 * ((precision * recall) / (precision + recall));
                MCC = eval.matthewsCorrelationCoefficient(positiveValueIndexOfClassFeature);//(tp * tn) - (fp * fn)) / (Math.sqrt(((tp + fp) * (tp + fn) * (tn + fp) * (tn + fn))));
                // TP, FP, FN, TN, Accuracy; Precision; Recall; fmeasure; areaUnderROC; MCC

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
            output += tp + ";" + fp + ";" + fn + ";" + tn + ";" + accuracy + ";" + precision + ";" + recall + ";" + fmeasure + ";" + auc + ";" + MCC + "\n";
            failed = false;

        }
        // System.out.println("HASHTABLE SIZE: " + hash.size());
        File wekaOutput = new File(outputFilePath);
        File predictionsOut = new File(predictionsFilePath);
        PrintWriter pw1 = new PrintWriter(wekaOutput);
        PrintWriter pw2 = new PrintWriter(predictionsOut);
        Set<String> names = hash.keySet();
        if (classSmell && classes != null) {
            for (String n : names) {
                for (ClassBean c : classes) {
                    //System.out.println(n+" - " +c.getBelongingPackage().replace('.', '/').toString() + "/" + c.getName() + ".java");
                    if (n.equals(c.getBelongingPackage().toString() + "." + c.getName() + ".java")) {
                        double dec = detector.isSmelly(c) ? 1 : 0;
                        hash.replace(n, hash.get(n) + ";" + dec);
                    }
                }
            }
        } else if (methods != null){
            for (String n : names) {
                for (MethodBean m : methods) {
                    //System.out.println(n+" - " +c.getBelongingPackage().replace('.', '/').toString() + "/" + c.getName() + ".java");
                    if (n.equals(m.getBelongingClass().getBelongingPackage().toString() + "." + m.getBelongingClass().getName() + ".java/" + m.getName())) {
                        double dec = detector.isSmelly(m) ? 1 : 0;
                        hash.replace(n, hash.get(n) + ";" + dec);
                    }
                }
            }
        }
        pw2.write("instance");
        for (int r = 0; r < rep; r++) {

            pw2.write(";R" + r);
        }
        pw2.write(";actual;DECOR\n");
        for (String s : names) {
            pw2.write(s + ";" + hash.get(s) + "\n");
        }
        pw1.write(output);
        // pw1.write(accuracy + ";" + precision + ";" + recall + ";" + fmeasure + ";" + auc + ";" + MCC + "\n");
        pw1.flush();
        pw2.flush();
        System.out.println("DATA WRITTEN IN " + outputFilePath);
        pw1.close();
        pw2.close();

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
        ClassBalancer filter = new ClassBalancer();
        filter.setInputFormat(instances);

        return filter;
    }
}
