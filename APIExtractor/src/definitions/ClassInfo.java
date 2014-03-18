package definitions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.FieldOrMethod;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
//import org.apache.bcel.generic.Type;
import org.eclipse.jdt.core.Signature;

import selection.IWordExtractor;
import selection.types.Const;
import selection.types.Substitution;
import selection.types.Type;
import selection.types.TypeFactory;

public class ClassInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8473504638929013042L;	
	private static final Map<String, ClassInfo> classes = new HashMap<String, ClassInfo>();
	private static TypeFactory factory;
	
	private String name;
	private ClassInfo[] interfaces;
	private ClassInfo[] superClasses;
	private boolean isClass;
	private boolean isPublic;

	private Declaration[] methods;
	private Declaration[] fields;
	private String simpleName;
	private String packageName;
	private Declaration[] udecls;
	private String[] classTypeParams;
	private Type clazzType;

	public ClassInfo(){}

	public ClassInfo(JavaClass clazz, IWordExtractor extractor) {
		this.name = clazz.getClassName();
		this.packageName = clazz.getPackageName();
		this.simpleName = getShortName(this.name);
		classes.put(this.name, this);

		this.isClass = clazz.isClass();
		this.isPublic = clazz.isPublic();

		typeParametersAndInheritedTypes(clazz);

		this.methods = initMethods(clazz, extractor);
		this.fields = initFields(clazz, extractor);

		try {
			this.interfaces = makeInterfaces(clazz.getInterfaces(), extractor);
		} catch (Exception e) {
			System.out.println("*******************************************************************************************");
			this.interfaces = new ClassInfo[0];
		}

		try {

			this.superClasses = makeSuperClasses(clazz.getSuperClasses(), extractor);		
		} catch (Exception e) {
			System.out.println("*******************************************************************************************");
			this.superClasses = new ClassInfo[0];
		}
	}

	private void typeParametersAndInheritedTypes(JavaClass clazz) {
		Attribute[] attributes = clazz.getAttributes();
		String signature = null;
		for (Attribute attribute : attributes) {
			if (attribute instanceof org.apache.bcel.classfile.Signature){
				signature  = ((org.apache.bcel.classfile.Signature) attribute).getSignature();
				break;
			}
		}

		this.classTypeParams = typeParameters(signature);
		this.clazzType = getClazzType(this.classTypeParams, this.name);
		
		//inherietedTypes = getgetInheritedTypes(signature); 
	}

	private static Type getClazzType(String[] typeParameters, String name) {
		int length = typeParameters.length;
		Type[] typeParam = new Type[length];
		for (int i = 0; i < length; i++) {
			typeParam[i] = factory.createVariable(typeParameters[i]);
		}
		return factory.createPolymorphic(name, typeParam);
	}

	private static String[] typeParameters(String signature) {
		if (signature == null) return new String[0];

		String[] typeParameters = Signature.getTypeParameters(signature);
		int length = typeParameters.length;
		String[] vars = new String[length];
		for (int i = 0; i < length; i++) {
			vars[i] = Signature.getTypeVariable(typeParameters[i]);
			//String[] typeParameterBounds = Signature.getTypeParameterBounds(param);
		}
		return vars;
	}

	private static Type[] getInheritedTypes(String signature, Set<String> vars) {
		if (signature == null) return new Type[0];
		
		int firstIndex = firstIndexOfInheritance(signature);
		String inheiritanceList = signature.substring(firstIndex);		
		String[] params = Signature.getParameterTypes("("+inheiritanceList+")V");
		int length = params.length;
		Type[] types = new Type[length];
		for (int i = 0; i < length; i++) {
			types[i] = type(params[i], vars);
		}
		return types;
	}	

	private static int firstIndexOfInheritance(String signature) {
		if (signature.length() > 0){
			int level = signature.charAt(0) == '<' ? 1:0;
			if (level > 0){
				int i=1;
				for(; level > 0; i++){
					char curr = signature.charAt(i);
					if(curr == '<') level++;
					else if (curr == '>') {
						level--;
					}
				}
				return i;
			}
		}
		return 0;
	}	

	private String getShortName(String name) {
		return name.substring(name.lastIndexOf(".")+1);
	}

	private ClassInfo[] makeSuperClasses(JavaClass[] superClasses2, IWordExtractor extractor) {
		List<ClassInfo> list = new LinkedList<ClassInfo>();
		for (JavaClass superClass: superClasses2) {
			getClass(superClass, list, extractor);
		}
		return list.toArray(new ClassInfo[list.size()]);
	}

	private void getClass(JavaClass javaClass, List<ClassInfo> list, IWordExtractor extractor) {
		String className = javaClass.getClassName();
		if (className != null) {
			ClassInfo clazz;
			if (classes.containsKey(className)){
				clazz = classes.get(className);
			} else {
				clazz = new ClassInfo(javaClass, extractor);
				classes.put(className, clazz);
			}
			list.add(clazz);
		}
	}

	private ClassInfo[] makeInterfaces(JavaClass[] interfaces, IWordExtractor extractor) {
		List<ClassInfo> list = new LinkedList<ClassInfo>();
		for (JavaClass interfaceClass: interfaces) {
			getClass(interfaceClass, list, extractor);
		}
		return list.toArray(new ClassInfo[list.size()]);
	}

	public Declaration[] getDeclarations(){
		int length = this.methods.length + this.fields.length;
		Declaration[] decls = new Declaration[length];

		System.arraycopy(this.methods, 0, decls, 0, this.methods.length);
		System.arraycopy(this.fields, 0, decls, this.methods.length, this.fields.length);	

		return decls;
	}

	private ClassInfo[] getInheritedTypes(){
		int length = this.superClasses.length + this.interfaces.length;
		ClassInfo[] types = new ClassInfo[length];

		System.arraycopy(this.superClasses, 0, types, 0, this.superClasses.length);
		System.arraycopy(this.interfaces, 0, types, this.superClasses.length, this.interfaces.length);

		return types;
	}	

	public Declaration[] getUniqueDeclarations() {
		if(this.udecls == null){
			Declaration[] decls = getDeclarations();
			List<Declaration> list = new LinkedList<Declaration>();
			for (Declaration decl : decls) {
				if(!isOverriden(decl, getInheritedTypes())){
					list.add(decl);
				}
			}
			return this.udecls = list.toArray(new Declaration[list.size()]);
		} else return this.udecls;
	}

	public static boolean isOverriden(Declaration decl, ClassInfo[] classes) {
		for (ClassInfo clazz : classes) {
			if(clazz.isOverriden(decl)){
				return true;
			}
		}
		return false;
	}

	public boolean isOverriden(Declaration decl){
		if (decl.isMethod()) {
			return isOverridenMethod(decl);
		} else {
			return isOverridenField(decl);
		}
	}

	private boolean isOverridenField(Declaration decl) {
		for (Declaration field: fields) {
			if (decl.overrides(field)) return true;
		}
		return false;
	}

	private boolean isOverridenMethod(Declaration decl) {
		for (Declaration method: methods) {
			if (decl.overrides(method)) return true;
		}
		return false;
	}

	private Declaration[] initMethods(JavaClass clazz, IWordExtractor extractor) {
		Method[] methods = clazz.getMethods();

		List<Declaration> decls = new ArrayList<Declaration>();

		for(Method method: methods){
			if(method.isPublic()){
				Declaration decl = new Declaration();
				decl.setClazz(clazz.getClassName());

				String name = method.getName();
				if (name.equals("<init>")){
					String clazzName = clazz.getClassName();
					decl.setName(clazzName.substring(clazzName.lastIndexOf('.')+1, clazzName.length()));
					decl.setConstructor(true);
					decl.setMethod(true);					
				} else {
					decl.setName(method.getName());
					decl.setMethod(true);
				}
				
				decl.setStatic(method.isStatic());
				decl.setPublic(method.isPublic());		
				decl.setArgNum(method.getArgumentTypes().length);

				String signature = getSignature(method);
				String[] methodTypeParams = typeParameters(signature);
				List<Substitution> classVarSubs = getUniqueVarNames(classTypeParams);
				List<Substitution> methodVarSubs = getUniqueVarNames(methodTypeParams);
				
				decl.setReceiverType(clazzType.apply(classVarSubs, factory));
				
				Set<String> vars = formVariables(methodTypeParams, classTypeParams);
				
				decl.setRetType(returnType(signature, classVarSubs, methodVarSubs, vars));
				decl.setArgType(parameterTypes(signature, classVarSubs, methodVarSubs, vars));
				
				decl.setWords(extractor.getWords(decl));

				decls.add(decl);				
			}
		}

		return decls.toArray(new Declaration[decls.size()]);
	}

	private static Set<String> formVariables(String[] methodTypeParams, String[] clazzTypeParams) {
		Set<String> vars = new HashSet<String>();
		vars.addAll(Arrays.asList(methodTypeParams));
		vars.addAll(Arrays.asList(clazzTypeParams));
		return vars;
	}
	
	private static Type[] parameterTypes(String signature, List<Substitution> classVarSubs, List<Substitution> methodVarSubs, Set<String> vars) {
		Type[] parameterTypes = parameterTypes(signature, vars);
		int length = parameterTypes.length;
		Type[] types = new Type[length];
		for (int i = 0; i < length; i++) {
			Type paramType = parameterTypes[i];
			types[i] = paramType.apply(methodVarSubs, factory).apply(classVarSubs, factory);
		}
		return types;
	}

	//Gives the priority to methodVarSubs cause they might hide some classVars.
	private static Type returnType(String signature, List<Substitution> classVarSubs, List<Substitution> methodVarSubs, Set<String> vars) {
		return returnType(signature, vars).apply(methodVarSubs, factory).apply(classVarSubs, factory);
	}

	private static List<Substitution> getUniqueVarNames(String[] typeParameters) {
		List<Substitution> list = new LinkedList<Substitution>();
		for (String param : typeParameters) {
			list.add(factory.varToNewVar(param));
		}
		return list;
	}

	private Declaration[] initFields(JavaClass clazz, IWordExtractor extractor) {
		Field[] fields = clazz.getFields();

		List<Declaration> decls = new ArrayList<Declaration>();

		for(Field field: fields){
			if(field.isPublic()){
				Declaration decl = new Declaration();
				decl.setName(field.getName());
				decl.setField(true);
				decl.setStatic(field.isStatic());
				decl.setPublic(field.isPublic());
				
				String signature = getSignature(field);
				String[] methodTypeParams = typeParameters(signature);
				List<Substitution> classVarSubs = getUniqueVarNames(classTypeParams);
				List<Substitution> methodVarSubs = getUniqueVarNames(methodTypeParams);
				
				decl.setReceiverType(clazzType.apply(classVarSubs, factory));
				
				Set<String> vars = formVariables(methodTypeParams, classTypeParams);
				
				decl.setRetType(returnType(signature, classVarSubs, methodVarSubs, vars));
				
				decl.setWords(extractor.getWords(decl));
				decls.add(decl);
			}
		}
		return decls.toArray(new Declaration[decls.size()]);
	}

	private static String getSignature(FieldOrMethod decl) {
		String signature = null;
		for (Attribute attribute : decl.getAttributes()) {
			if (attribute instanceof org.apache.bcel.classfile.Signature){
				signature = ((org.apache.bcel.classfile.Signature) attribute).getSignature();
				break;
			}
		}
		
		if (signature == null){
			signature = decl.getSignature();
		}
		return signature;
	}

	private static Type[] parameterTypes(String signature, Set<String> vars) {
		String[] parameterTypes = Signature.getParameterTypes(signature);
		int length = parameterTypes.length;
		Type[] types = new Type[length];
		for (int i = 0; i < length; i++) {
			types[i] = type(parameterTypes[i], vars);
		}
		return types;
	}

	private static Type returnType(String signature, Set<String> vars) {
		String returnType = Signature.getReturnType(signature);
		return type(returnType, vars);
	}

	private static Type type(String type, Set<String> vars) {
		if (isArrayType(type)){
			int dimension = Signature.getArrayCount(type);
			String elementType = Signature.getElementType(type);
			return arrayType(elementType, dimension, vars);
		} else if (isPolymorphicType(type)) {
			String[] typeParams = Signature.getTypeArguments(type);
			String typeErasure = Signature.getTypeErasure(type);
			return polyType(typeErasure, typeParams, vars);
		} else {
			String dotSignature = dottedTransformation(type);
			if(vars.contains(dotSignature))
				return factory.createVariable(dotSignature);
			else
				return factory.createConst(dotSignature);
		}
	}

	protected static String dottedTransformation(String type) {
		return dottedName(Signature.toString(type));
	}

	private static String dottedName(String string) {
		return string.replace("/", ".");
	}

	private static Type polyType(String typeErasure, String[] typeParams, Set<String> vars) {
		String name = Signature.toString(typeErasure);
		return factory.createPolymorphic(dottedName(name), types(typeParams, vars));
	}

	private static Type[] types(String[] signatures, Set<String> vars) {
		int length = signatures.length;
		Type[] types = new Type[length];
		for (int i = 0; i < length; i++) {
			types[i] = type(signatures[i], vars);
		}
		return types;
	}

	private static Type arrayType(String elementType, int dimension, Set<String> vars) {
		if (dimension > 0){
			return factory.createPolymorphic("java.lang.Array", new Type[]{arrayType(elementType, dimension - 1, vars)});	
		} else {
			return type(elementType, vars);
		}
	}

	private static boolean isPolymorphicType(String type) {
		return Signature.getTypeArguments(type).length > 0;
	}

	private static boolean isArrayType(String type) {
		return Signature.getArrayCount(type) > 0;
	}	

	public Declaration[] getMethods() {
		return methods;
	}

	public void setMethods(Declaration[] methods) {
		this.methods = methods;
	}

	public Declaration[] getFields() {
		return fields;
	}	

	public static Map<String, ClassInfo> getClasses() {
		return classes;
	}

	private String interfacesToString(){
		String s="";
		for (ClassInfo clazz: superClasses) {
			s+=" "+clazz.getName();
		}
		return s;
	}

	private String superClassesToString(){
		String s="";
		for (ClassInfo clazz: interfaces) {
			s+=" "+clazz.getName();
		}
		return s;		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ClassInfo[] getInterfaces() {
		return interfaces;
	}

	public void setInterfaces(ClassInfo[] interfaces) {
		this.interfaces = interfaces;
	}

	public ClassInfo[] getSuperClasses() {
		return superClasses;
	}

	public void setSuperClasses(ClassInfo[] superClasses) {
		this.superClasses = superClasses;
	}

	public boolean isClass() {
		return isClass;
	}

	public void setClass(boolean isClass) {
		this.isClass = isClass;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public void setFields(Declaration[] fields) {
		this.fields = fields;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}	

	public String getSimpleName(){
		return simpleName;
	}

	@Override
	public String toString() {
		return "ClassInfo [name=" + name + 
				", superClasses=["+ superClassesToString() + "]"+
				", interfaces=["+interfacesToString()+"],"+
				"isClass=" + isClass+ 
				", isPublic=" + isPublic + 
				"\ndeclarations=\n"+ Arrays.toString(getDeclarations())+
				"]\n";
	}

	public static TypeFactory getFactory() {
		return factory;
	}

	public static void setFactory(TypeFactory factory) {
		ClassInfo.factory = factory;
	}

}
