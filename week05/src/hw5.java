import ast.*;
import compile.*;

public class hw5 {
	public static int max(Node node) {
		// 최대값을 리턴하도록 작성
		// value와 next 값 중 큰 값을 리턴
		Node n = node;
		if (n instanceof ListNode) // ListNode일 때
			if (((ListNode) n).value != null) // ListNode의 value가 존재할 경우
				return Integer.max(max(((ListNode) n).value), max(((ListNode) node).getNext())); // 재귀적으로 합 계산하여 값 반환

		if (n instanceof IntNode) // IntNode일 때
			if (n.getNext() != null) // IntNode의 value가 존재할 경우
				return Integer.max(((IntNode) node).value, max(((IntNode) n).getNext())); // 재귀적으로 최대값 반환
			else // IntNode이고 value가 존재하지 않을 경우
				return ((IntNode) n).value; // 값 반환
		return Integer.MIN_VALUE;
	}

	public static int sum(Node node) {
		// 노드 value의 총합을 반환
		// value와 next의 총 합을 리턴하면 됨
		Node n = node;
		if (n instanceof ListNode) // ListNode일 때
			if (((ListNode) n).value != null) // ListNode의 value가 존재할 경우
				return Integer.sum(sum(((ListNode) n).value), sum(((ListNode) node).getNext())); // 재귀적으로 합 계산하여 값 반환

		if (n instanceof IntNode) // IntNode일 때
			if (n.getNext() != null) // IntNode의 value가 존재할 경우
				return Integer.sum(((IntNode) n).value, sum(((IntNode) n).getNext())); // 재귀적으로 합 계산하여 값 반환
			else // IntNode이고 value가 존재하지 않을 경우
				return ((IntNode) n).value; // 값 반환

		return 0;
	}

	public static void main(String... args) {
		Node n = TreeFactory.createtTree("( ( 3 ( ( 10 ) ) 6 ) 4 1 ( ) -2 ( ) )");  
		System.out.println("( ( 3 ( ( 10 ) ) 6 ) 4 1 ( ) -2 ( ) )");
		System.out.println("최대값 : " + max(n));
		System.out.println("총합 : " + sum(n));
	}
}
