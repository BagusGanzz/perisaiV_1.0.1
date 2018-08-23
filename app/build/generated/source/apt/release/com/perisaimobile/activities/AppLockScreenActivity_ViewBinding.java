// Generated code from Butter Knife. Do not modify!
package com.perisaimobile.activities;

import android.widget.TextView;
import butterknife.internal.Finder;
import com.perisaimobile.base.BaseToolbarActivity_ViewBinding;
import com.studioninja.locker.R;
import com.takwolf.android.lock9.Lock9View;
import java.lang.Object;
import java.lang.Override;

public class AppLockScreenActivity_ViewBinding<T extends AppLockScreenActivity> extends BaseToolbarActivity_ViewBinding<T> {
  public AppLockScreenActivity_ViewBinding(T target, Finder finder, Object source) {
    super(target, finder, source);

    target.layout = finder.findRequiredView(source, R.id.layout, "field 'layout'");
    target.layout_lock = finder.findRequiredView(source, R.id.layout_lock, "field 'layout_lock'");
    target.lock_view = finder.findRequiredViewAsType(source, R.id.lock_view, "field 'lock_view'", Lock9View.class);
    target.lock_view_disvibrate = finder.findRequiredViewAsType(source, R.id.lock_view_disvibrate, "field 'lock_view_disvibrate'", Lock9View.class);
    target.tv_forgot_password = finder.findRequiredViewAsType(source, R.id.tv_forgot_password, "field 'tv_forgot_password'", TextView.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    super.unbind();

    target.layout = null;
    target.layout_lock = null;
    target.lock_view = null;
    target.lock_view_disvibrate = null;
    target.tv_forgot_password = null;
  }
}
