/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.jDECAF_ML.execution;

import it.unisa.jDECAF_ML.oldgit.Checkout;
import it.unisa.jDECAF_ML.oldgit.Git;
import it.unisa.jDECAF_ML.decor.*;
import it.unisa.jDECAF_ML.hist.HistStudy;
import it.unisa.jDECAF_ML.hist.ClassHistoricalMetricsExtractorVisitor;
import it.unisa.jDECAF_ML.hist.MethodHistoricalMetricsExtractor;
import it.unisa.jDECAF_ML.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.parser.bean.MethodBean;
import it.unisa.jDECAF_ML.smell.*;
import it.unisa.jDECAF_ML.taco.ClassTextualMetricsExtractor;
import it.unisa.jDECAF_ML.taco.MethodTextualMetricsExtractor;
import it.unisa.jDECAF_ML.taco.detectors.BlobDetector;
import it.unisa.jDECAF_ML.taco.detectors.CodeSmellDetector;
import it.unisa.jDECAF_ML.taco.detectors.FeatureEnvyDetector;
import it.unisa.jDECAF_ML.taco.detectors.LongMethodDetector;
import org.repodriller.RepoDriller;
import org.repodriller.Study;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.persistence.csv.CSVFile;
import org.repodriller.scm.CommitVisitor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fably
 */
public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {

        String baseFolder = args[0];    //"D:/Google Drive/Unisa/PhD/Progetti/CodeSmells/jDecaf/data"
        String smellName = args[1]; //"LazyClass"
        //baseFolder = "/home/fabiano/Desktop/jdecaf";
        String repoURL = args[2];//"https://github.com/apache/derby.git"
        String folderName = args[3];//"derby"
        String tag = args[4];   //"10.3.3.0"
        String projectName = folderName + "/" + tag;
        String outputFolder = baseFolder + "/outputs/ML_CB/" + smellName;
        String oracle = args[5];    //"D:/Google Drive/Unisa/PhD/Progetti/CodeSmells/jDecaf/data/dataset/apache-derby-data/10.3_OK/Validated/candidate_Large_Class.csv"
        boolean classSmell = false;

        // CodeSmell spaghettiCode =  new SpaghettiCode("Spaghetti Code", "/home/fabiano/Desktop/jdecaf/dataset/apache-ant-data/apache_1.6/Validated/candidate_Spaghetti_Code.csv");
        CodeSmell smell = null;
        DetectionRule detectionRule = null;
        switch (smellName) {
            case "LargeClass":
                smell = new GodClass(smellName, oracle + "/candidate_Large_Class.csv");
                classSmell = true;
                detectionRule = new GodClassRule();
                break;
            case "ClassDataShouldBePrivate":
                smell = new ClassDataShouldBePrivate(smellName, oracle + "/candidate_Class_Data_Should_Be_Private.csv");
                classSmell = true;
                detectionRule = new ClassDataShouldBePrivateRule();
                break;
            case "SpaghettiCode":
                smell = new SpaghettiCode(smellName, oracle + "/candidate_Spaghetti_Code.csv");
                classSmell = true;
                detectionRule = new SpaghettiCodeRule();
                break;
            case "ComplexClass":
                smell = new ComplexClass(smellName, oracle + "/candidate_Complex_Class.csv");
                classSmell = true;
                detectionRule = new ComplexClassRule();
                break;
            case "FeatureEnvy":
                smell = new FeatureEnvy(smellName, oracle + "/candidate_Feature_Envy.csv");
                detectionRule = new FeatureEnvyRule();
                classSmell = false;
                break;
            case "LongMethod":
                smell = new FeatureEnvy(smellName, oracle + "/candidate_Long_Methods.csv");
                classSmell = false;
                detectionRule = new LongMethodRule();
                break;
            case "LazyClass":
                smell = new LazyClass(smellName, oracle + "/candidate_Lazy_Class.csv");
                classSmell = true;
                detectionRule = new LazyClassRule();
                break;
            case "InappropriateIntimacy":
                smell = new InappropriateIntimacy(smellName, oracle + "/candidate_Inappropriate_Intimacy.csv");
                classSmell = true;
                detectionRule = new InappropriateIntimacyRule();
                break;
            case "SpeculativeGenerality":
                smell = new SpeculativeGenerality(smellName, oracle + "/candidate_Speculative_Generality.csv");
                classSmell = true;
                detectionRule = new SpeculativeGeneralityRule();
                break;
            case "RefusedBequest":
                smell = new RefusedBequest(smellName, oracle + "/candidate_Refused_Bequest.csv");
                classSmell = true;
                detectionRule = new RefusedBequestRule();
                break;
            case "MiddleMan":
                smell = new MiddleMan(smellName, oracle + "/candidate_Middle_Man.csv");
                classSmell = true;
                detectionRule = new MiddleManRule();
                break;
            case "LongParameterList":
                smell = new LongParameterList(smellName, oracle + "/candidate_Long_Parameter_List.csv");
                classSmell = false;
                detectionRule = new LongParameterListRule();
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

        List<ClassBean> allProjectClasses = parseProject(baseFolder, tag, projectName);

        CalculateMetrics.Input calculateMetricsInput = new CalculateMetrics.Input(smell, classSmell, allProjectClasses);
        CalculateMetrics cm = new CalculateMetrics(projectName, outputFolder, calculateMetricsInput);
        cm.execute();

        List<ClassBean> classes = cm.getSystem();
        List<MethodBean> methods = new ArrayList<>();
        for (ClassBean cb : classes) {
            methods.addAll(cb.getMethods());
        }

        System.out.println(smellName);
        if (smellName.equals("LazyClass")) {
            ((LazyClassRule) detectionRule).setProjectClasses(classes);
        }
        else if (smellName.equals("InappropriateIntimacy")) {
            ((InappropriateIntimacyRule) detectionRule).setProjectClasses(classes);
        }
        else if (smellName.equals("SpeculativeGenerality")) {
            ((SpeculativeGeneralityRule) detectionRule).setProjectClasses(classes);
        }
        else if (smellName.equals("RefusedBequest")) {
            ((RefusedBequestRule) detectionRule).setProjectClasses(classes);
        }
        else if (smellName.equals("MiddleMan")) {
            ((MiddleManRule) detectionRule).setProjectClasses(classes);
        }
        else if (smellName.equals("LongParameterList")) {
            ((LongParameterListRule) detectionRule).setProjectClasses(classes);
        }

        String projectPathName = baseFolder + File.separator + projectName;
        String projectOutputFolderName = outputFolder + File.separator + projectName;
        extractHistoricalMetrics(allProjectClasses, projectPathName, projectOutputFolderName);

        ClassTextualMetricsExtractor classTextualMetricsExtractor = new ClassTextualMetricsExtractor(new File(projectOutputFolderName + File.separator + "ClassTextualMetrics.csv"));
        classTextualMetricsExtractor.extractMetrics(allProjectClasses);

        MethodTextualMetricsExtractor methodTextualMetricsExtractor = new MethodTextualMetricsExtractor(new File(projectOutputFolderName + File.separator + "MethodTextualMetrics.csv"));
        methodTextualMetricsExtractor.extractMetrics(allProjectClasses);
//            it.unisa.jDECAF_ML.hist.feature_envy.FeatureEnvyDetector detector  = new it.unisa.jDECAF_ML.hist.feature_envy.FeatureEnvyDetector(projectPath.getAbsolutePath());
//            List<AnalyzedComponent> historicalSmellyBean = detector.detectSmells(allProjectClasses);
//        System.out.println("Historical smelly component--------");
//        historicalSmellyBean.forEach(System.out::println);


//        CalculateMetrics calculateMetrics = new CalculateMetrics(projectName, outputFolder, calculateMetricsInput);
//        calculateMetrics.execute();
//
//        System.out.println("Performing TACO analysis...");
//        CodeSmellDetector detector = getSmellDetector(smellName, allProjectClasses);
//        TacoAnalysisPresenter csvPresenter = new ApacheTacoAnalysisCsvPresenter(outputFolder + File.separator + projectName);
//        TacoAnalysis tacoAnalysisUseCase = new TacoAnalysis(allProjectClasses, detector,csvPresenter);
//        tacoAnalysisUseCase.execute();
//
//        new WekaEvaluator(outputFolder + "/" + projectName + "/data.csv", outputFolder + "/" + projectName + "/output.csv", new NaiveBayes(), 30);
//        System.out.println(outputFolder + "/" + projectName + "/data.csv");
//        new BalancingComparison(outputFolder + "/" + projectName + "/data.csv", outputFolder + "/" + projectName + "/balancing.csv", outputFolder + "/" + projectName + "/overlap.csv", new NaiveBayes(), 6, classes, methods, classSmell, detectionRule);


        //new BalancingComparison_NB(outputFolder + "/" + projectName + "/data_oracle.csv", outputFolder + "/" + projectName + "/balancing_NB.csv", outputFolder + "/" + projectName + "/overlap.csv", new NaiveBayes(), 1, classes, methods, classSmell, dr);
        //new BalancingComparison_CB(outputFolder + "/" + projectName + "/data_oracle.csv", outputFolder + "/" + projectName + "/balancing_CB.csv", outputFolder + "/" + projectName + "/overlap.csv", new NaiveBayes(), 1, classes, methods, classSmell, dr);
        //new BalancingComparison_R(outputFolder + "/" + projectName + "/data_oracle.csv", outputFolder + "/" + projectName + "/balancing_R.csv", outputFolder + "/" + projectName + "/overlap.csv", new NaiveBayes(), 1, classes, methods, classSmell, dr);
        //new BalancingComparison_SM(outputFolder + "/" + projectName + "/data_oracle.csv", outputFolder + "/" + projectName + "/balancing_SM.csv", outputFolder + "/" + projectName + "/overlap.csv", new NaiveBayes(), 1, classes, methods, classSmell, dr);
     
        //new BalancingComparison_CSC(outputFolder + "/" + projectName + "/data_oracle.csv", outputFolder + "/" + projectName + "/balancing_CSC.csv", outputFolder + "/" + projectName + "/overlap.csv", new NaiveBayes(), 1, classes, methods, classSmell, dr);
        //new BalancingComparison_OCC(outputFolder + "/" + projectName + "/data_oracle.csv", outputFolder + "/" + projectName + "/balancing_OCC.csv", outputFolder + "/" + projectName + "/overlap.csv", new NaiveBayes(), 1, classes, methods, classSmell, dr);
        
        
        //new WekaNoBalance(outputFolder + "/" + projectName + "/data_oracle.csv", outputFolder + "/" + projectName + "/output.csv", outputFolder + "/" + projectName + "/predictions.csv", new NaiveBayes(), 10, classes, methods, classSmell, dr);
        //new CompareClassifiers(outputFolder + "/" + projectName + "/data_oracle.csv", outputFolder + "/" + projectName + "/classifiers.csv", 1);
        //new BaselineClassifiers(outputFolder + "/" + projectName + "/data_oracle.csv", outputFolder + "/" + projectName + "/baseline.csv", 1);
        //new CreatePythonScript(outputFolder, projectName, metrics, smells, 10);

    }

    private static void extractHistoricalMetrics(List<ClassBean> allProjectClasses, String projectPathName, String projectOutputFolderName) {
        ClassHistoricalMetricsExtractorVisitor historicalMetricsVisitor = new ClassHistoricalMetricsExtractorVisitor(allProjectClasses);
        String classMetricsOutputFileName = projectOutputFolderName + File.separator + "ClassHistoricalMetrics.csv";
        PersistenceMechanism classWriter = new CSVFile(classMetricsOutputFileName, new String[]{"ClassQualifiedName", "ChangePercentage"});

        CommitVisitor methodVisitor = new MethodHistoricalMetricsExtractor();
        String methodMetricsOutputFilename = projectOutputFolderName + File.separator + "MethodHistoricalMetrics.csv";
        PersistenceMechanism methodWriter = new CSVFile(methodMetricsOutputFilename, new String[]{"MethodQualifiedName","ChangePercentage"});

        Study histStudy = new HistStudy(historicalMetricsVisitor,projectPathName, classWriter, methodVisitor, methodWriter);
        new RepoDriller().start(histStudy);
    }

    private static CodeSmellDetector getSmellDetector(String smellName, List<ClassBean> allProjectClasses) {
        CodeSmellDetector detector;
        switch (smellName){
            case "LargeClass":
                detector = new BlobDetector();
                break;
            case "FeatureEnvy":
                detector = new FeatureEnvyDetector(allProjectClasses);
                break;
            case "LongMethod":
                detector = new LongMethodDetector();
                break;
            default:
                throw new IllegalArgumentException("There is no TACO detector for this smell");
        }
        return detector;
    }

    private static List<ClassBean> parseProject(String baseFolder, String tag, String projectName) throws IOException {
        String projectPath = baseFolder + "/" + projectName;
        ProjectParser projectParser = new ProjectParser();
        return projectParser.getAllProjectClassBeans(tag,projectPath);
    }
}
