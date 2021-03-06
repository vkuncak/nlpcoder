package config;
import symbol.SymbolFactory;



public class Config {
	public static final int NAIVE = 0;
	public static final int WITH_PARENT= 1;
	public static final int WITH_GRANDAD = 2;
	
	private static int type = NAIVE;
	
	private static SymbolFactory factory = new SymbolFactory();
	
	public static int getType() {
		return type;
	}

	public static void setType(int type) {
		Config.type = type;
	}

	public static SymbolFactory getFactory() {
		return factory;
	}

	public static void setFactory(SymbolFactory factory) {
		Config.factory = factory;
	}
	
}
