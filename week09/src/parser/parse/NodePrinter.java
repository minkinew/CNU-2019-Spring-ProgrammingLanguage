package parser.parse;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import parser.ast.ListNode;
import parser.ast.Node;
import parser.ast.QuoteNode;

public class NodePrinter {
	private final String OUTPUT_FILENAME = "output08.txt";
	public StringBuffer sb = new StringBuffer();
	private Node root;

	public NodePrinter(Node root) {
		this.root = root;
	}

	// ListNode, QuoteNode, Node�� ���� printNode �Լ��� ���� overload �������� �ۼ�
	private void printList(ListNode listNode) {
		if (listNode == ListNode.EMPTYLIST) {
			sb.append("");
			return;
		}
		if (listNode == ListNode.EMPTYLIST) {
			return;
		}

		// ���� �κ��� �־��� ��� ���Ŀ� �°� �ڵ带 �ۼ��Ͻÿ�
		printNode(listNode.car()); // listNode�� �� ó�� ����(head) ��ȯ
		printList(listNode.cdr()); // listNode�� �� ó�� ���Ҹ� ������ ������(tail)�� ��ȯ
	}

	private void printNode(QuoteNode quoteNode) {
		if (quoteNode.nodeInside() == null)
			return;

		// ���� �κ��� �־��� ��� ���Ŀ� �°� �ڵ带 �ۼ��Ͻÿ�.
		sb.append("\'"); // \' append
		if (quoteNode.nodeInside() instanceof ListNode) { // QuoteNode�� nodeInside()�� ListNode�� ��
			printNode((ListNode) quoteNode.nodeInside()); // ListNodeŸ������ ����ȯ�ϰ� nodeInside()��ȯ
		} else { // QuoteNode�� nodeInside()�� ListNode�� �ƴ� ��
			printNode(quoteNode.nodeInside()); // nodeInside()��ȯ
		}
	}

	private void printNode(Node node) {
		if (node == null)
			return;

		// ���� �κ��� �־��� ��� ���Ŀ� �°� �ڵ带 �ۼ��Ͻÿ�.
		else if (node instanceof ListNode) { // ListNode�� ��
			ListNode listNode = (ListNode) node; // ListNodeŸ������ ����ȯ
			if (listNode.car() instanceof QuoteNode)  // listNode�� car�� QuoteNode�� ��
				printNode((QuoteNode) listNode.car()); // QuoteNode�� ����
			else {
				sb.append("  (");
				printList(listNode); // printList�޼ҵ� ȣ��
				sb.append(")  ");
			}
		} else {
			sb.append(" " + node + " "); // ������ node�� append
		}
	}

	public void prettyPrint() {
		printNode(root);

		try (FileWriter fw = new FileWriter(OUTPUT_FILENAME); PrintWriter pw = new PrintWriter(fw)) {
			pw.write(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
