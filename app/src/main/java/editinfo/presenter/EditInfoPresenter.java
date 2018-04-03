package editinfo.presenter;


import android.os.Handler;

import java.util.List;

import com.gjzg.bean.SkillsBean;
import editinfo.listener.AddSkillListener;
import editinfo.listener.SubmitListener;
import editinfo.module.EditInfoModule;
import editinfo.module.IEditInfoModule;
import editinfo.view.IEditInfoFragment;
import com.gjzg.bean.UserInfoBean;
import com.gjzg.listener.JsonListener;

public class EditInfoPresenter implements IEditInfoPresenter {

    private IEditInfoFragment editInfoFragment;
    private IEditInfoModule editInfoModule;
    private Handler handler;

    public EditInfoPresenter(IEditInfoFragment editInfoFragment) {
        this.editInfoFragment = editInfoFragment;
        editInfoModule = new EditInfoModule();
        handler = new Handler();
    }

    @Override
    public void skill(String url) {
        editInfoModule.skill(url, new JsonListener() {
            @Override
            public void success(final String json) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        editInfoFragment.skillSuccess(json);
                    }
                });
            }

            @Override
            public void failure(final String failure) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        editInfoFragment.skillFailure(failure);
                    }
                });
            }
        });
    }

    @Override
    public void load(String url) {
        editInfoModule.load(url, new AddSkillListener() {
            @Override
            public void success(final List<SkillsBean> skillsBeanList) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        editInfoFragment.showAddSkillSuccess(skillsBeanList);
                    }
                });
            }

            @Override
            public void failure(final String failure) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        editInfoFragment.showAddSkillFailure(failure);
                    }
                });
            }
        });
    }

    @Override
    public void submit(UserInfoBean userInfoBean) {
        editInfoModule.submit(userInfoBean, new SubmitListener() {
            @Override
            public void success(final String success) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        editInfoFragment.showSubmitSuccess(success);
                    }
                });
            }

            @Override
            public void failure(final String failure) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        editInfoFragment.showSubmitFailure(failure);
                    }
                });
            }
        });
    }

    @Override
    public void destroy() {
        if (editInfoModule != null) {
            editInfoModule.cancelTask();
            editInfoModule = null;
        }
        if (handler != null) {
            handler = null;
        }
        if (editInfoFragment != null) {
            editInfoFragment = null;
        }
    }
}
