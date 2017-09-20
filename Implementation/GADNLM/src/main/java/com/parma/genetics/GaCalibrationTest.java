package com.parma.genetics;

import static org.junit.Assert.*;

import org.junit.Test;
import org.opencv.core.Mat;

import com.parma.genetics.settings.Crossover;
import com.parma.genetics.settings.Fitness;
import com.parma.genetics.settings.GaSettings;
import com.parma.genetics.settings.Mutation;
import com.parma.genetics.settings.Segmentation;
import com.parma.images.ImageHandler;

public class GaCalibrationTest {

	@Test
	public void test() {
		
		
		
		GaSettings settings = new GaSettings();
		settings.setCrossoverType(Crossover.SIMPLE);
		settings.setFitnessFunction(Fitness.DICE);
		settings.setLowerW(5);
		settings.setUpperW(21);
		settings.setLowerWn(3);
		settings.setUpperWn(5);
		settings.setLowerSigmaR(0);
		settings.setUpperSigmaR(100);
		
		settings.setMaxGenerations(100);
		settings.setMaxIndividuals(100);
		settings.setMutationPerc((float)0.05);
		settings.setMutationType(Mutation.BIT_SWAPPING);
		settings.setSegmentationTechnique(Segmentation.OTSU);
		settings.setSelectionThreshold((float)0.65);
		
		ImageHandler imageHandler = new ImageHandler();
		Mat imagen = imageHandler.leerImagenGrises("C:/Users/Eliot/Desktop/1.png");
		Mat imagengd = imageHandler.leerImagenGrises("C:/Users/Eliot/Desktop/1_g.png");
		
		settings.addToOriginalImages(imagen);
		settings.addToGroundtruthImages(imagengd);
		
		
		
		GaCalibration calibration = new GaCalibration(settings);
		
		calibration.runCalibration();
		
		
		
		assertTrue(true);
		
		
		
	}

}
