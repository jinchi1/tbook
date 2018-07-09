package com.travelbooks3.android.activity.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.travelbooks3.android.R;
import com.travelbooks3.android.TravelbookApp;
import com.travelbooks3.android.activity.info.adapter.FollowingListAdapter;
import com.travelbooks3.android.activity.info.model.FollowingListData;
import com.travelbooks3.android.http.GFailedHandler;
import com.travelbooks3.android.http.GHttpConstants;
import com.travelbooks3.android.http.GJSONDecoder;
import com.travelbooks3.android.http.GPClient;
import com.travelbooks3.android.http.GResultHandler;
import com.travelbooks3.android.http.GReturnCode;
import com.travelbooks3.android.util.JSONUtil;
import com.travelbooks3.android.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ra.genius.net.GBean;
import ra.genius.net.GExecutor;

import static com.travelbooks3.android.common.Constants.SERVER_URL;

/**
 * Created by system777 on 2017-10-27.
 */

public class LikeListFragment extends Fragment {
    ListView listView;
    private View view;
    ArrayList<FollowingListData> followingDataArr;
    FollowingListAdapter adapter;
    String trip_uid;
    int page =1;
    TextView title;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        trip_uid = getArguments().getString("trip_uid");

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_follower_list, container, false);
            listView = (ListView) view.findViewById(R.id.following_list);
            title = (TextView)view.findViewById(R.id.title_txt);
            followingDataArr = new ArrayList<FollowingListData>();
            adapter = new FollowingListAdapter(getContext(), followingDataArr);
            listView.setAdapter(adapter);

            listView.setOnScrollListener(new AbsListView.OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    if (scrollState == 0 && view.getCount() > 0 && view.getLastVisiblePosition() >= view.getCount() - 1) {
                        loadNextPage();
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                }

            });

            followingDataArr.clear();
            callApi_liker_list(trip_uid);
        }

        title.setText("좋아요");

        return view;
    }




    private void callApi_liker_list(String trip_uid){
        GPClient client = new GPClient(getContext());
        client.addProgress();

        client.addParameter("viewCount", 30);
        client.addParameter("like_type","L");
        client.addParameter("page",page);
        if(page==1)
        {
            followingDataArr.clear();
            adapter.notifyDataSetChanged();
        }


        client.setUri(SERVER_URL +"/user/like/"+trip_uid+"/list");

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
                    JSONArray jsonArray = JSONUtil.getJSONArray(json, "likes");


                    JSONObject obj;

                    FollowingListData data = null;

                    try {


                        for (int i =0; i< jsonArray.length(); i++) {
                            obj = jsonArray.getJSONObject(i);
                            data = FollowingListData.FollowingListJson(null, obj);

                            followingDataArr.add(data);

                        }
                        adapter.addItems(followingDataArr);
                        adapter.notifyDataSetChanged();

                        //adapter.addItems(PlaceDefaultDataArr);
                        //adapter.notifyDataSetChanged();


                    } catch (Exception e) {
                        LogUtil.d("list err = "+e.toString());

                    }
                }else if(returnCode == GReturnCode.SEARCH_PLACE_NO_DATA){

                }
                return null;
            }


        });
        GExecutor.getInstance().cancelAndExecute(client);
    }

   /* @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser)
        {
            callApi_following_list(user_idx);
        }
        else
        {

        }
    }*/

    private void loadNextPage() {

        page++;
        callApi_liker_list(trip_uid);

    }



}