package me.jessyan.armscomponent.commonsdk.data;

/**
 * 项目名称:    HappyChat
 * 创建人:        陈锦军
 * 创建时间:    2016/9/11      17:31
 * QQ:             1981367757
 */


import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.PushListener;
import cn.bmob.v3.listener.UpdateListener;
import io.reactivex.disposables.Disposable;
import me.jessyan.armscomponent.commonsdk.app.MyApplication;
import me.jessyan.armscomponent.commonsdk.bean.CustomInstallation;
import me.jessyan.armscomponent.commonsdk.bean.User;
import me.jessyan.armscomponent.commonsdk.bean.UserEntity;
import me.jessyan.armscomponent.commonsdk.core.Constants;
import me.jessyan.armscomponent.commonsdk.data.prefs.PreferenceHelper;
import me.jessyan.armscomponent.commonsdk.greendao.UserEntityDao;
import me.jessyan.armscomponent.commonsdk.utils.DateUtils;
import me.jessyan.autosize.utils.LogUtils;

/**
 * 用户管理类
 * 与用户操作有关的操作类：登录、退出、获取好友列表、获取当前的登录用户,删除好友，添加好友等
 */
public class UserManager {

    //        用于同步
    private static final Object INSTANCE_LOCK = new Object();
    private static UserManager INSTANCE;
    private String uid;

    public static UserManager getInstance() {
        if (INSTANCE == null) {
            synchronized (INSTANCE_LOCK) {
                if (INSTANCE == null) {
                    INSTANCE = new UserManager();
                }
            }
        }
        return INSTANCE;
    }


    /**
     * 如果没有自定义用户实体类extend BmobUser
     * 默认返回BmobUser类型的用户实体
     * 获取当前用户
     *
     * @return 当前用户
     */
    public static User getCurrentUser() {
        return BmobUser.getCurrentUser(User.class);
    }






    /**
     * 获取本用户ID
     *
     * @return 用户ID
     */
    public String getCurrentUserObjectId() {
        if (uid == null) {
            User user = getCurrentUser();
            if (user != null) {
                uid = user.getObjectId();
            }
        }
        return uid;
    }
    /**
     * 根据用户名查询用户
     *
     * @param name     根据用户名在服务器上查询用户
     * @param listener 回调
     */
    public Disposable queryUsers(String name, FindListener<User> listener) {
        BmobQuery<User> eq1 = new BmobQuery<>();
        eq1.addWhereEqualTo("username", name);
        BmobQuery<User> eq2 = new BmobQuery<>();
        eq2.addWhereEqualTo("name", name);
        List<BmobQuery<User>> queries = new ArrayList<>();
        queries.add(eq1);
        queries.add(eq2);
        BmobQuery<User> mainQuery = new BmobQuery<>();
        mainQuery.or(queries);
        return mainQuery.findObjects(listener);
    }


   



    /**
     * 根据用户ID获取用户信息
     *
     * @param uid          用户ID
     * @param findListener 回调
     */
    public Disposable findUserById(String uid, FindListener<User> findListener) {
        if (uid.equals(UserManager.getInstance().getCurrentUserObjectId())) {
            findListener.done(Collections.singletonList(UserManager.getInstance().getCurrentUser()), null);
            return null;
        }
        UserEntity userEntity = UserDBManager.getInstance().getUser(uid);
        if (userEntity == null) {
            BmobQuery<User> query = new BmobQuery<>();
            query.addWhereEqualTo("objectId", uid);
            return query.findObjects(new FindListener<User>() {
                @Override
                public void done(List<User> list, BmobException e) {
                    if (e == null) {
                        if (list != null && list.size() > 0) {
                            UserDBManager.getInstance()
                                    .addOrUpdateUser(cover(list.get(0), true));
                        }
                    }
                    if (findListener != null) {
                        findListener.done(list, e);
                    }
                }
            });
        } else {
            List<User> list1 = new ArrayList<>(1);
            list1.add(cover(userEntity));
            if (findListener != null) {
                findListener.done(list1, null);
            }
            return null;
        }
    }






    private void deleteBlackRelation(String uid, UpdateListener listener) {
        User currentUser = new User();
        BmobRelation relation = new BmobRelation();
        User friend = new User();
        friend.setObjectId(uid);
        relation.remove(friend);
        currentUser.setAddBlack(relation);
        currentUser.setObjectId(getCurrentUserObjectId());
        currentUser.update(listener);
    }

    private void bindInstallation(final UpdateListener listener) {
        BmobQuery<CustomInstallation> query = new BmobQuery<>();
        query.addWhereEqualTo("installationId", new CustomInstallation().getInstallationId());
        query.findObjects(new FindListener<CustomInstallation>() {
            @Override
            public void done(final List<CustomInstallation> list, BmobException e) {
                if (e == null) {
                    if (list != null && list.size() > 0) {
                        CustomInstallation installation = list.get(0);
                        installation.setUid(UserManager.getInstance().getCurrentUserObjectId());
                        installation.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    LogUtils.e("绑定设备表中UID成功");
                                } else {
                                    LogUtils.e("绑定设备表中UID设备失败");
                                }
                                listener.done(e);
                            }
                        });
                    }
                } else {
                    LogUtils.e("在服务器上查询设备失败" + e.toString());
                    listener.done(e);
                }
            }

        });
    }

    /**
     * 检查本设备表中的uid   ，如果有，就发送下线通知，操作成功后，再把uid更新到本地的设备表中
     */
    public Disposable checkInstallation(final UpdateListener listener) {
        BmobQuery<CustomInstallation> query = new BmobQuery<>();
        LogUtils.e("checkInstallation UID：" + getCurrentUserObjectId());
        query.addWhereEqualTo("uid", getCurrentUserObjectId());
        query.order("-updatedAt");
        return query.findObjects(new FindListener<CustomInstallation>() {
                                     @Override
                                     public void done(List<CustomInstallation> list, BmobException e) {
                                         if (e == null) {
                                             if (list != null && list.size() > 0) {
                                                 CustomInstallation customInstallation = list.get(0);
                                                 if (customInstallation.getInstallationId().equals(new CustomInstallation().getInstallationId())) {
                                                     LogUtils.e("由于绑定的是本设备表，不做操作所以");
                                                     listener.done(null);
                                                 } else {
                                                     //                                                        不管推送成功与否，都要更新设备表的UID
//                                                     MsgManager.getInstance().sendOfflineNotificationMsg(customInstallation, new PushListener() {
//                                                                 @Override
//                                                                 public void done(BmobException e) {
//                                                                     if (e == null) {
//                                                                         LogUtils.e("推送下线通知消息成功");
//                                                                     } else {
//                                                                         LogUtils.e("推送下线通知消息失败" + e.toString());
//
//                                                                     }
//                                                                     bindInstallation(listener);
//                                                                 }
//                                                             }
//                                                     );
                                                 }
                                             } else {
                                                 //                                          LogUtils.e("查询不到本用户所对应的设备ID,这里新建一个设备表");
                                                 bindInstallation(listener);
                                             }
                                         } else {
                                             LogUtils.e("查询本用户对应的设备表出错" + e.toString());
                                             listener.done(e);
                                         }
                                     }
                                 }
        );
    }

    public void updateUserInfo(final String name, final String content, final UpdateListener listener) {
        User user = new User();
        user.setObjectId(UserManager.getInstance().getCurrentUserObjectId());
        switch (name) {
            case Constants.PHONE:
                user.setMobilePhoneNumber(content);
                break;
            case Constants.EMAIL:
                user.setEmail(content);
                break;
            case Constants.NICK:
                user.setNick(content);
                break;
            case Constants.AVATAR:
                user.setAvatar(content);
                break;
            case Constants.GENDER:
                if (content.equals("男")) {
                    user.setSex(true);
                } else {
                    user.setSex(false);
                }
                break;
            case Constants.SIGNATURE:
                user.setSignature(content);
                break;
            case Constants.BIRTHDAY:
                user.setBirthDay(content);
                break;
            case Constants.ADDRESS:
                user.setAddress(content);
                break;
            case Constants.LOCATION:
                LogUtils.e("定位location" + content);
                String result[] = content.split("&");
                user.setLocation(new BmobGeoPoint(Double.parseDouble(result[0]), Double.parseDouble(result[1])));
                break;
            case Constants.TITLE_WALLPAPER:
                user.setTitleWallPaper(content);
                break;
            case Constants.WALLPAPER:
                user.setWallPaper(content);
                break;
            case Constants.INSTALL_ID:
                user.setInstallId(content);
                break;
            case Constants.NAME:
                user.setName(content);
                break;
        }
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    LogUtils.e("用户信息更新成功");
                } else {
                    LogUtils.e("用户信息更新失败" + e.toString());
                }
                if (listener != null) {
                    listener.done(e);
                }
            }
        });
    }

    public void queryNearbyPeople(int num, int flag, FindListener<User> findListener) {
        User currentUser = getCurrentUser();
        BmobQuery<User> query = new BmobQuery<>();
        if (flag == 1) {
            query.addWhereEqualTo("sex", false);
        } else if (flag == 2) {
            query.addWhereEqualTo("sex", true);
        }
        double longitude;
        double latitude;
        if (currentUser.getLocation() != null) {
            longitude = currentUser.getLocation().getLongitude();
            latitude = currentUser.getLocation().getLatitude();
        } else {
            PreferenceHelper preferenceHelper=MyApplication.mComponent
                    .getAppDataManager().getSharedPreferences();
            if (preferenceHelper.getString(Constants.LONGITUDE) == null) {
                findListener.done(null, new BmobException("定位信息为空!!!!"));
                return;
            }
            longitude = Double.parseDouble(preferenceHelper.getString(Constants.LONGITUDE));
            latitude = Double.parseDouble(preferenceHelper.getString(Constants.LATITUDE));
            updateUserInfo(Constants.LOCATION, longitude + "&" + latitude, null);
        }
        query.addWhereNear("location", new BmobGeoPoint(longitude, latitude));
        query.addWhereNotEqualTo("objectId", currentUser.getObjectId());
        query.setSkip(num);
        query.setLimit(10);
        query.findObjects(findListener);
    }


//    public void refreshUserInfo() {
//        List<String> userList = UserDBManager.getInstance().getAllFriendId();
//        String currentUserObjectId = getCurrentUserObjectId();
//        if (userList != null && userList.size() > 0) {
//            for (final String uid :
//                    userList) {
//                BmobQuery<User> query = new BmobQuery<>();
//
//                String lastTime = MyApplication.mComponent
//                        .getSharedPreferences().getString(currentUserObjectId + "&" + uid, null);
//                //                                        第一次断网查询用户数据
//                query.addWhereGreaterThan("updatedAt", new BmobDate(new Date(DateUtils.getTime(lastTime))));
//                query.addWhereEqualTo("objectId", uid);
//                query.findObjects(new FindListener<User>() {
//                    @Override
//                    public void done(List<User> list, BmobException e) {
//                        if (e == null) {
//                            if (list != null && list.size() > 0) {
//                                User user = list.get(0);
//                                MyApplication.mComponent
//                                        .getPreferenceHelper()
//                                        .edit().putString(currentUserObjectId + "&" + user.getObjectId(), user.getUpdatedAt())
//                                        .apply();
//                                UserDBManager.getInstance().addOrUpdateUser(user);
//                            }
//                        } else {
//                            LogUtils.e("断网期间内查询用户失败" + e.toString());
//                        }
//                    }
//                });
//            }
//        }
//    }


    public UserEntity cover(User currentUser, boolean isStranger) {
        return cover(currentUser, isStranger, false, UserEntity.BLACK_TYPE_NORMAL);
    }

    public UserEntity cover(User currentUser, boolean isStranger, boolean isBlack, int blackType) {
        UserEntity userEntity = new UserEntity();
        userEntity.setIsStranger(isStranger);
        userEntity.setTitlePaper(currentUser.getTitleWallPaper());
        userEntity.setUpdatedTime(currentUser.getUpdatedAt());
        userEntity.setCreatedTime(currentUser.getCreatedAt());
        userEntity.setSex(currentUser.isSex());
        userEntity.setAvatar(currentUser.getAvatar());
        userEntity.setNick(currentUser.getNick());
        userEntity.setUid(currentUser.getObjectId());
        userEntity.setAddress(currentUser.getAddress());
        userEntity.setPhone(currentUser.getMobilePhoneNumber());
        userEntity.setEmail(currentUser.getEmail());
        userEntity.setBirthDay(currentUser.getBirthDay());
        userEntity.setBlack(isBlack);
        userEntity.setBlackType(blackType);
        userEntity.setClassNumber(currentUser.getClassNumber());
        userEntity.setCollege(currentUser.getCollege());
        userEntity.setEducation(currentUser.getEducation());
        userEntity.setMajor(currentUser.getMajor());
        userEntity.setName(currentUser.getName());
        userEntity.setUserName(currentUser.getUsername());
        userEntity.setSchool(currentUser.getSchool());
        userEntity.setSignature(currentUser.getSignature());
        userEntity.setYear(currentUser.getYear());
        return userEntity;
    }


    public UserEntity cover(User user) {
        return cover(user, UserDBManager.getInstance().isStranger(user.getObjectId()));
    }


    public User cover(UserEntity userEntity) {
        User user = new User();
        user.setSex(userEntity.isSex());
        user.setAvatar(userEntity.getAvatar());
        user.setTitleWallPaper(userEntity.getTitlePaper());
        user.setCreatedTime(userEntity.getCreatedTime());
        user.setUpdatedAt(userEntity.getUpdatedTime());
        user.setNick(userEntity.getNick());
        user.setObjectId(userEntity.getUid());
        user.setMobilePhoneNumber(userEntity.getPhone());
        user.setEmail(userEntity.getEmail());
        user.setAddress(userEntity.getAddress());
        user.setBirthDay(userEntity.getBirthDay());
        user.setName(userEntity.getName());
        user.setUsername(userEntity.getUserName());
        user.setSchool(userEntity.getSchool());
        user.setCollege(userEntity.getCollege());
        user.setMajor(userEntity.getMajor());
        user.setEducation(userEntity.getEducation());
        user.setYear(userEntity.getYear());
        user.setClassNumber(userEntity.getClassNumber());
        user.setSignature(userEntity.getSignature());
        user.setCreatedTime(userEntity.getCreatedTime());
        user.setUpdatedAt(userEntity.getUpdatedTime());
        return user;
    }

}
