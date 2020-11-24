package The_proposed_method;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.TreeMap;

public class saveMap {

	public static void saveMapTxt(TreeMap<Integer, Double> map, String path) {
		OutputStreamWriter outFile  = null;
		FileOutputStream fileName;
		try {
			fileName = new FileOutputStream(path);
			outFile = new OutputStreamWriter(fileName);
			String str = "";
			for(int key : map.keySet()) {
				str += key+":"+map.get(key)+"\n";
			}
			outFile.write(str);
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			try {
				outFile.flush();
				outFile.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	public static void saveIntTxt(HashMap<Integer, Integer> map, String path) {
		OutputStreamWriter outFile  = null;
		FileOutputStream fileName;
		try {
			fileName = new FileOutputStream(path);
			outFile = new OutputStreamWriter(fileName);
			String str = "";
			for(int key : map.keySet()) {
				str += key+":"+map.get(key)+"\n";
			}
			outFile.write(str);
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			try {
				outFile.flush();
				outFile.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static void saveLongMapTxt(TreeMap<Integer, Long> map, String path) {
		OutputStreamWriter outFile  = null;
		FileOutputStream fileName;
		try {
			fileName = new FileOutputStream(path);
			outFile = new OutputStreamWriter(fileName);
			String str = "";
			for(int key : map.keySet()) {
				str += key+":"+map.get(key)+"\n";
			}
			outFile.write(str);
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			try {
				outFile.flush();
				outFile.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void saveDoubleMapTxt(TreeMap<Double, Double> map, String path) {
		OutputStreamWriter outFile  = null;
		FileOutputStream fileName;
		try {
			fileName = new FileOutputStream(path);
			outFile = new OutputStreamWriter(fileName);
			String str = "";
			for(Double key : map.keySet()) {
				str += key+":"+map.get(key)+"\n";
			}
			outFile.write(str);
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			try {
				outFile.flush();
				outFile.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}
