// Generated code from Butter Knife. Do not modify!
package com.perisaimobile.activities;

import android.widget.TextView;
import butterknife.internal.Finder;
import com.perisaimobile.base.BaseToolbarActivity_ViewBinding;
import com.studioninja.locker.R;
import com.takwolf.android.lock9.Lock9View;
import java.lang.Object;
import java.lang.Override;

public class AppLockEditPasswordActivity_ViewBinding<T extends AppLockEditPasswordActivity> extends BaseToolbarActivity_ViewBinding<T> {
  public AppLockEditPasswordActivity_ViewBinding(T target, Finder finder, Object source) {
    super(target, finder, source);

    target.la_old_password = finder.findRequiredView(source, R.id.la_old_password, "field 'la_old_password'");
    target.la_password = finder.findRequiredView(source, R.id.la_password, "field 'la_password'");
    target.la_password_again = finder.findRequiredView(source, R.id.la_password_again, "field 'la_password_again'");
    target.lock_view = finder.findRequiredViewAsType(source, R.id.lock_view, "field 'lock_view'", Lock9View.class);
    target.lock_view_again = finder.findRequiredViewAsType(source, R.id.lock_view_again, "field 'lock_view_again'", Lock9View.class);
    target.lock_view_old = finder.findRequiredViewAsType(source, R.id.lock_view_old, "field 'lock_view_old'", Lock9View.class);
    target.title = finder.findRequiredViewAsType(source, R.id.title, "field 'title'", TextView.class);
    target.title_again = finder.findRequiredViewAsType(source, R.id.title_again, "field 'title_again'", TextView.class);
    target.title_old = finder.findRequiredViewAsType(source, R.id.title_old, "field 'title_old'", TextView.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    super.unbind();

    target.la_old_password = null;
    target.la_password = null;
    target.la_password_again = null;
    target.lock_view = null;
    target.lock_view_again = null;
    target.lock_view_old = null;
    target.title = null;
    target.title_again = null;
    target.title_old = null;
  }
}
