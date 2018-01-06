package com.example.zyl.dqcar.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.example.zyl.dqcar.R;


/**
 * Author: Zhaoyl
 * Date: 2017/4/21 15:03
 * Description: 性别选项
 * PackageName: SexDialog
 * Copyright: 银点商城
 **/
public class SexDialog extends Dialog implements View.OnClickListener {

    LinearLayout boy;
    LinearLayout girl;

    public SexDialog(Context context) {
        super(context, R.style.Dialog);
        setContentView(R.layout.girl_boy_dialog);
        boy = (LinearLayout) findViewById(R.id.boy);
        girl = (LinearLayout) findViewById(R.id.girl);
        boy.setOnClickListener(this);
        girl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.boy:
                if (onSexCallBack != null)
                    onSexCallBack.onSexCallBack(Sex.MAN);
                dismiss();
                break;
            case R.id.girl:
                if (onSexCallBack != null)
                    onSexCallBack.onSexCallBack(Sex.WOMAN);
                dismiss();
                break;
        }
    }

    OnSexCallBack onSexCallBack;

    public void setOnSexCallBack(OnSexCallBack onSexCallBack) {
        this.onSexCallBack = onSexCallBack;
    }


    public interface OnSexCallBack {
        void onSexCallBack(Sex sex);
    }

    public enum Sex {
        //0:男 1:女
        MAN(0, R.mipmap.img_boy, "男"), WOMAN(1, R.mipmap.img_girl, "女");
        private int code;
        private int imageSourceID;
        private String str;

        private Sex(int code, int imageSourceID, String str) {
            this.code = code;
            this.imageSourceID = imageSourceID;
            this.str = str;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public int getImageSourceID() {
            return imageSourceID;
        }

        public void setImageSourceID(int imageSourceID) {
            this.imageSourceID = imageSourceID;
        }

        public String getStr() {
            return str;
        }

        public void setStr(String str) {
            this.str = str;
        }
    }



}
