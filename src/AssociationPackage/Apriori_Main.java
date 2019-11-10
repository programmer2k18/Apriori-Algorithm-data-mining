package AssociationPackage;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

public class Apriori_Main {

	public static void main(String[] args) throws FileNotFoundException {
		
		System.out.println("Please Enter Minimum support count in percentage:");
		double minSuppCount = new Scanner(System.in).nextDouble();
		System.out.println("Please Enter Minimum confidence in percentage:");
		double minCofidence = new Scanner(System.in).nextDouble();
		System.out.println("How much you wanna read from the file? In percentage: ");
		double ReadingPercentage = new Scanner(System.in).nextDouble();
		if(ReadingPercentage > 100)
			return;
		
		FrequentAssociationRules fr = new FrequentAssociationRules(minSuppCount,minCofidence,ReadingPercentage);
		fr.generateAllPossibleRules();
		
		System.out.println("Frequent item Data sets!");
		fr.printItemSets(fr.finalItemDataSets);
		//fr.printItemSets(fr.validDataSetsOverIterations);
		
		fr.printRules(fr.FrequentRules());
		
		System.out.println("\n\nFrequent Association reules!");
		fr.showResault(fr.finalFrequentAssociationRules);
	}

}
