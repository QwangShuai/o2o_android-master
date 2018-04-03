package password.presenter;

import android.os.Handler;

import password.listener.EditPwdListener;
import password.listener.ForgetPwdListener;
import password.listener.ProveOriPwdListener;
import password.listener.SetPwdListener;
import password.module.IPwdModule;
import password.module.PwdModule;
import password.view.IPwdActivity;


public class PwdPresenter implements IPwdPresenter {

    private IPwdActivity pwdActivity;
    private IPwdModule pwdModule;
    private Handler handler;

    public PwdPresenter(IPwdActivity pwdActivity) {
        this.pwdActivity = pwdActivity;
        pwdModule = new PwdModule();
        handler = new Handler();
    }

    @Override
    public void setPwd(String id, String pass) {
        pwdModule.setPwd(id, pass, new SetPwdListener() {
            @Override
            public void success(final String success) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        pwdActivity.showSetPwdSuccess(success);
                    }
                });
            }

            @Override
            public void failure(final String failure) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        pwdActivity.showSetPwdFailure(failure);
                    }
                });
            }
        });
    }

    @Override
    public void proveOriPwd(String id, String pass) {
        pwdModule.proveOriPwd(id, pass, new ProveOriPwdListener() {
            @Override
            public void success(final String success) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        pwdActivity.showProveOriPwdSuccess(success);
                    }
                });
            }

            @Override
            public void failure(final String failure) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        pwdActivity.showProveOriPwdFailure(failure);
                    }
                });
            }
        });
    }

    @Override
    public void editPwd(String id, String oldPass, String newPass) {
        pwdModule.editPwd(id, oldPass, newPass, new EditPwdListener() {
            @Override
            public void success(final String success) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        pwdActivity.showEditPwdSuccess(success);
                    }
                });
            }

            @Override
            public void failure(final String failure) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        pwdActivity.showEditPwdFailure(failure);
                    }
                });
            }
        });
    }

    @Override
    public void forgetPwd(String mobile, String verifycode, String newPwd, String idcard) {
        pwdModule.forgetPwd(mobile, verifycode, newPwd, idcard, new ForgetPwdListener() {
            @Override
            public void success(final String success) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        pwdActivity.showForgetPwdSuccess(success);
                    }
                });
            }

            @Override
            public void failure(final String failure) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        pwdActivity.showForgetPwdFailure(failure);
                    }
                });
            }
        });
    }

    @Override
    public void destroy() {
        if (pwdModule != null) {
            pwdModule.cancelTask();
            pwdModule = null;
        }
        if (handler != null) {
            handler = null;
        }
        if (pwdActivity != null) {
            pwdActivity = null;
        }
    }
}
