package testing;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import utils.RegexHelper;

public class RegexTester {


	@Test
	public void testAirportInsertion() {
		String[] validInput = new String[]{"BUE -34.602535 -58.368731",
		                       "EZE 39.0203 -1",
		                       "XOX -90.000 49.320302",
		                       "OAE 01.120 30.1022",
		                       "XOX 90.000 -180"};
		String[] invalidInput = new String[]{"BU -34.602535 -58.368731",
		                         "BUEN -34.602535 -58.368731",
		                         "XOX -90.001 49.320302",
		                         "AAA -90.000 181.12",
		                         "AAA -100 12.120"};

		for(String s: validInput){
			if(!RegexHelper.validateAirportInsertion(s, " ")){
				fail("Falied at: >" + s);
			}
		}
		
		for(String s: invalidInput){
			if(RegexHelper.validateAirportInsertion(s, " ")){
				fail("Falied at: >" + s);
			}
		}
	}

	@Test
	public void testAirportDeletion() {
		String[] validInput = new String[]{"BUE", "EZE", "OAE", "XOX", "AAA", "ZZZ"};
		String[] invalidInput = new String[]{"BEUE", "", "OE", "XOXOL", "1AB"};

		for(String s: validInput){
			if(!RegexHelper.validateAirportName(s)){
				fail("Falied at: >" + s);
			}
		}
		
		for(String s: invalidInput){
			if(RegexHelper.validateAirportName(s)){
				fail("Falied at: >" + s);
			}
		}
	}


	
	@Test
	public void testFlightInsertion() {
		String[] validInput = new String[]{"AA 1432 Lu-Ju-Ma-Sa BUE PAR 08:46 14h45m 1850.23",
		                       "LAN 11992 Ju EZE MIA 08:46 19h45m 18350.23",
		                       "LAN 11992 Ju EZE MIA 08:46 19h45m 0350.23",
		                       "AA 1432 Lu-Ma-Mi-Ju-Vi-Sa-Do BUE PAR 18:46 14h45m 1850.23",
		                       "AA 0 Lu-Ju-Vi BUE PAR 18:46 14h45m 1850.23",
		                       "Abc 1432 Lu-Lu-Lu BUE PAR 08:46 24h45m 239239",
		                       "AA 1432 Lu-Ju-Ma-Sa BUE PAR 08:46 14h40m 0",
		                       "AA 1432 Lu-Ju-Ma-Sa BUE PAR 08:46 05m 0"};

		String[] invalidInput = new String[]{"AAAA 1432 Lu-Ju-Ma-Sa BUE PAR 08:46 14h45m 1850.23",
                "LAN 11992 Ju EZE MIA 49:46 19h45m 18350.23",
                "LAN 11992 Ju EZE MIA 08:129 19h45m 0350.23",
                "AA 1432 Lu- BUE PAR 18:46 14h45m 1850.23",
                "AA 0 BUE PAR 18:46 14h45m 1850.23",
                "Abc 1432 Lu-Lu-Lu BUE PAR 128:46 14h45m 239239",
                "Abc 1432 Ja-Je BUE 08:46 1124h45m 2390",
				"AA 1432 Lu-Ju-Ma-Sa BUE PAR 08:46 5m 0",	//Los minutos deben ser 05m, no 5m
				"AA 1432 Lu-Ju-Ma-Sa BUE PAR 08:46 14h95m 1850.23"};


		for(String s: validInput){
			if(!RegexHelper.validateFlightInsertion(s, " ")){
				fail("False negative: >\"" + s + "\"");
			}
		}
		
		for(String s: invalidInput){
			if(RegexHelper.validateFlightInsertion(s, " ")){
				fail("False positive: >\"" + s + "\"");
			}
		}
	}
	
	
	@Test
	public void testRoute() {
	//	src=[origen] dst=[destino] priority={ft|pr|tt} *weekdays=[diasSemana]
		
		String[] validInput = new String[]{
				"src=AAA dst=BUE priority=ft weekdays=Lu",		
				"src=AAA dst=BUE priority=pr weekdays=Lu",		
				"src=AAA dst=BUE priority=tt weekdays=Lu",		
				"src=AAA dst=BUE priority=ft weekdays=Lu-Mi-Ma-Ju-Sa",		
				"src=AcB dst=abc priority=pr",		
				"src=AAA dst=BUE priority=ft",		
		};

		String[] invalidInput = new String[]{
				"src=AAAA dst=BUE priority=ft weekdays=Lu",		
				"src=AAA dst=BUUE priority=pr weekdays=Lu",		
				"src=AAA dst=BUE priority=p weekdays=Lu",		
				"src=AAA dst=BUE priority= weekdays=Ju-Sa",		
				"src=AcB dst=abc priority=pt weekdays=",		
				"src=AAA priority=ft"		
		};


		for(String s: validInput){
			if(!RegexHelper.validateRoute(s)){
				fail("False negative: >\"" + s + "\"");
			}
		}
		
		for(String s: invalidInput){
			if(RegexHelper.validateRoute(s)){
				fail("False positive: >\"" + s + "\"");
			}
		}
	}


	
}
