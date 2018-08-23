// Generated code from Butter Knife. Do not modify!
package com.perisaimobile.activities;

import android.widget.CheckBox;
import android.widget.TextView;
import butterknife.internal.Finder;
import com.perisaimobile.base.BaseToolbarActivity_ViewBinding;
import com.studioninja.locker.R;
import java.lang.Object;
import java.lang.Override;

public class AppLockSettingsActivity_ViewBinding<T extends AppLockSettingsActivity> extends BaseToolbarActivity_ViewBinding<T> {
  public AppLockSettingsActivity_ViewBinding(T target, Finder finder, Object source) {
    super(target, finder, source);

    target.checkbox_applocker_service = finder.findRequiredViewAsType(source, R.id.checkbox_applocker_service, "field 'checkbox_applocker_service'", CheckBox.class);
    target.checkbox_relock_policy = finder.findRequiredViewAsType(source, R.id.checkbox_relock_policy, "field 'checkbox_relock_policy'", CheckBox.class);
    target.checkbox_selfie = finder.findRequiredViewAsType(source, R.id.checkbox_selfie, "field 'checkbox_selfie'", CheckBox.class);
    target.checkbox_vibrate = finder.findRequiredViewAsType(source, R.id.checkbox_vibrate, "field 'checkbox_vibrate'", CheckBox.class);
    target.item_edit_password = finder.findRequiredView(source, R.id.item_edit_password, "field 'item_edit_password'");
    target.item_relock_timeout = finder.findRequiredView(source, R.id.item_relock_timeout, "field 'item_relock_timeout'");
    target.item_selfie = finder.findRequiredView(source, R.id.item_selfie, "field 'item_selfie'");
    target.title = finder.findRequiredViewAsType(source, R.id.title, "field 'title'", TextView.class);
    target.title_1 = finder.findRequiredViewAsType(source, R.id.title_1, "field 'title_1'", TextView.class);
    target.tv_applocker_service = finder.findRequiredViewAsType(source, R.id.tv_applocker_service, "field 'tv_applocker_service'", TextView.class);
    target.tv_applocker_service_decription = finder.findRequiredViewAsType(source, R.id.tv_applocker_service_decription, "field 'tv_applocker_service_decription'", TextView.class);
    target.tv_edit_password = finder.findRequiredViewAsType(source, R.id.tv_edit_password, "field 'tv_edit_password'", TextView.class);
    target.tv_edit_password_deription = finder.findRequiredViewAsType(source, R.id.tv_edit_password_deription, "field 'tv_edit_password_deription'", TextView.class);
    target.tv_relock_policy = finder.findRequiredViewAsType(source, R.id.tv_relock_policy, "field 'tv_relock_policy'", TextView.class);
    target.tv_relock_policy_decription = finder.findRequiredViewAsType(source, R.id.tv_relock_policy_decription, "field 'tv_relock_policy_decription'", TextView.class);
    target.tv_relock_timeout = finder.findRequiredViewAsType(source, R.id.tv_relock_timeout, "field 'tv_relock_timeout'", TextView.class);
    target.tv_relock_timeout_deription = finder.findRequiredViewAsType(source, R.id.tv_relock_timeout_deription, "field 'tv_relock_timeout_deription'", TextView.class);
    target.tv_selfie = finder.findRequiredViewAsType(source, R.id.tv_selfie, "field 'tv_selfie'", TextView.class);
    target.tv_selfie_deription = finder.findRequiredViewAsType(source, R.id.tv_selfie_deription, "field 'tv_selfie_deription'", TextView.class);
    target.tv_vibrate = finder.findRequiredViewAsType(source, R.id.tv_vibrate, "field 'tv_vibrate'", TextView.class);
    target.tv_vibrate_deription = finder.findRequiredViewAsType(source, R.id.tv_vibrate_deription, "field 'tv_vibrate_deription'", TextView.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    super.unbind();

    target.checkbox_applocker_service = null;
    target.checkbox_relock_policy = null;
    target.checkbox_selfie = null;
    target.checkbox_vibrate = null;
    target.item_edit_password = null;
    target.item_relock_timeout = null;
    target.item_selfie = null;
    target.title = null;
    target.title_1 = null;
    target.tv_applocker_service = null;
    target.tv_applocker_service_decription = null;
    target.tv_edit_password = null;
    target.tv_edit_password_deription = null;
    target.tv_relock_policy = null;
    target.tv_relock_policy_decription = null;
    target.tv_relock_timeout = null;
    target.tv_relock_timeout_deription = null;
    target.tv_selfie = null;
    target.tv_selfie_deription = null;
    target.tv_vibrate = null;
    target.tv_vibrate_deription = null;
  }
}
