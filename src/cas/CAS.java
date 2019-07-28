package math.cas;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import math.cas.function.basicfunction.BasicFunction;
import math.cas.function.trigfunction.TrigonometricFunction;

public class CAS {

	// alpha numeric strings
	private final HashMap<String, Class<? extends Function>> functions = new HashMap<>();
	private final HashMap<String, Constant> constants = new HashMap<>();
	private final HashMap<String, Variable> variables = new HashMap<>();

	{
		registerKnownFunctions();
		registerKnownConstants();
	}

	public CAS() {

	}

	public Expression parseExpression(String str) {
		return new Expression(this, str);
	}

	public void registerFunction(String funcName, Class<? extends Function> funcClass) {
		functions.put(funcName, funcClass);
	}

	public void registerConstant(String name, double num) {
		registerConstant(name, new Constant(this, num));
	}

	public void registerConstant(String name, Constant constant) {
		constants.put(name, constant);
	}

	public void registerVariable(String variableName) {
		variables.put(variableName, new Variable(this, variableName));
	}

	public boolean isKeyword(String name) {
		return isFunction(name) || isConstant(name) || isVariable(name);
	}

	public boolean isFunction(String name) {
		return functions.keySet().contains(name);
	}

	public boolean isConstant(String str) {
		if (constants.keySet().contains(str))
			return true;
		try {
			Double.parseDouble(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isVariable(String name) {
		return variables.keySet().contains(name);
	}

	public Function createFunction(String name, Entity... parameters) {
		try {
			Constructor<? extends Function> cons = functions.get(name).getConstructor(CAS.class, Entity[].class);
			Object param = parameters;
			Function function = cons.newInstance(this, param);
			return function;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Variable getVariable(String name) {
		return variables.get(name);
	}

	public Constant createConstant(String str) {
		if (constants.containsKey(str))
			return constants.get(str);
		try {
			return new Constant(this, Double.parseDouble(str));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getFunctionString(Class<? extends Function> funcClass) {
		for (String s : functions.keySet()) {
			if (funcClass.equals(functions.get(s)))
				return s;
		}
		return null;
	}

	public final Constant ZERO = new Constant(this, 0);
	public final Constant ONE = new Constant(this, 1);
	public final Constant NEGATIVE_ONE = new Constant(this, -1);
	public final Constant TWO = new Constant(this, 2);

	public final Constant PI = new Constant(this, Math.PI);
	public final Constant E = new Constant(this, Math.E);

	private void registerKnownFunctions() {
		BasicFunction.registerAllFunctions(this);
		TrigonometricFunction.registerAllFunctions(this);
	}

	private void registerKnownConstants() {
		Constant.registerAllConstants(this);
	}
}
