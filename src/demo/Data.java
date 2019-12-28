package demo;

import java.util.Random;

import org.uncommons.maths.random.ContinuousUniformGenerator;

import fq.utils.DataGenerator;

public class Data {

	public static void main(String[] args) {
		//number of objects
		int nObject = 10000;
		
		//number of dimensions. 
		int nDim = 10;
		
		DataGenerator g = new DataGenerator(nObject, nDim, null);

		//value ranges of attributes.
		double[][] ranges = {
				{0f,10f},
				{0f,10f},
				{0f,10f},
				{0f,10f},
				{0f,10f},
				{0f,10f},
				{0f,10f},
				{0f,10f},
				{0f,10f},
				{0f,10f}
		};
		
		int seed = 2019;
		
		for(int i = 0;i<nDim; i++) {
			//uniform distribution.
			g.generators.add(new ContinuousUniformGenerator(ranges[i][0],ranges[i][1],new Random(seed)));
		}
		
		g.generate();
		
		String path = "data.txt";
		//save the objects.
		g.save(path);
		
		//load the objects.
		double[][] data = DataGenerator.load(path);
	
		System.out.println("------");
	}

}
