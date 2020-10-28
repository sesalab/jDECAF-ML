package it.unisa.jDECAF_ML.metrics.parser;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BlockComment;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.LineComment;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.Collection;

class CommentVisitor extends ASTVisitor {

    private final Collection<Comment> commentNodes;

    public CommentVisitor(Collection<Comment> commentNodes) {
        this.commentNodes = commentNodes;
    }

    @Override
    public boolean visit(BlockComment comment) {
        commentNodes.add(comment);
        return true;
    }
    
     @Override
    public boolean visit(Javadoc comment) {
        commentNodes.add(comment);
        return true;
    }
    
     @Override
    public boolean visit(LineComment comment) {
        commentNodes.add(comment);
        return true;
    }

}
