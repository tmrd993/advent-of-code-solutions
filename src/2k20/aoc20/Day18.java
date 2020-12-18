package aoc20;

import java.io.File;
import java.util.List;
import java.util.Stack;

import myutils20.StaticUtils;

public class Day18 {

    List<String> rawData;

    public Day18(File inputFile) {
	rawData = StaticUtils.inputFileToStringList(inputFile);
    }

    public long run1() {
	return rawData.stream().mapToLong(s -> evaluatePostfix(infixToPostfixNoPrecedence(s))).sum();
    }

    public long run2() {
	return rawData.stream().mapToLong(s -> evaluatePostfix(infixToPostfixPlusPrecedence(s))).sum();
    }

    private long evaluatePostfix(String postfix) {
	Stack<String> stack = new Stack<>();
	for (int i = 0; i < postfix.length(); i++) {
	    char currentChar = postfix.charAt(i);
	    if (currentChar == '+' || currentChar == '*') {
		long a = Long.parseLong(stack.pop());
		long b = Long.parseLong(stack.pop());

		if (currentChar == '+')
		    stack.push(Long.toString(a + b));
		else if (currentChar == '*')
		    stack.push(Long.toString(a * b));

	    } else {
		stack.push(Character.toString(currentChar));
	    }
	}

	return Long.parseLong(stack.pop());
    }

    // convert an infix expression to postfix with left to right evaluation order
    // and no operator precedence
    private String infixToPostfixNoPrecedence(String infix) {
	String postfix = "";
	Stack<Character> stack = new Stack<>();

	for (int i = 0; i < infix.length(); i++) {
	    char currentChar = infix.charAt(i);
	    if (currentChar == '+' || currentChar == '*') {
		if (stack.isEmpty()) {
		    stack.push(currentChar);
		} else if (stack.peek() == '+' || stack.peek() == '*') {
		    postfix = postfix + stack.pop();
		    stack.push(currentChar);
		} else {
		    stack.push(currentChar);
		}
	    } else if (currentChar == '(') {
		stack.push(currentChar);
	    } else if (currentChar == ')') {

		while (stack.peek() != '(') {
		    postfix = postfix + stack.pop();
		}

		stack.pop();

	    } else if (currentChar != ' ') {
		postfix = postfix + currentChar;
	    }

	}

	while (!stack.isEmpty()) {
	    postfix = postfix + stack.pop();
	}

	return postfix;
    }

    // convert an infix expression to postfix from left to right with higher order
    // precedence for plus
    private String infixToPostfixPlusPrecedence(String infix) {
	String postfix = "";
	Stack<Character> stack = new Stack<>();

	for (int i = 0; i < infix.length(); i++) {
	    char currentChar = infix.charAt(i);
	    if (currentChar == '+' || currentChar == '*') {
		if (stack.isEmpty() || stack.peek() == '(') {
		    stack.push(currentChar);
		} else if (precedence(stack.peek()) >= precedence(currentChar)) {
		    postfix = postfix + stack.pop();
		    stack.push(currentChar);
		} else {
		    stack.push(currentChar);
		}
	    } else if (currentChar == '(') {
		stack.push(currentChar);
	    } else if (currentChar == ')') {

		while (stack.peek() != '(') {
		    postfix = postfix + stack.pop();
		}
		stack.pop();

	    } else if (currentChar != ' ') {
		postfix = postfix + currentChar;
	    }

	}

	while (!stack.isEmpty()) {
	    postfix = postfix + stack.pop();
	}

	return postfix;
    }

    private int precedence(char c) {
	switch (c) {
	case '+':
	    return 2;
	case '*':
	    return 1;
	}

	throw new IllegalArgumentException("No value mapped to " + c);
    }

    public static void main(String[] args) {
	Day18 test = new Day18(new File("C:\\Users\\Timucin\\Desktop\\Advent of code 2020\\Day 18\\InputFile1.txt"));
	System.out.println(test.run2());

    }
}
