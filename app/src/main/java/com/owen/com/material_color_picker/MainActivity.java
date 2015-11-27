package com.owen.com.material_color_picker;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.owen.com.material_color_picker.model.ColorModel;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    @Bind(R.id.view_color) View mViewColor;

    @Bind(R.id.tv_red_tip) TextView mTvRedTip;
    @Bind(R.id.seekBar_red) SeekBar mSeekBarRed;

    @Bind(R.id.tv_green_tip) TextView mTvGreenTip;
    @Bind(R.id.seekBar_green) SeekBar mSeekBarGreen;

    @Bind(R.id.tv_blue_tip) TextView mTvBlueTip;
    @Bind(R.id.seekBar_blue) SeekBar mSeekBarBlue;

    @Bind(R.id.btn_clip_result) Button mBtnClipResult;

    @Bind(R.id.btn_about) Button mBtnAbout;

    int red ,green, blue;

    Realm mRealm;
    ColorModel mColorModel;

    ClipboardManager mClipboard;
    ClipData mClipData;

    Rect thumbRect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        retrieveRGBFromDb();

        mSeekBarRed.setOnSeekBarChangeListener(this);
        mSeekBarGreen.setOnSeekBarChangeListener(this);
        mSeekBarBlue.setOnSeekBarChangeListener(this);

        mSeekBarRed.setProgress(red);
        mSeekBarGreen.setProgress(green);
        mSeekBarBlue.setProgress(blue);

        changeColor();

        mClipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
    }

    public void retrieveRGBFromDb() {
        mRealm = Realm.getInstance(this);
        mRealm.beginTransaction();

        mColorModel = mRealm.where(ColorModel.class).findFirst();
        if (mColorModel == null) {
            mColorModel = mRealm.createObject(ColorModel.class);
        } else {
            red = mColorModel.getRed();
            green = mColorModel.getGreen();
            blue = mColorModel.getBlue();
        }
    }

    private void changeColor() {
        mBtnClipResult.setText(String.format("#%02x%02x%02x", red, green, blue));
        mViewColor.setBackgroundColor(Color.rgb(red, green, blue));
    }

    @OnClick(R.id.btn_clip_result)
    public void copyColorValue() {
        mClipData = ClipData.newPlainText("clip", mBtnClipResult.getText().toString());

        Toast.makeText(this, "颜色" + mBtnClipResult.getText().toString() + "已复制到剪贴板", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.seekBar_red:
                red = progress;
//                mTvRedTip.setX(mSeekBarRed.getPaddingLeft() + mSeekBarRed.getThumbOffset());
//                mTvRedTip.setText(red);
                break;

            case R.id.seekBar_green:
                green = progress;
//                mTvGreenTip.setX(mSeekBarGreen.getPaddingLeft() + mSeekBarGreen.getThumbOffset());
//                mTvGreenTip.setText(green);
                break;

            case R.id.seekBar_blue:
                blue = progress;
//                mTvBlueTip.setX(mSeekBarBlue.getPaddingLeft() + mSeekBarBlue.getThumbOffset());
//                mTvBlueTip.setText(blue);
                break;

            default:
                break;
        }

        changeColor();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mColorModel.setRed(red);
        mColorModel.setGreen(green);
        mColorModel.setBlue(blue);
        mRealm.copyToRealmOrUpdate(mColorModel);
        mRealm.commitTransaction();
    }

}
