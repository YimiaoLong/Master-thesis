package Taskmodel;

import java.util.*;

public class slot {
	private ArrayList<Job> job_order;
	private long load;
	private long deadline;
	private long wcet;
	
	public slot() {
		job_order = new ArrayList();
		load = 0;
		deadline = 0;
		wcet = 0;
	}
	
	
	public long getload() {
		return load;
	}
	
	public void addload(long w) {
		load += w;
	}
	
	public void addjob(Job job) {
		job_order.add(job);
		wcet += job.getTask().getWcet().getValue();
	}
	
	public void addheadjob(Job job) {
		job_order.add(0,job);
		wcet += job.getTask().getWcet().getValue();
	}
	
	public long getwcet() {
		return wcet;
	}
	
	public long getdeadline() {
		long mind = Long.MAX_VALUE;
		int x = job_order.size();
		for(int i = 0;i<x;i++) {
			long tmp = job_order.get(i).getTask().getDeadline().getValue();
			for(int j = i+1;j<x;j++) {
				tmp += job_order.get(j).getTask().getWcet().getValue();
			}
			mind = Math.min(mind, tmp);
		}
		
		return mind;
	}
	
	public ArrayList<Job> getList(){
		return job_order;
	}
	
}
