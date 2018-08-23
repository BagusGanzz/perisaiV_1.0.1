// Generated code from Butter Knife. Do not modify!
package com.perisaimobile.activities;

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import butterknife.internal.Finder;
import com.perisaimobile.base.BaseToolbarActivity_ViewBinding;
import com.studioninja.locker.R;
import java.lang.Object;
import java.lang.Override;

public class AppLockHomeActivity_ViewBinding<T extends AppLockHomeActivity> extends BaseToolbarActivity_ViewBinding<T> {
  public AppLockHomeActivity_ViewBinding(T target, Finder finder, Object source) {
    super(target, finder, source);

    target.rv_app_lock = finder.findRequiredViewAsType(source, R.id.rv_app_lock, "field 'rv_app_lock'", RecyclerView.class);
    target.title = finder.findRequiredViewAsType(source, R.id.title, "field 'title'", TextView.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    super.unbind();

    target.rv_app_lock = null;
    target.title = null;
  }
}
