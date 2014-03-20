package symbol;

import java.util.Set;

import selection.types.Const;
import selection.types.Type;
import selection.types.TypeFactory;
import definitions.Declaration;

public class StringLiteral extends Symbol {

	private String value;
	private Type retType;
	
	public StringLiteral(String value, TypeFactory factory) {
		this.value = value;
		this.retType = factory.createConst("java.lang.String");
	}

	@Override
	public String head() {
		return "String("+value+")";
	}

	@Override
	public String toString() {
		return value;
	}

	@Override
	public Type retType() {
		return this.retType;
	}

	@Override
	public boolean hasRetType() {
		return true;
	}

	@Override
	public boolean isVariable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<Declaration> getDecls() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasDecls() {
		// TODO Auto-generated method stub
		return false;
	}
}
