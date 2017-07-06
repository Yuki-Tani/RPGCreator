package system.tool;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import system.PictureBox;
import system.PictureInfo;
import system.SettingValues;


public class ManifestFileReader {
	private static final String MF_EXTENSION 	= SettingValues.MF_EXTENSION,
								PIC_EXTENSION	= SettingValues.PIC_EXTENSION;
	private static final int BACK 				= SettingValues.BACK,
							 FORE 				= SettingValues.FORE;
	private static final char BACK_C 			= SettingValues.BACK_C,
							  FORE_C 			= SettingValues.FORE_C;
	private static final char NAME_PIN 			= SettingValues.NAME_PIN,
							  SERIAL_CODE_START_PIN = SettingValues.SERIAL_CODE_START_PIN,
							  SERIAL_CODE_END_PIN	= SettingValues.SERIAL_CODE_END_PIN,
							  DIM_CENTER_PIN 	= SettingValues.DIM_CENTER_PIN,
							  ITEM_END_PIN		= SettingValues.ITEM_END_PIN,
							  VERTEX_START_PIN 	= SettingValues.VERTEX_START_PIN,
							  VERTEX_CENTER_PIN	= SettingValues.VERTEX_CENTER_PIN,
							  VERTEX_END_PIN	= SettingValues.VERTEX_END_PIN,
							  PASSAGE_START_PIN = SettingValues.PASSAGE_START_PIN,
							  PASSAGE_END_PIN	= SettingValues.PASSAGE_END_PIN;
	private static final String END_CODE_NAME 	= SettingValues.END_CODE_NAME;
	
	private PictureBox box;
	private BufferedReader reader;
	
	public ManifestFileReader(PictureBox box){
		this.box = box;
		System.out.println("Default CharCode: " + System.getProperty("file.encoding")+"(MFR)");
	}
	
	
	public void readMF(File mf, File[] pictures){ 
		if(!MFCheck(mf)){
			System.out.println("MF extension error (MFR)");
			return;
		}
		try{
			FileInputStream input = new FileInputStream(mf);  
            InputStreamReader stream = new InputStreamReader(input,SettingValues.CHAR_CODE);
            reader = new BufferedReader(stream);
		}catch(Exception e){
			System.out.println("MF reader not made error (MFR)");
			return;
		}	
		
		box.startPart();							// write start index of this folder.
		
		LinkedList<Point> vertexes = new LinkedList<Point>();
		LinkedList<Integer> positions = new LinkedList<Integer>();
		LinkedList<Integer> ranks = new LinkedList<Integer>();
		LinkedList<boolean[]> passages = new LinkedList<boolean[]>();
	    // read phase
		String name = readString(reader,NAME_PIN);							//read "others"+"NAME"+':'
		while(!name.equals(END_CODE_NAME)){
			
			System.out.println("Read Pic(MFR)");
			System.out.print("   name:"+ name);
			if(!readPin(reader,SERIAL_CODE_START_PIN)){
				System.out.println("MF non-serial error(MFR)");
				return;
			}
			String serialCode = readString(reader,SERIAL_CODE_END_PIN);	//read "[XXXXXXX]"
			System.out.println("  serial:"+serialCode);
			Point dim = readPoint(reader,DIM_CENTER_PIN,ITEM_END_PIN);	//read "XX"+'x'+"XX"+','
			System.out.println("   dimension: "+ dim.x +"x"+dim.y);
			while(readPin(reader,VERTEX_START_PIN)){				//read '(' or '\n'
				vertexes.add(readPoint(reader,VERTEX_CENTER_PIN,VERTEX_END_PIN));
																	//read "x"+','+"y"+')'
				positions.add(readPosition(reader));				//read "P"
				ranks.add(readSingleInt(reader));						//read "r"
				if(readPin(reader,PASSAGE_START_PIN)){				//read '<' or ','
					passages.add(readPassage(reader,PASSAGE_END_PIN));
																	//read "0" + ',' + '>'
					readPin(reader,ITEM_END_PIN);					//read ',' 
				}else{
					boolean[] defPassage = {true,true,true,true};
					passages.add(defPassage);
				}
			}
///////////			
			for (int i=0;i<vertexes.size();i++) {
				System.out.println("   vertex: ("+vertexes.get(i).x+","+vertexes.get(i).y+")");
				System.out.println("     position: "+ positions.get(i)+"   rank: "+ranks.get(i));
				System.out.print("     passage: <");
				for(int k=0;k<4;k++){
					System.out.print(passages.get(i)[k]);
					if(k != 3) System.out.print(",");
					else	   System.out.println(">");
				}
			}
///////////			
			// make PictureInfo -> 
			BufferedImage pic = searchPicture(pictures,name);
			if(pic != null){
				box.addPictureInfo(new PictureInfo(serialCode,pic,dim, vertexes.toArray(new Point[0]),
											positions.toArray(new Integer[0]), ranks.toArray(new Integer[0]),
											passages.toArray(new boolean[0][0]) ));
			}
			vertexes.clear();
			positions.clear();
			ranks.clear();
			passages.clear();
			name = readString(reader,NAME_PIN);
		}
		try{
			reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public boolean MFCheck(File file){
		return (file.isFile() && file.getPath().endsWith(MF_EXTENSION));
	}
	private BufferedImage searchPicture(File[] pictures,String name){
		for (File file : pictures) {
			if(file.isFile() && file.getName().equals(name+PIC_EXTENSION)){
				try{
					return ImageIO.read(file);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		System.out.println("<Error> Pic\""+name+"\""+" is not found(MFR)");
		return null;
	}
//read methods	
	private String readString(BufferedReader reader,char pin){
		StringBuffer chars = new StringBuffer();
		try{
			int c = reader.read();
			while(c != -1 && (char)c != pin){
				if(Character.isLetterOrDigit((char)c)){
					chars.append((char)c);
				}
				c = reader.read();
			}
			return chars.toString();
		}catch(Exception e){
			System.out.println("MF error(MFR-readString())");
			return null;
		}
	}
	private boolean readPin(BufferedReader reader,char startPin){
		try{
			return((char)reader.read() == startPin);
		}catch(Exception e){
			System.out.println("MF error(MFR-readPin())");
			return false;
		}
	}
	private Point readPoint(BufferedReader reader,char centerPin,char endPin){
		try{
			return new Point(
					Integer.parseInt(readString(reader,centerPin)),
					Integer.parseInt(readString(reader,endPin))
					);
			
		}catch(Exception e){
			System.out.println("MF error(MFR-readPoint())");
			return null;
		}
	}
	private int readPosition(BufferedReader reader){
		try{
			char posiC = (char)reader.read();
			int position = BACK;
			switch(posiC){
				case BACK_C: position = BACK; break;
				case FORE_C: position = FORE; break;
				default: System.out.println("MF position errore(MFR)");
			}
			return position;
		}catch(Exception e){
			System.out.println("MF error(MFR-readPosition())");
			return -1;
		}
	}
	private int readSingleInt(BufferedReader reader){
		try{
			char n = (char)reader.read();
			return Integer.parseInt(String.valueOf(n));
		}catch(Exception e){
			System.out.println("MF error(MFR-readSingleInt)");
			return -1;
		}
	}
	private boolean[] readPassage(BufferedReader reader,char endPin){
			boolean[] passage = new boolean[4];
			for(int i=0;i<4;i++){
				switch(readSingleInt(reader)){
				case 0: passage[i] = true; break;
				case 1: passage[i] = false; break;
				default: System.out.println("MF error(MFR-readPassage)");
				}
				if(i==3 && !readPin(reader,PASSAGE_END_PIN)){
					System.out.println("MF endPin error(MFR-readPassage)");
				}
			}
			return passage;
	}
}
