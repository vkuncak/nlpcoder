package search.nlp.parser.declarations;

import java.util.LinkedList;
import java.util.List;

import search.nlp.parser.ComplexWordDecomposer;
import types.InitialTypeFactory;
import nlp.parser.Token;
import search.nlp.parser.declarations.IDeclarationParser;
import definitions.Declaration;

public class DeclarationParser implements IDeclarationParser {

	private ComplexWordDecomposer decomposer;
	private InitialTypeFactory tf;
	
	public DeclarationParser(ComplexWordDecomposer decomposer, InitialTypeFactory tf) {
		this.decomposer = decomposer;
		this.tf = tf;
	}
	
	@Override
	public Declaration parse(Declaration decl) {		
		simpleNameTokenization(decl);
		receiverTypeTokenization(decl);		
		remainderTypeTokenization(decl);
		clazzTokenization(decl);
		returnTypeTokenization(decl);
		
		return decl;
	}

	private void entireTokenization(Declaration decl) {
		List<Token> tokens = new LinkedList<Token>();		
		List<String> tokensAsStrings = decl.getTokensAsStrings(tf);
		for (String name : tokensAsStrings) {
			tokens.addAll(decomposer.decomposeString(name));
		}
		decl.setTokens(tokens.toArray(new Token[tokens.size()]));
	}
	
	private void remainderTypeTokenization(Declaration decl) {
		List<List<Token>> argTokens = new LinkedList<List<Token>>();		
		List<List<String>> args = decl.getArgsTextualForm(tf);
		
		for (List<String> arg : args) {
			List<Token> tokens = new LinkedList<Token>();
			for (String name : arg) {
				tokens.addAll(decomposer.decomposeString(name));
			}			
			argTokens.add(tokens);
		}
		

		decl.setArgTokens(argTokens);
	}	
	
	private void receiverTypeTokenization(Declaration decl) {
		List<Token> receiverTokens = new LinkedList<Token>();		
		List<String> receiver = decl.getReceiverTypeTextualForm(tf);
		for (String name : receiver) {
			receiverTokens.addAll(decomposer.decomposeString(name));
		}
		decl.setReceiverTokens(receiverTokens);
	}

	private void simpleNameTokenization(Declaration decl) {
		String simpleName = decl.getSimpleNameTextualForm();
		List<Token> simpleNameTokens = decomposer.decomposeSimpleNameString(simpleName);
		decl.setSimpleNameTokens(simpleNameTokens);
	}

	private void clazzTokenization(Declaration decl) {
		List<Token> clazzTokens = new LinkedList<Token>();	
		List<String> clazzName = decl.getClassTextualForm();
		for (String name : clazzName) {
			clazzTokens.addAll(decomposer.decomposeString(name));
		}
		decl.setClassTokens(clazzTokens);
	}
	
	private void returnTypeTokenization(Declaration decl) {
		List<Token> returnTypeTokens = new LinkedList<Token>();	
		List<String> returnTypeName = decl.getReturnTypeTextualForm(tf);
		for (String name : returnTypeName) {
			returnTypeTokens.addAll(decomposer.decomposeString(name));
		}
		decl.setReturnTypeTokens(returnTypeTokens);
	}	
}
