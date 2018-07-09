package com.travelbooks3.android.common;

import com.travelbooks3.android.BuildConfig;

/**
 * Created by system777 on 2017-06-22.
 */

public class Constants {
    public static final int VIEW_COUNT                                  = 12; // 게시물 개수
    public static final int VIEW_COUNT_BIG                              = 30;

    //// 실서버
    //public static String        SERVER_URL                                 = "http://www.travelbooks2.com";
    //public static String        SERVER_IMG_URL                                 = "http://www.travelbooks2.com";



    //public static String        SERVER_URL                                 = BuildConfig.SERVER_URL;
    //public static String        SERVER_IMG_URL                             = BuildConfig.SERVER_IMG_URL;


    //public static String        SERVER_URL                                  ="http://jhgj1292.cafe24.com";  // test
    /*public static String        SERVER_IMG_URL                              ="http://192.168.1.3:8080";  // localtest
    public static String        SERVER_URL                                  ="http://192.168.1.3:8080"; // localtest
   *//*public static String        SERVER_IMG_URL                              ="http://13.125.48.63";  // amazon
   public static String        SERVER_URL                                  ="http://13.125.48.63"; // amazon*/
/*
   public static String        SERVER_IMG_URL                              ="http://110.10.189.66:8080";  // new cafe24
   public static String        SERVER_URL                                  ="http://110.10.189.66:8080"; // new cafe24
*/

 public static String        SERVER_IMG_URL                              ="http://www.e-travelbook.com";  // new cafe24 domain
 public static String        SERVER_URL                                  ="http://www.e-travelbook.com"; // new cafe24 domain

 public static final String  MARKET_URL                                 = "market://details?id=com.travelbooks3.android";


    public static String        SERVER_URL_API_LOGIN                       = SERVER_URL + "/login";
    public static String        SERVER_URL_API_REGISTER                    = SERVER_URL + "/register";

    public static String        SERVER_URL_API_MAIN_INDEX                  = SERVER_URL + "/main/index";

    public static String        SERVER_URL_API_MAIN_CONTINENT_MAPPING      = SERVER_URL + "/main/continent/mapping";

    public static String        SERVER_URL_API_MAIN_SEARCH_FRIEND          = SERVER_URL + "/main/search/friend";
    /**
     * 대륙_국가별 게시물 조회
     */
    public static String        SERVER_URL_API_USER_SEARCH_CONTINENT       = SERVER_URL + "/user/search/continent";

    public static String        SERVER_URL_API_MYPAGE_ADD_PHONE            = SERVER_URL + "/mypage/addPhone";

    public static String        SERVER_URL_API_MYPAGE_DETAIL_USER          = SERVER_URL + "/mypage/detailUser";

    public static String        SERVER_URL_API_MYPAGE_USER_INFO            = SERVER_URL + "/mypage/userinfo";
    /**
     * 임시 비밀번호 발급
     */
    public static String        SERVER_URL_API_ISSUE_TEMPORARY_PWD         = SERVER_URL + "/issue/temporaryPwd";

    public static String        SERVER_URL_API_APP_VERSION                 = SERVER_URL + "/appversion";

    public static String        SERVER_URL_API_USER_FOLLOW_FOLLOWER_LIST   = SERVER_URL + "/user/follow/follower/{user_idx}/list";

    public static String        SERVER_URL_API_USER_FOLLOW_FOLLOWING_LIST  = SERVER_URL + "/user/follow/following/{user_idx}/list";

    public static String        SERVER_URL_API_USER_FOLLOW_DELETE          = SERVER_URL + "/user/follow/{idx}";

    public static String        SERVER_URL_API_USER_FOLLOW                 = SERVER_URL + "/user/follow/";

    public static String        SERVER_URL_API_USER_FOLLOW_ALL             = SERVER_URL + "/user/follow/all";

    public static String        SERVER_URL_API_USER_FOLLOW_FACEBOOK        = SERVER_URL + "/user/follow/facebook";

    public static String        SERVER_URL_API_USER_LIKE                   = SERVER_URL + "/user/like";

    public static String        SERVER_URL_API_USER_LIKE_DELETE            = SERVER_URL + "/user/like/";

    public static String        SERVER_URL_API_USER_LIKE_UPDATE            = SERVER_URL + "/user/like/update/";


    public static String        SERVER_URL_API_USER_LIKE_LIST              = SERVER_URL + "/user/like/list";

    public static String        SERVER_URL_API_USER_FOLLOWER_NOTI          = SERVER_URL + "/user/follower/noti";

    public static String        SERVER_URL_API_USER_MY_NOTI                = SERVER_URL + "/user/my/noti";

    public static String        SERVER_URL_API_USER_SEARCH_DEFAULT         = SERVER_URL + "/user/search/default";

    public static String        SERVER_URL_API_USER_SEARCH                 = SERVER_URL + "/user/search";

    public static String        SERVER_URL_API_USER_SEARCH_USER             = SERVER_URL + "/user/search/user";

    public static String        SERVER_URL_API_USER_SEARCH_HASHTAG         = SERVER_URL + "/user/search/hashtag";


    public static String        SERVER_URL_API_USER_SEARCH_HASHTAG_WORDLIST = SERVER_URL+"/user/search/hashtag/wordlist";

    public static String        SERVER_URL_API_USER_SEARCH_HASHTAG_DETAIL  = SERVER_URL + "/user/search/hashtag/detail";

    public static String        SERVER_URL_API_USER_SEARCH_PLACE           = SERVER_URL + "/user/search/place";

    public static String        SERVER_URL_API_USER_SEARCH_PLACE_WORDLIST = SERVER_URL+"/user/search/place/wordlist";

    public static String        SERVER_URL_API_USER_SEARCH_NEAR_CURRENT    = SERVER_URL + "/user/search/nearcurrent";

    public static String        SERVER_URL_API_USER_INFO                   = SERVER_URL + "/user/{search_user_idx}/detail";

    public static String        SERVER_URL_API_BLOCKED_USERS_LIST          = SERVER_URL + "/user/block/list";

    public static String        SERVER_URL_API_BLOCKED_USERS_DELETE        = SERVER_URL + "/user/block";

    public static String        SERVER_URL_API_MYPAGE_UPDATE_PWD           = SERVER_URL + "/mypage/updatePassword";

    public static String        SERVER_URL_API_MYPAGE_UPDATE_PHOTO         = SERVER_URL + "/mypage/updatePhoto";


    public static String        SERVER_URL_API_MYPAGE_DELETE_PROFILE_PHOTO = SERVER_URL + "/mypage/deleteProfilePhoto";

    public static String        SERVER_URL_API_MYPAGE_BADWORD_LIST         = SERVER_URL + "/mypage/badword/list";

    public static String        SERVER_URL_API_MYPAGE_BAD_USER             = SERVER_URL + "/mypage/baduser";

    public static String        SERVER_URL_API_DELETE_BOOKMARK             = SERVER_URL + "/trip/bookmark/{idx}";

    public static String        SERVER_URL_API_TRIP_CREATE_PHOTO           = SERVER_URL + "/trip/createPhoto";

    public static String        SERVER_URL_API_TRIP_DELETE_PHOTO           = SERVER_URL + "/trip/tripinfo/photo/{idx}";

    public static String        SERVER_URL_API_TRIP_ADD_PHOTO              = SERVER_URL + "/trip/{trip_uid}/createPhoto";

    public static String        SERVER_URL_API_TRIP_CREATE                 = SERVER_URL + "/trip/tripinfo";

    public static String        SERVER_URL_API_TRIP_DELETE                 = SERVER_URL + "/trip/tripinfo/";

    public static String        SERVER_URL_API_TRIP_UPDATE                 = SERVER_URL + "/trip/tripinfo/{trip_uid}/update";

    public static String        SERVER_URL_API_COMMENT_SELECT              = SERVER_URL + "/trip/selectComment";

    public static String        SERVER_URL_API_COMMENT                     = SERVER_URL + "/trip/comment";

    public static String        SERVER_URL_API_COMMENT_DELETE              = SERVER_URL + "/trip/comment/{idx}";

    public static String        SERVER_URL_API_BOOKMARK_LIST               = SERVER_URL + "/trip/bookmark/list";

    public static String        SERVER_URL_API_BOOKMARK_LIST_USER          = SERVER_URL + "/trip/bookmark/{user_idx}/list";

    public static String        SERVER_URL_API_BOOKMARK                    = SERVER_URL + "/trip/bookmark";

    public static String        SERVER_URL_API_BOOKMARK_DELETE             = SERVER_URL + "/trip/bookmark/";

    public static String        SERVER_URL_API_USER_LIKE_TRIP_LIST         = SERVER_URL + "/user/like/{trip_uid}/list";

    public static String        SERVER_URL_API_POST_MAP_LIST               = SERVER_URL + "/mypage/posting/{user_idx}/list";

    public static String        SERVER_URL_API_BOOKMARK_USER_LIST          = SERVER_URL + "/trip/bookmark_userlist/{trip_uid}";

    public static String        SERVER_URL_API_COMMENT_LIKE                = SERVER_URL + "/trip/commentLike";

    public static String        SERVER_URL_API_COMMENT_LIKE_DELETE         = SERVER_URL + "/trip/commentLike/{idx}";

    public static String        SERVER_URL_API_MYPAGE_DELETE_SEARCH        = SERVER_URL + "/mypage/search/deleteHistory";

    public static String        SERVER_URL_API_MYPAGE_PUSH_INFO            = SERVER_URL + "/mypage/push";

    public static String        SERVER_URL_API_MYPAGE_PUSH_MODIFY            = SERVER_URL + "/mypage/updatePush";

    public static String        SERVER_URL_API_MYPAGE_EXPIRE        = SERVER_URL + "/mypage/expire";
    /*
     * 플러스친구
     */
    public static String        SERVER_URL_API_ADVERTISE_LIST              = SERVER_URL + "/advertise/list";

    public static String        SERVER_URL_API_ADVERTISE_FOLLOW            = SERVER_URL + "/advertise/follow/{ad_idx}";

    public static String        SERVER_URL_API_ADVERTISE_FOLLOW_DELETE     = SERVER_URL + "/advertise/follow/{ad_idx}";

    public static String        SERVER_URL_API_ADVERTISE_AD_EVENT          = SERVER_URL + "/advertise/ad/event";

    public static String        SERVER_URL_MYPAGE_EXPIRE                   = SERVER_URL + "/mypage/expire/";

    ///
    public static final String  EXTRAS_MESSAGE                             = "EXTRAS_MESSAGE";

    public static final String  EXTRAS_URL                                 = "EXTRAS_URL";
    public static final String  EXTRAS_TITLE                               = "EXTRAS_TITLE";

    public static final String  EXTRAS_ID                                  = "EXTRAS_ID";

    public static final String  EXTRAS_NO                                  = "EXTRAS_NO";
    public static final String  EXTRAS_COUNT                               = "EXTRAS_COUNT";
    public static final String  EXTRAS_LIST_POSITION                       = "EXTRAS_LIST_POSITION";
    public static final String  EXTRAS_TYPE                                = "EXTRAS_TYPE";
    public static final String  EXTRAS_POSITION                            = "EXTRAS_POSITION";

    public static final String  EXTRAS_DATA                                = "EXTRAS_DATA";
    public static final String  EXTRAS_JSON_STRING                         = "EXTRAS_JSON_STRING";
    public static final String  EXTRAS_JSON_STRING2                        = "EXTRAS_JSON_STRING2";
    public static final String  EXTRAS_JSON_STRING3                        = "EXTRAS_JSON_STRING3";


    public static final String  EXTRAS_JSON_ARRAY_STRING                   = "EXTRAS_JSON_ARRAY_STRING";

    public static final String  EXTRAS_CONTINENT                           = "EXTRAS_CONTINENT";

    public static final String  LANG_KOR                                   = "kor";
    public static final String  LANG_ENG                                   = "eng";
    public static final String  LANG_CHG1                                  = "chg1";
    public static final String  LANG_CHG2                                  = "chg2";
    public static final String  LANG_ESP                                   = "esp";
    public static final String  LANG_RUS                                   = "pyc";
    public static final String  LANG_JPN                                   = "jpn";
    public static final String  LANG_DEU                                   = "deu";
    public static final String  LANG_FRN                                   = "frn";

    //intent code
    public static final int REQUEST_CODE_MEMBERSHIP                      = 100; //회원가입 페이지 이동
    public static final int REQUEST_CODE_FIND_PASSWORD                   = 101;//비밀번호 찾기 페이지
}
