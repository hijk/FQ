import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class DataGenerator {
	public int nObjects;
	public int nDims;

	public double[][] data;
	public double[] constants;
	public boolean withConstant;

	public static Random random = new Random(2020);

	public DataGenerator(int nobject, int ndim, boolean withConstant) {
		this.nObjects = nobject;
		this.nDims = ndim;
		this.data = new double[nobject][ndim];
		this.constants = new double[nobject];
		this.withConstant = withConstant;
	}

	public void generate(double down, double up) {
		double range = up - down;
		for (int i = 0; i < this.nObjects; i++) {
			for (int k = 0; k < this.nDims; k++) {
				this.data[i][k] = random.nextDouble() * range + down;
			}
			if (this.withConstant)
				this.constants[i] = random.nextDouble() * range + down;
		}
	}

	public void save(String path) {
		try {
			BufferedWriter in = new BufferedWriter(new FileWriter(path));
			in.append(this.nObjects + "," + this.nDims + "\n");

			for (int i = 0; i < this.nObjects; i++) {
				for (int j = 0; j < this.nDims; j++) {
					in.append(this.data[i][j] + ",");
				}
				in.append(this.constants[i]+"\n");
			}
			in.close();
		} catch (IOException ioException) {
		}
	}

	public static void main(String[] args) throws Exception {
		int nobject = Integer.parseInt(args[0]);
		int ndim = Integer.parseInt(args[1]);

		double down = Double.parseDouble(args[2]);
		double up = Double.parseDouble(args[3]);

		boolean withConstant = Boolean.parseBoolean(args[4]);
		String output = args.length > 5 ? args[5] : "data.txt";

		DataGenerator generator = new DataGenerator(nobject, ndim, withConstant);
		generator.generate(down, up);
		generator.save(output);
	}
}