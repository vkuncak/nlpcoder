package builders;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;

import scopes.NameScopes;
import scopes.ScopesKeyValue;
import symbol.Factory;
import symbol.Symbol;

public class EvalExpBuilder extends FalseBuilder {

	private Factory factory;
	private ScopesKeyValue<String, Symbol> locals;
	private NameScopes methods;
	private NameScopes fields;
	private NameScopes params;
	private Symbol symbol;
	
	public EvalExpBuilder(Factory factory, ScopesKeyValue<String, Symbol> locals, NameScopes methods, NameScopes fields, NameScopes params) {
		this.factory = factory;
		this.locals = locals;
		this.methods = methods;
		this.fields = fields;
		this.params = params;
	}

	public Symbol getSymbol(ASTNode node){
		symbol = null;
		node.accept(this);
		return symbol;
	}
	
	public boolean visit(ClassInstanceCreation node){
		String name = node.getType().toString();
		symbol = factory.createConstructor(name);
		return false;
	}
	
	public boolean visit(FieldAccess node) {
		String name = node.getName().getIdentifier();
		if(!isField(name)){
		  symbol = factory.createField(name);
		} else {
		  symbol = Factory.HOLE;
		}
		return false;
	}
	
	private boolean isField(String name) {
		return fields.contains(name);
	}

	public boolean visit(MethodInvocation node) {
		String name = node.getName().getIdentifier();
		if(!isMethod(name)){
		  symbol = factory.createMethod(name);
		} else {
			symbol = Factory.HOLE;
		}
		return false;
	}
	
	private boolean isMethod(String name) {
		return methods.contains(name);
	}

	public boolean visit(BooleanLiteral node) {
		symbol = factory.createBooleanLiteral(node.booleanValue());
		return false;
	}
	
	public boolean visit(CharacterLiteral node) {
		symbol = factory.createCharacterLiteral(node.charValue());
		return false;
	}	
	
	public boolean visit(NullLiteral node){
		symbol = Factory.NULL;
		return false;
	}

	public boolean visit(NumberLiteral node){
		symbol = factory.createNumberLiteral(node.getToken());
		return false;
	}
	
	public boolean visit(StringLiteral node) {
		symbol = factory.createStringLiteral(node.getEscapedValue());
		return false;
	}	
	
	public boolean visit(SimpleName node){
		String name = node.getIdentifier();

		if (!isParam(name)){
			if (isLocalVariable(name)){
				symbol = locals.get(name);
			} else {
				symbol = Factory.HOLE;
			}
		} else {
			symbol = Factory.HOLE;
		}

		return false;
	}

	private boolean isLocalVariable(String name) {
		return locals.contains(name);
	}

	private boolean isParam(String name) {
		return params.contains(name);
	}

	public boolean visit(QualifiedName node) {
		symbol = Factory.HOLE;
		return false;
	}
	
	public boolean visit(ConstructorInvocation node) {
		symbol = Factory.HOLE;
		return false;
	}

	public boolean visit(EmptyStatement node) {
		symbol = Factory.HOLE;
		return false;
	}	
	public boolean visit(ArrayAccess node) {
		symbol = Factory.HOLE;
		return false;
	}
	
	public boolean visit(ArrayCreation node) {
		symbol = Factory.HOLE;
		return false;
	}
	
	public boolean visit(ArrayInitializer node) {
		symbol = Factory.HOLE;
		return false;
	}	
	
	public boolean visit(SuperConstructorInvocation node) {
		symbol = factory.HOLE;
		return false;
	}	

	public boolean visit(Initializer node) {
		symbol = factory.HOLE;
		return false;
	}
}
