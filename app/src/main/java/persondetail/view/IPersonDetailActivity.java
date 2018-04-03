package persondetail.view;


public interface IPersonDetailActivity {

    void infoSuccess(String json);

    void infoFailure(String failure);

    void skillSuccess(String json);

    void skillFailure(String failure);

    void evaluateSuccess(String json);

    void evaluateFailure(String failure);
}
