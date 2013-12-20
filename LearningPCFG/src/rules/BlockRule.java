package rules;

import util.List;

import org.eclipse.jdt.core.dom.Block;

import symbol.Symbol;
import symbol.SymbolFactory;
import symbol.Tokens;

public class BlockRule extends Rule {

	private List<Symbol> statements;
	private Symbol lcurlyTerminal;
	private Symbol rcurlyTerminal;

	public BlockRule(Block node) {
		super(node);
		
		this.statements = makeNonTerminalList(node.statements());
		this.lcurlyTerminal = terminal(Tokens.L_CURLY_BRACKET, node);
		this.rcurlyTerminal = terminal(Tokens.R_CURLY_BRACKET, node);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void rhsAsList(List<Symbol> list) {
		list.f(this.lcurlyTerminal).f(this.statements).f(this.rcurlyTerminal);
	}

}
