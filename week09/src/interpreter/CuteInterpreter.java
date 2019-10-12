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
		Scanner sc = new Scanner(System.in); // Scanner 객체 생성

		while (true) {
			System.out.print("> ");
			String s = sc.nextLine(); // 문자열 입력
			File file = new File("as09.txt"); // 파일 객체 생성
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
		// CAR, CDR, CONS등에 대한 동작 구현
		case CAR:
			Node node_car = runExpr(operand); // 노드를 확인
			if (operand.car() instanceof QuoteNode)
				node_car = runQuote(operand); // 괄호를 제거
			ListNode result_car = (ListNode) node_car;
			return result_car.car(); // car 반환

		case CDR:
			Node node_cdr = runExpr(operand); // 노드를 확인
			if (node_cdr instanceof ListNode && ((ListNode) node_cdr).car() instanceof QuoteNode) // ListNode확인
				node_cdr = runQuote((ListNode) node_cdr);

			node_cdr = ((ListNode) node_cdr).cdr();
			return ListNode.cons(new QuoteNode(node_cdr), ListNode.EMPTYLIST); // 맨 처음 원소를 제외한 나머지 list반환

		case CONS:
			Node cons_head = runExpr(operand.car()); // 원소
			Node cons_tail = runExpr(operand.cdr().car()); // 리스트

			if (cons_head instanceof ListNode && ((ListNode) cons_head).car() instanceof QuoteNode) // head ListNode확인
				cons_head = runQuote((ListNode) cons_head);
			if (cons_tail instanceof ListNode && ((ListNode) cons_tail).car() instanceof QuoteNode) // tail ListNode확인
				cons_tail = runQuote((ListNode) cons_tail);

			return ListNode.cons(new QuoteNode((ListNode.cons(cons_head, (ListNode) cons_tail))), ListNode.EMPTYLIST); // 원소와
																														// 리스트를
																														// 붙임

		case NULL_Q:
			Node node_null = runExpr(operand); // 노드를 확인
			if (node_null instanceof ListNode && ((ListNode) node_null).car() instanceof QuoteNode) // ListNode확인
				node_null = runQuote((ListNode) node_null);

			if (node_null == ListNode.EMPTYLIST) // 리스트가 null이면 true
				return BooleanNode.TRUE_NODE;
			else // 리스트가 null이 아니면 false
				return BooleanNode.FALSE_NODE;

		case ATOM_Q:
			Node node_atom = runExpr(operand); // 노드를 확인
			if (node_atom instanceof ListNode && ((ListNode) node_atom).car() instanceof QuoteNode) // ListNode확인
				node_atom = runQuote((ListNode) node_atom);

			if (node_atom instanceof ListNode && node_atom != ListNode.EMPTYLIST) // list이면
				return BooleanNode.FALSE_NODE; // false
			else if (node_atom instanceof ListNode && node_atom == ListNode.EMPTYLIST) // list가 아니면
				return BooleanNode.TRUE_NODE; // true
			else // null list이면
				return BooleanNode.TRUE_NODE; // true

		case EQ_Q:
			Node eq_head = runExpr(operand.car()); // 첫 번째 노드
			Node eq_tail = runExpr(operand.cdr().car()); // 두 번째 노드

			if (eq_head instanceof ListNode && ((ListNode) eq_head).car() instanceof QuoteNode) // 첫 번째 노드 ListNode확인
				eq_head = runQuote((ListNode) eq_head);
			if (eq_tail instanceof ListNode && ((ListNode) eq_tail).car() instanceof QuoteNode) // 두 번째 노드 ListNode확인
				eq_tail = runQuote((ListNode) eq_tail);

			if (eq_head.toString().equals(eq_tail.toString())) // 두 노드 비교
				return BooleanNode.TRUE_NODE;
			else
				return BooleanNode.FALSE_NODE;

		case NOT:
			Node node_not = runExpr(operand);
			if (node_not == BooleanNode.TRUE_NODE)
				return BooleanNode.FALSE_NODE;
			return BooleanNode.TRUE_NODE;

		case COND:
			Node node_cond = operand.car(); // 노드를 확인
			if (operand == ListNode.EMPTYLIST)
				return null;
			else if (runExpr(((ListNode) node_cond).car()) == BooleanNode.TRUE_NODE)
				return ((ListNode) node_cond).cdr().car();
			else
				return runFunction(operator, operand.cdr()); // 재귀함수

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
		Node node = runExpr(list.cdr().car()); // 첫 번째 원소
		Node node2 = runExpr(list.cdr().cdr().car()); // 두 번째 원소

		// 구현과정에서 필요한 변수 및 함수 작업 가능
		switch (operator.binType) {
		// +,-,/ 등에 대한 바이너리 연산 동작 구현
		case PLUS: // 첫 번째 원소 + 두 번째 원소
			return new IntNode(Integer.toString(((IntNode) node).getValue() + ((IntNode) node2).getValue()));
		case MINUS: // 첫 번째 원소 - 두 번째 원소
			return new IntNode(Integer.toString(((IntNode) node).getValue() - ((IntNode) node2).getValue()));
		case TIMES: // 첫 번째 원소 * 두 번째 원소
			return new IntNode(Integer.toString(((IntNode) node).getValue() * ((IntNode) node2).getValue()));
		case DIV: // 첫 번째 원소 / 두 번째 원소
			return new IntNode(Integer.toString(((IntNode) node).getValue() / ((IntNode) node2).getValue()));
		case LT: // 첫 번째 원소 < 두 번째 원소, 참이면 true, 거짓이면 false
			return (((IntNode) node).getValue() < ((IntNode) node2).getValue()) ? BooleanNode.TRUE_NODE
					: BooleanNode.FALSE_NODE;
		case GT: // 첫 번째 원소 > 두 번째 원소, 참이면 true, 거짓이면 false
			return (((IntNode) node).getValue() > ((IntNode) node2).getValue()) ? BooleanNode.TRUE_NODE
					: BooleanNode.FALSE_NODE;
		case EQ: // 첫 번째 원소 = 두 번째 원소, 참이면 true, 거짓이면 false
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
