package com.stefan.postservice.api;

import com.stefan.postservice.exception.FailedServiceOperationException;
import com.stefan.postservice.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLConnection;

@RestController
@RequestMapping("/files")
public class FilesApi {
  @Autowired
  private FileStorageService fileStorageService;

  @GetMapping("/{postId}/{fileName}")
  public void getFile(@PathVariable int postId, @PathVariable String fileName, HttpServletResponse response) {
    try {
      File file = fileStorageService.getFile(fileName, Integer.toString(postId));
      String mimeType = URLConnection.guessContentTypeFromName(file.getName());

      if (mimeType == null) {
        mimeType = "application/octet-stream";
      }

      response.setContentType(mimeType);
      response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getName() + "\""));
      response.setContentLength((int) file.length());

      InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
      FileCopyUtils.copy(inputStream, response.getOutputStream());
    } catch (FailedServiceOperationException ex) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND,
          String.format("File %s not found", fileName),
          ex
      );
    } catch (Exception ex) {
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR,
          ex.getMessage(),
          ex
      );
    }
  }

  @DeleteMapping(path = "/{postId}/{fileName}")
  public ResponseEntity<?> deleteFile(@PathVariable int postId, @PathVariable String fileName) {
    try {
      this.fileStorageService.removeFile(String.format("%s/%s", postId, fileName));

      return new ResponseEntity<Boolean>(
          true,
          HttpStatus.OK
      );
    } catch (Exception ex) {
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR,
          ex.getMessage(),
          ex
      );
    }

  }
}