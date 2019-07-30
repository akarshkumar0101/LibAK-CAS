package math.cas.function.basicfunction;

import java.util.Map;

import array.Arrays;
import data.function.Function1D;
import data.tuple.Tuple2D;
import math.cas.CAS;
import math.cas.Constant;
import math.cas.Entity;
import math.cas.Variable;

public class AddFunction extends BasicFunction {

	public AddFunction(CAS cas, Entity... parameters) {
		super(cas, parameters);
	}

	@Override
	public double evaluate(Map<Variable, Double> variableValues) {
		return this.a.evaluate(variableValues) + this.b.evaluate(variableValues);
	}

	@Override
	public Entity partialWithRespectTo(Variable var) {
		return new AddFunction(this.cas, Arrays.performFunction(this.parameters, new Entity[this.parameters.length],
				(Function1D<Entity, Entity>) a -> a.partialWithRespectTo(var)));
	}

	@Override
	public Entity consolidate() {
		Entity ans = super.consolidate();

		if (ans instanceof AddFunction) {
			AddFunction func = (AddFunction) ans;

			Entity check0 = func.consolidateCheck0s();
			if (check0 != null)
				return check0;

			Entity checkLinComb = func.consolidateLinearCombination();
			if (checkLinComb != null)
				return checkLinComb;
		}

		return ans;
	}

	private Entity consolidateCheck0s() {
		if (this.a.equals(this.cas.ZERO))
			return this.b;
		else if (this.b.equals(this.cas.ZERO))
			return this.a;
		return null;
	}

	private Entity consolidateLinearCombination() {
		Tuple2D<Constant, Entity> scalea = this.splitScale(this.a), scaleb = this.splitScale(this.b);
		if (scalea.getB().equals(scaleb.getB())) {
			Constant newConst = new Constant(this.cas, scalea.getA().evaluate(null) + scaleb.getA().evaluate(null));
			return new MulFunction(this.cas, newConst, scalea.getB());
		}
		return null;
	}

	private Tuple2D<Constant, Entity> splitScale(Entity en) {
		if (en instanceof MulFunction) {
			MulFunction mulen = (MulFunction) en;
			if (mulen.a instanceof Constant)
				return new Tuple2D<>((Constant) mulen.a, mulen.b);
		}
		return new Tuple2D<>(this.cas.ONE, en);
	}

}
