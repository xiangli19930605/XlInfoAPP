package me.jessyan.armscomponent.commonsdk.bean;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import me.jessyan.autosize.utils.LogUtils;

/**
 * 项目名称:    TestChat
 * 创建人:        陈锦军
 * 创建时间:    2016/12/28      21:38
 * QQ:             1981367757
 */

public class RandomData {
    private static List<String> nickList = new ArrayList<>();
    private static List<String> avatarList = new ArrayList<>();
    private static List<String> wallPaperList = new ArrayList<>();
    private static List<String> titleWallPaper = new ArrayList<>();
    private static List<String> collegeList = new ArrayList<>();
    private static List<String> nameList = new ArrayList<>();
    private static List<String> yearList = new ArrayList<>();
    private static List<String> majorList = new ArrayList<>();
    private static List<String> classList = new ArrayList<>();
    private static List<String> signatureList = new ArrayList<>();

    static {
        String a = "丽丽";
        String b = "小屁孩";
        String c = "娜娜";
        String d = "萌萌";
        String e = "梦梦";
        String f = "琪琪";
        nickList.add(a);
        nickList.add(b);
        nickList.add(c);
        nickList.add(d);
        nickList.add(e);
        nickList.add(f);
        collegeList.add("经济管理学院");
        collegeList.add("艺术与传媒学院");
        collegeList.add("马克思主义学院");
        collegeList.add("自动化学院");
        collegeList.add("计算机学院");
        collegeList.add("工程学院");
        collegeList.add("外国语学院");
        yearList.add("2013级");
        yearList.add("2014级");
        yearList.add("2015级");
        yearList.add("2016级");
        yearList.add("2017级");
        nameList.add("陈锦军");
        nameList.add("温油");
        nameList.add("刘梦雪");
        nameList.add("辛安琪");
        nameList.add("爱妃");
        nameList.add("刘斌");
        nameList.add("陈群莹");
        majorList.add("信息管理与信息系统");
        majorList.add("计算机科学技术");
        majorList.add("会计学");
        majorList.add("金融学");
        majorList.add("旅游管理");
        majorList.add("工商管理");
        majorList.add("信息安全");
        majorList.add("软件工程");
        classList.add("086141");
        classList.add("054863");
        classList.add("089653");
        classList.add("053246");
        classList.add("087546");
        classList.add("095642");
        signatureList.add("帅得被人砍");
        signatureList.add("丑得想整容");
        signatureList.add("和尚洗发用霸王");
    }


    public static String getRandomNick() {
        Random random = new Random();
        int index = random.nextInt(nickList.size());
        if (index >= 0 && index < nickList.size()) {
            return nickList.get(index);
        } else {
            return nickList.get(0);
        }
    }


    public static String getRandomSignature() {
        Random random = new Random();
        int index = random.nextInt(signatureList.size());
        if (index >= 0 && index < signatureList.size()) {
            return signatureList.get(index);
        } else {
            return signatureList.get(0);
        }
    }

 


   

    public static String getRandomAvatar() {
        if (avatarList != null && avatarList.size() > 0) {
            Random random = new Random();
            int index = random.nextInt(avatarList.size() - 1);
            LogUtils.e("大小:" + index);
            if (index >= 0 && index < avatarList.size()) {
                return avatarList.get(index);
            }
        } else {
            LogUtils.e("为空？？？？？？？");
        }
        return null;
    }


    public static String getRandomTitleWallPaper() {
        if (titleWallPaper != null && titleWallPaper.size() > 0) {
            Random random = new Random();
            int index = random.nextInt(titleWallPaper.size() - 1);
            LogUtils.e("大小:" + index);
            if (index >= 0 && index < titleWallPaper.size()) {
                return titleWallPaper.get(index);
            }
        } else {
            LogUtils.e("为空？？？？？？？");
        }
        return null;
    }

    public static String getRandomWallPaper() {
        if (wallPaperList != null && wallPaperList.size() > 0) {
            Random random = new Random();
            int index = random.nextInt(wallPaperList.size() - 1);
            LogUtils.e("大小:" + index);
            if (index >= 0 && index < wallPaperList.size()) {
                return wallPaperList.get(index);
            }
        } else {
            LogUtils.e("为空？？？？？？？");
        }
        return null;
    }

    public static String getRandomCollege() {
        Random random = new Random();
        int index = random.nextInt(collegeList.size());
        if (index >= 0 && index < collegeList.size()) {
            return collegeList.get(index);
        } else {
            return collegeList.get(0);
        }
    }


    public static String getRandomName() {
        Random random = new Random();
        StringBuilder name = new StringBuilder();
        int index = random.nextInt(nameList.size());
        if (index >= 0 && index < nameList.size()) {
            name.append(nameList.get(index));
        } else {
            name.append(nameList.get(0));
        }
        return name.append(random.nextInt(5000)).toString();
    }


    public static String getRandomEducation() {
        Random random = new Random();
        if (random.nextInt(10) % 2 == 0) {
            return "本科";
        } else {
            return "研究生";
        }
    }


    public static String getRandomYear() {
        Random random = new Random();
        int index = random.nextInt(yearList.size());
        if (index >= 0 && index < yearList.size()) {
            return yearList.get(index);
        } else {
            return yearList.get(0);
        }
    }


    public static String getRandomMajor() {
        Random random = new Random();
        int index = random.nextInt(majorList.size());
        if (index >= 0 && index < majorList.size()) {
            return majorList.get(index);
        } else {
            return majorList.get(0);
        }
    }

    public static String getRandomClassNumber() {
        Random random = new Random();
        int index = random.nextInt(classList.size());
        if (index >= 0 && index < classList.size()) {
            return classList.get(index);
        } else {
            return classList.get(0);
        }
    }

    public static boolean getRandomSex() {
        Random random = new Random();
        return random.nextInt(10)%2==0;
    }
}
