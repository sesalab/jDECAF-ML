package it.unisa.jDECAF_ML.parser;

import it.unisa.jDECAF_ML.parser.bean.MethodBlockBean;
import org.eclipse.jdt.core.dom.Block;

public class BlockParser {

    public static MethodBlockBean parse(Block block) {
        MethodBlockBean blockBean = new MethodBlockBean();
        blockBean.setTextContent(block.toString());
        return blockBean;
    }

}
