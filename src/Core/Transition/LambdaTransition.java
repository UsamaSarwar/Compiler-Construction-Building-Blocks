package Core.Transition;

/**
 * 
 * @author Usama Sarwar
 * 
 */
public class LambdaTransition implements Transition {

	private String from, to;

	public LambdaTransition(String from, String to) {
		super();
		this.from = from;
		this.to = to;
	}

	@Override
	public boolean isLambda() {
		return true;
	}

	@Override
	public char getLetter() {
		return 'Λ';
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
					&& ((Transition) obj).getTo().equals(getTo()) && ((Transition) obj).isLambda())
				return true;
		} catch (Exception e) {
		}
		return false;
	}

	@Override
	public String toString() {
		return "From: " + getFrom() + " to " + getTo() + " with lambda";
	}

}
