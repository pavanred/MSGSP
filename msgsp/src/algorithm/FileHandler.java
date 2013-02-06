package algorithm;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class FileHandler {

	protected ArrayList<ArrayList<ItemSet>> getInputData(String inputFilePath){
		
		BufferedReader reader;
		ArrayList<ArrayList<ItemSet>> data = new ArrayList<ArrayList<ItemSet>>();
		
		System.out.println("Reading input data...");
				
		try {
			
			reader = new BufferedReader(new FileReader(inputFilePath));
			
			String currentLine;
			ArrayList<ItemSet> itemSetList  = new ArrayList<ItemSet>();;
			ItemSet itemset;
			ArrayList<Integer> items;
			
			while ((currentLine = reader.readLine()) != null) {
	            
				itemSetList = new ArrayList<ItemSet>();
				
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
					itemSetList.add(itemset);
				}
				
				data.add(itemSetList);	
			 }
			
			reader.close();
			
			
			
		} catch (FileNotFoundException e) {

			//TODO : log error
			System.out.println("input data file not found");
			
		} catch (Exception e){
			
			//TODO : log error
			System.out.println("Error - Reading input data file");
		}		
		
		return data;
	}
	
	protected void getParameterData(){

		//TODO: get parameter data
		System.out.println("Reading parameter data...");
	}
	
	protected void writeOutputFile(){

		//TODO: Write to output file
		System.out.println("writing output to file");
	}
	
	protected void log(String message){
		//TODO: Logging
	}
	
	protected void printData(ArrayList<ArrayList<ItemSet>> data){
		
		for(ArrayList<ItemSet> itemSetList : data){
			
			System.out.print("<");
			
			for(ItemSet itemset : itemSetList){
				
				System.out.print("{");
				
				for(Integer item : itemset.getItems()){
					
					System.out.print(item + ",");
				}
				
				System.out.print("}");				
			}
			
			System.out.print(">\n");

		}
	}
}
