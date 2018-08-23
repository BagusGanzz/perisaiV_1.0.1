// Generated code from Butter Knife. Do not modify!
package com.perisaimobile.fragment;

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Finder;
import com.studioninja.locker.R;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class ResultAppLockFragment_ViewBinding<T extends ResultAppLockFragment> implements Unbinder {
  protected T target;

  public ResultAppLockFragment_ViewBinding(T target, Finder finder, Object source) {
    this.target = target;

    target.rv_app_lock = finder.findRequiredViewAsType(source, R.id.rv_app_lock, "field 'rv_app_lock'", RecyclerView.class);
    target.tv_decription = finder.findRequiredViewAsType(source, R.id.tv_decription, "field 'tv_decription'", TextView.class);
    target.tv_protect = finder.findRequiredViewAsType(source, R.id.tv_protect, "field 'tv_protect'", TextView.class);
    target.tv_title = finder.findRequiredViewAsType(source, R.id.tv_title, "field 'tv_title'", TextView.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.rv_app_lock = null;
    target.tv_decription = null;
    target.tv_protect = null;
    target.tv_title = null;

    this.target = null;
  }
}
