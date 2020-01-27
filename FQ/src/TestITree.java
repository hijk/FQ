import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.swing.JFrame;

public class TestITree {
	private static TreeStatistics stat = new TreeStatistics();

	public static SubdomainNode none = new SubdomainNode(null);

	private static Logger logger = LogManager.getLogManager().getLogger(Logger.GLOBAL_LOGGER_NAME);

	public static void main(String[] args) throws Exception {
		URL url = TestITree2.class.getClassLoader().getResource("logging.properties");
		LogManager.getLogManager().readConfiguration(url.openStream());

		double[][] data = null;
		double[] constants = null;
		if (args.length <= 1) {
			String fileName = args.length == 1 ? args[0] : "data.txt";
			File file = new File(fileName);
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);

			String line;
			line = br.readLine();
			int nObjects, nDims;
			String[] fl = line.split(",");
			nObjects = Integer.parseInt(fl[0]);
			nDims = Integer.parseInt(fl[1]);

			data = new double[nObjects][nDims];
			constants = new double[nObjects];
			// System.out.println(nObjects+","+nDims);
			int i = 0;
			while (i < nObjects && (line = br.readLine()) != null) {
				String[] ss = line.split(",");
				for (int j = 0; j < ss.length - 1; j++) {
					data[i][j] = Double.parseDouble(ss[j]);
				}
				constants[i] = Double.parseDouble(ss[ss.length-1]);
				i++;
			}
			br.close();

		} else {
			DataGenerator generator = new DataGenerator(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Boolean.parseBoolean(args[4]));
			generator.generate(Double.parseDouble(args[2]), Double.parseDouble(args[3]));

			data = generator.data;
			constants = generator.constants;
		}

		double[][] weights = new double[data[0].length][2];
		for (int i = 0; i < weights.length; i++) {
			weights[i][0] = -100f;
			weights[i][1] = 100f;
		}

		ITree t = new ITree(data, constants, weights);
		t.buildTree();

		stat.walk(t);

		for (int i = 0; i < data.length; i++) {
			for (int j = i + 1; j < data.length; j++) {
				logger.log(Level.CONFIG, "Intersection(" + i + "," + j + ")"
						+ t.expression(t.intersection(i, j), t.constants[i] - t.constants[j]));
			}
		}

		logger.log(Level.CONFIG, "number of Subdomains: " + stat.nLeafNodes);
		 System.out.println("average depth(includes SubdomainNode) of ITree:"+stat.averageDepth);
	}
	
}