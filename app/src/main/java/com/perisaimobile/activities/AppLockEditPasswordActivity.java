package com.perisaimobile.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.perisaimobile.base.BaseToolbarActivity;
import com.perisaimobile.util.TypeFaceUttils;
import com.takwolf.android.lock9.Lock9View;
import com.takwolf.android.lock9.Lock9View.CallBack;
import com.studioninja.locker.R;

import butterknife.BindView;

public class AppLockEditPasswordActivity extends BaseToolbarActivity {
    @BindView(R.id.la_old_password)
    View la_old_password;
    @BindView(R.id.la_password)
    View la_password;
    @BindView(R.id.la_password_again)
    View la_password_again;
    @BindView(R.id.lock_view)
    Lock9View lock_view;
    @BindView(R.id.lock_view_again)
    Lock9View lock_view_again;
    @BindView(R.id.lock_view_old)
    Lock9View lock_view_old;
    private String password;
    private SharedPreferences sharedPreferences;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.title_again)
    TextView title_again;
    @BindView(R.id.title_old)
    TextView title_old;

    private void customFont() {
        TypeFaceUttils.setNomal((Context) this, this.title_old);
        TypeFaceUttils.setNomal((Context) this, this.title);
        TypeFaceUttils.setNomal((Context) this, this.title_again);
    }

    public int getLayoutId() {
        return R.layout.activity_app_lock_edit_password;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customFont();
        this.sharedPreferences = getSharedPreferences(AppLockCreatePasswordActivity.SHARED_PREFERENCES_NAME, 0);
        initView();
    }

    private void initView() {
        this.lock_view_old.setCallBack(new CallBack() {
            public void onFinish(String password) {
                if (password.equals(AppLockEditPasswordActivity.this.sharedPreferences.getString(AppLockCreatePasswordActivity.KEY_PASSWORD, null))) {
                    AppLockEditPasswordActivity.this.la_old_password.setVisibility(8);
                    AppLockEditPasswordActivity.this.la_password.setVisibility(0);
                    return;
                }
                Snackbar.make(AppLockEditPasswordActivity.this.la_old_password, R.string.patterns_do_not_match, -1).show();
            }
        });
        this.lock_view.setCallBack(new CallBack() {
            public void onFinish(String password) {
                AppLockEditPasswordActivity.this.password = password;
                AppLockEditPasswordActivity.this.la_password.setVisibility(8);
                AppLockEditPasswordActivity.this.la_password_again.setVisibility(0);
            }
        });
        this.lock_view_again.setCallBack(new CallBack() {
            public void onFinish(String password) {
                if (AppLockEditPasswordActivity.this.password.equals(password)) {
                    AppLockEditPasswordActivity.this.sharedPreferences.edit().putString(AppLockCreatePasswordActivity.KEY_PASSWORD, password).apply();
                    AppLockEditPasswordActivity.this.finish();
                    return;
                }
                Snackbar.make(AppLockEditPasswordActivity.this.la_password_again, R.string.patterns_do_not_match, -1).show();
            }
        });
    }
}
