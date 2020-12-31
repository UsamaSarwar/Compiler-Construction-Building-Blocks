package GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.QuadCurve2D;
import java.util.ArrayList;

import javax.swing.JComponent;

import Core.FA;
import Core.Transition.Transition;

/**
 * This is a JComponent. It basically gets an {@link FA} and draw it in its
 * area.
 * 
 * @author Usama Sarwar
 * 
 */
public class FAPanel extends JComponent {

	private static final long serialVersionUID = 8009241661275578342L;

	private StateView startingState;
	private StateView[] states;
	private ArrayList<Transition> transitions;
	private final static int STATES_DIAMETER = 70;
	private final static int STATES_GAP = 50;
	private final static int BORDER_WIDTH = 30;

	public FAPanel() {
		super();
		addComponentListener(new ComponentListener() {

			@Override
			public void componentShown(ComponentEvent event) {
			}

			@Override
			public void componentResized(ComponentEvent event) {
				reorderComponents();
				repaint();
			}

			@Override
			public void componentMoved(ComponentEvent event) {
			}

			@Override
			public void componentHidden(ComponentEvent event) {
			}
		});
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	public void setFA(FA fa) {
		states = null;
		startingState = null;
		transitions = null;
		removeAll();
		states = new StateView[fa.getStates().size()];
		for (int i = 0; i < fa.getStates().size(); i++) {
			states[i] = new StateView(fa.getStates().get(i), fa.getAcceptingStates().contains(
					fa.getStates().get(i)), STATES_DIAMETER);
			if (fa.getStartingState().equals(fa.getStates().get(i))) {
				startingState = states[i];
			}
			add(states[i]);
		}
		transitions = new ArrayList<Transition>();
		transitions.addAll(fa.getTransitions());

		reorderComponents();
		repaint();
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	private void reorderComponents() {
		try {
			if (getWidth() > 0 && getHeight() > 0) {
				int horizontalCount = (getWidth() - 2 * BORDER_WIDTH)
						/ (STATES_DIAMETER + STATES_GAP);
				for (int i = 0; i < states.length; i++) {
					int noOfStateColumns = states.length < horizontalCount ? states.length
							: horizontalCount;
					int noOfStateRows = (states.length - 1) / horizontalCount + 1;
					int wholeStatesWidth = noOfStateColumns * (STATES_GAP + STATES_DIAMETER)
							- STATES_GAP;
					int wholeStatesHeight = noOfStateRows * (STATES_GAP + STATES_DIAMETER)
							- STATES_GAP;

					int x = ((i % horizontalCount) * ((i % horizontalCount == 0 ? 0 : STATES_GAP) + STATES_DIAMETER))
							+ ((getWidth() / 2) - (wholeStatesWidth / 2));
					int y = ((i / horizontalCount) * (STATES_GAP + STATES_DIAMETER))
							+ ((getHeight() / 2) - (wholeStatesHeight / 2));
					states[i].setLocation(x, y);

				}
			}
		} catch (Exception e) {
		}
		validate();
		repaint();
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	private double distance(StateView s1, StateView s2) {
		return Math.sqrt(Math.pow(s1.getX() - s2.getX(), 2) + Math.pow(s1.getY() - s2.getY(), 2));
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	private StateView getStateView(String stateName) {
		for (int i = 0; i < states.length; i++) {
			if (states[i].getName().equals(stateName))
				return states[i];
		}
		return null;
	}

	/**
	 * 
	 * @author Usama Sarwar
	 * 
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (states == null)
			return;
		g.setColor(Color.black);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		RenderingHints qualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		qualityHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2.setRenderingHints(qualityHints);

		if (getWidth() > 0 && getHeight() > 0) {
			mainFor: for (int i = 0; i < transitions.size(); i++) {
				StateView s1 = getStateView(transitions.get(i).getFrom());
				StateView s2 = getStateView(transitions.get(i).getTo());
				int fromX = 0, fromY = 0, toX = 0, toY = 0;
				int diagnoalDifference = (int) (Math.sqrt(2 * Math.pow(STATES_DIAMETER / 2, 2)) - (STATES_DIAMETER / 2)) - 4;
				int xCurve = 0;
				int yCurve = 0;
				int curveDefaultValue = 40;
				int curveValue = (int) (curveDefaultValue * (distance(s1, s2) / (STATES_DIAMETER + STATES_GAP)));
				int arrow1X = 0, arrow1Y = 0, arrow2X = 0, arrow2Y = 0;
				int arrowLength = 10;
				int arrowShadow = (int) ((Math.sqrt(2 * Math.pow(arrowLength, 2))) / 2);
				if (s2.getX() < s1.getX()) {
					if (s2.getY() < s1.getY()) {
						fromX = s1.getX() + diagnoalDifference;
						fromY = s1.getY() + diagnoalDifference;
						toX = s2.getX() + STATES_DIAMETER - diagnoalDifference;
						toY = s2.getY() + STATES_DIAMETER - diagnoalDifference;
						arrow1X = toX;
						arrow1Y = toY + arrowLength;
						arrow2X = toX + arrowLength;
						arrow2Y = toY;
					} else if (s2.getY() == s1.getY()) {
						fromX = s1.getX() + diagnoalDifference;
						fromY = s1.getY() + STATES_DIAMETER - diagnoalDifference;
						toX = s2.getX() + STATES_DIAMETER - diagnoalDifference;
						toY = s2.getY() + STATES_DIAMETER - diagnoalDifference;
						arrow1X = toX;
						arrow1Y = toY + arrowLength;
						arrow2X = toX + arrowLength;
						arrow2Y = toY;
						yCurve = curveValue;
					} else if (s2.getY() > s1.getY()) {
						fromX = s1.getX() + diagnoalDifference;
						fromY = s1.getY() + STATES_DIAMETER - diagnoalDifference;
						toX = s2.getX() + STATES_DIAMETER - diagnoalDifference;
						toY = s2.getY() + diagnoalDifference;
						arrow1X = toX;
						arrow1Y = toY - arrowLength;
						arrow2X = toX + arrowLength;
						arrow2Y = toY;
					}
				} else if (s2.getX() == s1.getX()) {
					if (s2.getY() < s1.getY()) {
						fromX = s1.getX() + diagnoalDifference;
						fromY = s1.getY() + diagnoalDifference;
						toX = s2.getX() + diagnoalDifference;
						toY = s2.getY() + STATES_DIAMETER - diagnoalDifference;
						xCurve = -curveValue;
						arrow1X = toX;
						arrow1Y = toY + arrowLength;
						arrow2X = toX - arrowLength;
						arrow2Y = toY;
					} else if (s2.getY() == s1.getY()) {
						fromX = s1.getX() + diagnoalDifference;
						fromY = s1.getY() + STATES_DIAMETER - diagnoalDifference;
						toX = s2.getX() + STATES_DIAMETER - diagnoalDifference;
						toY = s2.getY() + STATES_DIAMETER - diagnoalDifference;
						yCurve = 60;
						arrow1X = toX - arrowShadow;
						arrow1Y = toY + arrowShadow;
						arrow2X = toX;
						arrow2Y = toY + arrowLength;
					} else if (s2.getY() > s1.getY()) {
						fromX = s1.getX() + STATES_DIAMETER - diagnoalDifference;
						fromY = s1.getY() + STATES_DIAMETER - diagnoalDifference;
						toX = s2.getX() + STATES_DIAMETER - diagnoalDifference;
						toY = s2.getY() + diagnoalDifference;
						xCurve = curveValue;
						arrow1X = toX;
						arrow1Y = toY - arrowLength;
						arrow2X = toX + arrowLength;
						arrow2Y = toY;
					}

				} else if (s2.getX() > s1.getX()) {
					if (s2.getY() < s1.getY()) {
						fromX = s1.getX() + STATES_DIAMETER - diagnoalDifference;
						fromY = s1.getY() + diagnoalDifference;
						toX = s2.getX() + diagnoalDifference;
						toY = s2.getY() + STATES_DIAMETER - diagnoalDifference;
						arrow1X = toX;
						arrow1Y = toY + arrowLength;
						arrow2X = toX - arrowLength;
						arrow2Y = toY;
					} else if (s2.getY() == s1.getY()) {
						fromX = s1.getX() + STATES_DIAMETER - diagnoalDifference;
						fromY = s1.getY() + diagnoalDifference;
						toX = s2.getX() + diagnoalDifference;
						toY = s2.getY() + diagnoalDifference;
						yCurve = -curveValue;
						arrow1X = toX;
						arrow1Y = toY - arrowLength;
						arrow2X = toX - arrowLength;
						arrow2Y = toY;
					} else if (s2.getY() > s1.getY()) {
						fromX = s1.getX() + STATES_DIAMETER - diagnoalDifference;
						fromY = s1.getY() + STATES_DIAMETER - diagnoalDifference;
						toX = s2.getX() + diagnoalDifference;
						toY = s2.getY() + diagnoalDifference;
						arrow1X = toX;
						arrow1Y = toY - arrowLength;
						arrow2X = toX - arrowLength;
						arrow2Y = toY;
					}
				}

				String text = transitions.get(i).getLetter() + "";
				for (int j = 0; j < transitions.size(); j++) {
					if (transitions.get(i).getFrom().equals(transitions.get(j).getFrom())
							&& transitions.get(i).getTo().equals(transitions.get(j).getTo())
							&& transitions.get(i).getLetter() != transitions.get(j).getLetter()) {
						text += "," + transitions.get(j).getLetter();
						if (j < i)
							continue mainFor;
					}
				}

				int middleX = 0, middleY = 0;
				middleX = (fromX + toX) / 2;
				middleY = (fromY + toY) / 2;

				QuadCurve2D.Double curve = new QuadCurve2D.Double(fromX, fromY, middleX + xCurve,
						middleY + yCurve, toX, toY);
				g2.draw(curve);

				g2.drawLine(toX, toY, arrow1X, arrow1Y);
				g2.drawLine(toX, toY, arrow2X, arrow2Y);

				int labelCenterX = middleX + xCurve / 2 + (13 * (int) Math.signum(xCurve));
				int labelCenterY = middleY + yCurve / 2 + (13 * (int) Math.signum(yCurve));

				int textWidth = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
				int textHeight = (int) g2.getFontMetrics().getStringBounds(text, g2).getHeight();

				int textX = labelCenterX - textWidth / 2;
				int textY = labelCenterY + textHeight / 2;

				g.drawString(text, textX, textY);
			}
			g2.drawLine(startingState.getX() - 40, startingState.getY() + (STATES_DIAMETER / 2),
					startingState.getX(), startingState.getY() + (STATES_DIAMETER / 2));
			g2.drawLine(startingState.getX(), startingState.getY() + (STATES_DIAMETER / 2),
					startingState.getX() - 10, startingState.getY() + (STATES_DIAMETER / 2) - 6);
			g2.drawLine(startingState.getX(), startingState.getY() + (STATES_DIAMETER / 2),
					startingState.getX() - 10, startingState.getY() + (STATES_DIAMETER / 2) + 6);
		}

	}
}
