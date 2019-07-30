package math.cas.function.basicfunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import math.cas.CAS;
import math.cas.Entity;
import math.cas.Function;

public abstract class BasicFunction extends Function {

	public static final HashMap<Class<? extends BasicFunction>, Character> TOKENS = new HashMap<>();

	public static final List<Class<? extends BasicFunction>> basicFunctionsOrderOfOperations = new ArrayList<>();

	static {
		basicFunctionsOrderOfOperations.add(ExpFunction.class);
		basicFunctionsOrderOfOperations.add(DivFunction.class);
		basicFunctionsOrderOfOperations.add(MulFunction.class);
		basicFunctionsOrderOfOperations.add(SubFunction.class);
		basicFunctionsOrderOfOperations.add(AddFunction.class);

		TOKENS.put(AddFunction.class, '+');
		TOKENS.put(SubFunction.class, '-');
		TOKENS.put(MulFunction.class, '*');
		TOKENS.put(DivFunction.class, '/');
		TOKENS.put(ExpFunction.class, '^');
	}

	public static void registerAllFunctions(CAS cas) {
		for (Class<? extends BasicFunction> c : TOKENS.keySet()) {
			cas.registerFunction(String.valueOf(BasicFunction.TOKENS.get(c)), c);
		}
	}

	protected final Entity a;
	protected final Entity b;

	protected BasicFunction(CAS cas, Entity... parameters) {
		super(cas, parameters);
		if (parameters[1].isConstant() && !parameters[0].isConstant()
				&& (this.getClass() == AddFunction.class || this.getClass() == MulFunction.class)) {
			a = parameters[1];
			b = parameters[0];
		} else {
			a = parameters[0];
			b = parameters[1];
		}

	}

	@Override
	public String toString() {
		return "(" + a + ")" + TOKENS.get(this.getClass()) + "(" + b + ")";
	}

}
