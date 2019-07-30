package math.cas.function.trigfunction;

import math.cas.CAS;
import math.cas.Entity;
import math.cas.function.OneArgumentFunction;

public abstract class TrigonometricFunction extends OneArgumentFunction {

	public static void registerAllFunctions(CAS cas) {
		cas.registerFunction("sin", SinFunction.class);
		cas.registerFunction("cos", CosFunction.class);
		cas.registerFunction("tan", TanFunction.class);
		cas.registerFunction("csc", CscFunction.class);
		cas.registerFunction("sec", SecFunction.class);
		cas.registerFunction("cot", CotFunction.class);
	}

	public TrigonometricFunction(CAS cas, Entity... parameters) {
		super(cas, parameters);
	}
}
