// Generated code from Butter Knife. Do not modify!
package com.perisaimobile.activities;

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import butterknife.internal.Finder;
import com.perisaimobile.base.BaseToolbarActivity_ViewBinding;
import com.studioninja.locker.R;
import java.lang.Object;
import java.lang.Override;

public class ResultActivity_ViewBinding<T extends ResultActivity> extends BaseToolbarActivity_ViewBinding<T> {
  public ResultActivity_ViewBinding(T target, Finder finder, Object source) {
    super(target, finder, source);

    target.framelayout_skip_all = finder.findRequiredView(source, R.id.framelayout_skip_all, "field 'framelayout_skip_all'");
    target.result_layout = finder.findRequiredView(source, R.id.result_layout, "field 'result_layout'");
    target.rv_scan_result = finder.findRequiredViewAsType(source, R.id.rv_scan_result, "field 'rv_scan_result'", RecyclerView.class);
    target.tv_num_of_issues = finder.findRequiredViewAsType(source, R.id.tv_num_of_issues, "field 'tv_num_of_issues'", TextView.class);
    target.tv_skip_all = finder.findRequiredViewAsType(source, R.id.tv_skip_all, "field 'tv_skip_all'", TextView.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    super.unbind();

    target.framelayout_skip_all = null;
    target.result_layout = null;
    target.rv_scan_result = null;
    target.tv_num_of_issues = null;
    target.tv_skip_all = null;
  }
}
