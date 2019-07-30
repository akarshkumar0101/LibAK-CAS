package math.cas.function.basicfunction;

import java.util.Map;

import math.cas.CAS;
import math.cas.Entity;
import math.cas.Variable;

public class ExpFunction extends BasicFunction {

	public ExpFunction(CAS cas, Entity... parameters) {
		super(cas, parameters);
	}

	@Override
	public double evaluate(Map<Variable, Double> variableValues) {
		return Math.pow(a.evaluate(variableValues), b.evaluate(variableValues));
	}

	@Override
	public Entity partialWithRespectTo(Variable var) {
		return null;
	}
}
