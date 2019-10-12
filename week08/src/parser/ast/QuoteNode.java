package parser.ast;

public class QuoteNode implements Node {
	// ���� �߰��� QuoteNode Class
	Node quoted;

	public QuoteNode(Node quoted) {
		this.quoted = quoted;
	}

	@Override
	public String toString() {
		return quoted.toString();
	}

	public Node nodeInside() {
		return quoted;
	}
}
