import java.io.*;

public class TestITree {
	private static TreeStatistics stat = new TreeStatistics();

	public static SubdomainNode none = new SubdomainNode(null);

		public static void main(String[] args) throws Exception{
			String fileName = args.length>0?args[0]:"data.txt";
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

			double[][] data = new double[nObjects][nDims];
			//System.out.println(nObjects+","+nDims);
			int i= 0;
			while(i<nObjects && (line = br.readLine()) != null){
			     String[] ss = line.split(",");
					 for(int j = 0;j<ss.length;j++){
						 data[i][j] = Double.parseDouble(ss[j]);
					 }
					 i++;
			}
			br.close();

			ITree t = new ITree(data);
			t.buildTree();

			stat.walk(t);

			System.out.println("number of Subdomains: "+stat.nLeafNodes);
			//System.out.println("average depth(includes SubdomainNode) of ITree: "+stat.averageDepth);

			printTreeNode(t.getRoot(),0,"");
		}

		public static void printTreeNode(Node e, int depth, String prefix){
			if(e==null || e instanceof SubdomainNode)return;
			InterNode n = (InterNode)e;
			for(int i = 0;i<depth;i++)System.out.print("\t");
			System.out.println(prefix+"intersection("+n.a+","+n.b+")");
			printTreeNode(n.left,depth+1,"left:");
			printTreeNode(n.right,depth+1,"right:");
		}
}
