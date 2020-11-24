package Taskmodel;

import java.util.ArrayList;

public class Task {

	private static int pub_id = 0;
	
	private String name;	//Name of the task
	private Time period;	//Period of the task
	private Time wcet;		//WCET of the task
	private int id;			//ID of the task
	private Time deadline;
	
	/**
	 * Constructor for a task
	 * @param _period	period 
	 * @param _wcet		wcet 
	 */
	public Task(Time _wcet, Time _period) {
		period = _period;
		wcet = _wcet;
		id = Task.pub_id;
		name = "Task_" + Integer.toString(Task.pub_id);
		Task.pub_id++;
		deadline = _period;
	}
	
	public Task(Time _wcet, Time _period, Time _deadline) {
		period = _period;
		wcet = _wcet;
		id = Task.pub_id;
		deadline = _deadline;
		name = "Task_" + Integer.toString(Task.pub_id);
		Task.pub_id++;
	}
	/**
	 * Constructor for a task with given name
	 * @param _period
	 * @param _wcet
	 * @param _name
	 */
	public Task(Time _wcet, Time _period, String _name) {
		period = _period;
		wcet = _wcet;
		id = Task.pub_id;
		name = _name;
		Task.pub_id++;
		deadline = _period;
	}
	
	public Task(Time _wcet, Time _period, Time _deadline, String _name) {
		period = _period;
		wcet = _wcet;
		id = Task.pub_id;
		name = _name;
		deadline = _deadline;
		Task.pub_id++;
	}
	
	/**
	 * If tasks are read from a file they might already have an ID.
	 * An input file always needs to be read at the beginning, so no other tasks should exist.
	 * The internal ID (i.e. pub_id) is then incremented to be at least the value of _id to avoid
	 * later issues with manually added tasks to the same taskset.
	 * @param _wcet
	 * @param _period
	 * @param _name
	 * @param _id
	 */
	public Task(Time _wcet, Time _period, String _name, int _id) {
		period = _period;
		wcet = _wcet;
		id = _id;
		name = _name;
		deadline = _period;
		if (Task.pub_id < _id) pub_id = _id;
	}
	
	@Override
	public String toString() {
		return "[" + id + "] " + name + " (" + wcet.toStringShort() + ", " + period.toStringShort() + ")";
	}
	
	public String getName() {
		return name;
	}
	
	public Time getPeriod() {
		return period;
	}
	
	public Time getDeadline() {
		return deadline;
	}
	
	public Time getWcet() {
		return wcet;
	}
	
	public int getId() {
		return id;
	}
	
	/**
	 * This method generates the set of jobs for this  task in the interval [0, _bound].
	 * The interval is in relation to the jobs deadline, i.e. if a jobs deadline falls into the interval, the job
	 * is part of the job set.
	 * @param _bound upper bound for the interval
	 * @return set of jobs that have their deadline in [0, _bound]
	 */
	public ArrayList<Job> generateJobSet(Time _bound) {
		ArrayList<Job> jobset = new ArrayList<Job>();
		
		int jobCount = (int)Math.floor((double)_bound.getValue() / (double)period.getValue());
		
		//Generate the jobs
		Time abs_release 	= new Time(0);
		Time abs_deadline 	= new Time(period.getValue());
		
		for(int i = 0; i < jobCount; i++) {
			Job tmp = new Job(this, abs_release.assign(), abs_deadline.assign());
			jobset.add(tmp);
			
			abs_release.setValue(abs_release.getValue() + period.getValue());
			abs_deadline.setValue(abs_deadline.getValue() + period.getValue());
		}
		
		return jobset;
	}
	
	/**
	 * Find a task with given ID in a set of tasks
	 * @param tasks	task set
	 * @param id ID of the task that is searched
	 * @return returns the task if it was found, null otherwise
	 */
	public static Task findTask(ArrayList<Task> tasks, int id){
		for (Task t : tasks) {
			if(t == null) continue;
			if(t.getId() == id) return t;
		}
		return null;
	}
	
	/**
	 * Fund a task with given name in a set of tasks
	 * @param tasks task set
	 * @param name name of the task that is searched
	 * @return returns the task if it was found, null otherwise
	 */
	public static Task findTaskByName(ArrayList<Task> tasks, String name){
		for (Task t : tasks) {
			if(t.getName().equals(name)) return t;
		}
		return null;
	}
	
	/**
	 * Method to get the hyperperiod of a task set
	 * @param tasks task set
	 * @return hyperperiod
	 */
	public static Time getHyperperiod(ArrayList<Task> tasks) {
		Time[] periods = new Time[tasks.size()];
		
		for (int i = 0; i < tasks.size(); i++) {
			periods[i] = tasks.get(i).getPeriod();
		}
		
		return Time.compLCM(periods);
	}
	
	
	public static Time getMicroperiod(ArrayList<Task> tasks) {
		Time[] periods = new Time[tasks.size()];
		
		for (int i = 0; i < tasks.size(); i++) {
			periods[i] = tasks.get(i).getPeriod();
		}
		
		return Time.compGCD(periods);
	}
	
	/**
	 * This method generates the set of jobs for a set of tasks in the interval [0, _bound].
	 * The interval is in relation to the jobs deadline, i.e. if a jobs deadline falls into the interval, the job
	 * is part of the job set.
	 * @param _bound upper bound for the interval
	 * @return set of jobs that have their deadline in [0, _bound]
	 */
	public static ArrayList<Job> generateJobSet(ArrayList<Task> t_tasks, Time _bound) {
		ArrayList<Job> jobset = new ArrayList<Job>();
		
		for (Task task : t_tasks) {
			jobset.addAll(task.generateJobSet(_bound));
		}
		
		return jobset;
	}
	
	/**
	 * This method returns the utilization of the task
	 * @return
	 */
	public double getUtilization() {
		return (double)wcet.getValue() / (double)period.getValue();
	}

	
}
