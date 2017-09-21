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
		settings.setUpperSigmaR(150);
		
		settings.setMaxGenerations(10);
		settings.setMaxIndividuals(25);
		settings.setMutationPerc((float)0.05);
		settings.setMutationType(Mutation.BIT_SWAPPING);
		settings.setSegmentationTechnique(Segmentation.OTSU);
		settings.setSelectionThreshold((float)0.55);
		
		ImageHandler imageHandler = new ImageHandler();
		Mat imagen = imageHandler.leerImagenGrises("C:/Users/Eliot/Desktop/1.png");
		Mat imagengd = imageHandler.leerImagenGrises("C:/Users/Eliot/Desktop/1g.png");
		
		settings.addToOriginalImages(imagen);
		settings.addToGroundtruthImages(imagengd);
		
		
		
		GaCalibration calibration = new GaCalibration(settings);
		
		calibration.runCalibration();
		double bestman = calibration.getPopulation().getIndividual(0).getFitness();
		
		
		assertTrue("Resultado incorrecto",Math.abs(bestman - 0.04000904462945238) <= 0.0001);
		
		
		
	}

}
