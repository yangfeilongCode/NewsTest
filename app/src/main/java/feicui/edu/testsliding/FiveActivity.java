package feicui.edu.testsliding;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 断点
 * Created by Administrator on 2016/10/18.
 */
public class FiveActivity extends Activity {
    Button mBtn;
    ArrayList<String>  list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         mBtn=new Button(this);
        setContentView(mBtn);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                get();
            }
        });
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        testDebug();
    }

    public void testDebug(){
        for (int i = 0; i <5 ; i++) {

            list.add(""+i);
            Log.e("aaa", "testDebug: "+list );
        }

    }

    public void get(){
        OkHttpClient client=new OkHttpClient.Builder()
                .connectTimeout(5000, TimeUnit.MILLISECONDS)
                .build();
        final Request request=new Request.Builder()
                .url("http://www.sina.com.cn/")
                .get()
                .build();
        Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("aaa", "onFailure: ----失败" );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str=response.body().string();
                Log.e("aaa", "onResponse: -----"+str );
            }
        });
    }
}
