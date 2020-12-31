package Core.Transition;

/**
 * 
 * @author Usama Sarwar
 * 
 */
public class LetterTransition implements Transition {

	private char letter;
	private String from, to;

	public LetterTransition(char letter, String from, String to) {
		super();
		this.letter = letter;
		this.from = from;
		this.to = to;
	}

	@Override
	public boolean isLambda() {
		return false;
	}

	@Override
	public char getLetter() {
		return letter;
	}

	@Override
	public String getFrom() {
		return from;
	}

	@Override
	public String getTo() {
		return to;
	}

	@Override
	public void setLetter(char letter) {
		this.letter = letter;
	}

	@Override
	public void setFrom(String from) {
		this.from = from;
	}

	@Override
	public void setTo(String to) {
		this.to = to;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Transition))
			return false;
		try {
			if (((Transition) obj).getFrom().equals(getFrom())
					&& ((Transition) obj).getTo().equals(getTo())
					&& ((Transition) obj).getLetter() == getLetter())
				return true;
		} catch (Exception e) {
		}
		return false;
	}

	@Override
	public String toString() {
		return "From: " + getFrom() + " to " + getTo() + " with " + getLetter();
	}

}
