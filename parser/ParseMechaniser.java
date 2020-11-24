package parser;

import java.io.File;
import java.io.IOException;
import java.util.*;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import Taskmodel.Application;
import Taskmodel.ConstraintType;
import Taskmodel.DelayConstraint;
import Taskmodel.Task;
import Taskmodel.Time;


public class ParseMechaniser extends ParserCommons{

	public static Application readModel(String _filename) {
		
		Document doc;
		File inputFile = new File(_filename);
		
		Application app = new Application(getFileNameWithoutExtension(inputFile));
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			ParseMechaniser.getTasks(app, doc);
			ParseMechaniser.getChains(app, doc);
			ParseMechaniser.getDependencies(app, doc);
			ParseMechaniser.getJobdep(app, doc);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return app;
	}
	
	/**
	 * This method parses all tasks and generates the respective instances of Task
	 */
	public static void getTasks(Application app, Document doc){
		
		NodeList nList = doc.getElementsByTagName("task");
		for (int temp = 0; temp < nList.getLength(); temp++) 
		{
			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {

				Element eElement = (Element) nNode;
				String name 	= eElement.getAttribute("name");
				String id		= eElement.getAttribute("id");
				String period 	= eElement.getAttribute("period");
				String wcet		= eElement.getAttribute("wcet");
				
				Task tmpTask = new Task(new Time(wcet), new Time(period), name, Integer.parseInt(id));
				app.addTask(tmpTask);
			}
		}
	} 
	
	
	public static void getDependencies(Application app, Document doc) {
		NodeList nList = doc.getElementsByTagName("jobleveldep");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Element eElement = (Element) nList.item(temp);
			int num = Integer.parseInt(eElement.getAttribute("jobstimulus")); 
			if(num == 1) {
				int sti = Integer.parseInt(eElement.getAttribute("stimulus")); 
				int res = Integer.parseInt(eElement.getAttribute("response")); 		
			
				app.addDep(sti, res);
			}
		}	
	}
	
	public static void getJobdep(Application app, Document doc){
		NodeList nList = doc.getElementsByTagName("jobleveldep");
		for(int tmp = 0;tmp<nList.getLength();tmp++) {
			Element eElement = (Element) nList.item(tmp);
			int sti = Integer.parseInt(eElement.getAttribute("stimulus")); 
			int res = Integer.parseInt(eElement.getAttribute("response")); 
			int sti_job = Integer.parseInt(eElement.getAttribute("jobstimulus")); 
			int res_job = Integer.parseInt(eElement.getAttribute("jobresponse")); 
			int[] stimu = {sti,sti_job};
			int[] respon = {res, res_job};
			app.addJobdep(stimu, respon);
		}
	}
	
	
	
	/**
	 * This method parses the cause-effect chains. In order to do so the task set must be provided
	 * @param tasks	The task set of the same xml file
	 * @return The set of cause-effect chains
	 */
	public static void getChains(Application app, Document doc){
		
		NodeList nList = doc.getElementsByTagName("chain");
		
		for (int temp = 0; temp < nList.getLength(); temp++)			//the different chains
		{
			Element eElement = (Element) nList.item(temp);
			
			String name = eElement.getAttribute("name");				//get the name of this chain
			int id = Integer.parseInt(eElement.getAttribute("id")); 	//get the id of the chain
			
			NodeList constraintNodes = eElement.getElementsByTagName("constraint");
			ArrayList<DelayConstraint> constraints = new ArrayList<DelayConstraint>();
			
			for (int tmpConst = 0; tmpConst < constraintNodes.getLength(); tmpConst++)			//the different constraints
			{
				Element constraintTmp = (Element) constraintNodes.item(tmpConst);
				String value = constraintTmp.getAttribute("value");
				String type	 = constraintTmp.getAttribute("type");
				ConstraintType constraintType = ConstraintType.AGE_CONSTRAINT;
				if(type.toLowerCase().equals("age")) constraintType = ConstraintType.AGE_CONSTRAINT;
				else if(type.toLowerCase().equals("reaction")) constraintType = ConstraintType.REACTION_CONSTRAINT;
				constraints.add(new DelayConstraint(new Time(value), constraintType));
			}
			
			//each chain can have a set of paths, in the tool those paths are equivalent to chains!
			NodeList paths = eElement.getElementsByTagName("path");					//this returns all paths 
			for (int tmpPath = 0; tmpPath < paths.getLength(); tmpPath++)			//the different chains as seen by the tool
			{
				
				int chainId = app.createChainFromFile(id, name);
				
				int p = 0;
				
				Element pathCurrent = (Element) paths.item(tmpPath);
				NodeList segments = pathCurrent.getElementsByTagName("pathsegment");
				
				for (int tmpSegment = 0; tmpSegment < segments.getLength(); tmpSegment++){
					Element segment = (Element) segments.item(tmpSegment);
					int taskid = Integer.parseInt(segment.getAttribute("taskid"));
					int place 	 = Integer.parseInt(segment.getAttribute("place"));
					
					if(p == place){
						Task tmpTask = app.getTaskById(taskid);
						app.appendTaskToChain(chainId, tmpTask);
						p++;
					}else{
						System.err.println("Missing element in chain...");
					}
				}
				
				if(constraints.size() > 1) System.err.println("More than one constraint specified for chain " + name + " using last constraint only!");
				for(DelayConstraint c : constraints){	
					app.addChainConstraint(chainId, c);
				}
			}
		}
	}
}
