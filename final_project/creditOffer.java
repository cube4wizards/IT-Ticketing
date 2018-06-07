package final_project;

import final_project.MyPriorityQueueI.element;

public class creditOffer extends element{
	//Class containing the data used to compute credit offer scores.
	
	/*IF YOU ARE LOOKING FOR THE DRIVER FOR THE WHOLE FINAL PROJECT, IT'S IN THE ITArchive CLASS.*/
	/*If you are looking to use this to test the PriorityQueue, the driver for that is in the MyPriorityQueueI class.*/
	
	double membershipFee = 0; //Annual membership cost.
	double CBR=0; //Cash Back Rate.
	double APR=0; //Annual percentage rate.
	static double expenses;
	
	public creditOffer(double fee,double cbr,double apr){
		//Constructor for credit card offer nodes. Takes the membership fee, CBR, and APR rates.
		//@param fee is the annual membership fee to be stored;
		//@param cbr is the cash back rate to be stored;
		//@param apr is the annual percentage rate to be stored.
		membershipFee = fee;
		CBR = cbr;
		APR = apr;
	}
	@Override
	public double value(){
		//@return returns the value of the credit offer.
		return (CBR*expenses)-(APR*(expenses*1.2))-membershipFee;
	}
}