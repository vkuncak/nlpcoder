package statistics.pretrees;

import static statistics.parsers.Parser.*;
import java.util.List;
import statistics.Names;
import statistics.parsers.Parser;
import statistics.parsers.SingleResult;
import statistics.parsers.StringResult;
import types.StabileTypeFactory;
import types.Type;
import util.Pair;

public class CastExpr extends Expr {

	private Expr exp;

	public CastExpr(Type type, Expr exp) {
		super(type);
		this.exp = exp;
	}

	@Override
	public String toString() {
		return "("+type+")"+ exp;
	}

	@Override
	public String shortRep() {
		return Names.CastExpr+Names.LPar+type.getPrefix()+Names.RPar;
	}

	@Override
	protected String argReps() {
		return exp.shortRep();
	}

	@Override
	protected void longReps(List<Pair<String, String>> list) {
		list.addAll(exp.longReps());
	}

	public static SingleResult parseShort(String string, StabileTypeFactory tf) {
		String rest = removeLPar(removeCastExpr(string));
		StringResult result = parseStringTillRPar(rest);
		String typePrefix = result.getString();
		Type type = tf.createTypeByTypePrefix(typePrefix);
		rest = removeRPar(result.getRest());
		return new SingleResult(Parser.createCastExpr(type), rest);
	}
	
}
