 package sg.just4fun.tgasdk.tga.global;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

import com.google.common.base.MoreObjects;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import sg.just4fun.tgasdk.R;
import sg.just4fun.tgasdk.callback.TGACallback;
import sg.just4fun.tgasdk.conctart.Conctant;
import sg.just4fun.tgasdk.modle.GameinfoBean;
import sg.just4fun.tgasdk.modle.GooglePayInfo;
import sg.just4fun.tgasdk.modle.UserInFoBean;
import sg.just4fun.tgasdk.modle.UsersinputBean;
import sg.just4fun.tgasdk.tga.base.HttpBaseResult;
import sg.just4fun.tgasdk.tga.base.JsonCallback;
import sg.just4fun.tgasdk.tga.utils.SharedPreferencesUtil;
import sg.just4fun.tgasdk.tga.utils.SpUtils;
import sg.just4fun.tgasdk.tga.utils.ToastUtil;
import sg.just4fun.tgasdk.web.Conctart;
import sg.just4fun.tgasdk.web.TgaSdk;
import sg.just4fun.tgasdk.web.pay.ResultBean;

public class HttpGetData {
    private static String googlepayUrl;
    public static String TGA="TgaSdk";
    public static List<GooglePayInfo> infoList=new ArrayList<>();
    public static String lang;
    public static String gamelistUrl;
    public static List<GameinfoBean> gameif=new ArrayList<>();
    private static String loginsdkUrl;
    private static String userinfoUrl;
    public static String ticket;

//刷新token
    public static void getToken(Context context, String retoken,int a) {
        String data="{}";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("refreshToken",retoken);
            data = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);
        OkGo.<String>post(AppUrl.GAME_REFRESHTOKEN)
                .tag(context)
                .upRequestBody(body)
                .execute(new JsonCallback<String>(context) {
                    @Override
                    public void onSuccess(Response response) {
                        String s1 = response.body().toString();
                        Log.e(TGA,"初始化成功的="+s1);
                        Gson gson = new GsonBuilder()
                                .serializeNulls()
                                .create();
                        HttpBaseResult httpBaseResult = gson.fromJson(s1, HttpBaseResult.class);
                        if (httpBaseResult.getStateCode() == 1) {
                            try {
                                JSONObject jsonObject1 = new JSONObject(new Gson().toJson(httpBaseResult.getResultInfo()));
                                String token = jsonObject1.getString("token");
                                JSONObject jsonObject2 = new JSONObject(token);
                                String accessToken = jsonObject2.getString("accessToken");
                                String refreshToken = jsonObject2.getString("refreshToken");
                                SpUtils.putString(context,"bipToken",accessToken);
                                SpUtils.putString(context,"reBipToken",refreshToken);
                                if (a==0){
                                    getGameListHttp(context,TgaSdk.appId, accessToken,refreshToken,TgaSdk.env,TgaSdk.listener);
                                }else if(a==1){
                                    String langString = Conctant.getLangString();
                                 getFightTicket(context,accessToken,refreshToken,TgaSdk.appId,"1v1",langString);
                                }else if (a==2){
                                    String langString = Conctant.getLangString();
                                    fightGetRivalInfo(context,accessToken,refreshToken,"1v1",TgaSdk.appId,langString,ticket);
                                }else if (a==3){
                                    String langString = Conctant.getLangString();
                                getRoomInfo(context,accessToken,refreshToken,TgaSdk.gameid,"1v1",TgaSdk.ticketList,TgaSdk.userIdList,TgaSdk.users,2,TgaSdk.appId,langString);

                                }else if (a==4){
                                    getGameInfo(context,accessToken,refreshToken,TgaSdk.gameid);

                                }else if (a==5){
                                    String langString = Conctant.getLangString();
                                    getStartFight( context,  accessToken, refreshToken, TgaSdk.appId,  langString,  TgaSdk.gameid,  TgaSdk.ticket,  "1v1");
                                }else if (a==6){
                                    String langString = Conctant.getLangString();
                                    getGameCombat(context,accessToken,refreshToken,TgaSdk.appId,TgaSdk.enemyid,TgaSdk.gameid,langString,TgaSdk.masterid);
                                }else if (a==7){
                                    getMoney( context,TgaSdk.appId,accessToken,refreshToken);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onError(Response response) {

                    }
                });
    }


    // TGASDK拉取google支付配置
    public static void getGooglePayInfo(Context mContext, String appId, String env) {
        JSONObject jsonObject = new JSONObject();
        String data = "{}";
        try {
            jsonObject.put("appId", appId);
            data = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);
        if(env.equals("bip")){
            googlepayUrl= AppUrl.BIP_GET_GOOGLEPAY_INFO;
        }else {
            googlepayUrl= AppUrl.GET_GOOGLEPAY_INFO;
        }
//
        OkGo.<String>post(googlepayUrl)
                .tag(mContext)
                .upRequestBody(body)
                .execute(new JsonCallback<String>(mContext) {
                    @Override
                    public void onSuccess(Response response) {
                        String s1 = response.body().toString();
                        Log.e(TGA,"初始化成功的="+s1);
                        Gson gson = new GsonBuilder()
                                .serializeNulls()
                                .create();
                        HttpBaseResult httpBaseResult = gson.fromJson(s1, HttpBaseResult.class);
                        if (httpBaseResult.getStateCode() == 1) {
                            try {
                                String s = gson.toJson(httpBaseResult.getResultInfo());
                                JSONObject jsonObject1 = new JSONObject(s);
                                JSONArray data1 = jsonObject1.getJSONArray("data");
                                if (data1!=null&&data1.length()>0){
                                    for (int a=0;a<data1.length();a++){
                                        String o = String.valueOf(data1.get(a));
                                        GooglePayInfo googlePayInfo = gson.fromJson(o, GooglePayInfo.class);
                                        infoList.add(googlePayInfo);
                                    }
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Log.e(TGA,"google支付配置="+infoList);
                        }

                    }
                    @Override
                    public void onError(Response response) {
                        Log.e(TGA,"google支付配置失败="+response.message());
                    }
                });
    }


    // TGASDK拉取游戏列表
    public static void getGameListHttp(Context mContext,String appId, String accessToken,String reToken, String env, TGACallback.TgaEventListener listener) {
        String lang1 = listener.getLang();
        if (lang1==null||lang1.equals("")){
            String local = Locale.getDefault().toString();
            lang= Conctart.toStdLang(local);
        }else {
            lang=lang1;
        }
        JSONObject jsonObject = new JSONObject();
        String data = "{}";
        try {
            jsonObject.put("tag", "battle_1v1");
            data = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);

        if(env.equals("bip")){
            gamelistUrl= AppUrl.BIP_GET_GAME_LIST;
        }else {
            gamelistUrl= AppUrl.GET_GAME_LIST;
        }
//
        OkGo.<String>post(gamelistUrl)
                .tag(mContext)
                .headers("Authorization",accessToken)//
                .headers("appId",appId)//
                .headers("lang",lang)
                .upRequestBody(body)
                .execute(new JsonCallback<String>(mContext) {
                    @Override
                    public void onSuccess(Response response) {
                        String s1 = response.body().toString();
//                        s1="{\"stateCode\":1,\"resultInfo\":{\"totalCount\":0,\"desc\":\"SUCCESS\",\"itemCount\":0}}";
                        Log.e(TGA,"初始化成功的="+s1);
                        Gson gson = new GsonBuilder()
                                .serializeNulls()
                                .create();
                        HttpBaseResult httpBaseResult = gson.fromJson(s1, HttpBaseResult.class);
                        if (httpBaseResult.getStateCode() == 1) {
                            String s = gson.toJson(httpBaseResult.getResultInfo());
//                            GameListInfoBean resultInfo = gson.fromJson(s, GameListInfoBean.class);
                            try {
                                Log.e(TGA,"gameif="+s);
                                JSONObject jsonObject1 = new JSONObject(s);

                                JSONArray data1 = jsonObject1.getJSONArray("data");
                                if(data1!=null&&data1.length()>0){
                                    gameif.clear();
                                    for (int a=0;a<data1.length();a++){
                                        String o = String.valueOf(data1.get(a));
                                        gameif.add(gson.fromJson(o, GameinfoBean.class));
                                    }
                                    Log.e(TGA,"1V1游戏列表数据="+gameif.size());
                                    String lang1 = listener.getLang();
                                    if (lang1==null||lang1.equals("")){
                                        String local = Locale.getDefault().toString();
                                        lang= Conctart.toStdLang(local);
                                    }else {
                                        lang=lang1;
                                    }
                                    for (int a=0;a<gameif.size();a++){
                                        Log.e(TGA,"1V1游戏列表数据="+gameif.get(a).getName());
                                        String s2 = Conctant.gameName(lang, gameif.get(a).getName());
                                        gameif.get(a).setName(s2);
                                        Log.e(TGA,"1V1游戏列表数据name="+s2);
                                        String s3 = Conctant.gameName(lang, gameif.get(a).getRemark());
                                        gameif.get(a).setRemark(s3);
                                        Log.e(TGA,"1V1游戏列表数据remark="+s3);
                                    }
                                }else {
                                    Log.e(TGA,"data是不是空=空了");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e(TGA,"data是不是空="+e.getMessage());
                            }

                        }else if (httpBaseResult.getStateCode() == -1){
                            getToken(mContext,reToken,0);

                        }
                    }
                    @Override
                    public void onError(Response response) {
                        Log.e(TGA,"1V1游戏列表数据失败="+response.getException().getMessage());

                    }
                });
    }

    // TGASDK获取游戏token
    public static void gameUserLogin(Context mContext, String pkName, UserInFoBean resultInfo, String appId, Gson gson, String env){
        String  fpId = Settings.System.getString(mContext.getContentResolver(), Settings.System.ANDROID_ID);
        String data="{}";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("fpId",fpId);
            jsonObject.put("appId",appId);
            data = jsonObject.toString();
            Log.e(TGA,"参数json"+data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);
        if(env.equals("bip")){
            loginsdkUrl= AppUrl.BIP_GAME_BIP_LOGIN_SDK;
        }else {
            loginsdkUrl= AppUrl.GAME_BIP_LOGIN_SDK;
        }
        OkGo.<String>post(loginsdkUrl)
                .tag(mContext)
                .upRequestBody(body)
                .execute(new JsonCallback<String>(mContext) {
                    @Override
                    public void onSuccess(Response response) {
                        String s1 = response.body().toString();
                        HttpBaseResult httpBaseResult = gson.fromJson(s1, HttpBaseResult.class);
                        if (httpBaseResult.getStateCode() == 1) {
                           TgaSdk.svanTokenInfo(gson.toJson(httpBaseResult.getResultInfo()));
                            Log.e(TGA,"获取1v1游戏列表token");
                            TgaSdk.initCodeTokenInfo(pkName, resultInfo, gson, R.string.packagename, R.string.packagename);
                        }
                    }

                    @Override
                    public void onError(Response response) {
                        Log.e(TGA,"获取1v1游戏列表token失败"+response.getException().getMessage());
                    }
                });
    }

    // TGASDK获取游戏token
    public static void userCodeLogin(Context mContext, String pkName, UserInFoBean resultInfo, Gson gson, String env, TGACallback.TgaEventListener listener, String appId){
        String  fpId = Settings.System.getString(mContext.getContentResolver(), Settings.System.ANDROID_ID);

        String data="{}";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("fpId",fpId);
            if (listener!=null){
                String userInfo = listener.getAuthCode();
                jsonObject.put("code",userInfo);
            }
            data = jsonObject.toString();
            Log.e(TGA,"参数json"+data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);
        if(env.equals("bip")){
            userinfoUrl= AppUrl.BIP_GAME_BIP_CODE_SDK_USER_INFO;
        }else {
            userinfoUrl= AppUrl.GAME_BIP_CODE_SDK_USER_INFO;
        }
//        BipGameUserInfo
        OkGo.<String>post(userinfoUrl)
                .tag(mContext)
                .headers("appId",appId)
                .upRequestBody(body)
                .execute(new JsonCallback<String>(mContext) {
                    @Override
                    public void onSuccess(Response response) {
                        String s1 = response.body().toString();
                        HttpBaseResult httpBaseResult = gson.fromJson(s1, HttpBaseResult.class);
                        if (httpBaseResult.getStateCode() == 1) {

                           TgaSdk.svanTokenInfo(gson.toJson(httpBaseResult.getResultInfo()));

                            TgaSdk.initCodeTokenInfo(pkName, resultInfo, gson, R.string.packagename, R.string.packagename);
//
                        }
                    }

                    @Override
                    public void onError(Response response) {
                        Log.e(TGA,"获取1v1游戏列表token失败"+response.getException().getMessage());
                    }
                });
    }




//    获取fight对战创建ticket
    public static void getFightTicket(Context context, String token,String reToken, String appid, String matchMode, String lang) {
        String data="{}";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("matchMode",matchMode);

            data = jsonObject.toString();
            Log.e(TGA,"参数json"+data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);
        OkGo.<String>post(AppUrl.GAME_FIGHT_TICKET)
                .tag(context)
                .upRequestBody(body)
                .headers("Authorization",token)//
                .headers("appId",appid)//
                .headers("lang",lang)
                .execute(new JsonCallback<String>(context) {
                    @Override
                    public void onSuccess(Response response) {
                        String s1 = response.body().toString();
                        Gson gson = new GsonBuilder()
                                .serializeNulls()
                                .create();
                        HttpBaseResult httpBaseResult = gson.fromJson(s1,HttpBaseResult.class);
                        if (httpBaseResult.getStateCode() == 1) {
                            Log.e("fight对战创建ticket=",gson.toJson(httpBaseResult.getResultInfo()));
                            String s = gson.toJson(httpBaseResult.getResultInfo());
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                ticket = jsonObject.getString("ticket");
                                TGACallback.fightTicketCallback.fightTiketCall(ticket);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else if (httpBaseResult.getStateCode() == -1){
                            getToken(context,reToken,1);

                        }else {
                            TGACallback.fightTicketCallback.fightTiketCall(httpBaseResult.getStateCode()+"");
                        }
                    }

                    @Override
                    public void onError(Response response) {
                        Log.e("加载失败","加载失败="+response.getException().getMessage());
                        TGACallback.fightTicketCallback.fightTiketCall(response.getException().getMessage());

                    }
                });
    }

    // fight对战轮询结果
    public static void fightGetRivalInfo(Context mContext,String token,String reToken,String matchMode,String appid,String lang,String ticket){

        String data="{}";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("matchMode",matchMode);
            jsonObject.put("ticket",ticket);
            data = jsonObject.toString();
            Log.e(TGA,"参数json"+data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);
        OkGo.<String>post(AppUrl.GAME_FIGHT_STATUS)
                .tag(mContext)
                .upRequestBody(body)
                .headers("Authorization",token)//
                .headers("appId",appid)//
                .headers("lang",lang)
                .execute(new JsonCallback<String>(mContext) {
                    @Override
                    public void onSuccess(Response response) {
                        String s1 = response.body().toString();
                        Gson gson = new GsonBuilder()
                                .serializeNulls()
                                .create();
                        HttpBaseResult httpBaseResult = gson.fromJson(s1,HttpBaseResult.class);
                        if (httpBaseResult.getStateCode() == 1) {
                            Log.e("游戏对战=",gson.toJson(httpBaseResult.getResultInfo()));
                            try {
                                JSONObject jsonObject = new JSONObject(gson.toJson(httpBaseResult.getResultInfo()));
                                String data = jsonObject.getString("data");
                                Log.e("游戏对战轮询=", "data="+data);
                                JSONObject jsonObject1 = new JSONObject(data);
                                String matchInfo = jsonObject1.getString("matchInfo");
                                Log.e("游戏对战轮询=", "matchInfo="+matchInfo);
                                JSONObject jsonObject2 = new JSONObject(matchInfo);
                                boolean matched = jsonObject2.getBoolean("matched");
                                Log.e("游戏对战轮询=", gson.toJson(httpBaseResult.getResultInfo()));
                                if (matched) {
                                    TGACallback.fightCallback.fightCall(data);
                                }else {
                                    fightGetRivalInfo(mContext,token,reToken,matchMode,appid,lang,ticket);
                                }
                            } catch (JSONException e) {
                                Log.e("游戏对战异常=", e.getMessage());
                                e.printStackTrace();
                            }
                        }else if (httpBaseResult.getStateCode() == -1){
                            getToken(mContext,reToken,2);

                        }else {
                            TGACallback.fightCallback.fightCall(httpBaseResult.getStateCode()+"");

                        }
                    }

                    @Override
                    public void onError(Response response) {
                        Log.e("加载失败","加载失败="+response.getException().getMessage());
                        TGACallback.fightCallback.fightCall(response.getException().getMessage());

                    }
                });
    }
//获取首页游戏列表
    public static void getGameList(Context context, String appid) {
        String data="{}";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("appId",appid);
            data = jsonObject.toString();
            Log.e(TGA,"参数json"+data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);
        PostRequest<String> post = OkGo.post(AppUrl.GAME_GAME_LIST);
        post.tag(context);
        post.upRequestBody(body);
        post.execute(new JsonCallback<String>(context) {
            @Override
            public void onSuccess(Response response) {

                String s1 = response.body().toString();
                Gson gson = new GsonBuilder()
                        .serializeNulls()
                        .create();
                HttpBaseResult httpBaseResult = gson.fromJson(s1,HttpBaseResult.class);
                if (httpBaseResult.getStateCode() == 1) {

                    TGACallback.homeGameListCallback.homeGameListCallback(gson.toJson(httpBaseResult.getResultInfo()));

                }else {
                    TGACallback.homeGameListCallback.homeGameListCallback(httpBaseResult.getStateCode()+"");

                }
            }

            @Override
            public void onError(Response response) {
                TGACallback.homeGameListCallback.homeGameListCallback(response.getException().getMessage());

            }
        });
    }
//对战游戏创建房间
    public static void getRoomInfo(Context context, String token, String reToken, String gameId, String matchMode, String ticketList, String userIdList, List<UsersinputBean> users, int usercount, String appId, String lang) {

        if(token==null||token.equals("")){
            token=SpUtils.getString(context,"sdkBipToken","");
        }

        String data="{}";
        Gson gson = new Gson();
        String usersString = gson.toJson(users);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("roomId","");
            jsonObject.put("gameId",gameId);
            jsonObject.put("matchMode",matchMode);
            jsonObject.put("usercount",usercount);
            data = jsonObject.toString();
            data=data.replace("}","");
            data=data+",\"ticketList\":"+"\""+ticketList+"\""+",\"userIdList\":"+"\""+userIdList+"\""+",\"users\":"+usersString+"}";
            Log.e("创建房间接口","创建房间接口="+data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (token==null||token.equals("")){
            token = SpUtils.getString(context, "bipToken", "");
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);
        OkGo.<String>post(AppUrl.GAME_CHATROOM)
                .tag(context)
                .headers("Authorization",token)
                .headers("appId",appId)
                .headers("lang",lang)
                .upRequestBody(body)
                .execute(new JsonCallback<String>(context) {
                    @Override
                    public void onSuccess(Response response) {

                        String s1 = response.body().toString();
                        Gson gson = new GsonBuilder()
                                .serializeNulls()
                                .create();
                        HttpBaseResult httpBaseResult = gson.fromJson(s1,HttpBaseResult.class);
                        if (httpBaseResult.getStateCode() == 1) {
                            Log.e("对战=",gson.toJson(httpBaseResult.getResultInfo()));
                            TGACallback.fightGameRoomCallback.fightGameRoomCallback(gson.toJson(httpBaseResult.getResultInfo()));
                        }else if (httpBaseResult.getStateCode() == -1){
                           getToken(context,reToken,3);
                        }else {
                            TGACallback.fightGameRoomCallback.fightGameRoomCallback(httpBaseResult.getStateCode()+"");

                        }
                    }

                    @Override
                    public void onError(Response  response) {
                        TGACallback.fightGameRoomCallback.fightGameRoomCallback(response.getException().getMessage());

                    }
                });
    }

//  获取某个指定游戏信息
    public static void getGameInfo(Context context, String token, String reToken, String id) {
        String data="{}";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token",token);
            data = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);
        OkGo.<String>post(AppUrl.GAME_GET_GAME_INFO+id)
                .tag(context)
                .upRequestBody(body)
                .execute(new JsonCallback<String>(context) {
                    @Override
                    public void onSuccess(Response response) {
                        String s1 = response.body().toString();
                        Gson gson = new GsonBuilder()
                                .serializeNulls()
                                .create();
                        HttpBaseResult httpBaseResult = gson.fromJson(s1,HttpBaseResult.class);
                        if (httpBaseResult.getStateCode() == 1) {
                            TGACallback.fightGameInfoCallback.fightGameInfoCallback(gson.toJson(httpBaseResult.getResultInfo()));
                        }else if (httpBaseResult.getStateCode() == -1){
                            getToken(context,reToken,4);
                        }else {
                            TGACallback.fightGameInfoCallback.fightGameInfoCallback(httpBaseResult.getStateCode()+"");

                        }

                    }

                    @Override
                    public void onError(Response response) {
                        TGACallback.fightGameInfoCallback.fightGameInfoCallback(response.getException().getMessage());

                    }
                });
    }
//对战游戏快速匹配创建
    public static void getStartFight(Context context, String token, String reToken, String appId, String lang, String gameId, String ticket, String matchMode) {
        String data="{}";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("gameId",gameId);
            jsonObject.put("matchMode",matchMode);
            jsonObject.put("ticket",ticket);
            data = jsonObject.toString();
            Log.e(TGA,"参数json"+data+" ticket="+ticket);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);
        OkGo.<String>post(AppUrl.GAME_FIGHT_GAME)
                .tag(context)
                .upRequestBody(body)
                .headers("Authorization",token)//
                .headers("appId",appId)//
                .headers("lang",lang)
                .execute(new JsonCallback<String>(context) {
                    @Override
                    public void onSuccess(Response response) {
                        String s1 = response.body().toString();
                        Gson gson = new GsonBuilder()
                                .serializeNulls()
                                .create();
                        HttpBaseResult httpBaseResult = gson.fromJson(s1,HttpBaseResult.class);
                        if (httpBaseResult.getStateCode() == 1) {
                            Log.e("游戏评分=",gson.toJson(httpBaseResult.getResultInfo()));
                            TGACallback.quickFightGameRoomCallback.quickFightGameRoomCallback(gson.toJson(httpBaseResult.getResultInfo()));
                        }else if (httpBaseResult.getStateCode() == -1){
                            getToken(context,reToken,5);
                        }else {
                            TGACallback.quickFightGameRoomCallback.quickFightGameRoomCallback(httpBaseResult.getStateCode()+"");

                        }
                    }

                    @Override
                    public void onError(Response response) {
                        Log.e("加载失败","加载失败="+response.getException().getMessage());
                        TGACallback.quickFightGameRoomCallback.quickFightGameRoomCallback(response.getException().getMessage());

                    }
                });
    }


//获取对战总战绩
    public static void getGameCombat(Context context, String token, String reToken, String appId, int enemyid, String gameid, String lang, int masterId) {
        if (token==null||token.equals("")){
            token = SpUtils.getString(context, "bipToken", "");
        }
        String data="{}";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("enemyid",enemyid);
//            jsonObject.put("gameid",gameid);
            jsonObject.put("masterId",masterId);
            data = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);
        OkGo.<String>post(AppUrl.GAME_GETCHENGJI)
                .tag(context)
                .headers("Authorization",token)
                .headers("appId",appId)
                .headers("lang",lang)
                .upRequestBody(body)
                .execute(new JsonCallback<String>(context) {
                    @Override
                    public void onSuccess(Response response) {
                        String s1 = response.body().toString();
                        Gson gson = new GsonBuilder()
                                .serializeNulls()
                                .create();
                        HttpBaseResult httpBaseResult = gson.fromJson(s1,HttpBaseResult.class);
                        if (httpBaseResult.getStateCode() == 1) {
                            Log.e("总战绩=",gson.toJson(httpBaseResult.getResultInfo()));
                           TGACallback.fightGameTotalScoreCallback.fightGameTotalScoreCallback(gson.toJson(httpBaseResult.getResultInfo()));
                        }else if (httpBaseResult.getStateCode() == -1){
                            getToken(context,reToken,6);
                        }else {
                            TGACallback.fightGameTotalScoreCallback.fightGameTotalScoreCallback(httpBaseResult.getStateCode()+"");

                        }
                    }

                    @Override
                    public void onError(Response response) {
                        TGACallback.fightGameTotalScoreCallback.fightGameTotalScoreCallback(response.getException().getMessage());

                    }
                });
    }

//获取金币钻石接口
    public static void getMoney(Context context,String appid,String token,String reToken) {
        String data="{}";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("appId",appid);
            jsonObject.put("token",token);
            data = jsonObject.toString();
            Log.e(TGA,"参数json"+data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);
        OkGo.<String>post(AppUrl.GAME_MONEY)
                .tag(context)
                .upRequestBody(body)
                .execute(new JsonCallback<String>(context) {
                    @Override
                    public void onSuccess(Response response) {
                        String s1 = response.body().toString();
                        Gson gson = new GsonBuilder()
                                .serializeNulls()
                                .create();
                        HttpBaseResult httpBaseResult = gson.fromJson(s1,HttpBaseResult.class);
                        if (httpBaseResult.getStateCode() == 1) {
                            Log.e("总战绩=",gson.toJson(httpBaseResult.getResultInfo()));
                            TGACallback.goldDiamondInfoCallback.goldDiamondInfoCallback(gson.toJson(httpBaseResult.getResultInfo()));
                        }else if (httpBaseResult.getStateCode() == -1){
                            getToken(context,reToken,7);
                        }else {
                            TGACallback.goldDiamondInfoCallback.goldDiamondInfoCallback(httpBaseResult.getStateCode()+"");

                        }
                    }
                    @Override
                    public void onError(Response response) {
                        TGACallback.goldDiamondInfoCallback.goldDiamondInfoCallback(response.getException().getMessage());
                    }
                });
    }

}
