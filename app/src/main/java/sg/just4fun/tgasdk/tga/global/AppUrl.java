package sg.just4fun.tgasdk.tga.global;

public class AppUrl {

    public static final String TGA_SDK_INFO=Global.TEST_HOST_GOOGLE_PAY_URL_NEW+"tgampApp/AppNativeInit";//获取用户配置表
    public static final String GET_GOOGLEPAY_INFO=Global.TEST_HOST_GOOGLE_PAY_URL_NEW+"googlePay/GetWareList";//获取googlepay配置表
    public static final String GET_GOOGLEPAY_RESULT=Global.TEST_HOST_GOOGLE_PAY_URL_NEW+"googlePay/PayFinish";//googlepay支付结果
    public static final String GET_GAME_LIST=Global.TEST_HOST_GOOGLE_PAY_URL_NEW+"tgadpGame/GameListByTag";//获取对战优秀列表
    public static final String GAME_BIP_LOGIN_SDK=Global.TEST_HOST_GOOGLE_PAY_URL_NEW+"tgadpUser/TPLogin";//bip登录获取用户信息
    public static final String GAME_BIP_CODE_SDK_USER_INFO=Global.TEST_HOST_GOOGLE_PAY_URL_NEW+"tgadpUser/TPLoginByCode";//bip登录获取用户信息
    public static final String GAME_REFRESHTOKEN=Global.TEST_HOST_GOOGLE_PAY_URL_NEW+"tgadpUser/RefreshToken";//获取刷新后的token
    public static final String GAME_GAME_LIST=Global.TEST_HOST_GOOGLE_PAY_URL_NEW+"tgadpGame/ThirdGameList";//bip游戏中心列表
    public static final String GAME_CHATROOM=Global.TEST_HOST_GOOGLE_BIP_PAY_URL_NEW+"BPlat/createRoom";//创建对战游戏房间
    public static final String GAME_GET_GAME_INFO=Global.TEST_HOST_GOOGLE_PAY_URL_NEW+"tgadpGame/GameStart?gameid=";//获取游戏信息
    public static final String GAME_MONEY=Global.TEST_HOST_GOOGLE_PAY_URL_NEW+"tgadpPayment/Coin";//bip查询货币余额

    public static final String GAME_FIGHT_STATUS=Global.TEST_HOST_GOOGLE_BIP_PAY_URL_NEW+"checkSeasonMatchStatus";//快速游戏轮询获取对手
    public static final String GAME_FIGHT_TICKET=Global.TEST_HOST_GOOGLE_BIP_PAY_URL_NEW+"startSeasonMatch";//fight对战创建ticket
    public static final String GAME_FIGHT_GAME=Global.TEST_HOST_GOOGLE_BIP_PAY_URL_NEW+"startSeasonFight";//fight对战开始游戏
    public static final String GAME_GETCHENGJI=Global.TEST_HOST_GOOGLE_BIP_PAY_URL_NEW+"BPlat/queryUserGameRecord";//获取游戏总战绩


    public static final String BIP_TGA_SDK_INFO=Global.BIP_TEST_HOST_GOOGLE_PAY_URL_NEW+"tgampApp/AppNativeInit";//获取用户配置表
    public static final String BIP_GET_GOOGLEPAY_INFO=Global.BIP_TEST_HOST_GOOGLE_PAY_URL_NEW+"googlePay/GetWareList";//获取googlepay配置表
    public static final String BIP_GET_GOOGLEPAY_RESULT=Global.BIP_TEST_HOST_GOOGLE_PAY_URL_NEW+"googlePay/PayFinish";//googlepay支付结果
    public static final String BIP_GET_GAME_LIST=Global.BIP_TEST_HOST_GOOGLE_PAY_URL_NEW+"tgadpGame/GameListByTag";//获取对战优秀列表
    public static final String BIP_GAME_BIP_LOGIN_SDK=Global.BIP_TEST_HOST_GOOGLE_PAY_URL_NEW+"tgadpUser/TPLogin";//bip登录获取用户信息
    public static final String BIP_GAME_BIP_CODE_SDK_USER_INFO=Global.BIP_TEST_HOST_GOOGLE_PAY_URL_NEW+"tgadpUser/TPLoginByCode";//bip登录获取用户信息
    public static final String BIP_GAME_REFRESHTOKEN=Global.BIP_TEST_HOST_GOOGLE_PAY_URL_NEW+"tgadpUser/RefreshToken";//获取刷新后的token

    public static final String BIP_GAME_FIGHT_STATUS=Global.BIP_TEST_HOST_GOOGLE_BIP_PAY_URL_NEW+"checkSeasonMatchStatus";//快速游戏轮询获取对手
    public static final String BIP_GAME_FIGHT_TICKET=Global.BIP_TEST_HOST_GOOGLE_BIP_PAY_URL_NEW+"startSeasonMatch";//fight对战创建ticket
    public static final String BIP_GAME_FIGHT_GAME=Global.BIP_TEST_HOST_GOOGLE_BIP_PAY_URL_NEW+"startSeasonFight";//fight对战开始游戏



}
