package system.tool;

// Picture is specified as (serialCode,picIndex) in this class.
public class PicAddress {
	
	private String serialCode;
	private int picIndex;
	
	public PicAddress(String serialCode ,int picIndex){
		this.serialCode = serialCode;
		this.picIndex = picIndex;
	}
// mutator method
	public void moveZero(){
		picIndex = 0;
	}
	public void translate(int dIndex){
		picIndex = picIndex + dIndex;
	}
//get method
	public String getSerial(){
		return serialCode;
	}
	public int getIndex(){
		return picIndex;
	}
	@Override
	public boolean equals(Object address){
		if(this.getClass() != address.getClass()) return false;
		return serialCode.equals(((PicAddress)address).getSerial()) &&
				picIndex == ((PicAddress)address).getIndex();
	}
}
