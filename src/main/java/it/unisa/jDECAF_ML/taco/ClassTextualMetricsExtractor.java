package it.unisa.jDECAF_ML.taco;

import it.unisa.jDECAF_ML.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.taco.presenters.TacoAnalysisCSVPresenter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ClassTextualMetricsExtractor {

    private static final Logger log = LogManager.getLogger(ClassTextualMetricsExtractor.class);

    enum Headers {
        CLASS_QUALIFIED_NAME,
        TEXTUAL_COHESION,
        TEXTUAL_ENTROPY;
    }
    private final CSVPrinter printer;

    public ClassTextualMetricsExtractor(File outputFile) throws IOException {
        printer = new CSVPrinter(new FileWriter(outputFile), CSVFormat.DEFAULT.withHeader(Headers.class));
    }

    public void extractMetrics(List<ClassBean> projectClasses){
        log.info("Starting class textual metrics extraction");
        projectClasses.forEach(clazz ->{
            try {
                log.info("Calculating metrics on "+clazz.getQualifiedName());
                printer.printRecord(clazz.getQualifiedName(), clazz.textualClassCohesion(), clazz.wordsEntropy());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
