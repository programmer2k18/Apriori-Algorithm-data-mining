package AssociationPackage;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;



public class FrequentAssociationRules {
	
	public Apriori object;
	public HashMap <String,String> colorAttributes;
	
	public Vector<itemSet> finalItemDataSets;
    public Vector<itemSet> validDataSetsOverIterations ;
    
    public Vector<Rule> allPossibleAssociationRules;
    public Vector<Rule> finalFrequentAssociationRules;
    
    
	public FrequentAssociationRules(double supcount, double minconf, double readingPerc) throws FileNotFoundException {
		object = new Apriori(supcount,minconf,readingPerc);
		// get necessary itemdatasets
		this.finalItemDataSets = this.object.generateFrequentDataItemSets();
		this.validDataSetsOverIterations = this.object.getValidItemDataSetsOverIteration();
		
		this.finalFrequentAssociationRules = new Vector<Rule>();
		this.allPossibleAssociationRules = new Vector<Rule>();
	}
	
	public List<String> getArrayNumbers( List<String> list){
		
		 List<String> arrayNumbers =new ArrayList<String>(); 
		 for(int i=0;i<list.size();i++) {
			 if(i==0) {
				for(int j=i+1;j<list.size();j++) 	
					arrayNumbers.add(list.get(i)+" "+list.get(j));
			 }
			 arrayNumbers.add(list.get(i));
		 }
		return arrayNumbers;
	}
	
	public List<String> getRemainNumbers(List<String> Fulllist,List<String> currentList){
		
		List<String> remaining = new ArrayList<String>();
		for(int i=0;i<Fulllist.size();i++) {
			if(!currentList.contains(Fulllist.get(i))) {
				remaining.add(Fulllist.get(i));
			}
		}
		return remaining;
	}
	
	public void generateAllPossibleRules() {
		
		for(int i=0;i<this.finalItemDataSets.size();i++) {
			List<String> resault = this.getArrayNumbers(this.finalItemDataSets.get(i).itemset);
			for(int j=0;j<resault.size();j++) {
				
				List<String> temp = this.getRemainNumbers(this.finalItemDataSets.get(i).itemset,Arrays.asList(resault.get(j).split(" ")));
				
				Rule obj1 = new Rule();
				obj1.left = temp;
				obj1.right = Arrays.asList(resault.get(j).split(" "));
				obj1.supportCount = this.finalItemDataSets.get(i).count;
				
				Rule obj2 = new Rule();
				obj2.left = obj1.right;
				obj2.right = temp;
				obj2.supportCount = this.finalItemDataSets.get(i).count;
				
				this.allPossibleAssociationRules.add(obj1);
				this.allPossibleAssociationRules.add(obj2);
			}
			
			
		}
	}
	
	public int getSupportCount(List<String> list) {
		
		for(int i=0;i<this.validDataSetsOverIterations.size();i++) {
			if(this.validDataSetsOverIterations.get(i).itemset.size()==list.size()
					&&this.validDataSetsOverIterations.get(i).itemset.containsAll(list)) {
				return this.validDataSetsOverIterations.get(i).count; 
			}
		}
		return 0;
	}
	
	public double calcConfidence(double supportcount, double supportCountOfLeft) {
		
		return (supportcount/supportCountOfLeft)*100;
	}
	
    public double calcSupportCountForRule(double supportcount) {
		
		return (supportcount/this.object.readingCount);
	}
	
    
    public Vector<Rule>  FrequentRules(){
    	
    	for(int i=0;i<this.allPossibleAssociationRules.size();i++) {
    		double supportCountForLeftSide = this.getSupportCount(this.allPossibleAssociationRules.get(i).left);
    		double confidence = this.calcConfidence(this.allPossibleAssociationRules.get(i).supportCount,supportCountForLeftSide);
    		double supportCount = this.calcSupportCountForRule(this.allPossibleAssociationRules.get(i).supportCount);
    		
    		if((confidence)>=this.object.minConfidence && supportCount >= (this.object.minSupportCount/this.object.readingCount)) {
    			
    			Rule rule = new Rule();
    			rule.left = this.allPossibleAssociationRules.get(i).left;
    			rule.right = this.allPossibleAssociationRules.get(i).right;
    			rule.supportCount = this.allPossibleAssociationRules.get(i).supportCount;
    			rule.confidence= confidence;
    			this.finalFrequentAssociationRules.add(rule);
    			
    	//System.out.println(rule.left+ ">"+rule.right+ " : "+ this.allPossibleAssociationRules.get(i).supportCount + " "+ supportCountForLeftSide +" "+confidence);
    		}/*else {
    			System.out.println("not freq");
    		}*/
    	}
    	return this.finalFrequentAssociationRules;
    }
    
    public void fillColorsAttributes() {
    	
    	this.colorAttributes = new HashMap<String,String>();
    				colorAttributes.put("0", "None ");	
    	colorAttributes.put("1", " Low R ");colorAttributes.put("2", " Mid R ");
    	colorAttributes.put("3", " High R ");colorAttributes.put("4", " Low G ");
    	colorAttributes.put("5", " Mid G ");colorAttributes.put("6", " High G ");
    	colorAttributes.put("7", " Low B ");colorAttributes.put("8", " Mid B ");
    	colorAttributes.put("9", " High B ");colorAttributes.put("10", " Family ");
    				colorAttributes.put("11", " Sports ");
    }
    
    public void showResault(Vector<Rule> rules) {
    	
    	this.fillColorsAttributes();
    	
    	String rule = "";
    	for(int i=0;i<rules.size();i++) {
			
    		//left part of the rule
    		for(int j=0;j<rules.get(i).left.size();j++) {
				rule+=this.colorAttributes.get(rules.get(i).left.get(j).toString());
			}
    		rule+=" >> ";
    		//right part of the rule
    		for(int k=0;k<rules.get(i).right.size();k++) {
    			rule+=this.colorAttributes.get(rules.get(i).right.get(k).toString());
			}
    		
    		System.out.println(rule);
    		rule="";
    	}
    }
    
	public void printRules(Vector<Rule> rule) {	
		for(int i=0;i<rule.size();i++) {
			System.out.println(rule.get(i).left+ " > " + rule.get(i).right);
		}
	}
	
	public void printItemSets(Vector<itemSet> itemSet) {	
		for(int i=0;i<itemSet.size();i++) {
			System.out.println(itemSet.get(i).itemset+ " > " + itemSet.get(i).count);
		}
	}
}
