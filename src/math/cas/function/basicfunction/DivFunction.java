package math.cas.function.basicfunction;

import java.util.Map;

import math.cas.CAS;
import math.cas.Entity;
import math.cas.Variable;

public class DivFunction extends BasicFunction {

	public DivFunction(CAS cas, Entity... parameters) {
		super(cas, parameters);
	}

	@Override
	public double evaluate(Map<Variable, Double> variableValues) {
		return a.evaluate(variableValues) / b.evaluate(variableValues);
	}

	@Override
	public Entity partialWithRespectTo(Variable var) {
		Entity a = parameters[0], b = parameters[1];
		Entity da = a.partialWithRespectTo(var), db = b.partialWithRespectTo(var);

		Entity top = new SubFunction(cas, new MulFunction(cas, da, b), new MulFunction(cas, a, db));
		Entity bot = new ExpFunction(cas, b, cas.TWO);
		return new DivFunction(cas, top, bot);
	}

}
