package com.perisaimobile.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.TextView;

import com.perisaimobile.util.TypeFaceUttils;
import com.studioninja.locker.R;

public class EnableAccessbilityDialog extends BottomSheetDialog {
    private CallBack callBack;
    private Context context;

    public interface CallBack {
        void execute();
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public EnableAccessbilityDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accessibility_dialog);
        getWindow().setLayout(-1, -2);
        TextView tv_decription = (TextView) findViewById(R.id.tv_decription);
        TextView btn_not_now = (TextView) findViewById(R.id.btn_not_now);
        TextView btn_enable = (TextView) findViewById(R.id.btn_enable);
        TypeFaceUttils.setBold(this.context, (TextView) findViewById(R.id.tv_title));
        TypeFaceUttils.setNomal(this.context, tv_decription);
        TypeFaceUttils.setNomal(this.context, btn_not_now);
        TypeFaceUttils.setNomal(this.context, btn_enable);
        btn_enable.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EnableAccessbilityDialog.this.dismiss();
                EnableAccessbilityDialog.this.context.startActivity(new Intent("android.settings.ACCESSIBILITY_SETTINGS"));
                Dialog dialog = new Dialog(EnableAccessbilityDialog.this.context);
                dialog.getWindow().setType(2003);
                dialog.requestWindowFeature(1);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                dialog.setContentView(R.layout.dialog_guide_accessibility);
                dialog.show();
            }
        });
        btn_not_now.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EnableAccessbilityDialog.this.dismiss();
                if (EnableAccessbilityDialog.this.callBack != null) {
                    EnableAccessbilityDialog.this.callBack.execute();
                }
            }
        });
    }
}
