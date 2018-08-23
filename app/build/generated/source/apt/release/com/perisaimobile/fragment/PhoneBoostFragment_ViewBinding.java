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

public class PhoneBoostFragment_ViewBinding<T extends PhoneBoostFragment> implements Unbinder {
  protected T target;

  public PhoneBoostFragment_ViewBinding(T target, Finder finder, Object source) {
    this.target = target;

    target.framelayout_boost = finder.findRequiredView(source, R.id.framelayout_boost, "field 'framelayout_boost'");
    target.rv_application = finder.findRequiredViewAsType(source, R.id.rv_application, "field 'rv_application'", RecyclerView.class);
    target.tv_boost = finder.findRequiredViewAsType(source, R.id.tv_boost, "field 'tv_boost'", TextView.class);
    target.tv_count_running_app = finder.findRequiredViewAsType(source, R.id.tv_count_running_app, "field 'tv_count_running_app'", TextView.class);
    target.tv_freeable = finder.findRequiredViewAsType(source, R.id.tv_freeable, "field 'tv_freeable'", TextView.class);
    target.tv_mb = finder.findRequiredViewAsType(source, R.id.tv_mb, "field 'tv_mb'", TextView.class);
    target.tv_memory_boost = finder.findRequiredViewAsType(source, R.id.tv_memory_boost, "field 'tv_memory_boost'", TextView.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.framelayout_boost = null;
    target.rv_application = null;
    target.tv_boost = null;
    target.tv_count_running_app = null;
    target.tv_freeable = null;
    target.tv_mb = null;
    target.tv_memory_boost = null;

    this.target = null;
  }
}
