package usermanage.listener;


import android.graphics.Bitmap;

public interface UpLoadingIconListener {

    void upLoadingIconFailure(String failure);

    void upLoadingIconSuccess(String success, Bitmap bitmap);
}
