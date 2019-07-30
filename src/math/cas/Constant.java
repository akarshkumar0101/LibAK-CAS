package math.cas;

import java.util.Map;

public class Constant extends Entity {

	public static final String PI_STRING = "PI";
	public static final Constant PI = new Constant(null, Math.PI);

	public static final String E_STRING = "E";
	public static final Constant E = new Constant(null, Math.E);

	private double value;

	public Constant(CAS cas, double value) {
		super(cas);
		this.value = value;
	}

	@Override
	public double evaluate(Map<Variable, Double> variableValues) {
		return value;
	}

	@Override
	public Entity partialWithRespectTo(Variable var) {
		return cas.ZERO;
	}

	@Override
	public Constant consolidate() {
		return this;
	}

	@Override
	public boolean equals(Object another) {
		return another instanceof Constant && value == ((Constant) another).value;
	}

	@Override
	public String toString() {
		if (this == PI)
			return PI_STRING;
		else if (this == E)
			return E_STRING;
		return String.valueOf(value);
	}

	public static void registerAllConstants(CAS cas) {
		cas.registerConstant(PI_STRING, cas.PI);
		cas.registerConstant(E_STRING, cas.E);
	}

}
