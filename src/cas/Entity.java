package math.cas;

import java.util.Map;

import array.Arrays;
import math.cas.function.basicfunction.MulFunction;

//will be of type variable, constant, or function
public abstract class Entity {

	protected CAS cas;

	protected Variable[] variables = new Variable[] {};

	protected Entity(CAS cas, Entity... dependancies) {
		this.cas = cas;
		for (Entity dep : dependancies) {
			for (Variable var : dep.variables) {
				if (!Arrays.contains(variables, var)) {
					variables = Arrays.append(variables, var);
				}
			}
		}
	}

	public Variable[] variableDependancies() {
		return variables;
	}

	public Entity scale(Constant constant) {
		return new MulFunction(cas, constant, this);
	}

	public abstract double evaluate(Map<Variable, Double> variableValues);

	public abstract Entity partialWithRespectTo(Variable var);

	public boolean isConstant() {
		return variables.length == 0;
	}

	public abstract Entity consolidate();

}
