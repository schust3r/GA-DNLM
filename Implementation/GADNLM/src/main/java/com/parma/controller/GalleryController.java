package com.parma.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.parma.dal.GalleryDal;
import com.parma.model.Image;

@Controller
public class GalleryController {

  @RequestMapping(value = "/gallery", method = RequestMethod.GET)
  public String dashboard(HttpServletRequest servletRequest, Model model,
      @RequestParam(value = "id", required = false) String groupId,
      @RequestParam(value = "i", required = false) Long index) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    model.addAttribute("username", auth.getName());

    List<Image> imageList = GalleryDal.loadGalleries();
    Collections.sort(imageList);

    SortedSet<String> groups = new TreeSet<String>();
    for (Image img : imageList) {
      groups.add(img.getGroup());
    }

    List<Image> imageList1 = new ArrayList<Image>();
    List<Image> imageList2 = new ArrayList<Image>();

    // manage indexes
    index = (index == null) ? 1 : index;
    int maxI = 0;

    // add empty lists to model
    model.addAttribute("imageList1", imageList1);
    model.addAttribute("imageList2", imageList2);

    // load images if a group name has been loaded
    if (!StringUtils.isEmpty(groupId)) {

      // get only the images relevant to the group
      List<Image> groupList = new ArrayList<Image>();
      for (Image img : imageList) {
        if (img.getGroup().equals(groupId)) {
          groupList.add(img);
        }
      }

      // calculate the number of pages and the starting
      // position in the list of images
      maxI = (int) Math.ceil((double) groupList.size() / 6.0);
      int initPos = (index.intValue() - 1) * 6;

      // add images to the first row
      for (int i = initPos; i < initPos + 3 && i < groupList.size(); i++) {
        imageList1.add(groupList.get(i));
      }

      // add images to the second row
      for (int i = initPos + 3; i < initPos + 6 && i < groupList.size(); i++) {
        imageList2.add(groupList.get(i));
      }

      model.addAttribute("id", groupId);
      model.addAttribute("imageList1", imageList1);
      model.addAttribute("imageList2", imageList2);
    }

    model.addAttribute("groups", new ArrayList<String>(groups));
    model.addAttribute("i", index == null ? 1 : index.intValue());
    model.addAttribute("max_i", maxI);

    return "gallery";
  }

  /**
   * Generate a ZipFile with all the images of a group.
   * 
   * @param request
   * @param model
   * @param response
   * @param imageGroup
   */
  @RequestMapping(value = "/download-images", method = RequestMethod.POST,
      produces = "application/zip")
  public void download(HttpServletRequest request, Model model, HttpServletResponse response,
      @RequestParam("image_group") String imageGroup) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    model.addAttribute("username", auth.getName());

    // set headers
    response.setStatus(HttpServletResponse.SC_OK);
    response.addHeader("Content-Disposition", "attachment; filename=\"" + imageGroup + ".zip\"");

    List<Image> imageList = GalleryDal.loadGallery(imageGroup);
    List<String> filenameList = new ArrayList<String>();

    try (ZipOutputStream zos = new ZipOutputStream(response.getOutputStream())) {

      for (Image img : imageList) {
        // get the original file name and remove the file extension
        String filename = img.getFilename();
        filename = filename.substring(0, filename.lastIndexOf('.'));

        // manage existing filenames to avoid a collision
        filenameList.add(filename);
        int ocurrences = Collections.frequency(filenameList, filename);
        if (ocurrences > 1) {
          filename += " - " + ocurrences;
        }
        filename += ".png"; // add extension again

        ZipEntry entry = new ZipEntry(filename);
        zos.putNextEntry(entry);

        // convert Base64 string to byte array and write to ZipEntry
        byte[] imgBytes = Base64.getDecoder().decode(img.getBase64());
        zos.write(imgBytes);
        zos.closeEntry();
      }

      zos.close();

    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  @RequestMapping(value = "/remove-images", method = RequestMethod.POST)
  public String remove(HttpServletRequest request, Model model,
      @RequestParam("image_group") String imageGroup) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    model.addAttribute("username", auth.getName());

    // remove gallery of name in variable 'imageGroup'
    GalleryDal.removeGallery(imageGroup);

    return "redirect:/gallery";
  }



}
