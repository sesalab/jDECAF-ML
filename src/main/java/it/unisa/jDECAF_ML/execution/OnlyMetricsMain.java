package it.unisa.jDECAF_ML.execution;

import it.unisa.jDECAF_ML.hist.ClassHistoricalMetricsExtractorVisitor;
import it.unisa.jDECAF_ML.hist.HistStudy;
import it.unisa.jDECAF_ML.hist.MethodHistoricalMetricsExtractor;
import it.unisa.jDECAF_ML.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.smell.AllMethodMetricsSmell;
import it.unisa.jDECAF_ML.smell.AllMetricsSmell;
import it.unisa.jDECAF_ML.taco.ClassTextualMetricsExtractor;
import it.unisa.jDECAF_ML.taco.MethodTextualMetricsExtractor;
import it.unisa.jDECAF_ML.utils.Git;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.repodriller.RepoDriller;
import org.repodriller.Study;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.persistence.csv.CSVFile;
import org.repodriller.scm.CommitVisitor;

import java.io.*;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class OnlyMetricsMain {
    public static void main(String[] args) throws IOException, InterruptedException, GitAPIException {
        String baseFolder = args[0];    //"D:/Google Drive/Unisa/PhD/Progetti/CodeSmells/jDecaf/data"
        //baseFolder = "/home/fabiano/Desktop/jdecaf";
        String folderName = args[1];//"derby"
        String repoURL = args[2];//"https://github.com/apache/derby.git"
        String tag = args[3];   //"10.3.3.0"
        String projectName = folderName + "-" + tag.replace('/','-');
        String outputFolder = Paths.get(baseFolder,"metrics",projectName).toString();
        //String oracle = args[4];    //"D:/Google Drive/Unisa/PhD/Progetti/CodeSmells/jDecaf/data/dataset/apache-derby-data/10.3_OK/Validated/candidate_Large_Class.csv"


        String repoPath = Paths.get(baseFolder, folderName).toString();
        Git.clone(repoURL, repoPath);
        Git.checkoutToTag(repoPath, tag);

        List<ClassBean> allProjectClasses = parseProject(repoPath, tag);

        CalculateMetrics.Input calculateMetricsInput = new CalculateMetrics.Input(new AllMetricsSmell(), true, allProjectClasses);
        CalculateMetrics cm = new CalculateMetrics(projectName, outputFolder, calculateMetricsInput);
        cm.execute();

        CalculateMetrics.Input calculateMetricsInput2 = new CalculateMetrics.Input(new AllMethodMetricsSmell(), false, allProjectClasses);
        CalculateMetrics cm2 = new CalculateMetrics(projectName, outputFolder, calculateMetricsInput2);
        cm2.execute();

        //extractHistoricalMetrics(allProjectClasses, repoPath, outputFolder);


//        new Thread(() -> {
//            try {
//                ClassTextualMetricsExtractor classTextualMetricsExtractor = new ClassTextualMetricsExtractor(new File(outputFolder + File.separator + "ClassTextualMetrics.csv"));
//                classTextualMetricsExtractor.extractMetrics(allProjectClasses);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }).start();
//
//        new Thread(() -> {
//            try {
//                MethodTextualMetricsExtractor methodTextualMetricsExtractor = new MethodTextualMetricsExtractor(new File(outputFolder + File.separator + "MethodTextualMetrics.csv"));
//                methodTextualMetricsExtractor.extractMetrics(allProjectClasses);
//            } catch (IOException e){
//                e.printStackTrace();
//            }
//        }).start();

        //generateOracleDataset(oracle, outputFolder);

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

    private static List<ClassBean> parseProject(String repoPath, String tag) throws IOException {
        ProjectParser projectParser = new ProjectParser();
        return projectParser.getAllProjectClassBeans(tag,repoPath);
    }

    private static void generateOracleDataset(String oraclePath, String outputFolder) throws IOException {
        String[] classSmells = {"Large_Class","Spaghetti_Code","Complex_Class"};
        String[] methodSmells = {"Feature_Envy","Long_Methods"};
        final String classOutputFilename = "AllClassSmells.csv";
        final String methodOutputFilename = "AllMethodSmells.csv";

        for (String smell : classSmells){

            FileWriter out = new FileWriter(Paths.get(outputFolder,"transformed_"+smell+".csv").toString());
            CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader("ClassQualifiedName",smell));

            Reader reader = new FileReader(Paths.get(oraclePath,"candidate_"+smell+".csv").toString());
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .withDelimiter(';')
                    .parse(reader);
            for (CSVRecord record : records){
                String qualifiedName = record.get(1).trim() + "." + record.get(0).trim();
                printer.printRecord(qualifiedName, "1");
            }

            printer.flush();
            printer.close();

        }

        for (String smell : methodSmells){

            FileWriter out = new FileWriter(Paths.get(outputFolder,"transformed_"+smell+".csv").toString());
            CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader("MethodQualifiedName",smell));

            Reader reader = new FileReader(Paths.get(oraclePath,"candidate_"+smell+".csv").toString());
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .withDelimiter(';')
                    .parse(reader);
            for (CSVRecord record : records){
                String qualifiedName = record.get(2).trim() + "." + record.get(1).trim() + "." + record.get(0).trim();
                printer.printRecord(qualifiedName, "1");
            }

            printer.flush();
            printer.close();

        }

    }
}
