package it.unisa.jDECAF_ML.hist;

import it.unisa.jDECAF_ML.metrics.ReadSourceCode;
import it.unisa.jDECAF_ML.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.parser.bean.MethodBean;
import org.repodriller.domain.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CommitParsingUtils {
    public CommitParsingUtils() {
    }

    public List<MethodBean> getMethodsFrom(List<ClassBean> classes) {
        return classes.stream().flatMap(clazz -> clazz.getMethods().stream()).collect(Collectors.toList());
    }

    public Set<MethodBean> getChangedMethods(Commit commit) {
        Set<MethodBean> result = new HashSet<>();

        for (Modification m : commit.getModifications()) {
            if (!m.fileNameEndsWith(".java")) {
                continue;
            }

            List<ClassBean> classesInFile = ReadSourceCode.readSourceCodeFromString(m.getSourceCode());
            List<MethodBean> methodsInFile = getMethodsFrom(classesInFile);

            DiffParser diffParser = new DiffParser(m.getDiff());
            List<DiffBlock> diffBlocks = diffParser.getBlocks();

            for (DiffBlock block : diffBlocks) {
                List<DiffLine> linesInNewFile = block.getLinesInNewFile();
                int firstLineNumber = linesInNewFile.get(0).getLineNumber();
                int lastLineNumber = linesInNewFile.get(linesInNewFile.size() - 1).getLineNumber();

                for (MethodBean method : methodsInFile) {
                    if ((firstLineNumber <= method.getEndLine()) && (lastLineNumber >= method.getStartLine())) {
                        result.add(method);
                    }
                }

            }

        }

        return result;
    }
}
