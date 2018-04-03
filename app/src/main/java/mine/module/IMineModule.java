package mine.module;

import mine.listener.MineListener;
import mine.listener.OnLineListener;


public interface IMineModule {

    void load(String id, MineListener mineListener);

    void postOnline(String id, String online, OnLineListener onLineListener);

    void cancelTask();
}
