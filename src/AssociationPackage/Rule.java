package AssociationPackage;

import java.util.ArrayList;
import java.util.List;

public class Rule {
	
	public double supportCount;
	public double confidence;
	public List<String> left;
	public List<String> right;
	
	public Rule(){
		this.left = new ArrayList<String>();
		this.right = new ArrayList<String>();
		this.supportCount=0;
		this.confidence=0;
	}

}
