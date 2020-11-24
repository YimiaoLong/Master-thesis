package The_proposed_method;


import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

import Taskmodel.Application;
import Taskmodel.ConstraintType;
import Taskmodel.DelayConstraint;
import Taskmodel.Task;
import Taskmodel.CauseEffectChain;
import Taskmodel.Time;
import Taskmodel.slot;


public class gathering {
	

static int c = 0;

//   group runnables with the dependencies
public static void groupDep(ArrayList<Task> taskset, ArrayList<ArrayList<Integer>> lists, HashMap<int[], int[]> jobleveldep) {

	ArrayList<Task> initial = (ArrayList<Task>) taskset.clone();
	for(ArrayList<Integer> list : lists) {
		
		
		
		if(list.size()==0||list==null) continue;
		int k = list.get(0);
		Task head = Task.findTask(taskset, k);
		if(head == null) 	continue;
		int q = list.size();
		String  nametmp = head.getName();
		for(int i = 1;i<q;i++) {
			int m = list.get(i);
			Task cur = Task.findTask(taskset, m);
			
			if(cur == null || cur==head) continue;
			
			String namecur = cur.getName();
		
			
			long ln = 0;
			long wn = 0;
			Time pn = null;
			Time nw = null;
			Time dw = null;
	
			if(head.getPeriod().getValue() == cur.getPeriod().getValue()) {
				ln = head.getPeriod().getValue();
				wn = head.getWcet().getValue() + cur.getWcet().getValue();
				pn = new Time(ln);
				nw = new Time(wn);
				dw = new Time(Math.min(head.getDeadline().getValue(), cur.getDeadline().getValue()));
			}else {
				
				long l1 = head.getPeriod().getValue();
				long l2 = cur.getPeriod().getValue();
			
				long w1 = head.getWcet().getValue();
				long w2 = cur.getWcet().getValue();
				long major = Task.getHyperperiod(initial).getValue();
				
				
				ArrayList<Task> tmp = new ArrayList();
				tmp.add(cur);
				if(head.getName().contains("_")) {
					String[] strs = head.getName().split("_");					
					for(String str : strs) {
						int len =  str.length();
						String s  = str.substring(4, len); 
						  int  im = Integer.parseInt(s);
						
						Task tm = Task.findTask(initial, (im));
						
						tmp.add(tm);
						
					}					
				}else {
					tmp.add(head);
				}
				
				
				
				long micro = Task.getMicroperiod(tmp).getValue();
				pn = new Time(micro);
				int  time =(int) (major/micro);
				
				
				long minc = Long.MIN_VALUE;
				long mind = Long.MAX_VALUE; 
				
				
				
				
				ArrayList<slot> slots = sequencing.new_sequence_8(tmp, initial, jobleveldep);
				for(slot s : slots) {
					if(s == null) continue;
					mind = Math.min(mind, s.getdeadline());
					minc = Math.max(minc, s.getwcet());
				}
				

				
				nw = new Time(minc);		//new WCET
				dw = new Time(mind);		//new deadline
			}
			
			String s1 = head.getName();
			String s2 = cur.getName();

			
			String sn = s1+"_"+s2;
			Task r = new Task(nw,pn,dw,sn);
			ArrayList<Task> clone = (ArrayList<Task>) taskset.clone();
			int index1 = clone.indexOf(cur);
			int index2 = clone.indexOf(head);
			clone.set(index1, null);
			clone.set(index2, r);
			if(feasibility(clone) == false) {	
	
				head = cur;
				k = m;
			}else {
				taskset.set(index1, null);
				taskset.set(index2, r);
				head = r;
				c++;
			}
			
			
		}
		
		
	}

	
}

public static boolean Dep(ArrayList<Task> taskset,int i,int j, HashMap<int[], int[]> jobdep) {
	Task t1 = taskset.get(i);
	Task t2 = taskset.get(j);
	int t1id = t1.getId();
	int t2id = t2.getId();
	Set<int[]> s = jobdep.keySet();
	for(int[] n1 : s) {
		int[] n2 = jobdep.get(n1);
		int sti = n1[0];
		int res = n2[0];
		if(sti == t1id && res == t2id) {
			return true;
		}
	}
	
	
	return false;
}





//   group runnables with the same period
public static void groupsamePer(ArrayList<Task> taskset) {
	
	
	
	ArrayList<Task> samepset = new ArrayList();
	int size = taskset.size();
	for(int i = 0;i<size;i++) {
		Task  task1 = taskset.get(i);
		if(task1 == null) {
			continue;
		}
		for(int j = i+1;j<size;j++) {	
			
			Task  task2 = taskset.get(j);
			if(task2 == null || task1 == null) continue;
			if(task1.getPeriod().getValue() == task2.getPeriod().getValue()) {
				Time tx = task1.getWcet();
				Time ty = task2.getWcet();
				Time tw = new Time(tx.getValue()+ty.getValue());
				Time dw = new Time(Math.min(task1.getDeadline().getValue(), task2.getDeadline().getValue()));
				String n1 = task1.getName();
				String n2 =  task2.getName();
				String s = n1+"_"+n2;		
					
				Task tnew = new Task(tw,task2.getPeriod(),dw,s);
				ArrayList<Task> clone = (ArrayList<Task>) taskset.clone();
				
				int index1 = clone.indexOf(task1);
				int index2 = clone.indexOf(task2);
				clone.set(index1, tnew);
				clone.set(index2, null);
				
				if(feasibility(clone) == false) {	
					
					break;
				}else {	
					taskset.set(index1, tnew);
				}
				
				taskset.set(index2, null);
				task1 = taskset.get(index1);
			
				
			}
			
			
		}
	
	taskset.removeAll(samepset);
}
}



public static double getpre(ArrayList<ArrayList<Integer>> lists) {
	double sum = 0;
	for(ArrayList<Integer> list : lists) {
		sum += list.size()-1;
	}	
	double res = c/sum;
	c = 0;
	return res;
	
}





public static boolean feasibility(ArrayList<Task> taskset) {
	
	ArrayList<Task> tmp = new ArrayList();
	for(Task t: taskset) {
		if(t!=null) {
			tmp.add(t);
		}
	}
	Collections.sort(tmp, new Comparator<Task>() {
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
	
	int wc = 0;
	for(int x = 0;x<tmp.size();x++) {
		wc++;
		boolean flag = true;
		long c = tmp.get(x).getWcet().getValue();
		long p = tmp.get(x).getPeriod().getValue();
		long d = tmp.get(x).getDeadline().getValue();
		long Rn = 0;
		long old = c;
		int cn = 0;
		while(old != Rn) {
			
			if(flag == false) {
				old = Rn;
			}
			flag = false;
			Rn = c;
			
			for(int j = 0;j<x;j++) {
				Rn += (long) (Math.ceil((double)old/tmp.get(j).getPeriod().getValue())) * tmp.get(j).getWcet().getValue();
				
			}
			if(Rn>d) {
				return false;
			}
			cn++;
			
		}
		
	}
	
	
	return true;
}






public static long get_gcd(long a, long b) {
	long max, min;
	max = (a > b) ? a : b;
	min = (a < b) ? a : b;

	if (max % min != 0) {
		return get_gcd(min, max % min);
	} else
		return min;

}


public static long get_lcm(long a, long b) {
	return a * b / get_gcd(a, b);
}

}
