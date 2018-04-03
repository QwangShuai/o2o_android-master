package complain.presenter;


import com.gjzg.bean.ToComplainBean;

public interface IComplainPresenter {

    void userInfo(String url);

    void userIssue(String url);

    void submit(String url, ToComplainBean toComplainBean);

    void destory();
}
