package tec.psa.segmentacion.algoritmos;

import tec.psa.segmentacion.Histograma;
import tec.psa.segmentacion.conf.Const;

/**
 * Clase que contiene los componentes para la implementación del Algoritmo de
 * Kittler, permite obtener el mejor Tao para umbralizar la imagen.
 * 
 * @author Joel Schuster
 *
 */
public class Kittler {

  private Histograma hist;

  // Tao inicializado en 0
  private int tao = 0;

  // Vars auxiliares para almacenar valores (test)
  public double mu0m;
  public double mu1m;
  public double var0m;
  public double var1m;

  /**
   * Constructor.
   * 
   * @param histo
   *          histograma de la matriz de imagen
   */
  public Kittler(Histograma histo) {
    this.hist = histo;
  }

  public Kittler() {
  }

  /**
   * Cálculo de P_i para obtener J(T) y buscar el mejor Tao P es la suma
   * acumulada de los valores del histograma h(z) en un rango a, b. Para P_i: si
   * i = 1 => a = 0 , b = T si i = 2 => a = T + 1 , b = L
   * 
   * @param a
   *          límite inferior de rango a evaluar
   * @param b
   *          límite superior de rango a evaluar
   * @return el valor de P_i
   */
  private int calcularP(int a, int b) {
    int res = 0;
    for (int i = a; i < b; i++) {
      res += hist.getHistograma()[i];
    }
    return res;
  }

  /**
   * Cálculo de u_i para obtener J(T) y buscar el mejor Tao Miu es la suma
   * acumulada de los valores del histograma h(z) por el peso del Tao evaluado
   * en un rango a, b. Para u_i: si i = 1 => a = 0 , b = T , Px = P_1 si i = 2
   * => a = T + 1 , b = L , Px = P_2
   * 
   * @param a
   *          límite inferior de rango a evaluar
   * @param b
   *          límite superior de rango a evaluar
   * @param Px
   *          valor de P calculado previamente
   * @return el valor de u_i
   */
  private double calcularMiu(int a, int b, int p) {
    double res = 0;
    for (int i = a; i < b; i++) {
      res += (double) (i * hist.getHistograma()[i]);
    }
    return res / (double) p;
  }

  /**
   * Cálculo de var_i para obtener J(T) y buscar el mejor Tao La varianza es la
   * suma acumulada del cuadrado de u_i por el valor del histograma en i, en un
   * rango de a hasta b.
   * 
   * Para var_i: si i = 1 => a = 0 , b = T , Px = P_1 , Ux = U_1 si i = 2 => a =
   * T + 1 , b = L , Px = P_2 , Ux = U_2
   * 
   * @param a
   *          límite inferior de rango a evaluar
   * @param b
   *          límite superior de rango a evaluar
   * @param Px
   *          valor de P calculado previamente
   * @param Ux
   *          valor de Miu calculado previamente
   * @return el valor de la varianza_i
   */
  private double calcularVar(int a, int b, int p, double u) {
    double res = 0;
    for (int i = a; i < b; i++) {
      res += ((double) i - u) * ((double) i - u) * (double) hist.getHistograma()[i];
    }
    return res / (double) p;
  }

  /**
   * calcula el J(T) que se debe minimizar.
   * 
   * @param P1
   *          el valor de P_1
   * @param P2
   *          el valor de P_2
   * @param var1
   *          el valor de la varianza 1
   * @param var2
   *          el valor de la varianza 2
   * @return el valor de J(T) para los parámetros
   */
  private double calcularJ(int p1, int p2, double var1, double var2) {

    // Obtiene la desviación a partir de la varianza
    double desv1 = Math.sqrt(var1);
    double desv2 = Math.sqrt(var2);

    // Aplica cálculo simplificado de Kittler
    return 1.0 + 2.0 * ((double) p1 * Math.log(desv1) + (double) p2 * Math.log(desv2))
        - 2.0 * ((double) p1 * Math.log(p1) + (double) p2 * Math.log(p2));
  }

  /**
   * calcula el umbral Tao según algoritmo de Kittler.
   * 
   * @return el mejor Tao para hacer la umbralización binaria de la imagen
   */
  public int calcularUmbral() {

    double min = Double.MAX_VALUE;

    for (int t = 0; t < Const.LIMITE; t++) {

      int p1 = this.calcularP(0, t);
      int p2 = this.calcularP(t + 1, Const.LIMITE);

      double u1 = this.calcularMiu(0, t, p1);
      double u2 = this.calcularMiu(t + 1, Const.LIMITE, p2);

      double var1 = this.calcularVar(0, t, p1, u1);
      double var2 = this.calcularVar(t + 1, Const.LIMITE, p2, u2);

      double j = this.calcularJ(p1, p2, var1, var2);

      if (j < min && j != Double.NEGATIVE_INFINITY) {
        this.tao = t;
        min = j;

        // Almacenar otros valores utilizados
        this.mu0m = u1;
        this.mu1m = u2;
        this.var0m = var1;
        this.var1m = var2;
      }
    }
    return this.tao;
  }

  // Getters & Setters

  /**
   * Setter para el histograma.
   * 
   * @param histo
   *          histograma de una matriz de imagen
   */
  public void setHistograma(Histograma histo) {
    this.hist = histo;
  }

  /**
   * Getter para valor t.
   * 
   * @return umbral t (tao)
   */
  public int getTao() {
    return this.tao;
  }

}