package com.parma.genetics.fitness;

import org.opencv.core.Mat;
import com.parma.filter.DnlmFilter;
import com.parma.genetics.ParamIndividual;
import com.parma.genetics.settings.Fitness;
import com.parma.images.ImageHandler;
import com.parma.segmentation.Dice;
import com.parma.segmentation.Otsu;
import com.parma.segmentation.Thresholding;

public class FitnessEval {

  private Fitness type;

  public FitnessEval(Fitness type) {
    this.type = type;
  }

  public double evaluate(ParamIndividual p, Mat pOriginal, Mat pGroundtruth) {

    int w = p.getW();
    int w_n = p.getW_n();
    int sigma_r = p.getSigma_r();

    Thresholding thresholder = new Thresholding();
    Mat original = new Mat();
    pOriginal.copyTo(original);
    
    DnlmFilter filter = new DnlmFilter();
    Mat filteredImage = filter.filter(original, w, w_n, sigma_r);
    
    int otsu = Otsu.getOtsusThreshold(filteredImage);
    thresholder.applyThreshold(filteredImage, otsu);
    
    // TODO add Filtering here

    //if (w % 2 == 0) w++;
    
    //DnlmIfftFilter filter = new  DnlmIfftFilter(original, w, 3, sigma_r);
    //filter.filterImage();

 
    //Mat newImagen = new Mat();
    //newImagen
    // ~~~~~~~~~

    if (type == Fitness.DICE) {
      return Dice.calcularDice(filteredImage, pGroundtruth);
    } else {
      return 0;
    }
    
  }

}
