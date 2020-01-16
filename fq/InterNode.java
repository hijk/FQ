

import java.util.List;

public class InterNode extends Node{
	public Node left;
	public Node right;

	//The indices of two objects that form the intersection of this node.
	protected int a;
	protected int b;

	protected List<Integer> Llist;

	public InterNode(Node p,int i1, int i2) {
		super(p);
		this.a = i1;
		this.b = i2;
		this.left = null;
		this.right = null;
		this.parent = null;
	}



}
