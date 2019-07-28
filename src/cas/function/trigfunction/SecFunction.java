package math.cas.function.trigfunction;

import java.util.Map;

import math.cas.CAS;
import math.cas.Entity;
import math.cas.Variable;
import math.cas.function.basicfunction.MulFunction;

public class SecFunction extends TrigonometricFunction {

	public SecFunction(CAS cas, Entity... parameters) {
		super(cas, parameters);
	}

	@Override
	public double evaluate(Map<Variable, Double> variableValues) {
		return 1 / Math.cos(parameters[0].evaluate(variableValues));
	}

	@Override
	public Entity derivativeWithRespectToArgument() {
		return new MulFunction(cas, new SecFunction(cas, a), new TanFunction(cas, a));
	}
}
