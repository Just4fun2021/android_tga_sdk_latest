package sg.just4fun.tgasdk.web.pay;

import android.os.Build;
import android.util.Log;
import android.webkit.WebView;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sg.just4fun.tgasdk.adsdk.TgaSdkEventDataBean;
import sg.just4fun.tgasdk.modle.AppConfig;
import sg.just4fun.tgasdk.modle.PayConfigBean;
import sg.just4fun.tgasdk.modle.UserInFoBean;
import sg.just4fun.tgasdk.web.TgaSdk;
import sg.just4fun.tgasdk.web.banner.AdConfigUtlis;

public class GooglePayWayUtils {

    private static List<PayConfigBean> appConfigbeanList=new ArrayList<>();
    private static boolean enabled;

    public static void getAppIdEvents(WebView webView, String uuid) {
            if (TgaSdk.appConfigList==null||TgaSdk.appConfigList.equals("")||TgaSdk.appConfigList.equals("{}")){
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("uuid",uuid);
                jsonObject.put("data","{}");
                String s = jsonObject.toString();
                Log.e("配置为空","getAppIdEventss ="+s);
                String scriptCode = toJavaScriptCode("null", s);
                Log.e("配置为空","getAppIdEvents scriptCode="+scriptCode);
                webView.post(new ScriptCodeRunnable(scriptCode, webView));
            } catch (JSONException jsonException) {
                Log.e("配置为空","getAppIdEvents配置为空="+jsonException.getMessage());
                jsonException.printStackTrace();
            }
            return;
        }
        Gson gson = new Gson();
        AppConfig appConfig = gson.fromJson(TgaSdk.appConfigList, AppConfig.class);
        appConfigbeanList = appConfig.getPayMentList();
        if(appConfigbeanList!=null&&appConfigbeanList.size()>0){
            Log.e("配置为空","配置不为空");
            for (int a=0;a<appConfigbeanList.size();a++){
                if (appConfigbeanList.get(a).getChannelName().equals("googlePay")) {
                    enabled = appConfigbeanList.get(a).getEnabled();
                    Log.e("googlePayWay","是否充值="+enabled);
                }
            }

            GooglePayWayInFo googlePayWayInFo = new GooglePayWayInFo(TgaSdk.appId,true,enabled);
            TgaSdkEventDataBean tgaSdkEventDataBean = new TgaSdkEventDataBean(uuid, googlePayWayInFo);
            try {
                String scriptCode = toJavaScriptCode("null", tgaSdkEventDataBean.toJsCode());
                Log.e("googlePayWay获取appid回调","share="+scriptCode+"  run on="+webView);
                webView.post(new ScriptCodeRunnable(scriptCode, webView));
            } catch (Exception e) {
                e.printStackTrace();
                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.put("uuid",uuid);
                    jsonObject.put("data","{}");
                    String s = jsonObject.toString();
                    String scriptCode = toJavaScriptCode("null", s);
                    webView.post(new ScriptCodeRunnable(scriptCode, webView));
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
            }
        }else {
            Log.e("配置为空","配置=为空");
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put("uuid",uuid);
                jsonObject.put("data","{}");
                String s = jsonObject.toString();
                Log.e("配置为空","s ="+s);
                String scriptCode = toJavaScriptCode("null", s);
                Log.e("配置为空","scriptCode="+scriptCode);
                webView.post(new ScriptCodeRunnable(scriptCode, webView));
            } catch (JSONException jsonException) {
                Log.e("配置为空","配置为空="+jsonException.getMessage());
                jsonException.printStackTrace();
            }
        }

    }

    public static String toJavaScriptCode(String error,String string )  {
        return "TgaSdk.onEvent("+error+", "+string+")";
    }

    public static void googlePayWayEvents(WebView webView1, String uuid, String success) {
        Log.e("googlePayWay","change="+success);
        GooglePayWayInFo googlePayWayInFo = new GooglePayWayInFo(success);
        TgaSdkEventDataBean tgaSdkEventDataBean = new TgaSdkEventDataBean(uuid, googlePayWayInFo);
        try {
            String scriptCode = toJavaScriptCode("null", tgaSdkEventDataBean.toJsCode());
            Log.e("googlePayWay回调","share="+scriptCode+"  run on= "+webView1);
            webView1.post(new ScriptCodeRunnable1(scriptCode, webView1));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static class ScriptCodeRunnable implements Runnable {
        private final String scriptCode;
        private final WebView webView;
        public ScriptCodeRunnable(String scriptCode, WebView webView) {
            this.scriptCode = scriptCode;
            this.webView = webView;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void run() {
            Log.e("配置为空", "JSON="+scriptCode +" run on " +webView.getUrl());
            Log.e("执行了回调", "JSON="+scriptCode +" run on " +webView.getUrl());
            webView.evaluateJavascript(scriptCode, null);

        }
    }


    public static class ScriptCodeRunnable1 implements Runnable {
        private final String scriptCode1;
        private final WebView webView1;
        public ScriptCodeRunnable1(String scriptCode, WebView webView) {
            this.scriptCode1 = scriptCode;
            this.webView1 = webView;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void run() {
            Log.e("googlePayWay执行了回调", " JSON= "+scriptCode1 +" run on " +  webView1.getUrl());
            webView1.evaluateJavascript(scriptCode1, null);
        }
    }
}
