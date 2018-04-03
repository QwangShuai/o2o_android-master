package talkemployer.view;



public interface ITalkEmployerActivity {

    void success(String json);

    void failure(String failure);

    void skillSuccess(String json);

    void skillFailure(String failure);

    void inviteSuccess(String json);

    void inviteFailure(String json);
}
