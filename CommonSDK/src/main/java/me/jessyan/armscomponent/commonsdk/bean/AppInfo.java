package me.jessyan.armscomponent.commonsdk.bean;


import lombok.Getter;
import lombok.Setter;
import me.jessyan.armscomponent.commonsdk.base.bean.BaseBean;

/**
 * Describe：
 * Created by 吴天强 on 2018/11/20.
 */

@Getter
@Setter
public class AppInfo extends BaseBean {

    private int id;
    private int versionCode;
    private String versionName;
    private String describe;
    private String path;

    @Override
    public String toString() {
        return "AppInfo{" +
                "id=" + id +
                ", versionCode=" + versionCode +
                ", versionName='" + versionName + '\'' +
                ", describe='" + describe + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
