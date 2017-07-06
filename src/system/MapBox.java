package system;

import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;

public class MapBox {
	
	private static int defMapW = 50, defMapH = 20;
	private int mapNum;
	
	private LinkedList<MapInfo> mapBox;
	private LinkedList<String> names;
	
	
	public MapBox(){
		mapNum = 0;
		mapBox = new LinkedList<MapInfo>();
		names = new LinkedList<String>();
	}
	
	
	/*public void setMapPic(int mapIndex,Point p,short topicIndex,short imageIndex ){
		MapInfo mapinfo = mapBox.get(mapIndex);
		System.out.println("from "+mapIndex+" >(MB)");
		mapinfo.setMapPic(p, topicIndex, imageIndex);
	}*/
	public MapInfo newMap(String name, int w, int h){
		String avName = avoidName(name);
		MapInfo mapInfo = new MapInfo(avName,w,h);
		mapBox.add(mapInfo);
		names.add(avName);
		mapNum ++;
		return mapInfo;
	}
	public MapInfo newMap(int w,int h){
		return newMap(getDefName(), w, h);
	}
	public MapInfo newMap(){
		return newMap(defMapW,defMapH);
	}
	
	public boolean changeMapName(String before,String after){
		if(!names.contains(before)) return false;
		int index = getIndex(before);
		String avAfter = avoidName(after);
		names.set(index, avAfter);
		mapBox.get(index).changeName(avAfter);
		return true;
	}
	public void deleteMap(String name){
		mapBox.remove(names.indexOf(name));
		names.remove(name);
		mapNum --;
	}
	
//get methods
	public int getIndex(String name){
		int index = -1;
		int size = names.size();
		for(int i=0;i<size;i++){
			String current = names.poll();
			if(current.equals(name)) index = i;
			names.offer(current);
		}
		return index;
	}
	public String getName(int index){
		return names.get(index);
	}
	public String getLastName(){
		return names.getLast();
	}
	
	public MapInfo getInfo(String name){
		return mapBox.get(getIndex(name));
	}
	
	public int getMapNum(){
		return mapNum;
	}
	public String[] getMapNames(){
		return names.toArray(new String[0]);
	}
	
	public int getDefMapW(){return defMapW;}
	public int getDefMapH(){return defMapH;}
	public String getDefName(){return "No"+ (mapNum+1);}
	
//private methods	
	private String avoidName(String name){
		String avName = name;
		int i = 1;
		while(names.contains(avName)){
			i ++;
			avName = name +"("+ i +")";
		}
		return avName;
	}
}
