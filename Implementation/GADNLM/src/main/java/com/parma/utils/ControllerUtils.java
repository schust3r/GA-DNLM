package com.parma.utils;

import java.util.SortedMap;
import java.util.TreeMap;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.web.multipart.MultipartFile;

public class ControllerUtils {

  /**
   * Generate the OpenCV matrices from a multipart file list.
   * 
   * @param files bytes to generate the images
   * @param applyThreshold indicate if binary threshold must be applied
   * @return a sorted map of filenames and opencv matrices
   */
  public static SortedMap<String, Mat> getNameMatrixMap(MultipartFile[] files,
      boolean groundtruth) {

    try {
      // load opencv
      System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

      SortedMap<String, Mat> matList = new TreeMap<>();
      for (int i = 0; i < files.length; i++) {
        // check if filename is not in name list
        MultipartFile mpf = files[i];
        String filename = mpf.getOriginalFilename();
        // get bytes and create a Mat
        byte[] imgBytes = mpf.getBytes();
        Mat image = Imgcodecs.imdecode(new MatOfByte(imgBytes), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
        // conversion to grayscale
        Mat imageGray = image.clone();
        if (imageGray.channels() > 2) {
          Imgproc.cvtColor(image, imageGray, Imgproc.COLOR_RGB2GRAY);
          if (!groundtruth) {
            imageGray.convertTo(imageGray, CvType.CV_64FC1);
          }
        }
        // must apply threshold for groundtruth images
        if (groundtruth) {
          Imgproc.threshold(imageGray, imageGray, 1, 256, Imgproc.THRESH_BINARY);
        }
        matList.put(filename, imageGray);
      }
      return matList;
    } catch (Exception ex) {
      return null;
    }
  }

}
