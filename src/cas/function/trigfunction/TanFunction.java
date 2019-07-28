package math.cas.function.trigfunction;

import java.util.Map;

import math.cas.CAS;
import math.cas.Entity;
import math.cas.Variable;
import math.cas.function.basicfunction.ExpFunction;

public class TanFunction extends TrigonometricFunction {

	public TanFunction(CAS cas, Entity... parameters) {
		super(cas, parameters);
	}

	@Override
	public double evaluate(Map<Variable, Double> variableValues) {
		return Math.tan(a.evaluate(variableValues));
	}

	@Override
	public Entity derivativeWithRespectToArgument() {
		return new ExpFunction(cas, new SecFunction(cas, a), cas.TWO);
	}

}
