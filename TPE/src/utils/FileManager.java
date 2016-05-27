package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import flightassistant.FlightAssistant;

//Por ahora solo se maneja guardando y leyendo FlightAssistant, no se si despues necesitaremos algo mas
public class FileManager {

	public void save(FlightAssistant flightAssistant) throws IOException{
		File f = new File("flightAssistant.obj");
		FileOutputStream fos = new FileOutputStream(f);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(flightAssistant);
		oos.close();
	}
	
	public FlightAssistant load() throws ClassNotFoundException, IOException {
		File f = new File("flightAssistant.obj");
		FileInputStream fis = new FileInputStream(f);
		ObjectInputStream ois = new ObjectInputStream(fis);
		FlightAssistant flightAssistant = (FlightAssistant)ois.readObject();
		ois.close();
		fis.close();
		return flightAssistant;
		
	}
	
}
