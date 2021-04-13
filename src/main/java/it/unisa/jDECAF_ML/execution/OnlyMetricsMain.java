package it.unisa.jDECAF_ML.execution;

import it.unisa.jDECAF_ML.bean.Checkout;
import it.unisa.jDECAF_ML.bean.Git;
import it.unisa.jDECAF_ML.hist.ClassHistoricalMetricsExtractorVisitor;
import it.unisa.jDECAF_ML.hist.HistStudy;
import it.unisa.jDECAF_ML.hist.MethodHistoricalMetricsExtractor;
import it.unisa.jDECAF_ML.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.smell.AllMethodMetricsSmell;
import it.unisa.jDECAF_ML.smell.AllMetricsSmell;
import it.unisa.jDECAF_ML.smell.GodClass;
import org.repodriller.RepoDriller;
import org.repodriller.Study;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.persistence.csv.CSVFile;
import org.repodriller.scm.CommitVisitor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class OnlyMetricsMain {
    public static void main(String[] args) throws IOException, InterruptedException {
        String baseFolder = args[0];    //"D:/Google Drive/Unisa/PhD/Progetti/CodeSmells/jDecaf/data"
        //baseFolder = "/home/fabiano/Desktop/jdecaf";
        String folderName = args[1];//"derby"
        String repoURL = args[2];//"https://github.com/apache/derby.git"
        String tag = args[3];   //"10.3.3.0"
        String projectName = folderName + "-" + tag;
        String outputFolder = Paths.get(baseFolder,"metrics",projectName).toString();
        //String oracle = args[5];    //"D:/Google Drive/Unisa/PhD/Progetti/CodeSmells/jDecaf/data/dataset/apache-derby-data/10.3_OK/Validated/candidate_Large_Class.csv"



        Git.clone(repoURL, false, projectName, baseFolder, tag);
        Checkout checkout = new Checkout(projectName, baseFolder, outputFolder, true);

        List<ClassBean> allProjectClasses = parseProject(baseFolder, tag, projectName);

        CalculateMetrics.Input calculateMetricsInput = new CalculateMetrics.Input(new AllMetricsSmell(), true, allProjectClasses);
        CalculateMetrics cm = new CalculateMetrics(projectName, outputFolder, calculateMetricsInput);
        cm.execute();

        CalculateMetrics.Input calculateMetricsInput2 = new CalculateMetrics.Input(new AllMethodMetricsSmell(), false, allProjectClasses);
        CalculateMetrics cm2 = new CalculateMetrics(projectName, outputFolder, calculateMetricsInput2);
        cm2.execute();

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

    private static List<ClassBean> parseProject(String baseFolder, String tag, String projectName) throws IOException {
        String projectPath = baseFolder + "/" + projectName;
        ProjectParser projectParser = new ProjectParser();
        return projectParser.getAllProjectClassBeans(tag,projectPath);
    }
}
