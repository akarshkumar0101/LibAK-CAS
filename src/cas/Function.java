package math.cas;

import array.Arrays;
import data.function.Function1D;

public abstract class Function extends Entity {

	protected final Entity[] parameters;

	// protected Function(Parameter parameter) {
	// this.parameter = parameter;
	// }

	public Function(CAS cas, Entity... parameters) {
		super(cas, parameters);
		this.parameters = parameters;
	}

	@Override
	public boolean equals(Object another) {

		if (this.getClass() != another.getClass())
			return false;
		Function func = (Function) another;
		if (this.parameters.length != func.parameters.length)
			return false;
		for (int i = 0; i < this.parameters.length; i++) {
			if (!this.parameters[i].equals(func.parameters[i]))
				return false;
		}
		return true;
	}

	/*
	 * Consolidates the parameters of the function and returns the same function
	 *
	 */
	@Override
	public Entity consolidate() {
		String funcString = this.cas.getFunctionString(this.getClass());

		Entity[] newparams = Arrays.performFunction(this.parameters, new Entity[this.parameters.length],
				new Function1D<Entity, Entity>() {
					@Override
					public Entity evaluate(Entity a) {
						return a.consolidate();
					}
				});

		Function newFunc = this.cas.createFunction(funcString, newparams);

		if (newFunc.isConstant())
			return new Constant(this.cas, newFunc.evaluate(null));
		else
			return newFunc;
	}

	@Override
	public String toString() {
		String str = "";
		str += this.cas.getFunctionString(this.getClass());
		str += "(";
		for (int i = 0; i < this.parameters.length; i++) {
			str += this.parameters[i];
			if (i != this.parameters.length - 1) {
				str += ", ";
			}
		}
		str += ")";
		return str;
	}
}
