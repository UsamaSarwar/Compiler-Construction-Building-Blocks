package GUI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;

public class StateView extends JComponent {

	private static final long serialVersionUID = -7766232547627440410L;
	private int diameter;
	private String name;
	private boolean isAccepting;

	
	public StateView(String name) {
		super();
		this.name = name;
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public StateView(String name, boolean isAccepting) {
		this(name);
		this.isAccepting = isAccepting;
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public StateView(String name, boolean isAccepting, int diameter) {
		this(name, isAccepting);
		setDiameter(diameter);
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public int getDiameter() {
		return diameter;
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public void setDiameter(int diameter) {
		this.diameter = diameter;
		setPreferredSize(new Dimension(diameter + 5, diameter + 5));
		setSize(getPreferredSize());
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public boolean isAccepting() {
		return isAccepting;
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public void setAccepting(boolean isAccepting) {
		this.isAccepting = isAccepting;
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2.setRenderingHints(qualityHints);
		g2.drawOval(0, 0, diameter, diameter);
		Font font = g2.getFont();
		font = new Font(font.getName(), Font.PLAIN, 20);
		g2.setFont(font);
		g2.drawString(
				name,
				(int) (diameter / 2 - g.getFontMetrics().getStringBounds(name, g).getWidth() / 2) + 1,
				(int) (diameter / 2 + g.getFontMetrics().getStringBounds(name, g).getHeight() / 2) - 3);
		if (isAccepting)
			g2.drawOval(5, 5, diameter - 10, diameter - 10);
	}

}
