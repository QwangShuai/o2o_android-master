package phoneprove.listener;


public interface ProveMobileCodeListener {

    void success(String success);

    void failure(String failure);
}
