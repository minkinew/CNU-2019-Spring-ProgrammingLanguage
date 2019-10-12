import java.util.Scanner;

public class Faery {
	public static void recursion(int a, int b, int c, int d, int n) { // a,c = ���� / b,d = �и� / n = Fn
		int numer = a + c; // ���� ���� ���ڸ� ���� ��
		int denom = b + d;// ���� ���� �и� ���� ��
		if (denom > n) // ���ؼ� ���� �и��� ���� Fn�� �ش�n�� ������ ũ�ٸ� ��ȯ
			return;

		//���� ���ҵ� �� �߰����� �������� �丮������ ���
		recursion(a, b, numer, denom, n); //�߰��� ���ʺκ��� ��ͷ� ��� 
		print(numer, denom); //�߰����̾��� �� ���
		recursion(numer, denom, c, d, n); //�߰��� ���ʺκ��� ��ͷ� ���
	}

	public static void print(int a, int b) { //�丮���� ���� ���ҵ��� ����ϴ� �޼ҵ�
		System.out.print(a + "/" + b + ", ");
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in); // Scanner��ü�� ����
		System.out.print("input : ");
		int input = sc.nextInt(); // ����ڰ� ������ �Է�
		
		while (input > 100 || input <= 0) { // 0���� 100�ʰ� ������ �Է����� �� �ٽ� �Է�
			System.out.println("�ٽ� �Է����ּ���.");
			System.out.print("input : ");
			input = sc.nextInt(); // ����ڰ� ������ �ٽ� �Է���
		}

		System.out.println("output :");
		for (int i = 1; i <= input; i++) { //����ڰ� �Է��� ���ڱ��� Fn�� ����ϴ� �ݺ���
			System.out.print("f" + i + ":[");
			print(0, 1); // 0/1���
			recursion(0, 1, 1, 1, i); //recursion�޼ҵ� ����
			System.out.print("1/1]"); // 1/1���
			System.out.println();
		}
		sc.close(); //��ĳ�� ��ü�� ����
	}
}