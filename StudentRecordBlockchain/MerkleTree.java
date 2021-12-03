import Includes.*;
import java.util.*;

public class MerkleTree{
	
	public TreeNode rootnode;
	public int numstudents;

	public String Build(List<Pair<String,Integer>> documents){
		
		CRF c = new CRF(64);
		int l = documents.size();
		if (l == 1) {
			rootnode.val = documents.get(0).First + "_" + documents.get(0).Second;
			rootnode.left = null;
			rootnode.right = null;
			rootnode.parent = null;
			rootnode.maxleafval = documents.get(0).Second;
			rootnode.numberLeaves = 1;
			rootnode.isLeaf = true;
			return rootnode.val;
		}

		TreeNode[] arr = new TreeNode[2 * l];

		int i = 0;
		for (i = 0; i < l; i++) {
			arr[i] = new TreeNode();
			arr[i].val = documents.get(i).First + "_" + documents.get(i).Second;
			arr[i].left = null;
			arr[i].right = null;
			arr[i].parent = null;
			arr[i].maxleafval = documents.get(i).Second;
			arr[i].numberLeaves = 1;
			arr[i].isLeaf = true;
		}

		int num = l;
		while (num > 1) {
			int t = i;
			for (int temp = t - num; temp < t; temp += 2) {
				arr[i] = new TreeNode();
				arr[i].val = c.Fn(arr[temp].val + "#" + arr[temp + 1].val);
				arr[i].left = arr[temp];
				arr[i].right = arr[temp + 1];
				arr[i].parent = null;
				arr[temp].parent = arr[i];
				arr[temp + 1].parent = arr[i];
				if (arr[temp].maxleafval > arr[temp + 1].maxleafval)
					arr[i].maxleafval = arr[temp].maxleafval;
				else
					arr[i].maxleafval = arr[temp + 1].maxleafval;

				arr[i].isLeaf = false;
				arr[i].numberLeaves = arr[temp].numberLeaves + arr[temp + 1].numberLeaves;
				i++;
			}
			num = num / 2;
		}

		rootnode = new TreeNode();
		rootnode = arr[i - 1];

		numstudents = l;
		return rootnode.val;
	}

	/*
		Pair is a generic data structure defined in the Pair.java file
		It has two attributes - First and Second
	*/

	public String UpdateDocument(int student_id, int newScore){
		
		int l = numstudents;
		CRF c = new CRF(64);
		TreeNode temp = new TreeNode();
		temp = rootnode;

		while (l > 1)
		{
			if (student_id < l / 2)
				temp = temp.left;
			else
				temp = temp.right;

			l = l / 2;
			student_id = student_id % l;
		}
		
		// temp is the node whose score I have to change
		int len = temp.val.length();
		temp.val = temp.val.substring(0, len - 2) + Integer.toString(newScore);
		temp.maxleafval = newScore;

		while(temp!=rootnode)
		{
			temp = temp.parent;
			temp.val = c.Fn(temp.left.val + "#" + temp.right.val);
			
			if(temp.left.maxleafval > temp.right.maxleafval)
				temp.maxleafval = temp.left.maxleafval;
			else
				temp.maxleafval = temp.right.maxleafval;			
		}
		
		rootnode = temp;
		return rootnode.val;
	}
}
