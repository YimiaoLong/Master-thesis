package Taskmodel;

import java.util.ArrayList;
import java.util.LinkedList;

public class CauseEffectChain {

	private static int 					pub_id = 0;
	
	private LinkedList<Task> 			chain 				= null;
	private DelayConstraint				constriant 			= null;
	private int 						id;
	private String 						name;
	
	/**
	 * Constructor for a cause-effect chain
	 */
	public CauseEffectChain() {
		
		chain = new LinkedList<Task>();
		constriant = null;
		name = "Chain_" + CauseEffectChain.pub_id;
		id = CauseEffectChain.pub_id;
		
		CauseEffectChain.pub_id++;
	}
	
	/**
	 * Constructor that sets a chain id, this should only be used when loading systems from a input file! 
	 * The internal chain id counter is increased to be at least the value of _id. This way, new manually added
	 * chains do not conflict in IDs.
	 * @param _id
	 */
	public CauseEffectChain(int _id) {
		
		chain = new LinkedList<Task>();
		constriant = null;
		name = "Chain_" + CauseEffectChain.pub_id;
		id = _id;
		
		if (CauseEffectChain.pub_id < _id) CauseEffectChain.pub_id = _id;
	}
	
	/**
	 * Method sets a user defined name. 
	 * @param _name New name for  the chain
	 */
	public void setName(String _name) {
		name = _name;
	}
	
	public int getId() {
		return id;
	}
	
	/**
	 * Constructor for a cause-effect chain
	 */
	public CauseEffectChain(DelayConstraint _constraint) {
		
		chain = new LinkedList<Task>();
		constriant = _constraint;
		name = "Chain_" + CauseEffectChain.pub_id;
		id = CauseEffectChain.pub_id;
		
		CauseEffectChain.pub_id++;
	}
	
	/**
	 * Set the delay constraint for the chain
	 * @param constriant
	 */
	public void setConstriant(DelayConstraint constriant) {
		this.constriant = constriant;
	}
	
	/**
	 * Return the delay constraint of the chain
	 * @return
	 */
	public DelayConstraint getConstriant() {
		return constriant;
	}
	
	/**
	 * Add a task to the cause-effect chain
	 * @param t task to be added
	 */
	public void appendTask(Task _t) {
		chain.add(_t);
	}
	
	/**
	 * Return the length of the chain
	 * @return Length of the chain
	 */
	public int getLength() {
		return chain.size();
	}
	
	/**
	 * This method checks if the task is part of the cause-effect chain
	 * @param r task
	 * @return  returns true or false 
	 */
	public boolean elementOfChain(Task _t){
		for (Task task : chain) {
			if(_t == task) return true;
		}
		return false;
	}
	
	/**
	 * Return a string that describes this cause-effect chain
	 */
	@Override
	public String toString() {
		String tmp = "[" + id + "] " + name + ": (";
		
		for (int i = 0; i < getLength(); i++) {
			if(i == 0) {
				tmp += chain.get(i).getName();
			}else {
				tmp += " -> " + chain.get(i).getName();
			}
		}
		tmp += ")";
		if(constriant != null) tmp += " " + constriant.toString();
		return tmp;
	}
	
	/**
	 * Compute the hyperperiod of the chain
	 * @return
	 */
	public Time getHyperperiod() {
		Time[] periods = new Time[chain.size()];
		
		for (int i = 0; i < chain.size(); i++) {
			periods[i] = chain.get(i).getPeriod();
		}
		
		return Time.compLCM(periods);
	}
	
	/**
	 * Method returns the list of tasks that comprise the chain
	 * @return
	 */
	public LinkedList<Task> getTasksOfChain() {
		return chain;
	}
	
	public int getTaskPosition(Task _task) {
		return chain.indexOf(_task);
	}
	
	public Task getNextTask(Task _task) {
		return chain.get(chain.indexOf(_task) + 1);
	}
	
	public String getName() {
		return name;
	}
	
	/**
	 * Get a chain from a set of chains by its ID
	 * @param _chains Set of chains
	 * @param _id ID of the searched chain
	 * @return returns the chain or null if not in set
	 */
	public static CauseEffectChain getChain(ArrayList<CauseEffectChain> _chains, int _id) {
		for (CauseEffectChain chain : _chains) {
			if (chain.getId() == _id) return chain;
		}
		return null;
	}
}

