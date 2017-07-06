package mainUI.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class ChoiceDialog extends JFrame implements ActionListener {
	//////////////////////////////////////////////////////
	//////////////////////////////////////////////////////

	private Color back = Color.DARK_GRAY, fore = Color.WHITE;

	private int fontSize = 20;

	private Insets inset = new Insets(0, fontSize, 8, 0);

	private int labelW = fontSize * 10;
	private int labelH = fontSize * 3 / 2;
	private int iconW = 10;

	///////////////////////////////////////////////////////
	///////////////////////////////////////////////////////
	private DialogInfomation dialogInfo;
	
	private JPanel panel;
	private GridBagLayout layout;
	private GridBagConstraints co;

	private boolean moreChoice;
	private LinkedList<JCheckBox> labelList;

	private DialogListener listener = null;

	public ChoiceDialog(String name) {
		super(name);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(500, 400);
		dialogInfo = new DialogInfomation();
		moreChoice = false;
		labelList = new LinkedList<JCheckBox>();
		layout = new GridBagLayout();
		co = new GridBagConstraints();
		co.gridy = 0;
		co.insets = inset;
		co.anchor = GridBagConstraints.WEST;

		panel = new JPanel(layout);
		panel.setBackground(back);
		panel.setForeground(fore);
		add(panel);

	}

	public void addDialogListener(DialogListener listener) {
		this.listener = listener;
	}

	public void optimizeSize() {
		setSize(iconW + labelW * 2 + inset.right + inset.left + 10,
				(labelH + inset.top + inset.bottom) * (co.gridy + 1) + 35);
	}
	
	public void setInfomationCommand(String command){
		dialogInfo.insertCommand(command);
	}

	public void setFontSize(int size) {
		fontSize = size;
	}

	public void setMoreChoice(boolean moreChoice) {
		this.moreChoice = moreChoice;
	}

	// add methods
	public void addBlank() {
		putLine();
	}

	public void addCheckLabel(String name) {
		JCheckBox checkLabel = new JCheckBox();
		adaptForm(checkLabel, labelW + iconW, labelH);
		checkLabel.setText(name);
		checkLabel.setBackground(back);
		checkLabel.setForeground(fore);
		checkLabel.setIconTextGap(fontSize);

		checkLabel.addActionListener(this);
		checkLabel.setActionCommand(name);

		labelList.add(checkLabel);

		putLine(checkLabel);

	}

	public void addButtons() {
		addButtons(true);
	}

	public void addButtons(boolean defButton) {
		if (moreChoice) {
			JPanel buttons = new JPanel();
			JButton ok = new JButton("OK");
			JButton cancel = new JButton("Cancel");

			ok.addActionListener(this);
			ok.setActionCommand("##SYSTEM##ok");
			cancel.addActionListener(this);
			cancel.setActionCommand("##SYSTEM##cancel");
			if (defButton)
				getRootPane().setDefaultButton(ok);

			buttons.setBackground(back);
			buttons.add(ok);
			buttons.add(cancel);

			co.gridy++;
			co.gridx = 1;
			co.anchor = GridBagConstraints.SOUTHEAST;
			co.weighty = 1;
			layout.setConstraints(buttons, co);
			panel.add(buttons);
			co.anchor = GridBagConstraints.WEST;
			co.weighty = 0;
		}
	}

	// private methods

	private void putLine(Component headline) {
		co.weightx = 0;
		co.gridy++;
		co.gridx = 0;
		JLabel blank = new JLabel();
		layout.setConstraints(headline, co);
		panel.add(headline);

		co.gridx = 1;
		co.weightx = 1;
		layout.setConstraints(blank, co);
		panel.add(blank);
		co.weightx = 0;
	}

	private void putLine() {
		JLabel blank = new JLabel();
		adaptForm(blank, labelW, labelH);
		putLine(blank);
	}

	private void adaptForm(JComponent comp) {
		comp.setFont(new Font(Font.DIALOG, Font.PLAIN, fontSize));
	}

	private void adaptForm(JComponent comp, int w, int h) {
		adaptForm(comp);
		comp.setPreferredSize(new Dimension(w, h));
	}

	// Event methods
	public void actionPerformed(ActionEvent e) {
		if(!moreChoice){
			String[] decided = new String[]{e.getActionCommand()};
			dialogInfo.insertInputs(decided);
			listener.fromDialog(dialogInfo);
			dispose();
		}else if(e.getActionCommand().equals("##SYSTEM##ok")){
			LinkedList<String> choices = new LinkedList<String>();
			int length = choices.size();
			for(int i =0;i<length;i++){
				if(labelList.peek().isSelected()){
					choices.offer(labelList.peek().getActionCommand());
				}
				labelList.poll();
			}
			dialogInfo.insertInputs(choices.toArray(new String[0]));
			listener.fromDialog(dialogInfo);
			dispose();
		}else if(e.getActionCommand().equals("##SYSTEM##cancel")){
			dispose();
		}
		
	}

}