package it.unisa.jDECAF_ML.hist;

import org.repodriller.RepositoryMining;
import org.repodriller.Study;
import org.repodriller.filter.commit.OnlyModificationsWithFileTypes;
import org.repodriller.filter.range.Commits;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.GitRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;

public class HistStudy implements Study {

    private final CommitVisitor classVisitor;
    private final String projectPath;
    private final PersistenceMechanism classWriter;
    private final CommitVisitor methodVisitor;
    private final PersistenceMechanism methodWriter;

    public HistStudy(CommitVisitor classVisitor, String projectPath, PersistenceMechanism classWriter, CommitVisitor methodVisitor, PersistenceMechanism methodWriter) {
        this.classVisitor = classVisitor;
        this.projectPath = projectPath;
        this.classWriter = classWriter;
        this.methodVisitor = methodVisitor;
        this.methodWriter = methodWriter;
    }

    @Override
    public void execute() {

        try {
            String repoTempDirectory = Files.createTempDirectory("HIST").toFile().getAbsolutePath();

            new RepositoryMining()
                    .in(GitRepository.singleProject(projectPath))
                    .setRepoTmpDir(Paths.get(repoTempDirectory))
                    .through(Commits.all())
                    .filters(new OnlyModificationsWithFileTypes(Collections.singletonList(".java")))
                    .process(classVisitor, classWriter)
                    .process(methodVisitor,methodWriter)
                    .visitorsAreThreadSafe(true)
                    .visitorsChangeRepoState(false)
                    .withThreads()
                    .mine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
