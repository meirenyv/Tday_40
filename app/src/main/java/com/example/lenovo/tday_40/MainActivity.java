package com.example.lenovo.tday_40;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.tencent.qq.QQ;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    String imageString=null;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ShareSDK.initSDK(this);
        imageView = (ImageView) findViewById(R.id.imagehhh);
        findViewById(R.id.mBtnShareSdk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });
        findViewById(R.id.mBtnLoginSdk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        String ppp = "http://img3.imgtn.bdimg.com/it/u=1799345195,2075280808&fm=23&gp=0.jpg";
         handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String titles= (String) msg.obj;
                Log.i("info","****************88andleMessage:"+titles);
                //Picasso必须在主线程中,不能在子线程中
                Picasso.with(MainActivity.this).load(titles)
                        .into(imageView);
            }
        };
    }

        /**
         * 第三方登录
         */
    private void login() {

        {
            Platform qq = ShareSDK.getPlatform(QQ.NAME);
            //回调信息，可以在这里获取基本的授权返回的信息，但是注意如果做提示和UI操作要传到主线程handler里去执行
            qq.setPlatformActionListener(new PlatformActionListener() {
                @Override
                public void onError(Platform arg0, int arg1, Throwable arg2) {
                    // TODO Auto-generated method stub
                    arg2.printStackTrace();
                    Log.e("---------","用户不合法");
                }

                @Override
                public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                    // TODO Auto-generated method stub
                    /*Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);*/

                    //输出所有授权信息
                    Log.e("--数据:----",arg0.getDb().exportData());
                    Log.e("---------","用户合法的"+arg2);
                    Log.i("info", "==用户名=== "+arg0.getDb().getUserName());

                    imageString=arg0.getDb().getUserIcon();
                    Log.i("info", "==头像=== "+imageString);
                    Message message=handler.obtainMessage();
                    message.obj=imageString;

                    handler.sendMessage(message);

                }
                @Override
                public void onCancel(Platform arg0, int arg1) {
                    // TODO Auto-generated method stub
                    Log.e("---------","用户取消了授权");
                }
            });
            //authorize与showUser单独调用一个即可
            //调用这个方法的话是没有数据返回来的
            // qq.authorize();//单独授权,OnComplete返回的hashmap是空的
            qq.showUser("null");//授权并获取用户信息
        }


    }

    /**
     * ShareSdk的分享
     */
    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");
        //不显示编辑页面分享
       // oks.setSilent(true);   //隐藏编辑页面
        // 启动分享GUI
        oks.show(this);

    }
}
