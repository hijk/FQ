//package fq.itree;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

public class ITree implements Serializable{

	private static final long serialVersionUID = 1L;
	private InterNode root = null;
	public InterNode getRoot() {
		return root;
	}

	transient private double [][] data;
	public double[][] weightRanges = null;
	private int nObjects;

	public ITree(double [][] data) {
		this.data = data;
		this.nObjects = data.length;
	}

	public ITree(double [][] data, double[][] weightRanges) {
		this.data = data;
		this.nObjects = data.length;
		this.weightRanges = weightRanges;
	}

	public void buildTree() {
		for(int i = 0;i<this.nObjects;i++) {
			for(int j = i+1;j<this.nObjects;j++) {
				this.insertIntersection(i,j);
			}
		}
	}

	public void insertIntersection(int i, int j) {
		if(this.root==null) {
			this.root = this.makeInterNode(i,j,null);
			return;
		}

		Queue<Node> queue = new LinkedList<Node>();
		queue.add(this.root);

		while(queue.size()>0) {
			Node n = queue.poll();
			if(n instanceof SubdomainNode) {
				InterNode p = (InterNode)n.parent;
				if(p.left==n) {
					p.left = this.makeInterNode(i, j, p);
				}else if(p.right==n){
					p.right = this.makeInterNode(i, j, p);
				}
				return;
			}else {
				InterNode inode = (InterNode)n;
				int p = inode.a;
				int q = inode.b;
				if(existRoot(p,q,i,j,true)) {// exists some X so that fp (X) − fq (X) ≥ 0 and fi (X) − fj (X) = 0.
					queue.add(inode.left);
				}else if(existRoot(p,q,i,j,false)) {// exists some X so that fp (X) − fq (X) < 0 and fi (X) − fj (X) = 0.
					queue.add(inode.right);
				}
			}
		}
	}

	private boolean existRoot(int p, int q, int i, int j, boolean above) {
		int k = 0;
		double[] pq = intersection(p,q);
		if(above==false) {
			for(int l = 0;l<pq.length;l++) {
				pq[l] = -pq[l];
			}
		}
		double[] ij = intersection(i,j);
		int l = 0;
		while(Math.abs(ij[l])<1e-6f) {
			l++;
		}
		double multiplier = pq[l]/ij[l];
		for(k = 0;k<pq.length;k++) {
			pq[k] -= ij[k]*multiplier;
		}

		return this.existSolution(pq);
	}

	private boolean existSolution(double[] coeficient) {
		//-x+y>=0 and -x+y=0 doestn't split the node.
		if(this.weightRanges==null) {
			for(int i = 0; i<coeficient.length;i++) {
				if(coeficient[i]>0)return true;
			}
			return false;
		}else {
			double[] w = new double[this.weightRanges.length];
			for(int i = 0; i<w.length;i++) {
				w[i] = this.weightRanges[i][0];
			}
			if(enumerateVertex(0, w, coeficient))
				return true;
			return false;
		}
	}

	private boolean enumerateVertex(int i, double[] w, double[] coeficient){
		if(i==w.length) {
			if(score(w, coeficient)>0)
				return true;
			return false;
		}else {
			if(enumerateVertex(i+1,w,coeficient))
				return true;
			w[i] = this.weightRanges[i][1];
			if(enumerateVertex(i+1,w,coeficient))
				return true;
			w[i] = this.weightRanges[i][0];
			return false;
		}
	}

	public InterNode makeInterNode(int i, int j, Node parent) {
		InterNode n = new InterNode(parent,i,j);
		n.left = new SubdomainNode(n);
		n.right = new SubdomainNode(n);
		return n;
	}

	public SubdomainNode search(double[] weight) {
		Node tmp = root;
		while(tmp!=null && !(tmp instanceof SubdomainNode)) {
			InterNode n = (InterNode)tmp;
			double score1 = score(weight, this.data[n.a]);
			double score2 = score(weight, this.data[n.b]);
			if(score1 >= score2) {
				tmp = n.left;
			}else {
				tmp = n.right;
			}
		}

		return (SubdomainNode)tmp;
	}

	public static double score(double[] weight, double[] values) {
		double s = 0f;
		for(int i = 0;i<weight.length;i++) {
			s+=weight[i]*values[i];
		}
		return s;
	}

	public double[] intersection(int i, int j){
		double[] di = this.data[i];
		double[] dj = this.data[j];
		double[] tmp = new double[this.data.length];
		for(int k = 0;k<di.length;k++) {
			tmp[k] = di[k] - dj[k];
		}
		return tmp;
	}

}
