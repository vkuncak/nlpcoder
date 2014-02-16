package definitions;

import java.io.Serializable;

public class Declaration implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private int argNum;
	private boolean isStatic;
	
	private String retType;
	private String[] argType;
	private String[] typeParams;
	
	private boolean method;
	private boolean constructor;
	private boolean field;

	private String clazz;
	
	public Declaration(){}	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getArgNum() {
		return argNum;
	}

	public void setArgNum(int argNum) {
		this.argNum = argNum;
	}

	public boolean isStatic() {
		return isStatic;
	}

	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

	public String getRetType() {
		return retType;
	}

	public void setRetType(String retType) {
		this.retType = retType;
	}

	public String[] getArgType() {
		return argType;
	}

	public void setArgType(String[] argType) {
		this.argType = argType;
	}

	public String[] getTypeParams() {
		return typeParams;
	}

	public void setTypeParams(String[] typeParams) {
		this.typeParams = typeParams;
	}

	public boolean isMethod() {
		return method;
	}

	public void setMethod(boolean method) {
		this.method = method;
	}

	public boolean isConstructor() {
		return constructor;
	}

	public void setConstructor(boolean constructor) {
		this.constructor = constructor;
	}

	public boolean isField() {
		return field;
	}

	public void setField(boolean field) {
		this.field = field;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	@Override
	public String toString() {
		return "Declaration [name=" + name + ", clazz=" + clazz + ", isStatic="
				+ isStatic + ", argNum=" + argNum + ", method=" + method + "]\n";
	}
}