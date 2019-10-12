package interpreter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import parser.ast.BinaryOpNode;
import parser.ast.BooleanNode;
import parser.ast.FunctionNode;
import parser.ast.IdNode;
import parser.ast.IntNode;
import parser.ast.ListNode;
import parser.ast.Node;
import parser.ast.QuoteNode;
import parser.parse.CuteParser;
import parser.parse.NodePrinter;

public class CuteInterpreter {
	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in); // Scanner ��ü ����

		while (true) {
			System.out.print("> ");
			String s = sc.nextLine(); // ���ڿ� �Է�
			File file = new File("as09.txt"); // ���� ��ü ����
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write(s);
			fileWriter.close();

			CuteParser cuteParser = new CuteParser(file);
			CuteInterpreter interpreter = new CuteInterpreter();
			Node parseTree = cuteParser.parseExpr();
			Node resultNode = interpreter.runExpr(parseTree);
			NodePrinter nodePrinter = new NodePrinter(resultNode);
			nodePrinter.prettyPrint();
			System.out.print("... ");
			System.out.println(nodePrinter.sb.toString());
		}
	}

	private void errorLog(String err) {
		System.out.println(err);
	}

	public Node runExpr(Node rootExpr) {
		if (rootExpr == null)
			return null;
		if (rootExpr instanceof IdNode)
			return rootExpr;
		else if (rootExpr instanceof IntNode)
			return rootExpr;
		else if (rootExpr instanceof BooleanNode)
			return rootExpr;
		else if (rootExpr instanceof ListNode)
			return runList((ListNode) rootExpr);
		else
			errorLog("run Expr error");
		return null;
	}

	private Node runList(ListNode list) {
		if (list.equals(ListNode.EMPTYLIST))
			return list;
		if (list.car() instanceof FunctionNode) {
			return runFunction((FunctionNode) list.car(), (ListNode) stripList(list.cdr()));
		}
		if (list.car() instanceof BinaryOpNode) {
			return runBinary(list);
		}
		return list;
	}

	private Node runFunction(FunctionNode operator, ListNode operand) {
		switch (operator.funcType) {
		// CAR, CDR, CONS� ���� ���� ����
		case CAR:
			Node node_car = runExpr(operand); // ��带 Ȯ��
			if (operand.car() instanceof QuoteNode)
				node_car = runQuote(operand); // ��ȣ�� ����
			ListNode result_car = (ListNode) node_car;
			return result_car.car(); // car ��ȯ

		case CDR:
			Node node_cdr = runExpr(operand); // ��带 Ȯ��
			if (node_cdr instanceof ListNode && ((ListNode) node_cdr).car() instanceof QuoteNode) // ListNodeȮ��
				node_cdr = runQuote((ListNode) node_cdr);

			node_cdr = ((ListNode) node_cdr).cdr();
			return ListNode.cons(new QuoteNode(node_cdr), ListNode.EMPTYLIST); // �� ó�� ���Ҹ� ������ ������ list��ȯ

		case CONS:
			Node cons_head = runExpr(operand.car()); // ����
			Node cons_tail = runExpr(operand.cdr().car()); // ����Ʈ

			if (cons_head instanceof ListNode && ((ListNode) cons_head).car() instanceof QuoteNode) // head ListNodeȮ��
				cons_head = runQuote((ListNode) cons_head);
			if (cons_tail instanceof ListNode && ((ListNode) cons_tail).car() instanceof QuoteNode) // tail ListNodeȮ��
				cons_tail = runQuote((ListNode) cons_tail);

			return ListNode.cons(new QuoteNode((ListNode.cons(cons_head, (ListNode) cons_tail))), ListNode.EMPTYLIST); // ���ҿ�
																														// ����Ʈ��
																														// ����

		case NULL_Q:
			Node node_null = runExpr(operand); // ��带 Ȯ��
			if (node_null instanceof ListNode && ((ListNode) node_null).car() instanceof QuoteNode) // ListNodeȮ��
				node_null = runQuote((ListNode) node_null);

			if (node_null == ListNode.EMPTYLIST) // ����Ʈ�� null�̸� true
				return BooleanNode.TRUE_NODE;
			else // ����Ʈ�� null�� �ƴϸ� false
				return BooleanNode.FALSE_NODE;

		case ATOM_Q:
			Node node_atom = runExpr(operand); // ��带 Ȯ��
			if (node_atom instanceof ListNode && ((ListNode) node_atom).car() instanceof QuoteNode) // ListNodeȮ��
				node_atom = runQuote((ListNode) node_atom);

			if (node_atom instanceof ListNode && node_atom != ListNode.EMPTYLIST) // list�̸�
				return BooleanNode.FALSE_NODE; // false
			else if (node_atom instanceof ListNode && node_atom == ListNode.EMPTYLIST) // list�� �ƴϸ�
				return BooleanNode.TRUE_NODE; // true
			else // null list�̸�
				return BooleanNode.TRUE_NODE; // true

		case EQ_Q:
			Node eq_head = runExpr(operand.car()); // ù ��° ���
			Node eq_tail = runExpr(operand.cdr().car()); // �� ��° ���

			if (eq_head instanceof ListNode && ((ListNode) eq_head).car() instanceof QuoteNode) // ù ��° ��� ListNodeȮ��
				eq_head = runQuote((ListNode) eq_head);
			if (eq_tail instanceof ListNode && ((ListNode) eq_tail).car() instanceof QuoteNode) // �� ��° ��� ListNodeȮ��
				eq_tail = runQuote((ListNode) eq_tail);

			if (eq_head.toString().equals(eq_tail.toString())) // �� ��� ��
				return BooleanNode.TRUE_NODE;
			else
				return BooleanNode.FALSE_NODE;

		case NOT:
			Node node_not = runExpr(operand);
			if (node_not == BooleanNode.TRUE_NODE)
				return BooleanNode.FALSE_NODE;
			return BooleanNode.TRUE_NODE;

		case COND:
			Node node_cond = operand.car(); // ��带 Ȯ��
			if (operand == ListNode.EMPTYLIST)
				return null;
			else if (runExpr(((ListNode) node_cond).car()) == BooleanNode.TRUE_NODE)
				return ((ListNode) node_cond).cdr().car();
			else
				return runFunction(operator, operand.cdr()); // ����Լ�

		default:
			break;
		}
		return null;
	}

	private Node stripList(ListNode node) {
		if (node.car() instanceof ListNode && node.cdr() == ListNode.EMPTYLIST) {
			Node listNode = node.car();
			return listNode;
		} else {
			return node;
		}
	}

	private Node runBinary(ListNode list) {
		BinaryOpNode operator = (BinaryOpNode) list.car();
		Node node = runExpr(list.cdr().car()); // ù ��° ����
		Node node2 = runExpr(list.cdr().cdr().car()); // �� ��° ����

		// ������������ �ʿ��� ���� �� �Լ� �۾� ����
		switch (operator.binType) {
		// +,-,/ � ���� ���̳ʸ� ���� ���� ����
		case PLUS: // ù ��° ���� + �� ��° ����
			return new IntNode(Integer.toString(((IntNode) node).getValue() + ((IntNode) node2).getValue()));
		case MINUS: // ù ��° ���� - �� ��° ����
			return new IntNode(Integer.toString(((IntNode) node).getValue() - ((IntNode) node2).getValue()));
		case TIMES: // ù ��° ���� * �� ��° ����
			return new IntNode(Integer.toString(((IntNode) node).getValue() * ((IntNode) node2).getValue()));
		case DIV: // ù ��° ���� / �� ��° ����
			return new IntNode(Integer.toString(((IntNode) node).getValue() / ((IntNode) node2).getValue()));
		case LT: // ù ��° ���� < �� ��° ����, ���̸� true, �����̸� false
			return (((IntNode) node).getValue() < ((IntNode) node2).getValue()) ? BooleanNode.TRUE_NODE
					: BooleanNode.FALSE_NODE;
		case GT: // ù ��° ���� > �� ��° ����, ���̸� true, �����̸� false
			return (((IntNode) node).getValue() > ((IntNode) node2).getValue()) ? BooleanNode.TRUE_NODE
					: BooleanNode.FALSE_NODE;
		case EQ: // ù ��° ���� = �� ��° ����, ���̸� true, �����̸� false
			return (((IntNode) node).getValue() == ((IntNode) node2).getValue()) ? BooleanNode.TRUE_NODE
					: BooleanNode.FALSE_NODE;
		default:
			break;
		}
		return null;
	}

	private Node runQuote(ListNode node) {
		return ((QuoteNode) node.car()).nodeInside();
	}
}
