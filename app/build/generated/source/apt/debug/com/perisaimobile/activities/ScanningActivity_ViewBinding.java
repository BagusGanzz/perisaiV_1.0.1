// Generated code from Butter Knife. Do not modify!
package com.perisaimobile.activities;

import android.widget.TextView;
import butterknife.internal.Finder;
import com.perisaimobile.base.BaseToolbarActivity_ViewBinding;
import com.studioninja.locker.R;
import java.lang.Object;
import java.lang.Override;

public class ScanningActivity_ViewBinding<T extends ScanningActivity> extends BaseToolbarActivity_ViewBinding<T> {
  public ScanningActivity_ViewBinding(T target, Finder finder, Object source) {
    super(target, finder, source);

    target.bottomIssues = finder.findRequiredViewAsType(source, R.id.bottomIssues, "field 'bottomIssues'", TextView.class);
    target.bottomIssues_booster = finder.findRequiredViewAsType(source, R.id.bottomIssues_booster, "field 'bottomIssues_booster'", TextView.class);
    target.bottomIssues_privacy = finder.findRequiredViewAsType(source, R.id.bottomIssues_privacy, "field 'bottomIssues_privacy'", TextView.class);
    target.tv_progress = finder.findRequiredViewAsType(source, R.id.tv_progress, "field 'tv_progress'", TextView.class);
    target.tv_step = finder.findRequiredViewAsType(source, R.id.tv_step, "field 'tv_step'", TextView.class);
    target.tv_title_booster = finder.findRequiredViewAsType(source, R.id.tv_title_booster, "field 'tv_title_booster'", TextView.class);
    target.tv_title_privacy = finder.findRequiredViewAsType(source, R.id.tv_title_privacy, "field 'tv_title_privacy'", TextView.class);
    target.tv_title_threat = finder.findRequiredViewAsType(source, R.id.tv_title_threat, "field 'tv_title_threat'", TextView.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    super.unbind();

    target.bottomIssues = null;
    target.bottomIssues_booster = null;
    target.bottomIssues_privacy = null;
    target.tv_progress = null;
    target.tv_step = null;
    target.tv_title_booster = null;
    target.tv_title_privacy = null;
    target.tv_title_threat = null;
  }
}
