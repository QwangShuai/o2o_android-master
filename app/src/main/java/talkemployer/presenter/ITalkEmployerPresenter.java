package talkemployer.presenter;


public interface ITalkEmployerPresenter {

    void load(String url);

    void loadSkill(String url);

    void invite(String url);

    void destroy();
}
