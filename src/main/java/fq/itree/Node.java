package fq.itree;

import java.io.Serializable;

public abstract class Node implements Serializable {
	protected Node parent = null;
	public int depth = 0;

	public Node(Node parent) {
		this.parent = parent;
		this.depth = parent!=null?  parent.depth+1: 1;
	}
}
