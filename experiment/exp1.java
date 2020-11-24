package experiment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;

import Taskmodel.Application;
import Taskmodel.Job;
import Taskmodel.Task;
import Taskmodel.slot;
import The_proposed_method.gathering;
import The_proposed_method.saveMap;
import The_proposed_method.sequencing;
import parser.ParseMechaniser;

public class exp1 {

public static void main(String[] args) {
	
	ParseMechaniser pm = new ParseMechaniser();
	ArrayList<HashMap<Integer,Double>> res= new ArrayList(); 
	HashMap<Integer,double[]> map0 = new HashMap();
	HashMap<Integer,double[]> map1 = new HashMap();
	HashMap<Integer,double[]> map2 = new HashMap();
	HashMap<Integer,double[]> map3 = new HashMap();
	
	for(int i = 1;i<866;i++) {

		int c1 = 0;
		int c2 = 0;
		int c0 = 0;
		Application app =  pm.readModel("/Users/miaoshuai/Desktop/thesis/system/rtns18_systems/sys_"+i+"/tgenout/mechaniser.xml");
		double ult = app.getUlt();
		
		
		ArrayList<ArrayList<Integer>> lists = app.getDep();
		HashMap<int[], int[]> jobdep= app.getJobdep();
		HashMap<int[], int[]> jobleveldep = sequencing.getjobdep(jobdep);
		int joblevelnum = jobleveldep.size();
		
		ArrayList<Task> taskset = app.getTaskSet();
		if(ult>1 || !gathering.feasibility(taskset)) {
			continue;
		}
		
		int jobnum =  jobdep.size();
		if(jobnum == 0) continue;
		int depnum = 0;
		for(ArrayList<Integer> list : lists) {
			depnum += list.size()-1;
			
		}
		
		int tasknum = taskset.size();
		
		ArrayList<Task> taskset1 = (ArrayList<Task>) taskset.clone();
		ArrayList<Task> taskset2 = (ArrayList<Task>) taskset.clone();

		
//		 Sorting by period
		Collections.sort(taskset1, new Comparator<Task>() {
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
		
	
		gathering.groupsamePer(taskset1);
		
		
		for(Task t : taskset1) {
			if(t!=null) {
				ArrayList<slot> slots = sequencing.random_sequence(t,taskset);
				c1 += sequencing.getnum(slots, jobdep);
				
			}
		}
		
		
		double r1 = (double) c1 / jobnum;
		if(!map1.containsKey(joblevelnum)) {
			double[] nums = new double[2];
			nums[0] = 1;
			nums[1] += r1;
			map1.put(joblevelnum,nums);
		}else {
			double[] nums = map1.get(joblevelnum);
			
			
			nums[0] ++;
			nums[1] += r1;
			map1.replace(joblevelnum, nums);
		}

				

		
// Sorting by Wcet
		
		Collections.sort(taskset2, new Comparator<Task>() {
		      @Override
		      public int compare(Task t1, Task t2) {
		        if(t1.getWcet().getValue() >= t2.getWcet().getValue()) {
		          return 1;
		        }
		        else {
		          return -1;
		        }
		      }
		    });
		

		gathering.groupsamePer(taskset2);
		
		
		
		for(Task t : taskset2) {
			if(t!=null) {
				ArrayList<slot> slots = sequencing.random_sequence(t,taskset);
				c2 += sequencing.getnum(slots, jobdep);			
			}
			
		}
		
		
		double r2= (double) c2 / jobnum;
	
		
		if(!map2.containsKey(joblevelnum)) {
			double[] nums = new double[2];
			nums[0] = 1;
			nums[1] += r2;
			map2.put(joblevelnum,nums);
		}else {
			double[] nums = map2.get(joblevelnum);
			nums[0] ++;
			nums[1] += r2;
			map2.replace(joblevelnum, nums);
		}
	
		
		

		
	}
	TreeMap<Integer,Double> map22 = new TreeMap();
	TreeMap<Integer,Double> map33 = new TreeMap();
	
	Set<Integer> set = map1.keySet();
	for(int key : set) {
		if(key == -1) continue;
		
		
		double[] d1 = map1.get(key);
		map22.put(key,d1[1]/d1[0]);
		
		
		double[] d3 = map2.get(key);
		map33.put(key, d3[1]/d3[0]);
		
	}
	
	
	
	saveMap.saveMapTxt(map22,"/Users/miaoshuai/Desktop/2.txt");
	saveMap.saveMapTxt(map33,"/Users/miaoshuai/Desktop/3.txt");
	
	
}

}