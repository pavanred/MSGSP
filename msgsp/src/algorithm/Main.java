package algorithm;

import java.net.URL;
import java.util.ArrayList;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		FileHandler fileHandler;
		Msgsp objMsgsp;
		
		String dataFilePath = System.getProperty("user.dir") + "/data/data.txt";
		String parameterFilePath = System.getProperty("user.dir") + "/data/para.txt";
		
		ArrayList<ArrayList<ItemSet>> data;
						
		fileHandler = new FileHandler();
		
		//Read the input data file	
		data = fileHandler.getInputData(dataFilePath);
		
		//DEBUG - verify input data
		fileHandler.printData(data);
		
		objMsgsp = new Msgsp();
		
		
		//Write the output to file
		fileHandler.writeOutputFile();		
	}

}
