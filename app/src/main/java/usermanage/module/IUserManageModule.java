package usermanage.module;


import android.content.Context;
import android.net.Uri;

import com.gjzg.listener.JsonListener;
import usermanage.listener.UpLoadingIconListener;

public interface IUserManageModule {

    void info(String url, JsonListener jsonListener);

    void up(Context context, String id, Uri uri, UpLoadingIconListener upLoadingIconListener);

    void cancelTask();
}
