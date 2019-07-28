package math.cas.function.trigfunction;

import java.util.Map;

import math.cas.CAS;
import math.cas.Entity;
import math.cas.Variable;
import math.cas.function.basicfunction.MulFunction;

public class CosFunction extends TrigonometricFunction {

	public CosFunction(CAS cas, Entity... parameters) {
		super(cas, parameters);
	}

	@Override
	public double evaluate(Map<Variable, Double> variableValues) {
		return Math.cos(a.evaluate(variableValues));
	}

	@Override
	public Entity derivativeWithRespectToArgument() {
		return new MulFunction(cas, cas.NEGATIVE_ONE, new SinFunction(cas, a));
	}

}
