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

	// ListNode, QuoteNode, Node에 대한 printNode 함수를 각각 overload 형식으로 작성
	private void printList(ListNode listNode) {
		if (listNode == ListNode.EMPTYLIST) {
			sb.append("");
			return;
		}
		if (listNode == ListNode.EMPTYLIST) {
			return;
		}

		// 이후 부분을 주어진 출력 형식에 맞게 코드를 작성하시오
		printNode(listNode.car()); // listNode의 맨 처음 원소(head) 반환
		printList(listNode.cdr()); // listNode의 맨 처음 원소를 제외한 나머지(tail)를 반환
	}

	private void printNode(QuoteNode quoteNode) {
		if (quoteNode.nodeInside() == null)
			return;

		// 이후 부분을 주어진 출력 형식에 맞게 코드를 작성하시오.
		sb.append("\'"); // \' append
		if (quoteNode.nodeInside() instanceof ListNode) { // QuoteNode의 nodeInside()가 ListNode일 떄
			printNode((ListNode) quoteNode.nodeInside()); // ListNode타입으로 형변환하고 nodeInside()반환
		} else { // QuoteNode의 nodeInside()가 ListNode가 아닐 때
			printNode(quoteNode.nodeInside()); // nodeInside()반환
		}
	}

	private void printNode(Node node) {
		if (node == null)
			return;

		// 이후 부분을 주어진 출력 형식에 맞게 코드를 작성하시오.
		else if (node instanceof ListNode) { // ListNode일 때
			ListNode listNode = (ListNode) node; // ListNode타입으로 형변환
			if (listNode.car() instanceof QuoteNode)  // listNode의 car이 QuoteNode일 때
				printNode((QuoteNode) listNode.car()); // QuoteNode로 보냄
			else {
				sb.append("  (");
				printList(listNode); // printList메소드 호출
				sb.append(")  ");
			}
		} else {
			sb.append(" " + node + " "); // 나머지 node들 append
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
