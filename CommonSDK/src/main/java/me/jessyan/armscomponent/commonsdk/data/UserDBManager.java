package me.jessyan.armscomponent.commonsdk.data;

import com.google.gson.Gson;
import org.greenrobot.greendao.database.Database;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.jessyan.armscomponent.commonsdk.app.MyApplication;
import me.jessyan.armscomponent.commonsdk.bean.UserEntity;
import me.jessyan.armscomponent.commonsdk.greendao.DaoMaster;
import me.jessyan.armscomponent.commonsdk.greendao.DaoSession;
import me.jessyan.armscomponent.commonsdk.greendao.UserEntityDao;

/**
 * 项目名称:    NewFastFrame
 * 创建人:      陈锦军
 * 创建时间:    2018/3/26     21:02
 * QQ:         1981367757
 */

public class UserDBManager {
    private static Map<String, UserDBManager> sMap = new HashMap<>();
    private DaoSession daoSession;
    private Gson gson;


    public static UserDBManager getInstance() {
        return getInstance(UserManager.getInstance().getCurrentUserObjectId());
    }


    public static UserDBManager getInstance(String uid) {
        if (uid == null) {
            throw new RuntimeException("创建数据库中用户ID不能为空!!!!!!!!!");
        }
        if (sMap.get(uid) == null) {
            synchronized (UserDBManager.class) {
                if (sMap.get(uid) == null) {
                    UserDBManager userDBManager = new UserDBManager(uid);
                    sMap.put(uid, userDBManager);
                }
            }
        }
        return sMap.get(uid);
    }


    public DaoSession getDaoSession() {
        return daoSession;
    }

    private UserDBManager(String uid) {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(MyApplication.getInstance(), uid, null);
        Database database = devOpenHelper.getWritableDb();
        DaoMaster master = new DaoMaster(database);
        daoSession = master.newSession();
//        gson = BaseApplication.getAppComponent().getGson();
    }



    public void updateUserInfo() {
        daoSession.getUserEntityDao().update(UserManager.getInstance().cover(UserManager.getInstance().getCurrentUser()));

    }
    public UserEntity getUser(String id) {
        if (id.equals(UserManager.getInstance().getCurrentUserObjectId())) {
            return UserManager.getInstance().cover(UserManager.getInstance().getCurrentUser());
        }
        List<UserEntity> list = daoSession.getUserEntityDao().queryBuilder().where(UserEntityDao.Properties
                .Uid.eq(id)).build().list();
        return list.size() == 0 ? null : list.get(0);

    }
    public boolean isStranger(String uid) {
        List<UserEntity> list = daoSession.getUserEntityDao().queryBuilder().where(UserEntityDao.Properties
                .Uid.eq(uid)).build().list();
        return list.size() == 0 || list.get(0).isStranger();
    }
    public void addOrUpdateUser(UserEntity userEntity) {
        daoSession.getUserEntityDao().insertOrReplace(userEntity);
    }

}
