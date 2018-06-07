package final_project;
public class ITHashTable {
	//I've been forced to give up on making a generic hash table for this project.
	//I found that, while almost every function worked, I couldn't re-hash the generic table because I had no way of identifying the original hash criteria.
	//(From just the index number, I couldn't tell if the table was supposed to hash tickets by username, host, or issue.)
	//Since being generic locked the ticket and hash table classes out of being able to communicate, it became impossible to store the data in either one for use by both.
	//To rectify this, the "short criteria" was added, and is set to 0, 1, or 2 when the table is instantiated, allowing the table to identify the hash criteria.
	
	/*IF YOU ARE LOOKING FOR THE DRIVER FOR THE WHOLE FINAL PROJECT, IT'S IN THE ITArchive CLASS.*/
	
	int[] theTable;
	final double loadfactor = 0.60;
	long elements;
	short criteria;
	
	public ITHashTable(short c){
		//Basic constructor. Makes an array of the supplied class, and has a default size of 10.
		//@param c determines that the hash table stores. 0 is usernames, 1 is hostnames, 2 is issue type.
		theTable = new int[10];
		elements=0;
		criteria = c;
		for (int i = 0; i<theTable.length; i++)
			theTable[i]=-1;
	}
	
	public ITHashTable(short c, int size){
		//Second constructor. Makes an array of the supplied class, and allows the user to specify the starting size.
		//@param c determines that the hash table stores. 0 is usernames, 1 is hostnames, 2 is issue type.
		//@param size is the starting size of the table.
		theTable = new int[size];
		elements=0;
		criteria=c;
		for (int i = 0; i<theTable.length; i++)
			theTable[i]=-1;
	}
	
	public long hashCode(String archive){
		//@param in is the string to be converted into a hashcode.
		//This function spits out the raw code. It doesn't account for the size of the current table.
		//This... is more or less useless, as the 
		long code=0;
		char temp;
		
		for (int i=0; i<archive.length();i++){
			temp=archive.charAt(i);
			code+=(long)temp*(Math.pow(23,(archive.length()-i)));
		}
		return code;
	}
	
	public long hashCodeM(String archive){
		//@param in is the string to be converted into a hashcode.
		//This version of the function accounts for the size of the current table.
		return hashCode(archive)%theTable.length;//This means I need to override the hashcode function for the object being stored.
	}
	
	public boolean add(ITticket o,int index){
		//@param o is the object to be added to the table.
		//@param in is the string the object should be filed by. This is added in the case that the objects should be stored by something other than it's .toString().
		//For example, the object can be integers, and the string could be a name. (In this case, the int is the index of a message, while the string is the username/host/issue)
		//@return True if the item was successfully added. Returns false if adding fails due to a full table, which should never happen.
		/*Not sure this is needed, since o is of type E.
		 * 
		if (o.getClass()!=l.getClass()){
			System.out.println("Type mismatch: An object "+o.getClass()+" cannot be put into a table of type "+l.getClass());
			return false;
		}
		*/
		int code=0;
		if(criteria==0){
			code=(int)hashCodeM(o.username);//Get the hash code.
			//System.out.println(code);
			//XXX System.out.println(o.username);
		}
		if(criteria==1)
			code=(int)hashCodeM(o.host);
		if(criteria==2)
			code=(int)hashCodeM(o.issue);
		//System.out.println(code);
		boolean flip=false;
		int backup = code;
		/*System.out.println("New Block");
		System.out.println(i);
		while(theTable[i]!=-1&& !flip){//If the slot is filled, move on to the next slot. 
			System.out.println(i);
			System.out.println(theTable[i]);
			i++;
			if (i==theTable.length-1){
				code=0;
				flip=true;
			}
			if (flip&&i==(int)code){
				System.out.println("Critical error: If you see this message, the table is 100% full, and the element has not been added to the table.");
				return false;
			}
		}
		System.out.println(o.username);
		System.out.println("Table Position: "+i+" Index: "+index+" Hashcode: "+(int)code);
		System.out.println(ITArchive.db[index].username);
		*/
		//XXX System.out.println("Index: "+index);
		while(theTable[code]!=-1){
			code++;
			if (code>=theTable.length){
				code=0;
				flip=true;
			}
			if (code==backup&&flip)
				return false;
			//XXX System.out.println(code);
		}
		//XXX System.out.println("Position in hash table: "+code);
		theTable[code]=index;
		elements++;
		if (((double)elements/theTable.length)>loadfactor){
			return resize();
		}
		return true;
	}
	
	@Override
	public String toString(){
		//@returns the string form of the table. Unfortunatley, that means this returns the address of the table, which is useless.
		return theTable.toString();
	}

	private boolean resize() {
		//Resizes the main table and redistributes the elements.
		//@return True if the table is resized successfully.
		int[] temp = theTable;
		theTable = new int[temp.length*2];
		elements=0;
		for (int i = 0; i<theTable.length; i++)
			theTable[i]=-1;
		
		for (int i = 0; i<temp.length; i++){
			//XXX System.out.println(i);
			if (temp[i]!=-1)
			add(ITArchive.db[temp[i]], temp[i]);
		}
		
		return true;
	}
	
	public int find(String in){
		//@param in is the string to be found. The search criteria is determined when the hash table is instantiated.
		//@return the first index number with a matching string in the appropriate field. The returned value is the index number of the item in the ITArchive.db array.
		//This is going to be a mess.
		int code =(int)hashCodeM(in);
		if (theTable[code]==-1)
			return -1;
		boolean flip = false;
		if (criteria==0){
			//System.out.println(code);
			//if (theTable[code]!=-1){
				while(theTable[code]!=-1&&ITArchive.db[theTable[code]].username.compareTo(in)!=0 && !flip){
					code++;
					if(code>=theTable.length){
						flip=true;
						code=0;
					}
				}
			}
		
		//}
		//So yeah, it has a different block for each criteria case. Beats doing the comparisons during each loop of the while statements.
		
		if (criteria==1){
			//if (){
			while(theTable[code]!=-1&&ITArchive.db[theTable[code]].host.compareTo(in)!=0 && !flip){
				code++;
				if(code>=theTable.length){
					flip=true;
					code=0;
				}
			}
		}
		//}
		if (criteria==2){
			//if (theTable[code]!=-1){
			while(theTable[code]!=-1&&ITArchive.db[theTable[code]].issue.compareTo(in)!=0 && !flip){
				code++;
				if(code>=theTable.length){
					flip=true;
					code=0;
				}
			}
		}
		//}

		return theTable[code];
	}

	public void print(){
		//Prints the entire hash table. Used mainly for debugging purposes.
		for (int i = 0; i<theTable.length; i++)
			System.out.print(theTable[i]+" ");
}
	
	public int[] findAll(String in){
		//@param in is the string to be located in the archive. The search criteria is determined when the hash table is instantiated.
		//@return an array of all the indices with the supplied string. The returned array is the same length as the hash table, and unfilled slots contain -1.
		//Since I made the array here larger than it really needed to be, the Search(something)() functions in ITArchive drop the -1s in the table. 
		int code =(int)hashCodeM(in);
		//XXX System.out.println("In front of the auto-eject.");
		if (theTable[code]==-1)
			return new int[] {-1};
		boolean flip = false;
		
		int [] container = new int[theTable.length]; //If the table has any ONE entry filling more than half of it... I will be very surprised.
		int contind=0;
		for (int i = 0; i<container.length; i++)
			container[i]=-1;
		//System.out.println(in);
		if (criteria==0){
			//System.out.println("Correct criteria.");
			
				while(theTable[code]!=-1&& !flip){
					
					//System.out.println("In the while loop.");
					//System.out.println(ITArchive.db[theTable[code]].username);
					//System.out.println(ITArchive.db[theTable[code]].username.compareTo(in));
					if(ITArchive.db[theTable[code]].username.compareTo(in)==0){
						//System.out.println(theTable[code]);
						container[contind]=theTable[code];
						//System.out.println(container[contind]);
						contind++;
					}
						
					code++;
					if(code>=theTable.length){
						flip=true;
						code=0;
					}
				}
			}
		if (criteria==1){
			//if (){
			while(theTable[code]!=-1&& !flip){
				
				//System.out.println("In the while loop.");
				//System.out.println(ITArchive.db[theTable[code]].username);
				//System.out.println(ITArchive.db[theTable[code]].username.compareTo(in));
				if(ITArchive.db[theTable[code]].host.compareTo(in)==0){
					//System.out.println(theTable[code]);
					container[contind]=theTable[code];
					//System.out.println(container[contind]);
					contind++;
				}
					
				code++;
				if(code>=theTable.length){
					flip=true;
					code=0;
				}
			}
		}
		if (criteria==2){
			while(theTable[code]!=-1&& !flip){
				
				//System.out.println("In the while loop.");
				//System.out.println(ITArchive.db[theTable[code]].username);
				//System.out.println(ITArchive.db[theTable[code]].username.compareTo(in));
				if(ITArchive.db[theTable[code]].issue.compareTo(in)==0){
					//System.out.println(theTable[code]);
					container[contind]=theTable[code];
					//System.out.println(container[contind]);
					contind++;
				}
					
				code++;
				if(code>=theTable.length){
					flip=true;
					code=0;
				}
			}
		}
		
		return container;
	}
	
	public int findExact(ITticket target){
		//@param target is the ITticket to locate.
		//@return the index number of the ITticket if an exact copy is found in ITArchive.db. Else, return -1.
		//This function is used to match an ITticket object to an EXACT COPY in the database, through the hashtables.
		//intended for testing use, to make sure that the tables could find an exact object.
		int[] container;
		//for (int i=0; i<ITArchive.searchUser(target.username).length;i++)
			//System.out.println(ITArchive.searchUser(target.username)[i]);
		if (criteria==0){
			container = ITArchive.searchUser(target.username);
				for (int i=0; i<container.length;i++)//It just compares the four variables. It's a REALLY long block though, hence the odd use of return lines.
					if (ITArchive.db[container[i]].username.compareTo(target.username)==0
					&& ITArchive.db[container[i]].host.compareTo(target.host)==0
					&& ITArchive.db[container[i]].issue.compareTo(target.issue)==0
					&& ITArchive.db[container[i]].level==target.level)
						return container[i];
		}
		if (criteria==1){
			container = findAll(target.host);
			for (int i=0; i<container.length;i++)//It just compares the four variables. It's a REALLY long block though, hence the odd use of return lines.
				if (ITArchive.db[container[i]].username.compareTo(target.username)==0
				&& ITArchive.db[container[i]].host.compareTo(target.host)==0
				&& ITArchive.db[container[i]].issue.compareTo(target.issue)==0
				&& ITArchive.db[container[i]].level==target.level)
					return container[i];
		}
		if (criteria==2){
			container = findAll(target.issue);
			for (int i=0; i<container.length;i++)//It just compares the four variables. It's a REALLY long block though, hence the odd use of return lines.
				if (ITArchive.db[container[i]].username.compareTo(target.username)==0
				&& ITArchive.db[container[i]].host.compareTo(target.host)==0
				&& ITArchive.db[container[i]].issue.compareTo(target.issue)==0
				&& ITArchive.db[container[i]].level==target.level)
					return container[i];
		}
		
		return 0;
	}
}
