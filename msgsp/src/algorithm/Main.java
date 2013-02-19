package algorithm;

import java.util.ArrayList;

public class Main {

	public static void main(String[] args) {
		
		//Initialize variable
		FileHandler fileHandler;
		Msgsp objMsgsp;
		ArrayList<Item> items;
		Float sdc;
		ArrayList<Sequence> frequentSequences;
		
		String dataFilePath = System.getProperty("user.dir") + "/data/data.txt";
		String parameterFilePath = System.getProperty("user.dir") + "/data/para.txt";
		String outputFilePath = System.getProperty("user.dir") + "/output/output.txt";

		//Instances
		fileHandler = new FileHandler();
		items = new ArrayList<Item>();
		
		//Read parameter data and SDC
		items = fileHandler.getMISValues(parameterFilePath);
		sdc = fileHandler.getSDC(parameterFilePath);
		
		//Algorithm
		objMsgsp = new Msgsp(items, dataFilePath, sdc);				
		
		frequentSequences = objMsgsp.main();
		
		//Write output
		fileHandler.writeOutput(frequentSequences, outputFilePath);	
	}
}
