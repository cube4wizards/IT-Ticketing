package final_project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import final_project.MyPriorityQueueI.element;

public class ITArchive {
	static MyPriorityQueueI theQueue = new MyPriorityQueueI();
	public static ITticket[] db;
	public static int index=0;
	static ITHashTable usernames = new ITHashTable((short)0);
	static ITHashTable hosts = new ITHashTable((short)1);
	static ITHashTable issues = new ITHashTable((short)2);
	
/*
 * So, this project.
 * Need to make a full IT center. Huzzah.
 * So, the PriorityQueue does... Uh... Well, it loads things. 
 * 		-Okay, so it loads in the data and sorts it according to the user's priority level. Then they ar "processed", which ignores, outputs messages, and stores stuff in the archive.
 * 	-Need to read in Messages/txt, and process messages when you hit a break.
 * 
 * The actual archive is an array. Probably a linked list. 
 * 
 * Hash table is openly addressed an linearly searched. It contains ONLY the ID (index value?) of the logged message. It's basically just a key translator.
 * 
 * DONE Priority Queue has been reworked to be generic. Will work as long as the classes used in it extend element and override the value function.
 * DONE Unless the hash tables magically explode, I shouldn't need to crack that file open again.
 * DONE The archive itself is made. 
 * DONE Pull it all together under this class. I think the only thing left is to make the main database resize itself, and  test the crap out of it.
 * DONE Hash code functions complete.
 * TODO Add comments to the code to add to readablilty, and make a second version that's contained in one file.
 * The graders can pick which version they want to try working with.
 * 
 * So I think I just need three hash tables- Username, Host, Issue. 
 * They're hashed to the specific trait. In order to retrieve all of the info under one attribute, you jump to the first entry under that name, and read till the block ends.
 * 		-Since entries under the same criteria should create blocks, you'll literally just need to read the block to get EVERYTHING under that criteria.
 * */

	
	public static boolean reader(BufferedReader in){
		//@param in is a buffered reader. This takes a buffered reader as an argument so a new one doesn't need to be made each run, and the one in use keeps its place in the file being read.
		//@return True if it hits a break, and there is more to read. Returns false when it hits the file's end. Makes it easy to use with a while statement.
		String username = "";
		String hostname = "";
		String issue = "";
		int priority = -1;
		String container;
		try{
			for(String inputLine= in.readLine(); inputLine!=null; inputLine=in.readLine())
			{
				// split string and only process first number read
				String[] splits = inputLine.split("\\|");
				//System.out.println("Read: "+ inputLine);
		
				if (inputLine.contentEquals("break")){
					return true;
				}
				if(splits[0].length()!=0)
				{
					for(int j=0;j<splits.length; j++)
					{
						container = splits[j];
						//System.out.println("Next input: "+ container);
						if (j==0)
							username=container;
						if (j==1)
							hostname=container;
						if (j==2)
							issue=container;
						if (j==3)
							priority=Integer.parseInt(container);
					}
					theQueue.offer(new ITticket(username,hostname,issue,priority));
					System.out.println("Adding new item to the Queue: "+username+"|"+hostname+"|"+issue+"|"+priority);
				}
			}
			in.close();
		}
		catch(IOException e){
			System.out.println("File I/O error");
			System.exit(1);
		}
		return false;
	}
	
	public static boolean archive(){
		//@returns true is the archiving process is successful.
		//Archives items in theQueue if there isn't a copy of said item in the db archive. Prints messages if the messages meet certain conditions.
		
		while(!theQueue.isEmpty()){
			
			if(index==db.length-1){
				ITticket[] temp=db;
				db=new ITticket[temp.length*2];
				for(int i=0; i<temp.length;i++)
					db[i]=temp[i];
			}
			//ITticket tem=(ITticket) theQueue.peek();
			ITticket temp = (ITticket) theQueue.remove();
			System.out.println(temp.username+"|"+temp.host+"|"+temp.issue+"|"+temp.level+"|"+temp.value()+" Value based on current db elements- may not be the same val the queue used.");
			//temp.print();
			//System.out.println(temp.username);
			boolean stop = false;
			
			//Tests to make sure the ticket should be archived.
			if (temp.level>5||temp.level<1)//Checks the message level, not the user's priority.
				stop=true;
			short state=0;
			int[]check=searchIssue(temp.issue);
			/*
			for(int i = 0; i<check.length; i++){
				System.out.println(check[i]);
			}
			*/
			if (check[0]!=-1){
			for (int i = 0; i<check.length; i++){
				if (db[check[i]].issue.compareTo(temp.issue)==0){
					if (db[check[i]].host.compareTo(temp.host)==0){
						if(db[check[i]].username.compareTo(temp.username)==0){
							stop=true;//All three are the same, so the issue was already reported by the same user. Ignore the message, and do not add it to the archive.
							break;//No point in letting it keep looping- it's already reached the last case.
						}
						else{
							//Same issue and host.
							if (state<2)//Can't use the print statements in the loop, so it changes the state so the prints can be done later.
								state=2;
						}
					}
					else{
						//Just same issue.
						if (state<1)
							state=1;
						
					}
				}
			}
			}
			if (!stop){
				if(state==1){
					System.out.println("A "+temp.issue+" problem has been solved before, so this might be fixed in a reasonable period of time.");
					System.out.println();
				}
				if(state==2){
					System.out.println("A "+temp.issue+" problem has been solved on "+temp.host+" before, so have no fear, "+temp.username+", it'll be fixed soon!");
					System.out.println();
				}
				//else, state=0, meaning we have a totally new report.
				temp.ID=index;
			db[index]=temp;
			usernames.add(temp, index);
			//usernames.print();
			//System.out.println();
			//System.out.println();
			hosts.add(temp, index);
			issues.add(temp, index);
			index++;
			}
		}
		return true;
	}
	
	public static int[] searchUser(String in){
		//@param in is the string representing a username.
		//@return Returns an array with the indices of every message in the array db containing the supplied username.
		int[] n = usernames.findAll(in);
		int i=0;//this needs to be persistent.
		if (n[0]!=-1){
			while (n[i]!=-1)
				i++;
		int[] ret = new int[i];
			for (i = 0; i<ret.length; i++){
				ret[i]=n[i];
			}
			return ret;
		}
		else{
			//System.out.println(in+" not found in archive.");
			return new int[] {-1};
		}
	}
	
	public static int[] searchHost(String in){
		//@param in is the string representing a hostname.
		//@return Returns an array with the indices of every message in the array db containing the supplied hostname.
		
		int[] n = hosts.findAll(in);
		int i=0;//this needs to be persistent.
		if (n[0]!=-1){
			while (n[i]!=-1)
				i++;
		int[] ret = new int[i];
			for (i = 0; i<ret.length; i++){
				ret[i]=n[i];
			}
			return ret;
		}
		else{
			//System.out.println(in+" not found in archive.");
			return new int[] {-1};
		}
	}

	public static int[] searchIssue(String in){
		//@param in is the string representing an issue.
		//@return Returns an array with the indices of every message in the array db containing the supplied issue.
		
		int[] n = issues.findAll(in);
		int i=0;//this needs to be persistent.
		if (n[0]!=-1){
			while (n[i]!=-1)
				i++;
		int[] ret = new int[i];
			for (i = 0; i<ret.length; i++){
				ret[i]=n[i];
			}
			return ret;
		}
		else{
			//System.out.println(in+" not found in archive.");
			return new int[] {-1};
		}
	}

	public static void main(String args[]){
		//Driver for the entire final project.
		
	BufferedReader in = null;//This sets the reader up so that it's persistent across uses of the reader method.
	
	try {//This block initializes the reader, since it needs to be done in a try/catch.
		in = new BufferedReader(new FileReader(new File("messages.txt")));//TODO change this to work from the java folder.... I think.
	} catch (FileNotFoundException e1) {
		System.out.println("File I/O error");
		System.exit(1);
		e1.printStackTrace();
	}
	
	db=new ITticket[10];//db wasn't initialized above. I'm initializing it here.
	
	while(reader(in)){//This while statement runs the queue and archiving, though it appears to stop one round short.
		//theQueue.showList(); //Uncomment this to see the priority queue before each archiving attempt.
		System.out.println("Break reached- Processing queue.");
		System.out.println();
		archive();
		System.out.println("Processing complete- Checking for more data.");
		System.out.println();
	}
	//theQueue.showList(); //Uncomment this to see the priority queue before each archiving attempt.
	System.out.println("Break reached- Processing queue.");
	System.out.println();
	archive();//Cause the while ends before the final round of archiving.
	System.out.println("Processing complete. End of file reached. Proceeding with verification of hash tables.");
	

	//ITticket holder = (ITticket) theQueue.peek();
	//System.out.println((holder.username));
	
	
	/*Testing cases for the find function.
	usernames.print();
	System.out.println();
	hosts.print();
	System.out.println();
	issues.print();
	System.out.println();
	/*
	int t=usernames.find("Winter");
	if (t!=-1)
		db[t].print();
	else
		System.out.println("Username not in the file.");
	
	t=hosts.find("OGPlanet");
	if (t!=-1)
		db[t].print();
	else
		System.out.println("Hostname not in the file.");
	
	t=issues.find("Firewall");
	if (t!=-1)
		db[t].print();
	else
		System.out.println("Issue not in the file.");
	
	*/

	
	
	/*
	System.out.println(usernames.hashCodeM("cube4wizards"));
	System.out.println(usernames.find("cube4wizards"));
	db[usernames.find("cube4wizards")].print();
	System.out.println(usernames.find("cube4wizards"));
	
	
	
	int[] cont = searchUser("Winter");
	for (int i=0; i<cont.length; i++){
		db[cont[i]].print();
	}
	*/
	
	int k = 0;
	int h = 0;
	int l = 0;
	for (int i=0; i<index;i++){
		if (i==usernames.findExact(db[i]))
					k++;
		if (i==issues.findExact(db[i]))
			l++;
		if (i==hosts.findExact(db[i]))
			h++;
	}
	
	System.out.println();
	System.out.println("Usernames verified: "+k);
	System.out.println("Hosts verified: "+h);
	System.out.println("Issues verified: "+l);
	System.out.println("Total Entries: "+index);
	System.out.println();
	
	
	
	if (k==h&&h==l&&l==index)
		System.out.println("All entries verified for all three criteria.");
	else
		System.out.println("Uh, Alex? We have a problem. Something didn't verify.");
	
	//System.out.println(db.length);
	}
	
}

