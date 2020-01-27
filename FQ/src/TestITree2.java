import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import javax.swing.JFrame;

public class TestITree2 {
	private static TreeStatistics stat = new TreeStatistics();

	public static SubdomainNode none = new SubdomainNode(null);

	private static Logger logger = LogManager.getLogManager().getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public static void main(String[] args) throws Exception {
	    URL fis =  TestITree2.class.getClassLoader().getResource("logging.properties");
	    LogManager.getLogManager().readConfiguration(fis.openStream());
	
		testCases();
	}
	
	public static void testCases() {
//		test_2_3();
//		test_2_4();
		test_2_5();
		
//		test1();
//		
//		double[][] data = { 
//				{ 2, 4, 9 }, 
//				{ 7, 1, 0} , 
//				{-3, 6, -3},
//				{8, 2, -3},
//				{3,  -4, 4},
//		};
//		testTree(data, null, defaultWeights(data));
//		
//		double[][] data1 = { 
//				{ 0, 0, 1 }, 
//				{ 0, 1, 0} , 
//				{5, 2, 0},
//				{10, 20, -30},
//		};
//		testTree(data1, null, defaultWeights(data1));
	}
	
	private static void test_2_3() {
		double[][] data = { 
				{ 3, 2, }, 
				{ 2, 4, } , 
				{ 4, 1, },
		};
		
		testTree(data, null, defaultWeights(data));
	}

	public static void test_2_4() {
		double[][] data = { 
				{ 1, 3, }, 
				{ 7, 5, } , 
				{-2, 6, },
				{ 3, 2, },
		};
		
		testTree(data, null, defaultWeights(data));
	}
	
	public static void test_2_5() {
		double[][] data = { 
				{ 1, 3, }, 
				{ 7, 5, } , 
				{-2, 6, },
				{ 3, 2, },
				{ 8 , -1}
		};
		
		testTree(data, null, defaultWeights(data));
	}
	
	private static void test1() {
		double[][] data = { 
				{ 3, 2, 4 }, 
				{ 2, 1, 3 }, 
				{ 1, 3, 2 }, 
		};
		
		testTree(data, null, defaultWeights(data));	
	}
	
	public static double[][] defaultWeights(double[][] d){
		double[][] weights = new double[d[0].length][2];
		for (int i = 0; i < weights.length; i++) {
			weights[i][0] = -100f;
			weights[i][1] = 100f;
		}
		return weights;
	}
	
	public static void testTree(double[][] data, double[] constants, double[][] weights ) {
		ITree t = new ITree(data, constants, weights);
		
		for (int i = 0; i < data.length; i++) {
			for (int j = i + 1; j < data.length; j++) {
				logger.log(Level.CONFIG,"Intersection ("+i+","+j+"): " + t.expression(t.intersection(i, j), t.constants[i] - t.constants[j]));
			}
		}
		
		t.buildTree();

		stat.walk(t);

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
		window.setBounds(WindowX, WindowY, Size, Size);
		WindowX += Size;
		if(WindowX > screenSize.width) {
			WindowX = 0;
			WindowY += Size;
		}
		// setting canvas for draw
		window.getContentPane().add(new DomainDrawer(t));
		// set visibility
		window.setVisible(true);
	}
	
	public static int Size = 500;
	public static int WindowX = 0;
	public static int WindowY = 0;
	public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	public static void printTreeNode(ITree t, Node e, int depth, String prefix) {
		if (e == null)
			return;
		if( e instanceof SubdomainNode) {
			for (int i = 0; i < depth; i++)
				logger.log(Level.CONFIG, "\t");
			logger.log(Level.CONFIG, t.describe((SubdomainNode)e));
			return;
		}
		
		InterNode n = (InterNode) e;
		for (int i = 0; i < depth; i++)
			logger.log(Level.CONFIG,"\t");
		logger.log(Level.CONFIG, prefix + "intersection:(" + 
			n.i + "," + n.j+")");
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
