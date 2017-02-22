package StageStructuredCompetition;
import java.util.*;

//compile and run with
/*

cd ~/Documents/StageStructuredCompetition
javac StageStructuredCompetition.java SpeciesOne.java SpeciesTwo.java Daphnia.java
cd ~/Documents
java StageStructuredCompetition.StageStructuredCompetition

*/

class StageStructuredCompetition {
	//Resource parameters:
	public static double[] Sp1Parameters = new double[7];
	public static double[] Sp2Parameters = new double[7];
	public static double K = 4.0;
	public static double r = 1.0;
	public static double Resources = K;
	public static double lakeVolume = 1.0;
	public static double dt = 0.1;
	public static double noiseLevel = 0.0;
	public static double period = 200.0;
	public static double amplitude = 0.0;
	public static int logistic = 1;
	
	public static double speciesOneJuvenileBiomass;
	public static double speciesOneAdultBiomass;
	public static double speciesTwoJuvenileBiomass;
	public static double speciesTwoAdultBiomass;
	public static int speciesOneAdultNumber = 0;
	public static int speciesOneJuvenileNumber = 0;
	public static int speciesTwoAdultNumber = 0;
	public static int speciesTwoJuvenileNumber = 0;
	
	
	static double eatResources(ArrayList<Daphnia> masterList) {
		double totalConsumed = 0.0;
		for (int i = 0; i < masterList.size(); i++) {
			totalConsumed += masterList.get(i).consume(Resources, dt);
		}
		return totalConsumed;
	}
	
	static void cleanDaphniaArray(ArrayList<Daphnia> masterList) {
		int index = 0;
		for (int i = 0; i < masterList.size(); i++) {
			if (!masterList.get(index).alive) {
			masterList.remove(index);
			index--;
			}
			index++;
		}
	}
	
	static void calculateBiomass(ArrayList<Daphnia> masterList) {
		speciesOneAdultBiomass = 0.0;
		speciesOneJuvenileBiomass = 0.0;
		speciesTwoAdultBiomass = 0.0;
		speciesTwoJuvenileBiomass = 0.0;
		speciesOneAdultNumber = 0;
		speciesOneJuvenileNumber = 0;
		speciesTwoAdultNumber = 0;
		speciesTwoJuvenileNumber = 0;
		for (int i = 0; i < masterList.size(); i++) {
			if (masterList.get(i).getDescription() == "SpeciesOne") {
				if (masterList.get(i).adult) {
					speciesOneAdultBiomass += masterList.get(i).size;
					speciesOneAdultNumber++;
				} else {
					speciesOneJuvenileBiomass += masterList.get(i).size;
					speciesOneJuvenileNumber++;
				}
			} else if (masterList.get(i).getDescription() == "SpeciesTwo") {
				if (masterList.get(i).adult) {
					speciesTwoAdultBiomass += masterList.get(i).size;
					speciesTwoAdultNumber++;
				} else {
					speciesTwoJuvenileBiomass += masterList.get(i).size;
					speciesTwoJuvenileNumber++;
				}
			}
		}
	}
	
	/*
	static void printOutput() {
		
	}
	*/
	
	static void runSimulation(double tf, boolean output) {
		double t = 0.0;
		if (output) {
			calculateBiomass(Daphnia.masterList);
			System.out.println(t + " " + Resources + " " + speciesOneJuvenileBiomass + " " + speciesOneAdultBiomass + " " + speciesTwoJuvenileBiomass + " " + speciesTwoAdultBiomass);
		} 
		while (t < tf && Daphnia.masterList.size() > 0) {
			t += dt;
			//loop through all daphnia
			for (int i = 0; i < Daphnia.masterList.size(); i++) {
				Daphnia.masterList.get(i).age(dt);
				Daphnia.masterList.get(i).grow(Resources, dt);
				Daphnia.masterList.get(i).reproduce();
				Daphnia.masterList.get(i).die(Resources, dt);
			}
			//resource renewal and depletion.
			if (logistic == 1) {
			Resources += Resources*r*(1.0 - Resources/K)*dt - eatResources(Daphnia.masterList)/lakeVolume;
			} else {
			Resources += r*(K - Resources)*dt - eatResources(Daphnia.masterList)/lakeVolume;
			}
			//delete all of the dead daphnia from the array:
			cleanDaphniaArray(Daphnia.masterList);
			if (output) {
			calculateBiomass(Daphnia.masterList);
			System.out.println(t + " " + Resources + " " + speciesOneJuvenileBiomass + " " + speciesOneAdultBiomass + " " + speciesTwoJuvenileBiomass + " " + speciesTwoAdultBiomass);
			} 
		}
	}
	
	static void changeParameters(double[] parameters, int species) {
		if (species == 1) {
			for (int i = 0; i < Daphnia.masterList.size(); i++) {
				if (Daphnia.masterList.get(i).getDescription() == "SpeciesOne") {
					Daphnia.masterList.get(i).T = parameters[0];
					Daphnia.masterList.get(i).H = parameters[1];
					Daphnia.masterList.get(i).IMax = parameters[2];
					Daphnia.masterList.get(i).sigma_j = parameters[3];
					Daphnia.masterList.get(i).sigma_a = parameters[4];
					Daphnia.masterList.get(i).mu_j = parameters[5];
					Daphnia.masterList.get(i).mu_a = parameters[6];
				}
			}
		} else if (species == 2) {
			for (int i = 0; i < Daphnia.masterList.size(); i++) {
				if (Daphnia.masterList.get(i).getDescription() == "SpeciesTwo") {
					Daphnia.masterList.get(i).T = parameters[0];
					Daphnia.masterList.get(i).H = parameters[1];
					Daphnia.masterList.get(i).IMax = parameters[2];
					Daphnia.masterList.get(i).sigma_j = parameters[3];
					Daphnia.masterList.get(i).sigma_a = parameters[4];
					Daphnia.masterList.get(i).mu_j = parameters[5];
					Daphnia.masterList.get(i).mu_a = parameters[6];
				}
			}
		}
	}
	
	static void linspace(double min, double max, int points, double[] c) {
		double h = (max-min)/(points-1);
		for (int i = 0; i < points; i++) {
			c[i] = min + h*i;
		}
	}
	
	static void runBifucration(int parmNumber, double min, double max, int count) {
			int initialDaphnia = 100;
			double tf = 1000.0;
			int speciesOne, speciesTwo;
			
			double[] bVec = new double[count];
			linspace(min, max, count, bVec);
			
			Sp1Parameters[0] = 0.1;
			Sp1Parameters[1] = 3.0;
			Sp1Parameters[2] = 1.0;
			Sp1Parameters[3] = 0.5;
			Sp1Parameters[4] = 0.5;
			Sp1Parameters[5] = 0.15;
			Sp1Parameters[6] = 0.15;
			
			Sp2Parameters[0] = 0.1;
			Sp2Parameters[1] = 3.0;
			Sp2Parameters[2] = 1.0;
			Sp2Parameters[3] = 0.5;
			Sp2Parameters[4] = 0.5;
			Sp2Parameters[5] = 0.15;
			Sp2Parameters[6] = 0.15;
			//set initial conditions.
			for (int j = 0; j < initialDaphnia; j++) {
				Daphnia.masterList.add(new SpeciesOne(Sp1Parameters));
				Daphnia.masterList.add(new SpeciesTwo(Sp2Parameters));
			}
			runSimulation(tf, false);
			
		for (int i = 0; i < count; i++) {
			//need a new function to change the parameter of existing guys
			Sp1Parameters[parmNumber] = bVec[i];
			changeParameters(Sp1Parameters, 1);
			
			//run the model to desired time
			runSimulation(tf, false);
			
			//collect stats
			calculateBiomass(Daphnia.masterList);
			speciesOne = speciesOneJuvenileNumber+speciesOneAdultNumber;
			speciesTwo = speciesTwoJuvenileNumber+speciesTwoAdultNumber;
			System.out.println(speciesOne + " " + speciesTwo);
		}
	}
	
	
	static void runRandomParameters(int count) {
		Random rand = new Random();
		int initialDaphnia = 10;
		int speciesOne = 0;
		int speciesTwo = 0;
		for (int i = 0; i < count; i++) {
		K = 3.5 + 3.0*(rand.nextDouble()-0.5);
		//inside a for loop:
			//first generaate random parameters for species 1
			//parameter order: T, H, Imax, sigma_j, sigma_a, mu_j, mu_a
			Sp1Parameters[0] = 0.1;
			Sp1Parameters[1] = 3.0+ 1.0*(rand.nextDouble()-0.5);
			Sp1Parameters[2] = 1.0 + 0.2*(rand.nextDouble()-0.5);
			Sp1Parameters[3] = 0.5 + 0.3*(rand.nextDouble()-0.5);
			Sp1Parameters[4] = 0.5 + 0.3*(rand.nextDouble()-0.5);
			Sp1Parameters[5] = 0.15 + 0.1*(rand.nextDouble()-0.5);
			Sp1Parameters[6] = 0.15 + 0.1*(rand.nextDouble()-0.5);
			for (int j = 0; j < initialDaphnia; j++) {
				Daphnia.masterList.add(new SpeciesTwo(Sp1Parameters));
			}
			//run species 1 alone
			runSimulation(1000.0, false);
			//then generate random parameters for species 2
			Sp2Parameters[0] = 0.1;
			Sp2Parameters[1] = 3.0+ 1.0*(rand.nextDouble()-0.5);
			Sp2Parameters[2] = 1.0 + 0.2*(rand.nextDouble()-0.5);
			Sp2Parameters[3] = 0.5 + 0.3*(rand.nextDouble()-0.5);
			Sp2Parameters[4] = 0.5 + 0.3*(rand.nextDouble()-0.5);
			Sp2Parameters[5] = 0.15 + 0.1*(rand.nextDouble()-0.5);
			Sp2Parameters[6] = 0.15 + 0.1*(rand.nextDouble()-0.5);
			for (int j = 0; j < initialDaphnia; j++) {
				Daphnia.masterList.add(new SpeciesOne(Sp2Parameters));
			}
			//then run them together
			runSimulation(2000.0, false);
			//collect data on coexistence
			calculateBiomass(Daphnia.masterList);
			speciesOne = speciesOneJuvenileNumber+speciesOneAdultNumber;
			speciesTwo = speciesTwoJuvenileNumber+speciesTwoAdultNumber;
			System.out.println(speciesOne + " " + speciesTwo);
			if ((speciesOneJuvenileNumber+speciesOneAdultNumber) > 0 && (speciesTwoJuvenileNumber+speciesTwoAdultNumber) > 0) {
				System.out.println("YES");
			} else {
				System.out.println("no");
			}
			//then clear all daphnia out of the master array
			Daphnia.masterList.clear();
		}
	}
	
	
	
	

	public static void main(String args[]) {
		//here is where we will run the model.
		//set up the initial conditions.
		/*
		Resources = K;
		double tf = 1000.0;
		double t = 0.0;
		int initialDaphnia = 100;
		
		for (int i = 0; i < initialDaphnia; i++) {
			Daphnia.masterList.add(new SpeciesOne());
			//Daphnia.masterList.add(new SpeciesTwo());
		}
		
		boolean output = true;
		
		runSimulation(tf, output);
		*/
		//runRandomParameters(100);
		runBifucration(2, 1.0, 1.2, 4);
	};

}