package Taskmodel;

import java.util.ArrayList;

public class Job {

	private Task task;
	private Time release;
	private Time deadline;
	private int id;
	private int job_id;
	public String name;
	
	public Job(Task _task, Time _release, Time _deadline) {
		task = _task;
		release = _release;
		deadline = _deadline;
		id = (int)(Math.floor((double)release.getValue() / (double)task.getPeriod().getValue()));
	}
	
	public Job(Task _task, int id2) {
		task = _task;
		job_id = id2;
		name = String.valueOf(_task.getId())+"_"+String.valueOf(id2);
	}
	
	public String toString2() {
		
		return "J_" + task.getId() + "," + job_id ;
	}
	
	
	public static slot findslotByName(ArrayList<slot> slots, String name){
		for(slot s : slots) {
			ArrayList<Job> list = s.getList();
			for(Job j: list) {
				if(j.name.equals(name)) {
					return s;
				}
			}
		}
		
		return null;
	}
	
	public static Job findJobByName(ArrayList<slot> slots, String name){
		for(slot s : slots) {
			ArrayList<Job> list = s.getList();
			for(Job j: list) {
				if(j.name.equals(name)) {
					return j;
				}
			}
		}
		
		return null;
	}
	
	@Override
	public String toString() {
		
		return "J_" + task.getId() + "," + id + " (" + release.toStringShort()+ ", " + deadline.toStringShort() + ")";
	}
	
	public Task getTask() {
		return task;
	}
	
	public int getId() {
		return id;
	}
	
	public Time getRelease() {
		return release;
	}
	
	public Time getDeadline() {
		return deadline;
	}
}
