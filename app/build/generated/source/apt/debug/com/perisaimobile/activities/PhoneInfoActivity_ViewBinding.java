// Generated code from Butter Knife. Do not modify!
package com.perisaimobile.activities;

import android.widget.TextView;
import butterknife.internal.Finder;
import com.liulishuo.magicprogresswidget.MagicProgressBar;
import com.perisaimobile.base.BaseToolbarActivity_ViewBinding;
import com.studioninja.locker.R;
import java.lang.Object;
import java.lang.Override;

public class PhoneInfoActivity_ViewBinding<T extends PhoneInfoActivity> extends BaseToolbarActivity_ViewBinding<T> {
  public PhoneInfoActivity_ViewBinding(T target, Finder finder, Object source) {
    super(target, finder, source);

    target.pb_cpu = finder.findRequiredViewAsType(source, R.id.pb_cpu, "field 'pb_cpu'", MagicProgressBar.class);
    target.pb_ram = finder.findRequiredViewAsType(source, R.id.pb_ram, "field 'pb_ram'", MagicProgressBar.class);
    target.pb_storage = finder.findRequiredViewAsType(source, R.id.pb_storage, "field 'pb_storage'", MagicProgressBar.class);
    target.tv_basic_information = finder.findRequiredViewAsType(source, R.id.tv_basic_information, "field 'tv_basic_information'", TextView.class);
    target.tv_title_cpu = finder.findRequiredViewAsType(source, R.id.tv_title_cpu, "field 'tv_title_cpu'", TextView.class);
    target.tv_title_imei = finder.findRequiredViewAsType(source, R.id.tv_title_imei, "field 'tv_title_imei'", TextView.class);
    target.tv_title_ram = finder.findRequiredViewAsType(source, R.id.tv_title_ram, "field 'tv_title_ram'", TextView.class);
    target.tv_title_root_state = finder.findRequiredViewAsType(source, R.id.tv_title_root_state, "field 'tv_title_root_state'", TextView.class);
    target.tv_title_storage = finder.findRequiredViewAsType(source, R.id.tv_title_storage, "field 'tv_title_storage'", TextView.class);
    target.tv_title_system_os_version = finder.findRequiredViewAsType(source, R.id.tv_title_system_os_version, "field 'tv_title_system_os_version'", TextView.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    super.unbind();

    target.pb_cpu = null;
    target.pb_ram = null;
    target.pb_storage = null;
    target.tv_basic_information = null;
    target.tv_title_cpu = null;
    target.tv_title_imei = null;
    target.tv_title_ram = null;
    target.tv_title_root_state = null;
    target.tv_title_storage = null;
    target.tv_title_system_os_version = null;
  }
}
