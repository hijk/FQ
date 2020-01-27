
import java.util.List;

public class InterNode extends Node {
	public Node left;
	public Node right;

	// The indices of two objects that form the intersection of this node.
	protected int i;
	protected int j;
	
	protected int progress_i = 0;
	protected int progress_j = 0;

	protected List<Intersection> L;

	public InterNode(Node p, int i1, int i2) {
		super(p);
		this.i = i1;
		this.j = i2;
	}

}
