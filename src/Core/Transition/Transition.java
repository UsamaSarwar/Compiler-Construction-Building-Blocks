package Core.Transition;

/**
 * 
 * @author Usama Sarwar
 * 
 */
public interface Transition {
	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public boolean isLambda();

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public char getLetter();

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public String getFrom();

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public String getTo();

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public void setLetter(char letter);

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public void setFrom(String from);

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public void setTo(String to);
}