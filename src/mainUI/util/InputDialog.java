package mainUI.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class InputDialog extends JFrame implements ActionListener{

//////////////////////////////////////////////////////
//////////////////////////////////////////////////////	
	
	private Color back = Color.DARK_GRAY,
				  fore = Color.WHITE;
	
	private int fontSize = 20;
	
	private Insets inset = new Insets(0,fontSize,8,0);
	
	private int inputW = fontSize*10;
	private int inputH = fontSize*3/2;
	private int labelW = fontSize*6;
	
///////////////////////////////////////////////////////	
///////////////////////////////////////////////////////
	private DialogInfomation dialogInfo;
	
	private JPanel panel;
	private GridBagLayout layout;
	private GridBagConstraints co;
	
	private DialogListener listener = null;
	private LinkedList<Component> inputList;
	private LinkedList<Component> intCheck;
	
	public InputDialog(String name){
		super(name);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(500,400);
	
		dialogInfo = new DialogInfomation();	
		
		inputList = new LinkedList<Component>();
		intCheck = new LinkedList<Component>();
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
	public void addDialogListener(DialogListener listener){
		this.listener = listener;
	}
	public void setInfomationCommand(String command){
		dialogInfo.insertCommand(command);
	}
	public void optimizeSize(){
		setSize(inputW+(labelW+inset.right+inset.left)*2 +10,
				(inputH+inset.top+inset.bottom)*(co.gridy+1) +35);
	}
	public void setFontSize(int size){
		fontSize = size;
	}
	
//add methods
	public void addBlank(){putLine();}
	
	public void addTextInput(String itemName){ addTextInput(itemName,"");}
	public void addTextInput(String itemName,String defText){
		JLabel nameLabel = makeNameLabel(itemName);
		JTextField field = new JTextField(defText);
		inputList.offer(field);
		
		adaptForm(field,inputW,inputH);
		
		putLine(nameLabel,field);	
	}
	
	public void addSizeInput(String itemName){addSizeInput(itemName,0,0);}
	public void addSizeInput(String itemName,int initial1,int initial2){
		JLabel nameLabel = makeNameLabel(itemName);
		JPanel inputs = new JPanel(new GridLayout());
		inputs.setBackground(back);
		JTextField field1 = new JTextField(initial1+"");
		JTextField field2 = new JTextField(initial2+"");
		inputList.offer(field1);
		inputList.offer(field2);
		intCheck.offer(field1);
		intCheck.offer(field2);
		adaptForm(field1,fontSize*3,inputH);
		adaptForm(field2,fontSize*3,inputH);
		JLabel x = new JLabel(" X ");
		adaptForm(x);
		x.setForeground(fore);
		
		
		inputs.add(field1);
		inputs.add(x);
		inputs.add(field2);
		
		putLine(nameLabel,inputs);
	}
	
	public void addButtons(){addButtons(true);}
	public void addButtons(boolean defButton){
		JPanel buttons = new JPanel();
		JButton ok = new JButton("OK");
		JButton cancel = new JButton("Cancel");
		
		ok.addActionListener(this);
		ok.setActionCommand("ok");
		cancel.addActionListener(this);
		cancel.setActionCommand("cancel");
		if(defButton) getRootPane().setDefaultButton(ok);
		
		buttons.setBackground(back);
		buttons.add(ok);
		buttons.add(cancel);
		
		co.gridy ++;
		co.gridx = 1;
		co.anchor = GridBagConstraints.SOUTHEAST;
		co.weighty = 1;
		layout.setConstraints(buttons, co);
		panel.add(buttons);
		co.anchor = GridBagConstraints.WEST;
		co.weighty = 0;
	}
//private methods	
	private JLabel makeNameLabel(String name){
		JLabel nameLabel = new JLabel();
		adaptForm(nameLabel);
		nameLabel.setText(name);
		nameLabel.setForeground(fore);
		nameLabel.setPreferredSize(new Dimension(labelW,inputH));	
		return nameLabel;
	}
	private void putLine(JLabel nameLabel,Component input){
		co.weightx = 0.0d;
		co.gridy ++;
		co.gridx = 0;
		layout.setConstraints(nameLabel,co);
		panel.add(nameLabel);
		
		co.weightx = 1.0d;
		co.gridx = 1;
		layout.setConstraints(input, co);
		panel.add(input);
	}
	private void putLine(){
		putLine(makeNameLabel(""),new JLabel());
	}
	
	private void adaptForm(JComponent comp){
		comp.setFont(new Font(Font.DIALOG,Font.PLAIN,fontSize));
	}
	private void adaptForm(JComponent comp,int w,int h){
		adaptForm(comp);
		comp.setPreferredSize(new Dimension(w,h));
	}
//Event methods
	public void actionPerformed(ActionEvent e){
		if(listener == null) return;
		String command = e.getActionCommand();
		if(command.equals("ok")){
			if(!blankCheck()){
				JOptionPane.showMessageDialog(this,"No Blank");
				return;
			}
			if(!integerCheck()){
				JOptionPane.showMessageDialog(this,"Not Number");
				return;
			}
			
			Object[] inputs = new Object[inputList.size()];
			for (int i = 0;i<inputs.length;i++) {
				Component field = inputList.poll();
				if(field.getClass().getName().equals("javax.swing.JTextField")){
					inputs[i] = ((JTextField) field).getText();
				}	
			} 
			dialogInfo.insertInputs(inputs);
			listener.fromDialog(dialogInfo);
			dispose();
			
		}else if(command.equals("cancel")){
			dispose();
		}
	}
	
	private boolean integerCheck(){
		LinkedList<Component> saucer = new LinkedList<Component>();
		boolean judge = true;
		while(intCheck.peek() != null){
			if(intCheck.peek().getClass().getName().equals("javax.swing.JTextField")){
				try{
					Integer.parseInt(((JTextField) intCheck.peek()).getText());
				}catch(NumberFormatException e){
					judge = false;
				}	
			}
			saucer.offer(intCheck.poll());
		}
		intCheck = saucer;
		return judge;
	}
	private boolean blankCheck(){
		LinkedList<Component> saucer = new LinkedList<Component>();
		boolean judge = true;
		while(inputList.peek() != null){
			if(inputList.peek().getClass().getName().equals("javax.swing.JTextField")){
				if(((JTextField)inputList.peek()).getText().equals("")){
					judge = false;
				}
			}
			saucer.offer(inputList.poll());
		}
		inputList = saucer;
		return judge;
	}
}
