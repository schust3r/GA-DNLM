package com.parma.genetics.fitness;

import org.opencv.core.Mat;
import com.parma.genetics.ParamIndividual;
import com.parma.genetics.settings.Fitness;
import com.parma.segmentation.Dice;
import com.parma.segmentation.Thresholding;

public class FitnessEval {

  private Fitness type;

  public FitnessEval(Fitness type) {
    this.type = type;
  }

  public double evaluate(ParamIndividual p, Mat original, Mat groundtruth) {

    int w = p.getW();
    int w_n = p.getW_n();
    int sigma_r = p.getSigma_r();

    Thresholding thresholder = new Thresholding();
    thresholder.applyThreshold(original, w+w_n+sigma_r);
    thresholder.applyThreshold(groundtruth, 1);
    
    // TODO add Filtering here
    
    

    // ~~~~~~~~~

    if (type == Fitness.DICE) {
      return Dice.calcularDice(original, groundtruth);
    } else {
      return 0;
    }
  }

}
