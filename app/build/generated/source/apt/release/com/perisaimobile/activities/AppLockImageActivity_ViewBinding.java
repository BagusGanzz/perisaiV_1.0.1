// Generated code from Butter Knife. Do not modify!
package com.perisaimobile.activities;

import android.widget.ImageView;
import android.widget.TextView;
import butterknife.internal.Finder;
import com.perisaimobile.base.BaseToolbarActivity_ViewBinding;
import com.studioninja.locker.R;
import java.lang.Object;
import java.lang.Override;

public class AppLockImageActivity_ViewBinding<T extends AppLockImageActivity> extends BaseToolbarActivity_ViewBinding<T> {
  public AppLockImageActivity_ViewBinding(T target, Finder finder, Object source) {
    super(target, finder, source);

    target.img_del = finder.findRequiredViewAsType(source, R.id.img_del, "field 'img_del'", ImageView.class);
    target.tv_app_name = finder.findRequiredViewAsType(source, R.id.tv_app_name, "field 'tv_app_name'", TextView.class);
    target.tv_date = finder.findRequiredViewAsType(source, R.id.tv_date, "field 'tv_date'", TextView.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    super.unbind();

    target.img_del = null;
    target.tv_app_name = null;
    target.tv_date = null;
  }
}
