package statistics.posttrees;

import java.util.List;

import statistics.Names;

public class NumberLiteral extends Expr {

	private String value;

	@Override
	public void addArgs(List<Expr> args) {
		this.value = args.get(0).getString();		
	}

	@Override
	protected String shortRep() {
		return Names.NumberLiteral;
	}

	@Override
	protected String argsRep() {
		return value;
	}
	@Override
	public String getPrefix() {
		return Names.NumberLiteral;
	}
	
	@Override
	public boolean isLiteral() {
		return true;
	}
}
