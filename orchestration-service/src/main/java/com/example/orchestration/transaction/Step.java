package com.example.orchestration.transaction;

import com.example.orchestration.messages.CommandMessage;
import com.example.orchestration.messages.ReplyMessage;
import io.reactivex.rxjava3.core.Observable;

public class Step<N, M> {
  private Transaction<N, M> transaction;
  private Compensation<M> compensation;
  private N transactionData;
  private M compensationData;
  private Step<?, ?> nextStep;
  private Step<?, ?> prevStep;

  private Step(Transaction<N, M> transaction) {
    this.transaction = transaction;
  }

  private Step(Transaction<N, M> transaction, N transactionData) {
    this.transaction = transaction;
    this.transactionData = transactionData;
  }

  private Step(Transaction<N, M> transaction, Compensation<M> compensation, N transactionData) {
    this.transaction = transaction;
    this.compensation = compensation;
    this.transactionData = transactionData;
  }

  private boolean gotTransactionData() {
    return this.transactionData != null;
  }

  public Observable<?> executeTransaction() {
    try {
      return transaction.execute(new CommandMessage<N>(this.transactionData))
          .switchMap(mReplyMessage -> {
            if (!mReplyMessage.isSuccess()) {
              return this.executeCompensation();
            } else if (this.nextStep != null) {
              this.compensationData = mReplyMessage.getData();
              this.nextStep.setPrevStep(this);

              if (!this.getNextStep().gotTransactionData()) {
                this.getNextStep().setTransactionData(mReplyMessage.getData());
              }

              return this.nextStep.executeTransaction();
            } else {

              return Observable.just(mReplyMessage);
            }
          });
    } catch (Exception ex) {
      return this.executeCompensation();
    }
  }

  public Observable<ReplyMessage<?>> executeCompensation() {
    if (this.compensation != null) {
      this.compensation.execute(this.compensationData);
    }

    if (this.prevStep != null) {
      this.prevStep.executeCompensation();
    }

    ReplyMessage<?> replyMessage = new ReplyMessage<>(true);
    replyMessage.setFailStatus();
    return Observable.just(replyMessage);
  }

  public void setPrevStep(Step<?, ?> prevStep) {
    this.prevStep = prevStep;
  }

  public void setNextStep(Step<?, ?> nextStep) {
    this.nextStep = nextStep;
  }

  public N getTransactionData() {
    return this.transactionData;
  }

  public <G> void setTransactionData(G data) {
    this.transactionData = (N) data;
  }

  public Step<?, ?> getNextStep() {
    return nextStep;
  }

  public static class StepBuilder {
    private Step firstStep;
    private Step nextStep;

    public <N, M> StepBuilder addStep(Transaction<N, M> transaction) {
      if (firstStep == null) {
        firstStep = new Step(transaction);
        nextStep = firstStep;
      } else {
        Step<N, M> newStep = new Step<>(transaction);
        nextStep.setNextStep(newStep);
        nextStep = newStep;
      }

      return this;
    }

    public <N, M> StepBuilder addStep(Transaction<N, M> transaction, N transactionData) {
      if (firstStep == null) {
        firstStep = new Step(transaction, transactionData);
        nextStep = firstStep;
      } else {
        Step<N, M> newStep = new Step<>(transaction, transactionData);
        nextStep.setNextStep(newStep);
        nextStep = newStep;
      }

      return this;
    }

    public <N, M> StepBuilder addStep(Transaction<N, M> transaction, Compensation<M> compensation, N transactionData) {
      if (firstStep == null) {
        firstStep = new Step(transaction, compensation, transactionData);
        nextStep = firstStep;
      } else {
        Step<N, M> newStep = new Step<>(transaction, compensation, transactionData);
        nextStep.setNextStep(newStep);
        nextStep = newStep;
      }

      return this;
    }

    public Step build() throws Exception {
      if (firstStep == null) {
        throw new Exception("There is no first step");
      }
      return firstStep;
    }
  }

}