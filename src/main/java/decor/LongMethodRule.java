package decor;

import it.unisa.jDECAF_ML.metrics.parser.bean.ComponentBean;
import it.unisa.jDECAF_ML.metrics.parser.bean.MethodBean;



public class LongMethodRule implements DetectionRule{

	public boolean isLongMethod(MethodBean pMethod) {
		String[] tokenizedTextualContent = pMethod.getTextContent().split("\n");
		
		if( (tokenizedTextualContent.length > 100) && (pMethod.getParameters().size() >= 2) )
			return true;
		
		return false;
	}

    @Override
    public boolean isSmelly(ComponentBean cb) {
        return isLongMethod((MethodBean) cb);
    }
}