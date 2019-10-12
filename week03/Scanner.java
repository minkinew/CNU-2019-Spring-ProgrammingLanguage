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
	private StringTokenizer st; // StringTokenizer Ŭ������ st���� ����
	private int pos;

	public Scanner(String source) {
		this.transM = new int[4][128];
		this.source = source == null ? "" : source;
		this.st = new StringTokenizer(this.source, " "); // ���ڸ� �������� ����
		initTM();
	}

	private void initTM() {
		for (int i = 0; i < 128; i++) // mDFA�� ��Ÿ���� TM�� 2�����迭�� �����ϰ�
			for (int j = 0; j < 4; j++) // �ʱⰪ�� -1(reject��Ȳ)���� ������
				transM[j][i] = -1;

		for (int k = 97; k <= 122; k++) { // �ƽ�Ű�ڵ� 97~122�� ���ĺ� �ҹ��� 'a' ~ 'z'
			transM[0][k] = 3; // transM[0]['a' ~ 'z']�� mDFA�� 3
			transM[3][k] = 3; // transM[3]['a' ~ 'z']�� mDFA�� 3
		}
		for (int k = 65; k <= 90; k++) { // �ƽ�Ű�ڵ� 65~90�� ���ĺ� �빮�� 'A' ~ 'Z'
			transM[0][k] = 3; // transM[0]['A' ~ 'Z']�� mDFA�� 3
			transM[3][k] = 3; // transM[0]['A' ~ 'Z']�� mDFA�� 3
		}
		for (int k = 48; k <= 57; k++) { // �ƽ�Ű�ڵ� 48~57�� ���� '0~9'
			transM[0][k] = 2; // transM[0]['0' ~ '9']�� mDFA�� 2
			transM[1][k] = 2; // transM[1]['0' ~ '9']�� mDFA�� 2
			transM[2][k] = 2; // transM[2]['0' ~ '9']�� mDFA�� 2
			transM[3][k] = 3; // transM[3]['0' ~ '9']�� mDFA�� 3
		}
		transM[0][45] = 1; // transM[0]['-']�� mDFA�� 1

		// The values of the other entries are all -1.
	}

	private Token nextToken() {
		int stateOld = 0, stateNew;

		// ��ū�� �� �ִ��� �˻�
		if (!st.hasMoreTokens())
			return null;

		// �� ���� ��ū�� ����
		String temp = st.nextToken();

		Token result = null;
		for (int i = 0; i < temp.length(); i++) {
			// ���ڿ��� ���ڸ� �ϳ��� ������ ������¿� TransM�� �̿��Ͽ� ���� ���¸� �Ǻ�
			// charAt�� �̿��� i�ε����� ��ġ�� ���ڸ� ��ȯ
			stateNew = transM[stateOld][temp.charAt(i)];

			if (stateNew == -1) { // -1�� ��
			// ���� �Էµ� ������ ���°� reject�̸� �����޼��� ��� �� return��
				System.out.println("Error Token! : " + temp);
				return result;
			}

			// ���� ���� ���¸� ���� ���·� ����
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
		// Token ����Ʈ��ȯ, nextToken()�̿�..
		// �Է����� ���� ��� token�� nextTOken���� ���� �ĺ� list�� ��ȯ
		List<Token> tokens = new ArrayList<>(); // token �迭����Ʈ ����
		Token temp;
		while (true) { // ��ū�� ���� ������ �ݺ�
			if (!st.hasMoreTokens()) //��ū�� �� ������ ����
				break;
			temp = nextToken(); // ���� ��ū���� �̵�
			tokens.add(temp); // token �迭����Ʈ�� �߰�
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
		br.close(); // ���� ����
	}

}
