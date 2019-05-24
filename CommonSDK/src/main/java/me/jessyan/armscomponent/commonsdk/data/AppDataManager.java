package me.jessyan.armscomponent.commonsdk.data;


import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import me.jessyan.armscomponent.commonsdk.bean.Historyrecord.HistoryData;
import me.jessyan.armscomponent.commonsdk.data.db.DataManager;
import me.jessyan.armscomponent.commonsdk.data.db.DbHelper;
import me.jessyan.armscomponent.commonsdk.data.http.HttpHelper;
import me.jessyan.armscomponent.commonsdk.data.prefs.PreferenceHelper;

/**
 * @author flymegoc
 * @date 2017/11/22
 * @describe
 */

@Singleton
public class AppDataManager implements DataManager {

    private DbHelper mDbHelper;
    private HttpHelper mHttpHelper;
    private PreferenceHelper mPreferencesHelper;

    @Inject
    public AppDataManager(DbHelper mDbHelper, PreferenceHelper mPreferencesHelper, HttpHelper mHttpHelper) {
        this.mDbHelper = mDbHelper;
        this.mHttpHelper = mHttpHelper;
        this.mPreferencesHelper = mPreferencesHelper;
    }

    public PreferenceHelper getSharedPreferences(){
        return mPreferencesHelper;
    }


    @Override
    public List<HistoryData> addHistoryData(String data) {
        return mDbHelper.addHistoryData(data);
    }

    @Override
    public void clearHistoryData() {
         mDbHelper.clearHistoryData();
    }

    @Override
    public List<HistoryData> loadAllHistoryData() {
        return mDbHelper.loadAllHistoryData();
    }


    @Override
    public boolean getNightModeState() {
        return  mPreferencesHelper.getNightModeState();
    }

    @Override
    public void putBoolean(String key, boolean defValue) {
        mPreferencesHelper.putBoolean(key,defValue);
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        return   mPreferencesHelper.getBoolean(key,defValue);
    }

    @Override
    public void putString(String key, String defValue) {
        mPreferencesHelper.putString(key,defValue);
    }

    @Override
    public String getString(String key) {
        return mPreferencesHelper.getString(key);
    }

    @Override
    public void setNightModeState(boolean b) {
        mPreferencesHelper .setNightModeState(b);
    }

    @Override
    public Observable<String> testPigAvAddress(String url) {
        return null;
    }
}
