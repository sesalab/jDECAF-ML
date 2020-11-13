/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.jDECAF_ML.parser.bean;

import it.unisa.jDECAF_ML.parser.bean.text.ApacheCosineSimilarityStrategy;
import it.unisa.jDECAF_ML.parser.bean.text.IRNormalizer;
import org.apache.commons.text.similarity.CosineDistance;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 * @author fabiano
 */
public abstract class ComponentBean {

    private static final Pattern NEWLINE = Pattern.compile("\n");

    protected String textContent;
    protected String name;
    protected List<CommentBean> comments;
    private final TextualSimilarityStrategy similarityStrategy;
    private final TextNormalizationStrategy normalizationStrategy;


    public ComponentBean(String name) {
        this.name = name;
        comments = new LinkedList<>();
        //TODO: Refactor to make them pure strategies
        similarityStrategy = new ApacheCosineSimilarityStrategy(new CosineDistance());
        normalizationStrategy = new IRNormalizer();
    }

    public String getName() {
        return name;
    }

    public void setName(String pName) {
        name = pName;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public int getLOC() {
        return NEWLINE.split(textContent).length;
    }

    public int getCLOC() {
        int CLOC = 0;
        for(CommentBean commentBean: comments){
            CLOC += NEWLINE.split(commentBean.getCommentText()).length;
        }
        return CLOC;
    }

    public void addComment(CommentBean commentBean){
        comments.add(commentBean);
    }
    public abstract String getQualifiedName();

    public Double textualSimilarityWith(ComponentBean otherBean){
        return similarityStrategy.textualSimilarity(normalizedTextContent(),otherBean.normalizedTextContent());
    }

    public String normalizedTextContent(){
        return normalizationStrategy.normalizeText(textContent);
    }
}
