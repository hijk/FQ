package fq.itree;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SubdomainNode extends Node{
	private static final long serialVersionUID = 1L;
	
	private Integer[] rankedList;
	
	public SubdomainNode(Node p) {
		super(p);
		this.rankedList = new Integer[0];
	}

	public Integer[] getRankedList() {
		return rankedList;
	}

	public void setRankedList(Integer[] rankedList) {
		this.rankedList = rankedList;
	}
	
	public List<Intersection> describe(){
		List<Intersection> res = new LinkedList<Intersection>();
		InterNode n = (InterNode)this.parent;
		while(n!=null) {
			res.add(new Intersection(n.a, n.b));
		}
		Collections.reverse(res);
		return res;
	}
}
