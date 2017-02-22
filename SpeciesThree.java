package StageStructuredCompetition;
import java.util.*;
import java.lang.*;

public class SpeciesThree extends Daphnia {
	//State variables of individuals:
	double birthBuffer = 0.0;
	double currentAge = 0.0;
	//boolean adult = false;
	//boolean alive = true;
	
	//static variables of population
	static int Total = 0;
	static double JuvenileBiomass  = 0.0;
	static double AdultBiomass = 0.0;
	final static String description = "SpeciesThree";
	
	
	//Model parameters for this species:
	static double sm = 1e-4;
	static double sb = 5e-6;
	double T = 0.1;
	double H = 3.0;
	double IMax = 0.003;
	double q = 1.0;
	double sigma_j = 0.33;
	double sigma_a = 0.33;
	double mu_j = 0.11;
	double mu_a = 0.11;
	static double noise = 0.0;
	
	SpeciesThree() {
	//constructor function, sets some random values for an individuals lifetime.
	//I guess here just add stochastic mortality in.
		super();
		Random rand = new Random();
		//draw from normal distributions for random parameter values
		this.T = rand.nextGaussian()*this.T*SpeciesThree.noise + this.T;
		this.H = rand.nextGaussian()*this.H*SpeciesThree.noise + this.H;
		this.IMax = rand.nextGaussian()*this.IMax*SpeciesThree.noise + this.IMax;
		this.mu_j = rand.nextGaussian()*this.mu_j*SpeciesThree.noise + this.mu_j;
		this.mu_a = rand.nextGaussian()*this.mu_a*SpeciesThree.noise + this.mu_a;
		this.sigma_j = rand.nextGaussian()*this.sigma_j*SpeciesThree.noise + this.sigma_j;
		this.sigma_a = rand.nextGaussian()*this.sigma_a*SpeciesThree.noise + this.sigma_a;
	};
	
	@Override
	void grow(double Resources, double dt) {
		double FR = Resources;
		//if feeding rate allows growth:
		if (super.size >= SpeciesThree.sm) {
			super.adult = true;
		}
		if ((this.sigma_a*this.IMax*this.q*FR) > this.T && super.adult) {
			this.birthBuffer += SpeciesThree.sm*(this.sigma_a*this.IMax*this.q*FR - this.T)*dt;
		} else if ((this.sigma_j*this.IMax*FR > this.T) && !super.adult) {
			super.size += super.size*(this.sigma_j*this.IMax*FR - this.T)*dt;
		}
	};
	
	@Override
	void reproduce() {
		if (super.adult) {
			double numberOfBabies = this.birthBuffer/SpeciesThree.sb;
			double numberRounded = Math.floor(numberOfBabies);
			if (numberRounded > 0.0) {
				//subtract from birth buffer
				this.birthBuffer -= numberRounded*SpeciesThree.sb;
				//make the new babies:
				for (int i = 0; i < numberRounded; i++) {
					//add the babies to the ArrayList
					SpeciesThree.Total++;
					Daphnia.masterTotal++;
					Daphnia.masterList.add(new SpeciesThree());
				}
			}
		}
	};
	
	@Override
	void age(double dt) {
		this.currentAge += dt;
	};
	
	@Override
	void die(double Resources, double dt) {
		double FR = Resources;
		double mortality = 0.0;
		//adults:
		if (super.adult) {
			if ((this.sigma_a*this.IMax*this.q*FR) > this.T) {
			mortality = this.mu_a;
			} 
			else {
			mortality = this.mu_a - (this.sigma_a*this.IMax*this.q*FR - this.T);
			} 
			//juveniles
		} else {
			if ((this.sigma_j*this.IMax*FR) > this.T) {
			mortality = this.mu_j;
			} else {
			mortality = this.mu_j - (this.sigma_j*this.IMax*FR - this.T);
			} 
		}
		Random rand = new Random();
		double randomNumber = rand.nextDouble();
		double deathProbability = 1.0-Math.exp(-mortality*dt);
		if (randomNumber < deathProbability) {
			super.alive = false;
		}
	};
	
	@Override
	String getDescription() {
		return SpeciesThree.description;
	};
	
	@Override
	double consume(double Resources, double dt) {
		double FR = Resources;
		if (super.adult) {
			return this.q*this.IMax*FR*super.size*dt;
		} else {
			return this.IMax*FR*super.size*dt;
		}
	};
	
	
}