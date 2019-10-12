import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Scanner {
	public enum TokenType {
		ID(3), INT(2);

		private final int finalState;

		TokenType(int finalState) {
			this.finalState = finalState;
		}
	}

	public static class Token {
		public final TokenType type;
		public final String lexme;

		Token(TokenType type, String lexme) {
			this.type = type;
			this.lexme = lexme;
		}

		@Override
		public String toString() {
			return String.format("[%s: %s]", type.toString(), lexme);
		}
	}

	private int transM[][];
	private String source;
	private StringTokenizer st; // StringTokenizer 클래스로 st변수 생성
	private int pos;

	public Scanner(String source) {
		this.transM = new int[4][128];
		this.source = source == null ? "" : source;
		this.st = new StringTokenizer(this.source, " "); // 문자를 공백으로 구분
		initTM();
	}

	private void initTM() {
		for (int i = 0; i < 128; i++) // mDFA를 나타나는 TM을 2차원배열로 생성하고
			for (int j = 0; j < 4; j++) // 초기값을 -1(reject상황)으로 설정함
				transM[j][i] = -1;

		for (int k = 97; k <= 122; k++) { // 아스키코드 97~122는 알파벳 소문자 'a' ~ 'z'
			transM[0][k] = 3; // transM[0]['a' ~ 'z']는 mDFA가 3
			transM[3][k] = 3; // transM[3]['a' ~ 'z']는 mDFA가 3
		}
		for (int k = 65; k <= 90; k++) { // 아스키코드 65~90는 알파벳 대문자 'A' ~ 'Z'
			transM[0][k] = 3; // transM[0]['A' ~ 'Z']는 mDFA가 3
			transM[3][k] = 3; // transM[0]['A' ~ 'Z']는 mDFA가 3
		}
		for (int k = 48; k <= 57; k++) { // 아스키코드 48~57는 숫자 '0~9'
			transM[0][k] = 2; // transM[0]['0' ~ '9']는 mDFA가 2
			transM[1][k] = 2; // transM[1]['0' ~ '9']는 mDFA가 2
			transM[2][k] = 2; // transM[2]['0' ~ '9']는 mDFA가 2
			transM[3][k] = 3; // transM[3]['0' ~ '9']는 mDFA가 3
		}
		transM[0][45] = 1; // transM[0]['-']는 mDFA가 1

		// The values of the other entries are all -1.
	}

	private Token nextToken() {
		int stateOld = 0, stateNew;

		// 토큰이 더 있는지 검사
		if (!st.hasMoreTokens())
			return null;

		// 그 다음 토큰을 받음
		String temp = st.nextToken();

		Token result = null;
		for (int i = 0; i < temp.length(); i++) {
			// 문자열의 문자를 하나씩 가져와 현재상태와 TransM를 이용하여 다음 상태를 판별
			// charAt를 이용해 i인덱스의 위치의 문자를 반환
			stateNew = transM[stateOld][temp.charAt(i)];

			if (stateNew == -1) { // -1일 떄
			// 만약 입력된 문자의 상태가 reject이면 에러메세지 출력 후 return함
				System.out.println("Error Token! : " + temp);
				return result;
			}

			// 새로 얻은 상태를 현재 상태로 저장
			stateOld = stateNew;
		}
		for (TokenType t : TokenType.values()) {
			if (t.finalState == stateOld) {
				result = new Token(t, temp);
				break;
			}
		}
		return result;
	}

	public List<Token> tokenize() {
		// Token 리스트반환, nextToken()이용..
		// 입력으로 들어온 모든 token을 nextTOken으로 상태 식별 list로 변환
		List<Token> tokens = new ArrayList<>(); // token 배열리스트 생성
		Token temp;
		while (true) { // 토큰이 없을 때까지 반복
			if (!st.hasMoreTokens()) //토큰이 더 없으면 멈춤
				break;
			temp = nextToken(); // 다음 토큰으로 이동
			tokens.add(temp); // token 배열리스트에 추가
		}
		return tokens;
	}

	public static void main(String[] args) throws IOException {
		FileReader fr = new FileReader("c:/as03.txt");
		BufferedReader br = new BufferedReader(fr);
		String source = br.readLine();
		Scanner s = new Scanner(source);
		List<Token> tokens = s.tokenize();
		System.out.println(tokens);
		br.close(); // 버퍼 닫음
	}

}
