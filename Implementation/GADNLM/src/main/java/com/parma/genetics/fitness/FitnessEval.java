package com.parma.genetics.fitness;

import org.opencv.core.Mat;
import com.parma.genetics.ParamIndividual;
import com.parma.genetics.settings.Fitness;
import com.parma.segmentation.Dice;

public class FitnessEval {

  private Fitness type;

  public FitnessEval(Fitness type) {
    this.type = type;
  }

  public double evaluate(ParamIndividual p, Mat original, Mat groundtruth) {

    int w = p.getW();
    int w_n = p.getW_n();
    int sigma_r = p.getSigma_r();

    // TODO add Filtering here

    // ~~~~~~~~~

    if (type == Fitness.DICE) {
      return Dice.calcularDice(original, groundtruth);
    } else {
      return 0;
    }
  }

}
