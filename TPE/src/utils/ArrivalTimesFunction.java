package utils;

import flightassistant.Airport;
import structures.AVLMap;
import structures.AVLSet;

import java.util.Iterator;

public class ArrivalTimesFunction {
    private Airport airport;
    private AVLMap<Double, Double> function;
    //private AVLMap<Integer, Integer> inverse;
    private AVLSet<Double> domain;

    private double refined;

    private double leftmost;
    private double rightmost;


    public ArrivalTimesFunction (Airport airport, AVLSet<Double> times) {
        if (times.size() < 1) {
            throw new IllegalArgumentException("Empty domain");
        }

        this.domain = times;

        this.airport = airport;
        function = new AVLMap<>();
        
        leftmost = Double.POSITIVE_INFINITY;
        
        for (Double t : times) {
            leftmost = t < leftmost ? t : leftmost;
            rightmost = t > rightmost ? t : rightmost;
            function.put(t, Double.POSITIVE_INFINITY);
        }

        refined = leftmost;
    }

    public double refinedUpTo () {
        return refined;
    }

    public void minimize (ArrivalTimesFunction refiner) {
        Iterator<Double> domain = function.keyIterator();
        double t;
        while (domain.hasNext()) {
            t = domain.next();
            function.put(t, Double.min(function.get(t), refiner.function.get(t)));
        }
    }

    public double eval (double t) {
        if (!function.containsKey(t)) {
            throw new IllegalArgumentException();
        }

        return function.get(t);
    }

    public void updateValue (Double t, Double newValue) {
        if (!function.containsKey(t))
            throw new IllegalArgumentException();
        function.put(t, newValue);
    }

    /**
     * Actualiza el valor al nuevo solo si es mas chico
     */
    public void minimizeValue (Double t, Double newValue) {
        if (!function.containsKey(t))
            throw new IllegalArgumentException();

        if (Double.compare(newValue, function.get(t)) < 1) {
            System.out.println(
                "Minimizing..." + airport + ":" + t + ":" + function.get(t) + " -> " + newValue);
            function.put(t, newValue);
        }

    }

    public void updateValue (Integer t, Integer newValue) {
        updateValue((double) t, (double) newValue);
    }


    public AVLSet<Double> getDomain () {
        return domain;
    }

    public void print () {
        System.out.println("Function for " + airport);
        Iterator<Double> domain = function.keyIterator();
        double t;
        while (domain.hasNext()) {
            t = domain.next();
            System.out.println("function(" + t + ") = " + function.get(t));
        }
    }

    public Double getMin () {
        Iterator<Double> domain = function.keyIterator();
        double t;
        Double min = Double.POSITIVE_INFINITY;
        Double minT = null;
        Double curr;
        while (domain.hasNext()) {
            t = domain.next();
            curr = function.get(t) - t;
            if (curr < min) {
                min = curr;
                minT = t;
            }
        }

        return minT;

    }


    public double getRightVal () {
        return rightmost;
    }

    public double getLeftVal () {
        return leftmost;
    }

    public boolean equals (Object o) {
        if (this == o)
            return true;
        if (o == null || !(o instanceof ArrivalTimesFunction))
            return false;

        return airport.equals(((ArrivalTimesFunction) o).airport);
    }


    //
    //	public static void main(String[] args) {
    //		AVLSet<Double> domain = new AVLSet<Double>();
    //		domain.add(300.0);
    //		domain.add(420.0);
    //		domain.add(100.0);
    //		domain.add(200.0);
    //
    //		ArrivalTimesFunction f = new ArrivalTimesFunction(null, domain);
    //		ArrivalTimesFunction g = new ArrivalTimesFunction(null, domain);
    //
    //		f.updateValue(100, 40);
    //		f.updateValue(200, 129);
    //		f.updateValue(300, 100);
    //
    //		g.updateValue(100, 50);
    //		g.updateValue(200, 100);
    //		g.updateValue(300, 44);
    //		g.updateValue(420, 1);
    //
    //		f.print();
    //		System.out.println("=============");
    //		f.minimize(g);
    //		f.print();
    //
    //
    //	}


    public Airport airport () {
        return airport;
    }

    public double getMaxBounded (double bound) {
        Iterator<Double> iter = domain.iterator();

        double prev = leftmost;
        double curr = iter.next();


        while (iter.hasNext() && eval(curr) < bound) {
            prev = curr;
            curr = iter.next();
            System.err.println(eval(curr));
        }

        System.err.println("Max bounded " + prev + " by bound: " + bound);
        return prev;
    }

    public void setRefined (double newRefined) {
        refined = newRefined;
    }
}
