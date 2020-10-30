package it.unisa.jDECAF_ML.execution;

import it.unisa.jDECAF_ML.bean.FileBean;
import it.unisa.jDECAF_ML.bean.Git;
import it.unisa.jDECAF_ML.metrics.ReadSourceCode;
import it.unisa.jDECAF_ML.metrics.classmetrics.ClassMetric;
import it.unisa.jDECAF_ML.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.smell.CodeSmell;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

class CalculateMLMetrics {

    public CalculateMLMetrics(String projectName, String baseFolderPath, String outputFolderPath, List<CodeSmell> smells, List<ClassMetric> classMetrics, String version) {

//        Process process = new Process();
//
//        process.initGitRepositoryFromFile(outputFolderPath + "/" + projectName
//                + "/gitRepository.data");
        try {

            File outputFolder = new File(outputFolderPath + "/" + projectName);
            outputFolder.mkdirs();

            File outputData = new File(outputFolderPath + "/" + projectName
                    + "/data.csv");
            PrintWriter pw = new PrintWriter(outputData);

            pw.write("name,");
            for (ClassMetric cm : classMetrics) {
                pw.write(cm.getName() + ",");
            }
            int i = 0;
            for (i = 0; i < smells.size() - 1; i++) {
                pw.write(smells.get(i).getName() + ",");
            }
            pw.write(smells.get(i).getName() + "\n");

            /*leggi oracolo*/
            String projectPath = baseFolderPath + "/" + projectName;

            List<FileBean> repoFiles = Git.gitList(new File(projectPath), version);
            System.out.println("SIZEEEEEEEEEEEEE " + repoFiles.size());
            ArrayList<ClassBean> System = new ArrayList<>();
            for (FileBean file : repoFiles) {
                if (file.getPath().contains(".java")) {
                    File workTreeFile = new File(projectPath + "/" + file.getPath());
                    if (workTreeFile.exists()) {
                        ArrayList<ClassBean> code = new ArrayList<>();
                        System.addAll(ReadSourceCode.readSourceCode(workTreeFile, code));
                    }
                }
            }
            for (FileBean file : repoFiles) {
                double[] values = new double[classMetrics.size()];
                i = 0;
                if (file.getPath().contains(".java")) {
                    File workTreeFile = new File(projectPath + "/" + file.getPath());

                    ClassBean classBean;
                    int[] smellPresences = new int[smells.size()];
                    if (workTreeFile.exists()) {
                        ArrayList<ClassBean> code = new ArrayList<>();
                        ArrayList<ClassBean> classes = ReadSourceCode.readSourceCode(workTreeFile, code);
                        if (classes.size() > 0) {
                            classBean = classes.get(0);
                            for (ClassMetric cm : classMetrics) {
                                values[i++] = cm.evaluate(classBean, System);
                            }
                            int x = 0;
                            for (CodeSmell smell : smells) {
                                smellPresences[x++] = smell.affectsComponent(classBean)? 1:0;
                            }

                        }

                        String message = "";

                        message += file.getPath() + ",";
                        for (i = 0; i < classMetrics.size(); i++) {
                            message += values[i] + ",";
                        }
                        for (i = 0; i < smells.size() - 1; i++) {
                            message += smellPresences[i] + ",";
                        }
                        message += smellPresences[i] + "\n";

                        pw.write(message);
                    }
                }
            }
            pw.flush();

        } catch (NumberFormatException | IOException ex) {
            Logger.getLogger(CalculateMLMetrics.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
