package system;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import system.tool.PicAddress;

public class MapCellInfo {
	
	//position-rank: B-0 <only 1 image>
	
	private static final int NORTH = 0,
							 EAST = 1,
							 SOUTH = 2,
							 WEST = 3;
	private static final int BACK = SettingValues.BACK,
							 FORE = SettingValues.FORE;
	// put (picAddress,cellIndex) 
	// always sorted by rank. 
	private ArrayList<PicAddress> back ,
							      fore ;
	// for sort
	private ArrayList<Integer>  backRanks,
							    foreRanks;
	// put cell coordinates of links
	private HashMap<PicAddress,Point> links; // <address,link>

	
	private boolean[] passages =  new boolean[4]; // {up,right,down,left}
	
	public MapCellInfo(){
		back = new ArrayList<PicAddress>();
		fore = new ArrayList<PicAddress>();
		backRanks = new ArrayList<Integer>();
		foreRanks = new ArrayList<Integer>();
		links = new HashMap<PicAddress,Point>();
		
		for (boolean pass: passages) {
			pass = true;
		}
	}
//get methods
	public PicAddress[] getBackPicAdds(){
		return back.toArray(new PicAddress[0]);
	}
	public PicAddress[] getForePicAdds(){
		return fore.toArray(new PicAddress[0]);
	}
	public boolean getPassage(int direction){
		return passages[direction];
	}
	public PicAddress getTopPicAdd(){
		if(fore.size() != 0){
			return fore.get(fore.size()-1);
		}else if(back.size() != 0){
			return back.get(back.size()-1);
		}else{
			return null;
		}
	}
	public boolean contains(PicAddress address){
		for (int i = 0; i < fore.size(); i++) {
			if(fore.get(i).equals(address)) return true;
		}
		for (int i = 0; i < back.size(); i++) {
			if(back.get(i).equals(address)) return true;
		}
		return false;
	}
	
//set methods	
	public void setPassage(int direction, boolean passage){
		passages[direction] = passage;
	}
	
	public void setPicAdd(int position, int rank ,PicAddress address){
		setPicAdd(position,rank,address,new Point(0,0));
	}
	public void setPicAdd(int position, int rank, PicAddress address,Point link){
		ArrayList<PicAddress> addresses;
		ArrayList<Integer> rankList;
		switch(position){
			case BACK: addresses = back;
					   rankList = backRanks;
					break;
			case FORE: addresses = fore;
					   rankList = foreRanks;
					break;
			default: return;			
		}
		
		if(addresses.contains(address)){
			System.out.println("avoid overlap(MCI)");
			return;						   //avoid overlap
		}
		
		if(position == BACK && rank == 0){ //B-0 is only 1
			if(rankList.contains(0)) removePicAdd(position,0);
		}
		int size = addresses.size();
		int index = 0;
		while(index<size && rank>=rankList.get(index)){
			index ++;
		}
		addresses.add(index,address);	
		rankList.add(index,rank);
		links.put(address,link);
		System.out.println("set Pic("+addresses.get(index).getSerial()+","+addresses.get(index).getIndex()+")  (MCI)");
		System.out.println("      rank:"+rankList.get(index)+"  link:("+links.get(address).x+","+link.y+")");
	}
//remove methods<return chain Point or null>	
	
	public Point removePicAdd(int position,int index){
		Point chain = new Point();
		if(position == BACK){
			if(index >= back.size()) return null;
			chain = links.remove(back.get(index));
			back.remove(index);
			backRanks.remove(index);
		}else if(position == FORE){
			if(index >= fore.size()) return null;
			chain = links.remove(fore.get(index));
			fore.remove(index);
			foreRanks.remove(index);
		}
		System.out.println("(MCI) remove (chain:("+chain.x+","+chain.y+"))");
		return chain;
	}
	// remove PicAddress and return link point or null
	public Point removePicAdd(PicAddress address){
		if(fore.contains(address)){
			foreRanks.remove(fore.indexOf(address));
			fore.remove(address);
		}else if(back.contains(address)){
			backRanks.remove(back.indexOf(address));
			back.remove(address);
		}
		System.out.println("(MCI) remove"+" contain:"+links.get(address));
		return links.remove(address);
	}
}
