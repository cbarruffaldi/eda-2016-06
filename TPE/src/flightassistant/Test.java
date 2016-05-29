package flightassistant;


import java.util.List;

import utils.Day;
import utils.Moment;
import utils.Time;

public class Test {
	public static void main(String[] args) {
		
		System.err.println(B.getDestinations().contains(A));
		
	//	System.out.println(A.getQuickestTo(B));
//		System.out.println(A.getCheapestTo(B));
//
//		System.out.println(A.routeExistsTo(B));
//		System.out.println(A.routeExistsTo(C));
//		System.out.println(A.routeExistsTo(D));
//		System.out.println(E.routeExistsTo(F));
		
		List<Flight> list = DijsktraForReal.minPath(A,F,new AirtimeWeighter());

		if(list == null)
			System.out.println("null");
		else{
		for(Flight f: list){
			System.out.println(f);
		}
		}
	
	}
}
