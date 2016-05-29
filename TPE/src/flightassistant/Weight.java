package flightassistant;

public class Weight<T extends Comparable<T>> implements Comparable<Weight<T>>{
	public final static Weight<?> INFINITY = null;
	
	private T value;
	
	public Weight(T value){
		this.value = value;
	}
	
	public int compareTo(Weight<T> other){
		if(other == INFINITY) return -1;
		return value.compareTo(other.value);
	}

	public void setToZero() {
		
	}
	
	
}
