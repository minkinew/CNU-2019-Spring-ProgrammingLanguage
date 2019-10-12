package lexer;

public enum TokenType {
	// Digit
	INT,

	// Alpha[Alpha|Digit]
	ID,

	// Sharp 'T', 'F'
	TRUE, FALSE,

	// special character
	PLUS, MINUS, TIMES, DIV, LT, GT, EQ, APOSTROPHE, L_PAREN, R_PAREN, QUESTION,

	// keyword in Question
	DEFINE, LAMBDA, COND, QUOTE, NOT, CAR, CDR, CONS, ATOM_Q, NULL_Q, EQ_Q;

	static TokenType fromSpecialCharactor(char ch) {
		switch (ch) {
		case '+':
			return PLUS;

		// 나머지 Special Character에 대해 토큰을 반환하도록 작성
		case '-':
			return MINUS;
		case '*':
			return TIMES;
		case '/':
			return DIV;
		case '<':
			return LT;
		case '>':
			return GT;
		case '=':
			return EQ;
		case '\'':
			return APOSTROPHE;
		case '(':
			return L_PAREN;
		case ')':
			return R_PAREN;
		case '?':
			return QUESTION;

		default:
			throw new IllegalArgumentException("unregistered char: " + ch);
		}
	}
}
