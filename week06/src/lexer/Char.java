package lexer;

class Char {
	private final char value;
	private final CharacterType type;

	enum CharacterType {
		LETTER, DIGIT, SPECIAL_CHAR, WS, END_OF_STREAM,
	}
	
	static Char of(char ch) {
		return new Char(ch, getType(ch));
	}
	
	static Char end() {
		return new Char(Character.MIN_VALUE, CharacterType.END_OF_STREAM);
	}
	
	private Char(char ch, CharacterType type) {
		this.value = ch;
		this.type = type;
	}
	
	char value() {
		return this.value;
	}
	
	CharacterType type() {
		return this.type;
	}
	
	private static CharacterType getType(char ch) {
		int code = (int)ch;
		if ( Character.isLetter(ch) || ch == '?') { //letter가 되는 조건식을 알맞게 채우기
			return CharacterType.LETTER; //Alpha[Alpha|Digit], “eq?”, “null?”, “atom?“
		}
		
		if ( Character.isDigit(ch) ) {
			return CharacterType.DIGIT; //0~9
		}
		
		switch ( ch ) {
			case '-': case '+': case '*': case '/':
			case '(': case ')':
			case '<': case '=': case '>':
			case '#': case '\'':
				return CharacterType.SPECIAL_CHAR; //특수 기호
		}
		
		if ( Character.isWhitespace(ch) ) { 
			return CharacterType.WS; //공백
		}
		
		throw new IllegalArgumentException("input=" + ch);
	}
}
