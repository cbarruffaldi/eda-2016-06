package structures;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public interface SimpleMap<K, V> {
	/**
	 * Agrega un par clave/valor a la tabla. Si la clave ya existe,
	 * actualiza el valor.
	 */
	public void put(K key, V value);

	/**
	 * Obtiene el valor asociado a una clave. Si no existe la clave retorna null.
	 */
	public V get(K key);

	/**
	 * Devuelve verdadero si el mapa contiene la clave especificada.
	 */
	public boolean containsKey(K key);

	/**
	 * Retorna true si contiene una o más claves asociadas al valor.
	 */
	public boolean containsValue(V value);

	/**
	 * Retorna todas las claves insertadas en el mapa.
	 */
	public Set<K> keySet();

	/**
	 * Retorna todos los valores insertados en el mapa.
	 */
	public Collection<V> values();

	/**
	 * Elimina un par clave/valor de la tabla. Si la clave no existe no hace nada.
	 */
	public void remove(K key);

	/**
	 * Obtiene la cantidad de pares clave/valor insertados en el mapa.
	 */
	public int size();

	/**
	 * Retorna true si no existen pares clave/valor.
	 */
	public boolean isEmpty();

	/**
	 * Vacía el mapa.
	 */
	public void clear();

	/**
	 * Retorna un Iterador sobre las keys del mapa.
	 */
	public Iterator<K> keyIterator();

	/**
	 * Retorna un Iterador sobre los valores del mapa.
	 */
	public Iterator<V> valueIterator();
}
