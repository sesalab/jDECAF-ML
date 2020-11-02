package it.unisa.jDECAF_ML.execution;

import it.unisa.jDECAF_ML.bean.FileBean;
import it.unisa.jDECAF_ML.bean.Git;
import it.unisa.jDECAF_ML.metrics.ReadSourceCode;
import it.unisa.jDECAF_ML.parser.bean.ClassBean;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProjectParser {
    public ProjectParser() {
    }

    List<ClassBean> getAllProjectClassBeans(String version, String projectPath) throws IOException {
        List<FileBean> repoFiles = Git.gitList(new File(projectPath), version);
        System.out.println("Repo Files Size: " + repoFiles.size());
        List<ClassBean> projectClasses = new ArrayList<ClassBean>();
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
}
