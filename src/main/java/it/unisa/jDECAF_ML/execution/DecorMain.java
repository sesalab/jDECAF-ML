package it.unisa.jDECAF_ML.execution;

import it.unisa.jDECAF_ML.decor.*;
import it.unisa.jDECAF_ML.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.smell.SpaghettiCode;
import it.unisa.jDECAF_ML.utils.Git;
import meka.core.F;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class DecorMain {

    public static void main(String[] args) throws IOException, GitAPIException {
        String baseFolder = args[0];    //"D:/Google Drive/Unisa/PhD/Progetti/CodeSmells/jDecaf/data"
        //baseFolder = "/home/fabiano/Desktop/jdecaf";
        String folderName = args[1];//"derby"
        String repoURL = args[2];//"https://github.com/apache/derby.git"
        String tag = args[3];   //"10.3.3.0"
        String projectName = folderName + "-" + tag;
        String outputFolder = Paths.get(baseFolder,"decor",projectName).toString();

        String repoPath = Paths.get(baseFolder, folderName).toString();
        Git.clone(repoURL, repoPath);
        Git.checkoutToTag(repoPath, tag);

        List<ClassBean> allProjectClasses = parseProject(repoPath, tag);

        FileWriter classWriter = new FileWriter(Paths.get(outputFolder,"decor_class_output.csv").toString());
        CSVPrinter classPrinter = new CSVPrinter(classWriter, CSVFormat.DEFAULT.withHeader("ClassQualifiedName","Decor_Spaghetti_Code","Decor_Large_Class","Decor_Complex_Class"));

        FileWriter methodWriter = new FileWriter(Paths.get(outputFolder,"decor_method_output.csv").toString());
        CSVPrinter methodPrinter = new CSVPrinter(methodWriter, CSVFormat.DEFAULT.withHeader("MethodQualifiedName","Decor_Feature_Envy","Decor_long_Method"));

        SpaghettiCodeRule spaghettiCodeRule = new SpaghettiCodeRule();
        GodClassRule godClassRule = new GodClassRule();
        ComplexClassRule complexClassRule = new ComplexClassRule();
        LongMethodRule longMethodRule = new LongMethodRule();
        FeatureEnvyRule featureEnvyRule = new FeatureEnvyRule();

        allProjectClasses.forEach(classBean -> {
            try {

                String isSpaghetti = spaghettiCodeRule.isSmelly(classBean) ? "1" : "0";
                String isLarge = godClassRule.isSmelly(classBean) ? "1" : "0";
                String isComplex = complexClassRule.isSmelly(classBean) ? "1" : "0";

                classBean.getMethods().forEach(methodBean -> {
                    String isFE = featureEnvyRule.isSmelly(classBean) ? "1" : "0";
                    String isLong = longMethodRule.isSmelly(classBean) ? "1" : "0";

                    try {
                        methodPrinter.printRecord(methodBean.getQualifiedName(),isFE,isLong);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                classPrinter.printRecord(classBean.getQualifiedName(),isSpaghetti,isLarge,isComplex);
            } catch (IOException e) {
                e.printStackTrace();
            }


        });

    }

    private static List<ClassBean> parseProject(String repoPath, String tag) throws IOException {
        ProjectParser projectParser = new ProjectParser();
        return projectParser.getAllProjectClassBeans(tag,repoPath);
    }
}
