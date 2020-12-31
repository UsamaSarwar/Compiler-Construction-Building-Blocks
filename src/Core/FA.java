package Core;

import java.util.ArrayList;

import Core.Transition.Transition;

/**
 * 
 * @author Usama Sarwar
 * 
 */
public class FA {

	private char[] alphabets;
	private ArrayList<String> states;
	private ArrayList<String> acceptingStates;
	private String startingState;
	private ArrayList<Transition> transitions;

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public FA(char[] alphabets) {
		this.alphabets = alphabets;
		states = new ArrayList<String>();
		acceptingStates = new ArrayList<String>();
		transitions = new ArrayList<Transition>();
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
	public ArrayList<String> getStates() {
		return states;
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public void setStates(ArrayList<String> states) {
		this.states = states;
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public void addState(String state) {
		this.states.add(state);
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public void addStates(ArrayList<String> list) {
		states.addAll(list);
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public ArrayList<String> getAcceptingStates() {
		return acceptingStates;
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public void setAcceptingStates(ArrayList<String> acceptingStates) {
		this.acceptingStates = acceptingStates;
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public void addAcceptingStates(ArrayList<String> list) {
		this.acceptingStates.addAll(list);
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public void addAcceptingState(String state) {
		this.acceptingStates.add(state);
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public String getStartingState() {
		return startingState;
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public void setStartingState(String startingState) {
		this.startingState = startingState;
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public ArrayList<Transition> getTransitions() {
		return transitions;
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public void setTransitions(ArrayList<Transition> transitions) {
		this.transitions = transitions;
	}

	public void addTransitions(ArrayList<Transition> list) {
		this.transitions.addAll(list);
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public void addTransition(Transition item) {
		this.transitions.add(item);
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public void optimizeStatesName() {
		ArrayList<String> newnewStates = new ArrayList<String>();
		for (int i = 0; i < states.size(); i++) {
			String oldStateName = states.get(i);
			String newStateName = String.valueOf(i + 1);
			newnewStates.add(newStateName);
			ArrayList<String> newAcceptingStates = new ArrayList<String>();
			for (int j = 0; j < acceptingStates.size(); j++) {
				if (acceptingStates.get(j).equals(oldStateName)) {
					newAcceptingStates.add(newStateName);
				} else {
					newAcceptingStates.add(acceptingStates.get(j));
				}
			}
			setAcceptingStates(newAcceptingStates);
			for (int j = 0; j < transitions.size(); j++) {
				if (transitions.get(j).getFrom().equals(oldStateName))
					transitions.get(j).setFrom(newStateName);
				if (transitions.get(j).getTo().equals(oldStateName))
					transitions.get(j).setTo(newStateName);
			}
			if (startingState.equals(oldStateName))
				startingState = newStateName;
		}
		setStates(newnewStates);
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("States:\n");
		for (int i = 0; i < getStates().size(); i++) {
			builder.append(getStates().get(i) + ", ");
		}

		builder.append("\n---------------------\n");
		builder.append("Starting State:\n");
		builder.append(getStartingState());

		builder.append("\n---------------------");
		builder.append("\nAccepting States:\n");
		for (int i = 0; i < getAcceptingStates().size(); i++) {
			builder.append(getAcceptingStates().get(i) + ", ");
		}

		builder.append("\n---------------------");
		builder.append("\nTransitions:");
		for (int i = 0; i < getTransitions().size(); i++) {
			builder.append("\n" + getTransitions().get(i).toString());
		}
		return builder.toString();
	}
}
