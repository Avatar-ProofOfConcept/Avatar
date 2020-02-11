package fr.insa.laas.Avatar;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public interface IExtract {
	
	public String ExtractName();
	public String ExtractOwner();
	public double ExtractLatitude();
	public double ExtractLongitude();
	public Map<String, Double> ExtractInterests();
	public ArrayList<Goal> ExtractGoals(ArrayList<String> FunctionsAble,ArrayList<String> FunctionsNotAble);
	public ArrayList<Service> ExtractServices(String name);
	public boolean IsGroupedTask(String task);
	public String ExtractInterestTask(String task,ArrayList<String> FunctionsAble,ArrayList<String> FunctionsNotAble ); 
	public String ExtractLabelTask(String task);
	public boolean IsAbleTask(String task);
	public void ExtractGroupedTask(Task groupedTask,ArrayList<String> FunctionsAble,ArrayList<String> FunctionsNotAble);
	public void ExtractTasks(Goal goal,ArrayList<String> FunctionsAble,ArrayList<String> FunctionsNotAble);
	public boolean IsAbleTaskFriend(String taskLabel);
	public String ExtractServiceFromLabel(String labelService);
 	public ArrayList<MetaAvatar> ExtractMetaAvatars(Set<String> keySet, String name);
}
