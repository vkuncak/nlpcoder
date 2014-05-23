package statistics.posttrees;

import java.util.List;

import statistics.Names;
import synthesis.handlers.Handler;
import synthesis.handlers.HandlerFactory;
import types.Type;

public class NumberLiteral extends Expr {

	private String value;
	private Type type;

	public NumberLiteral(Type type) {
		this.type = type;
	}
	
	@Override
	public void addArgs(List<Expr> args) {
		this.value = args.get(0).getString();		
	}

	@Override
	protected String shortReadableRep() {
		return Names.NumberLiteral;
	}
	
	@Override
	protected String shortRep() {
		return Names.NumberLiteral;
	}	
	
	@Override
	public String getPrefix() {
		return Names.NumberLiteral;
	}
	
	@Override
	public boolean isLiteral() {
		return true;
	}

	@Override
	public List<Type> getArgTypes() {
		return Type.EMPTY_TYPE_LIST;
	}

	@Override
	public Type getReturnType() {
		return type;
	}

	@Override
	public List<Expr> getArgs() {
		return Expr.EMPTY_EXPR_LIST;
	}

	@Override
	public synthesis.trees.Expr createRep(List<Integer> ids) {
		return new synthesis.trees.NumberLiteral(type);
	}

	@Override
	public Handler getHandler(HandlerFactory hf) {
		return hf.getNumberLiteralHandler();
	}
}
