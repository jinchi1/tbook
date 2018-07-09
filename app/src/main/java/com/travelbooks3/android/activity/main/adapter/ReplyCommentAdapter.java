package com.travelbooks3.android.activity.main.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.travelbooks3.android.R;
import com.travelbooks3.android.activity.BaseActivity;
import com.travelbooks3.android.activity.main.model.ReplyCommentData;
import com.travelbooks3.android.activity.search.SearchTagPostFragment;
import com.travelbooks3.android.common.Constants;
import com.travelbooks3.android.http.GFailedHandler;
import com.travelbooks3.android.http.GHttpConstants;
import com.travelbooks3.android.http.GJSONDecoder;
import com.travelbooks3.android.http.GPClient;
import com.travelbooks3.android.http.GResultHandler;
import com.travelbooks3.android.http.GReturnCode;
import com.travelbooks3.android.util.HashTag;
import com.travelbooks3.android.util.JSONUtil;
import com.travelbooks3.android.util.LogUtil;
import com.travelbooks3.android.util.SPUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ra.genius.net.GBean;
import ra.genius.net.GExecutor;
import ra.genius.net.GHttpMethod;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.travelbooks3.android.util.FormatUtil.getReplyDateFormat;

/**
 * Created by system777 on 2017-10-19.
 */

public class ReplyCommentAdapter extends BaseAdapter {


    private Context context;
    private ArrayList<ReplyCommentData> dataArr;
    String name;
    String introduce;
    String profile_photo;
    String regdate;
    String reply;
    int idx;
    String isLike;

    FragmentManager fragmentManager;

    public  ReplyCommentAdapter(Context context, ArrayList<ReplyCommentData> arr){
        this.context = context;
        this.dataArr = arr;
    }

    public void addItems(ArrayList<ReplyCommentData> addItem) {
        dataArr = addItem;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        return dataArr.size();
    }

    @Override
    public Object getItem(int position) {

        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private ArrayList<int[]> getSpans(String body, char prefix){
        ArrayList<int[]> spans = new ArrayList<>();

        Pattern pattern= Pattern.compile(prefix + "\\w+");
        Matcher matcher = pattern.matcher(body);

        while (matcher.find()) {
            int[] currentSpan = new int[2];
            currentSpan[0] = matcher.start();
            currentSpan[1] = matcher.end();
            spans.add(currentSpan);
        }

        return  spans;



    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        final ViewHolder viewHolder;
        final ReplyCommentData data = dataArr.get(getCount() - position -1);

        String[] tag = new String[dataArr.size()];
        tag[position] = dataArr.get(getCount() - position -1).comment + " ";
        LogUtil.d(tag[position]);

        ArrayList<int[]> hashtagSpans = getSpans(tag[position], '#');

        SpannableString tagsContent = new SpannableString(tag[position]);

        for (int i = 0; i < hashtagSpans.size(); i++) {
            int[] span = hashtagSpans.get(i);
            int hashTagStart = span[0];
            int hashTagEnd = span[1];

            HashTag hashTag = new HashTag(context);
            hashTag.setOnClickEventListener(new HashTag.ClickEventListener() {
                @Override
                public void onClickEvent(String data) {

                     String a = "#"+data;                            //해쉬태그클릭해서 서치태그포스트프래그먼트이동할때 던져줄 번들


                    Intent bundle = new Intent();
                    //bundle.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    bundle.setClass(context, BaseActivity.class);
                    bundle.putExtra("word", a);
                    bundle.putExtra("move","hashtag");
                    context.startActivity(bundle);



                }
            });

            tagsContent.setSpan(hashTag, hashTagStart, hashTagEnd, 0);
        }



        if(data.target_idx>0) {
            if (v == null) {
                v = LayoutInflater.from(context).inflate(R.layout.activity_feed_rereply, null);
                viewHolder = new ViewHolder();
                viewHolder.layout = (LinearLayout) v.findViewById(R.id.layout);
                viewHolder.name = (TextView) v.findViewById(R.id.name_txt);
                viewHolder.introduce = (TextView) v.findViewById(R.id.introduce_txt);
                viewHolder.profile_photo = (ImageView) v.findViewById(R.id.profile_img);
                viewHolder.regdate = (TextView) v.findViewById(R.id.regdate_txt);
                viewHolder.reply = (TextView) v.findViewById(R.id.rere_txt);
                viewHolder.reply_like = (ImageView) v.findViewById(R.id.reply_like);

                v.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
        }
        else{
            if (v == null) {
                v = LayoutInflater.from(context).inflate(R.layout.activity_feed_reply, null);
                viewHolder = new ViewHolder();
                viewHolder.layout = (LinearLayout) v.findViewById(R.id.layout);
                viewHolder.name = (TextView) v.findViewById(R.id.name_txt);
                viewHolder.introduce = (TextView) v.findViewById(R.id.introduce_txt);
                viewHolder.profile_photo = (ImageView) v.findViewById(R.id.profile_img);
                viewHolder.regdate = (TextView) v.findViewById(R.id.regdate_txt);
                viewHolder.reply = (TextView) v.findViewById(R.id.rere_txt);
                viewHolder.reply_like = (ImageView) v.findViewById(R.id.reply_like);
                viewHolder.boundary_line = (ImageView) v.findViewById(R.id.boundary_line);

                if(position==0) viewHolder.boundary_line.setVisibility(View.GONE);
                else viewHolder.boundary_line.setVisibility(View.VISIBLE);

                v.setTag(viewHolder);

            } else {

                viewHolder = (ViewHolder) convertView.getTag();
            }

        }

        name = data.name;
        viewHolder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.user_idx != SPUtil.getInstance().getUserNumber(context)) {
                    Intent intent = new Intent();
                    intent.setClass(context, BaseActivity.class);
                    intent.putExtra("move", "otherinfo");
                    intent.putExtra("search_user_idx", String.valueOf(data.user_idx));
                    LogUtil.d("aaaaaaaaaaaa"+data.user_idx);
                    context.startActivity(intent);

                }
                else if(data.user_idx == SPUtil.getInstance().getUserNumber(context)){
                    Intent intent = new Intent();
                    intent.setClass(context, BaseActivity.class);
                    intent.putExtra("move", "info");
                    context.startActivity(intent);
                }
            }
        });
        introduce = data.comment;
        profile_photo = data.profile_photo;
        regdate = getReplyDateFormat(data.regdate);
        if(data.isLike.equals("Y"))
        {
            viewHolder.reply_like.setImageResource(R.drawable.heart_red);
            viewHolder.reply_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callApi_replyLikeCancel(data.idx);
                    LogUtil.d("click comment like Cancel");
                    data.isLike="N";
                    viewHolder.reply_like.setImageResource(R.drawable.heart);
                    notifyDataSetChanged();
                }
            });
        }
        else if(data.isLike.equals("N"))
        {
            viewHolder.reply_like.setImageResource(R.drawable.heart);
            viewHolder.reply_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtil.d("click comment like");
                    callApi_replyLike(data.idx);
                    viewHolder.reply_like.setImageResource(R.drawable.heart_red);
                    data.isLike="Y";
                    notifyDataSetChanged();
                }
            });
        }

        viewHolder.name.setText(name);
        viewHolder.introduce.setText(tagsContent);
        viewHolder.introduce.setMovementMethod(LinkMovementMethod.getInstance());

        viewHolder.regdate.setText(regdate);


        //viewHolder.eng_addr.setText(eng_addr);
        if (!TextUtils.isEmpty(profile_photo)) {
            Glide.with(getApplicationContext())
                    .load(Constants.SERVER_IMG_URL + profile_photo)
                    .placeholder(R.drawable.written_face)
                    .centerCrop()
                    .thumbnail(0.1f)
                    .into(viewHolder.profile_photo);
//            }
        } else {
            Glide.with(getApplicationContext())
                    .load(Constants.SERVER_IMG_URL + profile_photo)
                    .placeholder(R.drawable.written_face)
                    .error(R.drawable.written_face)
                    .centerCrop()
                    .thumbnail(0.1f)
                    .into(viewHolder.profile_photo);
        }


        return v;
    }

    private static class ViewHolder {

        public LinearLayout layout;
        public TextView name;
        public TextView introduce;
        public ImageView profile_photo;
        public TextView regdate;
        public TextView reply;
        public ImageView reply_like;
        public ImageView boundary_line;

    }

    private void callApi_replyLike(int idx)
    {
        GPClient client = new GPClient(context);
        client.addProgress();
        client.setUri(Constants.SERVER_URL_API_COMMENT_LIKE);
        client.addParameter("idx",idx);
        client.setDecoder(new GJSONDecoder(GHttpConstants.TYPE_OBJECT));
        client.addHandler(new GFailedHandler() {
            @Override
            public void failedExecute(GBean bean) {

            }
        });
        client.addHandler(new GResultHandler() {
            @Override
            public GBean result(GBean bean, Object result, int returnCode, String returnMessage, JSONObject responseData) {
                LogUtil.d("returnCode : " + returnCode);
                LogUtil.d("returnMessage : " + returnMessage);
                if (returnCode == GReturnCode.SUCCESS_COMMENT_LIKE_REGISTER) {
                    LogUtil.d("좋아요 성공");

                    notifyDataSetChanged();
                } else {
                    LogUtil.d(returnMessage);
                }
                return null;
            }
        });
        GExecutor.getInstance().cancelAndExecute(client);

    }

    private void callApi_replyLikeCancel(int idx)
    {
        GPClient client = new GPClient(context);
        client.addProgress();
        client.setUri(Constants.SERVER_URL_API_COMMENT_LIKE+"/"+idx);
        client.setHttpMethod(GHttpMethod.Delete);
        client.setDecoder(new GJSONDecoder(GHttpConstants.TYPE_OBJECT));
        client.addHandler(new GFailedHandler() {
            @Override
            public void failedExecute(GBean bean) {

            }
        });
        client.addHandler(new GResultHandler() {
            @Override
            public GBean result(GBean bean, Object result, int returnCode, String returnMessage, JSONObject responseData) {
                LogUtil.d("returnCode : " + returnCode);
                LogUtil.d("returnMessage : " + returnMessage);
                if (returnCode == GReturnCode.SUCCESS_COMMENT_LIKE_DELETE) {
                    LogUtil.d("좋아요 취소성공");

                    notifyDataSetChanged();
                } else {
                    LogUtil.d(returnMessage);
                }
                return null;
            }
        });
        GExecutor.getInstance().cancelAndExecute(client);

    }


}
