package fq.matrix;

import java.io.Serializable;
import java.util.List;

import fq.itree.AbstractTreeWalker;
import fq.itree.ITree;
import fq.itree.Node;
import fq.itree.SubdomainNode;

class RankWalker extends AbstractTreeWalker{
	
	public RankMatrix matrix;

	@Override
	public void callback(Node n) {
		if(n instanceof SubdomainNode) {
			int[] rankedlist = ((SubdomainNode)n).getRankedList();
			for(int i = 0; i< rankedlist.length; i++) {
				matrix.rankLists[rankedlist[i]][i].add((SubdomainNode)n);
			}
		}
	}
}

public class RankMatrix implements Serializable{

	transient protected ITree tree;
	protected List<SubdomainNode>[][] rankLists;
	
	public RankMatrix(ITree tree) {
		this.tree = tree;
	}

	public void fill() {
		RankWalker walker = new RankWalker();
		walker.matrix = this;
		walker.walk(this.tree);
	}
	
	public List<SubdomainNode> at(int objIdx, int rank){
		return this.rankLists[objIdx][rank];
	}
	
}
