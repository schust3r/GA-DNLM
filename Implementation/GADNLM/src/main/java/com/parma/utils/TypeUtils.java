package com.parma.utils;

import com.parma.genetics.settings.Crossover;
import com.parma.genetics.settings.Fitness;
import com.parma.genetics.settings.Mutation;
import com.parma.genetics.settings.Segmentation;

public class TypeUtils {
  
  
  public static Crossover getCrossoverType(String in) {
    switch (in) {
      case "SIMPLE":
        return Crossover.SIMPLE;
      case "BITWISE":
        return Crossover.BITWISE;
      case "CLUSTER":
        return Crossover.CLUSTER;
      default:
        return Crossover.SIMPLE;
    }
  }
  
  
  public static Mutation getMutationType(String in) {
    switch (in) {
      case "RANDOM_BIT":
        return Mutation.RANDOM_BIT;
      case "BIT_SWAPPING":
        return Mutation.BIT_SWAPPING;
      case "ADDITIVE":
        return Mutation.ADDITIVE;
      default:
        return Mutation.RANDOM_BIT;
    }  
  }
  
  
  public static Fitness getFitnessType(String in) {
    switch (in) {
      default:
        return Fitness.DICE;
    }
  }
  
  
  public static Segmentation getSegmentationType(String in) {
    switch (in) {
      default:
        return Segmentation.OTSU;
    }
  }
  

}
