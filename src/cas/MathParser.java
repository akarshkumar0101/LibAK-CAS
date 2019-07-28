package math.cas;

import java.util.ArrayList;
import java.util.List;

import array.Lists;
import math.cas.function.basicfunction.BasicFunction;

class MathParser {

	public static final char OPEN_PAR = '(', CLOSE_PAR = ')';
	public static final char SEPERATOR = ',';

	public static Entity[] parse(CAS cas, List<Object> parts) {

		recurseInternalEntities(cas, parts);

		if (parts.contains(SEPERATOR))
			return getParameters(cas, parts);

		recognizeFunctions(cas, parts);

		recognizeAndImplementConstants(cas, parts);
		recognizeAndImplementVariables(cas, parts);

		implementFunctions(cas, parts);

		implementBasicFunctions(cas, parts);

		if (parts.size() != 1)
			throw new RuntimeException("Could not parse");
		return new Entity[] { (Entity) parts.get(0) };
	}

	private static Entity[] getParameters(CAS cas, List<Object> parts) {
		int num = Lists.numberOfElement(parts, SEPERATOR) + 1;
		Entity[] params = new Entity[num];
		parts.add(SEPERATOR);
		for (int j = 0; j < num; j++) {
			int i = parts.indexOf(SEPERATOR);
			parts.remove(i);
			List<Object> paramParts = Lists.removeSubList(parts, 0, i, new ArrayList<>());
			params[j] = parse(cas, paramParts)[0];
		}
		return params;
	}

	private static void recurseInternalEntities(CAS cas, List<Object> parts) {
		for (int i = 0;; i++) {
			if (i >= parts.size()) {
				break;
			}
			if (parts.get(i).equals(OPEN_PAR)) {
				int endi = findEmbeddedMatch(parts, i, OPEN_PAR, CLOSE_PAR);
				parts.remove(i);
				endi--;
				parts.remove(endi);
				endi--;
				List<Object> parParts = Lists.removeSubList(parts, i, endi + 1, new ArrayList<Object>(endi + 1 - i));
				Entity[] internalEntity = parse(cas, parParts);
				if (internalEntity.length == 1) {
					parts.add(i, internalEntity[0]);
				} else {
					parts.add(i, internalEntity);
				}
			}
		}
	}

	private static void recognizeFunctions(CAS cas, List<Object> parts) {
		for (int i = 0;; i++) {
			if (i >= parts.size()) {
				break;
			}
			if (isAlphabeticCharacter(parts.get(i))) {
				for (int endIndex = parts.size(); endIndex > i; endIndex--) {
					String funcName = toAlphabeticString(parts, i, endIndex);
					if (funcName != null && cas.isFunction(funcName)) {
						// found function string
						Lists.removeSubList(parts, i, endIndex);
						parts.add(i, funcName);
					}
				}
			}
		}
	}

	private static void recognizeAndImplementConstants(CAS cas, List<Object> parts) {
		for (int i = 0;; i++) {
			if (i >= parts.size()) {
				break;
			}
			if (isAlphabeticCharacter(parts.get(i)) || isNumericCharacter(parts.get(i))) {
				for (int endIndex = parts.size(); endIndex > i; endIndex--) {
					String cnstStr = toAlphaNumericString(parts, i, endIndex);
					if (cnstStr != null && cas.isConstant(cnstStr)) {
						Constant cnst = cas.createConstant(cnstStr);
						// found function string
						Lists.removeSubList(parts, i, endIndex);
						parts.add(i, cnst);
					}

				}
			}
		}
	}

	private static void recognizeAndImplementVariables(CAS cas, List<Object> parts) {
		for (int i = 0;; i++) {
			if (i >= parts.size()) {
				break;
			}
			if (isAlphabeticCharacter(parts.get(i))) {
				for (int endIndex = parts.size(); endIndex > i; endIndex--) {
					String varStr = toAlphaNumericString(parts, i, endIndex);
					if (varStr != null) {

						if (cas.isVariable(varStr)) {
							// found function string
							Lists.removeSubList(parts, i, endIndex);
							parts.add(i, cas.getVariable(varStr));
						}
					}
				}
			}
		}
	}

	private static void implementFunctions(CAS cas, List<Object> parts) {
		for (int i = 0;; i++) {
			if (i >= parts.size()) {
				break;
			}
			if (parts.get(i) instanceof String && cas.isFunction((String) parts.get(i))) {
				Object param = parts.get(i + 1);
				Function func = null;
				if (param instanceof Entity[]) {
					func = cas.createFunction((String) parts.get(i), (Entity[]) param);
				} else {
					func = cas.createFunction((String) parts.get(i), (Entity) param);
				}

				parts.remove(i + 1);
				parts.remove(i);
				parts.add(i, func);
			}
		}
	}

	private static void implementBasicFunctions(CAS cas, List<Object> parts) {
		for (Class<? extends BasicFunction> funcClass : BasicFunction.basicFunctionsOrderOfOperations) {
			implementBasicFunction(cas, parts, BasicFunction.TOKENS.get(funcClass));
		}
	}

	private static void implementBasicFunction(CAS cas, List<Object> parts, Character token) {
		for (int i = 0;; i++) {
			if (i >= parts.size()) {
				break;
			}
			if (token.equals(parts.get(i))) {
				Entity e1 = (Entity) parts.get(i - 1), e2 = (Entity) parts.get(i + 1);
				Function basicFunc = cas.createFunction(String.valueOf(token), e1, e2);
				parts.remove(i + 1);
				parts.remove(i);
				parts.remove(i - 1);
				parts.add(i - 1, basicFunc);
				i--;
			}
		}
	}

	private static int findEmbeddedMatch(List<Object> parts, int openIndex, Object open, Object close) {
		int embed = 0;
		for (int i = openIndex; i < parts.size(); i++) {
			if (parts.get(i).equals(open)) {
				embed++;
			} else if (parts.get(i).equals(close)) {
				embed--;
			}
			if (embed == 0)
				return i;
		}
		return -1;
	}

	private static String toAlphabeticString(List<Object> parts, int beginIndex, int endIndex) {
		String str = "";
		for (int i = beginIndex; i < endIndex; i++) {
			if (parts.get(i) instanceof Character && isAlphabeticCharacter(parts.get(i))) {
				str += (char) parts.get(i);
			} else
				return null;
		}
		return str;
	}

	// private static String toNumericString(List<Object> parts, int beginIndex, int
	// endIndex) {
	// String str = "";
	// for (int i = beginIndex; i < endIndex; i++) {
	// if (parts.get(i) instanceof Character && isNumericCharacter(parts.get(i))) {
	// str += (char) parts.get(i);
	// } else
	// return null;
	// }
	// return str;
	// }

	private static String toAlphaNumericString(List<Object> parts, int beginIndex, int endIndex) {
		String str = "";
		for (int i = beginIndex; i < endIndex; i++) {
			if (parts.get(i) instanceof Character
					&& (isAlphabeticCharacter(parts.get(i)) || isNumericCharacter(parts.get(i)))) {
				str += (char) parts.get(i);
			} else
				return null;
		}
		return str;
	}

	private static boolean isAlphabeticCharacter(Object c) {
		if (c instanceof Character) {
			char ch = (char) c;
			if (ch >= 'a' && ch <= 'z')
				return true;
			if (ch >= 'A' && ch <= 'Z')
				return true;
		}
		return false;
	}

	private static boolean isNumericCharacter(Object c) {
		if (c instanceof Character) {
			char ch = (char) c;
			if (ch >= '0' && ch <= '9')
				return true;
			if (ch == '.')
				return true;
		}
		return false;
	}

}
