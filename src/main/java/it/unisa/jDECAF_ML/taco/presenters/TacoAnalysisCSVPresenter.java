package it.unisa.jDECAF_ML.taco.presenters;

import it.unisa.jDECAF_ML.taco.detectors.AnalyzedComponent;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public abstract class TacoAnalysisCSVPresenter implements TacoAnalysisPresenter {

    protected enum Headers {
        ComponentName,
        AnalyzedSmell,
        SmellinessProbability
    }

    protected final String outputDirectoryPath;

    protected TacoAnalysisCSVPresenter(String outputDirectoryPath) {
        this.outputDirectoryPath = outputDirectoryPath;
    }

    @Override
    public void present(Collection<AnalyzedComponent> analyzedComponents) {
        try {
            File outputFile = getOutputFile();
            writeOutputFile(outputFile, analyzedComponents);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    protected File getOutputFile() {
        File outputDirectory = new File(outputDirectoryPath);
        if (!outputDirectory.exists()){
            outputDirectory.mkdirs();
        }
        return new File(outputDirectory.getAbsolutePath() + File.separator + "taco_output.csv");
    }

    protected abstract void writeOutputFile(File outputFile, Collection<AnalyzedComponent> analyzedComponents) throws IOException;
}
