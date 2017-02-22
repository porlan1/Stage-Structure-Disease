package StageStructuredCompetition;
import java.util.*;

public class Daphnia {
	static int masterTotal = 0;
	static ArrayList<Daphnia> masterList = new ArrayList<Daphnia>();
	
	void grow();
	void reproduce();
	void age();
	void die();
	String getDescription();
	double consume();
}

public class SpeciesOne extends Daphnia {
	//State variables of individuals:
	double birthBuffer = 0.0;
	double size = 5e-6;
	double currentAge = 0.0;
	double timeToJuvenileDeath = 0.0;
	double timeToAdultDeath = 0.0;
	boolean adult = false;
	boolean alive = true;
	
	//static variables of population
	static ArrayList<SpeciesOne> speciesOneArray = new ArrayList<SpeciesOne>();
	static int Total = 0;
	static double JuvenileBiomass  = 0.0;
	static double AdultBiomass = 0.0;
	final static String description = "SpeciesOne";
	
	
	//Model parameters for this species:
	static double sm = 1e-4;
	static double sb = 5e-6;
	static double T = 0.1;
	static double H = 3.0;
	static double IMax = 1.0;
	static double q = 1.0;
	static double sigma_j = 0.5;
	static double sigma_a = 0.5;
	static double mu_j = 0.015;
	static double mu_a = 0.015;
	
	SpeciesOne() {
	//constructor function, sets some random values for an individuals lifetime.
	//I guess here just add stochastic mortality in.
		super();
		Random rand = new Random();
		//draw from exponential distributions to get random death times
		this.timeToJuvenileDeath = Math.log(1 - rand.nextDouble())/(-this.mu_j);
		this.timeToAdultDeath = Math.log(1 - rand.nextDouble())/(-this.mu_a);
	}
	
	@Override
	void grow(double Resources, double dt) {
		double FR = Resources/(Resources + SpeciesOne.H);
		//if feeding rate allows growth:
		if (SpeciesOne.sigma_a*SpeciesOne.IMAX*SpeciesOne.q*FR > SpeciesOne.T && this.adult) {
			this.birthBuffer = this.birthBuffer*(SpeciesOne.sigma_a*SpeciesOne.IMAX*SpeciesOne.q*FR - SpeciesOne.T)*dt;
		} else if (SpeciesOne.sigma_j*speciesOne.IMAX*FR > SpeciesOne.T && !this.adult) {
			this.size = this.size*(SpeciesOne.sigma_j*SpeciesOne.IMAX*FR - SpeciesOne.T)*dt;
		}
	};
	
	@Override
	void reproduce() {
		if (this.adult) {
			double numberOfBabies = this.birthBuffer/SpeciesOne.sb;
			double numberRounded = Math.floor(numberOfBabies);
			if (numberRounded > 0) {
				//subtract from birth buffer
				this.birthBuffer -= numberRounded*SpeciesOne.sb;
				//make the new babies:
				for (int i = 0; i < numberRounded; i++) {
					//add the babies to the ArrayList
					SpeciesOne.Total++;
					super.masterTotal++;
					super.masterList.add(new SpeciesOne());
				}
			}
		}
	};
	
	@Override
	void age() {
		this.currentAge += dt;
	};
	
	@Override
	void die() {
		if (this.adult) {
			if (this.currentAge > this.timeToAdultDeath) {
				this.alive = false;
			}
		} else {
			if (this.currentAge > this.timeToJuvenileDeath) {
				this.alive = false;
			}
		}
	};
	
	@Override
	String getDescription() {
		return SpeciesOne.description;
	}
	
	@Override
	double consume(double Resources, double dt) {
		double FR = Resources/(Resources + SpeciesOne.H);
		if (this.adult) {
			return SpeciesOne.q*SpeciesOne.IMax*FR*this.size*dt;
		} else {
			return SpeciesOne.IMax*FR*this.size*dt;
		}
	};
	
	
}