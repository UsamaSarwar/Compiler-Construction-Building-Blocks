package Core.DFA;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import Core.NFA.NFA;
import Core.Transition.LetterTransition;
import Core.Transition.Transition;

/**
 * 
 * @author Usama Sarwar
 * 
 */
public class DFABuilder {

	private NFA nfa;
	private DFA dfa;
	private char[] alphabets;

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public DFABuilder(NFA nfa) {
		this.nfa = nfa;
		this.alphabets = nfa.getAlphabets();
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public DFABuilder(DFA dfa) {
		this.dfa = dfa;
		this.alphabets = dfa.getAlphabets();
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public NFA getNFA() {
		return nfa;
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
	public DFA getMinimizedDFAfromDFA() {
		int[][] decreaseTable = new int[dfa.getStates().size()][dfa.getStates().size()];
		decreaseTable = calculateDecreaseTable();

		ArrayList<String> oldStates = new ArrayList<String>();
		oldStates.addAll(dfa.getStates());

		for (int i = 1; i < decreaseTable.length; i++) {
			for (int j = 0; j < i; j++) {
				if (decreaseTable[i][j] == 0) {
					unifyStates(oldStates.get(i), oldStates.get(j));
				}
			}
		}
		Collections.sort(dfa.getStates());
		// dfa.optimizeStatesName();
		return dfa;
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	private int[][] calculateDecreaseTable() {
		int[][] decreaseTable = new int[dfa.getStates().size()][dfa.getStates().size()];
		ArrayList<String> states = dfa.getStates();

		for (int i = 0; i < dfa.getAcceptingStates().size(); i++) {
			int rowOrColumn = states.indexOf(dfa.getAcceptingStates().get(i));
			for (int j = 0; j < rowOrColumn; j++) {
				if (!dfa.getAcceptingStates().contains(states.get(j))) {
					decreaseTable[rowOrColumn][j] = 1;
				}
			}
			for (int j = rowOrColumn + 1; j < decreaseTable[rowOrColumn].length; j++) {
				if (!dfa.getAcceptingStates().contains(states.get(j))) {
					decreaseTable[j][rowOrColumn] = 1;
				}
			}
		}

		for (int i = 1; i < decreaseTable.length; i++) {
			for (int j = 0; j < i; j++) {
				decreaseTable[i][j] = statesRelation(i, j, decreaseTable,
						new boolean[decreaseTable.length][decreaseTable.length]);
			}
		}
		return decreaseTable;
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	private int statesRelation(int i, int j, int[][] tableSoFar, boolean[][] inProgress) {
		if (i < j) {
			int temp = i;
			i = j;
			j = temp;
		}
		int rel = tableSoFar[i][j];
		if (rel != 0)
			return rel;
		if (inProgress[i][j])
			return 0;
		inProgress[i][j] = true;
		String state1 = dfa.getStates().get(i);
		String state2 = dfa.getStates().get(j);

		HashMap<Character, String> state1To = new HashMap<Character, String>();
		HashMap<Character, String> state2To = new HashMap<Character, String>();

		for (int k = 0; k < dfa.getTransitions().size(); k++) {
			if (dfa.getTransitions().get(k).getFrom().equals(state1)) {
				state1To.put(dfa.getTransitions().get(k).getLetter(), dfa.getTransitions().get(k)
						.getTo());
			}
			if (dfa.getTransitions().get(k).getFrom().equals(state2)) {
				state2To.put(dfa.getTransitions().get(k).getLetter(), dfa.getTransitions().get(k)
						.getTo());
			}
		}

		for (int k = 0; k < getAlphabets().length; k++) {
			if (state1To.get(getAlphabets()[k]).equals(state2To.get(getAlphabets()[k]))) {
				continue;
			} else if (state1.equals(state1To.get(getAlphabets()[k]))
					&& state2.equals(state2To.get(getAlphabets()[k]))) {
				continue;
			} else {
				int relation = statesRelation(
						dfa.getStates().indexOf(state1To.get(getAlphabets()[k])), dfa.getStates()
								.indexOf(state2To.get(getAlphabets()[k])), tableSoFar, inProgress);
				if (relation != 0) {
					inProgress[i][j] = false;
					return relation + 1;
				}
			}
		}
		inProgress[i][j] = false;
		return 0;
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	private void unifyStates(String state1, String state2) {
		if (!dfa.getStates().contains(state1)) {
			outerFor: for (int i = 0; i < dfa.getStates().size(); i++) {
				String[] embededStates = dfa.getStates().get(i).split(",");
				for (int j = 0; j < embededStates.length; j++) {
					if (embededStates[j].equals(state1)) {
						state1 = dfa.getStates().get(i);
						break outerFor;
					}
				}
			}
		}

		if (!dfa.getStates().contains(state2)) {
			outerFor: for (int i = 0; i < dfa.getStates().size(); i++) {
				String[] embededStates = dfa.getStates().get(i).split(",");
				for (int j = 0; j < embededStates.length; j++) {
					if (embededStates[j].equals(state2)) {
						state2 = dfa.getStates().get(i);
						break outerFor;
					}
				}
			}
		}

		dfa.getStates().remove(state1);
		dfa.getStates().remove(state2);
		dfa.getStates().add(state1 + "," + state2);

		if (dfa.getStartingState().equals(state1) || dfa.getStartingState().equals(state2)) {
			dfa.setStartingState(state1 + "," + state2);
		}

		if (dfa.getAcceptingStates().contains(state1) || dfa.getAcceptingStates().contains(state2)) {
			dfa.addAcceptingState(state1 + "," + state2);
		}
		dfa.getAcceptingStates().remove(state1);
		dfa.getAcceptingStates().remove(state2);

		for (int i = 0; i < dfa.getTransitions().size(); i++) {
			if (dfa.getTransitions().get(i).getFrom().equals(state1)
					|| dfa.getTransitions().get(i).getFrom().equals(state2)) {
				dfa.getTransitions().get(i).setFrom(state1 + "," + state2);
			}

			if (dfa.getTransitions().get(i).getTo().equals(state1)
					|| dfa.getTransitions().get(i).getTo().equals(state2)) {
				dfa.getTransitions().get(i).setTo(state1 + "," + state2);
			}
		}
		ArrayList<Transition> shallowCopy = new ArrayList<Transition>();
		shallowCopy.addAll(dfa.getTransitions());
		for (int i = 0; i < shallowCopy.size(); i++) {
			while (dfa.getTransitions().contains(shallowCopy.get(i))) {
				dfa.getTransitions().remove(shallowCopy.get(i));
			}
			dfa.addTransition(shallowCopy.get(i));
		}
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public DFA getDFAfromNFA() {
		DFA dfa = new DFA(getAlphabets());
		dfa.addState(nfa.getStartingState());
		dfa.setStartingState(nfa.getStartingState());
		for (int i = 0; i < getAlphabets().length; i++) {
			dfa.addTransition(new LetterTransition(getAlphabets()[i], dfa.getStartingState(),
					getUniqueNameFromList(deltaStar(dfa.getStartingState(), getAlphabets()[i]))));
		}

		buildTransitionsRecursively(dfa);

		for (int i = 0; i < dfa.getTransitions().size(); i++) {
			if (!dfa.getStates().contains(dfa.getTransitions().get(i).getFrom()))
				dfa.addState(dfa.getTransitions().get(i).getFrom());
			if (!dfa.getStates().contains(dfa.getTransitions().get(i).getTo()))
				dfa.addState(dfa.getTransitions().get(i).getTo());
		}

		for (int i = 0; i < dfa.getStates().size(); i++) {
			if (!dfa.getStates().get(i).equals("null")) {
				if (dfa.getStates().get(i).indexOf(',') > 0) {
					for (int j = 0; j < dfa.getStates().get(i).split(",").length; j++) {
						if (nfa.getAcceptingStates().contains(dfa.getStates().get(i).split(",")[j])) {
							dfa.addAcceptingState(dfa.getStates().get(i));
							break;
						}
					}
				} else {
					if (nfa.getAcceptingStates().contains(dfa.getStates().get(i)))
						dfa.addAcceptingState(dfa.getStates().get(i));
				}
			}
		}
		if (acceptsLambda(nfa.getStartingState())) {
			if (!dfa.getAcceptingStates().contains(nfa.getStartingState()))
				dfa.addAcceptingState(nfa.getStartingState());
		}
		dfa.optimizeStatesName();
		return dfa;
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	private void buildTransitionsRecursively(DFA dfa) {
		ArrayList<Transition> newTransitions = new ArrayList<Transition>();
		newTransitions.addAll(dfa.getTransitions());

		for (int i = 0; i < dfa.getTransitions().size(); i++) {
			HashMap<Character, Boolean> alphasInTransitions = new HashMap<Character, Boolean>();
			for (int j = 0; j < dfa.getTransitions().size(); j++) {
				if (dfa.getTransitions().get(i).getTo()
						.equals(dfa.getTransitions().get(j).getFrom())) {
					alphasInTransitions.put(dfa.getTransitions().get(j).getLetter(), true);
				}
			}
			ArrayList<Character> notIncludedLetters = getNotIncludedLetters(alphasInTransitions
					.keySet());
			for (int j = 0; j < notIncludedLetters.size(); j++) {
				Transition trans = new LetterTransition(notIncludedLetters.get(j), dfa
						.getTransitions().get(i).getTo(), getUniqueNameFromList(deltaStar(dfa
						.getTransitions().get(i).getTo(), notIncludedLetters.get(j))));
				if (!newTransitions.contains(trans))
					newTransitions.add(trans);
			}
		}

		if (!dfa.getTransitions().equals(newTransitions)) {
			dfa.setTransitions(newTransitions);
			buildTransitionsRecursively(dfa);
		}
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	private boolean acceptsLambda(String state) {
		for (int i = 0; i < nfa.getTransitions().size(); i++) {
			if (nfa.getTransitions().get(i).getFrom().equals(state)
					&& nfa.getTransitions().get(i).isLambda()) {
				if (nfa.getAcceptingStates().contains(nfa.getTransitions().get(i).getTo())) {
					return true;
				} else {
					if (acceptsLambda(nfa.getTransitions().get(i).getTo())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	private ArrayList<Character> getNotIncludedLetters(Set<Character> list) {
		ArrayList<Character> result = new ArrayList<Character>();
		for (int i = 0; i < getAlphabets().length; i++) {
			if (!list.contains(getAlphabets()[i]))
				result.add(getAlphabets()[i]);
		}
		return result;
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	private String getUniqueNameFromList(ArrayList<String> list) {
		String result = "";
		for (int i = 0; i < list.size(); i++) {
			result += list.get(i) + (i == list.size() - 1 ? "" : ",");
		}
		if (result.equals(""))
			result = "null";
		return result;
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	private ArrayList<String> deltaStar(String state, char letter) {
		ArrayList<String> resultStates = new ArrayList<String>();
		ArrayList<String> departureClosure = new ArrayList<String>();
		if (state.indexOf(',') > 0)
			for (int i = 0; i < state.split(",").length; i++) {
				departureClosure.addAll(lambdaClosure(state.split(",")[i]));
			}
		else
			departureClosure = lambdaClosure(state);
		for (int i = 0; i < departureClosure.size(); i++) {
			for (int j = 0; j < nfa.getTransitions().size(); j++) {
				if (nfa.getTransitions().get(j).getFrom().equals(departureClosure.get(i))
						&& !nfa.getTransitions().get(j).isLambda()
						&& nfa.getTransitions().get(j).getLetter() == letter) {
					ArrayList<String> states = lambdaClosure(nfa.getTransitions().get(j).getTo());
					if (!resultStates.containsAll(states))
						resultStates.addAll(states);
				}
			}
		}
		return resultStates;
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	private ArrayList<String> lambdaClosure(String state) {
		ArrayList<String> resultStates = new ArrayList<String>();
		resultStates.add(state);
		for (int i = 0; i < nfa.getTransitions().size(); i++) {
			if (nfa.getTransitions().get(i).getFrom().equals(state)
					&& nfa.getTransitions().get(i).isLambda()) {
				resultStates.addAll(lambdaClosure(nfa.getTransitions().get(i).getTo()));
			}
		}
		return resultStates;

	}

}
