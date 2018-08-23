package com.perisaimobile.activities;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.perisaimobile.base.BaseToolbarActivity;
import com.perisaimobile.service.MonitorShieldService;
import com.perisaimobile.util.TypeFaceUttils;
import com.studioninja.locker.R;

import org.zakariya.stickyheaders.BuildConfig;

import butterknife.BindView;

public class AppLockSettingsActivity extends BaseToolbarActivity {
    public static final String KEY_SELFIE = "selfie";
    public static final String KEY_VIBRATE = "vibrate";
    private boolean bound;
    @BindView(R.id.checkbox_applocker_service)
    CheckBox checkbox_applocker_service;
    @BindView(R.id.checkbox_relock_policy)
    CheckBox checkbox_relock_policy;
    @BindView(R.id.checkbox_selfie)
    CheckBox checkbox_selfie;
    @BindView(R.id.checkbox_vibrate)
    CheckBox checkbox_vibrate;
    @BindView(R.id.item_edit_password)
    View item_edit_password;
    @BindView(R.id.item_relock_timeout)
    View item_relock_timeout;
    @BindView(R.id.item_selfie)
    View item_selfie;
    private MonitorShieldService monitorShieldService;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            AppLockSettingsActivity.this.bound = true;
            AppLockSettingsActivity.this.monitorShieldService = ((MonitorShieldService.MonitorShieldLocalBinder) service).getServiceInstance();
            AppLockSettingsActivity.this.initView();
        }

        public void onServiceDisconnected(ComponentName name) {
            AppLockSettingsActivity.this.bound = false;
            AppLockSettingsActivity.this.monitorShieldService = null;
        }
    };
    private SharedPreferences sharedPreferences;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.title_1)
    TextView title_1;
    @BindView(R.id.tv_applocker_service)
    TextView tv_applocker_service;
    @BindView(R.id.tv_applocker_service_decription)
    TextView tv_applocker_service_decription;
    @BindView(R.id.tv_edit_password)
    TextView tv_edit_password;
    @BindView(R.id.tv_edit_password_deription)
    TextView tv_edit_password_deription;
    @BindView(R.id.tv_relock_policy)
    TextView tv_relock_policy;
    @BindView(R.id.tv_relock_policy_decription)
    TextView tv_relock_policy_decription;
    @BindView(R.id.tv_relock_timeout)
    TextView tv_relock_timeout;
    @BindView(R.id.tv_relock_timeout_deription)
    TextView tv_relock_timeout_deription;
    @BindView(R.id.tv_selfie)
    TextView tv_selfie;
    @BindView(R.id.tv_selfie_deription)
    TextView tv_selfie_deription;
    @BindView(R.id.tv_vibrate)
    TextView tv_vibrate;
    @BindView(R.id.tv_vibrate_deription)
    TextView tv_vibrate_deription;

    private void customFont() {
        TypeFaceUttils.setNomal((Context) this, this.title);
        TypeFaceUttils.setNomal((Context) this, this.tv_applocker_service);
        TypeFaceUttils.setNomal((Context) this, this.tv_applocker_service_decription);
        TypeFaceUttils.setNomal((Context) this, this.title_1);
        TypeFaceUttils.setNomal((Context) this, this.tv_edit_password);
        TypeFaceUttils.setNomal((Context) this, this.tv_edit_password_deription);
        TypeFaceUttils.setNomal((Context) this, this.tv_relock_policy);
        TypeFaceUttils.setNomal((Context) this, this.tv_relock_policy_decription);
        TypeFaceUttils.setNomal((Context) this, this.tv_relock_timeout);
        TypeFaceUttils.setNomal((Context) this, this.tv_relock_timeout_deription);
        TypeFaceUttils.setNomal((Context) this, this.tv_selfie);
        TypeFaceUttils.setNomal((Context) this, this.tv_selfie_deription);
        TypeFaceUttils.setNomal((Context) this, this.tv_vibrate);
        TypeFaceUttils.setNomal((Context) this, this.tv_vibrate_deription);
    }

    public int getLayoutId() {
        return R.layout.activity_app_lock_settings;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customFont();
        this.sharedPreferences = getSharedPreferences(AppLockCreatePasswordActivity.SHARED_PREFERENCES_NAME, 0);
        bindService(new Intent(this, MonitorShieldService.class), this.serviceConnection, 1);
    }

    private void initView() {
        this.checkbox_applocker_service.setChecked(this.sharedPreferences.getBoolean(AppLockCreatePasswordActivity.KEY_APPLOCKER_SERVICE, true));
        this.checkbox_applocker_service.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AppLockSettingsActivity.this.sharedPreferences.edit().putBoolean(AppLockCreatePasswordActivity.KEY_APPLOCKER_SERVICE, isChecked).apply();
                if (isChecked) {
                    AppLockSettingsActivity.this.monitorShieldService.startLockAppTask();
                } else {
                    AppLockSettingsActivity.this.monitorShieldService.stopLockAppTask();
                }
            }
        });
        this.item_edit_password.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                AppLockSettingsActivity.this.startActivity(new Intent(AppLockSettingsActivity.this, AppLockEditPasswordActivity.class));
            }
        });
        this.checkbox_relock_policy.setChecked(this.sharedPreferences.getBoolean(AppLockCreatePasswordActivity.KEY_RELOCK_POLICY, false));
        this.checkbox_relock_policy.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AppLockSettingsActivity.this.sharedPreferences.edit().putBoolean(AppLockCreatePasswordActivity.KEY_RELOCK_POLICY, isChecked).apply();
            }
        });
        this.item_selfie.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
           //     AppLockSettingsActivity.this.startActivity(new Intent(AppLockSettingsActivity.this, AppLockImagesActivity.class));
            }
        });
        this.item_relock_timeout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Dialog dialog = new Dialog(AppLockSettingsActivity.this);
                dialog.requestWindowFeature(1);
                dialog.setContentView(R.layout.dialog_single_choice_items);
                LayoutParams params = dialog.getWindow().getAttributes();
                params.width = -1;
                dialog.getWindow().setAttributes(params);
                RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radio_group);
                TypeFaceUttils.setNomal(AppLockSettingsActivity.this, (RadioButton) dialog.findViewById(R.id.rd_1));
                TypeFaceUttils.setNomal(AppLockSettingsActivity.this, (RadioButton) dialog.findViewById(R.id.rd_3));
                TypeFaceUttils.setNomal(AppLockSettingsActivity.this, (RadioButton) dialog.findViewById(R.id.rd_5));
                TypeFaceUttils.setNomal(AppLockSettingsActivity.this, (RadioButton) dialog.findViewById(R.id.rd_15));
                TypeFaceUttils.setNomal(AppLockSettingsActivity.this, (RadioButton) dialog.findViewById(R.id.rd_30));
                switch (AppLockSettingsActivity.this.sharedPreferences.getInt(AppLockCreatePasswordActivity.KEY_RELOCK_TIMEOUT, 1)) {
                    case BuildConfig.VERSION_CODE /*1*/:
                        radioGroup.check(R.id.rd_1);
                        break;
                    case org.zakariya.stickyheaders.R.styleable.RecyclerView_spanCount /*3*/:
                        radioGroup.check(R.id.rd_3);
                        break;
                    case org.zakariya.stickyheaders.R.styleable.RecyclerView_stackFromEnd /*5*/:
                        radioGroup.check(R.id.rd_5);
                        break;
                    case R.styleable.Toolbar_titleMarginStart /*15*/:
                        radioGroup.check(R.id.rd_15);
                        break;
                    case R.styleable.AppCompatTheme_actionModeSplitBackground /*30*/:
                        radioGroup.check(R.id.rd_30);
                        break;
                }
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        int values = 1;
                        switch (checkedId) {
                            case R.id.rd_1 /*2131820887*/:
                                values = 1;
                                break;
                            case R.id.rd_3 /*2131820888*/:
                                values = 3;
                                break;
                            case R.id.rd_5 /*2131820889*/:
                                values = 5;
                                break;
                            case R.id.rd_15 /*2131820890*/:
                                values = 15;
                                break;
                            case R.id.rd_30 /*2131820891*/:
                                values = 30;
                                break;
                        }
                        AppLockSettingsActivity.this.sharedPreferences.edit().putInt(AppLockCreatePasswordActivity.KEY_RELOCK_TIMEOUT, values).apply();
                    }
                });
                dialog.show();
            }
        });
//        this.checkbox_selfie.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                if (!AppLockSettingsActivity.this.checkbox_selfie.isChecked()) {
//                    AppLockSettingsActivity.this.sharedPreferences.edit().putBoolean(AppLockSettingsActivity.KEY_SELFIE, false).apply();
//                } else if (VERSION.SDK_INT >= 23) {
//                    boolean permissionCamera = ContextCompat.checkSelfPermission(AppLockSettingsActivity.this, "android.permission.CAMERA") == 0;
//                    boolean permissionRead;
//                    if (ContextCompat.checkSelfPermission(AppLockSettingsActivity.this, "android.permission.READ_EXTERNAL_STORAGE") == 0) {
//                        permissionRead = true;
//                    } else {
//                        permissionRead = false;
//                    }
//                    boolean permissionWrite;
//                    if (ContextCompat.checkSelfPermission(AppLockSettingsActivity.this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
//                        permissionWrite = true;
//                    } else {
//                        permissionWrite = false;
//                    }
//                    if (permissionCamera && permissionRead && permissionWrite) {
//                        AppLockSettingsActivity.this.sharedPreferences.edit().putBoolean(AppLockSettingsActivity.KEY_SELFIE, true).apply();
//                    } else {
//                        AppLockSettingsActivity.this.requestPermissions(new String[]{"android.permission.CAMERA", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 1122);
//                    }
//                } else {
//                    AppLockSettingsActivity.this.sharedPreferences.edit().putBoolean(AppLockSettingsActivity.KEY_SELFIE, true).apply();
//                }
//            }
//        });
        this.checkbox_vibrate.setChecked(this.sharedPreferences.getBoolean(KEY_VIBRATE, false));
        this.checkbox_vibrate.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AppLockSettingsActivity.this.sharedPreferences.edit().putBoolean(AppLockSettingsActivity.KEY_VIBRATE, isChecked).apply();
            }
        });
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1122:
                boolean permissionCamera;
                if (ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") == 0) {
                    permissionCamera = true;
                } else {
                    permissionCamera = false;
                }
                boolean permissionRead;
                if (ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") == 0) {
                    permissionRead = true;
                } else {
                    permissionRead = false;
                }
                boolean permissionWrite;
                if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
                    permissionWrite = true;
                } else {
                    permissionWrite = false;
                }
                if (!permissionCamera || !permissionRead || !permissionWrite) {
                    this.checkbox_selfie.setChecked(false);
                    break;
                } else {
                    this.sharedPreferences.edit().putBoolean(KEY_SELFIE, true).apply();
                    break;
                }
              //  break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.bound && this.monitorShieldService != null) {
            unbindService(this.serviceConnection);
            this.bound = false;
        }
    }
}
