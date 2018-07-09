package com.travelbooks3.android.util;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.travelbooks3.android.R;


public class Alert
{
    public final static int CANCEL  = -1;
    public final static int BUTTON1 = 1;
    public final static int BUTTON2 = 2;
    public final static int BUTTON3 = 3;
    public final static int BUTTON4 = 4;
                                    
    protected static Toast  mToast;
    protected static int    mToastTime;
                            
    private Dialog          mDialog = null;
                                    
                                    
    public static void toastShort(Context context, String msg)
    {
        toast(context, msg, 1);
    }
    
    
    public static void toastShort(Context context, int msgRes)
    {
        String msg = context.getString(msgRes);
        toast(context, msg, 1);
    }
    
    
    public static void toastLong(Context context, String msg)
    {
        toast(context, msg, 2);
    }
    
    
    public static void toastLong(Context context, int msgRes)
    {
        String msg = context.getString(msgRes);
        toast(context, msg, 2);
    }
    
    
    private static void toast(Context context, String msg, int lengthType)
    {
        if (mToast == null)
        {
            switch (lengthType)
            {
                case 1:
                    mToastTime = Toast.LENGTH_SHORT;
                    break;
                    
                case 2:
                    mToastTime = Toast.LENGTH_LONG;
                    break;
            }
            mToast = Toast.makeText(context.getApplicationContext(), msg, mToastTime);
            mToast.show();
            return;
        }
        mToast.setText(msg);
        mToast.show();
    }
    
    
    /**
     * 기본 얼럿 (확인버튼)
     * 
     * @param message
     * @return
     */
    public Builder showAlert(Context context, String message)
    {
        return showAlert(context, null, message, true, "확인");
    }


    public Builder showAlert(Context context, String message, boolean cancelable)
    {
        return showAlert(context, null, message, cancelable, "확인");
    }


    /**
     * 일반 팝업 메시지만적용(버튼은 최대 두개까지)
     *
     * @param message
     * @return
     */
    public Builder showAlert(Context context, String message, boolean cancelable, String... btns)
    {
        return showAlert(context, null, message, cancelable, btns);
    }


    /**
     * 일반 팝업 메시지,타이틀적용(버튼은 최대 두개까지)
     */
    public Builder showAlert(Context context, String title, String message, boolean cancelable, String... btns)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton(R.string.txt_211, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton(R.string.txt_027, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

        /*
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.alert_message);
        dialog.setCancelable(true);

        TextView txtMessage = (TextView) dialog.findViewById(R.id.txtMessage);
        txtMessage.setText(message);

        final View baseAlertOut = dialog.findViewById(R.id.baseAlertOut);
        baseAlertOut.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.btnConfirm).setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                if (mListener != null) mListener.onClose(dialog, BUTTON1, null);
            }
        });
        dialog.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                if (mListener != null) mListener.onClose(dialog, BUTTON2, null);
            }
        });

        if (btns.length == 1)
        {
            dialog.findViewById(R.id.baseSplit).setVisibility(View.GONE);
            dialog.findViewById(R.id.btnCancel).setVisibility(View.GONE);
        }

        if (btns.length > 0)
        {
            ((TextView) dialog.findViewById(R.id.btnConfirm)).setText(btns[0]);
        }

        if (btns.length > 1)
        {
            ((TextView) dialog.findViewById(R.id.btnCancel)).setText(btns[1]);
        }

        dialog.show();
*/
        return null;

    }


    public Builder showAlertNoti(Context context, String title, String message, boolean cancelable, String... btns)
    {

        Builder builder = new Builder(context);
        builder.setCancelable(cancelable);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setIcon(R.mipmap.ic_launcher);
        switch (btns.length)
        {
            case 2:
                builder.setNegativeButton(btns[1], new OnClickListener()
                {

                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (mListener != null) mListener.onClose(dialog, BUTTON2, null);
                    }
                });
            case 1:
                builder.setPositiveButton(btns[0], new OnClickListener()
                {
                    
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (mListener != null) mListener.onClose(dialog, BUTTON1, null);
                    }
                });
                break;
        }
        
        builder.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            
            @Override
            public void onCancel(DialogInterface dialog)
            {
                if (mListener != null) mListener.onClose(dialog, CANCEL, null);
            }
        });
        
        builder.show();
        
        return builder;
    }
    
   /*
    public Dialog showSeletPhoto(Context context, boolean isExist)
    {
        
        String[] items = null;
        
        if (isExist)
        {
            items = new String[]
            { "촬영하기", "사진선택", "삭제" };
        }
        else items = new String[]
        { "촬영하기", "사진선택" };
        
        return showSelet(context, "", -1, items);
        
    }*/
    
  /*
    public Dialog showSelet(Context context, String title, int selection, String[] items)
    {
        
        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        
        // if (layout_id == -1)
        dialog.setContentView(R.layout.alert_select);
        // else
        // dialog.setContentView(layout_id);
        
        dialog.setCancelable(true);
        
        Button btn01 = (Button) dialog.findViewById(R.id.btn01);
        Button btn02 = (Button) dialog.findViewById(R.id.btn02);
        Button btn03 = (Button) dialog.findViewById(R.id.btn03);
        Button btn04 = (Button) dialog.findViewById(R.id.btn04);
        
        ArrayList<Button> list = new ArrayList<Button>();
        
        list.add(btn01);
        list.add(btn02);
        list.add(btn03);
        list.add(btn04);
        
        for (Button b : list)
        {
            b.setVisibility(View.GONE);
        }
        
        for (int i = 0; i < items.length; i++)
        {
            list.get(i).setText(items[i]);
            list.get(i).setVisibility(View.VISIBLE);
        }
        
        if (items.length < 3)
        {
            dialog.findViewById(R.id.line02).setVisibility(View.GONE);
        }
        if (items.length < 4)
        {
            dialog.findViewById(R.id.line03).setVisibility(View.GONE);
        }
        
        dialog.findViewById(R.id.baseOut).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mListener != null)
                {
                    mListener.onClose(dialog, CANCEL, null);
                }
                dialog.cancel();
            }
        });
        
        for (int i = 0; i < list.size(); i++)
        {
            final int index = i + 1;
            
            list.get(i).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (mListener != null)
                    {
                        mListener.onClose(dialog, index, null);
                    }
                    
                    if (dialog != null) dialog.dismiss();
                }
            });
        }
        
        dialog.show();
        
        return dialog;
    }
    */
    
    protected OnCloseListener mListener = null;
    
    
    public void setOnCloseListener(OnCloseListener listener)
    {
        mListener = listener;
    }
    
    
    public interface OnCloseListener
    {
        public void onClose(DialogInterface dialog, int which, Object data);
    }

    // 시간표시 (?시간전...)

    private static class TIME_MAXIMUM
    {


    }

    
}