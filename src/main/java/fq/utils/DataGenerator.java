package fq.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.uncommons.maths.number.NumberGenerator;

public class DataGenerator {
	public int nObjects;
	public int nDims;

	public double[][] data;

	public List<NumberGenerator<Double>> generators = new ArrayList<NumberGenerator<Double>>();

	public DataGenerator(int nobject, int ndim, double[][] dataRange) {
		this.nObjects = nobject;
		this.nDims = ndim;
		this.data = new double[nobject][ndim];
	}

	public void generate() {
		for (int i = 0; i < this.nObjects; i++) {
			int _j = 0;
			for (int k = 0; k < generators.size(); k++) {
				NumberGenerator<Double> g = generators.get(k);
				Double d = g.nextValue();
				this.data[i][_j] = d.doubleValue();
				_j += 1;
			}
		}
	}

	public void save(String path) {
		try {
			BufferedWriter in = new BufferedWriter(new FileWriter(path));
			in.append(this.nObjects+","+this.nDims+"\n");
			
			for(int i = 0;i<this.nObjects;i++) {
				for(int j = 0; j<this.nDims;j++) {
					in.append(this.data[i][j]+",");
				}
				in.append("\n");
			}
			
			in.close();
		} catch (IOException ioException) {
		}
	}

	public static double[][] load(String path) {
		double[][] data = null;
		try {
			BufferedReader in = new BufferedReader(new FileReader(path));
			String line;
			
			String[] strs = in.readLine().split(",");
			int n = Integer.parseInt(strs[0]);
			int d = Integer.parseInt(strs[1]);
			data = new double[n][d];
			
			int i = 0;
			while ((line = in.readLine()) != null)
			{
				String[] values = line.split(",");
				int j = 0;
				for (int k = 0; k<values.length;k++) {
					double str_double = Double.parseDouble(values[k]);
					data[i][j] = str_double;
					j = j + 1;
				}
				i = i + 1;
			}
			in.close();
		} catch (IOException ioException) {
		}
		
		return data;
	}
}
