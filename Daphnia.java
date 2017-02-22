package StageStructuredCompetition;
import java.util.*;

public abstract class Daphnia {
	static int masterTotal = 0;
	public static ArrayList<Daphnia> masterList = new ArrayList<Daphnia>();
	boolean adult = false;
	boolean alive = true;
	double size = 1e-4;
	
	static double sm = 1e-3;
	static double sb = 1e-4;
	double T = 0.1;
	double H = 3.0;
	double IMax = 0.5;
	double q = 1.0;
	double sigma_j = 0.5;
	double sigma_a = 0.5;
	double mu_j = 0.015;
	double mu_a = 0.015;
	
	abstract void grow(double Resources, double dt);
	abstract void reproduce();
	abstract void age(double dt);
	abstract void die(double Resources, double dt);
	abstract String getDescription();
	abstract double consume(double Resources, double dt);
}