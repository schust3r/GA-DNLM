package com.parma.segmentation;

import static org.junit.Assert.*;


import org.junit.Test;
import org.opencv.core.Mat;

import com.parma.images.ImageHandler;;

public class DiceTest {

	@Test
	public void test() {
		ImageHandler imageHandler = new ImageHandler();
		Mat imagen1  = imageHandler.leerImagenColor("test_files/input/dice1.png");
		Mat imagen2  = imageHandler.leerImagenColor("test_files/input/dice2.png");
		Double dice = Dice.calcularDice(imagen1, imagen2);
		Double dice2 = Dice.calcularDice(imagen2, imagen2);
		
		
		assertTrue("Resultado incorreto.", dice == 0.5 && dice2 == 1);
		
	}

}
