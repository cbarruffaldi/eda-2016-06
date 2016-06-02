package testing;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import flightassistant.Airport;
import flightassistant.AirtimeWeighter;
import flightassistant.DijsktraForReal;
import flightassistant.Flight;
import flightassistant.FlightAssistant;
import flightassistant.InfinityDijkstra;
import flightassistant.WeightedFlight;
import flightassistant.Weighter;
import structures.BinaryMinHeap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;


import utils.Day;
import utils.Moment;
import utils.Time;



public class TestEmergencia {

	public static void main(String[] args) {
		PriorityQueue<Airport> pq = new PriorityQueue<Airport>(new Comparator<Airport>() {

			@Override
			public int compare(Airport o1, Airport o2) {
				return Double.compare(o1.weight(), o2.weight());
			}
			
		});
		
		Airport A = new Airport("AAA", 1, 1);
		
		A.setWeight(0);
		
		Airport B = new Airport("BBB", 1, 1);
		Airport C = new Airport("CCC", 1, 1);
		Airport D = new Airport("DDD", 1, 1);
		Airport E = new Airport("EEE", 1, 1);
		Airport F = new Airport("FFF", 1, 1);
		Airport G = new Airport("GGG", 1, 1);
		Airport H = new Airport("HHH", 1, 1);

		
		pq.add(A);
		

		System.out.println(pq.remove()); //AAA
		
		B.setWeight(10);
		C.setWeight(5);
		D.setWeight(4);
		
		pq.add(B);
		pq.add(C);
		pq.add(D);
		
		System.out.println(pq.remove()); // DDD
		
		E.setWeight(13);
		F.setWeight(14);
		
		pq.add(E);
		pq.add(F);
		
		B.setWeight(1);
		
		pq.add(B);
		
		System.out.println(pq.remove()); //Debería ser BBB pero es CCC
		System.out.println(pq.remove());
		System.out.println(pq.remove());
		System.out.println(pq.remove());
		System.out.println(pq.remove());
		
	}
	

}
