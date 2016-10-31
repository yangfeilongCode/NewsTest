package feicui.edu.testsliding;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;

import edu.zx.slidingmenu.SlidingMenu;

/**
 * 手机支付
 * Created by Administrator on 2016/10/14.
 */
public class TwoActivity extends Activity implements View.OnClickListener {

    Button mBtnDownload;  //下载按钮
    SlidingMenu slidingMenu; //划动菜单
    ListView mListView; //右边布局View控件
    DownloadManager manager; //下载管理
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
    }
    @Override
    public void onContentChanged() {
        super.onContentChanged();

        mBtnDownload= (Button) findViewById(R.id.btn_download);
        mBtnDownload.setOnClickListener(this);
        slidingMenu=new SlidingMenu(this); //初始化一个滑动菜单
        slidingMenu.attachToActivity(this,slidingMenu.SLIDING_CONTENT); //滑动菜单与Activity关联
        //布局填充器
        LayoutInflater inflater=getLayoutInflater();
        View leftView=inflater.inflate(R.layout.view_left,null); //布局填充
        mListView= (ListView) leftView.findViewById(R.id.lv_left);
        slidingMenu.setMenu(leftView); //设置左边菜单布局
        View rightView=inflater.inflate(R.layout.view_rigth,null);
        slidingMenu.setSecondaryMenu(rightView); //设置右边菜单布局

        slidingMenu.setMode(SlidingMenu.LEFT_RIGHT); //展示模式 左右都有
        slidingMenu.setBehindOffset(200); //设置边距
        //设置触摸方式
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN); //全屏模式
    }

    @Override
    public void onBackPressed() {
        if (slidingMenu.isMenuShowing()||slidingMenu.isSecondaryMenuShowing()){//是否有Menu（左/右）展示
            slidingMenu.showContent();//展示内容
            return;
        }//没有 调父类原有的 不做改变
        super.onBackPressed();
    }

    /**
     * 下载APK
     * @param downloadUri 下载请求的地址
     * @param downloadPath 下载到的路径地址
     */
    public void downloadAPK(String downloadUri,String downloadPath){

      //1.拿到DownloadManager对象 ---------获取系统服务
        manager= (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        //2.需要创建一个下载请求
        DownloadManager.Request request=new DownloadManager.Request(Uri.parse(downloadUri));//parse 解析

        //3.做一些额外的设置
         //  1.网络要求    network 网络（wifi下载|流量下载）
         //设置允许下在环境类型
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI|DownloadManager.Request.NETWORK_MOBILE);
         //2.设备通知栏是否显示
        request.setShowRunningNotification(true); //显示
        request.setDestinationInExternalFilesDir(this,null,downloadPath); //下载的目的地地址
        //4.下载
        Toast.makeText(this,"加入下载队列。。。",Toast.LENGTH_LONG).show();
        downId= manager.enqueue(request); //入队
    }
    long downId;
    @Override
    public void onClick(View v) {
        //调下载的方法 并传入路径参数
        downloadAPK("http://192.168.199.239:8080/b.apk","download.apk");
    }

    DownLoadReceiver receiver; //声明一个下载接收器
    @Override
    protected void onResume() { //？？
        super.onResume();
        receiver=new DownLoadReceiver(); //初始化下载接收器
        //Action
        IntentFilter filter=new IntentFilter(); //过滤器
        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE); //下载完成
        registerReceiver(receiver,filter); //记录接收
    }
    @Override
    protected void onDestroy() { //取消  破坏
        super.onDestroy();
        unregisterReceiver(receiver); //移除接收器
    }
    /**
     * 广播接收器  下载完成监听广播
     */
    public class DownLoadReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) { //接收
            Toast.makeText(context,"下载完成",Toast.LENGTH_SHORT).show();

            long id=intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1);
            if (id==downId) {//是本次下载
                //打开此   APK    隐式意图
                //l路径
                Uri uri=manager.getUriForDownloadedFile(downId);
                //文件列路径
                String[] filePathColumn={MediaStore.Images.Media.DATA};  //MediaStore---媒体库
                //游标 接收内容解析查询结果
                Cursor curs=getContentResolver().query(uri,filePathColumn,null,null,null);
                curs.moveToFirst();//游标移至第一行
                //通过游标获得对应列的下标
                int columnIndex=curs.getColumnIndex(filePathColumn[0]);
                String path=curs.getString(columnIndex); //通过游标获取对应列的路径
                Log.e("aaa","onActivityResult++++++++"+path);

                Intent intent1 = new Intent();
                intent1.setAction(Intent.ACTION_VIEW); //开启
                //指定文件路径
                //APK--------application/vnd.android.package-archive
                intent1.setDataAndType(Uri.fromFile(new File(path)),"application/vnd.android.package-archive");
                startActivity(intent1);
            }
        }
    }
}
