package experiment;

import java.util.*;

import Taskmodel.Application;
import Taskmodel.Task;
import The_proposed_method.gathering;
import The_proposed_method.saveMap;
import The_proposed_method.sequencing;
import parser.ParseMechaniser;


public class exp2 {

	public static void main(String[] args) {
		ParseMechaniser pm = new ParseMechaniser();
		HashMap<Integer,double[]> map = new HashMap();
		HashMap<Integer,double[]> map1 = new HashMap();
		HashMap<Integer,Double> mapmax1 = new HashMap();
		HashMap<Integer,Double> mapmax2 = new HashMap();
		HashMap<Integer,Double> mapmin1 = new HashMap();
		HashMap<Integer,Double> mapmin2 = new HashMap();
		TreeMap<Integer,Double> mapmax11 = new TreeMap();
		TreeMap<Integer,Double> mapmax22 = new TreeMap();
		TreeMap<Integer,Double> mapmin11 = new TreeMap();
		TreeMap<Integer,Double> mapmin22 = new TreeMap();
	
		
	
		for(int i = 1;i<866;i++) {
			Application app =  pm.readModel("/Users/miaoshuai/Desktop/thesis/system/rtns18_systems/sys_"+i +"/tgenout/mechaniser.xml");
			double ult = app.getUlt();
			
			ArrayList<ArrayList<Integer>> lists = app.getDep();
			ArrayList<Task> taskset = app.getTaskSet();	
			

			if(ult>1 || !gathering.feasibility(taskset)) {
				continue;
			}
			Collections.sort(taskset, new Comparator<Task>() {
			      @Override
			      public int compare(Task t1, Task t2) {
			        if(t1.getPeriod().getValue() >= t2.getPeriod().getValue()) {
			          return 1;
			        }
			        else {
			          return -1;
			        }
			      }
			    });
			
			
			ArrayList<Task> taskset1 = (ArrayList<Task>) taskset.clone();
			int oldnum = taskset.size();
			
			HashMap<int[], int[]> jobdep = app.getJobdep();
			HashMap<int[], int[]> jobleveldep = sequencing.getjobdep(jobdep);
			gathering.groupDep(taskset,lists,jobleveldep);
			gathering.groupsamePer(taskset);
			
		
			double newnum = 0;
			for(Task t : taskset) {
				if(t!=null) {
					newnum++;
				}
			}
			
			
			gathering.groupsamePer(taskset1);
			
			double newnum1 = 0;
			for(Task t : taskset1) {
				if(t!=null) {
					newnum1++;
			 }
			}
			
			
		//	更新最大值
			if(!mapmax1.containsKey(oldnum)) {
				mapmax1.put(oldnum, newnum);
			}else {
				double t = mapmax1.get(oldnum);
				if(newnum>t) {
					mapmax1.replace(oldnum,newnum);
				}
			}
			
			if(!mapmax2.containsKey(oldnum)) {
				mapmax2.put(oldnum, newnum1);
			}else {
				double t = mapmax2.get(oldnum);
				if(newnum1>t) {
					mapmax2.replace(oldnum,newnum1);
				}
			}
			
			//更新最小值
			
			if(!mapmin1.containsKey(oldnum)) {
				mapmin1.put(oldnum, newnum);
			}else {
				double t = mapmin1.get(oldnum);
				if(newnum<t) {
					mapmin1.replace(oldnum,newnum);
				}
			}
			
			if(!mapmin2.containsKey(oldnum)) {
				mapmin2.put(oldnum, newnum1);
			}else {
				double t = mapmin2.get(oldnum);
				if(newnum1<t) {
					mapmin2.replace(oldnum,newnum1);
				}
			}
			
		
			//更新平均值
			if(!map.containsKey(oldnum)) {
				double[] tmp  = new double[2];
				tmp[0] = 1;
				tmp[1] = newnum;
				map.put(oldnum, tmp);
			}else {
				double[] tmp = map.get(oldnum);
				tmp[0]++;
				tmp[1] += newnum;
				map.replace(oldnum, tmp);
			}
			
			if(!map1.containsKey(oldnum)) {
				double[] tmp  = new double[2];
				tmp[0] = 1;
				tmp[1] = newnum1;
				map1.put(oldnum, tmp);
			}else {
				double[] tmp = map1.get(oldnum);
				tmp[0]++;
				tmp[1] += newnum1;
				map1.replace(oldnum, tmp);
			}
			
		}
		
		
		
		
		Set<Integer> set = map.keySet();
		TreeMap<Integer,Double> map11 = new TreeMap();
		TreeMap<Integer,Double> map22 = new TreeMap();
		ArrayList<Integer> tmp = new ArrayList();
		for(int key : set) {
			tmp.add(key);
		}
		
		Collections.sort(tmp, new Comparator<Integer>() {
		      @Override
		      public int compare(Integer t1, Integer t2) {
		        if(t1 >= t2) {
		          return 1;
		        }
		        else {
		          return -1;
		        }
		      }

			
		    });
		
		
		for(int k = 0;k<tmp.size();k++) {
		
			int key = tmp.get(k);
			double[] nums = map.get(key);
			double n = nums[1]/nums[0];
			double[] nums1 = map1.get(key);
			double n1 = nums1[1]/nums1[0];
			map11.put(key, n);
			map22.put(key, n1);
			mapmax11.put(key, mapmax1.get(key));
			mapmax22.put(key, mapmax2.get(key));
			mapmin11.put(key, mapmin1.get(key));
			mapmin22.put(key, mapmin2.get(key));
			
		}
		
		saveMap.saveMapTxt(map11,"/Users/miaoshuai/Desktop/avg1.txt");
		saveMap.saveMapTxt(map22,"/Users/miaoshuai/Desktop/avg2.txt");
		saveMap.saveMapTxt(mapmax11,"/Users/miaoshuai/Desktop/max1.txt");
		saveMap.saveMapTxt(mapmax22,"/Users/miaoshuai/Desktop/max2.txt");
		saveMap.saveMapTxt(mapmin11,"/Users/miaoshuai/Desktop/min1.txt");
		saveMap.saveMapTxt(mapmin22,"/Users/miaoshuai/Desktop/min2.txt");
		
		
	}
	
}
