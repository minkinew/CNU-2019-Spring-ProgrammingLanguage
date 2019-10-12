package parser.parse;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Stream;

import parser.ast.*;
import parser.ast.BinaryOpNode.BinType;
import lexer.Scanner;
import lexer.ScannerMain;
import lexer.Token;
import lexer.TokenType;

public class CuteParser {
	private Iterator<Token> tokens;
	private static Node END_OF_LIST = new Node() {
	}; // ���� �߰��� �κ�

	public CuteParser(File file) {
		try {
			tokens = Scanner.scan(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private Token getNextToken() {
		if (!tokens.hasNext())
			return null;
		return tokens.next();
	}

	public Node parseExpr() {
		Token t = getNextToken();
		if (t == null) {
			System.out.println("No more token");
			return null;
		}
		TokenType tType = t.type();
		String tLexeme = t.lexme();

		switch (tType) {
		case ID:
			return new IdNode(tLexeme);
		case INT:
			if (tLexeme == null)
				System.out.println("???");
			return new IntNode(tLexeme);
		case DIV:
		case EQ:
		case MINUS:
		case GT:
		case PLUS:
		case TIMES:
		case LT:
			BinaryOpNode binNode = new BinaryOpNode(); // BinaryOpNodeŸ���� binNode ��ü ����
			binNode.setValue(tType); // BinaryOpNodeŸ���� setValue�� �� �޾ƿ�
			return binNode; // �޾ƿ� �� ����
		case ATOM_Q:
		case CAR:
		case CDR:
		case COND:
		case CONS:
		case DEFINE:
		case EQ_Q:
		case LAMBDA:
		case NOT:
		case NULL_Q:
			FunctionNode fNode = new FunctionNode(); // FunctionNodeŸ���� fNode ��ü ����
			fNode.setValue(tType); // FunctionNodeŸ���� setValue�� �� �޾ƿ�
			return fNode; // �޾ƿ� �� ����

		// ���� ������ BooleanNode Case
		case FALSE:
			return BooleanNode.FALSE_NODE;
		case TRUE:
			return BooleanNode.TRUE_NODE;
		// ���� ������ L_PAREN, R_PAREN Case
		case L_PAREN:
			return parseExprList();
		case R_PAREN:
			return END_OF_LIST;
		// ���� �߰��� APOSTROPHE, QUOTE
		case APOSTROPHE:
			QuoteNode quoteNode = new QuoteNode(parseExpr());
			ListNode listNode = ListNode.cons(quoteNode, ListNode.ENDLIST);
			return listNode;
		case QUOTE:
			return new QuoteNode(parseExpr());
		default:
			System.out.println("Parsing Error!");
			return null;
		}

	}

	private ListNode parseExprList() {
		Node head = parseExpr();
		if (head == null)
			return null;
		if (head == END_OF_LIST) // if next token is RPAREN
			return ListNode.ENDLIST;
		ListNode tail = parseExprList();
		if (tail == null)
			return null;
		return ListNode.cons(head, tail);
	}
}
