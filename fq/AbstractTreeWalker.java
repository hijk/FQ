
public abstract class AbstractTreeWalker {

	public void walk(ITree tree){
		if(tree.getRoot()!=null)
			visit(tree.getRoot());
		afterall();
	}
	
	public void visit(Node n) {
		if(n!=null)
			callback(n);
		if(n instanceof InterNode) {
			InterNode node = (InterNode)n;
			  if(node.left!=null)
				  visit(node.left);
			  if(node.right!=null)
				  visit(node.right);
		}
	}

	public abstract void callback(Node n);
	
	public void afterall() {
	}
}
