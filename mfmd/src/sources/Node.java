package sources;

import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

@SuppressWarnings("unchecked")
public class Node extends DefaultMutableTreeNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2953702767597834982L;

	public Node(Object data) {
		super(data);
	}

	public Node() {

	}

	public int getSize() {
		int size = 0;

		Enumeration<Node> e = preorderEnumeration();

		while (e.hasMoreElements()) {
			e.nextElement();
			size++;
		}

		return size;

	}

	@Override
	public void add(MutableTreeNode newChild) {
		super.add(newChild);
	}

	@Override
	public void remove(MutableTreeNode aChild) {
		super.remove(aChild);
	}

}
