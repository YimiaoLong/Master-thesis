package The_proposed_method;

import java.util.*;
import parser.ParseMechaniser;
import Taskmodel.Job;
import Taskmodel.Task;
import Taskmodel.Time;
import Taskmodel.slot;

public class sequencing {
	static int count = 0;
	
	
	
	public static HashMap<int[], int[]> getjobdep(HashMap<int[], int[]> jobdep){
		HashMap<int[], int[]> res = new HashMap<int[], int[]>();
		
		Set<int[]> setbef= jobdep.keySet();
		
		Set<int[]> deporder = new TreeSet<int[]>(new Comparator<int[]>() {
            @Override
            public int compare(int[] n1, int[] n2) {
               if(n1[0]  == n2[0]) {
            	   return n1[1]-n2[1];
               }else {
            	   return n1[0]-n2[0];
               }
            		 
            }
        });	
		
		int resjob = 0;
		for(int[] nums :setbef) {			
				deporder.add(nums);
			
		}
		
		Set<Integer> set = new HashSet();
		 for(Iterator iter = deporder.iterator(); iter.hasNext(); ) {
	           int[] tmp = (int[]) iter.next();
	           int tmp1 = tmp[0];
	           if(set.contains(tmp1)) {
	        	   continue;
	           }else {
	        	   set.add(tmp1);
	        	   int[] tmpp = jobdep.get(tmp);
	        	   res.put(tmp, tmpp);
	           }
	        }
		
		
		return res;
	}
	
	
	public static ArrayList<slot> random_sequence(Task t, ArrayList<Task> initialtaskset) {
		String s1 = t.getName();		
		String[] ss = s1.split("_");
		ArrayList<Integer> tmp = new ArrayList();
		for(String string : ss) {
			String str1 = string.substring(4, string.length());
			int pre = Integer.parseInt(str1);
			tmp.add(pre);
		}
		ArrayList<Task> set = new ArrayList();
		for(int i: tmp) {
			set.add(Task.findTask(initialtaskset, i));
		}
		
		Collections.shuffle(set);

		long Tcycle = Task.getHyperperiod(initialtaskset).getValue();
		long slot_number =  (Tcycle/t.getPeriod().getValue());
		
		//create slots
		
		ArrayList<slot> slots = new ArrayList();
		for(int i = 0; i<slot_number;i++) {
			slot s = new slot();
			slots.add(s);
		}
		
		//  allocate jobs
		for(int i = 0;i<set.size();i++) {
			Task task = set.get(i);
			int index = 1;
			for(int x = 0;x<slots.size();x++) {
				slot s = slots.get(x);
				Job job = new Job(task,index++);				
				s.addjob(job);
				s.addload(task.getWcet().getValue());			
			}
			
		}
		
		
		return slots;
	}

	
	
	public static int getnum(ArrayList<slot> slots, HashMap<int[], int[]> jobdep) {
		int c = 0;
		Set<int[]> set = jobdep.keySet();
		for(int[] sti : set) {
			int[] res = jobdep.get(sti);
			String sti_name = String.valueOf(sti[0])+"_"+String.valueOf(sti[1]); 
			String res_name = String.valueOf(res[0])+"_"+String.valueOf(res[1]);  
			slot s1 = Job.findslotByName(slots, sti_name);
			if(s1 == null) {
				continue;
			}
			slot s2 = Job.findslotByName(slots, res_name);
			int n1 = slots.indexOf(s1);
			int n2 = slots.indexOf(s2);
			if(n1<n2) {
				c++;
			}else if(n1 == n2) {
				ArrayList<Job> list = s1.getList();
				Job j1 = Job.findJobByName(slots, sti_name);
				Job j2 = Job.findJobByName(slots, res_name);
				int index1 = list.indexOf(j1);
				int index2 = list.indexOf(j2);
				if(index1<index2) {
					c++;
				}
			}
		}
		return c;
	}
	
	
	
	
	public static boolean checkslots(ArrayList<slot> slots, long Tmicro) {
		for(slot s : slots) {
			if(s.getload() > Tmicro) {
				return false;
			}
		}	
		return true;
	}
	
	
public static ArrayList<slot> new_sequence_9(Task t, ArrayList<Task> initialtaskset, HashMap<int[], int[]> jobdep) {
		
		String s1 = t.getName();		
		String[] ss = s1.split("_");
		ArrayList<Integer> tmp = new ArrayList();
		ArrayList<ArrayList<Job>> joblists = new ArrayList();
		for(String string : ss) {
			String str1 = string.substring(4, string.length());
			int pre = Integer.parseInt(str1);
			tmp.add(pre);
		}
		ArrayList<Task> set = new ArrayList();
		for(int i: tmp) {
			set.add(Task.findTask(initialtaskset, i));
		}
		
		
		long Tcycle = Task.getHyperperiod(initialtaskset).getValue();
		long Tmicro = Task.getMicroperiod(set).getValue();
		
		//create slots
		long slot_number = Tcycle/Tmicro;
		ArrayList<slot> slots = new ArrayList();
		for(int i = 0; i<slot_number;i++) {
			slot s = new slot();
			slots.add(s);
		}
		
		
		// least-loaded   allocate jobs
		for(int i = 0;i<set.size();i++) {
			boolean flag = true; 
			int index = 1;
			int min = 0;
			long min_load = Long.MAX_VALUE;
			Task task1 = set.get(i);
			int interval = (int) (task1.getPeriod().getValue()/Tmicro);
			
			for(int j = 0; j<i;j++) {
			Task task2 = set.get(j);
			if(gathering.Dep(set, i, j, jobdep)) {
				int resjob = 0;			
				
				Set<int[]> set2= jobdep.keySet();
				for(int[] num : set2) {
					if(num[0] == task1.getId()) {
						index = num[1];
					}
				}
			
				int index1 = index;
				
				for(int[] nums : set2) {
					if(nums[0] == task1.getId() && nums[1] == index) {
						int[] va = jobdep.get(nums);
						resjob = va[1];
					}
				}
				

				slot s = Job.findslotByName(slots, task2.getId()+"_"+resjob);
				int tmp2 = slots.indexOf(s);
				
					
					if(tmp2>interval*index) {
						for(int w = interval*index -1;w>Math.max(0, interval*(index-1));w--) {		//look for least-loaded slot
							
							slot next = slots.get(w);
							long lo = next.getload();
							if(lo<min_load) {
								min_load = lo;
								min = w;
							}
						}
					}else {
						for(int w = tmp2;w>interval*(index-1);w--) {		
					
							slot next = slots.get(w);
							long lo = next.getload();
							if(lo<min_load) {
								min_load = lo;
								min = w;
							}
						}
						
					}
					
					
					for(int x = min;x<slots.size();x+=interval) {				 //allocate
						slot sl = slots.get(x);
						Job job = new Job(task1,index++);	
						if(min == tmp2) {
							sl.addheadjob(job);
						}else {
							sl.addjob(job);
						}
						
						sl.addload(task1.getWcet().getValue());
						
				}
					if(index1!=0) {
					for(int x = min-interval;x>=0;x-=interval) {
						slot sl = slots.get(x);
						Job job = new Job(task1,index1--);	
						if(min == tmp2) {
							sl.addheadjob(job);
						}else {
							sl.addjob(job);
						}
						
						sl.addload(task1.getWcet().getValue());
						
					}
				}
				
				
				flag = false;
				
				}else if(gathering.Dep(set, j, i, jobdep)) {
					
					int resjob = 0;
					int[] sti1 = {0,0};
									
					Set<int[]> set2 = jobdep.keySet();
					for(int[] num : set2) {
						if(num[0] == task2.getId()) {
							sti1[0] = num[0];
							sti1[1] = num[1];
							int[] nums = jobdep.get(num);
							if(nums[0] == task1.getId()) {
								resjob = nums[1];
							}
						}
					}
					
					
					index = resjob;
					int index1 = index-1;
					
					
					
					slot s = Job.findslotByName(slots, task2.getId()+"_"+sti1[1] );
					int tmp2 = slots.indexOf(s);
					if(tmp2>interval*index) {
							for(int w = interval*index -1;w>=interval*(index-1);w--) {		//look for least-loaded slot
								
								slot next = slots.get(w);
								long lo = next.getload();
								if(lo<min_load) {
									min_load = lo;
									min = w;
								}
							}
						}else {
								for(int w = Math.max(tmp2, interval*(index-1)) ;w<interval*index;w++) {		//look for least-loaded slot		
								slot next = slots.get(w);
								long lo = next.getload();
								if(lo<min_load) {
									min_load = lo;
									min = w;
								}
							}
						}
					
					
						
						for(int x = min;x<slots.size();x+=interval) {				 //allocate
							
							slot sl = slots.get(x);
							Job job = new Job(task1,index++);	
							sl.addjob(job);		
							
							sl.addload(task1.getWcet().getValue());
							
					}
						if(index1 !=0) {
						for(int x = min-interval;x>=0;x-=interval) {
							slot sl = slots.get(x);
							Job job = new Job(task1,index1--);								
							sl.addjob(job);											
							sl.addload(task1.getWcet().getValue());
							
							}
						}
//					}	
					
					flag = false;
					
					
					}
			
			
			}
			if(flag) {
				for(int x = 0;x<slots.size();x+=interval) {       //allocate
					slot sl = slots.get(x);
					Job job = new Job(task1,index++);	
					sl.addjob(job);
					sl.addload(task1.getWcet().getValue());
					
					flag = false;
				}
			}
			
				
	 	}
				
		return slots;
	}
	
	
public static ArrayList<slot> new_sequence_8(ArrayList<Task> set, ArrayList<Task> initialtaskset, HashMap<int[], int[]> jobdep) {
		
	
		
		long Tcycle = Task.getHyperperiod(initialtaskset).getValue();
		long Tmicro = Task.getMicroperiod(set).getValue();
		
		//create slots
		long slot_number = Tcycle/Tmicro;
		ArrayList<slot> slots = new ArrayList();
		for(int i = 0; i<slot_number;i++) {
			slot s = new slot();
			slots.add(s);
		}
		
		
		// least-loaded   allocate jobs
		for(int i = 0;i<set.size();i++) {
			boolean flag = true; 
			int index = 1;
			int min = 0;
			long min_load = Long.MAX_VALUE;
			Task task1 = set.get(i);
			int interval = (int) (task1.getPeriod().getValue()/Tmicro);
			
			for(int j = 0; j<i;j++) {
			Task task2 = set.get(j);
			if(gathering.Dep(set, i, j, jobdep)) {
				
				int resjob = 0;			
				
				Set<int[]> set2= jobdep.keySet();
				for(int[] num : set2) {
					if(num[0] == task1.getId()) {
						index = num[1];
					}
				}
			
				int index1 = index;
				
				for(int[] nums : set2) {
					if(nums[0] == task1.getId() && nums[1] == index) {
						int[] va = jobdep.get(nums);
						resjob = va[1];
					}
				}
				

				slot s = Job.findslotByName(slots, task2.getId()+"_"+resjob);
				int tmp2 = slots.indexOf(s);
				
					
					if(tmp2>interval*index) {
						for(int w = interval*index;w>interval*(index-1);w--) {		//look for least-loaded slot
							
							slot next = slots.get(w);
							long lo = next.getload();
							if(lo<min_load) {
								min_load = lo;
								min = w;
							}
						}
					}else {
						for(int w = tmp2;w>interval*(index-1);w--) {		
					
							slot next = slots.get(w);
							long lo = next.getload();
							if(lo<min_load) {
								min_load = lo;
								min = w;
							}
						}
						
					}
					
					for(int x = min;x<slots.size();x+=interval) {				 //allocate
						slot sl = slots.get(x);
						Job job = new Job(task1,index++);	
						if(min == tmp2) {
							sl.addheadjob(job);
						}else {
							sl.addjob(job);
						}
						
						sl.addload(task1.getWcet().getValue());
					
				}
					
					for(int x = min-interval;x>=0;x-=interval) {
						slot sl = slots.get(x);
						Job job = new Job(task1,index1--);	
						if(min == tmp2) {
							sl.addheadjob(job);
						}else {
							sl.addjob(job);
						}
						
						sl.addload(task1.getWcet().getValue());
						
				}
				
				
				flag = false;
				
				}else if(gathering.Dep(set, j, i, jobdep)) {
					
					int resjob = 0;
					int[] sti1 = {0,0};
									
					Set<int[]> set2 = jobdep.keySet();
					for(int[] num : set2) {
						if(num[0] == task2.getId()) {
							sti1[0] = num[0];
							sti1[1] = num[1];
							int[] nums = jobdep.get(num);
							if(nums[0] == task1.getId()) {
								resjob = nums[1];
							}
						}
					}
					
					
					
					index = resjob;
					int index1 = index;
					
					
					
					slot s = Job.findslotByName(slots, task2.getId()+"_"+sti1[1] );
					int tmp2 = slots.indexOf(s);
					if(tmp2>interval*index) {
							for(int w = interval*index;w>=interval*(index-1);w--) {		//look for least-loaded slot
								
								slot next = slots.get(w);
								long lo = next.getload();
								if(lo<min_load) {
									min_load = lo;
									min = w;
								}
							}
						}else {
								for(int w = Math.max(tmp2, interval*(index-1));w<interval*index;w++) {		//look for least-loaded slot		
								slot next = slots.get(w);
								long lo = next.getload();
								if(lo<min_load) {
									min_load = lo;
									min = w;
								}
							}
						}
						
						for(int x = min;x<slots.size();x+=interval) {				 //allocate
							slot sl = slots.get(x);
							Job job = new Job(task1,index++);	
							sl.addjob(job);							
							sl.addload(task1.getWcet().getValue());
							
					}
						
						for(int x = min-interval;x>=0;x-=interval) {
							slot sl = slots.get(x);
							Job job = new Job(task1,index1--);								
							sl.addjob(job);											
							sl.addload(task1.getWcet().getValue());
							
					}

					
					flag = false;
					
					
					}
			
			
			}
			if(flag) {
				for(int x = 0;x<slots.size();x+=interval) {       //allocate
					slot sl = slots.get(x);
					Job job = new Job(task1,index++);	
					sl.addjob(job);
					sl.addload(task1.getWcet().getValue());
					flag = false;
				}
			}
			
				
	 	}
				
		return slots;
	}
	

	
}
