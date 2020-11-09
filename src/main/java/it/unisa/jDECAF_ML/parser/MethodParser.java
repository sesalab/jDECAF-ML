package it.unisa.jDECAF_ML.parser;

import it.unisa.jDECAF_ML.parser.bean.ClassBean;
import it.unisa.jDECAF_ML.parser.bean.InstanceVariableBean;
import it.unisa.jDECAF_ML.parser.bean.MethodBean;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Comment;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.regex.Pattern;

class MethodParser {

    public static MethodBean parse(MethodDeclaration pMethodNode, Collection<InstanceVariableBean> pClassInstanceVariableBeans, ClassBean cb) {

        // Instantiate the bean
        MethodBean methodBean = new MethodBean();

        // Set the name
        methodBean.setName(pMethodNode.getName().toString());

        methodBean.setParameters(pMethodNode.parameters());

        methodBean.setReturnType(pMethodNode.getReturnType2());
        
        methodBean.setBelongingClass(cb);

        // Set the textual content
        methodBean.setTextContent(pMethodNode.toString());
        
        Pattern newLine = Pattern.compile("\n");
        String[] lines = newLine.split(pMethodNode.toString());
        
        methodBean.setLOC(lines.length);
        
        // Get the comment nodes
        Collection<Comment> comments = new ArrayList<>();
        pMethodNode.accept(new CommentVisitor(comments));
        int CLOC = 0;
        for (Comment comment : comments) {
            lines = newLine.split(comment.toString());
            CLOC += lines.length;
        }
        methodBean.setCLOC(CLOC);
//        Pattern comment = Pattern.compile("((^\\s*[*])|(^\\s*[/][*])|(^\\s*[/][/])).*");
//        String[] cLines = comment.split(pMethodNode.toString());
//        int CLOC = 0;
//        for (String s: lines){
//            if (comment.matcher(s).matches())
//                CLOC++;
//        }
//        
//        methodBean.setCLOC(CLOC);
        // Get the names in the method
        Collection<String> names = new HashSet<>();
        pMethodNode.accept(new NameVisitor(names));

        methodBean.setNames(names);
        // Verify the correspondence between names and instance variables 
        Collection<InstanceVariableBean> usedInstanceVariableBeans = getUsedInstanceVariable(names, pClassInstanceVariableBeans);

        // Set the used instance variables
        methodBean.setUsedInstanceVariables(usedInstanceVariableBeans);

        // Get the invocation names
        Collection<String> invocations = new HashSet<>();
        pMethodNode.accept(new InvocationVisitor(invocations));

        // Get the invocation beans from the invocation names
        Collection<MethodBean> invocationBeans = new ArrayList<>();
        for (String invocation : invocations) {
            invocationBeans.add(InvocationParser.parse(invocation));
        }

        // Set the invocations
        methodBean.setMethodCalls(invocationBeans);
        
        // Get the accessed field names
        Collection<String> selfAccessedFieldNames = new HashSet<>();
        Collection<String> foreignAccessedFieldNames = new HashSet<>();
        pMethodNode.accept(new FieldAccessVisitor(selfAccessedFieldNames, foreignAccessedFieldNames));
        
         // Get the foreign accessed fields
        Collection<InstanceVariableBean> selfAccessedFields = new ArrayList<>();
        for (String accessedFieldName : selfAccessedFieldNames) {
            selfAccessedFields.add(FieldAccessParser.parse(accessedFieldName));
        }
        Collection<InstanceVariableBean> foreignAccessedFields = new ArrayList<>();
        for (String accessedFieldName : foreignAccessedFieldNames) {
            foreignAccessedFields.add(FieldAccessParser.parse(accessedFieldName));
        }

        // Set the invocations
        methodBean.setSelfAccessedFields(selfAccessedFields);
        methodBean.setForeignAccessedFields(foreignAccessedFields);

        //GET BLOCKS
        BlockVisitor blockVisitor = new BlockVisitor();
        pMethodNode.accept(blockVisitor);
        Collection<Block> blocks = blockVisitor.getBlocks();
        for (Block block: blocks){
            methodBean.addMethodBlock(BlockParser.parse(block));
        }

        // Return the bean
        return methodBean;

    }

    private static Collection<InstanceVariableBean> getUsedInstanceVariable(Collection<String> pNames, Collection<InstanceVariableBean> pClassInstanceVariableBeans) {

        // Instantiate the collection to return
        Collection<InstanceVariableBean> usedInstanceVariableBeans = new ArrayList<>();

        // Iterate over the instance variables defined in the class
        for (InstanceVariableBean classInstanceVariableBean : pClassInstanceVariableBeans) // If there is a correspondence, add to the returned collection
        {
            if (pNames.remove(classInstanceVariableBean.getName())) {
                usedInstanceVariableBeans.add(classInstanceVariableBean);
            }
        }

        // Return the collection
        return usedInstanceVariableBeans;

    }

}
