package me.jessyan.armscomponent.commonsdk.base.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jess.arms.di.component.AppComponent;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.filter.Filter;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhihu.matisse.listener.OnCheckedListener;
import com.zhihu.matisse.listener.OnSelectedListener;


import java.util.List;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;
import cn.bingoogolapple.qrcode.zxing.ZXingView;
import me.jessyan.armscomponent.commonsdk.R;
import me.jessyan.armscomponent.commonsdk.app.GlideImageLoader;

/**
 * Describe：带下拉刷新 上拉加载更多的Activity
 * 内部实现为刷新控件 PullToRefreshLayout + 列表控件 RecyclerView
 * Created by 吴天强 on 2018/10/23.
 */
public class ScanActivity extends BaseActivity implements QRCodeView.Delegate, View.OnClickListener {
    private static final String TAG = ScanActivity.class.getSimpleName();
    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;
    public static final int REQUEST_CODE_SUCCESS = 01;
    public static final String RESULT = "";

    private ZXingView mZXingView;
    TextView tv_photo;
    CheckBox tv_light;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {

    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_scan;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        tv_photo = findViewById(R.id.tv_photo);
        tv_photo.setOnClickListener(this);
        tv_light = findViewById(R.id.tv_light);
        tv_light.setOnClickListener(this);
        tv_light.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    if (mZXingView != null) {
                        mZXingView.openFlashlight(); // 打开闪光灯
                    }
                } else {
                    if (mZXingView != null) {
                        mZXingView.closeFlashlight(); // 打开闪光灯
                    }
                }
            }
        });
        mZXingView = findViewById(R.id.zxingview);
        mZXingView.setDelegate(this);
//        initImagePicker();
    }


//    ImagePicker imagePicker;
//
//    private void initImagePicker() {
//        imagePicker = ImagePicker.getInstance();
//        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
//        imagePicker.setShowCamera(true);                      //显示拍照按钮
//        imagePicker.setCrop(true);                           //允许裁剪（单选才有效）
//        imagePicker.setMultiMode(true);                        //是否单选
//        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
//        imagePicker.setSelectLimit(1);              //选中数量限制
//        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
//        imagePicker.setFocusWidth(750);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
//        imagePicker.setFocusHeight(750);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
//        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
//        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
//    }

    @Override
    protected void onStart() {
        super.onStart();
        mZXingView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
//        mZXingView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT); // 打开前置摄像头开始预览，但是并未开始识别

        mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
    }

    @Override
    protected void onStop() {
        mZXingView.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mZXingView.onDestroy(); // 销毁二维码扫描控件
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        Log.i(TAG, "result:" + result);
        vibrate();
        Intent dataIntent = new Intent();
        dataIntent.putExtra(RESULT, result);
        setResult(RESULT_OK, dataIntent);
        finish();
        mZXingView.startSpot(); // 开始识别
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        // 这里是通过修改提示文案来展示环境是否过暗的状态，接入方也可以根据 isDark 的值来实现其他交互效果
        String tipText = mZXingView.getScanBoxView().getTipText();
        String ambientBrightnessTip = "\n环境过暗，请打开闪光灯";
        if (isDark) {
            if (!tipText.contains(ambientBrightnessTip)) {
                mZXingView.getScanBoxView().setTipText(tipText + ambientBrightnessTip);
            }
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip));
                mZXingView.getScanBoxView().setTipText(tipText);
            }
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e(TAG, "打开相机出错");
    }

    public void onClick(View v) {
        //            case R.id.start_preview:
        //                mZXingView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
        //                break;
        //            case R.id.stop_preview:
        //                mZXingView.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
        //                break;
        //            case R.id.start_spot:
        //                mZXingView.startSpot(); // 开始识别
        //                break;
        //            case R.id.stop_spot:
        //                mZXingView.stopSpot(); // 停止识别
        //                break;
        //            case R.id.start_spot_showrect:
        //                mZXingView.startSpotAndShowRect(); // 显示扫描框，并且开始识别
        //                break;
        //            case R.id.stop_spot_hiddenrect:
        //                mZXingView.stopSpotAndHiddenRect(); // 停止识别，并且隐藏扫描框
        //                break;
        //            case R.id.show_scan_rect:
        //                mZXingView.showScanRect(); // 显示扫描框
        //                break;
        //            case R.id.hidden_scan_rect:
        //                mZXingView.hiddenScanRect(); // 隐藏扫描框
        //                break;
        //            case R.id.decode_scan_box_area:
        //                mZXingView.getScanBoxView().setOnlyDecodeScanBoxArea(true); // 仅识别扫描框中的码
        //                break;
        //            case R.id.decode_full_screen_area:
        //                mZXingView.getScanBoxView().setOnlyDecodeScanBoxArea(false); // 识别整个屏幕中的码
        //                break;
//                    case R.id.open_flashlight:
//                        mZXingView.openFlashlight(); // 打开闪光灯
//                        break;
//                    case R.id.close_flashlight:
//                        mZXingView.closeFlashlight(); // 关闭闪光灯
//                        break;
        //            case R.id.scan_one_dimension:
        //                mZXingView.changeToScanBarcodeStyle(); // 切换成扫描条码样式
        //                mZXingView.setType(BarcodeType.ONE_DIMENSION, null); // 只识别一维条码
        //                mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
        //                break;
        //            case R.id.scan_two_dimension:
        //                mZXingView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
        //                mZXingView.setType(BarcodeType.TWO_DIMENSION, null); // 只识别二维条码
        //                mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
        //                break;
        //            case R.id.scan_qr_code:
        //                mZXingView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
        //                mZXingView.setType(BarcodeType.ONLY_QR_CODE, null); // 只识别 QR_CODE
        //                mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
        //                break;
        //            case R.id.scan_code128:
        //                mZXingView.changeToScanBarcodeStyle(); // 切换成扫描条码样式
        //                mZXingView.setType(BarcodeType.ONLY_CODE_128, null); // 只识别 CODE_128
        //                mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
        //                break;
        //            case R.id.scan_ean13:
        //                mZXingView.changeToScanBarcodeStyle(); // 切换成扫描条码样式
        //                mZXingView.setType(BarcodeType.ONLY_EAN_13, null); // 只识别 EAN_13
        //                mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
        //                break;
        //            case R.id.scan_high_frequency:
        //                mZXingView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
        //                mZXingView.setType(BarcodeType.HIGH_FREQUENCY, null); // 只识别高频率格式，包括 QR_CODE、UPC_A、EAN_13、CODE_128
        //                mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
        //                break;
        //            case R.id.scan_all:
        //                mZXingView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
        //                mZXingView.setType(BarcodeType.ALL, null); // 识别所有类型的码
        //                mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
        //                break;
        //            case R.id.scan_custom:
        //                mZXingView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
        //
        //                Map<DecodeHintType, Object> hintMap = new EnumMap<>(DecodeHintType.class);
        //                List<BarcodeFormat> formatList = new ArrayList<>();
        //                formatList.add(BarcodeFormat.QR_CODE);
        //                formatList.add(BarcodeFormat.UPC_A);
        //                formatList.add(BarcodeFormat.EAN_13);
        //                formatList.add(BarcodeFormat.CODE_128);
        //                hintMap.put(DecodeHintType.POSSIBLE_FORMATS, formatList); // 可能的编码格式
        //                hintMap.put(DecodeHintType.TRY_HARDER, Boolean.TRUE); // 花更多的时间用于寻找图上的编码，优化准确性，但不优化速度
        //                hintMap.put(DecodeHintType.CHARACTER_SET, "utf-8"); // 编码字符集
        //                mZXingView.setType(BarcodeType.CUSTOM, hintMap); // 自定义识别的类型
        //
        //                mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
        //                break;
        if (v.getId() == R.id.tv_photo) {

            Matisse.from(this)
                    .choose(MimeType.ofAll(), false)
                    .countable(true)
                    .capture(true)
                    .captureStrategy(
                            new CaptureStrategy(true, "me.jessyan.armscomponent.commonsdk.fileprovider","test"))
                    .maxSelectable(1)
                    .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
                    .gridExpectedSize(
                            getResources().getDimensionPixelSize(R.dimen.dp_120))
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                    .thumbnailScale(0.85f)
//                                            .imageEngine(new GlideEngine())  // for glide-V3
                    .imageEngine(new Glide4Engine())    // for glide-V4
                    .setOnSelectedListener(new OnSelectedListener() {
                        @Override
                        public void onSelected(
                                @NonNull List<Uri> uriList, @NonNull List<String> pathList) {
                            // DO SOMETHING IMMEDIATELY HERE
                            Log.e("onSelected", "onSelected: pathList=" + pathList);

                        }
                    })
                    .originalEnable(true)
                    .maxOriginalSize(10)
                    .autoHideToolbarOnSingleTap(true)
                    .setOnCheckedListener(new OnCheckedListener() {
                        @Override
                        public void onCheck(boolean isChecked) {
                            // DO SOMETHING IMMEDIATELY HERE
                            Log.e("isChecked", "onCheck: isChecked=" + isChecked);
                        }
                    })
                    .forResult(REQUEST_CODE_SELECT);
//            startActivityForResult(intent1, REQUEST_CODE_SELECT);
        } else if (v.getId() == R.id.tv_light) {

        }
    }

    public static final int REQUEST_CODE_SELECT = 100;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mZXingView.startSpotAndShowRect(); // 显示扫描框，并开始识别
//        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
//            if (data != null && requestCode == REQUEST_CODE_SELECT) {
//                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
//                final String picturePath = images.get(0).path;
//                new AsyncTask<Void, Void, String>() {
//                    @Override
//                    protected String doInBackground(Void... params) {
//                        return QRCodeDecoder.syncDecodeQRCode(picturePath);
//                    }
//
//                    @Override
//                    protected void onPostExecute(String result) {
//                        if (TextUtils.isEmpty(result)) {
//                            Toast.makeText(ScanActivity.this, "未发现二维码", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(ScanActivity.this, result, Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }.execute();
//
//            } else {
//                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
//            }
//        }
        if (requestCode == REQUEST_CODE_SELECT && resultCode == RESULT_OK) {
            List<String> mSelected;
            mSelected = Matisse.obtainPathResult(data);
            new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... params) {
                        return QRCodeDecoder.syncDecodeQRCode(mSelected.get(0));
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        if (TextUtils.isEmpty(result)) {
                            Toast.makeText(ScanActivity.this, "未发现二维码", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent dataIntent = new Intent();
                            dataIntent.putExtra(RESULT, result);
                            setResult(RESULT_OK, dataIntent);
                            finish();
//                            Toast.makeText(ScanActivity.this, result, Toast.LENGTH_SHORT).show();
                        }
                    }
                }.execute();

            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }


        }
//    }


}