package system;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import system.tool.ManifestFileReader;

// This class manages pictures.
// Pictures are managed as "PictureInfo".
// Only by inputing "root picture folder" and making an instance of this class, all pictures can be used. 
public class PictureBox {
	
	// this list put and ordered all PictureInfoes.
	// partitions has start index of folder.
	private ArrayList<PictureInfo> pictureInfoes;
	private ArrayList<Integer>	  partitions;
	// this HashMap is translate serialCode to pictureInfoes.
	private HashMap<String,PictureInfo> serialKeyMap;
	private ManifestFileReader	  reader;
	
	public PictureBox(File picFolder) {
		pictureInfoes = new ArrayList<PictureInfo>();
		partitions = new ArrayList<Integer>();
		serialKeyMap = new HashMap<String,PictureInfo>();		
		reader = new ManifestFileReader(this);
		
		readPictureFolder(picFolder);
		System.out.println("partition :" + partitions.size());
	}
	public PictureInfo getPicInfo(String serialCode){
			return serialKeyMap.get(serialCode);
	}		
	public LinkedList<PictureInfo> getPicInfoAll(){
		return new LinkedList<PictureInfo>(pictureInfoes);
	}
	
	// read all MF and make PictureInfo under the folder and the child folders.
	// All PictureInfo are set in ArrayList "pictureInfoes" with their own indexes.
	// "partitions" indicates start index of each folder.
	private void readPictureFolder(File folder){
		System.out.println("readPicFol(PB) ->" + folder.getName());
		if(!folder.isDirectory()) return;
		File[] files = folder.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()){
					readPictureFolder(files[i]);
				}else if(reader.MFCheck(files[i])){
					reader.readMF(files[i],files); // readMF
				}
			}
		}	
	}
	public void startPart(){
		partitions.add(pictureInfoes.size());
	}
	public void addPictureInfo(PictureInfo info){
		serialKeyMap.put(info.getSerialCode(),info);
		pictureInfoes.add(info);
	}
}
