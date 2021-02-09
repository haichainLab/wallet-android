package io.samos.wallet.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.signature.MediaStoreSignature
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.samos.wallet.R
import io.samos.wallet.api.ApiService
import io.samos.wallet.api.RetrofitService
import io.samos.wallet.base.SamosAppliacation
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException



/**
 * Created by luch on 2018/5/24.
 */

object ImageUtils {


    //保存文件到指定路径
    fun saveImageToGallery(context: Context, bmp: Bitmap): Boolean {
        // 首先保存图片
        val storePath = (Environment.getExternalStorageDirectory().toString()
                + File.separator + Environment.DIRECTORY_DCIM
                + File.separator + "Camera" + File.separator)
        val appDir = File(storePath)
        if (!appDir.exists()) {
            appDir.mkdir()
        }
        val fileName = System.currentTimeMillis().toString() + ".png"
        val file = File(appDir, fileName)
        try {
            var fileName = file.toString()
            val fos = FileOutputStream(fileName)
            //通过io流的方式来压缩保存图片
            val isSuccess = bmp.compress(Bitmap.CompressFormat.PNG, 60, fos)
            fos.flush()
            fos.close()

            //把文件插入到系统图库
            MediaStore.Images.Media.insertImage(context.contentResolver, file.absolutePath, fileName, null)

            //保存图片后发送广播通知更新数据库
            val uri = Uri.fromFile(file)
            context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
            return isSuccess
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return false
    }

    /**
     * 加载头像（网络数据）
     * @param imageView  控件
     * @param url  图片url
     */
    fun loaderIcon(imageView: ImageView, url: String) {
        //当url为空时，不请求网络
        if (url.isEmpty()) {
            Glide.with(SamosAppliacation.mInstance).load(R.drawable.samos_logo).into(imageView)
            //url不为空时，请求图片
            Log.d("samos image","url:"+url)
        } else {
            //判断用户是不是第一次请求该图片
            val lastModified = SharePrefrencesUtil.getInstance().getString(url+"f")
            //字段不为null和“”,则不是第一次请求该图片
            if (lastModified.isNotEmpty()) {
                Log.d("samos lastModified","url:"+url)

                //先加载本地缓存图片
                Glide.with(SamosAppliacation.mInstance)
                        .setDefaultRequestOptions(
                                RequestOptions()
                                        .placeholder(R.drawable.samos_logo)
                                        //图片签名信息，相同url下如果需要刷新图片，signature不同则会加载网络端的图片资源
                                        .signature(MediaStoreSignature(lastModified, 0, 0)))
                        .load(url)
                        .into(imageView)
                //先访问图片的信息，只拿到响应头（Head请求）,如果Last-Modified跟本地缓存图片不一致则向服务器请求最新图片
                RetrofitService.getInstance().retrofit.create(ApiService::class.java).executeGetWithHeaders(url, mapOf())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                {

                                    t: Response<Void> ->
                                    //访问图片成功时
                                    if (t.code() == 200) {
                                        val dateModified = t.headers().get("Last-Modified")
                                        //将Last-Modified字段存到SP里面
                                        SharePrefrencesUtil.getInstance().putString(url+"f", dateModified!!)

                                        //请求最新的图片
                                        Glide.with(SamosAppliacation.mInstance)
                                                .setDefaultRequestOptions(
                                                        RequestOptions()
                                                                .signature(MediaStoreSignature(dateModified, 0, 0)))
                                                .load(url)
//                                                .apply()

                                            //    .diskCacheStrategy( DiskCacheStrategy.SOURCE )
//                                                .into(imageView)
                                                .into(imageView)
                                        //图片找不到资源时
                                    } else if (t.code() == 404) {
                                        Log.d("samos img","url: 404")

                                    }
                                },
                                {
                                    t: Throwable -> LogUtils.d("header failed:"+t.toString())
                                })
            } else {
                Log.d("samos img occupy: ","url: first"+url)

                //第一次请求该图片时，没有缓存，先加载本地占位图
                Glide.with(SamosAppliacation.mInstance).load(R.drawable.samos_logo).into(imageView)
                //然后请求网络图片
                //先访问图片的信息，只拿到响应头（Head请求）,如果Last-Modified跟本地缓存图片不一致则向服务器请求最新图片
                RetrofitService.getInstance().retrofit.create(ApiService::class.java).executeGetWithHeaders(url, mapOf())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                { t: Response<Void> ->
                                    LogUtils.d("response:"+t.toString())
                                    //访问图片成功时
                                    if (t.code() == 200) {
                                        LogUtils.d(url+" try to load")

                                        val dateModified = t.headers().get("Last-Modified")
                                        //将Last-Modified字段存到SP里面
                                        SharePrefrencesUtil.getInstance().putString(url, dateModified!!)
                                        //请求最新的图片
                                        Glide.with(SamosAppliacation.mInstance)
                                                .setDefaultRequestOptions(
                                                        RequestOptions()
                                                                .signature(MediaStoreSignature(dateModified, 0, 0)))
                                                .load(url).into(imageView)
                                        //图片找不到资源时
                                    } else if (t.code() == 404) {
                                        //第一次访问该图片默认会加载占位图，所以网络图片404时不用再次加载
                                        LogUtils.d(url+" not found")
                                    }
                                },
                                    //访问图片失败时，//第一次访问该图片默认会加载占位图，所以不用再次加载
                                {
                                    t: Throwable -> LogUtils.d(t.toString()+" ->error url:"+url)
                                }
                        )
            }
        }


    }

}
