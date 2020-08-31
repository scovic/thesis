package com.example.orchestration.saga.postservicesagas;

import com.example.orchestration.dto.DetailedPostsDto;
import com.example.orchestration.dto.EmptyDto;
import com.example.orchestration.dto.iamservice.AuthorizeDto;
import com.example.orchestration.dto.iamservice.UserIdsDto;
import com.example.orchestration.dto.postservice.PostDto;
import com.example.orchestration.dto.postservice.PostsDto;
import com.example.orchestration.messages.CommandMessage;
import com.example.orchestration.messages.ReplyMessage;
import com.example.orchestration.proxy.IamServiceProxy;
import com.example.orchestration.proxy.PostServiceProxy;
import com.example.orchestration.transaction.Step;
import io.reactivex.rxjava3.core.Observable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GetPostsSaga {
  private Step step;

  @Autowired
  private IamServiceProxy iamServiceProxy;

  @Autowired
  private PostServiceProxy postServiceProxy;

  public void initSaga(String token) throws Exception {
    AuthorizeDto authorizeDto = new AuthorizeDto(token);

    this.step = new Step.StepBuilder()
        .addStep(
            commandMessage -> iamServiceProxy.authorize(commandMessage),
            authorizeDto
        )
        .<EmptyDto, PostsDto>addStep(
            commandMessage -> this.postServiceProxy.getPosts(commandMessage),
            new EmptyDto()
        )
        .<PostsDto, DetailedPostsDto>addStep(
            commandMessage -> {
              List<PostDto> posts = commandMessage.getData().getPosts();
              List<Integer> authorIds = new ArrayList<>();

              for (PostDto post : posts) {
                authorIds.add(post.getAuthorId());
              }

              return this.iamServiceProxy.getUsers(new CommandMessage<UserIdsDto>(new UserIdsDto(authorIds)))
                  .map(usersDtoReplyMessage -> usersDtoReplyMessage.getData().getUsers()  )
                  .map(users -> new ReplyMessage<DetailedPostsDto>(
                          new DetailedPostsDto(
                              posts,
                              users
                          )
                      )
                  );
            }
        )
        .build();
  }

  public Observable<?> executeSaga() throws Exception {
    return this.step.executeTransaction();
  }
}
