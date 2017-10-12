package com.parma.model;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.TreeSet;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

@Document(collection = "Calibration")
public class Calibration {

  private Date initTime;
  private Date endTime;

  private int lower_w;
  private int upper_w;
  private int lower_w_n;
  private int upper_w_n;
  private int lower_s_r;
  private int upper_s_r;

  @Indexed(unique = true)
  private String title;
  private String description;

  private int max_ind;
  private int max_gen;
  private double mut_perc;

  private String mut_type;
  private String cross_type;
  private String fit_func;
  private String seg_method; 

  private int best_w;
  private int best_w_n;
  private int best_s_r;
  
  // Progress trackers for MongoDB
  private String status;
  private int current_gen;
  private double current_fitness;

  private String owner;

  @Transient
  private MultipartFile[] originalImages;

  @Transient
  private MultipartFile[] groundtruthImages;

  /**
   * Constructor for Time
   */
  public Calibration() {
    this.initTime = new Date();
    this.initTime.getTime();
    this.endTime = new Date();
    this.endTime.getTime();
    this.best_w = 0;
    this.best_w_n = 0;
    this.best_s_r = 0;    
    this.status = "RUNNING";
    this.current_gen = 1;
    this.current_fitness = 0.0;
  }

  public int getLower_w() {
    return lower_w;
  }

  public void setLower_w(int lower_w) {
    this.lower_w = lower_w;
  }

  public int getUpper_w() {
    return upper_w;
  }

  public void setUpper_w(int upper_w) {
    this.upper_w = upper_w;
  }

  public int getLower_w_n() {
    return lower_w_n;
  }

  public void setLower_w_n(int lower_w_n) {
    this.lower_w_n = lower_w_n;
  }

  public int getUpper_w_n() {
    return upper_w_n;
  }

  public void setUpper_w_n(int upper_w_n) {
    this.upper_w_n = upper_w_n;
  }

  public int getLower_s_r() {
    return lower_s_r;
  }

  public void setLower_s_r(int lower_s_r) {
    this.lower_s_r = lower_s_r;
  }

  public int getUpper_s_r() {
    return upper_s_r;
  }

  public void setUpper_s_r(int upper_s_r) {
    this.upper_s_r = upper_s_r;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getMax_ind() {
    return max_ind;
  }

  public void setMax_ind(int max_ind) {
    this.max_ind = max_ind;
  }

  public int getMax_gen() {
    return max_gen;
  }

  public void setMax_gen(int max_gen) {
    this.max_gen = max_gen;
  }

  public double getMut_perc() {
    return mut_perc;
  }

  public void setMut_perc(double mut_perc) {
    this.mut_perc = mut_perc;
  }

  public String getMut_type() {
    return mut_type;
  }

  public void setMut_type(String mut_type) {
    this.mut_type = mut_type;
  }

  public String getCross_type() {
    return cross_type;
  }

  public void setCross_type(String cross_type) {
    this.cross_type = cross_type;
  }

  public String getFit_func() {
    return fit_func;
  }

  public void setFit_func(String fit_func) {
    this.fit_func = fit_func;
  }

  public String getSeg_method() {
    return seg_method;
  }

  public void setSeg_method(String seg_method) {
    this.seg_method = seg_method;
  }

  public MultipartFile[] getOriginalImages() {
    return originalImages;
  }

  public void setOriginalImages(MultipartFile[] images) {
    this.originalImages = images;
  }

  public MultipartFile[] getGroundtruthImages() {
    return groundtruthImages;
  }

  public void setGroundtruthImages(MultipartFile[] groundtruthImages) {
    this.groundtruthImages = groundtruthImages;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public String getInitTime() {
    SimpleDateFormat outFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    String[] format = outFormatter.format(initTime).split(" ");
    return format[0] + "<br/>" + format[1] + " CST";
  }

  public void setInitTime(Date initTime) {
    this.initTime = initTime;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public int getBest_w() {
    return best_w;
  }

  public void setBest_w(int best_w) {
    this.best_w = best_w;
  }

  public int getBest_w_n() {
    return best_w_n;
  }

  public void setBest_w_n(int best_w_n) {
    this.best_w_n = best_w_n;
  }

  public int getBest_s_r() {
    return best_s_r;
  }

  public void setBest_s_r(int best_s_r) {
    this.best_s_r = best_s_r;
  }

  public Collection<String> getOriginalFilenames() {
    Collection<String> names = new TreeSet<String>();
    for (MultipartFile file : originalImages) {
      names.add(file.getOriginalFilename());
    }
    return names;
  }

  public Collection<String> getGroundtruthFilenames() {
    Collection<String> names = new TreeSet<String>();
    for (MultipartFile file : groundtruthImages) {
      names.add(file.getOriginalFilename());
    }
    return names;
  }
  
  public int getCurrent_gen() {
    return current_gen;
  }

  public void setCurrent_gen(int current_gen) {
    this.current_gen = current_gen;
  }

  public double getCurrent_fitness() {
    return (double)Math.round(current_fitness * 100000d) / 100000d;
  }

  public void setCurrent_fitness(double current_fitness) {
    this.current_fitness = current_fitness;
  }

  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime() {    
    this.endTime.getTime();
  }

}
