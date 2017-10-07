package com.parma.genetics.fitness;

import java.util.concurrent.ExecutionException;
import org.opencv.core.Mat;
import com.parma.filter.DnlmFilter;
import com.parma.genetics.ParamIndividual;
import com.parma.genetics.settings.Fitness;
import com.parma.genetics.settings.Segmentation;
import com.parma.segmentation.Dice;
import com.parma.segmentation.Otsu;
import com.parma.segmentation.Thresholding;

public class FitnessEval {

  private Fitness fitnessFunction;
  private Segmentation segTechnique;

  public FitnessEval(Fitness fitness, Segmentation seg) {
    this.fitnessFunction = fitness;
    this.segTechnique = seg;
  }

  public double evaluate(ParamIndividual p, Mat pOriginal, Mat pGroundtruth) {

    int w = p.getW();
    int w_n = p.getW_n();
    int sigma_r = p.getSigma_r();

    System.out.println(w);
    System.out.println(w_n);
    System.out.println(sigma_r);
    System.out.println(" -- ");

    w = (w % 2 == 0) ? w++ : w;
    w_n = (w % 2 == 0) ? w_n++ : w_n;

    Mat original = new Mat();
    pOriginal.copyTo(original);

    DnlmFilter filter = new DnlmFilter();

    // filter the image with DNLM-IDFT
    Mat filteredImage = filter.filter(original, w, w_n, sigma_r);
    
    // segmentation of the filtered image
    filteredImage = applySegmentation(filteredImage);

    // calculate fitness with the specified similarity check function
    double fitness = getFitnessResult(filteredImage, pGroundtruth);

    return fitness;
  }


  private Mat applySegmentation(Mat image) {
    /*
     * Watershed segmentation does not return a Threshold and must be implemented differently
     */
    if (segTechnique == Segmentation.WATERSHED) {
      // TODO implementation of watershed
      return new Mat();
    }

    else {
      // calculate the binary threshold with the specified technique
      int threshold = getThreshold(image);
      Thresholding thresholder = new Thresholding();
      // apply the binary segmentation to DNLM-IFFT filtered image
      thresholder.applyThreshold(image, threshold);
      return image;
    }
    
  }

  
  private int getThreshold(Mat image) {
    switch (segTechnique) {
      case OTSU:
        return Otsu.getOtsusThreshold(image);
      /*
       * More methods to be added ...
       */        
      default:
        return 0;
    }
  }


  private double getFitnessResult(Mat image, Mat groundtruth) {
    switch (fitnessFunction) {
      case DICE:
        return Dice.calcularDice(image, groundtruth);
      /*
       * More methods to be added ...
       */
      default:
        return 0;
    }
  }


}
