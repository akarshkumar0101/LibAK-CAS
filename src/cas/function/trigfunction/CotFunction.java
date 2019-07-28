package math.cas.function.trigfunction;

import java.util.Map;

import math.cas.CAS;
import math.cas.Entity;
import math.cas.Variable;
import math.cas.function.basicfunction.ExpFunction;
import math.cas.function.basicfunction.MulFunction;

public class CotFunction extends TrigonometricFunction {

	public CotFunction(CAS cas, Entity... parameters) {
		super(cas, parameters);
	}

	@Override
	public double evaluate(Map<Variable, Double> variableValues) {
		return 1 / Math.tan(parameters[0].evaluate(variableValues));
	}

	@Override
	public Entity derivativeWithRespectToArgument() {
		return new MulFunction(cas, cas.NEGATIVE_ONE, new ExpFunction(cas, new CscFunction(cas, a), cas.TWO));
	}
}
