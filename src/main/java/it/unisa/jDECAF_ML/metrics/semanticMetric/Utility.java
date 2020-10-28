package it.unisa.jDECAF_ML.metrics.semanticMetric;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Utility {

    public static String readFile(String nomeFile) {
        StringBuilder sb = new StringBuilder();
        char[] buf = new char[1024];
        int len;

        try (InputStream is = new FileInputStream(nomeFile)) {
            InputStreamReader isr = new InputStreamReader(is);
            while ((len = isr.read(buf)) > 0) {
                sb.append(buf, 0, len);
            }
        } catch (IOException ex) {
            Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, ex);
        }

        return sb.toString();

    }

    public static void writeFile(String pContent, String pPath) {
        File file = new File(pPath);
        try (FileWriter fstream = new FileWriter(file);
             BufferedWriter out = new BufferedWriter(fstream)) {
            out.write(pContent);
        } catch (IOException ex) {
            Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
