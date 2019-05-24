package com.xgli.info.app.manage;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.jess.arms.base.BaseApplication;
import com.xgli.info.app.LocationEvent;

import me.jessyan.armscomponent.commonsdk.app.MyApplication;
import me.jessyan.armscomponent.commonsdk.core.Constants;
import me.jessyan.armscomponent.commonsdk.data.UserManager;
import me.jessyan.armscomponent.commonsdk.rxbus.RxBusManager;
import me.jessyan.autosize.utils.LogUtils;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/3/25     21:46
 * 获取位置信息
 */

public class NewLocationManager implements AMapLocationListener {
    private static NewLocationManager instance;
    private AMapLocationClient mLocationClient = null;
    private double latitude;
    private double longitude;
    private String address;

    public static NewLocationManager getInstance() {
        if (instance == null) {
            instance = new NewLocationManager();
        }
        return instance;
    }


    public void startLocation() {
        if (mLocationClient == null) {
            LogUtils.e("这里新建定位");
            mLocationClient = new AMapLocationClient(MyApplication.getInstance());
            mLocationClient.setLocationOption(getDefaultOption());
            mLocationClient.setLocationListener(this);
            mLocationClient.startLocation();
        }
    }


    private NewLocationManager() {
    }

    private AMapLocationClientOption getDefaultOption() {
        LogUtils.e("获取定位选项");
        AMapLocationClientOption option = new AMapLocationClientOption();
//          定位模式：1 高精度、2仅设备、3仅网络
// 设置高精度模式
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
//                设置是否优先使用GPS
        option.setGpsFirst(false);
//                连接超时3秒
        option.setHttpTimeOut(3000);
//                设置定位间隔60秒
        option.setInterval(60000);
//                设置是否返回地址，默认返回
        option.setNeedAddress(true);
//                设置是否单次定位
        option.setOnceLocation(false);
        //可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        option.setOnceLocationLatest(false);
//                设置网络请求协议
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);
//                设置是否使用传感器,不使用
        option.setSensorEnable(false);
        return option;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                LogUtils.e("1获取到位置信息拉");
                //                获取纬度
                if (latitude != aMapLocation.getLatitude() || longitude != aMapLocation.getLongitude()) {
                    latitude = aMapLocation.getLatitude();
                    //                获取经度
                    longitude = aMapLocation.getLongitude();
                    address = aMapLocation.getAddress();
                    LogUtils.e(aMapLocation.toString());
//                                        aMapLocation.getLocationType();//获取当前定位结果来源
//                                        aMapLocation.getLatitude();//获取纬度
//                                        aMapLocation.getLongitude();//获取经度
//                                        aMapLocation.getAccuracy();//获取精度信息
//                                        aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
//                                        aMapLocation.getCountry();//国家信息
//                                        aMapLocation.getProvince();//省信息
//                                        aMapLocation.getCity();//城市信息
//                                        aMapLocation.getDistrict();//城区信息
//                                        aMapLocation.getStreet();//街道信息
//                                        aMapLocation.getStreetNum();//街道门牌号信息
//                                        aMapLocation.getCityCode();//城市编码
//                                        aMapLocation.getAdCode();//地区编码
//                                        aMapLocation.getAoiName();//获取当前定位点的AOI信息
                    LocationEvent locationEvent = new LocationEvent();
                    locationEvent.setLongitude(longitude);
                    locationEvent.setLatitude(latitude);
                    locationEvent.setLocation(address);
                    locationEvent.setCountry(aMapLocation.getCountry());
                    locationEvent.setProvince(aMapLocation.getProvince());
                    locationEvent.setCity(aMapLocation.getCity());
                    locationEvent.setTitle(aMapLocation.getPoiName());
                    RxBusManager.getInstance().post(locationEvent);
                    MyApplication.mComponent.getAppDataManager().putString(Constants.LATITUDE, latitude+"");
                    MyApplication.mComponent.getAppDataManager().putString(Constants.LONGITUDE, longitude+"");
                    MyApplication.mComponent.getAppDataManager().putString(Constants.ADDRESS, address+"");
                    MyApplication.mComponent.getAppDataManager().putString(Constants.CITY, aMapLocation.getCity());
                    if (UserManager.getInstance().getCurrentUser() != null) {
                        UserManager.getInstance().updateUserInfo(Constants.LOCATION,longitude+"&"+latitude,null);
                    }
                } else {
                    LogUtils.e("定位相同,不定位");
                }
            } else {
                LogUtils.e("出错消息：" + aMapLocation.getErrorInfo() + "\n" + "错误码:" + aMapLocation.getErrorCode() + "\n" + "错误的细节" +
                        aMapLocation.getLocationDetail());
            }
        }
    }


    public void clear() {
        LogUtils.e("这里清除定位");
        if (mLocationClient != null) {
            mLocationClient.onDestroy();
            mLocationClient = null;
        }
        longitude = 0;
        latitude = 0;
    }

}
