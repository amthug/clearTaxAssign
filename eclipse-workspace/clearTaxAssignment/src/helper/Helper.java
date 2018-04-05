package helper;

import java.util.HashMap;

import model.Equation;
import model.Operator;
import model.Substring;

public class Helper {

	public static final String RHS_KEY = "rhs", LHS_LEY = "lhs", OP_KEY = "op";
	public static final HashMap<String, String> opMap = new HashMap<>();
	public static final HashMap<Character, Character> opposites = new HashMap<>();
	static {
		opMap.put("add", "+");
		opMap.put("subtract", "-");
		opMap.put("multiply", "*");
		opMap.put("divide", "/");
		opMap.put("equal", "=");
		opposites.put('+', '-');
		opposites.put('-', '+');
		opposites.put('*', '/');
		opposites.put('/', '*');

	}

	public static String sanitizeEquation(StringBuilder equationBuilder) {
		equationBuilder.deleteCharAt(0);
		int index = equationBuilder.indexOf("=");
		equationBuilder.deleteCharAt(index - 2);
		return equationBuilder.toString();
	}

	public static String sanitzeRhs(StringBuilder rhs) {
		rhs.deleteCharAt(0);
		rhs.deleteCharAt(rhs.length() - 1);
		return rhs.toString();
	}

	public static Equation constructEquation(HashMap<String, Object> map) {
		Equation equation = new Equation();
		equation.setOp((String) map.get(OP_KEY));
		equation.setLhs(map.get(LHS_LEY));
		equation.setRhs(map.get(RHS_KEY));
		return equation;
	}

	public static String removeWhiteSpaces(String str) {
		return str.replace(" ", "");
	}

	public static StringBuilder removeOuterMostBracketsIfPresent(StringBuilder sb) {
		if (sb.charAt(0) == '(') {
			sb.deleteCharAt(0);
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb;
	}

	public static Substring findConstantWithNoBrackets(StringBuilder sb) {
		int bracketCount = 0;
		int startIndex = -1;
		for (int i = 0; i < sb.length() - 1; i++) {
			if (sb.charAt(i) == '(') {
				bracketCount++;
			} else if (sb.charAt(i) == ')') {
				bracketCount--;
			} else if (sb.charAt(i) == 'x' || opposites.containsKey(sb.charAt(i))) {

			} else if (bracketCount == 0) {
				startIndex = i;
				break;
			}
		}
		int endIndex = findConstantWithNoBrackets(sb, startIndex);
		return new Substring(startIndex, endIndex, sb);
	}

	private static int findConstantWithNoBrackets(StringBuilder lhs, int startIndex) {
		int endIndex = startIndex;
		for (int i = startIndex; i <= lhs.length() - 1; i++) {
			if (lhs.charAt(i) == '(' || lhs.charAt(i) == ')' || opposites.containsKey(lhs.charAt(i))) {
				break;
			} else {
				endIndex = i;
			}
		}
		return endIndex;

	}

	public static Operator getOpForConstant(StringBuilder sb, Substring constant) {
		if (constant.getStartIndex() == 0) {
			char op = sb.charAt(constant.getEndIndex() + 1);
			if (op == '*' || op == '/') {
				return new Operator(op, constant.getEndIndex() + 1);
			} else {
				return new Operator('+', -1);
			}
		} else {
			char op = sb.charAt(constant.getStartIndex() - 1);
			return new Operator(op, constant.getStartIndex() - 1);
		}
	}

	public static void moveConstantToRhs(Substring constant, Operator op, StringBuilder rhs) {
		char opposite = opposites.get(op.getOperator());
		rhs.append(opposite).append(constant.getContent());
		rhs.append(')');
		rhs.insert(0, '(');
	}

	public static void removeRedundantOperators(StringBuilder lhs, StringBuilder rhs, Substring substring,
			Operator op) {
		if (op.getIndex() == -1) {
			if (lhs.charAt(substring.getEndIndex() + 1) != '-') {
				lhs.delete(substring.getStartIndex(), substring.getEndIndex() + 2);
			} else {
				lhs.delete(substring.getStartIndex(), substring.getEndIndex() + 2);
				rhs.insert(0, '-');
			}
		} else {
			lhs.delete(op.getIndex(), substring.getEndIndex() + 1);
		}
	}
}
