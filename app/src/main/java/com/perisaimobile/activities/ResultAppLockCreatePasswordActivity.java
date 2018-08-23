package com.perisaimobile.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.perisaimobile.adapter.QuestionSpinnerAdapter;
import com.perisaimobile.base.BaseToolbarActivity;
import com.perisaimobile.util.TypeFaceUttils;
import com.takwolf.android.lock9.Lock9View;
import com.takwolf.android.lock9.Lock9View.CallBack;
import com.studioninja.locker.R;

import butterknife.BindView;

public class ResultAppLockCreatePasswordActivity extends BaseToolbarActivity {
    @BindView(R.id.done)
    TextView done;
    @BindView(R.id.edt_answer)
    EditText edt_answer;
    @BindView(R.id.la_password)
    View la_password;
    @BindView(R.id.la_password_again)
    View la_password_again;
    @BindView(R.id.la_question)
    View la_question;
    @BindView(R.id.lock_view)
    Lock9View lock_view;
    @BindView(R.id.lock_view_again)
    Lock9View lock_view_again;
    private String password;
    private String passwordAgain;
    private SharedPreferences sharedPreferences;
    @BindView(R.id.spinner_question)
    Spinner spinner_question;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.title_1)
    TextView title_1;
    @BindView(R.id.title_2)
    TextView title_2;
    @BindView(R.id.title_again)
    TextView title_again;

    private void customFont() {
        TypeFaceUttils.setNomal((Context) this, this.title);
        TypeFaceUttils.setNomal((Context) this, this.title_again);
        TypeFaceUttils.setNomal((Context) this, this.title_1);
        TypeFaceUttils.setNomal((Context) this, this.title_2);
        TypeFaceUttils.setNomal((Context) this, this.done);
    }

    public int getLayoutId() {
        return R.layout.activity_app_lock_create_password;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customFont();
        this.sharedPreferences = getSharedPreferences(AppLockCreatePasswordActivity.SHARED_PREFERENCES_NAME, 0);
        initView();
    }

    private void initView() {
        this.lock_view.setCallBack(new CallBack() {
            public void onFinish(String password) {
                ResultAppLockCreatePasswordActivity.this.password = password;
                if (ResultAppLockCreatePasswordActivity.this.password != null) {
                    ResultAppLockCreatePasswordActivity.this.la_password.setVisibility(8);
                    ResultAppLockCreatePasswordActivity.this.la_password_again.setVisibility(0);
                }
            }
        });
        this.lock_view_again.setCallBack(new CallBack() {
            public void onFinish(String password) {
                ResultAppLockCreatePasswordActivity.this.passwordAgain = password;
                if (ResultAppLockCreatePasswordActivity.this.password.equals(ResultAppLockCreatePasswordActivity.this.passwordAgain)) {
                    ResultAppLockCreatePasswordActivity.this.la_password_again.setVisibility(8);
                    ResultAppLockCreatePasswordActivity.this.la_question.setVisibility(0);
                    return;
                }
                Snackbar.make(ResultAppLockCreatePasswordActivity.this.la_password_again, R.string.patterns_do_not_match, -1).show();
            }
        });
        this.spinner_question.setAdapter(new QuestionSpinnerAdapter(this, getResources().getStringArray(R.array.question_arrays)));
        this.done.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ResultAppLockCreatePasswordActivity.this.actionDone();
            }
        });
        this.edt_answer.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == 6) {
                    ResultAppLockCreatePasswordActivity.this.actionDone();
                }
                return false;
            }
        });
    }

    private void actionDone() {
        if (this.edt_answer.getText().toString().length() == 0) {
            Snackbar.make(this.la_password_again, R.string.answer_is_not_empty, -1).show();
            return;
        }
        Editor editor = this.sharedPreferences.edit();
        editor.putString(AppLockCreatePasswordActivity.KEY_PASSWORD, this.password);
        editor.putString(AppLockCreatePasswordActivity.KEY_QUESTION, this.spinner_question.getSelectedItem().toString());
        editor.putString(AppLockCreatePasswordActivity.KEY_ANSWER, this.edt_answer.getText().toString());
        editor.apply();
        finish();
    }
}
