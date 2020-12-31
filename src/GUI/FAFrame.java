package GUI;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import Core.FA;
import Core.DFA.DFABuilder;
import Core.NFA.NFA;
import Core.NFA.NFABuilder;

/**
 * 
 * This class is the main frame of the application. You just need to initialize
 * it with it's constructor and boom... You're ready to go!
 * 
 * @author Usama Sarwar
 * 
 * 
 */
public class FAFrame extends JFrame {

	private static final long serialVersionUID = -6559694410472964579L;

	public FAFrame() {
		super("Usama Sarwar's FA Drawer");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		setPreferredSize(new Dimension(900, 600));
		pack();
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		init();
		setVisible(true);
	}

	/**
	 * Initializes components of the JFrame.
	 * 
	 * @author Usama Sarwar
	 */
	private void init() {
		final JPanel leftPane = new JPanel();
		JPanel regPan = new JPanel();
		regPan.setBorder(BorderFactory.createTitledBorder("Regular Expression"));
		JLabel enterAlphas = new JLabel("Enter language alphabets, seprated with commas:");
		final JTextField alphaText = new JTextField("a,b");
		alphaText.setPreferredSize(new Dimension(250, (int) alphaText.getPreferredSize()
				.getHeight()));

		JLabel enterRegex = new JLabel("Enter regular expression:");
		final JTextField regexText = new JTextField("a*b*");
		regexText.setPreferredSize(new Dimension(250, (int) regexText.getPreferredSize()
				.getHeight()));
		JPanel faTypePan = new JPanel();
		faTypePan.setBorder(BorderFactory.createTitledBorder("FA Type"));
		JLabel chooseFAType = new JLabel("Choose FA type:");
		ButtonGroup gp = new ButtonGroup();
		JRadioButton nfaButton = new JRadioButton("NFA");
		nfaButton.setSelected(true);
		final JRadioButton dfaButton = new JRadioButton("DFA");
		final JRadioButton minidfaButton = new JRadioButton("Minimized DFA");
		// minidfaButton.setEnabled(false);
		gp.add(nfaButton);
		gp.add(dfaButton);
		gp.add(minidfaButton);
		final JButton plotButton = new JButton("Plot!");
		// plotButton.setEnabled(false);
		final JPanel rightPane = new JPanel();
		rightPane.setBorder(BorderFactory.createTitledBorder("Finite Automaton"));
		final FAPanel faPanel = new FAPanel();

		regPan.setLayout(new VerticalFlowLayout());
		regPan.add(enterAlphas);
		regPan.add(alphaText);
		regPan.add(enterRegex);
		regPan.add(regexText);
		faTypePan.setLayout(new VerticalFlowLayout());
		faTypePan.add(chooseFAType);
		faTypePan.add(nfaButton);
		faTypePan.add(dfaButton);
		faTypePan.add(minidfaButton);

		leftPane.setLayout(new VerticalFlowLayout());
		leftPane.add(regPan);
		leftPane.add(faTypePan);
		leftPane.add(plotButton);

		rightPane.setLayout(new FlowLayout());
		rightPane.add(faPanel);

		getContentPane().setLayout(new SpringLayout());
		getContentPane().add(leftPane);
		getContentPane().add(rightPane);

		regexText.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				if (regexText.getText().trim().equals("") || alphaText.getText().trim().equals("")) {
					plotButton.setEnabled(false);
				} else
					plotButton.setEnabled(true);
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				if (regexText.getText().trim().equals("")) {
					plotButton.setEnabled(false);
				} else
					plotButton.setEnabled(true);
			}

			@Override
			public void changedUpdate(DocumentEvent arg0) {

			}
		});

		alphaText.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				if (regexText.getText().trim().equals("") || alphaText.getText().trim().equals("")) {
					plotButton.setEnabled(false);
				} else
					plotButton.setEnabled(true);
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				if (regexText.getText().trim().equals("")) {
					plotButton.setEnabled(false);
				} else
					plotButton.setEnabled(true);
			}

			@Override
			public void changedUpdate(DocumentEvent arg0) {

			}
		});

		getContentPane().addComponentListener(new ComponentListener() {

			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentResized(ComponentEvent e) {
				rightPane.setSize(getContentPane().getWidth() - leftPane.getWidth() - 15,
						getContentPane().getHeight() - 15);
				rightPane.setPreferredSize(new Dimension(getContentPane().getWidth()
						- leftPane.getWidth() - 15, getContentPane().getHeight() - 15));
				rightPane.setLocation(leftPane.getWidth() + 5, getContentPane().getY() + 5);
				faPanel.setSize(rightPane.getWidth() - 40, rightPane.getHeight() - 50);
				faPanel.setPreferredSize(new Dimension(rightPane.getWidth() - 40, rightPane
						.getHeight() - 50));
				faPanel.setLocation(20, 30);
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub

			}
		});

		plotButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String alphabets = alphaText.getText();
					String regex = regexText.getText();
					String[] alphas = alphabets.split(",");
					char[] realAlhpas = new char[alphas.length];
					for (int i = 0; i < realAlhpas.length; i++) {
						realAlhpas[i] = alphas[i].trim().charAt(0);
					}
					FA fa = null;
					NFABuilder nfaBuilder = new NFABuilder(regex, realAlhpas);
					fa = nfaBuilder.toNFA();
					if (dfaButton.isSelected()) {
						NFA nfa = (NFA) fa;
						DFABuilder dfaBuilder = new DFABuilder(nfa);
						fa = dfaBuilder.getDFAfromNFA();
					}
					if (minidfaButton.isSelected()) {
						NFA nfa = (NFA) fa;
						DFABuilder dfaBuilder = new DFABuilder(nfa);
						DFABuilder minBuilder = new DFABuilder(dfaBuilder.getDFAfromNFA());
						fa = minBuilder.getMinimizedDFAfromDFA();
					}
					faPanel.setFA(fa);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
	}
}
