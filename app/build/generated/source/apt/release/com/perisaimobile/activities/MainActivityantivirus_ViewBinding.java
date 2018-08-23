// Generated code from Butter Knife. Do not modify!
package com.perisaimobile.activities;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Finder;
import com.studioninja.locker.R;
import java.lang.IllegalStateException;
import java.lang.Object;
import java.lang.Override;

public class MainActivityantivirus_ViewBinding<T extends MainActivityantivirus> implements Unbinder {
  protected T target;

  private View view2131820932;

  private View view2131820865;

  private View view2131820944;

  private View view2131820942;

  private View view2131820943;

  private View view2131820941;

  private View view2131820871;

  private View view2131820945;

  private View view2131820946;

  private View view2131820947;

  public MainActivityantivirus_ViewBinding(final T target, Finder finder, Object source) {
    this.target = target;

    View view;
    view = finder.findRequiredView(source, R.id.bg_animation_scan, "field 'bg_animation_scan' and method 'onStartScan'");
    target.bg_animation_scan = finder.castView(view, R.id.bg_animation_scan, "field 'bg_animation_scan'", ImageView.class);
    view2131820932 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onStartScan();
      }
    });
    target.img_resolvep_roblems = finder.findRequiredViewAsType(source, R.id.img_resolvep_roblems, "field 'img_resolvep_roblems'", ImageView.class);
    target.iv_start_scan = finder.findRequiredViewAsType(source, R.id.iv_start_scan_anim, "field 'iv_start_scan'", ImageView.class);
    target.noti_danger = finder.findRequiredView(source, R.id.noti_danger, "field 'noti_danger'");
    target.notifi_safe = finder.findRequiredView(source, R.id.notifi_safe, "field 'notifi_safe'");
    view = finder.findRequiredView(source, R.id.img_threat, "field 'img_threat' and method 'onPhoneInfo'");
    target.img_threat = finder.castView(view, R.id.img_threat, "field 'img_threat'", ImageView.class);
    view2131820865 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onPhoneInfo();
      }
    });
    target.tv_app_system = finder.findRequiredViewAsType(source, R.id.tv_app_system, "field 'tv_app_system'", TextView.class);
    target.tv_danger = finder.findRequiredViewAsType(source, R.id.tv_danger, "field 'tv_danger'", TextView.class);
    target.tv_first_run = finder.findRequiredViewAsType(source, R.id.tv_first_run, "field 'tv_first_run'", TextView.class);
    target.tv_found_problem = finder.findRequiredViewAsType(source, R.id.tv_found_problem, "field 'tv_found_problem'", TextView.class);
    target.tv_safe = finder.findRequiredViewAsType(source, R.id.tv_safe, "field 'tv_safe'", TextView.class);
    target.tv_scan = finder.findRequiredViewAsType(source, R.id.tv_scan, "field 'tv_scan'", TextView.class);
    view = finder.findRequiredView(source, R.id.batteryx, "method 'batteryx'");
    view2131820944 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.batteryx();
      }
    });
    view = finder.findRequiredView(source, R.id.button5, "method 'appmanager'");
    view2131820942 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.appmanager();
      }
    });
    view = finder.findRequiredView(source, R.id.button2, "method 'infosdevice'");
    view2131820943 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.infosdevice();
      }
    });
    view = finder.findRequiredView(source, R.id.button4, "method 'onStartAppLock'");
    view2131820941 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onStartAppLock();
      }
    });
    view = finder.findRequiredView(source, R.id.img_booster, "method 'onRate'");
    view2131820871 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onRate();
      }
    });
    view = finder.findRequiredView(source, R.id.soon1, "method 'onSoon1'");
    view2131820945 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onSoon1();
      }
    });
    view = finder.findRequiredView(source, R.id.soon2, "method 'onSoon2'");
    view2131820946 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onSoon2();
      }
    });
    view = finder.findRequiredView(source, R.id.soon3, "method 'onSoon3'");
    view2131820947 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onSoon3();
      }
    });
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.bg_animation_scan = null;
    target.img_resolvep_roblems = null;
    target.iv_start_scan = null;
    target.noti_danger = null;
    target.notifi_safe = null;
    target.img_threat = null;
    target.tv_app_system = null;
    target.tv_danger = null;
    target.tv_first_run = null;
    target.tv_found_problem = null;
    target.tv_safe = null;
    target.tv_scan = null;

    view2131820932.setOnClickListener(null);
    view2131820932 = null;
    view2131820865.setOnClickListener(null);
    view2131820865 = null;
    view2131820944.setOnClickListener(null);
    view2131820944 = null;
    view2131820942.setOnClickListener(null);
    view2131820942 = null;
    view2131820943.setOnClickListener(null);
    view2131820943 = null;
    view2131820941.setOnClickListener(null);
    view2131820941 = null;
    view2131820871.setOnClickListener(null);
    view2131820871 = null;
    view2131820945.setOnClickListener(null);
    view2131820945 = null;
    view2131820946.setOnClickListener(null);
    view2131820946 = null;
    view2131820947.setOnClickListener(null);
    view2131820947 = null;

    this.target = null;
  }
}
