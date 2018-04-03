package giveevaluate.view;


public interface IGiveEvaluateFragment {

    void loadSuccess(String json);

    void loadFailure(String failure);
}
