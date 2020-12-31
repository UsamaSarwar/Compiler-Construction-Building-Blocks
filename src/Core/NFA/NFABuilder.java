package Core.NFA;

import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import Core.BinaryTree.BinaryTree;
import Core.BinaryTree.BinaryTreeNode;
import Core.Transition.LambdaTransition;
import Core.Transition.LetterTransition;
import Core.Transition.Transition;

/**
 * 
 * @author Usama Sarwar
 * 
 */
public class NFABuilder {

	private static final String CONCAT_IDENTIFIER = "concat";
	private static final String UNION_IDENTIFIER = "+";
	private static final String KLEENE_START_IDENTIFIER = "*";

	private String regularExpression;
	private char[] alphabets;
	private long uniqueNumber;
	private int uniqueCounter = 0;

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public NFABuilder(String regularText, char[] alphabets) {
		setAlphabets(alphabets);
		setRegularExpression(regularText);
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public String getRegularExpression() {
		return regularExpression;
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public void setRegularExpression(String regularExpression) {
		regularExpression = regularExpression.replace(" ", "");
		this.regularExpression = regularExpression;
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public char[] getAlphabets() {
		return alphabets;
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public void setAlphabets(char[] alphabets) {
		this.alphabets = alphabets;
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	private boolean isRegex() {
		String temp = getRegularExpression();
		temp = temp.replace(" ", "");
		char[] regex = temp.toCharArray();
		for (int i = 0; i < regex.length - 1; i++) {
			if (regex[i] == '(' && regex[i + 1] == '*')
				return false;
		}
		temp = temp.replace("*", "");
		temp = temp.replace("(", "");
		temp = temp.replace(")", "");
		temp = temp.replace("+", "");
		for (int i = 0; i < getAlphabets().length; i++) {
			temp = temp.replace(getAlphabets()[i] + "", "");
		}
		if (temp.equals(""))
			return true;
		return false;
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	private void checkRegexValidity() {
		try {
			Pattern.compile(getRegularExpression());
		} catch (PatternSyntaxException e) {
			throw new NotRegularStringException("String is not regular.");
		}
		if (!isRegex()) {
			throw new NotRegularStringException("String is not regular.");
		}
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	private String getEnhancedRegex(String regex) {
		String temp = regex;
		for (int i = 0; i < getAlphabets().length; i++) {
			temp = temp.replace(getAlphabets()[i] + "", "(" + getAlphabets()[i] + ")");
		}

		String fixedKleeneStartPriorityRegex = temp;
		ArrayList<Integer> kleenePositions = new ArrayList<Integer>();
		for (int i = 0; i < temp.length() - 1; i++) {
			if (temp.toCharArray()[i] == ')' && temp.toCharArray()[i + 1] == '*') {
				kleenePositions.add(i);
			}
		}
		for (int i = 0; i < kleenePositions.size(); i++) {
			int counter = 0, position = kleenePositions.get(i);
			firstLoop: while (true) {
				if (temp.charAt(position) == ')')
					counter++;
				else if (temp.charAt(position) == '(')
					counter--;
				if (counter == 0)
					break firstLoop;
				position--;
			}
			position += i * 2;
			int endPos = kleenePositions.get(i) + 2 + i * 2;
			fixedKleeneStartPriorityRegex = fixedKleeneStartPriorityRegex.substring(0, position)
					+ "(" + fixedKleeneStartPriorityRegex.substring(position, endPos) + ")"
					+ fixedKleeneStartPriorityRegex.substring(endPos);
		}

		String fixedConcatPriorityRegex = fixedKleeneStartPriorityRegex;
		ArrayList<Integer> concatPositions = new ArrayList<Integer>();
		char[] basicRegexArray = fixedKleeneStartPriorityRegex.toCharArray();
		for (int i = 0; i < basicRegexArray.length - 1; i++) {
			if (basicRegexArray[i] == ')' && basicRegexArray[i + 1] == '(') {
				concatPositions.add(i);
			} else if (i < basicRegexArray.length - 2)
				if (basicRegexArray[i] == ')' && basicRegexArray[i + 1] == '*'
						&& basicRegexArray[i + 2] == '(') {
					concatPositions.add(i);
				}
		}
		for (int i = 0; i < concatPositions.size(); i++) {
			int counter = 0, position = concatPositions.get(i);
			firstLoop: while (true) {
				if (fixedKleeneStartPriorityRegex.charAt(position) == ')')
					counter++;
				else if (fixedKleeneStartPriorityRegex.charAt(position) == '(')
					counter--;
				if (counter == 0)
					break firstLoop;
				position--;
			}
			position += i * 2;
			fixedConcatPriorityRegex = fixedConcatPriorityRegex.substring(0, position) + "("
					+ fixedConcatPriorityRegex.substring(position);

			counter = 0;
			position = concatPositions.get(i)
					+ (fixedKleeneStartPriorityRegex.charAt(concatPositions.get(i) + 1) == '*' ? 2
							: 1);
			secondLoop: while (true) {
				if (fixedKleeneStartPriorityRegex.charAt(position) == '(')
					counter++;
				else if (fixedKleeneStartPriorityRegex.charAt(position) == ')')
					counter--;
				if (counter == 0)
					break secondLoop;
				position++;
			}
			position += i * 2 + 1;
			fixedConcatPriorityRegex = fixedConcatPriorityRegex.substring(0, position) + ")"
					+ fixedConcatPriorityRegex.substring(position);
		}
		return fixedConcatPriorityRegex;
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public NFA toNFA() {
		checkRegexValidity();
		BinaryTree tree = new BinaryTree(new BinaryTreeNode(
				getEnhancedRegex(getRegularExpression())));
		buildRegexTree(tree.getRoot());
		NFA nfa = buildNFAFromRegexTree(tree);
		nfa.optimizeStatesName();
		return nfa;
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	private void buildRegexTree(BinaryTreeNode node) {
		Object value = node.getValue();
		if (value != null && value instanceof String) {
			String expression = (String) value;
			if (expression.length() > 0 && expression.charAt(0) == '(') {
				int counter = 0, position = 0;
				matchFinder: while (true) {
					if (expression.charAt(position) == '(')
						counter++;
					else if (expression.charAt(position) == ')')
						counter--;
					position++;
					if (counter == 0) {
						break matchFinder;
					}
				}
				if (position >= expression.length()) {
					node.setValue(expression.substring(1, expression.length() - 1));
					buildRegexTree(node);
					return;
				} else {
					BinaryTreeNode leftChildNode = new BinaryTreeNode(), rightChildNode = new BinaryTreeNode();
					if (expression.charAt(position) == '*' && position >= expression.length() - 1) {
						leftChildNode.setValue(expression.substring(1, expression.length() - 2));
						node.setValue(KLEENE_START_IDENTIFIER);
						node.setLeftChild(leftChildNode);
						buildRegexTree(leftChildNode);
						return;
					} else {
						if (expression.charAt(position) == '*') {
							position++;
							BinaryTreeNode leftleftChildNode = new BinaryTreeNode();
							leftChildNode.setValue(KLEENE_START_IDENTIFIER);
							leftleftChildNode.setValue(expression.substring(1, position - 2));
							leftChildNode.setLeftChild(leftleftChildNode);
							node.setLeftChild(leftChildNode);
							buildRegexTree(leftleftChildNode);
						}
						String nodeValue = expression.charAt(position) == '+' ? UNION_IDENTIFIER
								: CONCAT_IDENTIFIER;
						if (nodeValue.equals(UNION_IDENTIFIER))
							position++;
						node.setValue(nodeValue);
						if (node.getLeftChild() == null) {
							leftChildNode.setValue(expression.substring(1,
									position - (nodeValue.equals(CONCAT_IDENTIFIER) ? 1 : 2)));
							node.setLeftChild(leftChildNode);
							buildRegexTree(leftChildNode);
						}
						rightChildNode.setValue(expression.substring(position));
						node.setRightChild(rightChildNode);
						buildRegexTree(rightChildNode);
						return;
					}
				}
			} else if (expression.length() == 1) {
				return;
			}
		}
		// if (value != null && value instanceof String) {
		// System.out.println(value);
		// String text = (String) value;
		// for (int i = 0; i < getAlphabets().length; i++) {
		// text = text.replace(getAlphabets()[i] + "", "(" + getAlphabets()[i] +
		// ")");
		// }
		// while (true) {
		// String newText = text.replace("()", "");
		// if (newText.equals(text))
		// break;
		// text = newText;
		// }
		// System.out.println(text);
		// node.setValue(text);
		// buildRegexTree(node);
		// }
		throw new NotRegularStringException();
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	private NFA buildNFAFromRegexTree(BinaryTree tree) {
		return buildNFAFromNode(tree.getRoot());
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	private NFA buildNFAFromNode(BinaryTreeNode node) {
		if (node != null && node.getValue() != null) {
			// if (node.getValue() instanceof NFA) {
			// node.setRightChild(null);
			// node.setLeftChild(null);
			// return (NFA) node.getValue();
			// }
			if (node.getValue() instanceof String) {
				String value = (String) node.getValue();
				if (isInAlphabets(value))
					return createSimpleNFA(value.charAt(0));
				if (value.equals(CONCAT_IDENTIFIER) || value.equals(UNION_IDENTIFIER)) {
					if (node.getLeftChild() == null || node.getLeftChild().getValue() == null
							|| node.getRightChild() == null
							|| node.getRightChild().getValue() == null)
						throw new RuntimeException("Error in creating NFA");
					NFA leftNFA = buildNFAFromNode(node.getLeftChild());
					NFA rightNFA = buildNFAFromNode(node.getRightChild());
					if (value.equals(CONCAT_IDENTIFIER))
						return concatNFA(leftNFA, rightNFA);
					else if (value.equals(UNION_IDENTIFIER))
						return unionNFA(leftNFA, rightNFA);
				} else if (value.equals(KLEENE_START_IDENTIFIER)) {
					if (node.getLeftChild() == null || node.getLeftChild().getValue() == null)
						throw new RuntimeException("Error in creating NFA");
					NFA leftNFA = buildNFAFromNode(node.getLeftChild());
					return kleeneStarNFA(leftNFA);
				}
			}
		}
		// return null;
		throw new RuntimeException("Error in creating NFA");
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	private boolean isInAlphabets(String text) {
		try {
			for (int i = 0; i < getAlphabets().length; i++) {
				if (text.equals(getAlphabets()[i] + ""))
					return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	private NFA createSimpleNFA(char acceptingChar) {

		String state1 = (getUniqueNumber() + "");
		String state2 = (getUniqueNumber() + "");

		NFA nfa = new NFA(getAlphabets());

		ArrayList<String> states = new ArrayList<String>();
		ArrayList<String> acceptingStates = new ArrayList<String>();
		ArrayList<Transition> transitions = new ArrayList<Transition>();
		String startingState = state1;

		states.add(state1);
		states.add(state2);
		acceptingStates.add(state2);
		transitions.add(new LetterTransition(acceptingChar, state1, state2));

		nfa.setStates(states);
		nfa.setAcceptingStates(acceptingStates);
		nfa.setStartingState(startingState);
		nfa.setTransitions(transitions);

		return nfa;
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	private NFA unionNFA(NFA nfa1, NFA nfa2) {
		if (nfa1 == null)
			return nfa2;
		if (nfa2 == null)
			return nfa1;
		String newState = (getUniqueNumber() + "");
		NFA nfa = new NFA(getAlphabets());
		nfa.addState(newState);
		nfa.addStates(nfa1.getStates());
		nfa.addStates(nfa2.getStates());

		nfa.setStartingState(newState);

		nfa.addAcceptingStates(nfa1.getAcceptingStates());
		nfa.addAcceptingStates(nfa2.getAcceptingStates());

		nfa.addTransition(new LambdaTransition(newState, nfa1.getStartingState()));
		nfa.addTransition(new LambdaTransition(newState, nfa2.getStartingState()));
		nfa.addTransitions(nfa1.getTransitions());
		nfa.addTransitions(nfa2.getTransitions());

		return nfa;
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	private NFA concatNFA(NFA nfa1, NFA nfa2) {
		if (nfa1 == null)
			return nfa2;
		if (nfa2 == null)
			return nfa1;
		NFA nfa = new NFA(getAlphabets());
		nfa.addStates(nfa1.getStates());
		nfa.addStates(nfa2.getStates());

		nfa.setStartingState(nfa1.getStartingState());

		nfa.addAcceptingStates(nfa2.getAcceptingStates());

		for (int i = 0; i < nfa1.getAcceptingStates().size(); i++) {
			nfa.addTransition(new LambdaTransition(nfa1.getAcceptingStates().get(i), nfa2
					.getStartingState()));
		}
		nfa.addTransitions(nfa1.getTransitions());
		nfa.addTransitions(nfa2.getTransitions());

		return nfa;
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	private NFA kleeneStarNFA(NFA givenNfa) {
		String newState = (getUniqueNumber() + "");
		NFA nfa = new NFA(getAlphabets());
		nfa.addState(newState);
		nfa.addStates(givenNfa.getStates());

		nfa.setStartingState(newState);

		nfa.addAcceptingState(newState);

		nfa.addTransition(new LambdaTransition(newState, givenNfa.getStartingState()));
		for (int i = 0; i < givenNfa.getAcceptingStates().size(); i++) {
			nfa.addTransition(new LambdaTransition(givenNfa.getAcceptingStates().get(i), newState));
		}
		nfa.addTransitions(givenNfa.getTransitions());

		return nfa;
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	private long getUniqueNumber() {
		uniqueNumber = new Date().getTime();
		uniqueNumber += uniqueCounter;
		uniqueCounter++;
		return uniqueNumber;
	}

	// private void printAllChildren(BinaryTreeNode node) {
	// if (node != null && node.getValue() != null) {
	// String text = node.getValue().toString();
	// if (node.getParent() != null && node.getParent().getValue() != null)
	// text += "  my parent: " + node.getParent().getValue().toString();
	// System.out.println(text);
	// }
	// if (node != null) {
	// printAllChildren(node.getLeftChild());
	// printAllChildren(node.getRightChild());
	// }
	// }
	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	static class NotRegularStringException extends RuntimeException {

		private static final long serialVersionUID = -5793081370423590610L;

		/**
		 * 
		 * @author Usama Sarwar
		 * 
		 */
		public NotRegularStringException(String string) {
			super(string);
		}

		/**
		 * 
		 * @author Usama Sarwar
		 * 
		 */
		public NotRegularStringException() {
			super();
		}

	}

}
