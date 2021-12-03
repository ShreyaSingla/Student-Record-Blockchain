import Includes.*;
import java.util.*;

public class BlockChain{
	public static final String start_string = "LabModule5";
	public Block firstblock;
	public Block lastblock;

	public String InsertBlock(List<Pair<String,Integer>> Documents, int inputyear){
		
		CRF c = new CRF(64);
		MerkleTree t = new MerkleTree();
		t.Build(Documents);
		if (firstblock == null)
		{
			firstblock = new Block();
			lastblock = new Block();
			firstblock.next = null;
			firstblock.previous = null;
			firstblock.year = inputyear;
			firstblock.mtree = t;
			firstblock.value = t.rootnode.val + "_" + Integer.toString(t.rootnode.maxleafval);
			firstblock.dgst = c.Fn(start_string + "#" + firstblock.value);
			lastblock = firstblock;
		}

		else if (lastblock == firstblock)
		{
			Block b = new Block();
			b.previous = firstblock;
			//b.next = null;
			b.mtree = t;
			b.value = t.rootnode.val + "_" + Integer.toString(t.rootnode.maxleafval);
			b.dgst = c.Fn(b.previous.dgst + "#" + b.value);
			b.year = inputyear;
			firstblock.next = b;
			lastblock = b;
		}
		else
		{
			Block b = new Block();
			b.previous = lastblock;
			//b.next = null;
			b.mtree = t;
			b.value = t.rootnode.val + "_" + Integer.toString(t.rootnode.maxleafval);
			b.dgst = c.Fn(b.previous.dgst + "#" + b.value);
			b.year = inputyear;
			lastblock.next = b;
			lastblock = b;
		}
		return lastblock.dgst;
	}

	public Pair<List<Pair<String,String>>, List<Pair<String,String>>> ProofofScore(int student_id, int year){
		// Implement Code here

		ArrayList<Pair<String, String>> l = new ArrayList<Pair<String, String>>();
		Block temp = firstblock;
		Block use = firstblock;
		// use: whose year is the given year
		while(temp.year != year)
		{
			temp = temp.next;
		}
		use = temp;
		while(temp != lastblock)
		{
			Pair<String, String> t = new Pair<String, String>(temp.value, temp.dgst);
			l.add(t);
			temp = temp.next;
		}
		Pair<String, String> t = new Pair<String, String>(temp.value, temp.dgst);
		l.add(t);
		
		int len = temp.mtree.numstudents;
		TreeNode tr = new TreeNode();
		tr = use.mtree.rootnode;

		while (len > 1) {
			if (student_id < len / 2)
				tr = tr.left;
			else
				tr = tr.right;
			len = len / 2;
			student_id = student_id % len;
		}

		// tr is what I want!

		ArrayList<Pair<String, String>> a = new ArrayList<Pair<String, String>>();

		while (tr != use.mtree.rootnode) {
			if (tr.parent.left == tr) {
				Pair<String, String> s = new Pair<String, String>(tr.val, tr.parent.right.val);
				a.add(s);
			} else {
				Pair<String, String> s = new Pair<String, String>(tr.parent.left.val, tr.val);
				a.add(s);
			}

			tr = tr.parent;
		}
		Pair<String, String> s = new Pair<String, String>(use.mtree.rootnode.val, null);
		a.add(s);


		Pair<List<Pair<String, String>>, List<Pair<String, String>>> p = new Pair<List<Pair<String, String>>, List<Pair<String, String>>>(a, l);
		
		return p;
	}
}
