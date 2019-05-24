package me.jessyan.armscomponent.commonsdk.data.db;

import android.database.sqlite.SQLiteDatabase;

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import me.jessyan.armscomponent.commonsdk.app.MyApplication;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.HistoryData;
import me.jessyan.armscomponent.commonsdk.greendao.DaoMaster;
import me.jessyan.armscomponent.commonsdk.greendao.DaoSession;
import me.jessyan.armscomponent.commonsdk.greendao.HistoryDataDao;


/**
 * 对外隐藏操作数据库的实现细节
 *
 * @author quchao
 * @date 2017/11/27
 */
@Singleton
public class DbHelperImpl implements DbHelper {

    private static final int HISTORY_LIST_SIZE = 10;

    private DaoSession daoSession;
    private List<HistoryData> historyDataList;
    private String data;
    private HistoryData historyData;

    @Inject
    DbHelperImpl() {
//        daoSession = MyApplication.getDaoSession();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(MyApplication.getInstance(), "common_library.db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    @Override
    public List<HistoryData> addHistoryData(String data) {
        this.data = data;
        getHistoryDataList();
        createHistoryData();
        if (historyDataForward()) {
            return historyDataList;
        }

        if (historyDataList.size() < HISTORY_LIST_SIZE) {
            getHistoryDataDao().insert(historyData);
        } else {
            historyDataList.remove(0);
            historyDataList.add(historyData);
            getHistoryDataDao().deleteAll();
            getHistoryDataDao().insertInTx(historyDataList);
        }
        return historyDataList;
    }

    @Override
    public void clearHistoryData() {
        daoSession.getHistoryDataDao().deleteAll();
    }

    @Override
    public List<HistoryData> loadAllHistoryData() {
        return daoSession.getHistoryDataDao().loadAll();
    }

    /**
     * 历史数据前移
     *
     * @return 返回true表示查询的数据已存在，只需将其前移到第一项历史记录，否则需要增加新的历史记录
     */
    private boolean historyDataForward() {
        //重复搜索时进行历史记录前移
        Iterator<HistoryData> iterator = historyDataList.iterator();
        //不要在foreach循环中进行元素的remove、add操作，使用Iterator模式
        while (iterator.hasNext()) {
            HistoryData historyData1 = iterator.next();
            if (historyData1.getData().equals(data)) {
                historyDataList.remove(historyData1);
                historyDataList.add(historyData);
                getHistoryDataDao().deleteAll();
                getHistoryDataDao().insertInTx(historyDataList);
                return true;
            }
        }
        return false;
    }

    private void getHistoryDataList() {
        historyDataList = getHistoryDataDao().loadAll();
    }

    private void createHistoryData() {
        historyData = new HistoryData();
        historyData.setDate(System.currentTimeMillis());
        historyData.setData(data);
    }

    private HistoryDataDao getHistoryDataDao() {
        return daoSession.getHistoryDataDao();
    }

}
