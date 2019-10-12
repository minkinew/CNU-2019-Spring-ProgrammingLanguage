import ast.*;
import compile.*;

public class hw5 {
	public static int max(Node node) {
		// �ִ밪�� �����ϵ��� �ۼ�
		// value�� next �� �� ū ���� ����
		Node n = node;
		if (n instanceof ListNode) // ListNode�� ��
			if (((ListNode) n).value != null) // ListNode�� value�� ������ ���
				return Integer.max(max(((ListNode) n).value), max(((ListNode) node).getNext())); // ��������� �� ����Ͽ� �� ��ȯ

		if (n instanceof IntNode) // IntNode�� ��
			if (n.getNext() != null) // IntNode�� value�� ������ ���
				return Integer.max(((IntNode) node).value, max(((IntNode) n).getNext())); // ��������� �ִ밪 ��ȯ
			else // IntNode�̰� value�� �������� ���� ���
				return ((IntNode) n).value; // �� ��ȯ
		return Integer.MIN_VALUE;
	}

	public static int sum(Node node) {
		// ��� value�� ������ ��ȯ
		// value�� next�� �� ���� �����ϸ� ��
		Node n = node;
		if (n instanceof ListNode) // ListNode�� ��
			if (((ListNode) n).value != null) // ListNode�� value�� ������ ���
				return Integer.sum(sum(((ListNode) n).value), sum(((ListNode) node).getNext())); // ��������� �� ����Ͽ� �� ��ȯ

		if (n instanceof IntNode) // IntNode�� ��
			if (n.getNext() != null) // IntNode�� value�� ������ ���
				return Integer.sum(((IntNode) n).value, sum(((IntNode) n).getNext())); // ��������� �� ����Ͽ� �� ��ȯ
			else // IntNode�̰� value�� �������� ���� ���
				return ((IntNode) n).value; // �� ��ȯ

		return 0;
	}

	public static void main(String... args) {
		Node n = TreeFactory.createtTree("( ( 3 ( ( 10 ) ) 6 ) 4 1 ( ) -2 ( ) )");  
		System.out.println("( ( 3 ( ( 10 ) ) 6 ) 4 1 ( ) -2 ( ) )");
		System.out.println("�ִ밪 : " + max(n));
		System.out.println("���� : " + sum(n));
	}
}
