package it.unisa.jDECAF_ML.metrics.parser;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.Collection;
import org.eclipse.jdt.core.dom.BlockComment;

public class ClassVisitor extends ASTVisitor {

    private final Collection<TypeDeclaration> classNodes;

    public ClassVisitor(Collection<TypeDeclaration> pClassNodes) {
        classNodes = pClassNodes;
    }

    @Override
    public boolean visit(TypeDeclaration pClassNode) {
        classNodes.add(pClassNode);
        return true;
    }
 

}
