//package fq.itree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.math3.exception.TooManyIterationsException;
import org.apache.commons.math3.optim.MaxIter;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.NoFeasibleSolutionException;
import org.apache.commons.math3.optim.linear.Relationship;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.linear.UnboundedSolutionException;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

class InterNodeConstraint extends LinearConstraint{
	private static final long serialVersionUID = 1L;
	public boolean equalAllowed;

	public InterNodeConstraint(double[] coefficients, Relationship relationship, double value, boolean equalAllowed) {
		super(coefficients, relationship, value);
		this.equalAllowed = equalAllowed;
	}

}

public class ITree implements Serializable {

	private static final long serialVersionUID = 1L;
	private InterNode root = null;

	public InterNode getRoot() {
		return root;
	}

	private final static Logger LOGGER = Logger.getLogger(ITree.class.getName());

	public double[][] data;
	public double[] constants;
	private int nObjects;
	private int nDims;
	public double[][] weightRanges = null;

	private List<SubdomainNode> bottomNodes;

	public ITree(double[][] data, double[] constants, double[][] weightRanges, boolean c) {
		if (weightRanges != null && data[0].length != (weightRanges.length)) {
			LOGGER.log(Level.SEVERE, "dimension of data != dimension of weights.");
			System.exit(1);
		}

		this.nObjects = data.length;
		if(data.length > 0)
			this.nDims = data[0].length;
		this.data = data;

		if (weightRanges == null) {
			double[][] weights = new double[data[0].length][2];
			for (int i = 0; i < weights.length; i++) {
				weights[i][0] = 0;
				weights[i][1] = Integer.MAX_VALUE;
			}
		} else {
			this.weightRanges = weightRanges;
		}

		if(this.constants==null) {
			double[] zeros = new double[data.length];
			Arrays.fill(zeros, 0);
			this.constants = zeros;
		}else {
			this.constants = constants;
		}

		this.bottomNodes = new ArrayList<SubdomainNode>();
	}

	public ITree(double[][] data, double[][] weightRanges, boolean c) {
		this(data,null,weightRanges,false);
	}

	public ITree(double[][] data, boolean c) {
		this(data,null,null,c);
	}

	public ITree(double[][] data) {
		this(data,null,null,false);
	}

	public void buildTree() {
		for (int i = 0; i < this.nObjects; i++) {
			for (int j = i + 1; j < this.nObjects; j++) {
				LOGGER.log(Level.INFO,"Intersection("+i+","+j+")");
				this.insertIntersection(i, j);
			}
		}
	}

	public void insertIntersection(int i, int j) {
		if (this.root == null) {
			boolean[] results = canSplitRoot(i, j);
			if(results[0] || results[1]) {
				LOGGER.log(Level.CONFIG, "solution: "+Arrays.toString(results));
				this.root = new InterNode(null, i, j);
				if(results[0]) {
					this.bottomNodes.add((SubdomainNode) (this.root.left = new SubdomainNode(this.root)));
				}
				if(results[1]) {
					this.bottomNodes.add((SubdomainNode) (this.root.right = new SubdomainNode(this.root)));
				}
			}			
			return;
		}

		List<SubdomainNode> bottomNodes2 = new ArrayList<SubdomainNode>();

		for(SubdomainNode inode: this.bottomNodes) {
			InterNode parent = (InterNode) inode.parent;
			boolean left = parent.left==inode;
			boolean[] results = canSplit(inode, left, i, j);
			if(results[0] || results[1]) {// exists some X so that fp (X) − fq (X) ≥ 0 and fi (X) − fj (X)
				// = 0.
				LOGGER.log(Level.CONFIG, "solution: "+Arrays.toString(results));
				
				InterNode node = this.makeInterNode(i, j, inode);
				if(left)
					parent.left = node;
				else
					parent.right = node;
				
				bottomNodes2.add((SubdomainNode)node.left);
//				bottomNodes2.add((SubdomainNode)node.right);
			}else {
				LOGGER.log(Level.CONFIG, "solution: false");
				
				if(parent.L==null)
					parent.L = new ArrayList<Intersection>();
				parent.L.add(new Intersection(i,j,left));

				bottomNodes2.add(inode);
			}
		}

		this.bottomNodes = bottomNodes2;
	}

	private boolean[] canSplitRoot(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private boolean[] canSplit(SubdomainNode n, boolean left, int i, int j) {
		double[] ij = intersection(i, j);
		double cij = this.constants[i] - this.constants[j];
		int l = 0;
		while (l<nDims && Math.abs(ij[l]) == 0f) {
			l++;
		}

		if (l == nDims) {
			if (ij[nDims - 1] == 0f) {
				return true;
			} else {
				return false;
			}
		}

		LinearObjectiveFunction f = new LinearObjectiveFunction(ij, cij);	
		
		Collection<LinearConstraint> constraints = new
				ArrayList<LinearConstraint>();
		
		InterNode parent = (InterNode) n.parent;
		Node node = n;
		while(parent!=null) {
			left = parent.left==node;
			double[] pq = intersection(parent.i, parent.j);
			double cpq = this.constants[parent.i] - this.constants[parent.j];
			if(constraints.size()>0) {
				constraints.add(new InterNodeConstraint(pq, left? Relationship.GEQ: Relationship.LEQ, -cpq, left));
			}else {
				if (left == false) {
					for (l = 0; l < pq.length; l++) {
						pq[l] = -pq[l];
					}
					cpq = -cpq;
				}
			}
			node = parent;
			parent = (InterNode) parent.parent;
		}
		
		for(int k =0;k<nDims;k++) {
			double[] zeros = new double[nDims];
			Arrays.fill(zeros, 0f);
			zeros[k] = 1;
			constraints.add(new InterNodeConstraint(zeros, Relationship.GEQ, this.weightRanges[k][0], true));
			constraints.add(new InterNodeConstraint(zeros, Relationship.LEQ, this.weightRanges[k][1], true));
		}

		LOGGER.log(Level.CONFIG, "\n----Solving LP----");
		LOGGER.log(Level.CONFIG, "target: "+ this.expression(f.getCoefficients().toArray(), f.getConstantTerm()));
		for(LinearConstraint c: constraints) {
			InterNodeConstraint ic = (InterNodeConstraint)c;
			String s = ic.getRelationship()==Relationship.GEQ? " >= ": " < ";
			LOGGER.log(Level.CONFIG, "constraint: "+ this.expression(ic.getCoefficients().toArray(), ic.getValue()) + s + 0f);
		}
		
		boolean[] results = new boolean[2];
		
		SimplexSolver solver = new SimplexSolver();
		PointValuePair optSolution = null;
		try {
			optSolution = solver.optimize(f, new MaxIter(100), new
				LinearConstraintSet(constraints),
				GoalType.MAXIMIZE);
		}catch(TooManyIterationsException e) { //?
			LOGGER.log(Level.CONFIG, "result1: TooManyIterations");
			results[0] = false;
		}catch(NoFeasibleSolutionException e) {
			LOGGER.log(Level.CONFIG, "result1: NoFeasibleSolution");
			results[0] = false;
		}catch(UnboundedSolutionException e) {
			LOGGER.log(Level.CONFIG, "result1: UnboundedSolution");
			results[0] = true;
		}

		double[] solution = optSolution.getPoint();
		double max = optSolution.getValue();

		LOGGER.log(Level.CONFIG, "max: " + max + "\n");
		
		// requires max >= v and v >=0
		if(max >0) {
			results[0] = true;
		}else if(max==0) {
			if(!left)
				results[0] = false;
			for(LinearConstraint c: constraints) {
				InterNodeConstraint ic = (InterNodeConstraint)c;
				if(!ic.equalAllowed && this.score(solution, ic.getCoefficients().toArray(), ic.getValue())==0 ) {
					results[0] = false;
				}
			}
			results[0] = true;
		}else
			results[0] = false;
		
		
		try {
			optSolution = solver.optimize(f, new MaxIter(100), new
				LinearConstraintSet(constraints),
				GoalType.MINIMIZE);
		}catch(TooManyIterationsException e) { //?
			LOGGER.log(Level.CONFIG, "result2: TooManyIterations");
			results[1] = false;
		}catch(NoFeasibleSolutionException e) {
			LOGGER.log(Level.CONFIG, "result2: NoFeasibleSolution");
			results[1] = false;
		}catch(UnboundedSolutionException e) {
			LOGGER.log(Level.CONFIG, "result2: UnboundedSolution");
			results[1] = true;
		}
		
		solution = optSolution.getPoint();
		double min = optSolution.getValue();

		LOGGER.log(Level.CONFIG, "min: " + min + "\n");
		
		//requires min <= v and  v < 0
		if(min < 0) {
			results[1] = true;
		}else
			results[1] = false;
		
		return results;
	}

	public InterNode makeInterNode(int i, int j, Node parent) {
		InterNode n = new InterNode(parent, i, j);
		n.left = new SubdomainNode(n);
		n.right = new SubdomainNode(n);
		return n;
	}

//	public SubdomainNode search(double[] weight) {
//		InterNode node = root;
//		while (node==null || ( node.i <= this.nObjects-1 && node.j <= this.nObjects ) {
//			
//			
//			InterNode n = (InterNode) node;
//			double score1 = this.score(weight, this.data[n.i], this.constants[n.i]);
//			double score2 = this.score(weight, this.data[n.j], this.constants[n.j]);
//			if (score1 >= score2) {
//				node = n.left;
//			} else {
//				node = n.right;
//			}
//		}
//
//		return (SubdomainNode) node;
//	}

	public double score(double[] weight, double[] values, double constant) {
		double s = 0f;
		for (int i = 0; i < values.length; i++) {
			s += weight[i] * values[i];
		}
		s += constant;
		return s;
	}

	public double[] intersection(int i, int j) {
		double[] di = this.data[i];
		double[] dj = this.data[j];
		double[] tmp = new double[this.data[0].length];
		for (int k = 0; k < di.length; k++) {
			tmp[k] = di[k] - dj[k];
		}
		return tmp;
	}

	public String describe(SubdomainNode e) {
		InterNode p = (InterNode) e.parent;
		String s = "";
		while(p!=null) {
			int i = p.i;
			int j = p.j;
			s += this.expression(this.intersection(i, j), this.constants[i]-this.constants[j]) +"\n";
			p = (InterNode) p.parent;
		}
		
		return s;
	}
	
	public String expression(double[] data, double c) {
		String s = "";
		for(int i = 0;i<data.length;i++) {
			s += data[i] + " * "+ (i<Variables.length?Variables[i]:"?") + " + ";
		}
		
		return s + c;
	}
	
	public static String[] Variables = {
		"x","y","z","p","q","r","s","t"
	};

}
