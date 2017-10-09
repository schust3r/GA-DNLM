package com.parma.model;

import java.util.SortedMap;
import org.opencv.core.Mat;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Image")
public class Image implements Comparable<Image> {

  private String filename;
  private String group;
  private String base64;
  private String owner;

  // filtering parameters
  private int w;
  private int w_n;
  private int s_r;

  public Image() {}

  public Image(String filename, String group, String base64, String owner, 
      int w, int w_n, int s_r) {
    this.filename = filename;
    this.group = group;
    this.base64 = base64;
    this.owner = owner;
    this.w = w;
    this.w_n = w_n;
    this.s_r = s_r;
  }

  @Transient
  private SortedMap<String, Mat> matList;

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public String getBase64() {
    return base64;
  }

  public void setBase64(String base64) {
    this.base64 = base64;
  }

  public String getGroup() {
    return group;
  }

  public void setGroup(String group) {
    this.group = group;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public int getW() {
    return w;
  }

  public void setW(int w) {
    this.w = w;
  }

  public int getW_n() {
    return w_n;
  }

  public void setW_n(int w_n) {
    this.w_n = w_n;
  }

  public int getS_r() {
    return s_r;
  }

  public void setS_r(int s_r) {
    this.s_r = s_r;
  }

  public SortedMap<String, Mat> getMatList() {
    return matList;
  }

  public void setMatList(SortedMap<String, Mat> matList) {
    this.matList = matList;
  }

  @Override
  public int compareTo(Image other) {
    return this.group.compareTo(other.group);
  }


}
