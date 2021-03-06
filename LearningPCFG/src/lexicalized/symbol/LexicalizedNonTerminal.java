package lexicalized.symbol;

import lexicalized.info.LexicalizedInfo;

import org.eclipse.jdt.core.dom.ASTNode;

import symbol.NonTerminal;

public class LexicalizedNonTerminal extends NonTerminal {

	private LexicalizedInfo info;
	
	public LexicalizedNonTerminal(ASTNode node, LexicalizedInfo info) {
		super(node);
		this.setInfo(info);
		// TODO Auto-generated constructor stub
	}

	
	public boolean isUserDef(){
		return getInfo().isUserDef();
	}
	
	protected String toStringNaive(){
		return "L"+super.toStringNaive()+this.getInfo();
	}


	public LexicalizedInfo getInfo() {
		return info;
	}


	public void setInfo(LexicalizedInfo info) {
		this.info = info;
	}	
}
