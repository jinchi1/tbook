package com.travelbooks3.android.activity.search;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import com.travelbooks3.android.R;
import com.travelbooks3.android.activity.news.adapter.NewsViewPagerAdapter;
import com.travelbooks3.android.activity.search.adapter.AllAdapter;
import com.travelbooks3.android.activity.search.adapter.PlaceAboutAdapter;
import com.travelbooks3.android.activity.search.adapter.TagAboutAdapter;
import com.travelbooks3.android.activity.search.adapter.UserAdapter;
import com.travelbooks3.android.activity.search.adapter.UserDefaultAdapter;
import com.travelbooks3.android.activity.search.data.AllDefaultData;
import com.travelbooks3.android.activity.search.data.PlaceDefaultData;
import com.travelbooks3.android.activity.search.data.PlacePagerData;
import com.travelbooks3.android.activity.search.data.TagDefaultData;
import com.travelbooks3.android.activity.search.data.UserDefaultData;
import com.travelbooks3.android.common.Constants;
import com.travelbooks3.android.http.GFailedHandler;
import com.travelbooks3.android.http.GHttpConstants;
import com.travelbooks3.android.http.GJSONDecoder;
import com.travelbooks3.android.http.GPClient;
import com.travelbooks3.android.http.GResultHandler;
import com.travelbooks3.android.http.GReturnCode;
import com.travelbooks3.android.util.JSONUtil;
import com.travelbooks3.android.util.LogUtil;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import ra.genius.net.GBean;
import ra.genius.net.GExecutor;

import static com.travelbooks3.android.activity.search.UserPagerFragment.list;
import static com.travelbooks3.android.activity.search.TagPagerFragment.list_tag;
import static com.travelbooks3.android.activity.search.PlacePagerFragment.list_place;
import static com.travelbooks3.android.activity.search.AllPagerFragment.list_all;

public class SearchPagerFragment extends Fragment {
    private View view;
    public static final String WORD_PARAM = "word";
    private int _page;
    private String word;
    //private PlacePagerAdapter placeadapter;
    int page_position =0;
    EditText search_txt;

    ArrayList AllDataArr;
    AllAdapter allAdapter;
    ArrayList <UserDefaultData> UserDataArr;
    UserAdapter userAdapter;
    ArrayList TagDataArr;
    TagAboutAdapter tagAboutAdapter;
    ArrayList PlaceDataArr;
    PlaceAboutAdapter placeAboutAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_search_pager,container, false);





            ImageView search_Btn;
            search_Btn=(ImageView)view.findViewById(R.id.search_btn);
            search_txt = (EditText)view.findViewById(R.id.search_txt);



            ViewPager searchViewPager = (ViewPager) view.findViewById(R.id.search_viewPager);
            NewsViewPagerAdapter searchPagerAdapter = new NewsViewPagerAdapter(getChildFragmentManager());
            //searchPagerAdapter.addFragment(new MapPagerFragment(), "지도");
            searchPagerAdapter.addFragment(new AllPagerFragment(), "종합");
            searchPagerAdapter.addFragment(new PlacePagerFragment(), "장소");
            searchPagerAdapter.addFragment(new TagPagerFragment(), "태그");
            searchPagerAdapter.addFragment(new UserPagerFragment(), "사용자");
            searchViewPager.setAdapter(searchPagerAdapter);
            TabLayout tab_lay = (TabLayout) view.findViewById(R.id.search_tabLay);
            tab_lay.setupWithViewPager(searchViewPager);
            searchViewPager.setCurrentItem(0);
            searchViewPager.setOffscreenPageLimit(3);


            searchViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int num, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int num) {

                    page_position=num;
                    LogUtil.i("aaa",String.valueOf(num));
                    /*if(search_txt.getText().toString().length()!=0)
                    {
                        word = search_txt.getText().toString();
                        LogUtil.d("word word word"+word);

                        switch (page_position)
                        {
                            case 0:
                                word = search_txt.getText().toString();

                                AllDataArr = new ArrayList<AllDefaultData>();
                                allAdapter = new AllAdapter(getContext(), AllDataArr);
                                callApi_all();
                                list_all.setAdapter(allAdapter);
                                break;
                            case 1:
                                word = search_txt.getText().toString();
                                PlaceDataArr = new ArrayList<PlaceDefaultData>();
                                placeAboutAdapter = new PlaceAboutAdapter(getContext(), PlaceDataArr);
                                callApi_place_about();

                                list_place.setAdapter(placeAboutAdapter);
                                break;
                            case 2:
                                word = search_txt.getText().toString();
                                TagDataArr = new ArrayList<TagDefaultData>();
                                tagAboutAdapter = new TagAboutAdapter(getContext(), TagDataArr);
                                callApi_tag_about();

                                list_tag.setAdapter(tagAboutAdapter);
                                break;
                            case 3:
                                word = search_txt.getText().toString();
                                UserDataArr = new ArrayList<UserDefaultData>();
                                userAdapter = new UserAdapter(getContext(), UserDataArr);
                                callApi_search_user();

                                list.setAdapter(userAdapter);
                                break;

                        }

                    }*/


                }

                @Override
                public void onPageScrollStateChanged(int state) {



                }
            });

                search_txt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        switch (actionId) {
                            case EditorInfo.IME_ACTION_SEARCH:
                                word = search_txt.getText().toString();
                                LogUtil.d(String.valueOf(page_position));

                                if(page_position==0)
                                {
                                    AllDataArr = new ArrayList<AllDefaultData>();
                                    allAdapter = new AllAdapter(getContext(), AllDataArr);
                                    callApi_all();

                                    list_all.setAdapter(allAdapter);
                                }
                               else if(page_position==1)
                               {
                                   PlaceDataArr = new ArrayList<PlaceDefaultData>();
                                   placeAboutAdapter = new PlaceAboutAdapter(getContext(), PlaceDataArr);

                                   callApi_place_about();
                                   list_place.setAdapter(placeAboutAdapter);

                               }
                               else if(page_position==2)
                               {
                                   TagDataArr = new ArrayList<TagDefaultData>();
                                   tagAboutAdapter = new TagAboutAdapter(getContext(), TagDataArr);

                                   callApi_tag_about();
                                   list_tag.setAdapter(tagAboutAdapter);
                               }
                               else if(page_position==3)
                               {
                                   UserDataArr = new ArrayList<UserDefaultData>();
                                   userAdapter = new UserAdapter(getContext(), UserDataArr);
                                   callApi_search_user();
                                   list.setAdapter(userAdapter);

                               }
                                InputMethodManager keyboard = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                keyboard.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

                                break;
                            default:
                                return false;
                        }
                        return true;
                    }
                });
                search_Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        word = search_txt.getText().toString();
                        if (word!=null) {
                            if (page_position == 0) {
                                AllDataArr = new ArrayList<AllDefaultData>();
                                allAdapter = new AllAdapter(getContext(), AllDataArr);
                                callApi_all();

                                list_all.setAdapter(allAdapter);
                            } else if (page_position == 1) {
                                  PlaceDataArr = new ArrayList<PlaceDefaultData>();
                                placeAboutAdapter = new PlaceAboutAdapter(getContext(), PlaceDataArr);
                                callApi_place_about();

                                list_place.setAdapter(placeAboutAdapter);
                            } else if (page_position == 2) {
                                 TagDataArr = new ArrayList<TagDefaultData>();
                                tagAboutAdapter = new TagAboutAdapter(getContext(), TagDataArr);
                                callApi_tag_about();
                                list_tag.setAdapter(tagAboutAdapter);
                            } else if (page_position == 3) {
                                UserDataArr = new ArrayList<UserDefaultData>();
                                userAdapter = new UserAdapter(getContext(), UserDataArr);
                                callApi_search_user();

                                list.setAdapter(userAdapter);
                            }
                        }
                        else {
                            Toast.makeText(getContext(),"검색어를 입력해 주세요",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            //////////////////////////////////////////////////////////////////////////////////////////////////////

        }
        return view;
   }


    private void callApi_tag_about(){
        GPClient client = new GPClient(getContext());
        client.addProgress();

        client.setUri(Constants.SERVER_URL_API_USER_SEARCH_HASHTAG_WORDLIST);

        client.addParameter("word", word);

        client.setDecoder(new GJSONDecoder(GHttpConstants.TYPE_OBJECT));
        client.addHandler(new GFailedHandler() {
            @Override
            public void failedExecute(GBean bean) {

            }
        });
        client.addHandler( new GResultHandler() {

            @Override
            public GBean result(GBean bean, Object result, int returnCode, String returnMessage, JSONObject responseData) {

                LogUtil.d("returnCode : " + returnCode);
                LogUtil.d("returnMessage : " + returnMessage);


                if(returnCode == GReturnCode.SUCCESS){
                    JSONObject json = (JSONObject) bean.get(GHttpConstants.RESPONSE_BODY);
                    JSONArray jsonArray = JSONUtil.getJSONArray(json, "tags");


                    JSONObject obj;

                    TagDefaultData data = null;

                    try {
                        TagDataArr.clear();

                        for (int i =0; i< jsonArray.length(); i++) {
                            obj = jsonArray.getJSONObject(i);
                            data = TagDefaultData.TagDefaultJson(null, obj);

                            TagDataArr.add(data);

                        }
                        tagAboutAdapter.addItems(TagDataArr);
                        tagAboutAdapter.notifyDataSetChanged();


                        //adapter.addItems(PlaceDefaultDataArr);
                        //adapter.notifyDataSetChanged();


                    } catch (Exception e) {
                        LogUtil.d("list err = "+e.toString());

                    }
                }else if(returnCode == GReturnCode.SEARCH_TAG_NO_DATA){
                    //setNoDataVisibility(true);
                }
                return null;
            }


        });
        GExecutor.getInstance().cancelAndExecute(client);
    }



    private void callApi_search_user(){
        GPClient client = new GPClient(getContext());
        client.addProgress();

        client.setUri(Constants.SERVER_URL_API_USER_SEARCH_USER);

        client.addParameter("word", word);

        client.setDecoder(new GJSONDecoder(GHttpConstants.TYPE_OBJECT));
        client.addHandler(new GFailedHandler() {
            @Override
            public void failedExecute(GBean bean) {

            }
        });
        client.addHandler( new GResultHandler() {

            @Override
            public GBean result(GBean bean, Object result, int returnCode, String returnMessage, JSONObject responseData) {

                LogUtil.d("returnCode : " + returnCode);
                LogUtil.d("returnMessage : " + returnMessage);


                if(returnCode == GReturnCode.SUCCESS){
                    JSONObject json = (JSONObject) bean.get(GHttpConstants.RESPONSE_BODY);
                    JSONArray jsonArray = JSONUtil.getJSONArray(json, "userinfos");

                    JSONObject obj;

                    UserDefaultData data = null;

                    try {
                        UserDataArr.clear();

                        for (int i =0; i< jsonArray.length(); i++) {
                            obj = jsonArray.getJSONObject(i);
                            data = UserDefaultData.UserDefaultJson(null, obj);

                            UserDataArr.add(data);

                        }
                        userAdapter.addItems(UserDataArr);
                        userAdapter.notifyDataSetChanged();

                        //adapter.addItems(PlaceDefaultDataArr);
                        //adapter.notifyDataSetChanged();


                    } catch (Exception e) {
                        LogUtil.d("list err = "+e.toString());

                    }
                }else if(returnCode == GReturnCode.SEARCH_USER_NO_DATA){
                    //setNoDataVisibility(true);
                }
                return null;
            }


        });
        GExecutor.getInstance().cancelAndExecute(client);
    }

    private void callApi_place_about(){
        GPClient client = new GPClient(getContext());
        client.addProgress();

        client.setUri(Constants.SERVER_URL_API_USER_SEARCH_PLACE_WORDLIST);

        client.addParameter("word", word);

        client.setDecoder(new GJSONDecoder(GHttpConstants.TYPE_OBJECT));
        client.addHandler(new GFailedHandler() {
            @Override
            public void failedExecute(GBean bean) {

            }
        });
        client.addHandler( new GResultHandler() {

            @Override
            public GBean result(GBean bean, Object result, int returnCode, String returnMessage, JSONObject responseData) {

                LogUtil.d("returnCode : " + returnCode);
                LogUtil.d("returnMessage : " + returnMessage);


                if(returnCode == GReturnCode.SUCCESS){
                    JSONObject json = (JSONObject) bean.get(GHttpConstants.RESPONSE_BODY);
                    JSONArray jsonArray = JSONUtil.getJSONArray(json, "tripPhoto");


                    JSONObject obj;

                    PlaceDefaultData data = null;

                    try {
                        PlaceDataArr.clear();

                        for (int i =0; i< jsonArray.length(); i++) {
                            obj = jsonArray.getJSONObject(i);
                            data = PlaceDefaultData.PlaceDefaultJson(null, obj);

                            PlaceDataArr.add(data);

                        }
                        placeAboutAdapter.addItems(PlaceDataArr);
                        placeAboutAdapter.notifyDataSetChanged();


                        //adapter.addItems(PlaceDefaultDataArr);
                        //adapter.notifyDataSetChanged();


                    } catch (Exception e) {
                        LogUtil.d("list err = "+e.toString());

                    }
                }else if(returnCode == GReturnCode.SEARCH_PLACE_ABOUT_NO_DATA){
                    //setNoDataVisibility(true);
                }
                return null;
            }


        });
        GExecutor.getInstance().cancelAndExecute(client);
    }


    private void callApi_all(){
        GPClient client = new GPClient(getContext());
        client.addProgress();

        client.setUri(Constants.SERVER_URL_API_USER_SEARCH);

        client.addParameter("word", word);

        client.setDecoder(new GJSONDecoder(GHttpConstants.TYPE_OBJECT));
        client.addHandler(new GFailedHandler() {
            @Override
            public void failedExecute(GBean bean) {

            }
        });
        client.addHandler( new GResultHandler() {

            @Override
            public GBean result(GBean bean, Object result, int returnCode, String returnMessage, JSONObject responseData) {

                LogUtil.d("returnCode : " + returnCode);
                LogUtil.d("returnMessage : " + returnMessage);


                if(returnCode == GReturnCode.SUCCESS){
                    JSONObject json = (JSONObject) bean.get(GHttpConstants.RESPONSE_BODY);
                    JSONArray jsonArray = JSONUtil.getJSONArray(json, "searchHistorys");


                    JSONObject obj;

                    AllDefaultData data = null;

                    try {
                        AllDataArr.clear();

                        for (int i =0; i< jsonArray.length(); i++) {
                            obj = jsonArray.getJSONObject(i);
                            data = AllDefaultData.AllDefaultJson(null, obj);

                            AllDataArr.add(data);

                        }
                        allAdapter.addItems(AllDataArr);
                        allAdapter.notifyDataSetChanged();


                        //adapter.addItems(PlaceDefaultDataArr);
                        //adapter.notifyDataSetChanged();


                    } catch (Exception e) {
                        LogUtil.d("list err = "+e.toString());

                    }
                }else if(returnCode == 250){
                    //setNoDataVisibility(true);
                }
                return null;
            }


        });
        GExecutor.getInstance().cancelAndExecute(client);
    }

}