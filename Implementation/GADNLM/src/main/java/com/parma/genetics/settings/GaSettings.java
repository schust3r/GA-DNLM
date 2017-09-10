package com.parma.genetics.settings;

import java.util.List;
import org.opencv.core.Mat;

/**
 * 
 * @author Joel S.
 *
 */

public class GaSettings {
	
	/** Initial parameter ranges **/
	
	// Lives in [0.5, 5]
	private float lowerLambda;	
	
	private float upperLambda;
	
	// Lives in [5, 30]
	private int lowerSigmaS;
	
	private int upperSigmaS;
	
	// Lives in [3, 21]
	private int lowerSigmaR;			

	private int upperSigmaR;
	
	// images to filter and check against the ground truth	
	private List<Mat> originalImages;
	
	// the ground truth
	private List<Mat> groundtruthImages;
	
	public int getSampleCount() {
	  return originalImages.size();
	}
	
	public Mat getOriginalImage(int index) {
	  return originalImages.get(index);
	}
	
	public void addToOriginalImages(Mat imagen) {
	  originalImages.add(imagen);
	}
    
    public Mat getGroundtruthImage(int index) {
      return groundtruthImages.get(index);
    }
	
	public void addToGroundtruthImages(Mat imagen) {
      groundtruthImages.add(imagen);
    }
		
	public float getLowerLambda() {
		return lowerLambda;
	}

	public void setLowerLambda(float lowerLambda) {
		this.lowerLambda = lowerLambda;
	}

	public float getUpperLambda() {
		return upperLambda;
	}

	public void setUpperLambda(float upperLambda) {
		this.upperLambda = upperLambda;
	}

	public int getLowerSigmaS() {
		return lowerSigmaS;
	}

	public void setLowerSigmaS(int lowerSigmaS) {
		this.lowerSigmaS = lowerSigmaS;
	}

	public int getUpperSigmaS() {
		return upperSigmaS;
	}

	public void setUpperSigmaS(int upperSigmaS) {
		this.upperSigmaS = upperSigmaS;
	}

	public int getLowerSigmaR() {
		return lowerSigmaR;
	}

	public void setLowerSigmaR(int lowerSigmaR) {
		this.lowerSigmaR = lowerSigmaR;
	}

	public int getUpperSigmaR() {
		return upperSigmaR;
	}

	public void setUpperSigmaR(int upperSigmaR) {
		this.upperSigmaR = upperSigmaR;
	}	
	
	/** Genetic Algorithm configuration **/

	private String title;
	
	private String description;
	
	private int maxIndividuals;
	
	private int maxGenerations;
	
	private float mutationPerc;
	
	private Mutation mutationType;
	
	private Crossover crossoverType;
	
	private Fitness fitnessFunction;
	
	private Segmentation segmentationTechnique;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getMaxIndividuals() {
		return maxIndividuals;
	}

	public void setMaxIndividuals(int maxIndividuals) {
		this.maxIndividuals = maxIndividuals;
	}

	public int getMaxGenerations() {
		return maxGenerations;
	}

	public void setMaxGenerations(int maxGenerations) {
		this.maxGenerations = maxGenerations;
	}

	public float getMutationPerc() {
		return mutationPerc;
	}

	public void setMutationPerc(float mutationPerc) {
		this.mutationPerc = mutationPerc;
	}

	public Mutation getMutationType() {
		return mutationType;
	}

	public void setMutationType(Mutation mutationType) {
		this.mutationType = mutationType;
	}

	public Crossover getCrossoverType() {
		return crossoverType;
	}

	public void setCrossoverType(Crossover crossoverType) {
		this.crossoverType = crossoverType;
	}

	public Fitness getFitnessFunction() {
		return fitnessFunction;
	}

	public void setFitnessFunction(Fitness fitnessFunction) {
		this.fitnessFunction = fitnessFunction;
	}

	public Segmentation getSegmentationTechnique() {
		return segmentationTechnique;
	}

	public void setSegmentationTechnique(Segmentation segmentationTechnique) {
		this.segmentationTechnique = segmentationTechnique;
	}
		
}
