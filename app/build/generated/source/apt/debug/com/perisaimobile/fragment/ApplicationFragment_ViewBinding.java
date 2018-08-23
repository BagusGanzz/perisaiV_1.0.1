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

public class ApplicationFragment_ViewBinding<T extends ApplicationFragment> implements Unbinder {
  protected T target;

  public ApplicationFragment_ViewBinding(T target, Finder finder, Object source) {
    this.target = target;

    target.framelayout_skip_all = finder.findRequiredView(source, R.id.framelayout_skip_all, "field 'framelayout_skip_all'");
    target.rv_scan_result = finder.findRequiredViewAsType(source, R.id.rv_scan_result, "field 'rv_scan_result'", RecyclerView.class);
    target.tv_num_of_issues = finder.findRequiredViewAsType(source, R.id.tv_num_of_issues, "field 'tv_num_of_issues'", TextView.class);
    target.tv_skip_all = finder.findRequiredViewAsType(source, R.id.tv_skip_all, "field 'tv_skip_all'", TextView.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.framelayout_skip_all = null;
    target.rv_scan_result = null;
    target.tv_num_of_issues = null;
    target.tv_skip_all = null;

    this.target = null;
  }
}