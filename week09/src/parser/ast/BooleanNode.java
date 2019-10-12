package parser.ast;

public class BooleanNode implements ValueNode{
	// ���� ������ BooleanNode Class
	public static BooleanNode FALSE_NODE = new BooleanNode(false);
	public static BooleanNode TRUE_NODE = new BooleanNode(true);
	Boolean value;
	
	private BooleanNode(Boolean b) {
		value = b;
	}
	
	@Override
	public String toString(){
		return value ? "#T" : "#F";
	}
}
