package com.example.orchestration.api;

import com.example.orchestration.config.FileStorageConfig;
import com.example.orchestration.dto.postservice.*;
import com.example.orchestration.messages.ReplyMessage;
import com.example.orchestration.saga.PostServiceSagaManager;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/post-service")
public class PostServiceApi {
  @Autowired
  private PostServiceSagaManager postServiceSagaManager;

  @Autowired
  private FileStorageConfig fileStorageConfig;

  @GetMapping
  public DeferredResult<ResponseEntity<?>> getPosts(@RequestHeader("Authorization") String token) {
    DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();

    try {
      this.postServiceSagaManager.getGetPostsSaga().initSaga(token.substring(7));

      this.postServiceSagaManager.getGetPostsSaga()
          .executeSaga()
          .subscribe(replyMessage -> {
                ReplyMessage rm = (ReplyMessage) replyMessage;
                if (!rm.isSuccess()) {
                  result.setErrorResult(
                      new ResponseStatusException(
                          HttpStatus.BAD_REQUEST,
                          "Something went wrong"
                      ));
                } else {
                  result.setResult(new ResponseEntity<>(
                      rm.getData(),
                      HttpStatus.ACCEPTED
                  ));
                }
              },
              throwable -> {
                result.setErrorResult(throwable);
              });

    } catch (Exception ex) {
      result.setErrorResult(
          new ResponseStatusException(
              HttpStatus.INTERNAL_SERVER_ERROR,
              String.format("Something went wrong - %s", ex.getMessage())
          ));
    }

    return result;
  }

  @GetMapping("/{id}")
  public DeferredResult<ResponseEntity<?>> getPost(
      @RequestHeader("Authorization") String token,
      @PathVariable("id") int postId
  ) {
    DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();

    try {
      this.postServiceSagaManager.getGetPostSaga().initSaga(token.substring(7), postId);

      this.postServiceSagaManager.getGetPostSaga()
          .executeSaga()
          .subscribe(replyMessage -> {
                ReplyMessage rm = (ReplyMessage) replyMessage;
                if (!rm.isSuccess()) {
                  result.setErrorResult(
                      new ResponseStatusException(
                          HttpStatus.BAD_REQUEST,
                          "Something went wrong"
                      ));
                } else {
                  result.setResult(new ResponseEntity<>(
                      rm.getData(),
                      HttpStatus.ACCEPTED
                  ));
                }
              },
              throwable -> {
                result.setErrorResult(throwable);
              }

          );

    } catch (Exception ex) {
      result.setErrorResult(
          new ResponseStatusException(
              HttpStatus.INTERNAL_SERVER_ERROR,
              String.format("Something went wrong - %s", ex.getMessage())
          ));
    }

    return result;
  }

  @GetMapping("/{id}/comments")
  public void getPostComments(@RequestHeader("Authorization") String token, @PathVariable int id) {
    System.out.println(id);
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public DeferredResult<ResponseEntity<?>> createPost(
      @RequestHeader("Authorization") String token,
      @RequestParam("files") MultipartFile[] files,
      PostDto post
  ) {
    DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();

    try {
      this.postServiceSagaManager.getCreatePostSaga().initSaga(
          token.substring(7),
          new CreatePostDto(post, files)
      );

      this.postServiceSagaManager.getCreatePostSaga()
          .executeSaga()
          .subscribe(replyMessage -> {
                ReplyMessage rm = (ReplyMessage) replyMessage;
                if (!rm.isSuccess()) {
                  result.setErrorResult(
                      new ResponseStatusException(
                          HttpStatus.BAD_REQUEST,
                          "Something went wrong"
                      ));
                } else {
                  result.setResult(new ResponseEntity<>(
                      rm.getData(),
                      HttpStatus.ACCEPTED
                  ));
                }
              },
              throwable -> {
                result.setErrorResult(throwable);
              });
    } catch (Exception ex) {
      result.setErrorResult(
          new ResponseStatusException(
              HttpStatus.INTERNAL_SERVER_ERROR,
              String.format("Something went wrong - %s", ex.getMessage())
          ));
    }

    return result;
  }

  @DeleteMapping("/{id}")
  public DeferredResult<ResponseEntity<?>> deletePost(
      @RequestHeader("Authorization") String token,
      @PathVariable("id") int postId
  ) {
    DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();
    PostDto post = new PostDto();
    post.setId(postId);

    try {
      this.postServiceSagaManager.getDeletePostSaga().initSaga(token.substring(7), post);
      this.postServiceSagaManager.getDeletePostSaga()
          .executeSaga()
          .subscribe(replyMessage -> {
                ReplyMessage rm = (ReplyMessage) replyMessage;
                if (!rm.isSuccess()) {
                  result.setErrorResult(
                      new ResponseStatusException(
                          HttpStatus.BAD_REQUEST,
                          "Something went wrong"
                      ));
                } else {
                  result.setResult(new ResponseEntity<>(
                      rm.getData(),
                      HttpStatus.ACCEPTED
                  ));
                }
              },
              throwable -> {
                result.setErrorResult(throwable);
              }
          );

    } catch (Exception ex) {
      result.setErrorResult(
          new ResponseStatusException(
              HttpStatus.INTERNAL_SERVER_ERROR,
              String.format("Something went wrong - %s", ex.getMessage())
          ));
    }

    return result;
  }

  @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public DeferredResult<ResponseEntity<?>> updatePost(
      @RequestHeader("Authorization") String token,
      @RequestParam("files") MultipartFile[] files,
      PostDto post
  ) {
    DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();

    try {
      this.postServiceSagaManager.getUpdatePostSaga().initSaga(
          token.substring(7),
          new UpdatePostDto(post, files)
      );

      this.postServiceSagaManager.getUpdatePostSaga()
          .executeSaga()
          .subscribe(replyMessage -> {
                ReplyMessage rm = (ReplyMessage) replyMessage;
                if (!rm.isSuccess()) {
                  result.setErrorResult(
                      new ResponseStatusException(
                          HttpStatus.BAD_REQUEST,
                          "Something went wrong"
                      ));
                } else {
                  result.setResult(new ResponseEntity<>(
                      rm.getData(),
                      HttpStatus.ACCEPTED
                  ));
                }
              },
              throwable -> {
                result.setErrorResult(throwable);
              });

    } catch (Exception ex) {
      result.setErrorResult(
          new ResponseStatusException(
              HttpStatus.INTERNAL_SERVER_ERROR,
              String.format("Something went wrong - %s", ex.getMessage())
          ));
    }

    return result;
  }

  @PostMapping(path = "/comments", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public DeferredResult<ResponseEntity<?>> createComment(
      @RequestHeader("Authorization") String token,
      CommentDto commentDto
  ) {
    DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();

    try {
      this.postServiceSagaManager.getCreateCommentSaga().initSaga(token.substring(7), commentDto);

      this.postServiceSagaManager.getCreateCommentSaga()
          .executeSaga()
          .subscribe(replyMessage -> {
                ReplyMessage rm = (ReplyMessage) replyMessage;
                if (!rm.isSuccess()) {
                  result.setErrorResult(
                      new ResponseStatusException(
                          HttpStatus.BAD_REQUEST,
                          "Something went wrong"
                      ));
                } else {
                  result.setResult(new ResponseEntity<>(
                      rm.getData(),
                      HttpStatus.ACCEPTED
                  ));
                }
              },
              throwable -> {
                result.setErrorResult(throwable);
              });

    } catch (Exception ex) {
      result.setErrorResult(
          new ResponseStatusException(
              HttpStatus.INTERNAL_SERVER_ERROR,
              String.format("Something went wrong - %s", ex.getMessage())
          ));
    }

    return result;
  }

  @DeleteMapping(path = "/comments", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public DeferredResult<ResponseEntity<?>> deleteComment(
      @RequestHeader("Authorization") String token,
      CommentDto commentDto
  ) {
    DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();

    try {
      this.postServiceSagaManager.getDeleteCommentSaga().initSaga(token.substring(7), commentDto);

      this.postServiceSagaManager.getDeleteCommentSaga()
          .executeSaga()
          .subscribe(replyMessage -> {
                ReplyMessage rm = (ReplyMessage) replyMessage;
                if (!rm.isSuccess()) {
                  result.setErrorResult(
                      new ResponseStatusException(
                          HttpStatus.BAD_REQUEST,
                          "Something went wrong"
                      ));
                } else {
                  result.setResult(new ResponseEntity<>(
                      rm.getData(),
                      HttpStatus.ACCEPTED
                  ));
                }
              },
              throwable -> {
                result.setErrorResult(throwable);
              });

    } catch (Exception ex) {
      result.setErrorResult(
          new ResponseStatusException(
              HttpStatus.INTERNAL_SERVER_ERROR,
              String.format("Something went wrong - %s", ex.getMessage())
          ));
    }

    return result;
  }

  @PutMapping(path = "/comments", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public DeferredResult<ResponseEntity<?>> updateComment(
      @RequestHeader("Authorization") String token,
      CommentDto commentDto
  ) {
    DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();

    try {
      this.postServiceSagaManager.getUpdateCommentSaga().initSaga(token.substring(7), commentDto);

      this.postServiceSagaManager.getUpdateCommentSaga()
          .executeSaga()
          .subscribe(replyMessage -> {
                ReplyMessage rm = (ReplyMessage) replyMessage;
                if (!rm.isSuccess()) {
                  result.setErrorResult(
                      new ResponseStatusException(
                          HttpStatus.BAD_REQUEST,
                          "Something went wrong"
                      ));
                } else {
                  result.setResult(new ResponseEntity<>(
                      rm.getData(),
                      HttpStatus.ACCEPTED
                  ));
                }
              },
              throwable -> {
                result.setErrorResult(throwable);
              });

    } catch (Exception ex) {
      result.setErrorResult(
          new ResponseStatusException(
              HttpStatus.INTERNAL_SERVER_ERROR,
              String.format("Something went wrong - %s", ex.getMessage())
          ));
    }

    return result;
  }

  @GetMapping("/{postId}/{fileName}")
  public DeferredResult<ResponseEntity<StreamingResponseBody>> getFile(
      @RequestHeader("Authorization") String token,
      @PathVariable int postId,
      @PathVariable String fileName,
      HttpServletResponse response) {

    DeferredResult<ResponseEntity<StreamingResponseBody>> result = new DeferredResult<ResponseEntity<StreamingResponseBody>>();

    try {
      this.postServiceSagaManager.getGetFileSaga().initSaga(token.substring(7), new FileDto(postId, fileName));

      this.postServiceSagaManager.getGetFileSaga()
          .executeSaga()
          .subscribe(
              replyMessage -> {
                ReplyMessage<RawFileDto> rm = (ReplyMessage<RawFileDto>) replyMessage;
                if (!rm.isSuccess()) {
                  result.setErrorResult(
                      new ResponseStatusException(
                          HttpStatus.BAD_REQUEST,
                          "Something went wrong"
                      )
                  );
                } else {

                  Path path = Paths.get(String.format("%s/%s", fileStorageConfig.getStorage(), rm.getData().getFileName()));

                  if (!Files.exists(path)) {
                    Files.createFile(path);
                  }

                  File file = new File(path.toString());

                  OutputStream outStream = new FileOutputStream(file);
                  outStream.write(rm.getData().getFileBytes());
                  outStream.close();

                  String mimeType = URLConnection.guessContentTypeFromName(file.getName());

                  if (mimeType == null) {
                    mimeType = "application/octet-stream";
                  }

                  response.setContentType(mimeType);
                  response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getName() + "\""));
                  response.setContentLength((int) file.length());

                  StreamingResponseBody stream = outputStream -> {
                    InputStream inputStream = new FileInputStream(file);
                    IOUtils.copy(inputStream, response.getOutputStream());
                    inputStream.close();
                    response.getOutputStream().flush();
                    Files.deleteIfExists(path);
                  };

                  result.setResult(
                      new ResponseEntity<>(
                          stream,
                          HttpStatus.OK
                      )
                  );
                }
              },
              throwable -> {
                result.setErrorResult(
                    new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        String.format("File %s not found", fileName),
                        throwable
                    ));
              }
          );
    } catch (Exception ex) {
      result.setErrorResult(
          new ResponseStatusException(
              HttpStatus.INTERNAL_SERVER_ERROR,
              String.format("Something went wrong - %s", ex.getMessage())
          ));
    }

    return result;
  }

  @DeleteMapping("/{postId}/{fileName}")
  public DeferredResult<ResponseEntity<?>> deleteFile(
      @RequestHeader("Authorization") String token,
      @PathVariable int postId,
      @PathVariable String fileName
  ) {
    DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();

    try {
      this.postServiceSagaManager.getDeleteFileSaga().initSaga(token.substring(7), new FileDto(postId, fileName));

      this.postServiceSagaManager.getDeleteFileSaga()
          .executeSaga()
          .subscribe(replyMessage -> {
                ReplyMessage rm = (ReplyMessage) replyMessage;
                if (!rm.isSuccess()) {
                  result.setErrorResult(
                      new ResponseStatusException(
                          HttpStatus.BAD_REQUEST,
                          "Something went wrong"
                      ));
                } else {
                  result.setResult(new ResponseEntity<>(
                      rm.getData(),
                      HttpStatus.ACCEPTED
                  ));
                }
              },
              throwable -> {
                result.setErrorResult(throwable);
              });
    } catch (Exception ex) {
      result.setErrorResult(
          new ResponseStatusException(
              HttpStatus.INTERNAL_SERVER_ERROR,
              String.format("Something went wrong - %s", ex.getMessage())
          ));
    }

    return result;
  }

  @GetMapping("/{postId}/comments/all")
  public DeferredResult<ResponseEntity<?>> getPostsComments(
      @RequestHeader("Authorization") String token,
      @PathVariable("postId") int postId
  ) {
    DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();

    try {
      this.postServiceSagaManager.getGetPostsCommentsSaga().initSaga(token.substring(7), postId);

      this.postServiceSagaManager.getGetPostsCommentsSaga()
          .executeSaga()
          .subscribe(replyMessage -> {
                ReplyMessage rm = (ReplyMessage) replyMessage;
                if (!rm.isSuccess()) {
                  result.setErrorResult(
                      new ResponseStatusException(
                          HttpStatus.BAD_REQUEST,
                          "Something went wrong"
                      ));
                } else {
                  result.setResult(new ResponseEntity<>(
                      rm.getData(),
                      HttpStatus.ACCEPTED
                  ));
                }
              },
              throwable -> {
                result.setErrorResult(throwable);
              });
    } catch (Exception ex) {
      result.setErrorResult(
          new ResponseStatusException(
              HttpStatus.INTERNAL_SERVER_ERROR,
              String.format("Something went wrong - %s", ex.getMessage())
          ));
    }

    return result;
  }

}
