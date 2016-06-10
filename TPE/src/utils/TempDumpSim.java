package utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class TempDumpSim {
	

	static Random rand = new Random(System.currentTimeMillis());
	static String[] alpha = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
									"N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
	
	static String[] hours = new String[]{"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10",
			"11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
	
	static String[] days = new String[]{"Lu", "Ma", "Mi", "Ju", "Vi", "Sa", "Do"};
	
	static String[] airs;
	static Set<String> airset;
	
	private static String generateFlightString(){
		String s = "";
		for(int i = 0; i<2 ; i++){
			s += alpha[rand.nextInt(26)];
		}
		s += "#" + rand.nextInt(1000);
		s += "#" + days[rand.nextInt(days.length)];
		for(int i=0 ; i < days.length ; i++){
			if(rand.nextBoolean()){
				s += "-" + days[i];
			}
		}
		
		String air1, air2;
		do{
			air1 = airs[rand.nextInt(airs.length)];
			air2 = airs[rand.nextInt(airs.length)];
		}while(air1.equals(air2));
		s += "#" + air1 + "#" + air2;
		s += "#" + hours[rand.nextInt(hours.length)] + ":" + Integer.toString(10 + rand.nextInt(50));
		s += "#" + Integer.toString(rand.nextInt(100)) + "h" + Integer.toString(rand.nextInt(60)) + "m";
		s += "#" + Double.toString(rand.nextDouble()*40000);
		
		return s;
	}
	
	private static void genAirports(int cant){
		airset = new HashSet<>();
		airs = new String[cant];
		for(int i = 0; i < cant ; i++){
			String s;
			do{ 
				s =  "" + alpha[rand.nextInt(26)] + alpha[rand.nextInt(26)] + alpha[rand.nextInt(26)];
			}while(airset.contains(s));
			airset.add(s);
			airs[i] = s;
		}
	}

//	public static void main(String[] args) throws FileNotFoundException {
//		PrintStream outFlights = new PrintStream(new FileOutputStream("hugeFlights.txt"));
//		PrintStream outAirs = new PrintStream(new FileOutputStream("hugeAirs.txt"));
//		
//		genAirports(15000);
//		for(int i = 0 ; i < 100000 ; i++){
//			outFlights.println(generateFlightString());
//		}
//		
//		for(Integer i = 0; i < airs.length ; i++){
//			String air = airs[i] + "#1.0#1.0";
//			outAirs.println(air);
//		}
//
//		outFlights.close();
//		outAirs.close();
//	}
}
