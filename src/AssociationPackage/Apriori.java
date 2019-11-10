package AssociationPackage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;



public class Apriori {
	
	// attributes
	public double minSupportCount=0;
	public double minConfidence=0;
	public double readingCount=0;
	public double fileSize = 245057;
	public boolean isValidToNextIteration=false;
	
	//files
	public File ReadFromFile;
    public Scanner Input;
	
	//Lists for one item dataset
    private List<String> items = new ArrayList<String>();
    private List<Integer> counter = new ArrayList<Integer>();
    
	//vectors for valid and removed Data sets over iterations
    private Vector<itemSet> dataSets = new Vector<itemSet>();
    private Vector<itemSet> validDataSetsOverIterations = new Vector<itemSet>();
    
    //constructor
	public Apriori(double supcount, double minconf, double readingPerc) throws FileNotFoundException {
		
		// in integer numbers
		this.readingCount = (readingPerc*this.fileSize)/100;
		this.minSupportCount = (supcount*this.readingCount)/100;
		//in percentage
		this.minConfidence =minconf;
		System.out.println("MINSUPPORT:" + this.minSupportCount);
		System.out.println("MINConfidence:" + this.minConfidence);
		
		System.out.println("reading:" + this.readingCount);
		//Opening files to read transactions
		ReadFromFile=new File("CarSales.txt");
        Input =new Scanner(ReadFromFile);
	}
	
	public void  oneItemset() {
		
		String[] record;
		for(int i = 0;i<this.readingCount;i++) {
			record = this.Input.nextLine().split(" ");
			for(int j=0;j<record.length;j++) {
				if(this.items.contains(record[j])) {
			    	int index = this.items.indexOf(record[j]);
			    	this.counter.set(index, this.counter.get(index)+1);
			    }
			    else {
			    	this.items.add(record[j]);
			    	this.counter.add(1);
			    }
			}   
		}
		this.Input.close();
	}
	
	
	public void combineDataSets() {
		
		for(int i=0;i<this.items.size();i++) {
			if(this.counter.get(i) >= this.minSupportCount) {
				itemSet object = new itemSet();
				object.count = this.counter.get(i);
				object.itemset.add(this.items.get(i));
				this.dataSets.add(object);
				this.validDataSetsOverIterations.add(object);
			}
		}
	}
	
	public boolean isExist(Vector<itemSet> data, List<String> unionitemSet) {
		for(int i=0; i<data.size();i++) {
			if(data.get(i).itemset.toString().equals(unionitemSet.toString()))
				return true;
		}
		return false;
		
	}
	
	public Vector<itemSet> getItemSets(Vector<itemSet> dataset) throws FileNotFoundException{
		
		 List<String> unionitemSet;
	     Vector<itemSet> nextIterationDataItemSet = new Vector<itemSet>();
		for(int i=0; i<dataset.size();i++) {
			for(int j=i+1; j<dataset.size();j++) {
				
				unionitemSet = this.getUnion(dataset.get(i).itemset, dataset.get(j).itemset);
				if(this.isExist(nextIterationDataItemSet, unionitemSet)) {
					continue;
				}
				
				int subSetCount = this.getSubsetCount(unionitemSet);
				if(subSetCount >= this.minSupportCount ) {
					
					itemSet object = new itemSet();
					object.itemset = unionitemSet;
					object.count = subSetCount;
					this.validDataSetsOverIterations.add(object);
					nextIterationDataItemSet.add(object);
					this.isValidToNextIteration = true;
					//System.out.println(unionitemSet + " " + subSetCount);
				}
			}
		}
		return nextIterationDataItemSet;
	}
	
	public Vector<itemSet> generateFrequentDataItemSets() throws FileNotFoundException{
		//prepare data
		this.oneItemset();
		this.combineDataSets();
		
		//generate dataItemSets
		Vector<itemSet> currentDataItemSet = new Vector<itemSet>();
		Vector<itemSet> nextIterationDataItemSet = this.getItemSets(this.dataSets);
			while(nextIterationDataItemSet.size()>1 && this.isValidToNextIteration) {
				this.isValidToNextIteration = false;
				nextIterationDataItemSet =this.getItemSets(nextIterationDataItemSet);
				if(nextIterationDataItemSet.size()>0)
					currentDataItemSet=nextIterationDataItemSet;
			}
		return currentDataItemSet;
	}
	
	public List<String> getUnion(List<String> list1, List<String> list2){
        
		Set<String> set = new HashSet<String>(list1);
        set.addAll(list2);
        
        String union[]= {};
        union = set.toArray(union);
		return Arrays.asList(union);
	}
	
	public int getSubsetCount(List<String> list) throws FileNotFoundException {
		
		ReadFromFile=new File("CarSales.txt");
        Input =new Scanner(ReadFromFile);
        List<String> record;
		int count = 0;
		
		for(int i = 0;i<this.readingCount;i++) {
			record = Arrays.asList(this.Input.nextLine().split(" "));
			if(this.isSubset(list, record)) {
				count++;
			}
		}
		this.Input.close();
		return count;
		
	}
	
	public boolean isSubset(List<String> list,List<String> recordFromFile) {
	
			if(recordFromFile.containsAll(list))
				return true;
			return false;
	}
	
	public Vector<itemSet> getValidItemDataSetsOverIteration(){
		return this.validDataSetsOverIterations;
	}
}
