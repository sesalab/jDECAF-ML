package it.unisa.jDECAF_ML.execution;

import it.unisa.jDECAF_ML.metrics.Metric;
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

    private final String outputFolderPath;
    private final String projectName;
    private List<ClassBean> projectClasses;
    private List<Metric> classMetrics;
    private CodeSmell smell;
    private boolean classSmell;

    public CalculateMetrics(String projectName, String outputFolderPath, Input input) {
        this.smell = input.smell;
        this.classMetrics = smell.getMetrics();
        this.classSmell = input.classSmell;
        this.projectClasses = input.projectClasses;
        this.outputFolderPath = outputFolderPath;
        this.projectName = projectName;
    }

    public void execute() {
        try {

            System.out.println("Calculating metrics...");
            File outputFolder = new File(this.outputFolderPath + "/" + this.projectName);
            outputFolder.mkdirs();

            File outputData = new File(this.outputFolderPath + "/" + this.projectName
                    + "/data.csv");
            PrintWriter pw = new PrintWriter(outputData);

            pw.write("name,");
            for (Metric m : classMetrics) {
                pw.write(m.getName() + ",");
            }
            pw.write("isSmelly\n");

            /*leggi oracolo*/

            for(ClassBean candidateClass : this.projectClasses){
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

    public List<ClassBean> getSystem() {
        return projectClasses;
    }

    static class Input {
        private final CodeSmell smell;
        private final boolean classSmell;
        private final List<ClassBean> projectClasses;

        Input(CodeSmell smell, boolean classSmell, List<ClassBean> projectClasses) {
            this.smell = smell;
            this.classSmell = classSmell;
            this.projectClasses = projectClasses;
        }

        public CodeSmell getSmell() {
            return smell;
        }

        public boolean isClassSmell() {
            return classSmell;
        }

        public List<ClassBean> getProjectClasses() {
            return projectClasses;
        }
    }
}
