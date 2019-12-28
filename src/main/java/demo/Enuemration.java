package demo;

import fq.itree.ITree;
import fq.utils.DataGenerator;

public class Enuemration {

	public static String path = "data.txt";
	
	public static void main(String[] args) {
//		double[][] data = DataGenerator.load(path);
//		
//		ITree tree = new ITree(data);
//		
		double[] w = {
				0,0,0,0
		}; 
		enumerateVertex(0,w,null);
	}

	static double[][] weightRanges = {
			{0,1},
			{0,1},
			{0,1},
			{0,1},
	};
	
	private static boolean enumerateVertex(int i, double[] w, double[] coeficient){
		if(i==w.length) {
			for(int k=0;k<w.length;k++) {
				System.out.print(w[k]+",");
			}
			System.out.println("");
			return false;
		}else{
			if(enumerateVertex(i+1,w,coeficient))
				return true;
			w[i] = weightRanges[i][1];
			if(enumerateVertex(i+1,w,coeficient))
				return true;	
			w[i] = weightRanges[i][0];
			return false;
		}
	}
}
