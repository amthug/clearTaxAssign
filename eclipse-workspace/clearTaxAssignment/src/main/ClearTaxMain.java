package main;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;

import helper.Helper;
import model.Equation;
import model.Operator;
import model.Substring;

public class ClearTaxMain {
	private static final String filePath = "C://Users/Shubhank/Desktop/input.json";
	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static void main(String[] args) {
		configureObjectMapper();
		File file = new File(filePath);
		try {
			String json = new Scanner(file).useDelimiter("\\Z").next();
			Equation equation = objectMapper.readValue(json, Equation.class);
			String stringEquation = printEquation(equation);
			String reorganizedEquation = printReorganizedEquation(stringEquation);
			solveEquation(reorganizedEquation);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String printEquation(Equation equation) throws IOException {
		StringBuilder equationBulder = new StringBuilder(
				getLhs(equation) + " " + getOp(equation) + " " + getRhs(equation));// visits the lhs, op and rhs
																					// recursively
		String stringEquation = Helper.sanitizeEquation(equationBulder);
		System.out.println("The Equation is: " + stringEquation);
		return stringEquation;
	}

	public static String printReorganizedEquation(String stringEquation) {
		String[] sides = stringEquation.split("=");
		String sanitizedLhs = Helper.removeWhiteSpaces(sides[0]);
		String sanitizedRhs = Helper.removeWhiteSpaces(sides[1]);
		StringBuilder lhsBuilder = new StringBuilder(sanitizedLhs);
		StringBuilder rhsBuilder = new StringBuilder(sanitizedRhs);
		reorganize(lhsBuilder, rhsBuilder);
		String newSanitizedRhs = Helper.sanitzeRhs(rhsBuilder);
		String reorganizedEquation = "x = " + newSanitizedRhs;
		System.out.println(reorganizedEquation);
		return reorganizedEquation;
	}

	public static String solveEquation(String reorganizedEquation) throws ScriptException {
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("JavaScript");
		String solution = engine.eval(reorganizedEquation.split("=")[1]).toString();
		System.out.println("final solution = " + solution);
		return solution;
	}

	private static void reorganize(StringBuilder lhs, StringBuilder rhs) {
		if (lhs.toString().equals("x")) {
			return;
		}
		Substring constant = Helper.findConstantWithNoBrackets(lhs);
		Operator op = Helper.getOpForConstant(lhs, constant);
		Helper.moveConstantToRhs(constant, op, rhs);
		Helper.removeRedundantOperators(lhs, rhs, constant, op);
		Helper.removeOuterMostBracketsIfPresent(lhs);
		reorganize(lhs, rhs);
	}

	private static String getRhs(Equation equation) throws IOException {
		try {
			Integer.parseInt(equation.getRhs().toString());
			return equation.getRhs().toString();
		} catch (NumberFormatException e) {
		}
		Equation subEquation = Helper.constructEquation((HashMap<String, Object>) equation.getRhs());
		return "(" + getLhs(subEquation) + " " + getOp(subEquation) + " " + getRhs(subEquation) + ")";
	}

	private static String getLhs(Equation equation) throws IOException {
		try {
			if (equation.getLhs().equals("x"))
				return equation.getLhs().toString();
			Integer.parseInt(equation.getLhs().toString());
			return equation.getLhs().toString();
		} catch (NumberFormatException e) {
		}
		Equation subEquation = Helper.constructEquation((HashMap<String, Object>) equation.getLhs());
		return "(" + getLhs(subEquation) + " " + getOp(subEquation) + " " + getRhs(subEquation) + ")";
	}

	private static String getOp(Equation equation) {
		return Helper.opMap.get(equation.getOp());
	}

	private static void configureObjectMapper() {
		objectMapper.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, false);
		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
	}
}
