package com.parma.service;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.SortedMap;
import javax.imageio.ImageIO;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.parma.genetics.GaCalibration;
import com.parma.genetics.settings.GaSettings;
import com.parma.images.ImageHandler;
import com.parma.model.Image;
import com.parma.dal.ImageDal;
import com.parma.filter.DnlmFilter;

@Service
public class AsyncProcessService {

  Logger log = LoggerFactory.getLogger(this.getClass().getName());

  @Async
  public void processCalibration(GaSettings settings) {

    log.info("Calibration " + settings.getTitle() + " has started");

    // create and run a GA calibration instance
    GaCalibration gaExecution = new GaCalibration(settings);
    gaExecution.runCalibration();

  }

  @Async
  public void processImages(Image filterSettings) {

    // get filtering main info
    String group = filterSettings.getGroup();
    String owner = filterSettings.getOwner();
    SortedMap<String, Mat> filelist = filterSettings.getMatList();

    // get parameters from settings
    int w = filterSettings.getW();
    int w_n = filterSettings.getW_n();
    int sigma_r = filterSettings.getS_r();

    log.info("Image processing '" + group + "' has started");
    
    /** DEBUG **/
    ImageHandler ih = new ImageHandler();

    try {
      // perform DNLM-IFFT filtering for each file
      for (String filename : filelist.keySet()) {

        // create a new image instance
        Image image = new Image();
        image.setFilename(filename);
        image.setGroup(group);
        image.setOwner(owner);
        
        // apply filtering to one-channel Mat
        DnlmFilter filter = new DnlmFilter();
        Mat res = filter.filter(filelist.get(filename), w, w_n, sigma_r);
        res.convertTo(res, CvType.CV_8U);
        
        // get result and transform to byte array
        int length = (int) (res.total() * res.elemSize());
        byte byteRes[] = new byte[length];           
        res.get(0, 0, byteRes);
              
        // transform byte array to BufferedImage
        BufferedImage bufferImage = new BufferedImage(res.width(), res.rows(), BufferedImage.TYPE_BYTE_GRAY);
        byte[] imgData = ((DataBufferByte)bufferImage.getRaster().getDataBuffer()).getData();
        System.arraycopy(byteRes, 0, imgData, 0, byteRes.length);
        
        // transform BufferedImage to outputstream and encode as Base64
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bufferImage, "png", os);
        String base64res = Base64.getEncoder().encodeToString(os.toByteArray());
        
        // create Image Obj and save to database
        Image saveImg = new Image(filename, group, base64res, owner, w, w_n, sigma_r);        
        ImageDal.saveImage(saveImg);

      }
    } catch (Exception ex) {
      log.info(ex.getMessage());
    }

  }



}
