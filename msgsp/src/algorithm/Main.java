package algorithm;

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
		
		ArrayList<Sequence> data;
		ArrayList<MISValue> misValues;
		Double sdc;
						
		fileHandler = new FileHandler();
		
		//Input data 
		data = fileHandler.getInputData(dataFilePath);
		misValues = fileHandler.getMISValues(parameterFilePath);
		sdc = fileHandler.getSDC(parameterFilePath);
		
		//DEBUG - verify input data
		//fileHandler.printData(data);
		//fileHandler.printMISValues(misValues);
		//System.out.println("SDC = " + sdc);
		
		objMsgsp = new Msgsp();
		
		//Compute actual item support
		misValues = objMsgsp.computeItemSupport(misValues, data);
		
		//Sort min support values
		misValues = objMsgsp.sortMinSupportValue(misValues);
		
		//Init Pass - compute L
		ArrayList<ItemSet> lSet = objMsgsp.initPass(misValues, data);
		
		//Compute Frequent Set 1 - F1
		ArrayList<Sequence> freqSet1 = objMsgsp.computeFrequentSet_1(misValues);
			
		
		
		//Write the output to file
		fileHandler.writeOutputFile();		
	}

}
