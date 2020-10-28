/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decor;

import it.unisa.jDECAF_ML.metrics.parser.bean.ComponentBean;

/**
 *
 * @author fably
 */
public interface DetectionRule {
    boolean isSmelly(ComponentBean cb);
}
