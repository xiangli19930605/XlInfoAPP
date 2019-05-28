package me.jessyan.armscomponent.commonsdk.app;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.youth.banner.loader.ImageLoader;

import java.io.File;

import me.jessyan.armscomponent.commonsdk.R;

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide.with(context)                             //配置上下文
                .load(Uri.fromFile(new File(path.toString()))) //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
//                .error(R.drawable.ic_error)           //设置错误图片
//                .placeholder(R.drawable.ic_default_image)     //设置占位图片
//                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
                .into(imageView);
    }


//    @Override
//    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
//        Glide.with(activity)                             //配置上下文
//                .load(Uri.fromFile(new File(path))) //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
////                .error(R.drawable.ic_error)           //设置错误图片
////                .placeholder(R.drawable.ic_default_image)     //设置占位图片
////                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
//                .into(imageView);
//    }
//
//    @Override
//    public void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height) {
//        Glide.with(activity)                             //配置上下文
//                .load(Uri.fromFile(new File(path)))      //设置图片路径(fix #8,文件名包含%符号 无法识别和显示)
////                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存全尺寸
//                .into(imageView);
//    }
//
//    @Override
//    public void clearMemoryCache() {
//        //这里是清除缓存的方法,根据需要自己实现
//    }
}
