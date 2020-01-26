//package fq.itree;

public class Intersection {
	public int a;
	public int b;
	/*That means intersection(a,b) seperates the left/right half subdomain of its parent.*/
	public boolean greatThan;
	
	public Intersection(int a, int b, boolean ge) {
		this.a = a;
		this.b = b;
		this.greatThan = ge;
	}
}
