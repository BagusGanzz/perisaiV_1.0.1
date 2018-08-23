// Generated code from Butter Knife. Do not modify!
package com.perisaimobile.activities;

import android.support.v7.widget.RecyclerView;
import butterknife.internal.Finder;
import com.perisaimobile.base.BaseToolbarActivity_ViewBinding;
import com.studioninja.locker.R;
import java.lang.Object;
import java.lang.Override;

public class AppLockImagesActivity_ViewBinding<T extends AppLockImagesActivity> extends BaseToolbarActivity_ViewBinding<T> {
  public AppLockImagesActivity_ViewBinding(T target, Finder finder, Object source) {
    super(target, finder, source);

    target.rv_images = finder.findRequiredViewAsType(source, R.id.rv_images, "field 'rv_images'", RecyclerView.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    super.unbind();

    target.rv_images = null;
  }
}
