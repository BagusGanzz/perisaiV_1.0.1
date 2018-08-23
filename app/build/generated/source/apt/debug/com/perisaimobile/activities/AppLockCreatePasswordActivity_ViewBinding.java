// Generated code from Butter Knife. Do not modify!
package com.perisaimobile.activities;

import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import butterknife.internal.Finder;
import com.perisaimobile.base.BaseToolbarActivity_ViewBinding;
import com.studioninja.locker.R;
import com.takwolf.android.lock9.Lock9View;
import java.lang.Object;
import java.lang.Override;

public class AppLockCreatePasswordActivity_ViewBinding<T extends AppLockCreatePasswordActivity> extends BaseToolbarActivity_ViewBinding<T> {
  public AppLockCreatePasswordActivity_ViewBinding(T target, Finder finder, Object source) {
    super(target, finder, source);

    target.done = finder.findRequiredViewAsType(source, R.id.done, "field 'done'", TextView.class);
    target.edt_answer = finder.findRequiredViewAsType(source, R.id.edt_answer, "field 'edt_answer'", EditText.class);
    target.la_password = finder.findRequiredView(source, R.id.la_password, "field 'la_password'");
    target.la_password_again = finder.findRequiredView(source, R.id.la_password_again, "field 'la_password_again'");
    target.la_question = finder.findRequiredView(source, R.id.la_question, "field 'la_question'");
    target.lock_view = finder.findRequiredViewAsType(source, R.id.lock_view, "field 'lock_view'", Lock9View.class);
    target.lock_view_again = finder.findRequiredViewAsType(source, R.id.lock_view_again, "field 'lock_view_again'", Lock9View.class);
    target.spinner_question = finder.findRequiredViewAsType(source, R.id.spinner_question, "field 'spinner_question'", Spinner.class);
    target.title = finder.findRequiredViewAsType(source, R.id.title, "field 'title'", TextView.class);
    target.title_1 = finder.findRequiredViewAsType(source, R.id.title_1, "field 'title_1'", TextView.class);
    target.title_2 = finder.findRequiredViewAsType(source, R.id.title_2, "field 'title_2'", TextView.class);
    target.title_again = finder.findRequiredViewAsType(source, R.id.title_again, "field 'title_again'", TextView.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    super.unbind();

    target.done = null;
    target.edt_answer = null;
    target.la_password = null;
    target.la_password_again = null;
    target.la_question = null;
    target.lock_view = null;
    target.lock_view_again = null;
    target.spinner_question = null;
    target.title = null;
    target.title_1 = null;
    target.title_2 = null;
    target.title_again = null;
  }
}
