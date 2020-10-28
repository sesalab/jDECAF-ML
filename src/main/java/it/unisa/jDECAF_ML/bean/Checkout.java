package it.unisa.jDECAF_ML.bean;


import java.io.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Checkout {

    public Checkout(String projectName, String baseFolderPath, String scatteringFolderPath, boolean init) {

        System.out.println("Start checkout");
        Process process = new Process();

        if (init == true) {
            process.initGitRepository(baseFolderPath +"/"+ projectName);
            File scatteringProjectFolder = new File(scatteringFolderPath +"/"+ projectName);
            scatteringProjectFolder.mkdirs();
            process.saveGitRepository(scatteringFolderPath + "/"+ projectName + "/gitRepository.data");
        } else {
            process.initGitRepositoryFromFile(scatteringFolderPath + "/"+  projectName
                    + "/gitRepository.data");
        }
        System.out.println("Init done");
    }
}
