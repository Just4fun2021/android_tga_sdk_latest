package sg.just4fun.tgasdk.web;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.share.Share;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import sg.just4fun.tgasdk.R;
import sg.just4fun.tgasdk.callback.TGACallback;
import sg.just4fun.tgasdk.conctart.Conctant;
import sg.just4fun.tgasdk.modle.AdConfigBean;
import sg.just4fun.tgasdk.modle.AppConfig;
import sg.just4fun.tgasdk.modle.BipGameUserInfo;
import sg.just4fun.tgasdk.modle.BipGameUserUser;
import sg.just4fun.tgasdk.modle.GameListInfoBean;
import sg.just4fun.tgasdk.modle.GameinfoBean;
import sg.just4fun.tgasdk.modle.GooglePayInfo;
import sg.just4fun.tgasdk.modle.GooglePayInfoBean;
import sg.just4fun.tgasdk.modle.UserInFoBean;
import sg.just4fun.tgasdk.modle.UsersinputBean;
import sg.just4fun.tgasdk.tga.base.HttpBaseResult;
import sg.just4fun.tgasdk.tga.base.JsonCallback;
import sg.just4fun.tgasdk.tga.global.AppUrl;
import sg.just4fun.tgasdk.tga.global.Global;
import sg.just4fun.tgasdk.tga.global.HttpGetData;
import sg.just4fun.tgasdk.tga.ui.home.HomeActivity;
import sg.just4fun.tgasdk.tga.ui.home.model.RegisterBean;
import sg.just4fun.tgasdk.tga.ui.home.model.TgaSdkUserInFo;
import sg.just4fun.tgasdk.tga.utils.SpUtils;

//TGASDK初始化类
public class TgaSdk
{
    public static Context mContext;
    public static TGACallback.TgaEventListener listener;
    public static List<AdConfigBean> appConfigbeanList = new ArrayList<AdConfigBean>();
    public static String appKey;
    public static String appId;
    public static String appPaymentKey;
    public static String appConfigList;
    public static TGACallback.initCallback initCallback;
    public static String applovnIdConfig;
    public static String gameCentreUrl;
    private static String TGA = "TgaSdk";
    private static int isSuccess = 0;
    public static String iconpath;
    public static String packageName;
    public static String schemeUrl;
    public static int bipUserid;
    public static String bipToken;
    public static String rebipToken;
    public static TGACallback.GameCenterCallback gameCenterCallback;
    public static String appCode;
    public static String txnid = "";
    public static String msisid = "";
    public static String env;
    private static String infoUrl;
    public static String theme1;
    private static String theme;
    private static String lang1;
    public static String gameid;
    public static String ticketList;
    public static String userIdList;
    public static List<UsersinputBean> users;
    public static String ticket;
    public static int enemyid;
    public static int masterid;
    public static String theme2;
    public static HashMap<String, JSONObject> urlParam = new HashMap<>();

    private TgaSdk()
    {

    }

    public static String urlEncode(String text)
    {
        try
        {
            return URLEncoder.encode(text, "utf-8");
        } catch (Exception e)
        {
            return text;
        }
    }


    //TGASDK初始化方法
    public static void init(Context context, String appKey, String schemeUrl, String appPaymentKey, TGACallback.TgaEventListener listener, TGACallback.initCallback initCallback)
    {
        init(context, "", appKey, schemeUrl, appPaymentKey, listener, initCallback);
    }

    //TGASDK初始化方法
    public static void init(Context context, String env, String appKey, String schemeUrl, String appPaymentKey, TGACallback.TgaEventListener listener, TGACallback.initCallback initCallback)
    {
        TgaSdk.env = env;
        mContext = context.getApplicationContext();
//       if(!metaDataStringApplication.equals("")){
        MobileAds.initialize(mContext);
//       }
        TgaSdk.appKey = appKey;
        TgaSdk.schemeUrl = schemeUrl;
        TgaSdk.listener = listener;
        Log.e(TGA, "listener是不是空了=" + listener);
        TgaSdk.initCallback = initCallback;
        Log.e(TGA, "linitCallback是不是空了=" + initCallback);
        TgaSdk.appPaymentKey = appPaymentKey;
        Log.e(TGA, "appPaymentKey是不是空了=" + appPaymentKey);
//        MobileAds.initialize(context, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//                Gson gson = new Gson();
//                String s = gson.toJson(initializationStatus);
//                Log.e(TGA,"谷歌广告初始化="+ s);
//            }
//        });
//       获取用户配置表
        getUserInfo(appKey);

    }

    public static void initCodeTokenInfo(String pkName, UserInFoBean resultInfo, Gson gson, int p, int p2)
    {
        if (packageName != null && !packageName.equals(""))
        {
            if (packageName.equals(pkName))
            {
                iconpath = resultInfo.getIconpath();
                appConfigList = resultInfo.getAppConfig();
                if (appConfigList != null && !appConfigList.equals("") && !appConfigList.equals("{}"))
                {
                    AppConfig adConfigBean = gson.fromJson(appConfigList, AppConfig.class);
                    try
                    {
                        gameCentreUrl = Objects.requireNonNull(requireNotBlankString(adConfigBean.getGameCentreUrl()));
                        Log.e("游戏列表url", "游戏列表url=" + gameCentreUrl);
                    } catch (Exception e)
                    {
                        gameCentreUrl = Global.TEST_MOREN;
                    }
                    try
                    {
                        appCode = Objects.requireNonNull(requireNotBlankString(adConfigBean.getAppCode()));
                        Log.e("游戏列表url", "appCode=" + appCode);
                    } catch (Exception e)
                    {
                        appCode = "";
                    }
                    if (adConfigBean != null)
                    {
                        List<AdConfigBean> adList = adConfigBean.getAd();
                        if (adList == null || adList.isEmpty())
                        {
                            Log.e("tgasdk", "ad配置表isEmpty==" + adList.size());
                            applovnIdConfig = null;
                        } else
                        {
                            Log.e("tgasdk", "ad配置表==" + adList.size());
                            try
                            {
                                Log.e("tgasdk", "ad配置表applovnIdConfig==" + applovnIdConfig);
                                applovnIdConfig = adList.get(0).getConfig().toJson().toString();
                            } catch (Exception e2)
                            {
                                applovnIdConfig = null;
                                Log.e("tgasdk", "ad配置表Exception==" + adList.size());
                            }
                            Log.e("tgasdk", "ad配置表adList==" + applovnIdConfig);
                        }
                    }
                } else
                {
                    gameCentreUrl = Global.TEST_MOREN;
                }
                HttpGetData.getGooglePayInfo(mContext, appId, TgaSdk.env);
                Log.e("tgasdk", "ad配置表==" + applovnIdConfig);
                isSuccess = 1;
                initCallback.initSucceed();
            } else
            {
                Log.e(TGA, "包名不一致=");
                isSuccess = 0;
                initCallback.initError(mContext.getResources().getString(p));
            }
        } else
        {
            Log.e(TGA, "包名不一致=");
            isSuccess = 0;
            initCallback.initError(mContext.getResources().getString(p2));
        }
    }

    public static void svanTokenInfo(String response)
    {
        Log.e("TGA", "response" + response);
        try
        {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject user = jsonObject.getJSONObject("user");
            bipToken = jsonObject.getString("accessToken");
            rebipToken = jsonObject.getString("refreshToken");
            bipUserid = user.getInt("id");


            SpUtils.putString(mContext, "bipToken", bipToken);
            SpUtils.putInt(mContext, "bipUserId", bipUserid);
            SpUtils.putString(mContext, "reBipToken", rebipToken);
            HttpGetData.getGameListHttp(mContext, appId, bipToken, rebipToken, TgaSdk.env, TgaSdk.listener);

            String txnId = user.getString("txnId");
            SpUtils.putString(mContext, "bipTxnId", txnId);
            String name = user.getString("name");
            String header = user.getString("header");
            SpUtils.putString(mContext, "bipHeader", header);
            SpUtils.putString(mContext, "bipName", name);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    public static Context getContext()
    {
        return mContext;
    }

    //跳转游戏中心
    public static void goPage(Context context, String theme2, String url, String gameid, boolean navigationbar)
    {
        if (theme2 != null && !theme2.equals(""))
        {
            theme = Conctant.themeCorol(theme2);
        } else
        {
            theme = theme2;
        }
        goPage(context, theme, url, true, gameid, navigationbar);
    }

    //进入TGAsdk游戏中心方法
    public static void goPage(Context context, String theme, String url, boolean autoToken, String schemeQuery, boolean navigationbar)
    {
        if (theme == null || theme.equals(""))
        {
            theme1 = Conctant.themeCorolAppCode(appCode);
            theme2 = appCode;
        } else
        {
            theme1 = Conctant.themeCorolVuel(theme);
            theme2 = theme;
        }
        String bipHeader = SpUtils.getString(mContext, "bipHeader", "");
        String bipName = SpUtils.getString(mContext, "bipName", "");
        String bipTxnId = SpUtils.getString(mContext, "bipTxnId", "");
        String bipToken = SpUtils.getString(mContext, "bipToken", "");
        String reBipToken = SpUtils.getString(mContext, "reBipToken", "");
        //拼接参数
        JSONObject userParam = new JSONObject();
        JSONObject appParam = new JSONObject();
        String version = Conctant.getVersion(mContext);
        if (url == null || url.equals(""))
        {
            if (isSuccess == 1)
            {
                Log.e(TGA, "isSuccess==1");
                if (TgaSdk.listener != null)
                {
                    Log.e(TGA, "TgaSdk.listener不为空");
//                    String userInfo = TgaSdk.listener.getAuthCode();
                    if (bipToken == null || bipToken.equals(""))
                    {
                        Log.e(TGA, "bipToken=" + bipToken);
//                                  "&txnId=1&msisdn=1"+
                        url = TgaSdk.gameCentreUrl;// + "?appId=" + TgaSdk.appId + "&theme=" + theme2 + "&navigationbar=" + navigationbar + "&token=" + bipToken + "&refresh-token=" + reBipToken;//无底部
                        try
                        {
                            appParam.put("appId", TgaSdk.appId);
                            appParam.put("theme", theme2);
                            appParam.put("navigationBar", navigationbar);
                            //
                            userParam.put("token", bipToken);
                            userParam.put("refresh-token", reBipToken);

                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                        urlParam.put("app", appParam);
                        urlParam.put("user", userParam);

                        theme1 = "#" + theme1;
                        Intent intent = new Intent(context, HomeActivity.class);
                        intent.putExtra("url", url);
                        intent.putExtra("gopag", 0);
                        intent.putExtra("yssdk", 1);
                        intent.putExtra("statusaBarColor", theme1);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        return;
                    } else
                    {
//                                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
//                                TgaSdkUserInFo userInFo = gson.fromJson(userInfo, TgaSdkUserInFo.class);
                        Log.e(TGA, "游戏中心列表=" + TgaSdk.gameCentreUrl);
                        if (TgaSdk.gameCentreUrl == null || TgaSdk.gameCentreUrl.equals(""))
                        {
                            TgaSdk.gameCentreUrl = Global.TEST_MOREN;
                        }
                        Log.e("rebipToken没有", "rebipToken==" + reBipToken);

                        if (bipTxnId != null && !bipTxnId.equals(""))
                        {
                            txnid = "txnId=" + bipTxnId;
                            msisid = "&msisdn=" + bipTxnId;
                        } else
                        {
                            txnid = "";
                            msisid = "";
                        }

                        if (schemeQuery != null && !schemeQuery.equals(""))
                        {
                            url = TgaSdk.gameCentreUrl;// + "?" + txnid + "&theme=" + theme2 + "&" + schemeQuery + "&navigationbar=" + navigationbar + "&appId=" + TgaSdk.appId + "&nickname=" + bipName + msisid + "&token=" + bipToken + "&refresh-token=" + reBipToken + "&appversion=" + version + "&avatar=" + bipHeader;//无底部
                            try
                            {
                                userParam.put("txnId", bipTxnId);
                                userParam.put("nickname", bipName);
                                userParam.put("msisdn", bipTxnId);
                                userParam.put("token", bipToken);
                                userParam.put("refresh-token", reBipToken);
                                userParam.put("avatar", bipHeader);
                                //
                                appParam.put("theme", theme2);
                                appParam.put("schemeQuery", schemeQuery);
                                appParam.put("navigationBar", navigationbar);
                                appParam.put("appId", TgaSdk.appId);
                                appParam.put("appversion", version);

                                urlParam.put("app", appParam);
                                urlParam.put("user", userParam);
                            } catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        } else
                        {
                            url = TgaSdk.gameCentreUrl;// + "?" + txnid + "&theme=" + theme2 + "&appId=" + TgaSdk.appId + "&navigationbar=" + navigationbar + "&nickname=" + bipName + "&token=" + bipToken + "&refresh-token=" + reBipToken + msisid + "&appversion=" + version + "&avatar=" + bipHeader;//无底部
                            try
                            {
                                userParam.put("txnId", bipTxnId);
                                userParam.put("nickname", bipName);
                                userParam.put("msisdn", bipTxnId);
                                userParam.put("token", bipToken);
                                userParam.put("refresh-token", reBipToken);
                                userParam.put("avatar", bipHeader);
                                //
                                appParam.put("theme", theme2);
                                appParam.put("navigationBar", navigationbar);
                                appParam.put("appId", TgaSdk.appId);
                                appParam.put("appversion", version);

                                urlParam.put("app", appParam);
                                urlParam.put("user", userParam);
                            } catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                        theme1 = "#" + theme1;
                        Intent intent = new Intent(context, HomeActivity.class);
                        intent.putExtra("url", url);
                        intent.putExtra("gopag", 0);
                        intent.putExtra("yssdk", 1);
                        intent.putExtra("statusaBarColor", theme1);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        return;
                    }
                } else
                {
                    Log.e(TGA, "TgaSdk.listener=null");
                    initCallback.initError(mContext.getResources().getString(R.string.sdkiniterror));
                    return;
                }
            } else if (isSuccess == 2)
            {
                Log.e(TGA, "初始化.isSuccess=2");
                initCallback.initError(mContext.getResources().getString(R.string.sdkinitcompleted));
                return;
            } else
            {
                Log.e(TGA, "初始化.isSuccess=0");
                initCallback.initError(mContext.getResources().getString(R.string.sdkiniterror));
                return;
            }
        }
        theme1 = "#" + theme1;
        try
        {
            userParam.put("txnId", bipTxnId);
            userParam.put("nickname", bipName);
            userParam.put("msisdn", bipTxnId);
            userParam.put("token", bipToken);
            userParam.put("refresh-token", reBipToken);
            userParam.put("avatar", bipHeader);
            //
            appParam.put("theme", theme1);
            appParam.put("navigationBar", navigationbar);
            appParam.put("appId", TgaSdk.appId);
            appParam.put("appversion", version);

            urlParam.put("app", appParam);
            urlParam.put("user", userParam);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        Intent intent = new Intent(context, HomeActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("yssdk", 1);
        intent.putExtra("statusaBarColor", theme1);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
//            }
//        }).start();

    }

    public static String buildUserInfo(String userId, String nickName, String headImg)
    {
        TgaSdkUserInFo userInFo = new TgaSdkUserInFo(userId, nickName, headImg);
        try
        {
            return userInFo.toJson().toString();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }

    public static String getSchemeQuery(String query)
    {
        TgaSdkUserInFo userInFo = new TgaSdkUserInFo(query);
        try
        {
            return userInFo.toJson().toString();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }

    public static void openGameCenter(Context context, String theme2, boolean showCenterNavBar, TGACallback.GameCenterCallback gameCenterCallback)
    {
        openGameCenter(context, theme2, showCenterNavBar, "", gameCenterCallback);
    }

    //跳转游戏中心
    public static void openGameCenter(Context context, String theme2, boolean showCenterNavBar, String secUrl, TGACallback.GameCenterCallback gameCenterCallback)
    {
        TgaSdk.gameCenterCallback = gameCenterCallback;
        if (theme2 != null && !theme2.equals(""))
        {
            theme = Conctant.themeCorol(theme2);
        } else
        {
            theme = theme2;
        }
        goPage(context, theme, "", true, "", showCenterNavBar);
        if (!TextUtils.isEmpty(secUrl))
        {
            String bipHeader = SpUtils.getString(mContext, "bipHeader", "");
            String bipName = SpUtils.getString(mContext, "bipName", "");
            String bipTxnId = SpUtils.getString(mContext, "bipTxnId", "");
            String bipToken = SpUtils.getString(mContext, "bipToken", "");
            String reBipToken = SpUtils.getString(mContext, "reBipToken", "");
            String version = Conctant.getVersion(mContext);
            JSONObject userParam = new JSONObject();
            JSONObject appParam = new JSONObject();
            try
            {
                userParam.put("txnId", bipTxnId);
                userParam.put("nickname", bipName);
                userParam.put("msisdn", bipTxnId);
                userParam.put("token", bipToken);
                userParam.put("refresh-token", reBipToken);
                userParam.put("avatar", bipHeader);
                //
                appParam.put("theme", theme1);
                appParam.put("navigationBar", showCenterNavBar);
                appParam.put("appId", TgaSdk.appId);
                appParam.put("appversion", version);

                urlParam.put("app", appParam);
                urlParam.put("user", userParam);
            } catch (Exception e)
            {
                e.printStackTrace();
            }

            Intent intent = new Intent(context, WebViewGameActivity.class);
            intent.putExtra("url", secUrl);
            intent.putExtra("gopag", 1);
            intent.putExtra("navigationBar", showCenterNavBar);
            intent.putExtra("statusaBarColor", theme1);
            intent.putExtra("yssdk", 1);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    //跳转游戏中心
    public static void goLink(Context context, String url, boolean navigationbar)
    {
        goPage(context, "", url, true, "", navigationbar);
    }

    public static void shareSuccess(String uuid)
    {
        shared(uuid, true);
    }

    public static void shareError(String uuid)
    {
        shared(uuid, false);
    }

    public static void shared(String uuid, boolean success)
    {
        if (TGACallback.listener != null)
        {
            TGACallback.listener.shareCall(uuid, success);
        }
    }

    public static void onUserLogined(String uuid, String code)
    {
        if (TGACallback.codeCallback != null)
        {

            TGACallback.codeCallback.codeCall(uuid, code);
        }
    }

    public static void onLogout()
    {
        if (TGACallback.outLoginCallback != null)
        {
            TGACallback.outLoginCallback.outLoginCall();
        }

    }

    public static void lang(String lang)
    {
        if (TGACallback.langListener != null)
        {
            TGACallback.langListener.getLang(lang);
        }
    }

    public static void shared(String uuid, int successCount)
    {
        shared(uuid, successCount > 0);
    }

    public static String requireNotBlankString(String value)
    {
        if (value == null || value.trim().equals(""))
        {
            return null;
        }
        return value;
    }

    //拉取SDK配置表
    public static void getUserInfo(String appKe)
    {
        isSuccess = 2;
        JSONObject jsonObject = new JSONObject();
        String data = "{}";
        try
        {
            jsonObject.put("appSdkKey", appKe);
            data = jsonObject.toString();
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);
        if (env.equals("bip"))
        {
            infoUrl = AppUrl.BIP_TGA_SDK_INFO;
        } else
        {
            infoUrl = AppUrl.TGA_SDK_INFO;
        }
        OkGo.<String>post(infoUrl)
                .tag(mContext)
                .upRequestBody(body)
                .execute(new JsonCallback<String>(mContext)
                {
                    @Override
                    public void onSuccess(Response response)
                    {
                        String s1 = response.body().toString();
                        Log.e(TGA, "初始化成功的=" + s1);
                        Gson gson = new GsonBuilder()
                                .serializeNulls()
                                .create();
                        HttpBaseResult httpBaseResult = gson.fromJson(s1, HttpBaseResult.class);
                        Log.e(TGA, "初始化成功的=" + httpBaseResult.getStateCode());
                        Log.e(TGA, "resultInfo内容=" + gson.toJson(httpBaseResult.getResultInfo()));
                        try
                        {
                            if (httpBaseResult.getStateCode() == 1)
                            {
                                Log.e(TGA, "初始化成功的=");
                                if (listener != null)
                                {
                                    Log.e(TGA, "listener是不是空了=" + gson.toJson(httpBaseResult.getResultInfo()));
                                    String s = gson.toJson(httpBaseResult.getResultInfo());
                                    UserInFoBean resultInfo = gson.fromJson(s, UserInFoBean.class);
                                    Log.e(TGA, "listener是不是空了resultInfo1" + gson.toJson(httpBaseResult.getResultInfo()));
                                    String pkName = mContext.getPackageName();
                                    packageName = resultInfo.getPackageName();
                                    //包名相等
                                    appId = resultInfo.getAppId();
//                            获取游戏token
//                            gameUserLogin();
//                            通过code获取用户信息
                                    String userInfo = listener.getAuthCode();
                                    SpUtils.putString(mContext, "yhAppId", appId);
                                    isSuccess = 2;
                                    if (userInfo != null && !userInfo.equals(""))
                                    {
                                        HttpGetData.userCodeLogin(mContext, pkName, resultInfo, gson, TgaSdk.env, listener, appId);
                                    } else
                                    {
                                        HttpGetData.gameUserLogin(mContext, pkName, resultInfo, appId, gson, TgaSdk.env);
                                    }
                                } else
                                {
                                    isSuccess = 0;
                                    initCallback.initError("TgaEventListener接口为空");
                                }
                            } else
                            {
                                isSuccess = 0;
                                initCallback.initError("服务端errorCode=" + httpBaseResult.getStateCode());
                            }
                        } catch (Exception e)
                        {
                            isSuccess = 0;
                            initCallback.initError("errorCode=" + -5);
                            Log.e("tgasdk", "initiate failed", e);
                        }
                    }

                    @Override
                    public void onError(Response response)
                    {
                        isSuccess = 0;
                        initCallback.initError("errorCode=" + -5);
                        Log.e("初始化", "充值失败=" + response.getException().getMessage());
                    }
                });
    }


    public static void goPageByScheme(Uri schemeUri, boolean navigationbar)
    {
        if (schemeUri != null || !schemeUri.equals(""))
        {
            try
            {
                String query = schemeUri.getQuery();
                goPage(mContext, "", "", true, query, navigationbar);
            } catch (Exception e)
            {
                initCallback.initError("schemeUri存在异常");
            }
        } else
        {
            goPage(mContext, "", "", null, navigationbar);
        }
    }

    public static List<GameinfoBean> getGameList()
    {
        Log.e(TGA, "1V1游戏列表数据getGameList=" + new Gson().toJson(HttpGetData.gameif));
        return HttpGetData.gameif;
    }

    //    获取sdk对应info
    public static String getBipUserInfo()
    {
        try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", bipUserid);
            jsonObject.put("token", bipToken);
            jsonObject.put("retoken", rebipToken);
            return jsonObject.toString();
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return "获取错误";
    }

    //figh对战轮询结果
    public static void figtPollingInfo(String ticket)
    {
        String langString = Conctant.getLangString();
        HttpGetData.fightGetRivalInfo(mContext, bipToken, rebipToken, "1v1", appId, langString, ticket);

    }

    //fight对战获取tiket
    public static void fightTichet()
    {
        String langString = Conctant.getLangString();
        HttpGetData.getFightTicket(mContext, bipToken, rebipToken, appId, "1v1", langString);

    }

    //获取首页游戏列表
    public static void homeGameListData()
    {
        HttpGetData.getGameList(mContext, appId);
    }

    //创建fight对战房间
    public static void fightGameRoom(String gameid, String ticketList, String userIdList, List<UsersinputBean> users)
    {
        String langString = Conctant.getLangString();
        TgaSdk.gameid = gameid;
        TgaSdk.ticketList = ticketList;
        TgaSdk.userIdList = userIdList;
        TgaSdk.users = users;
        HttpGetData.getRoomInfo(mContext, bipToken, rebipToken, gameid, "1v1", ticketList, userIdList, users, 2, appId, langString);
    }

    //    根据游戏id获取某款游戏信息
    public static void fightGameInfo(String gameid)
    {
        TgaSdk.gameid = gameid;
        HttpGetData.getGameInfo(mContext, bipToken, rebipToken, gameid);
    }

    //    快速游戏创建房间
    public static void quickFightGameRoom(String gameid, String ticket)
    {
        String langString = Conctant.getLangString();
        TgaSdk.gameid = gameid;
        TgaSdk.ticket = ticket;
        HttpGetData.getStartFight(mContext, bipToken, rebipToken, appId, langString, gameid, ticket, "1v1");

    }

    //   获取对战总战绩
    public static void gameTotalScore(String gameid, int enemyid, int masterid)
    {
        String langString = Conctant.getLangString();
        TgaSdk.gameid = gameid;
        TgaSdk.enemyid = enemyid;
        TgaSdk.masterid = masterid;
        HttpGetData.getGameCombat(mContext, bipToken, rebipToken, TgaSdk.appId, enemyid, TgaSdk.gameid, langString, masterid);
    }

    //获取平台金币钻石信息数据
    public static void getGoldInfo()
    {

        HttpGetData.getMoney(mContext, appId, bipToken, rebipToken);

    }

    // 对战游戏平台门槛值
    public static int getGoldSill()
    {

        return 100;//暂时是100金币 等服务端接口提供值
    }


}
