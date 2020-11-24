package experiment;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

import Taskmodel.Application;
import Taskmodel.Task;
import Taskmodel.slot;
import The_proposed_method.gathering;
import The_proposed_method.saveMap;
import The_proposed_method.sequencing;
import parser.ParseMechaniser;

public class exp4 {

	public static void main(String[] args) {
		ParseMechaniser pm = new ParseMechaniser();
		HashMap<Integer,double[]> map = new HashMap();
		HashMap<Integer,double[]> map1 = new HashMap();
		HashMap<Integer,double[]> map2 = new HashMap();
		
		int cn = 0;
		for(int i = 1;i<866;i++) {
			Application app =  pm.readModel("/Users/miaoshuai/Desktop/thesis/system/rtns18_systems/sys_"+i +"/tgenout/mechaniser.xml");
			double ult = app.getUlt();
			
			ArrayList<ArrayList<Integer>> lists = app.getDep();

			ArrayList<Task> taskset = app.getTaskSet();	
			
			if(ult>1 ||  !gathering.feasibility(taskset)) {
				continue;
			}

			HashMap<int[], int[]> jobdep = app.getJobdep();
			HashMap<int[], int[]> jobleveldep = sequencing.getjobdep(jobdep);
			int joblevelnum = jobleveldep.size();
			
			int jobnum = jobdep.size();
			if(jobnum == 0) continue;
			
			ArrayList<Task> taskset2 = (ArrayList<Task>) taskset.clone();
			
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
			ArrayList<Task> initial = (ArrayList<Task>) taskset.clone();
			
	//the proposed method
			
			gathering.groupDep(taskset,lists,jobleveldep);
			gathering.groupsamePer(taskset);
			
			int c0 = 0 ;
			ArrayList<Task> result = new ArrayList();
			for(Task task : taskset) {
				if(task!=null) {
					result.add(task);
				}
			}
			for(Task t : result) {
					ArrayList<slot> slots = sequencing.new_sequence_9(t, initial,jobleveldep);
					c0 += sequencing.getnum(slots, jobdep);				
			}
			
			
			double res= (double) c0 / jobnum;
	
			int tasksum = taskset.size();
			
			if(!map.containsKey(joblevelnum)) {
				double[] nums = new double[2];
				nums[0] = 1;
				nums[1] += res;
				map.put(joblevelnum,nums);
			}else {
				double[] nums = map.get(joblevelnum);
				nums[0] ++;
				nums[1] += res;
				map.replace(joblevelnum, nums);
			}
			
			
			
			
			
			
	// PS + DLL
			gathering.groupsamePer(taskset1);

			int c1 = 0 ;
			ArrayList<Task> result1 = new ArrayList();
			for(Task task : taskset1) {
				if(task!=null) {
					result1.add(task);
				}
			}
			
			
			for(Task t : result1) {
					ArrayList<slot> slots = sequencing.new_sequence_9(t, initial,jobleveldep);
					c1 += sequencing.getnum(slots, jobdep);
				
			}
			
			
			
			double	res1 = (double) c1 / jobnum;

			if(!map1.containsKey(joblevelnum)) {
				double[] nums = new double[2];
				nums[0] = 1;
				nums[1] += res1;
				map1.put(joblevelnum,nums);
			}else {
				double[] nums = map1.get(joblevelnum);
				nums[0] ++;
				nums[1] += res1;
				map1.replace(joblevelnum, nums);
			}
			
			// PS + random sequencing
			gathering.groupsamePer(taskset2);

			int c2 = 0 ;
	     	ArrayList<Task> result2 = new ArrayList();
			for(Task task : taskset2) {
				if(task!=null) {
				 result2.add(task);
					}
				}
						
						
			for(Task t : result2) {
				ArrayList<slot> slots = sequencing.random_sequence(t,initial);
				c2 += sequencing.getnum(slots, jobdep);					
			}
					
			double	res2 = (double) c2 / jobnum;

			if(!map2.containsKey(joblevelnum)) {
				double[] nums = new double[2];
				nums[0] = 1;
				nums[1] += res2;
				map2.put(joblevelnum,nums);
			}else {
				double[] nums = map2.get(joblevelnum);
				nums[0] ++;
				nums[1] += res2;
				map2.replace(joblevelnum, nums);
			}
		
			
			
		  }
		

		
		Set<Integer> set = map.keySet();
		
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
		TreeMap<Integer,Double> map11 = new TreeMap();
		TreeMap<Integer,Double> map22 = new TreeMap();
		TreeMap<Integer,Double> map33 = new TreeMap();
		for(int k = 0 ;k<tmp.size();k++) {
			int key = tmp.get(k);
			double[] nums = map.get(key);
			double n = (double)nums[1]/nums[0];
			NumberFormat nt = NumberFormat.getPercentInstance();
		    nt.setMinimumFractionDigits(2);
		    
		       map11.put(key, n);
		    
		    double[] nums1 = map1.get(key);
		    double n1 = (double)nums1[1]/nums1[0];
		   
		    	map22.put(key, n1);
		    	
		    double[] nums2 = map2.get(key);
		    double n2 = (double)nums2[1]/nums2[0];
				   
			   map33.put(key, n2);
		    
		}
		
		
		saveMap.saveMapTxt(map11,"/Users/miaoshuai/Desktop/6.txt");
		saveMap.saveMapTxt(map22,"/Users/miaoshuai/Desktop/7.txt");
		saveMap.saveMapTxt(map33,"/Users/miaoshuai/Desktop/8.txt");
		
		
	}
		
	
}
