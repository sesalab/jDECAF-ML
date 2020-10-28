package it.unisa.jDECAF_ML.metrics;

import it.unisa.jDECAF_ML.metrics.parser.ClassParser;
import it.unisa.jDECAF_ML.metrics.parser.ClassVisitor;
import it.unisa.jDECAF_ML.metrics.parser.bean.ClassBean;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadSourceCode {

    public static ArrayList<ClassBean> readSourceCodeFromString(String source, ArrayList<ClassBean> classes) {

        // Get the package
        String regex = "package .*;";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source);

        String belongingPackage = "";

        if (matcher.find()) {
            belongingPackage = matcher.group();
        }

        belongingPackage = belongingPackage.replace("package ", "");
        belongingPackage = belongingPackage.replace(";", "");

        regex = "import .*;";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(source);

        ArrayList<String> imports = new ArrayList<>();

        while (matcher.find()) {
            String tmpImport = matcher.group();
            if (!tmpImport.startsWith("java.")) {
                imports.add(tmpImport);
            }
        }

        ASTParser parser = ASTParser.newParser(AST.JLS8);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setSource(source.toCharArray());

        CompilationUnit unit = (CompilationUnit) parser.createAST(null);

        Collection<TypeDeclaration> classNodes = new ArrayList<>();
        unit.accept(new ClassVisitor(classNodes));

        ArrayList<ClassBean> classBeans = new ArrayList<>();
        for (TypeDeclaration classNode : classNodes) {
            classBeans.add(ClassParser.parse(classNode, belongingPackage,
                    imports));
        }

        if (classBeans.size() > 0) {
            classes.add(classBeans.get(0));
        }

        return classes;

    }

    public static ArrayList<ClassBean> readSourceCode(File path,
                                                      ArrayList<ClassBean> classes) throws IOException {

        if (path.isDirectory() && !path.getName().equals(".DS_Store")
                && !path.getName().equals("bin")) {
            for (File f : path.listFiles()) {
                readSourceCode(f, classes);
            }
        } else {
            if (path.getName().endsWith(".java")) {
                String source = readFile(path.getAbsolutePath());
                // Get the package
                String regex = "package .*;";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(source);

                String belongingPackage = "";

                if (matcher.find()) {
                    belongingPackage = matcher.group();
                }

                belongingPackage = belongingPackage.replace("package ", "");
                belongingPackage = belongingPackage.replace(";", "");

                regex = "import .*;";
                pattern = Pattern.compile(regex);
                matcher = pattern.matcher(source);

                ArrayList<String> imports = new ArrayList<>();

                while (matcher.find()) {
                    String tmpImport = matcher.group();
                    if (!tmpImport.startsWith("java.")) {
                        imports.add(tmpImport);
                    }
                }

                ASTParser parser = ASTParser.newParser(AST.JLS8);
                parser.setKind(ASTParser.K_COMPILATION_UNIT);
                parser.setSource(source.toCharArray());

                CompilationUnit unit = (CompilationUnit) parser.createAST(null);

                Collection<TypeDeclaration> classNodes = new ArrayList<>();
                unit.accept(new ClassVisitor(classNodes));

                ArrayList<ClassBean> classBeans = new ArrayList<>();
                for (TypeDeclaration classNode : classNodes) {
                    classBeans.add(ClassParser.parse(classNode,
                            belongingPackage, imports));
                }

                if (classBeans.size() > 0) {
                    classes.add(classBeans.get(0));
                }

            }
        }

        return classes;

    }

    private static String readFile(String nomeFile) throws IOException {
        InputStream is;
        InputStreamReader isr = null;

        StringBuilder sb = new StringBuilder();
        char[] buf = new char[1024];
        int len;

        try {
            is = new FileInputStream(nomeFile);
            isr = new InputStreamReader(is);

            while ((len = isr.read(buf)) > 0) {
                sb.append(buf, 0, len);
            }

            return sb.toString();
        } finally {
            if (isr != null) {
                isr.close();
            }
        }
    }

}
