package fq.itree.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.uncommons.maths.random.ContinuousUniformGenerator;

import fq.itree.ITree;
import fq.itree.SubdomainNode;
import fq.itree.TreeStatistics;
import fq.utils.DataGenerator;

public class TestTreePerformance {
	private static TreeStatistics stat;
	
	private static int samplePerDim = 3;
	
	private static Random random = new Random(2019);
    
    @BeforeClass
    public static void beforeAll()  throws Exception{
    	stat = new TreeStatistics();
    	
    	Logger.getGlobal().setLevel(Level.INFO);
    }
 
    @AfterClass
    public static void afterAll()  throws Exception{
    }
    
    @Before
    public void beforeEach() {
    	stat.clear();
    }
    
//    @Test
//    public void testFlipRanklist() {        
//        for(int n = 0;n< Math.pow(samplePerDim, nDim);n++ ) {
//        	double[] w = sampleWeight(nDim, weightRanges);
//        	List<Integer> l = sortByWeight(data, w);
//        	
//        	List<Integer> rankedlist = new ArrayList<Integer>();
//        	
//        }    
//        assertTrue(true);
//    }
    
    @Test
    public void test1() {
    	double[][] ranges = {
    			{0f,10f},
    			{0f,10f},
    			{0f,10f}
        };
    	
    	double[] w =  {0.2f, 0.3f, 0.4f};
    	
    	compareQueryAndSort(10,3,ranges, null, w);
    }
    
    //@Test
    public void test2() {
    	double[][] ranges = {
    			{0f,10f},
    			{0f,10f},
    			{0f,10f}
        };
    	
    	double[] w =  {0.2f, 0.3f, 0.4f};
    	
    	compareQueryAndSort(200,3,ranges, null, w);
    }
    
    //@Test
    public void test3() {
    	double[][] ranges = {
    			{0f,10f},
    			{0f,10f},
    			{0f,10f}
        };
    	
    	double[] w =  {0.2f, 0.3f, 0.4f};
    	
    	compareQueryAndSort(200,3,ranges, null, w);
    }
    
    /** 
     * nObject: number of objects,
     * nDim: number of dimensions,
     * ranges: ranges of data,
     * weightRanges: ranges of weights,
     * w: the weights to be queries.
     */
    public void compareQueryAndSort(int nObject, int nDim, double[][] ranges, double[][] weightRanges, double[] w){
		DataGenerator g = new DataGenerator(nObject, nDim, null);
		
		int seed = new Random().nextInt();
		Random gaia = new Random(seed);
		
		for(int i = 0;i<nDim; i++) {
			//uniform distribution.
			g.generators.add(new ContinuousUniformGenerator(ranges[i][0],ranges[i][1],new Random(gaia.nextInt())));
		}
		
		g.generate();
		
        ITree t = new ITree(g.data);
        t.buildTree();
        
        stat.walk(t);
        
        Logger logger = Logger.getLogger(TestTreePerformance.class.getName());
		logger.log(Level.INFO , String.format("Tree has %d leafNodes, %d max Depth, %f average Depth.", 
				stat.nLeafNodes, stat.maxDepth, stat.averageDepth));
        
        long tStart = System.currentTimeMillis();
        
        //query
    	SubdomainNode n = t.search(w);
        assertTrue(true);
        
        long tEnd = System.currentTimeMillis();
        double tDelta = tEnd - tStart;
        double elapsedSeconds = tDelta / 1000.0;
        
        logger.log(Level.INFO, String.format("Querying takes %f s\n", elapsedSeconds));
        
        tStart = System.currentTimeMillis();
        
        //sort
    	List<Integer> l = sortByWeight(g.data, w);
        assertTrue(true);
        
        tEnd = System.currentTimeMillis();
        tDelta = tEnd - tStart;
        elapsedSeconds = tDelta / 1000.0;
        logger.log(Level.INFO, String.format("Querying takes %f s\n", elapsedSeconds));
    }
    
    public List<Integer> sortByWeight(double[][] data, double[] w) {
    	List<Integer> l = new ArrayList<Integer>();
    	for(int i=0; i<w.length ;i++) {
    		l.add(i);
    	}
    	Collections.sort(l, new Comparator<Integer>() {
			@Override
			public int compare(Integer arg0, Integer arg1) {
				double diff = ITree.score(data[arg0], w) - ITree.score(data[arg1],w);
				return diff>0? 1:diff<0?-1:0;
			}
    		
    	});
    	return l;
    }

    public static double[] sampleWeight(int dim, double[][] weightRanges){
    	double[] weight = new double[dim];
    	for(int i = 0;i<dim;i++) {
    		double min = weightRanges[i][0];
    		double max = weightRanges[i][1];
    		weight[i] = min + random.nextDouble() * (max - min);
    	}
    	return weight;
    }
}
