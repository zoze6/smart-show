package com.coder.zzq.smartshow.toast;

import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Toast;

import com.coder.zzq.smartshow.R;
import com.coder.zzq.smartshow.SmartShow;
import com.coder.zzq.smartshow.Utils;

public class PlainToastManager extends BaseToastManager implements IPlainShow {

    private int mXOffset;
    private int mYOffset;
    private int mVerticalAxisOffset;
    private int mGravity;

    public PlainToastManager() {
        super();
        setupInitialPosInfo();
    }

    @Override
    protected Toast createToast() {
        if (SmartToastDelegate.get().hasToastSetting()
                && SmartToastDelegate.get().getsToastSetting().isCustom()) {

            mView = SmartToastDelegate.get().getsToastSetting().getCustomView();
            mMsgView = mView.findViewById(R.id.custom_toast_msg);
            mToast = new Toast(SmartShow.getContext());
            mToast.setView(mView);

        } else {
            mToast = Toast.makeText(SmartShow.getContext(), "", Toast.LENGTH_SHORT);
            mView = mToast.getView();
            mMsgView = mView.findViewById(android.R.id.message);
        }

        return mToast;
    }

    @Override
    protected void rebuildToast() {
        super.rebuildToast();
        setupInitialPosInfo();
    }


    private void setupInitialPosInfo() {
        mGravity = mToast.getGravity();
        mXOffset = mToast.getXOffset();
        mVerticalAxisOffset = mYOffset = mToast.getYOffset();
    }

    @Override
    protected void setupToast() {
        super.setupToast();
        if (SmartToastDelegate.get().hasToastSetting()) {

            if (SmartToastDelegate.get().getsToastSetting().isBgColorSetup()) {
                if (SmartToastDelegate.get().getsToastSetting().isCustom()) {
                    mView.setBackgroundColor(SmartToastDelegate.get().getsToastSetting().getBgColor());
                } else {
                    DrawableCompat.setTint(mView.getBackground(), SmartToastDelegate.get().getsToastSetting().getBgColor());
                }
            }

            if (SmartToastDelegate.get().getsToastSetting().isTextColorSetup()) {
                mMsgView.setTextColor(SmartToastDelegate.get().getsToastSetting().getTextColor());
            }
            if (SmartToastDelegate.get().getsToastSetting().isTextSizeSetup()) {
                mMsgView.setTextSize(TypedValue.COMPLEX_UNIT_SP, SmartToastDelegate.get().getsToastSetting().getTextSizeSp());
            }

            mMsgView.setGravity(Gravity.CENTER);
            mMsgView.getPaint().setFakeBoldText(SmartToastDelegate.get().getsToastSetting().isTextBold());
            if (SmartToastDelegate.get().getsToastSetting().isViewCallbackSetup()) {
                SmartToastDelegate.get().getsToastSetting().getIProcessToastCallback().processView(SmartToastDelegate.get().getsToastSetting().isCustom(), mView, mMsgView);
            }

        }

    }

    @Override
    public void show(CharSequence msg) {
        showHelper(msg, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, mVerticalAxisOffset, Toast.LENGTH_SHORT);
    }

    @Override
    public void showAtTop(CharSequence msg) {
        showHelper(msg, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, mVerticalAxisOffset, Toast.LENGTH_SHORT);
    }

    @Override
    public void showInCenter(CharSequence msg) {
        showHelper(msg, Gravity.CENTER, 0, 0, Toast.LENGTH_SHORT);
    }

    @Override
    public void showAtLocation(CharSequence msg, int gravity, float xOffsetDp, float yOffsetDp) {
        int xOffset = Utils.dpToPx(xOffsetDp);
        int yOffset = Utils.dpToPx(yOffsetDp);
        showHelper(msg, gravity, xOffset, yOffset, Toast.LENGTH_SHORT);
    }

    @Override
    public void showLong(CharSequence msg) {
        showHelper(msg, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, mVerticalAxisOffset, Toast.LENGTH_LONG);
    }

    @Override
    public void showLongAtTop(CharSequence msg) {
        showHelper(msg, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, mVerticalAxisOffset, Toast.LENGTH_LONG);
    }

    @Override
    public void showLongInCenter(CharSequence msg) {
        showHelper(msg, Gravity.CENTER, 0, 0, Toast.LENGTH_LONG);
    }

    @Override
    public void showLongAtLocation(CharSequence msg, int gravity, float xOffsetDp, float yOffsetDp) {
        int xOffset = Utils.dpToPx(xOffsetDp);
        int yOffset = Utils.dpToPx(yOffsetDp);
        showHelper(msg, gravity, xOffset, yOffset, Toast.LENGTH_LONG);
    }

    private void showHelper(CharSequence msg, int gravity, int xOffset, int yOffset, int duration) {
        SmartToastDelegate.get().dismissTypeShowIfNeed();
        msg = (msg == null) ? "" : msg;
        //位置是否改变
        boolean locationChanged = locationChanged(gravity, xOffset, yOffset);
        //文本是否改变
        boolean contentChanged = !mCurMsg.equals(msg);

        mCurMsg = msg;
        mDuration = duration;
        mGravity = gravity;
        mXOffset = xOffset;
        mYOffset = yOffset;

        if (isShowing() && (locationChanged || contentChanged)) {
            dismiss();
        } else {
            updateToast();
        }

        mToast.setGravity(mGravity, mXOffset, mYOffset);
        mToast.setDuration(mDuration);
        mToast.show();
    }

    private boolean locationChanged(int gravity, int xOffset, int yOffset) {
        return mGravity != gravity || mXOffset != xOffset || mYOffset != yOffset;
    }

    public void cancel() {
        mToast.cancel();
        rebuildToast();
    }
}