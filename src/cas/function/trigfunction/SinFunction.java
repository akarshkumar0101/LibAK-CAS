package math.cas.function.trigfunction;

import java.util.Map;

import math.cas.CAS;
import math.cas.Entity;
import math.cas.Variable;

public class SinFunction extends TrigonometricFunction {

	public SinFunction(CAS cas, Entity... parameters) {
		super(cas, parameters);
	}

	@Override
	public double evaluate(Map<Variable, Double> variableValues) {
		return Math.sin(parameters[0].evaluate(variableValues));
	}

	@Override
	public Entity derivativeWithRespectToArgument() {
		return new CosFunction(cas, a);
	}
}
