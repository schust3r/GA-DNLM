package tec.psa.unit;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.opencv.core.Mat;

import tec.psa.segmentacion.algoritmos.Dice;
import tec.psa.segmentacion.imagenes.ImageHandler;

public class DiceTest {

  @Test
  public void test() {

    // Imagenes de prueba
    ImageHandler ih = new ImageHandler();
    Mat dice1 = ih.leerImagenGrises("test_files/input/perro.png");
    Mat dice2 = ih.leerImagenGrises("test_files/input/perroInverso.png");

    // Indices de dice para 0% de similaridad
    final double diceIndex0 = Dice.calcularDice(dice1, dice2);

    assertTrue("Resultado incorrecto para la prueba de 0% de similaridad", diceIndex0 == 0);

  }

}