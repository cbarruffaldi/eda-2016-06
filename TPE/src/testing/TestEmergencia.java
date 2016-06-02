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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


import utils.Day;
import utils.Moment;
import utils.Time;



public class TestEmergencia {

		static FlightAssistant fa;
		static BinaryMinHeap<Airport> pq;

		
		static String[] airports;
		static Random random;

		@BeforeClass
		public static void setUpBeforeClass() throws Exception {
			fa = new FlightAssistant();

			airports = new String[]{"AAA", "BBB", "CCC", "DDD", "EEE", "FFF", "GGG", "HHH", "III", 
											"JJJ", "KKK", "LLL", "MMM", "NNN", "OOO", "PPP", "QQQ", "RRR",
											"SSS", "TTT", "UUU", "VVV", "WWW", "XXX", "YYY", "ABC"};
					
			int i = 0;
			
			for(String s: airports){
				fa.insertAirport(s, i, i);
			}
					
			random = new Random(System.currentTimeMillis());
			
			i = 0;
			while(i < 10){
				i++;
				
				String air1 = airports[random.nextInt(airports.length)];
				
				String air2 = null;
				
				do{
					air2 = airports[random.nextInt(airports.length)];
				} while(air2.equals(air1));
				
				
				double price = random.nextInt(15500) + 1000; //entre 1000 y 15500
				Time duration = new Time(random.nextInt(1000));
				int hour = random.nextInt(24);
				int minutes = random.nextInt(60);
				
				Moment[] departures = new Moment[]{ new Moment(Day.LU, new Time(hour, minutes)) };
				
				fa.insertFlight("UAI", i, price, departures, duration, air1, air2);
			}
			
			pq = queueAirports(fa);
			
			
		}


		@Test
		public void whatever() {
			int i = 0;
			while(i++<10000){
			
			Airport air1 = fa.airports.get(airports[random.nextInt(airports.length)]);
			Airport air2 = fa.airports.get(airports[random.nextInt(airports.length)]);
			
			
			fa.refreshAirportsNodeProperties();

			List<Flight> l1 = DijsktraForReal.minPath(air1, air2, AirtimeWeighter.WEIGHTER);
			
			fa.refreshAirportsNodeProperties();

			List<Flight> l2 = InfinityDijkstra.minPath(fa, air1, air2, AirtimeWeighter.WEIGHTER);
			}
		}
		
		
			
		public static List<Flight> minPath(FlightAssistant fa, Airport origin, Airport dest, Weighter weighter){
			BinaryMinHeap<Airport> pq = queueAirports(fa);
			
			
			pq.decreasePriority(origin, 0);
			origin.visit();
			
			while(!pq.isEmpty()){
				Double minWeight = pq.minWeight();
				
				if(Double.compare(minWeight, Double.POSITIVE_INFINITY) == 0)
					return null; //No existe el camino, no tiene sentido seguir
				
				Airport airport = pq.dequeue();
				airport.visit();
				
				if(airport.equals(dest)){
					return buildList(dest);
				}
				
				Iterator<Airport> iter = airport.connectedAirportsIterator();
					while(iter.hasNext()){
						Airport next = iter.next();
						if(!next.visited() && airport.flightExistsTo(next)){
							WeightedFlight wflight = weighter.minFlight(airport, next);
							
							double nextCurrWeight = pq.getPriority(next);
							double acumWeight = minWeight + wflight.weight();
							if(acumWeight < nextCurrWeight){
								pq.decreasePriority(next, acumWeight);
								next.setIncident(wflight.flight());
							} //end if 									
						} // end if
					} // end while
				} //end if			

			return null;
		}
		

		private static List<Flight> buildList(Airport last) {
			LinkedList<Flight> list = new LinkedList<>();

			Airport curr = last;
			Flight f;
			while((f = curr.getIncident()) != null){
				list.addFirst(f);
				curr = f.getOrigin();
			}
			return list;
		}

		
		private static BinaryMinHeap<Airport> queueAirports(FlightAssistant fa) {
			int size = fa.airports.size();
		
			BinaryMinHeap<Airport> heap = new BinaryMinHeap<Airport>(size);
			
			Iterator<Airport> iter = fa.airports.valueIterator();
		
			while(iter.hasNext()){
				heap.enqueue(iter.next(), Double.POSITIVE_INFINITY);		
			}
			
			return heap;
		}
}
