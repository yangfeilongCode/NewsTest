package feicui.edu.testsliding;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * OkHttp 的使用
 * Created by Administrator on 2016/10/17.
 */
public class FourActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button mBtn=new Button(this);
        setContentView(mBtn);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post();
//                get();
            }
        });
    }

    /**
     * OkHttp 网络请求框架  稳定
     * Volly  适合频繁请求
     *  OkHttp 的使用
     *   get();
     *   post();
     */
    public void get(){
        //1.实例化一个OkHttpClient 对象
        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(3000, TimeUnit.MILLISECONDS)  //连接超时   毫秒/。。。
                .build(); //返回build
        //2.初始化一个请求
        Request request=new Request.Builder()
                .url("http://118.244.212.82:9092/newsClient//news_list?nid=1&stamp=20140321000000&dir=1&subid=1&cnt=20&ver=0000000")
                .get()
                .build();
        //3.加入请求
        Call call=client.newCall(request);
        //4.执行请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {  //失败
                Log.e("aaa","onFailure--------失败");

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException { //成功
                ResponseBody body=response.body();  //响应体
                String str=body.string(); //调string() 方法
                Log.e("aaa","onResponse-----"+str);
            }
        });
    }

    public void post(){
        //1.
        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(2000,TimeUnit.MILLISECONDS)
                .build();
        //2.请求
        //请求体
        FormBody body=new   FormBody.Builder()
                .add("size","10")
                .build();

        Request request=new Request.Builder()
                .url("Http://www.wycode.cn/api/movie/getMovies")
                .post(body)
                .build();
        //3.
        Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("aaa","onFailure--------失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body=response.body();
                String str=body.string();
                Log.e("aaa","onResponse-----"+str);
            }
        });
    }

}
