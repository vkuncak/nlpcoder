package sequences.one.exprs;

import java.util.List;

import util.Pair;

public class CondExpr extends Expr {

	private Expr exp;
	private Expr thenExp;
	private Expr elseExp;

	public CondExpr(Expr exp, Expr thenExp, Expr elseExp) {
		super(thenExp.getType());

		this.exp = exp;
		this.thenExp = thenExp;
		this.elseExp = elseExp;
	}

	public Expr getExp() {
		return exp;
	}

	public void setExp(Expr exp) {
		this.exp = exp;
	}

	public Expr getThenExp() {
		return thenExp;
	}

	public void setThenExp(Expr thenExp) {
		this.thenExp = thenExp;
	}

	public Expr getElseExp() {
		return elseExp;
	}

	public void setElseExp(Expr elseExp) {
		this.elseExp = elseExp;
	}

	@Override
	public String toString() {
		return exp + " ? " + thenExp + " : "+ elseExp;
	}

	@Override
	public String shortRep() {
		return ExprConsts.CondExpr;
	}

	@Override
	protected String representation() {
		return shortReps(exp, thenExp, elseExp);
	}
	
	@Override
	protected void representations(List<Pair<String, String>> list) {
		list.addAll(exp.longReps());
		list.addAll(thenExp.longReps());
		list.addAll(elseExp.longReps());
	}	
}
