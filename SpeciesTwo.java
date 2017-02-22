package StageStructuredCompetition;
import java.util.*;
import java.lang.*;

public class SpeciesTwo extends Daphnia {
	//State variables of individuals:
	double birthBuffer = 0.0;
	double currentAge = 0.0;
	//boolean adult = false;
	//boolean alive = true;
	
	//static variables of population
	static ArrayList<SpeciesOne> speciesTwoArray = new ArrayList<SpeciesOne>();
	static int Total = 0;
	static double JuvenileBiomass  = 0.0;
	static double AdultBiomass = 0.0;
	final static String description = "SpeciesTwo";
	
	
	//Model parameters for this species:
	static double noise = 0.0;
	
	SpeciesTwo() {
	//constructor function, sets some random values for an individuals lifetime.
	//I guess here just add stochastic mortality in.
		super();
		Random rand = new Random();
		//draw from normal distributions for random parameter values
		this.T = rand.nextGaussian()*this.T*SpeciesTwo.noise + this.T;
		this.H = rand.nextGaussian()*this.H*SpeciesTwo.noise + this.H;
		this.IMax = rand.nextGaussian()*this.IMax*SpeciesTwo.noise + this.IMax;
		this.mu_j = rand.nextGaussian()*this.mu_j*SpeciesTwo.noise + this.mu_j;
		this.mu_a = rand.nextGaussian()*this.mu_a*SpeciesTwo.noise + this.mu_a;
		this.sigma_j = rand.nextGaussian()*this.sigma_j*SpeciesTwo.noise + this.sigma_j;
		this.sigma_a = rand.nextGaussian()*this.sigma_a*SpeciesTwo.noise + this.sigma_a;
	};
	
	//random constructor
	SpeciesTwo(double[] Sp2Parameters) {
		//parameter order: T, H, Imax, q, sigma_j, sigma_a, mu_j, mu_a
		super();
		this.T = Sp2Parameters[0];
		this.H = Sp2Parameters[1];
		this.IMax = Sp2Parameters[2];
		this.sigma_j = Sp2Parameters[3];
		this.sigma_a = Sp2Parameters[4];
		this.mu_j = Sp2Parameters[5];
		this.mu_a = Sp2Parameters[6];
	};
	
	@Override
	void grow(double Resources, double dt) {
		double FR = Resources/(Resources + this.H);
		//if feeding rate allows growth:
		if (super.size >= SpeciesTwo.sm) {
			super.adult = true;
		}
		if ((this.sigma_a*this.IMax*this.q*FR) > this.T && super.adult) {
			this.birthBuffer += SpeciesTwo.sm*(this.sigma_a*this.IMax*this.q*FR - this.T)*dt;
		} else if ((this.sigma_j*this.IMax*FR > this.T) && !super.adult) {
			super.size += super.size*(this.sigma_j*this.IMax*FR - this.T)*dt;
		}
	};
	
	@Override
	void reproduce() {
		if (super.adult) {
			double numberOfBabies = this.birthBuffer/SpeciesTwo.sb;
			double numberRounded = Math.floor(numberOfBabies);
			if (numberRounded > 0.0) {
				double[] Sp2Parameters = new double[7];
				Sp2Parameters[0] = this.T;
				Sp2Parameters[1] = this.H;
				Sp2Parameters[2] = this.IMax;
				Sp2Parameters[3] = this.sigma_j;
				Sp2Parameters[4] = this.sigma_a;
				Sp2Parameters[5] = this.mu_j;
				Sp2Parameters[6] = this.mu_a;
				//subtract from birth buffer
				this.birthBuffer -= numberRounded*SpeciesTwo.sb;
				//make the new babies:
				for (int i = 0; i < numberRounded; i++) {
					//add the babies to the ArrayList
					SpeciesTwo.Total++;
					Daphnia.masterTotal++;
					Daphnia.masterList.add(new SpeciesTwo(Sp2Parameters));
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
		double FR = Resources/(Resources + this.H);
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
		return SpeciesTwo.description;
	};
	
	@Override
	double consume(double Resources, double dt) {
		double FR = Resources/(Resources + this.H);
		if (super.adult) {
			return this.q*this.IMax*FR*super.size*dt;
		} else {
			return this.IMax*FR*super.size*dt;
		}
	};
	
	
}