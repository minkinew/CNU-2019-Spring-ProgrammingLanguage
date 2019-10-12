package parser.ast;

import java.util.HashMap;
import java.util.Map;

import lexer.TokenType;

// 여기를 작성하게 해도 좋을 듯 18.4.10
public class FunctionNode extends Node{
	public enum FunctionType {
	DEFINE 	{ TokenType tokenType() {return TokenType.DEFINE;} }, 
	LAMBDA 	{ TokenType tokenType() {return TokenType.LAMBDA;} }, 
	COND	{ TokenType tokenType() {return TokenType.COND;} },  
	NOT 	{ TokenType tokenType() {return TokenType.NOT;} }, 
	CAR 	{ TokenType tokenType() {return TokenType.CAR;} },
	CDR 	{ TokenType tokenType() {return TokenType.CDR;} },
	CONS	{ TokenType tokenType() {return TokenType.CONS;} },
	EQ_Q 	{ TokenType tokenType() {return TokenType.EQ_Q;} },
	NULL_Q  { TokenType tokenType() {return TokenType.NULL_Q;} },
	ATOM_Q  { TokenType tokenType() {return TokenType.ATOM_Q;} };
	
	private static Map<TokenType, FunctionType> fromTokenType = new HashMap<TokenType, FunctionType>();
	
	static {
		for (FunctionType fType : FunctionType.values()){
			fromTokenType.put(fType.tokenType(), fType);
		}
	}
	
	static FunctionType getFunctionType(TokenType tType){ //FunctionType에 대한 get메소드
		return fromTokenType.get(tType);
	}
	
	abstract TokenType tokenType();
	
}
	public FunctionType value;
	
	@Override
	public String toString(){
		return value.name();
	}

	public void setValue(TokenType tType) { //FunctionType에 대한 set메소드
		FunctionType fType = FunctionType.getFunctionType(tType);
		value = fType;
	}
}
