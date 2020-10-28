package it.unisa.jDECAF_ML.execution;

import it.unisa.jDECAF_ML.bean.FileBean;
import it.unisa.jDECAF_ML.bean.Git;
import it.unisa.jDECAF_ML.metrics.CKMetrics;
import it.unisa.jDECAF_ML.metrics.ReadSourceCode;
import it.unisa.jDECAF_ML.metrics.parser.bean.ClassBean;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import it.unisa.jDECAF_ML.bean.Process;
import it.unisa.jDECAF_ML.metrics.Metric;
import it.unisa.jDECAF_ML.metrics.classmetrics.ClassMetric;
import it.unisa.jDECAF_ML.metrics.methodmetrics.MethodMetric;
import it.unisa.jDECAF_ML.metrics.parser.bean.MethodBean;
import it.unisa.jDECAF_ML.smell.CodeSmell;

class CalculateMetrics {

    private ArrayList<ClassBean> projectClasses = new ArrayList<>();

    public CalculateMetrics(String projectName, String baseFolderPath, String outputFolderPath, CodeSmell smell, boolean classSmell, List<Metric> classMetrics, String version) {

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
            for (Metric m : classMetrics) {
                pw.write(m.getName() + ",");
            }
            pw.write("isSmelly\n");

            /*leggi oracolo*/
            String projectPath = baseFolderPath + "/" + projectName;

            List<FileBean> repoFiles = Git.gitList(new File(projectPath), version);
            System.out.println("SIZEEEEEEEEEEEEE " + repoFiles.size());
            for (FileBean file : repoFiles) {
                if (file.getPath().contains(".java")) {
                    File workTreeFile = new File(projectPath + "/" + file.getPath());
                    if (workTreeFile.exists()) {
                        ArrayList<ClassBean> code = new ArrayList<>();
                        projectClasses.addAll(ReadSourceCode.readSourceCode(workTreeFile, code));
                    }
                }
            }
            for (FileBean file : repoFiles) {
                double[] values = new double[classMetrics.size()];
                int i = 0;
                if (file.getPath().contains(".java")) {
                    File workTreeFile = new File(projectPath + "/" + file.getPath());

                    ClassBean classBean;

                    if (workTreeFile.exists()) {
                        ArrayList<ClassBean> code = new ArrayList<>();
                        ArrayList<ClassBean> classes = ReadSourceCode.readSourceCode(workTreeFile, code);
                        boolean isSmelly = false;

                        if (classes.size() > 0) {
                            if (classSmell) {
                                classBean = classes.get(0);
                                for (Metric m : classMetrics) {
                                    values[i++] = ((ClassMetric) m).evaluate(classBean, projectClasses);
                                }
                                isSmelly = smell.affectsComponent(classBean);
                                String message = "";

                                message += classBean.getBelongingPackage() + "." + classBean.getName() + ".java" + ",";
                                for (i = 0; i < classMetrics.size(); i++) {
                                    message += values[i] + ",";
                                }
                                message += isSmelly + "\n";

                                pw.write(message);
                            } else {
                                classBean = classes.get(0);
                                for (MethodBean mb : classBean.getMethods()) {
                                    values = new double[classMetrics.size()];
                                    i = 0;
                                    for (Metric m : classMetrics) {
                                        values[i++] = ((MethodMetric) m).evaluate(mb);
                                    }
                                    isSmelly = smell.affectsComponent(mb);
                                    String message = "";

                                    message += mb.getBelongingClass().getBelongingPackage() + "." + mb.getBelongingClass().getName() + ".java/" + mb.getName() + ",";
                                    for (i = 0; i < classMetrics.size(); i++) {
                                        message += values[i] + ",";
                                    }
                                    message += isSmelly + "\n";

                                    pw.write(message);
                                }
                            }
                        }
                    }
                }
            }
            pw.flush();

        } catch (NumberFormatException | IOException ex) {
            Logger.getLogger(CalculateMetrics.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<ClassBean> getSystem() {
        return projectClasses;
    }
}
