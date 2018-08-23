// Generated code from Butter Knife. Do not modify!
package com.perisaimobile.base;

import android.support.v7.widget.Toolbar;
import butterknife.Unbinder;
import butterknife.internal.Finder;
import com.studioninja.locker.R;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class BaseToolbarActivity_ViewBinding<T extends BaseToolbarActivity> implements Unbinder {
  protected T target;

  public BaseToolbarActivity_ViewBinding(T target, Finder finder, Object source) {
    this.target = target;

    target.toolbar = finder.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.toolbar = null;

    this.target = null;
  }
}
