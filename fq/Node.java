//package fq.itree;

import java.io.Serializable;

public abstract class Node  implements Serializable {
	protected Node parent = null;
	public int depth = 0;

	public Node(Node p) {
		this.parent = p;
		this.depth = p!=null?  p.depth+1: 1;
	}
}
