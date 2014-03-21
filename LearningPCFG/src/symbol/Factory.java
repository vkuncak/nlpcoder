package symbol;

import java.util.Set;

import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;

import declarations.API;
import definitions.Declaration;

import symbol.temp.DeclarationSet;

public class Factory {
    private final Symbol NULL = new Null();
	private final Symbol HOLE = new Hole();
    private final Symbol TRUE;
    private final Symbol FALSE;
    private final Symbol StringLiteral;
    private final Symbol CharacterLiteral;
    private final Symbol NumberLiteral;
	
	public Factory(API api){
		this.TRUE = new Decl(api.getTrueBooleanLiteral());
		this.FALSE = new Decl(api.getFalseBooleanLiteral());
		this.StringLiteral = new Decl(api.getStringLiteral());
		this.CharacterLiteral = new Decl(api.getCharacterLiteral());
		this.NumberLiteral = new Decl(api.getNumberLiteral());
	}
	
    public Symbol createHole(){
    	return HOLE;
    }
    
	public Symbol createNull() {
		return NULL;
	}    

	public Decl createDecl(Declaration decl){
		return new Decl(decl);
	}

	public Decl createDecl(Declaration decl, Symbol receiver, Symbol[] arguments){
		return new Decl(decl, receiver, arguments);
	}
	
	public DeclarationSet createDeclarationSet(Set<Declaration> decls){
		return new DeclarationSet(decls);
	}

	public Symbol createBooleanLiteral(boolean value) {
		if(value) return TRUE;
		else return FALSE;
	}

	public Symbol createCharacterLiteral() {
		return CharacterLiteral;
	}

	public Symbol createNumberLiteral() {
		return NumberLiteral;
	}

	public Symbol createStringLiteral() {
		return StringLiteral;
	}
	
	public Symbol createVariable(String name){
		return new Variable(name);
	}

	public Symbol createInfixOperator(InfixExpression.Operator operator, Symbol[] operands) {
		return new InfixOperator(operator, operands);
	}

	public Symbol createPostfixOperator(PostfixExpression.Operator operator, Symbol operand) {
		return new PostfixOperator(operator, operand);
	}

	public Symbol createPrefixOperator(PrefixExpression.Operator operator, Symbol operand) {
		return new PrefixOperator(operator, operand);
	}
}
