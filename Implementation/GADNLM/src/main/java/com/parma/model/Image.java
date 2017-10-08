package com.parma.model;

import org.springframework.data.annotation.Transient;
import org.springframework.web.multipart.MultipartFile;

public class Image {

  private String filename;
  private String group;
  private String base64;
  private String owner;
  
  @Transient
  private MultipartFile[] filelist; 
  
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

  public MultipartFile[] getFilelist() {
    return filelist;
  }

  public void setFilelist(MultipartFile[] filelist) {
    this.filelist = filelist;
  }  
  

}
