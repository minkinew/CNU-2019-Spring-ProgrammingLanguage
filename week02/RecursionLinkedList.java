import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class RecursionLinkedList {
	private Node head;
	private static char UNDEF = Character.MIN_VALUE;

	/** ���Ӱ� ������ ��带 ����Ʈ�� ó������ ���� */
	private void linkFirst(char element) {
		head = new Node(element, head);
	}

	/**
	 * ���� (1) �־��� Node x�� ���������� ����� Node�� �������� ���Ӱ� ������ ��带 ����
	 * 
	 * @param element //������
	 * @param x       //���
	 */
	private void linkLast(char element, Node x) { // ä���� ���, recursion ���
		if (x.next == null) // ������ ����̸�
			x.next = new Node(element, null); // next�� null�� ���ο� ��带 ����
		else // ������ ��尡 �ƴϸ�
			linkLast(element, x.next); // ������ ������ �Ѿ
	}

	/**
	 * ���� Node�� ���� Node�� ���Ӱ� ������ ��带 ����
	 * 
	 * @param element //����
	 * @param pred    //�������
	 */
	private void linkNext(char element, Node pred) {
		Node next = pred.next;
		pred.next = new Node(element, next);
	}

	/**
	 * ����Ʈ�� ù��° ���� ����(����)
	 * 
	 * @return //ù��° ������ ������
	 */
	private char unlinkFirst() {
		Node x = head;
		char element = x.item;
		head = head.next;
		x.item = UNDEF;
		x.next = null;
		return element;
	}

	/**
	 * ����Node�� ���� Node���� ����(����)
	 * 
	 * @param pred //�������
	 * @return //��������� ������
	 */
	private char unlinkNext(Node pred) {
		Node x = pred.next;
		Node next = x.next;
		char element = x.item;
		x.item = UNDEF;
		x.next = null;
		pred.next = next;
		return element;
	}

	/** ���� (2) x��忡�� index��ŭ ������ Node ��ȯ */
	private Node node(int index, Node x) { // ä���� ���, recursion ���
		if (index == 0) // index�� 0�̸� index��ŭ ������ ��带 �������Ƿ�
			return x; // �� ��� ��ȯ
		else // ã�� ��带 ���� ������ index�� 1�� ���ҽ�Ű�� ���� ���� �Ѿ
			return node(index - 1, x.next);
	}

	/** ���� (3) ���κ��� �������� ����Ʈ�� ��� ���� ��ȯ */
	private int length(Node x) {// ä���� ���, recursion ���
		if (x.next == null) // ������ ����϶��� ����� ������ 1�̹Ƿ�
			return 1; // 1�� ��ȯ
		else
			return 1 + length(x.next); // ���� ���� �̵��ϸ鼭 ��尹�� ī����
	}

	/** ���� (4) ���κ��� �����ϴ� ����Ʈ�� ���� ��ȯ */
	private String toString(Node x) { // ä���� ���, recursion ���
		if (x.next == null) // ������ ����̸�
			return "" + x.item; // ����� ������ ��ȯ
		else // ������ ��尡 �ƴϸ�
			return x.item + toString(x.next); // ������ ��ȯ�ϰ� ���� ���� �Ѿ
	}

	/**
	 * �߰� ���� (5) ���� ����� ���� ������ ����Ʈ�� �������� �Ųٷ� ���� ex) ��尡 [s]->[t]->[r]�� ��, reverse
	 * ���� �� [r]->[t]->[s]
	 * 
	 * @param x    //���� ���
	 * @param pred //�������� ���� ���
	 */
	private void reverse(Node x, Node pred) { // ä���� ���, recursion ���
		if (x.next == null) { // ������ ����̸�
			head = x; // �� ó����带 x�� �ٲٰ�
			x.next = pred; // ������带 �������� �ٲ���
		} else { // ������ ��尡 �ƴϸ�
			Node temp = x.next; // ��带 �ϳ� ������ ���� ���� ����
			x.next = pred; // ���� ���¿� ���� ����� ���� �־���
			reverse(temp, x); // ���
		}
	}

	/** ����Ʈ�� �Ųٷ� ���� */
	public void reverse() {
		reverse(head, null);
	}

	/**
	 * �߰� ���� (6) �� ����Ʈ�� ��ħ ( A + B ) ex) list1 =[l]->[o]->[v]->[e] , list2=[p]->[l]
	 * �� ��, list1.addAll(list2) ���� �� [l]->[o]->[v]->[e]-> [p]->[l]
	 * 
	 * @param x //list1�� ���
	 * @param y //list2�� head
	 */
	private void addAll(Node x, Node y) { // ä���� ���, recursion ���
		if (x.next == null) // x�� ������ ����̸�
			x.next = new Node(y.item, y.next); // x�� ������忡 ���ο� ��� y�� ����
		else // x�� ������ ��尡 �ƴϸ�
			addAll(x.next, y); // y�� ������ �ʰ� �������� �̵�
	}

	/** �� ����Ʈ�� ��ħ ( this + B ) */
	public void addAll(RecursionLinkedList list) {
		addAll(this.head, list.head);
	}

	/** ���Ҹ� ����Ʈ�� �������� �߰� */
	public boolean add(char element) {
		if (head == null)
			linkFirst(element);
		else
			linkLast(element, head);

		return true;
	}

	/**
	 * ���Ҹ� �־��� index ��ġ�� �߰�
	 * 
	 * @param index   //����Ʈ���� �߰��� ��ġ
	 * @param element //�߰��� ������
	 */
	public void add(int index, char element) {
		if (!(index >= 0 && index <= size()))
			throw new IndexOutOfBoundsException("" + index);
		if (index == 0)
			linkFirst(element);
		else
			linkNext(element, node(index - 1, head));
	}

	/** ����Ʈ���� index ��ġ�� ���� ��ȯ */
	public char get(int index) {
		if (!(index >= 0 && index < size()))
			throw new IndexOutOfBoundsException("" + index);
		return node(index, head).item;
	}

	/** ����Ʈ���� index ��ġ�� ���� ���� */
	public char remove(int index) {
		if (!(index >= 0 && index < size()))
			throw new IndexOutOfBoundsException("" + index);
		if (index == 0)
			return unlinkFirst();

		return unlinkNext(node(index - 1, head));
	}

	/** ����Ʈ�� ���� ���� ��ȯ */
	public int size() {
		return length(head);
	}

	@Override
	public String toString() {
		if (head == null)
			return "[]";
		return "[ " + toString(head) + "]";
	}

	/** ����Ʈ�� ���� �ڷᱸ�� */
	private static class Node {
		char item;
		Node next;

		Node(char element, Node next) {
			this.item = element;
			this.next = next;
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		RecursionLinkedList list = new RecursionLinkedList();
		FileReader fr;
		try {
			fr = new FileReader("hw01.txt");
			BufferedReader br = new BufferedReader(fr);
			String inputString = br.readLine();
			for (int i = 0; i < inputString.length(); i++)
				list.add(inputString.charAt(i));
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("===== Original Sentence =====");
		System.out.println(list.toString());
		System.out.println();
		System.out.println("===== Reverse Sentence =====");
		list.reverse();
		System.out.println(list.toString());
		System.out.println();
	}
}
