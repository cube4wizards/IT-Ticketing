package final_project;

import final_project.MyPriorityQueueI.element;

//import final_project.MyPriorityQueueI.element;

/*IF YOU ARE LOOKING FOR THE DRIVER FOR THE WHOLE FINAL PROJECT, IT'S IN THE ITArchive CLASS.*/

public class ITticket extends element{

	String host;
	String username;
	String issue;
	int level;
	int ID;
	static short storeBy=0;
	
	public ITticket(String u, String h, String i, int l){
		//Constructor for credit card offer nodes. Takes the membership fee, CBR, and APR rates.
		//@param fee is the annual membership fee to be stored;
		//@param cbr is the cash back rate to be stored;
		//@param apr is the annual percentage rate to be stored.
		username=u;
		host = h;
		issue=i;
		if (l>=1&&l<=5)
			level=l;
		else
			level=-1;
	}
	
	public double value(){
		//@return the priority value of the ITticket, augmented by the user's current history of whining.
		double userPriority=0;
		int[] userscore = ITArchive.searchUser(username);
		if (userscore[0]==-1)
			return level;
		for (int i=0; i<userscore.length; i++)
			userPriority+=ITArchive.db[userscore[i]].level;
		return level - .05*userPriority;
	}
	
	@Override
	public int hashCode(){
		//@return the hashcode of the ITTicket, based on the criteria determined by ticket's storeby value. 
		//This function is unused. It was created when I was looking for solutions to determining how a hash table would know what criteria
		//was used to store a ticket (username,host,issue) with nothing more than an index number. This was resolved by having the criteria in question
		//be determined by a short in the hash table itself, which is set when the table is instantiated.
		int code=0;
		char temp;
		String current;
		if (storeBy==0)
			current=username;
		if (storeBy==1)
			current=host;
		else
			current=issue;
		for (int i=0; i<current.length();i++){
			temp=current.charAt(i);
			code+=(long)temp*(Math.pow(23,(current.length()-i)));
		}
		return code;
	}
	
	public void print(){
		//Just prints the ITticket in the format it was read from the file in.
		System.out.println(username+"|"+host+"|"+issue+"|"+level);
	}
}
