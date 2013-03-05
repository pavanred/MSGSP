package algorithm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileHandler {

	private Integer head = 0;
	private BufferedReader nextReader;
	
	/*
	 * Reads from parameter file and returns a list of items
	 * @param  parameterFilePath - parameter file path 
	 * @return list of Items
	 */
	protected ArrayList<Item> getMISValues(String parameterFilePath){

		BufferedReader reader;	
		ArrayList<Item> items = new ArrayList<Item>();
		
		System.out.println("Reading item data...");
		
		try {
			
			reader = new BufferedReader(new FileReader(parameterFilePath));
			String currentLine;
			Item tempitem;
			
			while ((currentLine = reader.readLine()) != null) {
				
				if(currentLine.contains("MIS")){
					
					tempitem = new Item();
					
					String tempLine = currentLine;
					
					Integer equalPos = tempLine.indexOf('=');
					Integer startBracketPos = tempLine.indexOf('(');
					Integer endBracketPos = tempLine.indexOf(')');
					
					tempitem.setItemNo(Integer.parseInt(tempLine.substring(startBracketPos + 1, endBracketPos)));
					
					tempLine = currentLine;
					
					tempitem.setMinItemSupport(Float.parseFloat(tempLine.substring(equalPos + 1).trim()));

					items.add(tempitem);					
				} 				
			}
			
			reader.close();
			
		} catch (FileNotFoundException e) {

			System.out.println("[Error] Parameter file not found");
			
		} catch (Exception e){

			System.out.println("[Error] Reading parameter file values");
		}	
		
		return items;
	}
	
	/*
	 * Reads from support difference constraint from the parameter file
	 * @param  parameterFilePath - parameter file path 
	 * @return support difference constraint
	 */
	protected float getSDC(String parameterFilePath){
		
		float sdc = 0;		
		BufferedReader reader;	
		
		System.out.println("Reading SDC...");
		
		try {
			
			reader = new BufferedReader(new FileReader(parameterFilePath));
			String currentLine;

			while ((currentLine = reader.readLine()) != null) {
				
				if(currentLine.contains("SDC")){

					Integer equalPos = currentLine.indexOf('=');
					sdc = Float.parseFloat(currentLine.substring(equalPos + 1).trim());			
				}			
			}
			
			reader.close();
			
		} catch (FileNotFoundException e) {

			System.out.println("[Error] Parameter file not found");
			
		} catch (Exception e){
			
			System.out.println("[Error] Reading SDC value");
		}	
		
		return sdc;
	}
	
	/*
	 * Write list of sequences to output file
	 * @param  frequentSequences - list of sequences
	 * @param outputFilePath - path of the output file
	 */
	protected void writeOutput(ArrayList<Sequence> frequentSequences, String outputFilePath){
				
		ArrayList<Sequence> tmpseq = new ArrayList<Sequence>();
		
		try {
			
			File file = new File(outputFilePath);
			
			if(file.exists()){
				file.delete();
			}
			
			BufferedWriter output = new BufferedWriter(new FileWriter(file));		
			
			//output.write(new Timestamp(new java.util.Date().getTime()).toString());
			//output.write("\n");
			
			for(int i=1; (tmpseq = Utilities.getOfSize(frequentSequences, i)).size() > 0 ; i++){
				
				System.out.println("");
				System.out.println("The number of length " + i + " sequential patterns is " + tmpseq.size());
				
				
				output.write("The number of length " + i + " sequential patterns is " + tmpseq.size());
				output.write("\n");
				
				for(Sequence seq : tmpseq){
					
					System.out.print("Pattern : <");
					output.write("Pattern : <");
					
					for(ItemSet itemset : seq.getItemsets()){
						
						System.out.print("{");
						output.write("{");
						
						for(int j=0; j < itemset.getItems().size(); j++){
							
							if(j == itemset.getItems().size() - 1){
								System.out.print(itemset.getItems().get(j));
								output.write(itemset.getItems().get(j).toString());
							}
							else{
								System.out.print(itemset.getItems().get(j) + ",");
								output.write(itemset.getItems().get(j) + ",");
							}						
						}
						
						System.out.print("}");
						output.write("}");
					}	
					
					System.out.print("> Count : " + seq.getCount() + " \n");
					output.write("> Count : " + seq.getCount() + " \n");
				}	
			}
			
			output.close();
			System.out.println("");
			System.out.println("Process Complete!");
		
		} catch (IOException e) {
			
			System.out.println("[Error] Output file write operation");
		}
	}

	/*
	 * Read the next sequence from the data file
	 * @param resetHead - true if the data file should be parsed from the start	 * 
	 * @param dataFilePath - path of the data file
	 */
	protected Sequence getNextSequence(boolean resetHead, String dataFilePath){
		
		String currentLine;
		Sequence sequence = new Sequence();
		ItemSet itemset;
		ArrayList<Integer> items;
		
		try {	
		
			if(resetHead){
				head = 0;
				nextReader = new BufferedReader(new FileReader(dataFilePath));
			}
		
			if ((currentLine = nextReader.readLine()) != null) {
				
				sequence = new Sequence();
				
				currentLine = currentLine.replace("<", "");
				currentLine = currentLine.replace(">", "");
				currentLine = currentLine.replace("{", "");
				
				String[] splitItemSets = currentLine.split("}");
								
				for(String tempItemSet : splitItemSets){
					
					itemset = new ItemSet();
					items = new ArrayList<Integer>();
					
					String[] splitItems = tempItemSet.split(",");
					
					for(String tempItem : splitItems){
						
						items.add(Integer.parseInt(tempItem.trim()));						
					}
					
					itemset.setItems(items);
					sequence.addItemSet(itemset);
				}
			 }
			else{
				nextReader.close();
				sequence = null;
			}
			
			head = head + 1;
				
		} catch (FileNotFoundException e) {

			System.out.println("[Error] Data file not found " + e);
			
		} catch (Exception e){

			System.out.println("[Error] Reading data " + e);
		}
		
		return sequence;
	}

}