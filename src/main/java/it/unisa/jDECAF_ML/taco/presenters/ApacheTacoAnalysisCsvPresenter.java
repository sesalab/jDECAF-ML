package it.unisa.jDECAF_ML.taco.presenters;

import it.unisa.jDECAF_ML.taco.detectors.AnalyzedComponent;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

public class ApacheTacoAnalysisCsvPresenter extends TacoAnalysisCSVPresenter {

    public ApacheTacoAnalysisCsvPresenter(String outputDirectoryPath) {
        super(outputDirectoryPath);
    }

    @Override
    protected void writeOutputFile(File outputFile, Collection<AnalyzedComponent> analyzedComponents) throws IOException {
        CSVPrinter printer = new CSVPrinter(new FileWriter(outputFile), CSVFormat.DEFAULT.withHeader(Headers.class));
        for (AnalyzedComponent component: analyzedComponents){
            printer.printRecord(component.getComponent().getQualifiedName(), component.getAnalyzedSmell(), component.getSmellinessProbability());
        }
        printer.flush();
        printer.close();
    }
}
