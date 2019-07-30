package math.cas;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//entity is a constant, variable, function, or expression
public class Expression {
	private final CAS cas;

	private final String originalString;
	private String processedString;

	private Entity root;

	public Expression(CAS cas, String str) {
		this.cas = cas;
		originalString = str;
		processedString = processString(str);

		char[] charr = processedString.toCharArray();
		List<Object> parts = new ArrayList<Object>(charr.length);
		for (char c : charr) {
			parts.add(c);
		}

		root = MathParser.parse(cas, parts)[0];

	}

	// private static Variable[] toVariableArray(CAS cas, String... varStrings) {
	// Variable[] vars = new Variable[varStrings.length];
	// for (int i = 0; i < vars.length; i++) {
	// vars[i] = new Variable(cas, varStrings[i]);
	// }
	// return vars;
	// }

	private static String processString(String str) {
		return str.replace(" ", "");
	}

	public CAS getCAS() {
		return cas;
	}

	public Entity getRoot() {
		return root;
	}

	public void consolidate() {
		root = root.consolidate();
	}

	public double evaluate(Map<Variable, Double> variableValues) {
		return root.evaluate(variableValues);
	}

	public String getOriginalString() {
		return originalString;
	}

	@Override
	public String toString() {
		return root.toString();
	}
}
