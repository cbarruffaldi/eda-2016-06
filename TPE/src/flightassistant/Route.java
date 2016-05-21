package flightassistant;

public class Route {
	
	private String id;	//ID de la ruta para realizar las busquedas, podria ser un int tambien...
	
	//Airport 1 y 2 son nombres y no implican ningun orden.
	private Airport airport1;
	private Airport airport2;
	//Colección de vuelos ordenados salientes por dia y hora
	//Colección de vuelos incidentes
	
	public Route(Airport airport1, Airport airport2) {
		if(airport1 == null || airport2 == null) {
			throw new IllegalArgumentException();
		}
		if(airport1.equals(airport2)) {
			throw new IllegalArgumentException();
		}
		this.airport1 = airport1;
		this.airport2 = airport2;
		this.id = generateId(airport1, airport2);
	}
	
	private String generateId(Airport a1, Airport a2) {
		//TODO:Ver que conviene usar como Id en cuanto a busquedas
		return "";
	}
	
	/*
	 * Determina la igualdad de dos Rutas, segun los dos aeropuertos visitados
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (getClass() != o.getClass())
			return false;
		Route other = (Route) o;

		//Aca se asume que no puede haber una ruta con algun aeropuerto en null,
		//y que no existe una ruta tal que los dos aeropuertos sean el mismo,
		//esto esta contemplado en el constructor
		if(!airport1.equals(other.airport1)) {
			if(!airport1.equals(other.airport2)) {
				return false;
			}
		}
		if (!airport2.equals(other.airport1)) {
			if(!airport2.equals(other.airport2)) {
				return false;
			}
		}
		return true;

	}
	
	
	
	
	
	
}
