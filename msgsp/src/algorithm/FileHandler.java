package algorithm;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class FileHandler {

	private Integer head = 0;
	private BufferedReader nextReader;
	
	@Deprecated
	protected ArrayList<Sequence> getInputData(String inputFilePath){
		
		BufferedReader reader;
		ArrayList<Sequence> data = new ArrayList<Sequence>();
		
		System.out.println("Reading input data...");
				
		try {
			
			reader = new BufferedReader(new FileReader(inputFilePath));
			
			String currentLine;
			Sequence sequence  = new Sequence();
			ItemSet itemset;
			ArrayList<Integer> items;
			
			while ((currentLine = reader.readLine()) != null) {
	            
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
				
				data.add(sequence);	
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
	
	protected ArrayList<MISValue> getMISValues(String parameterFilePath){

		BufferedReader reader;	
		ArrayList<MISValue> misValues = new ArrayList<MISValue>();
		
		System.out.println("Reading MIS values...");
		
		try {
			
			reader = new BufferedReader(new FileReader(parameterFilePath));
			String currentLine;
			MISValue tempMISValue;
			
			while ((currentLine = reader.readLine()) != null) {
				
				if(currentLine.contains("MIS")){
					
					tempMISValue = new MISValue();
					
					String tempLine = currentLine;
					
					Integer equalPos = tempLine.indexOf('=');
					Integer startBracketPos = tempLine.indexOf('(');
					Integer endBracketPos = tempLine.indexOf(')');
					
					tempMISValue.setitemNo(Integer.parseInt(tempLine.substring(startBracketPos + 1, endBracketPos)));
					
					tempLine = currentLine;
					
					tempMISValue.setMinItemSupport(Float.parseFloat(tempLine.substring(equalPos + 1).trim()));

					misValues.add(tempMISValue);					
				} 				
			}
			
			reader.close();
			
		} catch (FileNotFoundException e) {
	
			//TODO : log error
			System.out.println("input data file not found");
			
		} catch (Exception e){
			
			//TODO : log error
			System.out.println("Error - Reading MIS values");
		}	
		
		return misValues;
	}
	
	protected Double getSDC(String parameterFilePath){
		
		double sdc = 0;		
		BufferedReader reader;	
		
		System.out.println("Reading SDC...");
		
		try {
			
			reader = new BufferedReader(new FileReader(parameterFilePath));
			String currentLine;

			while ((currentLine = reader.readLine()) != null) {
				
				if(currentLine.contains("SDC")){

					Integer equalPos = currentLine.indexOf('=');
					sdc = Double.parseDouble(currentLine.substring(equalPos + 1).trim());			
				}			
			}
			
			reader.close();
			
		} catch (FileNotFoundException e) {
	
			//TODO : log error
			System.out.println("input data file not found");
			
		} catch (Exception e){
			
			//TODO : log error
			System.out.println("Error - Reading SDC value");
		}	
		
		return sdc;
	}
	
	protected Sequence getNextSequence(boolean resetHead, String inputFilePath){
			
		String currentLine;
		Sequence sequence = new Sequence();
		ItemSet itemset;
		ArrayList<Integer> items;
		
		try {	
		
			if(resetHead){
				head = 0;
				nextReader = new BufferedReader(new FileReader(inputFilePath));
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
			
			//TODO : log error
			System.out.println("input data file not found");
			
		} catch (Exception e){
			
			//TODO : log error
			System.out.println("Error - Reading SDC value");
		}
		
		return sequence;
	}
	
	protected void writeOutputFile(){

		//TODO: Write to output file
		System.out.println("writing output to file");
	}
	
	protected void log(String message){
		//TODO: Logging
	}
	
	protected void printData(ArrayList<Sequence> data){
		
		for(Sequence seq : data){
			
			System.out.print("<");
			
			for(ItemSet itemset : seq.getItemsets()){
				
				System.out.print("{");
				
				for(Integer item : itemset.getItems()){
					
					System.out.print(item + ",");
				}
				
				System.out.print("}");			
			}
			
			System.out.print(">\n");
		}
	}
	
	protected void printSequence(Sequence seq){
		
		System.out.print("<");
			
		for(ItemSet itemset : seq.getItemsets()){
				
			System.out.print("{");
				
			for(Integer item : itemset.getItems()){
					
				System.out.print(item + ",");
			}
				
			System.out.print("}");			
		}
		
		System.out.print(">\n");
	}
	
	protected void printMISValues(ArrayList<MISValue> misValues){
		
		Integer count = 1;
		
		for(MISValue misValue : misValues){			
			
			System.out.println("MIS(" + misValue.getItemNo() + ") = " + misValue.getMinItemSupport() + " -- "
					+ misValue.getActualSupport() + "--" + misValue.getSupportCount());
			count = count+1;
		}
	}

	public void printFrequentSets(ArrayList<Sequence> frequentSets) {
		
		ArrayList<Sequence> tmpseq = new ArrayList<Sequence>();
		
		for(int i=1; (tmpseq = getOfSize(frequentSets, i)).size() > 0 ; i++){
			
			System.out.println("");
			System.out.println("The number of length " + i + " sequential patterns is " + tmpseq.size());
			
			for(Sequence seq : tmpseq){
				
				System.out.print("Pattern : <");
				
				for(ItemSet itemset : seq.getItemsets()){
					
					System.out.print("{");
					
					for(int j=0; j < itemset.getItems().size(); j++){
						
						if(j == itemset.getItems().size() - 1){
							System.out.print(itemset.getItems().get(j));
						}
						else{
							System.out.print(itemset.getItems().get(j) + ",");
						}						
					}
					
					System.out.print("}");
				}	
				
				System.out.print("> Count : " + seq.getCount() + " \n");
			}
			
			
		}
	}
	
	public ArrayList<Sequence> getOfSize(ArrayList<Sequence> frequentSets, int size) {
		
		ArrayList<Sequence> setOfSize = new ArrayList<Sequence>();
		
		for(Sequence seq : frequentSets){
			//if(seq.getItemsets().size() == size){
			if(seq.getAllItems().size() == size){
				setOfSize.add(seq);
			}
		}
		
		return setOfSize;
	}
}
