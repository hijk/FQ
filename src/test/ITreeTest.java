package test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import fq.itree.ITree;
import fq.itree.InterNode;
import fq.itree.SubdomainNode;
import fq.itree.TreeStatistics;

@DisplayName("ITree First")
class ITreeTest {
	private static TreeStatistics stat;
	
    @BeforeAll
    static void beforeAll() {
    	stat = new TreeStatistics();
    }
 
    @AfterAll
    static void afterAll() {
    }
    
    @BeforeEach
    void beforeEach() {
    	stat.clear();
    }
    
    
    //@Test
    void test1() {
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
    }
 
    @Test
    void test2() {
        double[][] data = { 
        		{1,5},
        		{2,3},
        		{3,4},
        };
        ITree t = new ITree(data);
        t.buildTree();

    }
    
    @Test
    void test3() {
        double[][] data = { 
        		{1,3},
        		{2,2},
        		{1,1},
        		{3,1},
        };
        ITree t = new ITree(data);
        t.buildTree();

    }
    
    @Test
    void test4() {
        double[][] data = { 
        		{1,3,2},
        		{2,2,1},
        		{1,1,4},
        		{3,1,3},
        };
        ITree t = new ITree(data);
        t.buildTree();

        InterNode node = t.getRoot();
    }
}