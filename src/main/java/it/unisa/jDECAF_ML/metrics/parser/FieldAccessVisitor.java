package it.unisa.jDECAF_ML.metrics.parser;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.Collection;
import org.eclipse.jdt.core.dom.FieldAccess;

class FieldAccessVisitor extends ASTVisitor {

    private final Collection<String> foreignAccessedFields;
    private final Collection<String> selfAccessedFields;

    public FieldAccessVisitor(Collection<String> selfAccessedFields, Collection<String> foreignAccessedFields) {
        this.selfAccessedFields = selfAccessedFields;
        this.foreignAccessedFields = foreignAccessedFields;
    }

    @Override
    public boolean visit(FieldAccess accessedField) {
        if (accessedField.getExpression().toString().equals("this")) {
            selfAccessedFields.add(accessedField.getName().toString());
        } else {
            foreignAccessedFields.add(accessedField.getName().toString());
        }
        return true;
    }

    @Override
    public boolean visit(TypeDeclaration pClassNode) {
        return false;
    }

}
