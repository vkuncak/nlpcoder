package rules;

import util.List;

import org.eclipse.jdt.core.dom.VariableDeclarationExpression;

import symbol.Symbol;
import symbol.Tokens;

public class VariableDeclarationExpressionRule extends Rule {

	private Symbol type;
	private List<Symbol> fragments;
	private Symbol commaTerminal;
	
	public VariableDeclarationExpressionRule(VariableDeclarationExpression node) {
		super(node);
		
		this.type = nonTerminal(node.getType());
		this.fragments = makeNonTerminalList(node.fragments());
		this.commaTerminal = terminal(Tokens.COMMA, node);
		// TODO Auto-generated constructor stub
	}

	private List<Symbol> toFragments(){
		return toInfixList(this.fragments, this.commaTerminal);
	}
	
	@Override
	protected void rhsAsList(List<Symbol> list) {
		list.f(this.type).f(toFragments());
	}

}
