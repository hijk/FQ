//package fq.itree;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SubdomainNode extends Node {
	private static final long serialVersionUID = 1L;

	private int[] rankedList;

	public SubdomainNode(Node p) {
		super(p);
	}

	public int[] getRankedList() {
		return rankedList;
	}

	public void setRankedList(int[] rankedList) {
		this.rankedList = rankedList;
	}
}
