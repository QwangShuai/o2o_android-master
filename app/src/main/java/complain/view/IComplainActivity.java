package complain.view;

public interface IComplainActivity {

    void userInfoSuccess(String json);

    void userInfoFailure(String failure);

    void userIssueSuccess(String json);

    void userIssueFailure(String failure);

    void submitSuccess(String json);

    void submitFailure(String failure);
}
