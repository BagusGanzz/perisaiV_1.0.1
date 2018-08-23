// Generated code from Butter Knife. Do not modify!
package com.perisaimobile.activities;

import android.support.v7.widget.CardView;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.internal.Finder;
import com.perisaimobile.base.BaseToolbarActivity_ViewBinding;
import com.studioninja.locker.R;
import java.lang.Object;
import java.lang.Override;

public class ScanningResultActivity_ViewBinding<T extends ScanningResultActivity> extends BaseToolbarActivity_ViewBinding<T> {
  public ScanningResultActivity_ViewBinding(T target, Finder finder, Object source) {
    super(target, finder, source);

    target.img_app_lock = finder.findRequiredViewAsType(source, R.id.img_app_lock, "field 'img_app_lock'", ImageView.class);
    target.img_app_lock_1 = finder.findRequiredViewAsType(source, R.id.img_app_lock_1, "field 'img_app_lock_1'", ImageView.class);
    target.img_app_lock_2 = finder.findRequiredViewAsType(source, R.id.img_app_lock_2, "field 'img_app_lock_2'", ImageView.class);
    target.img_app_lock_3 = finder.findRequiredViewAsType(source, R.id.img_app_lock_3, "field 'img_app_lock_3'", ImageView.class);
    target.img_application = finder.findRequiredViewAsType(source, R.id.img_application, "field 'img_application'", ImageView.class);
    target.img_application_1 = finder.findRequiredViewAsType(source, R.id.img_application_1, "field 'img_application_1'", ImageView.class);
    target.img_application_2 = finder.findRequiredViewAsType(source, R.id.img_application_2, "field 'img_application_2'", ImageView.class);
    target.img_application_3 = finder.findRequiredViewAsType(source, R.id.img_application_3, "field 'img_application_3'", ImageView.class);
    target.result_app_lock = finder.findRequiredView(source, R.id.result_app_lock, "field 'result_app_lock'");
    target.result_application = finder.findRequiredView(source, R.id.result_application, "field 'result_application'");
    target.result_booster = finder.findRequiredView(source, R.id.result_booster, "field 'result_booster'");
    target.result_junk_files = finder.findRequiredView(source, R.id.result_junk_files, "field 'result_junk_files'");
    target.result_layout = finder.findRequiredView(source, R.id.result_layout, "field 'result_layout'");
    target.scanning_result_layout = finder.findRequiredView(source, R.id.scanning_result_layout, "field 'scanning_result_layout'");
    target.card_view_recommend_app = finder.findRequiredViewAsType(source, R.id.card_view_recommend_app, "field 'card_view_recommend_app'", CardView.class);
    target.tv_app_lock = finder.findRequiredViewAsType(source, R.id.tv_app_lock, "field 'tv_app_lock'", TextView.class);
    target.tv_application = finder.findRequiredViewAsType(source, R.id.tv_application, "field 'tv_application'", TextView.class);
    target.tv_detecting_dangerous = finder.findRequiredViewAsType(source, R.id.tv_detecting_dangerous, "field 'tv_detecting_dangerous'", TextView.class);
    target.tv_freeable_memory = finder.findRequiredViewAsType(source, R.id.tv_freeable_memory, "field 'tv_freeable_memory'", TextView.class);
    target.tv_junk_files_size = finder.findRequiredViewAsType(source, R.id.tv_junk_files_size, "field 'tv_junk_files_size'", TextView.class);
    target.tv_junk_found = finder.findRequiredViewAsType(source, R.id.tv_junk_found, "field 'tv_junk_found'", TextView.class);
    target.tv_mb_junk_files = finder.findRequiredViewAsType(source, R.id.tv_mb_junk_files, "field 'tv_mb_junk_files'", TextView.class);
    target.tv_mb_phone_boost = finder.findRequiredViewAsType(source, R.id.tv_mb_phone_boost, "field 'tv_mb_phone_boost'", TextView.class);
    target.tv_num_of_issues = finder.findRequiredViewAsType(source, R.id.tv_num_of_issues, "field 'tv_num_of_issues'", TextView.class);
    target.tv_phone_boost = finder.findRequiredViewAsType(source, R.id.tv_phone_boost, "field 'tv_phone_boost'", TextView.class);
    target.tv_title_application = finder.findRequiredViewAsType(source, R.id.tv_title_application, "field 'tv_title_application'", TextView.class);
    target.tv_title_junk_files = finder.findRequiredViewAsType(source, R.id.tv_title_junk_files, "field 'tv_title_junk_files'", TextView.class);
    target.tv_title_phone_boost = finder.findRequiredViewAsType(source, R.id.tv_title_phone_boost, "field 'tv_title_phone_boost'", TextView.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    super.unbind();

    target.img_app_lock = null;
    target.img_app_lock_1 = null;
    target.img_app_lock_2 = null;
    target.img_app_lock_3 = null;
    target.img_application = null;
    target.img_application_1 = null;
    target.img_application_2 = null;
    target.img_application_3 = null;
    target.result_app_lock = null;
    target.result_application = null;
    target.result_booster = null;
    target.result_junk_files = null;
    target.result_layout = null;
    target.scanning_result_layout = null;
    target.card_view_recommend_app = null;
    target.tv_app_lock = null;
    target.tv_application = null;
    target.tv_detecting_dangerous = null;
    target.tv_freeable_memory = null;
    target.tv_junk_files_size = null;
    target.tv_junk_found = null;
    target.tv_mb_junk_files = null;
    target.tv_mb_phone_boost = null;
    target.tv_num_of_issues = null;
    target.tv_phone_boost = null;
    target.tv_title_application = null;
    target.tv_title_junk_files = null;
    target.tv_title_phone_boost = null;
  }
}
