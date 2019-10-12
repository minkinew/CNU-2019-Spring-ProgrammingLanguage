import java.math.BigInteger;
import java.util.Scanner;

public class DoubleFactorial {
	public static BigInteger doublefactorial(int n) {
		BigInteger big = BigInteger.valueOf(n); // valueOf() �޼ҵ�� ��ü�� �����Ͽ� n�� ����
		int distinct = n % 2; // n�� ¦������ Ȧ������ �Ǻ��ϴ� ���� distinct
		if (n == 0) // 0!!�϶� 1���
			return big = BigInteger.ONE; // �̸� ���ǵ� ��� ��ü(1)

		switch (distinct) {
		case 0: // n�� ¦���� ��
			if (n == 2) // n�� 2�� �� 2�� ��ȯ
				return big;
			// �� �������ʹ� ����Լ��� ���� ��ȯ
			return big.multiply(doublefactorial(n - 2)); // n-2k = n(n-2)(n-4)...4*2
		case 1: // n�� Ȧ���� ��
			if (n == 1) // n�� 1�� �� 1�� ��ȯ
				return big;
			// �� �������ʹ� ����Լ��� ���� ��ȯ
			return big.multiply(doublefactorial(n - 2)); // 2k-1 = n(n-2)(n-4)...3*1
		default: // ����
			break;
		}
		return big;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in); // Scanner��ü�� ����
		System.out.print("input : ");
		int input = sc.nextInt(); // ����ڰ� ������ �Է���

		while (input > 100 || input < 0) { // 0�̸� 100�ʰ� ������ �Է����� �� �ٽ� �Է�
			System.out.println("�ٽ� �Է����ּ���.");
			System.out.print("input : ");
			input = sc.nextInt(); // ����ڰ� ������ �ٽ� �Է���
		}

		System.out.print("output :");
		System.out.println(doublefactorial(input)); // doublefactorial�޼ҵ� ���
		sc.close(); // Scanner��ü�� ����

	}

}
