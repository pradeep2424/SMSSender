package com.smser.smssender.comman;

public interface Constants {

    String BASE_URL = "http://80.241.213.202:8091/Service1.svc/";

    String LASTOPTION = "LastOption";
    String FIRSTSWITCH = "first";
    String SECONDSWITCH = "second";
    String THIRDSWITCH = "third";
    String WHATSAPPSWITCH = "whatsApp";
    String  WHATSAPPS_TEMPLATE_IMAGE = "whatsAppTemplateImage";
    String USERID = "userId";
    String PASSWORD = "password";
    String SENDERID = "senderId";

    String MSGLIMITER = "msgLimiter";
    String ENABLE = "enable";
    String DISABLE = "disable";
    String PURCHASE = "purchased";
    String CALLLIMIT = "callLimit";

    String PERMISSION = "askPermission";
    String WEBSMS = "webSmsService";
    String APPPOWER = "appPower";

    String SMSMETHOD = "smsSendingMethod";

    String ONLYSMS = "smsOnly";
    String SMSWHATSAPP = "smsWhatsApp";
    String WHATSAPP = "whatsAppOnly";

    String TOTALSENT = "totalSent";
    String BALANCE = "balanceMsg";
    String SMSTOTAL = "smsTotal";
    String SMSDAILYTOTAL = "smsDailyTotal";
    String WHATSTOTAL = "whatsAppTotal";
    String WHATSDAILYTOTAL = "whatsAppDailyTotal";
    String DAILY = "dailyDate";

    String ELEMENATOR1 = "-";
    String ELEMENATOR2 = " ";
    String ELEMENATOR3 = "";

    String INCOMMING = "incoming";
    String OUTGOING = "outgoing";
    String MISSEDCALL = "missedCall";

    String WHATSAPPSMS = "whatsAppSms";
    String WHATSAPPIMG = "whatsAppImg";
    String WHATSAPPVIDEO = "whatsAppVideo";
    String WHATSAPPMETHOD = "whatsAppMethod";
    String WHATSAPPIMGPATH = "whatsAppImgPath";
    String WHATSAPPVIDEOPATH = "whatsAppVideoPath";
    String IMGPATH = "imgPath";
    String VIDEOPATH = "videoPath";

    String IMGTEXT = "Image selected";
    String VIDEOTEXT = "Video selected";

    String REGISTER = "registerStatus";
    String USERNAME = "userName";
    String UID = "uId";
    String EMAILID = "emailId";
    String MOBILE = "mobileNumber";
    String COMPANY = "companyName";
    String CITY = "cityState";
    String AGENTCODE = "agentCode";
    String TOKEN = "token";
    String EXPIRY = "expiryDate";
    String TOLLFREE = "tollFree";

    String TRUE = "True";
    String ISACTIVE = "isActive";
    String ISADMIN = "isAdmin";
    String ADMINCODE = "adminCode";

    String PERMISSION_STATUS = "com.smser.smssender.service.CallerService";
    String MAIN_ACTION = "com.marothiatechs.foregroundservice.action.main";
//    String INIT_ACTION = "com.marothiatechs.foregroundservice.action.init";
    String PREV_ACTION = "com.marothiatechs.foregroundservice.action.prev";
    String PLAY_ACTION = "com.marothiatechs.foregroundservice.action.play";
    String NEXT_ACTION = "com.marothiatechs.foregroundservice.action.next";
    String STARTFOREGROUND_ACTION = "com.marothiatechs.foregroundservice.action.startforeground";
    String STOPFOREGROUND_ACTION = "com.marothiatechs.foregroundservice.action.stopforeground";
    int FOREGROUND_SERVICE = 101;
}
