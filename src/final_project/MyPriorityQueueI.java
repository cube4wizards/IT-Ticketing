package final_project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MyPriorityQueueI {
	
	/*IF YOU ARE LOOKING FOR THE DRIVER FOR THE WHOLE FINAL PROJECT, IT'S IN THE ITArchive CLASS.*/
	
/*
 * Okay, this is odd. This version of the queue is modified so that the creditOffer class is now called "element", and the element class is abstract.
 * That this SHOULD do, is allow me to use the queue on any class that extends the element class, effectively allowing this queue to be generic. 
 * Unless the value method of the element class is overridden, the value of every item being entered will be treated as 0.
 * BUT, assuming the override is done, this class SHOULD be usable with any data structure. (Once again, as long as it extends element.)
 * 
 * 
 * 
 * 
 * */
	
	MyMaxHeap theQueue;
	
	public MyPriorityQueueI(){
		//Constructor for the Queue.
		//@param montlyExpenses is the amount of expected monthly expenses used to calculate the score of credit card offers.
		theQueue=new MyMaxHeap();
	}
	
	public MyPriorityQueueI(element[] arr){
		//Expanded constructor, accepting all elements needed for the queue.
		//@param arr takes an array of elements.
		//@param n is the number of elements in array arr.
		//@param montlyExpenses is the amount of money used to calculate the value of elements.
		theQueue=new MyMaxHeap(arr);
	}
	
	public boolean offer(element co){
		//@param co is a element to be added to the queue.
		//@return Returns true if add is successful. Returns false if the world breaks, or if the queue is at maximum capacity.
		return theQueue.add(co);
	}
	
	public element remove(){
		//@return Returns and removes the element with the largest value from the queue.
		return theQueue.removeMax();
	}
	
	public element peek(){
		//@return the element with the largest value. Does not remove it from the queue.
		return theQueue.theHeap[1]; //The underlying array SHOULD use 1 as the root index, with 0 being empty. I'll need to make sure that shift doesn't come back to bite me.
	}
	
	public boolean isEmpty(){
		//@return True if the queue is empty. Returns false otherwise.
		if (theQueue.size()==0)
			return true;
		else
			return false;
	}
	
	public void showList(){
		//Prints the underlying array.
		theQueue.showList();
	}
	
	
	public static void main (String args[]){
		//This isn't going to be pretty. The raw number of variables I'm going to have to make for this...
		double fee=0;
		double cbr=0;
		double apr=0;
		double number;
		short c=0;
		element[] test = new element[6];
		//This part's going to contain the doublereader skeleton code, modified however I need to make it load the data from the given file.
		//This... is going to be inelegant as hell.
		try{
			BufferedReader in= new BufferedReader(new
			FileReader(new File("src/offers.txt")));  //TO THE TA'S: I'm sorry, this is the only way I could make java read the file. 
			for(String inputLine= in.readLine(); inputLine!=null; inputLine=in.readLine())
			{
				// split string and only process first number read
				String[] splits = inputLine.split(" ");
				System.out.println("Read: "+ inputLine);
			
				if(splits[0].length()!=0)
				{
					for(int j=0;j<splits.length; j++)
					{
						number = Double.parseDouble(splits[j]);
						System.out.println("Next input: "+ number);
						if (j==0)
							fee=number;
						if (j==1)
							cbr=number;
						if (j==2)
							apr=number;
						if (j>2)
							System.out.println("Uh... Are you SURE you're using the correct source file?");
					}
				}
				test[c]=new creditOffer(fee,cbr,apr);//I said this would be inelegant, didn't I?
				c++;//Ha. Programming language references.
				System.out.println(fee + " " + cbr + " " + apr);
			}
			in.close();
		}
		catch(IOException e){
			System.out.println("File I/O error");
			System.exit(1);
		}
	
		MyPriorityQueueI thingy = new MyPriorityQueueI(test);
		thingy.showList();
		System.out.println("Adding 7th element");
		thingy.offer(new creditOffer(0,.04,.05));
		thingy.showList();
		while (!thingy.isEmpty()){
			thingy.remove();
			thingy.showList();
		}
		//Took a few edits to the second constructor (but that's why testing is a thing) and this appears to work too. Guess that means it's submission time.
		
		
		/*
		 * This block tests the Priority Queue. Hits all functions EXCEPT the overloaded constructor.
		 * 
		MyPriorityQueue test1 = new MyPriorityQueue(10000);
		System.out.println(test1.isEmpty());
		for (int i = 0; i<6; i++){
			test1.offer(new element(-1000*i, .1*i, .01*i));
			test1.showList();
		}
		System.out.println(test1.peek());
		System.out.println(test1.isEmpty());
		
		for (int i = 0; i<6; i++){
			test1.remove();
			test1.showList();
		}
		*/
		
		
		/*This block tests the MaxHeap. Tests all functions EXCEPT the overloaded constructor.
		 * If this block works as expected, the PriorityQueue block should run just as well.
		 * 
		MyMaxHeap test2 = new MyMaxHeap(10000);
		for (int i = 0; i<6; i++){
			test2.add(new element(-1000*i, .1*i, .01*i));
			test2.showList();
		}
		test.showList();
		
		for (int i = 0; i<6; i++){
			test2.removeMax();
			test2.showList();
		}
		*/
	}
	
	
	
	
	
	private static class MyMaxHeap{
		//The Max Heap- a data structure that will make the root the highest number, with each child being smaller than the parent.
		//Implemented using an array to hold the data.
		
		element[] theHeap;
		private int size;
		private int capacity;
		
		public MyMaxHeap(){
			//Constructor for the Max Heap. Creates a heap with a capacity of 5.
			//@param monthlyExpenses is the amount of money spent on the card per month.
			capacity = 5;
			theHeap=new element[capacity+1];
			size=0;
		}
		
		public MyMaxHeap(element[] arr){
			//Constructor that automatically loads the heap with a pre-arranged array.
			//@param monthlyExpenses is the amount of money spent on the card per month.
			//@param arr is an array of credit offers that will be loaded into the heap.
			//@param n is the current number of items in the array. AKA size, not capacity.
			theHeap=new element[arr.length+1];
			size = 0;
			capacity = arr.length; //Yeah, this setup is correct. The +1 lets me use 1 as the root instead of 0, making the math work.
			for (int i = 0; i<arr.length; i++){
				add(arr[i]);
			}
		}
		
		public int size(){
			//@return Returns the current size of the containted array. (AKA the heap.)
			return size;
		}
		
		private void swap(int i, int j){
			//Swaps indices i and j in the array arr.
			//@param i is the index of the first item to be swapped.
			//@param j is the index of the second item to be swapped.
			element temp=theHeap[i];
			theHeap[i]=theHeap[j];
			theHeap[j]=temp;
		}
		
		private int compare(int i, int j){
			//@return Returns 1, 0, -1. 1 if greater, 0 if equal, -1 if less.
			//@param i is the index of the first item to be compared.
			//@param j is the index of the second item to be compared.
			if (theHeap[i]==null&&theHeap[j]==null)
				return 0;
			if (theHeap[i]==null)
				return -1;
			if (theHeap[j]==null)
				return 1;
			if (theHeap[i].value()>theHeap[j].value())
				return 1;
			if (theHeap[i].value()==theHeap[j].value())
				return 0;
			else return -1;
		}
		
		public void showList(){
			//Prints the "theHeap" array. Shows the data in the order stored.
			for (int i = 1; i<capacity-1; i++)
				if (theHeap[i]!=null)
					System.out.print((theHeap[i].value())+" ");
				else
					System.out.print("null ");
			System.out.println();
		}
		
		private boolean add(element job){
			//Adds a element to the heap.
			//@param job is the element to be added to the heap.
			
			//System.out.println(size);
			//showList();
			if (size == capacity){
				resize();
			}
			theHeap[size+1]=job;//Okay, so add the item to the next available slot.
			reheap(size+1);//Then reheap from there. It'll only take a few exchanges as the new element climbs up or finds an appropriate slot.
			size++;
			return true;
		}
		
		private void resize(){
			//Added during the attempt to make the queue generic. Allows the arrays to be resized if capacity is reached.
			capacity=theHeap.length*2;
			theHeap=java.util.Arrays.copyOf(theHeap, capacity-1); //Remember, the first element is unused. 
		}
		
		private element removeMax(){
			//Removes the root of the heap, reheaps, and returns that value.
			//@return Returns the root of the heap.
			element temp = theHeap[1];
			int pointer=1;
			theHeap[1]=theHeap[size];//So we swap the root with the last element in the array, and null the item that WAS the root.
			theHeap[size]=null;//Then, the next lines sift the new root back down into it's proper position, as it's likely one of the smaller elements in the list.
			size--;
			while ((pointer*2)+1<=size&&(compare(pointer,(pointer*2))==-1||compare(pointer, (pointer*2)+1)==-1)){//While the pointer has two children...
				if (compare(pointer*2, (pointer*2)+1)>=0){//and the left child is greater than the right...
					if (compare(pointer,pointer*2)==-1){//AND the number at the pointer is LESS than said child, 
						swap(pointer, pointer*2);//Swap them out, and change the location of the pointer.
						pointer=pointer*2;
					}
				}
				else{//and the RIGHT child is greater than the left...
					if (compare(pointer,(pointer*2)+1)==-1){//AND the number at the pointer is LESS than said child, 
						swap(pointer,(pointer*2)+1);//Swap them out, and change the location of the pointer.
						pointer=(pointer*2)+1;
					}
				}
			}//So this'll repeat till either... Actually, this'll never stop. Editing it so the while checks to make sure the "root" isn't in the right place.
			if (pointer*2==size&&compare(pointer*2,pointer)>1){ //Checks if the last item is a left subtree. If it is, use that for the final swap.
				swap(pointer, pointer*2);
			}
			
			return temp;
		}
		
		private boolean reheap(int ele){
			//Takes an existing heap and makes the element indicated by "ele" sift into it's intended location.
			//@param ele is the element to be organized into the heap.
			if (ele == 1)
				return true;
			while(compare(ele, ele/2)==1 && ele!=1){
				swap(ele, ele/2);
				ele=ele/2;
			}
			return true;
		}
		
	}
	
	/*
	 * Heap access routines.
	 * 
	 * Parent of a node is i/2
	 * Left child of a node is 2i
	 * Right child of a node is 2i+1
	 * 
	 * 
	 */
	
	
	
	
	public static abstract class element{
		//Abstract class allowing the PriorityQueue to be used on ANY CLASS that implements this element class.
		public double value(){
			//@return 0 as a default. This is meant to be overriden in any class that extends this.
			return 0;
		}
		}
		

		
}


