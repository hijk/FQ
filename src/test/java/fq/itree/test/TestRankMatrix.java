package fq.itree.test;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import fq.itree.ITree;
import fq.matrix.RankMatrix;

public class TestRankMatrix {
    @BeforeClass
    public static void beforeAll()  throws Exception{
    }
 
    @AfterClass
    public static void afterAll()  throws Exception{
    }
    
    @Before
    public void beforeEach() {
    }
    
    
    @Test
    public void test() {
        double[][] data = { 
        		{1,5},
        		{2,3},
        		{3,4},
        };
        ITree t = new ITree(data);
        t.buildTree();
        
        RankMatrix m = new RankMatrix(t);
        m.fill();
        
        
    }
}
