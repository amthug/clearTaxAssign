package model;

public class Operator {
	char operator;
	int index;

	public Operator(char operator, int index) {
		this.operator = operator;
		this.index = index;
	}

	public char getOperator() {
		return operator;
	}

	public void setOperator(char operator) {
		this.operator = operator;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
