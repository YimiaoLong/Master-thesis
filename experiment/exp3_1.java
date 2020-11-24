package experiment;

import java.awt.GridLayout;
import java.util.*;

import javax.swing.JFrame;

import Taskmodel.Application;
import Taskmodel.Job;
import Taskmodel.Task;
import Taskmodel.Time;
import Taskmodel.slot;
import The_proposed_method.gathering;
import The_proposed_method.sequencing;
import parser.ParseMechaniser;


// runtime of the proposed method
public class exp3_1 {
	public static void main(String[] args) {
			ParseMechaniser pm = new ParseMechaniser();
			ArrayList<HashMap<Integer,Double>> res= new ArrayList(); 
			HashMap<Integer,Integer> jobmap = new HashMap();
			HashMap<Integer,Integer> taskmap = new HashMap();
			HashMap<Long,Integer> m = new HashMap();
			
			
			long max = Long.MIN_VALUE;
			long min = Long.MAX_VALUE;
			long sum = 0;

			
		
			for(int x = 0;x<100;x++) {
				long startTime1=System.nanoTime(); //start time
			for(int i = 0;i<866;i++) {
				Application app =  pm.readModel("/Users/miaoshuai/Desktop/thesis/system/rtns18_systems/sys_"+i+"/tgenout/mechaniser.xml");
				double ult = app.getUlt();
				
				
				ArrayList<ArrayList<Integer>> lists = app.getDep();
				ArrayList<Task> taskset = app.getTaskSet();

				HashMap<int[], int[]> jobdep = app.getJobdep();
				HashMap<int[], int[]> jobleveldep = sequencing.getjobdep(jobdep);
				if(ult>1 ||!gathering.feasibility(taskset) ) {
				
					continue;
				}
				ArrayList<Task> initial = (ArrayList<Task>) taskset.clone();
				HashMap<int[], int[]> jobdep2 = app.getJobdep();
				
				
				gathering.groupDep(taskset, lists,jobleveldep);
				gathering.groupsamePer(taskset);
				for(Task t : taskset) {
					if(t == null) continue;
					ArrayList<slot> slots = sequencing.new_sequence_9(t, initial,jobleveldep);	
				}
				
			}
			long endTime1=System.nanoTime(); //end time
			long tmp = endTime1-startTime1;
			
			if(tmp>max) {
				max = tmp;
			}
			
			if(tmp<min) {
				min = tmp;
			}
			sum+=tmp;
		
			
}
			
			System.out.println("max:  "+max/1000000+"   min"+min/1000000+"    avg"+(sum/100)/1000000);
				}
	}

