package redpacket.view;

public interface IRedPacketActivity {

    void loadSuccess(String json);

    void loadFailure(String failure);
}
