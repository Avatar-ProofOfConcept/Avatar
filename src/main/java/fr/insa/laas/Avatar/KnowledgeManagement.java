package fr.insa.laas.Avatar;

import java.util.ArrayList;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

 

 

public class KnowledgeManagement implements IExtract{
	private Model modelData;
	String urlKB;
 	public KnowledgeManagement(String url)
	{    this.urlKB=url;
		this.modelData = ModelFactory.createDefaultModel();
        this.modelData.read(this.urlKB);
	}
	
	public String ExtractName(){ 
		String nameTmp=new String();
		String queryString = 
	    		"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"+ 
	    		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
	    	    "PREFIX avataront: <http://www.laas-cnrs.fr/recherches/SARA/ontologies/AvatarOnt#>\n"+

	    	        "SELECT ?avatar "+
	    	        "WHERE {?avatar rdf:type avataront:Avatar ."+
	    	        "}";
		   
	    	    Query query = QueryFactory.create(queryString);
	    	    QueryExecution qe = QueryExecutionFactory.create(query, modelData);
	    	    ResultSet results =  qe.execSelect();
	    	    //ResultSetFormatter.out(System.out, results);
	    	    while(results.hasNext()){ 
	    	    	QuerySolution binding = results.nextSolution(); 
	    	    	nameTmp= binding.get("avatar").toString().split("#")[1];
	    	    }
	    	    return nameTmp;
	}
	public String ExtractOwner(){ 
		String ownerTmp=new String();
		String queryString = 
	    		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
	    	    "PREFIX avataront: <http://www.laas-cnrs.fr/recherches/SARA/ontologies/AvatarOnt#>\n"+
	    	        "SELECT ?owner "+
	    	        "WHERE { "+   
	    	         "?avatar avataront:hasOwner ?owner ."+ 
	    	        "}";
		 
	    	    Query query = QueryFactory.create(queryString);
	    	    QueryExecution qe = QueryExecutionFactory.create(query, modelData);
	    	    ResultSet results =  qe.execSelect();
	    	    //ResultSetFormatter.out(System.out, results);
	    	    while(results.hasNext()){ 
	    	    	QuerySolution binding = results.nextSolution(); 
	    	    	ownerTmp=binding.get("owner").toString();
	    			//System.out.println("[EXTRACTOWNER] "+name+": "+owner) ;		
	    	    }
	    	    return ownerTmp;
	}
	
	//Get its Location
	public double ExtractLatitude(){ 
		double latitudeTmp=0;
		String queryString = 
	    		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
	    	    "PREFIX avataront: <http://www.laas-cnrs.fr/recherches/SARA/ontologies/AvatarOnt#>\n"+
	    	        "SELECT ?location "+
	    	        "WHERE { "+   
	    	         "?avatar avataront:hasLocation ?location ."+ 
	    	        "}";
		   
	    	    Query query = QueryFactory.create(queryString);
	    	    QueryExecution qe = QueryExecutionFactory.create(query, modelData);
	    	    ResultSet results =  qe.execSelect();
	    	    //ResultSetFormatter.out(System.out, results);
	    	    while(results.hasNext()){ 
	    	    	QuerySolution binding = results.nextSolution(); 
	    	    	latitudeTmp = Double.parseDouble(binding.get("location").toString().split("/")[0]);
 	    	    	//System.out.println("[EXTRACTLOC] "+name+": "+latitude+"/"+longitude) ;		
	    	    }
	    	    return latitudeTmp;
	}
	public double ExtractLongitude(){ 
		double longitudeTmp=0;
		String queryString = 
	    		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
	    	    "PREFIX avataront: <http://www.laas-cnrs.fr/recherches/SARA/ontologies/AvatarOnt#>\n"+
	    	        "SELECT ?location "+
	    	        "WHERE { "+   
	    	         "?avatar avataront:hasLocation ?location ."+ 
	    	        "}";
		   
	    	    Query query = QueryFactory.create(queryString);
	    	    QueryExecution qe = QueryExecutionFactory.create(query, modelData);
	    	    ResultSet results =  qe.execSelect();
	    	    //ResultSetFormatter.out(System.out, results);
	    	    while(results.hasNext()){ 
	    	    	QuerySolution binding = results.nextSolution(); 
	    	    	longitudeTmp=Double.parseDouble(binding.get("location").toString().split("/")[1]);
 	    	    	//System.out.println("[EXTRACTLOC] "+name+": "+latitude+"/"+longitude) ;		
	    	    }
	    	    return longitudeTmp;
	}
	//Get all its interests from the semantic data
		public ArrayList<Interest> ExtractInterests(){ 
			ArrayList<Interest> interestsListTmp=new ArrayList<Interest>();
			String queryString = 
		    		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
		    	    "PREFIX avataront: <http://www.laas-cnrs.fr/recherches/SARA/ontologies/AvatarOnt#>\n"+
		    	        "SELECT ?interest "+
		    	        "WHERE { "+   
		    	         "?avatar avataront:hasInterest ?interest ."+
		    	         "?avatar rdf:type avataront:Avatar ."+
		    	        "}";

				    Query query = QueryFactory.create(queryString);
				    QueryExecution qe = QueryExecutionFactory.create(query, modelData);
				    ResultSet results =  qe.execSelect();
				    //ResultSetFormatter.out(System.out, results);
				    String name2 = null;
				    
				    //For each Interest
				    while(results.hasNext()){ 
				    	QuerySolution binding = results.nextSolution(); 
				    	name2=binding.get("interest").toString();
		    	    	//Name and level Interest split
		    	    	String [] parts = name2.split("/");
		    	    	interestsListTmp.add(new Interest(parts[0],Double.parseDouble(parts[1])));
		    	    	//interestsVector.put(parts[0],Double.parseDouble(parts[1]));
		    	    }
				 for(int i=0;i< interestsListTmp.size();i++)
				 {
						System.out.println("[EXTRACTINTERETS] "+interestsListTmp.get(i).getName());

					 
				 }
					System.out.println("list size: "+interestsListTmp.size());
					return interestsListTmp;
		}
 

		public ArrayList<Goal> ExtractGoals(ArrayList<String> InteretsTasksList){ 
			ArrayList<Goal> goalsList=new ArrayList<Goal>();
			String queryString = 
		    	    "PREFIX avataront: <http://www.laas-cnrs.fr/recherches/SARA/ontologies/AvatarOnt#>\n"+
		    	        "SELECT ?goal "+
		    	        "WHERE { "+   
		    	         "?avatar avataront:hasGoal ?goal ."+ 
		    	        "}";
			   
		    	    Query query = QueryFactory.create(queryString);
		    	    QueryExecution qe = QueryExecutionFactory.create(query, modelData);
		    	    ResultSet results =  qe.execSelect();
		    	    //ResultSetFormatter.out(System.out, results);
		    	    String name = "test";
		    	    
		    	    //For each goal
		    	    while(results.hasNext()){ 
		    	    	QuerySolution binding = results.nextSolution(); 
		    	    	name=binding.get("goal").toString();
 		    	    	//We create an instance of goal
		    	    	Goal newGoal = new Goal(name);
		    	    	ExtractTasks(newGoal,InteretsTasksList);
		    	    	goalsList.add(newGoal);
		    	    }
			return goalsList;	
		}
		//Get all its services from the semantic data
		public ArrayList<Service> ExtractServices(String name){ 
			ArrayList<Service> servicesListTmp=new ArrayList<Service>();

			String queryString = 
		    		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
		    	    "PREFIX avataront: <http://www.laas-cnrs.fr/recherches/SARA/ontologies/AvatarOnt#>\n"+
		    	        "SELECT   ?service ?operation ?omsg ?met ?lab "+
		    	        "WHERE { "+   
		    	         "?avatar avataront:hasService ?service ."+
			    	     "?service avataront:hasLabel ?lab ."+
		    	         "?service <http://iserve.kmi.open.ac.uk/ns/msm#hasOperation>  ?operation ."+
		    	         "?operation <http://www.laas-cnrs.fr/recherches/SARA/ontologies/AvatarOnt#hasOutputMessage>  ?omsg ."+
		    	         "?operation <http://www.laas-cnrs.fr/recherches/SARA/ontologies/AvatarOnt#hasMethod>  ?met ."+
		    	         //TBD: Search the input msg if it's a PUT Method
		    	         //"?operation <http://www.laas-cnrs.fr/recherches/SARA/ontologies/AvatarOnt#hasInputMessage>  ?imsg ."+
		    	         "}";

				    Query query = QueryFactory.create(queryString);
				    QueryExecution qe = QueryExecutionFactory.create(query, modelData);
				    ResultSet results =  qe.execSelect();
				    //ResultSetFormatter.out(System.out, results);
				    
				    //For each Service
				    //TBD: Each Service may have many operations
				    while(results.hasNext()){ 
				    	QuerySolution binding = results.nextSolution(); 
				    	//TBD: Check if a service has many ops
				    	String service=binding.get("service").toString();
				    	String serviceOp=binding.get("operation").toString();
				    	String outputMsg=binding.get("omsg").toString();
				    	//String inputMsg=binding.get("imsg").toString();
				    	String method=binding.get("met").toString();
				    	String label=binding.get("lab").toString();

				    	//Create a service instance and add it to the list
				    	ServiceOperation sOP = new ServiceOperation(serviceOp, method, "inputMSG:TBD", outputMsg);
				    	Service serv = new Service(name, service, label, sOP);
				    	servicesListTmp.add(serv);
				    	//System.out.println("			"+name+"   SIZE:"+servicesList.size() +"   adding serv  "+service);
		    	    }
				    return servicesListTmp;
		}	
		/*	--------------------------------------- 		T A S K S 	M E T H O D S		---------------------------------------------	*/
		/*																																	*/
		/*	-----------------------------------------------------------------------------------------------------------------------------	*/
		
		
		//Check if it is an atomic task or a composite task
		public boolean IsGroupedTask(String task){
				//System.out.println("Task to test: "+task);
				String queryString = "PREFIX DEMISA: <http://www.semanticweb.org/kkhadir/ontologies/2019/1/DEMISA#>\n" +
			    		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
			    	    "PREFIX avataront: <http://www.laas-cnrs.fr/recherches/SARA/ontologies/AvatarOnt#>\n"+				
						" ASK {<"+task+"> rdf:type DEMISA:GroupedTask .}";			
			    Query query = QueryFactory.create(queryString) ;
			    QueryExecution qexec = QueryExecutionFactory.create(query, modelData) ;
			    boolean b = qexec.execAsk();
			    //ResultSetFormatter.out(System.out, b);
			    qexec.close() ;   
				return b;	
			}
		public String ExtractInterestTask(String task,ArrayList<String> InteretsTasksList ){ 
			String name2=null;
			String queryString =  
		    		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
		    	    "PREFIX avataront: <http://www.laas-cnrs.fr/recherches/SARA/ontologies/AvatarOnt#>\n"+
		    	        "SELECT ?interest "+
		    	        "WHERE {<"+ task + "> avataront:hasFunction ?interest ."+
		    	        "}";
		    	    Query query = QueryFactory.create(queryString);
		    	    QueryExecution qe = QueryExecutionFactory.create(query, modelData);
		    	    ResultSet results =  qe.execSelect();
		    	    //ResultSetFormatter.out(System.out, results);
		    	    
		    	    while(results.hasNext()){ 
		    	    	QuerySolution binding = results.nextSolution(); 
		    	    	name2=binding.get("interest").toString();	
		    	    	//System.out.println("[EXTRACTINTERESTTASK: ]"+name+": "+task+" has the interest: "+name2);
		    	    	//We add this interest to the InterestsTasks List if it's not already in 
		    	    	if(!InteretsTasksList.contains(name2)){
		    	    		InteretsTasksList.add(name2);
		    	    	}
		    	    }
		    return name2;
		}
		public String ExtractLabelTask(String task){ 
			String name2=null;
			String queryString = 
		    		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
		    	    "PREFIX avataront: <http://www.laas-cnrs.fr/recherches/SARA/ontologies/AvatarOnt#>\n"+
		    	        "SELECT ?label "+
		    	        "WHERE {<"+ task + "> avataront:hasLabel ?label ."+
		    	        "}";
		    	    Query query = QueryFactory.create(queryString);
		    	    QueryExecution qe = QueryExecutionFactory.create(query, modelData);
		    	    ResultSet results =  qe.execSelect();
		    	    //ResultSetFormatter.out(System.out, results);
		    	    while(results.hasNext()){ 
		    	    	QuerySolution binding = results.nextSolution(); 
		    	    	name2=binding.get("label").toString();	
		    	    }
	    	//System.out.println("[EXTRACTLABELTASK: ]"+name+": "+task+" has the label: "+name2);
		    return name2;
		}
		public boolean IsAbleTask(String task){
			//System.out.println("TASK FROM ISABLE: "+task);
			String queryString = 
		    		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
		    		"PREFIX iserve: <http://iserve.kmi.open.ac.uk/ns/msm#>"+
		    	    "PREFIX avataront: <http://www.laas-cnrs.fr/recherches/SARA/ontologies/AvatarOnt#>\n"+
					" ASK {<"+
						task + "> avataront:hasLabel ?label ."+
						"?service avataront:hasLabel ?label ."+
						"?service rdf:type iserve:Service ."+
						"}";

			Query query = QueryFactory.create(queryString) ;
		    QueryExecution qexec = QueryExecutionFactory.create(query, modelData) ;
		    boolean b = qexec.execAsk();
		    //ResultSetFormatter.out(System.out, b);
		    qexec.close() ;
		    return b;	
		}
		public void ExtractGroupedTask(Task groupedTask,ArrayList<String> InteretsTasksList){ 
			String queryString =  
		    		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
		    	    "PREFIX avataront: <http://www.laas-cnrs.fr/recherches/SARA/ontologies/AvatarOnt#>\n"+
		    	        "SELECT ?task "+
		    	        "WHERE {<"+   
		    	        groupedTask.getContent()+">" + " avataront:hasChildTask ?task ."+
		    	        "}";
			   
		    	    Query query = QueryFactory.create(queryString);
		    	    QueryExecution qe = QueryExecutionFactory.create(query, modelData);
		    	    ResultSet results =  qe.execSelect();
		    	    //ResultSetFormatter.out(System.out, results);
	 
		    	    String name2 = null;
		    		ArrayList <Task> tasksList = new ArrayList <Task>() ;	//Will contain all the sub tasks
	    	    	//System.out.println("[EXTRACTGROUPEDTASK]");
		    	    
		    	    //For each Task
		    	    while(results.hasNext()){ 
		    	    	QuerySolution binding = results.nextSolution(); 
		    	    	name2=binding.get("task").toString();	
		    	        	    	
		    	    	//We create a new Task
		    	    	String interest=ExtractInterestTask(name2,InteretsTasksList);
		    	    	String label=ExtractLabelTask(name2);
			    	    Task newTask = new Task(name2,false,IsAbleTask(name2),interest, label);
		    	    	tasksList.add(newTask);
		    	    }
		    	    groupedTask.majTasksList(tasksList);	
		}
		public void ExtractTasks(Goal goal,ArrayList<String> InteretsTasksList){ 
			//System.out.println("We extract the tasks of "+goal.getName());
			String queryString = 
		    		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
		    	    "PREFIX avataront: <http://www.laas-cnrs.fr/recherches/SARA/ontologies/AvatarOnt#>\n"+

		    	        "SELECT ?task "+
		    	        "WHERE {<"+   
		    	         goal.getName()+">" + " avataront:hasChildTask ?task ."+
		    	        "}";
			   
		    	    Query query = QueryFactory.create(queryString);
		    	    QueryExecution qe = QueryExecutionFactory.create(query, modelData);
		    	    ResultSet results =  qe.execSelect();
		    	    //ResultSetFormatter.out(System.out, results);
		    	    String name2 = null;
		    	    
		    	    //For each Task
		    	    while(results.hasNext()){ 
		    	    	QuerySolution binding = results.nextSolution(); 
		    	    	name2=binding.get("task").toString();	
		    	    			   	
		    	    	String interest=ExtractInterestTask(name2,InteretsTasksList);
		    	    	String label=ExtractLabelTask(name2);
		    	    	
		    	    	//Check if it's a composed task
		    	    	if(IsGroupedTask(name2)){
			   				 //System.out.println(name2+" :Composed");		
				    	     Task newTask = new Task(name2,true,IsAbleTask(name2),interest,label);
			   				 ExtractGroupedTask(newTask, InteretsTasksList);
					    	 goal.addTask(newTask);  
			   			 }
			    	    else{
			   				 //System.out.println(name2+" :Not Composed");	    	    	
				    	     Task newTask = new Task(name2,false,IsAbleTask(name2),interest,label);
					    	 goal.addTask(newTask);  
			    	    }    	
		    	    }
		}
		//Check if he can realize a task for a friend <=> If he has a service with a similar label than the label asked for
		public boolean IsAbleTaskFriend(String taskLabel){
			String queryString = 
		    		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
		    		"PREFIX iserve: <http://iserve.kmi.open.ac.uk/ns/msm#>"+
		    	    "PREFIX avataront: <http://www.laas-cnrs.fr/recherches/SARA/ontologies/AvatarOnt#>\n"+
					" ASK {"+
						"?service avataront:hasLabel \""+taskLabel+"\" ."+
						"?service rdf:type iserve:Service ."+
						"}";

			Query query = QueryFactory.create(queryString) ;
		    QueryExecution qexec = QueryExecutionFactory.create(query, modelData) ;
		    boolean b = qexec.execAsk();
		    //ResultSetFormatter.out(System.out, b);
		    qexec.close() ;
		    return b;	
		}
		//Get the service (its name, Label and QoS) with a certain label
		public String ExtractServiceFromLabel(String labelService){ 
			String name2="ExtractServiceERROR"; String name3="ExtractServiceERROR"; String name4="ExtractServiceERROR";
			String queryString =
		    		"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"+
		    	    "PREFIX avataront: <http://www.laas-cnrs.fr/recherches/SARA/ontologies/AvatarOnt#>\n"+
		    		"PREFIX iserve: <http://iserve.kmi.open.ac.uk/ns/msm#>"+
		    	        "SELECT  ?service ?label ?qos "+
		    	        "WHERE {?service avataront:hasLabel \""+labelService+"\" ."+
		    	        "?service avataront:hasLabel ?label ."+
		    	        "?service <http://www.laas-cnrs.fr/recherches/SARA/ontologies/AvatarOnt#hasQoS> ?qos ."+
						"?service rdf:type iserve:Service ."+
		    	        "}";
			   
		    	    Query query = QueryFactory.create(queryString);
		    	    QueryExecution qe = QueryExecutionFactory.create(query, modelData);
		    	    ResultSet results =  qe.execSelect();
		    	    //ResultSetFormatter.out(System.out, results);
		    	    
		    	    while(results.hasNext()){ 
		    	    	QuerySolution binding = results.nextSolution(); 
		    	    	name2=binding.get("service").toString();
		    	    	name3=binding.get("label").toString();	
		    	    	name4=binding.get("qos").toString();	
		    	    	//System.out.println("[EXTRACT SERVICE : ]"+name+": "+labelService+"  "+binding.get("qos").toString());	
		    	    }
		    return name2+"&"+name3+"&"+name4;
		}

}
