package it.unisa.jDECAF_ML.execution;

import it.unisa.jDECAF_ML.bean.FileBean;
import it.unisa.jDECAF_ML.bean.Git;
import it.unisa.jDECAF_ML.metrics.Metric;
import it.unisa.jDECAF_ML.metrics.ReadSourceCode;
import it.unisa.jDECAF_ML.metrics.classmetrics.ClassMetric;
import it.unisa.jDECAF_ML.metrics.methodmetrics.MethodMetric;
import it.unisa.jDECAF_ML.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.parser.bean.MethodBean;
import it.unisa.jDECAF_ML.smell.CodeSmell;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

class CalculateMetrics {

    private List<ClassBean> projectClasses = new ArrayList<>();

    public CalculateMetrics(String projectName, String baseFolderPath, String outputFolderPath, CodeSmell smell, boolean classSmell, List<Metric> classMetrics, String version) {

        try {

            System.out.println("Calculating metrics...");
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
            projectClasses = getAllProjectClassBeans(version, projectPath);

            for(ClassBean candidateClass : projectClasses){
                computeSmellMetricsAndMessage(smell, classSmell, classMetrics, pw, candidateClass);
            }

            pw.flush();

        } catch (NumberFormatException | IOException ex) {
            Logger.getLogger(CalculateMetrics.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void computeSmellMetricsAndMessage(CodeSmell smell, boolean classSmell, List<Metric> classMetrics, PrintWriter pw, ClassBean classBean) {
        boolean isSmelly;
        double[] values = new double[classMetrics.size()];
        int i = 0;
        if (classSmell) {
            for (Metric m : classMetrics) {
                values[i++] = ((ClassMetric) m).evaluate(classBean, new ArrayList<>(projectClasses));
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

    private List<ClassBean> getAllProjectClassBeans(String version, String projectPath) throws IOException {
        List<FileBean> repoFiles = Git.gitList(new File(projectPath), version);
        System.out.println("Repo Files Size: " + repoFiles.size());
        List<ClassBean> projectClasses = new ArrayList<>();
        for (FileBean file : repoFiles) {
            if (file.getPath().contains(".java")) {
                File workTreeFile = new File(projectPath + "/" + file.getPath());
                if (workTreeFile.exists()) {
                    projectClasses.addAll(ReadSourceCode.readSourceCode(workTreeFile));
                }
            }
        }
        return projectClasses;
    }

    public List<ClassBean> getSystem() {
        return projectClasses;
    }
}
