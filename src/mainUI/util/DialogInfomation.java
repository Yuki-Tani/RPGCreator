package mainUI.util;

public class DialogInfomation {
	
	private Object[] inputs;
	private String command;
	
	public DialogInfomation(){
		inputs = null;
		command = "##System##default";
	}
	
//get methods	
	public Object[] getInputs(){
		return inputs;
	}
	public Object getObject(int index){
		return inputs[index];
	}
	public String getString(int index){
		if(index>=inputs.length||index<0) return "##SYSTEM## IndexError(DI)";
		return (String)inputs[index];
	}
	public int getInt(int index){
		if(index>=inputs.length) return -99999;
		try{
			Integer.parseInt((String)inputs[index]);
		}catch(NumberFormatException e){
			return -99999;
		}
		return Integer.parseInt((String)inputs[index]);
	}
//
	public void insertInputs(Object[] inputs){
		this.inputs = inputs;
	}
//command methods	
	public void insertCommand(String command){
		this.command = command;
	}
	public String getInfomationCommand(){
		return command;
	}
	
}
