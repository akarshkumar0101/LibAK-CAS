package math.cas.function;

import math.cas.CAS;
import math.cas.Entity;
import math.cas.Function;
import math.cas.Variable;
import math.cas.function.basicfunction.MulFunction;

public abstract class OneArgumentFunction extends Function {

	protected final Entity a;

	protected OneArgumentFunction(CAS cas, Entity... parameters) {
		super(cas, parameters);
		a = parameters[0];
	}

	@Override
	public Entity partialWithRespectTo(Variable var) {
		return new MulFunction(cas, derivativeWithRespectToArgument(), a.partialWithRespectTo(var));
	}

	public abstract Entity derivativeWithRespectToArgument();

}
