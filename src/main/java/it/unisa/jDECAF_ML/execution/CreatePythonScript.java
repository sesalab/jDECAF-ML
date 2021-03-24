/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.jDECAF_ML.execution;

import it.unisa.jDECAF_ML.metrics.classmetrics.ClassMetric;
import it.unisa.jDECAF_ML.smell.CodeSmell;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fably
 */
public class CreatePythonScript {

    private String outputFolderPath;
    private String projectName;
    private List<ClassMetric> metrics;
    private List<CodeSmell> smells;
    private int numberOfFolds;

    public CreatePythonScript(String outputFolderPath, String projectName, List<ClassMetric> metrics, List<CodeSmell> smells, int numberOfFolds) {
        this.outputFolderPath = outputFolderPath;
        this.projectName = projectName;
        this.metrics = metrics;
        this.smells = smells;
        this.numberOfFolds = numberOfFolds;
        try {

            File outputData = new File("C:/Users/fably/Desktop/script.py");

            PrintWriter pw = new PrintWriter(outputData);

            writeIntestation(pw);
            writeDatasetLoading(pw);
            writekFoldCrossValidation(pw);
            writeEvaluationPhase(pw);
            pw.flush();
            pw.close();
            System.out.println("Executing 'python C:/Users/fably/Desktop/script.py'");
            Process process = Runtime.getRuntime().exec("python C:/Users/fably/Desktop/script.py");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CreatePythonScript.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CreatePythonScript.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void writeIntestation(PrintWriter pw) {
        pw.write("import csv\n"
                + "import pandas as pd \n"
                + "from sklearn.metrics import classification_report\n"
                + "from sklearn.metrics import accuracy_score\n"
                + "from sklearn.metrics import precision_score\n"
                + "from sklearn.metrics import recall_score\n"
                + "from sklearn.metrics import roc_auc_score\n"
                + "from skmultilearn.problem_transform import BinaryRelevance\n"
                + "from sklearn.naive_bayes import GaussianNB\n"
                + "from sklearn.tree import DecisionTreeClassifier\n\n");
    }

    private void writeDatasetLoading(PrintWriter pw) {
        pw.write("df = pd.read_csv(\"" + outputFolderPath + "/" + projectName + "/data_oracle.csv\", header=0) \n"
                + "df.head()\n"
                + "print(list(df.columns.values))\n"
                + "df = df.dropna()\n"
                + "df = df.reset_index(drop=True)\n"
                + "X = df[[");
        writeMetricNames(pw);
        pw.write("]]\n"
                + "y = df[[");
        writeSmellNames(pw);
        pw.write("]]\n"
                + "print(\"NUMBER OF ROWS:\")\n"
                + "print(len(df))\n\n");
    }

    private void writekFoldCrossValidation(PrintWriter pw) {
        pw.write("folds = " + numberOfFolds + "\n"
                + "fold_size = len(df)/folds + 0.000000000001\n"
                + "classifier = BinaryRelevance(DecisionTreeClassifier())\n"
                + "predictions = pd.DataFrame(columns=[");
        writeSmellNames(pw);
        pw.write("])\n"
                + "for x in range(0, folds):\n"
                + "\ttest_beg = round(x*fold_size)\n"
                + "\ttest_end = round(x*fold_size + fold_size -1)\n"
                + "\tX_test = X.loc[test_beg:test_end]\n"
                + "\tX_train = pd.concat([X.loc[0:test_beg-1],X.loc[test_end+1:]])\n"
                + "\ty_train = pd.concat([y.loc[0:test_beg-1],y.loc[test_end+1:]])\n"
                + "\tclassifier.fit(X_train, y_train)\n"
                + "\tpredictions = pd.concat([predictions,pd.DataFrame(classifier.predict(X_test).todense(),columns=[");
        writeSmellNames(pw);
        pw.write("])])\n\n"
                + "predictions = predictions.reset_index(drop=True)\n\n");
    }

    private void writeEvaluationPhase(PrintWriter pw) {
        for (CodeSmell s : smells) {
            pw.write("accuracy = accuracy_score(y['" + s.getName() + "'].astype('bool'), predictions['" + s.getName() + "'].astype('bool'))\n"
                    + "precision = precision_score(y['" + s.getName() + "'].astype('bool'), predictions['" + s.getName() + "'].astype('bool'))\n"
                    + "recall = recall_score(y['" + s.getName() + "'].astype('bool'), predictions['" + s.getName() + "'].astype('bool'))\n"
                    + "fmeasure = \"NaN\"\n"
                    + "if (precision + recall) != 0:\n"
                    + "\tfmeasure = 2 * ((precision * recall) / (precision + recall))\n"
                    + "try:\n"
                    + "\tauc = roc_auc_score(y['"+s.getName()+"'].astype('bool'), predictions['"+s.getName()+"'].astype('bool'))\n"
                    + "except ValueError:\n"
                    + "\tauc = \"NaN\""
                    + "\n"
                    + "with open('" + outputFolderPath + "/" + projectName + "/" + s.getName() + "_output.csv', mode='w') as file:\n"
                    + "    writer = csv.writer(file, delimiter=';', quotechar='\"', quoting=csv.QUOTE_MINIMAL, lineterminator='\\n')\n"
                    + "    writer.writerow(['Accuracy', 'Precision', 'Recall', 'fmeasure', 'areaUnderROC'])\n"
                    + "    writer.writerow([accuracy, precision, recall, fmeasure, auc])\n\n");
        }
    }

    private void writeSmellNames(PrintWriter pw) {
        int i = 0;
        for (; i < smells.size() - 1; i++) {
            pw.write("'" + smells.get(i).getName() + "', ");
        }
        pw.write("'" + smells.get(i).getName() + "'");
    }

    private void writeMetricNames(PrintWriter pw) {
        int i = 0;
        for (; i < metrics.size() - 1; i++) {
            pw.write("'" + metrics.get(i).getName() + "', ");
        }
        pw.write("'" + metrics.get(i).getName() + "'");
    }
}
