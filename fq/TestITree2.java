import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.swing.JFrame;

public class TestITree2 {
	private static TreeStatistics stat = new TreeStatistics();

	public static SubdomainNode none = new SubdomainNode(null);

	private static Logger logger = LogManager.getLogManager().getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public static void main(String[] args) throws Exception {
	
		logger.setLevel(Level.CONFIG);
		
		double[][] data = { { 3, 2, 4 }, { 2, 1, 3 }, { 1, 3, 2 },
		};

		double[][] weights = new double[data[0].length][2];
		for (int i = 0; i < weights.length; i++) {
			weights[i][0] = -10f;
			weights[i][1] = 10f;
		}
		
		ITree t = new ITree(data, weights, true);
		t.buildTree();

		stat.walk(t);
		
		for (int i = 0; i < data.length; i++) {
			for (int j = i + 1; j < data.length; j++) {
				logger.log(Level.CONFIG,"Intersection("+i+","+j+")" + t.expression(t.intersection(i, j), t.constants[i] - t.constants[j]));
			}
		}

		logger.log(Level.CONFIG, "number of Subdomains: " + stat.nLeafNodes);
		// System.out.println("average depth(includes SubdomainNode) of ITree:
		// "+stat.averageDepth);
		if (stat.nLeafNodes < 100)
			printTreeNode(t, t.getRoot(), 0, "");

		// creating object of JFrame(Window popup)
		JFrame window = new JFrame();
		// setting closing operation
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// setting size of the pop window
		window.setBounds(100, 100, 800, 800);
		// setting canvas for draw
		window.getContentPane().add(new DomainDrawer(t));
		// set visibility
		window.setVisible(true);
	}

	public static void printTreeNode(ITree t, Node e, int depth, String prefix) {
		if (e == null)
			return;
		if( e instanceof SubdomainNode) {
			for (int i = 0; i < depth; i++)
				logger.log(Level.CONFIG, "\t");
			logger.log(Level.CONFIG, t.describe((SubdomainNode)e));
		}
		
		InterNode n = (InterNode) e;
		for (int i = 0; i < depth; i++)
			logger.log(Level.CONFIG,"\t");
		logger.log(Level.CONFIG, prefix + "intersection:(" + n.i + "," + n.j+")");
		double[] ab = t.intersection(n.i, n.j);
		for (double d : ab) {
			System.out.print(d + ", ");
		}
//		if (n.L != null) {
//			System.out.print("  L: ");
//			for (Intersection i : n.L) {
//				System.out.print("(" + i.a + "," + i.b + ") ");
//			}
//		}
//		System.out.println("");

		printTreeNode(t, n.left, depth + 1, "left:");
		printTreeNode(t, n.right, depth + 1, "right:");
	}

	
}
