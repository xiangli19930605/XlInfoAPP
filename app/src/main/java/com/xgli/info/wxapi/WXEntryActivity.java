//package com.xgli.info.wxapi;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.util.Log;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonParser;
//import com.tencent.mm.sdk.modelbase.BaseReq;
//import com.tencent.mm.sdk.modelbase.BaseResp;
//import com.tencent.mm.sdk.modelmsg.SendAuth;
//import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
//import com.wutohome.wutohome.Constants;
//import com.wutohome.wutohome.MainActivity;
//import com.wutohome.wutohome.MyApplication;
//import com.wutohome.wutohome.UI.base.BaseActivity;
//import com.wutohome.wutohome.Utils.Intents;
//import com.wutohome.wutohome.Utils.LogUtil;
//import com.wutohome.wutohome.Utils.MobileInfoUtil;
//import com.wutohome.wutohome.Utils.StringUtils;
//import com.wutohome.wutohome.Utils.ToastUtil;
//import com.wutohome.wutohome.Utils.UserUtils;
//import com.wutohome.wutohome.bean.MobBaseEntity;
//import com.wutohome.wutohome.bean.UserInfo;
//import com.wutohome.wutohome.bean.WeChat.Access_token;
//import com.wutohome.wutohome.bean.WeChat.UserMesg;
//import com.wutohome.wutohome.http.MobApi;
//import com.wutohome.wutohome.http.MyCallBack;
//
//import net.tsz.afinal.http.AjaxCallBack;
//import net.tsz.afinal.http.AjaxParams;
//
//
//import org.json.JSONObject;
//
//import java.util.List;
//
//import static com.wutohome.wutohome.Utils.GsonTools.changeGsonToBean;
//
//public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {
//    private static final int RETURN_MSG_TYPE_LOGIN = 1;
//    private static final int RETURN_MSG_TYPE_SHARE = 2;
//    public static final String APP_ID = "wx3ff99700d5cc0578";
//    public static final String APP_SECRET = "818344405fec1920c064c8f8f6cebfba";
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        //如果没回调onResp，八成是这句没有写
//        MyApplication.mWxApi.handleIntent(getIntent(), this);
//    }
//
//    // 微信发送请求到第三方应用时，会回调到该方法
//    @Override
//    public void onReq(BaseReq req) {
//    }
//
//    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
//    //app发送消息给微信，处理返回消息的回调
//    @Override
//    public void onResp(BaseResp resp) {
//        LogUtil.E(resp.errStr);
//        LogUtil.E("错误码 : " + resp.errCode + "");
//        switch (resp.errCode) {
//
//            case BaseResp.ErrCode.ERR_AUTH_DENIED:
//            case BaseResp.ErrCode.ERR_USER_CANCEL:
//                if (RETURN_MSG_TYPE_SHARE == resp.getType()) ToastUtil.showToast(this, "分享失败");
//                else ToastUtil.showToast(this, "登录失败");
//                break;
//            case BaseResp.ErrCode.ERR_OK:
//                switch (resp.getType()) {
//                    case RETURN_MSG_TYPE_LOGIN:
//                        //拿到了微信返回的code,立马再去请求access_token
//                        String code = ((SendAuth.Resp) resp).code;
//                        LogUtil.E("code = " + code);
//                        //就在这个地方，用网络库什么的或者自己封的网络api，发请求去咯，注意是get请求
////                        getAccess_token(code);
////                        finish();
//                        WxLogin(code);
//                        break;
//                    case RETURN_MSG_TYPE_SHARE:
//                        ToastUtil.showToast(this, "微信分享成功");
////                        finish();
//                        break;
//                }
//                break;
//        }
//        finish();
//    }
//
//    private void WxLogin(String code) {
//        showProgressDialog("登录中！");
//        MobApi.wxLogin(code, MobileInfoUtil.getIMEI(this), 0x001, new MyCallBack() {
//            @Override
//            public void onSuccess(int what, Object result) {
//                dissmissProgressDialog();
//                showProgressSuccess("登录成功!");
//                UserInfo info = (UserInfo) result;
//                UserUtils.saveUserCache(info);
//                Intents.getIntents().Intent(WXEntryActivity.this, MainActivity.class);
//
//            }
//
//            @Override
//            public void onSuccessList(int what, List results) {
//
//            }
//
//            @Override
//            public void onFail(int what, String result) {
//                dissmissProgressDialog();
//                showStringToastMsg(result);
//            }
//        });
//
//    }
//
//
//    /**
//     * 2.用code.调用Wx接口拿到 openid & accessToken
//     */
//
//    private void getAccess_token(final String code) {
//
//        MobApi.getAccess_token(APP_ID, APP_SECRET, code, 0x006, new MyCallBack() {
//            @Override
//            public void onSuccess(int what, Object result) {
//                Access_token access_token = (Access_token) result;
//                getUserMesg(access_token.getAccess_token(), access_token.getOpenid());
//            }
//
//            @Override
//            public void onSuccessList(int what, List results) {
//
//            }
//
//            @Override
//            public void onFail(int what, String result) {
//
//            }
//        });
//
//
//    }
//
//
//    /**
//     * 获取微信的个人信息
//     *
//     * @param access_token
//     * @param openid
//     */
//    private void getUserMesg(final String access_token, final String openid) {
//
//        MobApi.getUserMesg(access_token, openid, 0x006, new MyCallBack() {
//            @Override
//            public void onSuccess(int what, Object result) {
//                UserMesg access_token = (UserMesg) result;
//                Log.e("access_token", "" + access_token.getNickname());
//                Intents.getIntents().Intent(WXEntryActivity.this, MainActivity.class);
//                finish();
//            }
//
//            @Override
//            public void onSuccessList(int what, List results) {
//
//            }
//
//            @Override
//            public void onFail(int what, String result) {
//
//            }
//        });
//
//
//    }
//
//
//}
//
