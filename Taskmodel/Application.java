package Taskmodel;

import java.util.*;

import logging.Logging;

public class Application {

	private String						name;
	private ArrayList<Task> 			taskSet;
	private ArrayList<CauseEffectChain> chains;
	private Time						hyperperiod;
	private Time						microperiod;
	private double 						utilization;
	private ArrayList<int[]> 			set;
	private HashMap<int[], int[]>		jobdep;
	/**
	 * Initialize an empty application
	 */
	public Application(String _name) {
		name = _name;
		taskSet = new ArrayList<Task>();
		chains = new ArrayList<CauseEffectChain>();
		hyperperiod = new Time(0);
		microperiod = new Time(0);
		utilization = 0;
		set = new ArrayList<int[]>();
		jobdep = new HashMap();
	}
	
	public double getUlt() {
		return this.utilization;
	}

	@Override
	public String toString() {
		return "Application: " + name + " (HP = " + hyperperiod.toStringShort() + ", " + taskSet.size() + " tasks, " + chains.size() + " chains, utilization: " + utilization + ")";
	}

	/**
	 * Add a task to the application.
	 * @param _task task to be added
	 * @return true if task could be added, false otherwise (it was already there)
	 */
	public boolean addTask(Task _task) {
		if (Task.findTask(taskSet, _task.getId()) == null) {
			taskSet.add(_task);
			hyperperiod = Task.getHyperperiod(taskSet);	//update the hyperperiod
			microperiod = Task.getMicroperiod(taskSet);	//update the hyperperiod
			utilization += _task.getUtilization();		//add the tasks utilization
			return true;
		}
		return false;
	}

	public void addDep(Integer sti, Integer res) {
		int[] nums = {sti,res};
		
		set.add(nums);
	}
	
	public void addJobdep(int[] sti, int[] res) {
		jobdep.put(sti, res);
	}
	
	/**
	 * returns the task of the application by its ID
	 * @param _id
	 * @return
	 */
	public Task getTaskById(int _id) {
		return Task.findTask(taskSet, _id);
	}

	public int createChain() {
		CauseEffectChain tmpChain = new CauseEffectChain();
		chains.add(tmpChain);
		return tmpChain.getId();
	}

	public int createChainWithName(String _name) {
		CauseEffectChain tmpChain = new CauseEffectChain();
		tmpChain.setName(_name);
		chains.add(tmpChain);
		return tmpChain.getId();
	}

	public int createChainFromFile(int _id, String _name) {
		CauseEffectChain tmpChain = new CauseEffectChain(_id);
		if(_name != null) tmpChain.setName(_name);
		chains.add(tmpChain);
		return tmpChain.getId();
	}

	public int createChain(DelayConstraint _constraint) {
		CauseEffectChain tmpChain = new CauseEffectChain(_constraint);
		chains.add(tmpChain);
		return tmpChain.getId();
	}

	/**
	 * Method to add a delay constraint to a chain that is identified by its ID
	 * @param _chainId		chain ID
	 * @param _constraint 	delay constraint for the chain
	 * @return				true if successful, false otherwise
	 */
	public boolean addChainConstraint(int _chainId, DelayConstraint _constraint) {
		CauseEffectChain chain = CauseEffectChain.getChain(chains, _chainId);

		if (chain != null) {
			chain.setConstriant(_constraint);
			return true;
		} else {
			return false;
		}
	}

	public boolean appendTaskToChain(int _chainId, Task _task) {
		CauseEffectChain chain = CauseEffectChain.getChain(chains, _chainId);

		if (chain != null) {
			if (taskSet.contains(_task)) {
				chain.appendTask(_task);
			}
			return false;
		} else {
			return false;
		}
	}

	public String chainToString(int _chainId) {
		CauseEffectChain chain = CauseEffectChain.getChain(chains, _chainId);

		if (chain != null) {
			return chain.toString();
		} else {
			return null;
		}
	}

	public LinkedList<Task> getChainTasks(int _chainId) {
		return chains.get(_chainId).getTasksOfChain();
	}

	public void printExtensiveSummary() {
		Logging.log("######################################################################");
		Logging.log("# " + this.toString());
		Logging.log("######################################################################");
		Logging.log("Task-Set:");
		for (Task t : taskSet) {
			Logging.log(t.toString());
		}
		Logging.log("----------------------------------------------------------------------");
		Logging.log("Cause-Effect Chains:");
		for (CauseEffectChain chain : chains) {
			Logging.log(chain.toString());
		}
		Logging.log("----------------------------------------------------------------------");
	}

	public ArrayList<CauseEffectChain> getChains() {
		return chains;
	}

	public Time getHyperperiod() {
		return hyperperiod;
	}
	

	public ArrayList<Task> getTaskSet() {
		return taskSet;
	}

	public ArrayList<ArrayList<Integer>> getDep(){
		
		ArrayList<ArrayList<Integer>> res = new ArrayList();
		ArrayList<Integer> list = new ArrayList();

	 for(int i = set.size()-1;i>=0;i--) {
		 int[] nums = set.get(i);
		 int num1 = nums[0];
		 int num2 = nums[1];
			 if(list.isEmpty()) {
		          	list.add(num1);
		          	list.add(num2);
		          }else if(list.contains(num1)) {
		          	if(!list.contains(num2)){
		          		list.add(num2);
		          	}
		          }else {
		          	res.add(new ArrayList(list));
		          
		          	list.clear();
		          	list.add(num1);
		          	list.add(num2);
		          }
	 }
   
         
        
      res.add(new ArrayList(list));
		
		
//		
//		ListIterator<Map.Entry<Integer,Integer>> i=new ArrayList<Map.Entry<Integer,Integer>>(set.entrySet()).listIterator(set.size());  
//        while(i.hasPrevious()) {  
//            Map.Entry<Integer, Integer> entry=i.previous();  
//            System.out.print(entry.getKey()+" + ");
//            System.out.println(entry.getValue());
//        	
//            if(list.isEmpty()) {
//            	list.add(entry.getKey());
//            	list.add(entry.getValue());
//            }else if(list.contains(entry.getKey())) {
//            	if(!list.contains(entry.getValue())){
//            		list.add(entry.getValue());
//            	}
//            }else {
//            	res.add(new ArrayList(list));
//            
//            	list.clear();
//            	list.add(entry.getKey());
//            	list.add(entry.getValue());
//            }
//        }  
//        res.add(new ArrayList(list));
//        System.out.println(res.toString());
//        
        for(int w = 0; w<res.size();w++) {
        	ArrayList<Integer> lis = res.get(w);
        	if(lis.size() == 0 || lis == null) continue;
        	int begin = lis.get(0);
        	for(ArrayList<Integer> li : res) {
        		int last = li.get(li.size()-1);
        		if(begin == last) {
        			for(int x = 1;x<lis.size();x++) {
        				li.add(lis.get(x));
        			}
        			res.remove(lis);
        			break;
        		}
        		
        	}
        	
        }
        
        Collections.sort(res, new Comparator<ArrayList<Integer>>() {
		      @Override
		      public int compare(ArrayList<Integer> l1, ArrayList<Integer> l2) {
		        if(Task.findTask(taskSet, l1.get(0)).getPeriod().getValue() >= Task.findTask(taskSet, l2.get(0)).getPeriod().getValue()) {
		          return 1;
		        }
		        else {
		          return -1;
		        }
		      }
		    }); 
		
		return res;
	}
	
	public HashMap<int[], int[]> getJobdep(){
		return jobdep;
	}
	
	

	public int getIdOfChainByName(String _name) {
		
		
		
		
		for (CauseEffectChain chain : chains) {
			if (chain.getName().equals(_name)) return chain.getId();
		}

		return -1;
	}
}
