/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.jDECAF_ML.execution;

import decor.ClassDataShouldBePrivateRule;
import decor.ComplexClassRule;
import decor.DetectionRule;
import decor.FeatureEnvyRule;
import decor.GodClassRule;
import decor.InappropriateIntimacyRule;
import decor.LazyClassRule;
import decor.LongMethodRule;
import decor.LongParameterListRule;
import decor.MiddleManRule;
import decor.RefusedBequestRule;
import decor.SpaghettiCodeRule;
import decor.SpeculativeGeneralityRule;
import it.unisa.jDECAF_ML.bean.Checkout;
import it.unisa.jDECAF_ML.bean.Git;
import it.unisa.jDECAF_ML.metrics.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.metrics.parser.bean.MethodBean;
import it.unisa.jDECAF_ML.smell.ClassDataShouldBePrivate;
import it.unisa.jDECAF_ML.smell.CodeSmell;
import it.unisa.jDECAF_ML.smell.ComplexClass;
import it.unisa.jDECAF_ML.smell.FatRepository;
import it.unisa.jDECAF_ML.smell.FeatureEnvy;
import it.unisa.jDECAF_ML.smell.GodClass;
import it.unisa.jDECAF_ML.smell.InappropriateIntimacy;
import it.unisa.jDECAF_ML.smell.LazyClass;
import it.unisa.jDECAF_ML.smell.LongParameterList;
import it.unisa.jDECAF_ML.smell.MiddleMan;
import it.unisa.jDECAF_ML.smell.PromiscuousController;
import it.unisa.jDECAF_ML.smell.RefusedBequest;
import it.unisa.jDECAF_ML.smell.SmartController;
import it.unisa.jDECAF_ML.smell.SmartRepository;
import it.unisa.jDECAF_ML.smell.SpaghettiCode;
import it.unisa.jDECAF_ML.smell.SpeculativeGenerality;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import meka.classifiers.multilabel.BR;
import mulan.classifier.transformation.BinaryRelevance;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.pmml.jaxbbindings.DecisionTree;

/**
 *
 * @author fably
 */
public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {

        String baseFolder = args[0];//"D:/Google Drive/Unisa/PhD/Progetti/CodeSmells/jDecaf/data"
        String smellName = args[1];//"LazyClass"
        //baseFolder = "/home/fabiano/Desktop/jdecaf";
        String repoURL = args[2];//"https://github.com/apache/derby.git"
        String folderName = args[3];//"derby"
        String tag = args[4];//"10.3.3.0"
        String projectName = folderName + "/" + tag;
        String outputFolder = baseFolder + "/outputs/ML_CB/" + smellName;
        String oracle = args[5];//"D:/Google Drive/Unisa/PhD/Progetti/CodeSmells/jDecaf/data/dataset/apache-derby-data/10.3_OK/Validated/candidate_Large_Class.csv"
        boolean classSmell = false;

        // CodeSmell spaghettiCode =  new SpaghettiCode("Spaghetti Code", "/home/fabiano/Desktop/jdecaf/dataset/apache-ant-data/apache_1.6/Validated/candidate_Spaghetti_Code.csv");
        CodeSmell smell = null;
        DetectionRule dr = null;
        switch (smellName) {
            case "LargeClass":
                smell = new GodClass(smellName, oracle + "/candidate_Large_Class.csv");
                classSmell = true;
                dr = new GodClassRule();
                break;
            case "ClassDataShouldBePrivate":
                smell = new ClassDataShouldBePrivate(smellName, oracle + "/candidate_Class_Data_Should_Be_Private.csv");
                classSmell = true;
                dr = new ClassDataShouldBePrivateRule();
                break;
            case "SpaghettiCode":
                smell = new SpaghettiCode(smellName, oracle + "/candidate_Spaghetti_Code.csv");
                classSmell = true;
                dr = new SpaghettiCodeRule();
                break;
            case "ComplexClass":
                smell = new ComplexClass(smellName, oracle + "/candidate_Complex_Class.csv");
                classSmell = true;
                dr = new ComplexClassRule();
                break;
            case "FeatureEnvy":
                smell = new FeatureEnvy(smellName, oracle + "/candidate_Feature_Envy.csv");
                dr = new FeatureEnvyRule();
                classSmell = false;
                break;
            case "LongMethod":
                smell = new FeatureEnvy(smellName, oracle + "/candidate_Long_Methods.csv");
                classSmell = false;
                dr = new LongMethodRule();
                break;
            case "LazyClass":
                smell = new LazyClass(smellName, oracle + "/candidate_Lazy_Class.csv");
                classSmell = true;
                dr = new LazyClassRule();
                break;
            case "InappropriateIntimacy":
                smell = new InappropriateIntimacy(smellName, oracle + "/candidate_Inappropriate_Intimacy.csv");
                classSmell = true;
                dr = new InappropriateIntimacyRule();
                break;
            case "SpeculativeGenerality":
                smell = new SpeculativeGenerality(smellName, oracle + "/candidate_Speculative_Generality.csv");
                classSmell = true;
                dr = new SpeculativeGeneralityRule();
                break;
            case "RefusedBequest":
                smell = new RefusedBequest(smellName, oracle + "/candidate_Refused_Bequest.csv");
                classSmell = true;
                dr = new RefusedBequestRule();
                break;
            case "MiddleMan":
                smell = new MiddleMan(smellName, oracle + "/candidate_Middle_Man.csv");
                classSmell = true;
                dr = new MiddleManRule();
                break;
            case "LongParameterList":
                smell = new LongParameterList(smellName, oracle + "/candidate_Long_Parameter_List.csv");
                classSmell = false;
                dr = new LongParameterListRule();
                break;
            /*case "FatRepository":
                smell = new FatRepository(smellName, null);
                classSmell = true;
                dr = null;
                break;
            case "SmartController":
                smell = new SmartController(smellName, null);
                classSmell = true;
                dr = null;
                break;
            case "SmartRepository":
                smell = new SmartRepository(smellName, null);
                classSmell = true;
                dr = null;
                break;
            case "PromiscuousController":
                smell = new PromiscuousController(smellName, null);
                classSmell = true;
                dr = null;
                break;*/
            default:
                break;
            //     throw new InvalidParameterException(smellName+" not found");
        }

        Git.clone(repoURL, false, projectName, baseFolder, tag);
        Checkout checkout = new Checkout(projectName, baseFolder, outputFolder, true);
        CalculateMetrics cm = new CalculateMetrics(projectName, baseFolder, outputFolder, smell, classSmell, smell.getMetrics(), tag);
      
        List<ClassBean> classes = cm.getSystem();
        List<MethodBean> methods = new ArrayList<>();
        for (ClassBean cb : classes) {
            methods.addAll(cb.getMethods());
        }

        System.out.println(smellName);
        if (smellName.equals("LazyClass")) {
            ((LazyClassRule) dr).setProjectClasses(classes);
        }
        else if (smellName.equals("InappropriateIntimacy")) {
            ((InappropriateIntimacyRule) dr).setProjectClasses(classes);
        }
        else if (smellName.equals("SpeculativeGenerality")) {
            ((SpeculativeGeneralityRule) dr).setProjectClasses(classes);
        }
        else if (smellName.equals("RefusedBequest")) {
            ((RefusedBequestRule) dr).setProjectClasses(classes);
        }
        else if (smellName.equals("MiddleMan")) {
            ((MiddleManRule) dr).setProjectClasses(classes);
        }
        else if (smellName.equals("LongParameterList")) {
            ((LongParameterListRule) dr).setProjectClasses(classes);
        }

        // System.out.println("SYSTEM SIZEEEEEEEEE: "+classes.size());
        //System.out.println("METHODS SIZEEEEEEEEE: "+methods.size());
        new CalculateMetrics(projectName, baseFolder, outputFolder, smell, classSmell, smell.getMetrics(), tag);
        new WekaEvaluator(outputFolder + "/" + projectName + "/data.csv", outputFolder + "/" + projectName + "/output.csv", new NaiveBayes(), 30);
        System.out.println(outputFolder + "/" + projectName + "/data.csv");
        new BalancingComparison(outputFolder + "/" + projectName + "/data.csv", outputFolder + "/" + projectName + "/balancing.csv", outputFolder + "/" + projectName + "/overlap.csv", new NaiveBayes(), 6, classes, methods, classSmell, dr);


        //new BalancingComparison_NB(outputFolder + "/" + projectName + "/data.csv", outputFolder + "/" + projectName + "/balancing_NB.csv", outputFolder + "/" + projectName + "/overlap.csv", new NaiveBayes(), 1, classes, methods, classSmell, dr);
        //new BalancingComparison_CB(outputFolder + "/" + projectName + "/data.csv", outputFolder + "/" + projectName + "/balancing_CB.csv", outputFolder + "/" + projectName + "/overlap.csv", new NaiveBayes(), 1, classes, methods, classSmell, dr);
        //new BalancingComparison_R(outputFolder + "/" + projectName + "/data.csv", outputFolder + "/" + projectName + "/balancing_R.csv", outputFolder + "/" + projectName + "/overlap.csv", new NaiveBayes(), 1, classes, methods, classSmell, dr);
        //new BalancingComparison_SM(outputFolder + "/" + projectName + "/data.csv", outputFolder + "/" + projectName + "/balancing_SM.csv", outputFolder + "/" + projectName + "/overlap.csv", new NaiveBayes(), 1, classes, methods, classSmell, dr);
     
        //new BalancingComparison_CSC(outputFolder + "/" + projectName + "/data.csv", outputFolder + "/" + projectName + "/balancing_CSC.csv", outputFolder + "/" + projectName + "/overlap.csv", new NaiveBayes(), 1, classes, methods, classSmell, dr);
        //new BalancingComparison_OCC(outputFolder + "/" + projectName + "/data.csv", outputFolder + "/" + projectName + "/balancing_OCC.csv", outputFolder + "/" + projectName + "/overlap.csv", new NaiveBayes(), 1, classes, methods, classSmell, dr);
        
        
        //new WekaNoBalance(outputFolder + "/" + projectName + "/data.csv", outputFolder + "/" + projectName + "/output.csv", outputFolder + "/" + projectName + "/predictions.csv", new NaiveBayes(), 10, classes, methods, classSmell, dr);
        //new CompareClassifiers(outputFolder + "/" + projectName + "/data.csv", outputFolder + "/" + projectName + "/classifiers.csv", 1);
        //new BaselineClassifiers(outputFolder + "/" + projectName + "/data.csv", outputFolder + "/" + projectName + "/baseline.csv", 1);
        //new CreatePythonScript(outputFolder, projectName, metrics, smells, 10);

    } 
}
