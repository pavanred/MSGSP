package algorithm;

import java.util.ArrayList;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		FileHandler fileHandler;
		Msgsp objMsgsp;
		
		String dataFilePath = "";
		String parameterFilePath = "";
		
		ArrayList<Transaction> itemset;
						
		fileHandler = new FileHandler();
		
		//Read the input data file
		itemset = fileHandler.getInputData(dataFilePath);
		
		objMsgsp = new Msgsp();
		
		
		//Write the output to file
		fileHandler.writeOutputFile();		
	}

}
