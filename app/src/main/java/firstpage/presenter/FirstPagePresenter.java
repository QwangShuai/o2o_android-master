package firstpage.presenter;

import android.os.Handler;

import firstpage.listener.ComCityListener;
import firstpage.listener.HotCityListener;
import firstpage.listener.LocIdListener;
import firstpage.module.FirstPageModule;
import firstpage.module.IFirstPageModule;
import firstpage.view.IFirstPageFragment;
import com.gjzg.listener.JsonListener;

public class FirstPagePresenter implements IFirstPagePresenter {

    private IFirstPageFragment firstPageFragment;
    private IFirstPageModule firstPageModule;
    private Handler handler;

    public FirstPagePresenter(IFirstPageFragment firstPageFragment) {
        this.firstPageFragment = firstPageFragment;
        firstPageModule = new FirstPageModule();
        handler = new Handler();
    }

    @Override
    public void loadHotCity(String hotUrl) {
        firstPageModule.loadHotCity(hotUrl, new HotCityListener() {
            @Override
            public void success(final String hotCityJson) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        firstPageFragment.showHotSuccess(hotCityJson);
                    }
                });
            }

            @Override
            public void failure(final String failure) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        firstPageFragment.showHotFailure(failure);
                    }
                });
            }
        });
    }

    @Override
    public void loadComCity(String comUrl) {
        firstPageModule.loadComCity(comUrl, new ComCityListener() {
            @Override
            public void success(final String comCityJson) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        firstPageFragment.showComSuccess(comCityJson);
                    }
                });
            }

            @Override
            public void failure(final String failure) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        firstPageFragment.showComFailure(failure);
                    }
                });
            }
        });
    }

    @Override
    public void getLocId(String[] letter, String locCity, String comJson) {
        firstPageModule.getLocId(letter, locCity, comJson, new LocIdListener() {
            @Override
            public void success(final String id) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        firstPageFragment.showLocIdSuccess(id);
                    }
                });
            }

            @Override
            public void failure(final String failure) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        firstPageFragment.showLocIdFailure(failure);
                    }
                });
            }
        });
    }

    @Override
    public void changePosition(String url) {
        firstPageModule.changePosition(url, new JsonListener() {
            @Override
            public void success(final String json) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        firstPageFragment.changePositionSuccess(json);
                    }
                });
            }

            @Override
            public void failure(final String failure) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        firstPageFragment.changePositionFailure(failure);
                    }
                });
            }
        });
    }

    @Override
    public void destroy() {
        if (firstPageModule != null) {
            firstPageModule.cancelTask();
            firstPageModule = null;
        }
        if (handler != null) {
            handler = null;
        }
        if (firstPageFragment != null) {
            firstPageFragment = null;
        }
    }
}
