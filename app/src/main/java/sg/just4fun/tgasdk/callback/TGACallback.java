package sg.just4fun.tgasdk.callback;


import android.content.Context;

import sg.just4fun.tgasdk.modle.UserInFoBean;

public class TGACallback {
    public static ShareCallback listener;
    public static LangCallback langListener;
    public static FightGameCallback fightGameListener;
    public static CodeCallback codeCallback;
    public static OutLoginCallback outLoginCallback;
    public static FightCallback fightCallback;
    public static FightTicketCallback fightTicketCallback;
    public static HomeGameListCallback homeGameListCallback;
    public static FightGameRoomCallback fightGameRoomCallback;
    public static FightGameInfoCallback fightGameInfoCallback;
    public static QuickFightGameRoomCallback quickFightGameRoomCallback;
    public static FightGameTotalScoreCallback fightGameTotalScoreCallback;
    public static GoldDiamondInfoCallback goldDiamondInfoCallback;


    public interface initCallback {
        void initSucceed();

        void initError(String error);
    }

    public interface TgaEventListener {
        default void quitLogin(Context context) {

        }

        String getAuthCode();

        void onInAppShare(Context context, String uuid, String iconUrl, String link, String title, String type);
//      void onInAppPay(Context context);

        default void goMyFragemnt(Context context) {

        }

        String getLang();


    }


    public interface LangCallback {//获取语言回调
        void getLang(String lang);
    }

    public static void setLangCallback(LangCallback langListener) {
        TGACallback.langListener = langListener;
    }


    public interface ShareCallback {//分享回调
        void shareCall(String uuid, boolean success);
    }

    public static void setShareCallback(ShareCallback listener) {
        TGACallback.listener = listener;
    }


    public interface FightGameCallback {//对战游戏回调
        void fightGameCall();
    }

    public static void setFightGameCallback(FightGameCallback fightGameListener) {
        TGACallback.fightGameListener = fightGameListener;
    }


    public interface GameCenterCallback{
        void onGameCenterClosed();//关闭h5页回调
        void openUserLogin(String uuid);//调起app登录页
    }


    public interface OutLoginCallback {//退出登录
        void outLoginCall();
    }
    public static void setOutLoginCallback(OutLoginCallback outLoginCallback) {
        TGACallback.outLoginCallback = outLoginCallback;
    }


    public interface CodeCallback {//获取app传过来的code
        void codeCall(String uuid,String code);
    }
    public static void setCodeCallback(CodeCallback codeCallback) {
        TGACallback.codeCallback = codeCallback;
    }

    public interface FightCallback {//fight对战轮询接口
        void fightCall(String bean);
    }
    public static void setFightCallback(FightCallback fightCallback) {
        TGACallback.fightCallback = fightCallback;
    }
    public interface FightTicketCallback {//fight对战ticket
        void fightTiketCall(String bean);
    }
    public static void setFightTicketCallback(FightTicketCallback fightTicketCallback) {
        TGACallback.fightTicketCallback = fightTicketCallback;
    }

    public interface HomeGameListCallback {//首页游戏列表数据回调
        void homeGameListCallback(String bean);
    }
    public static void setHomeGameListCallback(HomeGameListCallback homeGameListCallback) {
        TGACallback.homeGameListCallback = homeGameListCallback;
    }

    public interface FightGameRoomCallback {//对战游戏创建房间数据回调
        void fightGameRoomCallback(String bean);
    }
    public static void setFightGameRoomCallback(FightGameRoomCallback fightGameRoomCallback) {
        TGACallback.fightGameRoomCallback = fightGameRoomCallback;
    }

    public interface FightGameInfoCallback {//获取某一款游戏信息
         void fightGameInfoCallback(String bean);
    }
    public static void setFightGameInfoCallback(FightGameInfoCallback fightGameInfoCallback) {
        TGACallback.fightGameInfoCallback = fightGameInfoCallback;
    }

    public interface QuickFightGameRoomCallback {//快速匹配游戏创建房间回调
        void quickFightGameRoomCallback(String bean);
    }
    public static void setQuickFightGameRoomCallback(QuickFightGameRoomCallback quickFightGameRoomCallback) {
        TGACallback.quickFightGameRoomCallback = quickFightGameRoomCallback;
    }

    public interface FightGameTotalScoreCallback {//获取总战绩回调
        void fightGameTotalScoreCallback(String bean);
    }
    public static void setFightGameTotalScoreCallback(FightGameTotalScoreCallback fightGameTotalScoreCallback) {
        TGACallback.fightGameTotalScoreCallback = fightGameTotalScoreCallback;
    }


    public interface GoldDiamondInfoCallback {//获取金币钻石信息回调
        void goldDiamondInfoCallback(String bean);
    }
    public static void setGoldDiamondInfoCallback(GoldDiamondInfoCallback goldDiamondInfoCallback) {
        TGACallback.goldDiamondInfoCallback = goldDiamondInfoCallback;
    }

}



