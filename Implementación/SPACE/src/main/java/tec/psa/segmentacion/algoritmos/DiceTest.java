package tec.psa.segmentacion.algoritmos;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.opencv.core.Mat;

import tec.psa.segmentacion.algoritmos.Dice;
import tec.psa.segmentacion.conf.Const;
import tec.psa.segmentacion.imagenes.ImageHandler;

public class DiceTest {

  @Test
  public void test() {

    //Imagenes de prueba
    ImageHandler ih = new ImageHandler();
    Mat dice1 = ih.leerImagenGrises(Const.IMG_DIR + "Dice.bmp");
    Mat dice2 = ih.leerImagenGrises(Const.IMG_DIR + "Dice2.bmp");
    Mat dice3 = ih.leerImagenGrises(Const.IMG_DIR + "Dice3.bmp");
    Mat dice4 = ih.leerImagenGrises(Const.IMG_DIR + "Dice4.bmp");

    
    //Indices de dice para 0%,25%,50% y 75% de similaridad
    final double diceIndex0 = Dice.calcularDice(dice3,dice4); 
    final double diceIndex25 = Dice.calcularDice(dice2,dice4); 
    final double diceIndex50 = Dice.calcularDice(dice1,dice3); 
    final double diceIndex75 = Dice.calcularDice(dice2,dice3); 

    assertTrue("Resultado incorrecto para la prueba de 0% de similaridad",
        Math.abs(0 - diceIndex0) < 0.001);

    assertTrue("Resultado incorrecto para la prueba de 25% de similaridad",
        Math.abs(0.25 - diceIndex25) < 0.001);

    assertTrue("Resultado incorrecto para la prueba de 50% de similaridad",
        Math.abs(0.5 - diceIndex50) < 0.001);

    assertTrue("Resultado incorrecto para la prueba de 75% de similaridad",
        Math.abs(0.75 - diceIndex75) < 0.001);

  }

}
