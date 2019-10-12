import java.util.Scanner;

public class Faery {
	public static void recursion(int a, int b, int c, int d, int n) { // a,c = 분자 / b,d = 분모 / n = Fn
		int numer = a + c; // 양쪽 항의 분자를 더한 값
		int denom = b + d;// 양쪽 항의 분모를 더한 값
		if (denom > n) // 더해서 나온 분모의 값이 Fn의 해당n의 값보다 크다면 반환
			return;

		//항의 원소들 중 중간항을 기준으로 페리수열을 계산
		recursion(a, b, numer, denom, n); //중간항 앞쪽부븐을 재귀로 계산 
		print(numer, denom); //중간항이었던 값 출력
		recursion(numer, denom, c, d, n); //중간항 뒤쪽부분을 재귀로 계산
	}

	public static void print(int a, int b) { //페리수열 항의 원소들을 출력하는 메소드
		System.out.print(a + "/" + b + ", ");
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in); // Scanner객체를 생성
		System.out.print("input : ");
		int input = sc.nextInt(); // 사용자가 정수를 입력
		
		while (input > 100 || input <= 0) { // 0이하 100초과 정수를 입력했을 때 다시 입력
			System.out.println("다시 입력해주세요.");
			System.out.print("input : ");
			input = sc.nextInt(); // 사용자가 정수를 다시 입력함
		}

		System.out.println("output :");
		for (int i = 1; i <= input; i++) { //사용자가 입력한 숫자까지 Fn을 출력하는 반복문
			System.out.print("f" + i + ":[");
			print(0, 1); // 0/1출력
			recursion(0, 1, 1, 1, i); //recursion메소드 실행
			System.out.print("1/1]"); // 1/1출력
			System.out.println();
		}
		sc.close(); //스캐너 객체를 닫음
	}
}