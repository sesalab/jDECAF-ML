package it.unisa.jDECAF_ML.taco;

import it.unisa.jDECAF_ML.parser.bean.ClassBean;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class MethodTextualMetricsExtractor {

    private static final Logger log = LogManager.getLogger(ClassTextualMetricsExtractor.class);

    enum Headers {
        METHOD_QUALIFIED_NAME,
        TEXTUAL_COHESION,
        WORDS_ENTROPY
    }
    private final CSVPrinter printer;

    public MethodTextualMetricsExtractor(File outputFile) throws IOException {
        printer = new CSVPrinter(new FileWriter(outputFile), CSVFormat.DEFAULT.withHeader(ClassTextualMetricsExtractor.Headers.class));
    }

    public void extractMetrics(List<ClassBean> projectClasses){
        log.info("Starting method textual metrics extraction");
        projectClasses.forEach(clazz -> clazz.getMethods().forEach(method ->{
            try {
                log.info("Calculating metrics on "+method.getQualifiedName());
                printer.printRecord(method.getQualifiedName(),method.textualMethodCohesion(), method.wordsEntropy());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }

}
