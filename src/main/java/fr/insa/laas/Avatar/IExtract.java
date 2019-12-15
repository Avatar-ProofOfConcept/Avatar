package fr.insa.laas.Avatar;

import java.util.ArrayList;

public interface IExtract {
	
	public String ExtractName();
	public String ExtractOwner();
	public double ExtractLatitude();
	public double ExtractLongitude();
	public ArrayList<Interest> ExtractInterests();
	public ArrayList<Goal> ExtractGoals(ArrayList<String> InteretsTasksList);
	public ArrayList<Service> ExtractServices(String name);
	public boolean IsGroupedTask(String task);
	public String ExtractInterestTask(String task,ArrayList<String> InteretsTasksList);
	public String ExtractLabelTask(String task);
	public boolean IsAbleTask(String task);
	public void ExtractGroupedTask(Task groupedTask,ArrayList<String> InteretsTasksList);
	public void ExtractTasks(Goal goal,ArrayList<String> InteretsTasksList);
	public boolean IsAbleTaskFriend(String taskLabel);
	public String ExtractServiceFromLabel(String labelService);
}
