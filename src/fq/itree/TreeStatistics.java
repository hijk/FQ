package fq.itree;

public class TreeStatistics extends AbstractTreeWalker{
	public int nLeafNodes;
	public int nInnerNodes;
	public int maxDepth = 0;
	public float averageDepth = 0;
	private int _sumDepth = 0;
	
	@Override
	public void callback(Node n) {
		if(n instanceof SubdomainNode) {
			this.nLeafNodes += 1;
			this.maxDepth = Math.max(this.maxDepth, n.depth);
			this._sumDepth += n.depth;
		}else {
			this.nInnerNodes +=1;
		}
		
	}

	public void clear() {
		this.nInnerNodes = 0;
		this.nLeafNodes = 0;
		this.maxDepth = 0;
		this._sumDepth = 0;
		this.averageDepth = 0;
	}

	@Override
	public void afterall() {
		this.averageDepth = ((float)this._sumDepth)/this.nLeafNodes;
	}
}
