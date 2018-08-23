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

public class JunkFilesFragment_ViewBinding<T extends JunkFilesFragment> implements Unbinder {
  protected T target;

  public JunkFilesFragment_ViewBinding(T target, Finder finder, Object source) {
    this.target = target;

    target.framelayout_boost = finder.findRequiredView(source, R.id.framelayout_boost, "field 'framelayout_boost'");
    target.rv_cache = finder.findRequiredViewAsType(source, R.id.rv_cache, "field 'rv_cache'", RecyclerView.class);
    target.tv_boost = finder.findRequiredViewAsType(source, R.id.tv_boost, "field 'tv_boost'", TextView.class);
    target.tv_junk_files_total = finder.findRequiredViewAsType(source, R.id.tv_junk_files_total, "field 'tv_junk_files_total'", TextView.class);
    target.tv_mb = finder.findRequiredViewAsType(source, R.id.tv_mb, "field 'tv_mb'", TextView.class);
    target.tv_suggested = finder.findRequiredViewAsType(source, R.id.tv_suggested, "field 'tv_suggested'", TextView.class);
    target.tv_title_total_found = finder.findRequiredViewAsType(source, R.id.tv_title_total_found, "field 'tv_title_total_found'", TextView.class);
    target.tv_total_found = finder.findRequiredViewAsType(source, R.id.tv_total_found, "field 'tv_total_found'", TextView.class);
    target.tv_total_mb = finder.findRequiredViewAsType(source, R.id.tv_total_mb, "field 'tv_total_mb'", TextView.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.framelayout_boost = null;
    target.rv_cache = null;
    target.tv_boost = null;
    target.tv_junk_files_total = null;
    target.tv_mb = null;
    target.tv_suggested = null;
    target.tv_title_total_found = null;
    target.tv_total_found = null;
    target.tv_total_mb = null;

    this.target = null;
  }
}
