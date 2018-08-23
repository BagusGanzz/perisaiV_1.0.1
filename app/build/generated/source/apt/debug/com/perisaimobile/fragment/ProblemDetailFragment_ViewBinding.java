// Generated code from Butter Knife. Do not modify!
package com.perisaimobile.fragment;

import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Finder;
import com.studioninja.locker.R;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class ProblemDetailFragment_ViewBinding<T extends ProblemDetailFragment> implements Unbinder {
  protected T target;

  public ProblemDetailFragment_ViewBinding(T target, Finder finder, Object source) {
    this.target = target;

    target.bt_ignore_setting = finder.findRequiredViewAsType(source, R.id.bt_ignore_setting, "field 'bt_ignore_setting'", ImageView.class);
    target.bt_open_setting = finder.findRequiredViewAsType(source, R.id.bt_open_setting, "field 'bt_open_setting'", ImageView.class);
    target.bt_trust_app = finder.findRequiredViewAsType(source, R.id.bt_trust_app, "field 'bt_trust_app'", ImageView.class);
    target.bt_uninstall_app = finder.findRequiredViewAsType(source, R.id.bt_uninstall_app, "field 'bt_uninstall_app'", ImageView.class);
    target.iv_icon_app = finder.findRequiredViewAsType(source, R.id.iv_icon_app, "field 'iv_icon_app'", ImageView.class);
    target.ll_layout_for_app = finder.findRequiredViewAsType(source, R.id.ll_layout_for_app, "field 'll_layout_for_app'", LinearLayout.class);
    target.ll_layout_for_system = finder.findRequiredViewAsType(source, R.id.ll_layout_for_system, "field 'll_layout_for_system'", LinearLayout.class);
    target.rv_warning_problem = finder.findRequiredViewAsType(source, R.id.rv_warning_problem, "field 'rv_warning_problem'", RecyclerView.class);
    target.tv_app_name = finder.findRequiredViewAsType(source, R.id.tv_app_name, "field 'tv_app_name'", TextView.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.bt_ignore_setting = null;
    target.bt_open_setting = null;
    target.bt_trust_app = null;
    target.bt_uninstall_app = null;
    target.iv_icon_app = null;
    target.ll_layout_for_app = null;
    target.ll_layout_for_system = null;
    target.rv_warning_problem = null;
    target.tv_app_name = null;

    this.target = null;
  }
}
