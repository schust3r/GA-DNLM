package com.parma.genetics.settings;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;

/**
 * 
 * @author Joel S.
 *
 */

public class GaSettings {
	
	/** Initial parameter ranges **/
	
	private int lowerSigmaR;		
	private int upperSigmaR;
	
	private int lowerW;
	private int upperW;
	
	private int lowerWn;
	private int upperWn;	
	
	/** Genetic Algorithm configuration **/

    private String title;
    
    private String description;
    
    private int maxIndividuals;
    
    private int maxGenerations;
    
    private double mutationPerc;
    
    private Mutation mutationType;
    
    private Crossover crossoverType;
    
    private Fitness fitnessFunction;
    
    private Segmentation segmentationTechnique;
    
    private double selectionThreshold;
    
    // images to filter and check against the ground truth  
    private List<Mat> originalImages;
    
    // the ground truth
    private List<Mat> groundtruthImages;
	
	public GaSettings() {
		originalImages = new ArrayList<Mat>();
		groundtruthImages = new ArrayList<Mat>();
	}
	
	public int getSampleCount() {
	  return originalImages.size();
	}
	
	public Mat getOriginalImage(int index) {
	  return originalImages.get(index);
	}
	
	public void setOriginalImages(List<Mat> oList) {
      this.originalImages = oList;
    }
	
	public void addToOriginalImages(Mat imagen) {
	  originalImages.add(imagen);
	}
    
    public Mat getGroundtruthImage(int index) {
      return groundtruthImages.get(index);
    }
    
    public void setGroundtruthImages(List<Mat> gList) {
      this.groundtruthImages = gList;
    }
	
	public void addToGroundtruthImages(Mat imagen) {
      groundtruthImages.add(imagen);
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

	public int getLowerW() {
		return lowerW;
	}

	public void setLowerW(int lowerW) {
		this.lowerW = lowerW;
	}

	public int getUpperW() {
		return upperW;
	}

	public void setUpperW(int upperW) {
		this.upperW = upperW;
	}

	public int getLowerWn() {
		return lowerWn;
	}

	public void setLowerWn(int lowerWn) {
		this.lowerWn = lowerWn;
	}

	public int getUpperWn() {
		return upperWn;
	}

	public void setUpperWn(int upperWn) {
		this.upperWn = upperWn;
	}		
	
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

	public double getMutationPerc() {
		return mutationPerc;
	}

	public void setMutationPerc(double mutationPerc) {
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

	public double getSelectionThreshold() {
		return selectionThreshold;
	}

	public void setSelectionThreshold(double selectionThreshold) {
		this.selectionThreshold = selectionThreshold;
	}
		
}
