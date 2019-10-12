import java.math.BigInteger;
import java.util.Scanner;

public class DoubleFactorial {
	public static BigInteger doublefactorial(int n) {
		BigInteger big = BigInteger.valueOf(n); // valueOf() 메소드로 객체를 생성하여 n을 받음
		int distinct = n % 2; // n이 짝수인지 홀수인지 판별하는 변수 distinct
		if (n == 0) // 0!!일때 1출력
			return big = BigInteger.ONE; // 미리 정의된 상수 객체(1)

		switch (distinct) {
		case 0: // n이 짝수일 때
			if (n == 2) // n이 2일 때 2를 반환
				return big;
			// 그 다음부터는 재귀함수로 값을 반환
			return big.multiply(doublefactorial(n - 2)); // n-2k = n(n-2)(n-4)...4*2
		case 1: // n이 홀수일 때
			if (n == 1) // n이 1일 때 1을 반환
				return big;
			// 그 다음부터는 재귀함수로 값을 반환
			return big.multiply(doublefactorial(n - 2)); // 2k-1 = n(n-2)(n-4)...3*1
		default: // 예외
			break;
		}
		return big;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in); // Scanner객체를 생성
		System.out.print("input : ");
		int input = sc.nextInt(); // 사용자가 정수를 입력함

		while (input > 100 || input < 0) { // 0미만 100초과 정수를 입력했을 때 다시 입력
			System.out.println("다시 입력해주세요.");
			System.out.print("input : ");
			input = sc.nextInt(); // 사용자가 정수를 다시 입력함
		}

		System.out.print("output :");
		System.out.println(doublefactorial(input)); // doublefactorial메소드 출력
		sc.close(); // Scanner객체를 닫음

	}

}
