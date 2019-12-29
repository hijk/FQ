package fq.itree.test;

import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import fq.itree.ITree;
import fq.itree.InterNode;
import fq.itree.SubdomainNode;
import fq.itree.TreeStatistics;


public class TestITree {
	private static TreeStatistics stat;
	
	public static SubdomainNode none = new SubdomainNode(null);
	
    @BeforeClass
    public static void beforeAll()  throws Exception{
    	stat = new TreeStatistics();
    }
 
    @AfterClass
    public static void afterAll()  throws Exception{
    }
    
    @Before
    public void beforeEach() {
    	stat.clear();
    }
    
    
    @Test
    public void test1() {
        double[][] data = { 
        		{1,2},
        		{2,1},
        };
        ITree t = new ITree(data);
        t.buildTree();
        
        stat.walk(t);
        
        InterNode node = t.getRoot();
        assertTrue(node.left instanceof SubdomainNode);
        assertTrue(node.right instanceof SubdomainNode);
        assertTrue(stat.nInnerNodes==1);
        assertTrue(stat.nLeafNodes==2);   
        assertTrue(t.equals(
        		new ITree(new InterNode(0,1,
        				none,
        				none)
        				))
        		);
    }
 
    @Test
    public void test2() {
        double[][] data = { 
        		{1,5},
        		{2,3},
        		{3,4},
        };
        ITree t = new ITree(data);
        t.buildTree();
        
        assertTrue(t.equals(
        		new ITree(new InterNode(0,1,
        				new InterNode(0,2,
        						new InterNode(1,2,none,none),
        						none),
        				none)
        				))
        		);
    }
    
    @Test
    public void test3() {
        double[][] data = { 
        		{1,3},
        		{2,2},
        		{1,1},
        		{3,1},
        };
        ITree t = new ITree(data);
        t.buildTree();
        assertTrue(true);
    }
    
    @Test
    public void test4() {
        double[][] data = { 
        		{1,3,2},
        		{2,2,1},
        		{1,1,4},
        		{3,1,3},
        };
        ITree t = new ITree(data);
        t.buildTree();

        InterNode node = t.getRoot();
        
        assertTrue(true);
    }
}