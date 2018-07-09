package com.travelbooks3.android.util;

import android.content.Context;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FormatUtil
{
    private static double distance = 1000.0;
    
    
    /**
     * null 체크
     * 
     * @param targetString
     * @return null이면 true, null이 아니면 false 반환
     */
    public static boolean isNullorEmpty(String targetString)
    {
        if (targetString == null || targetString.trim().equals("") || targetString.trim().equals("null")) return true;
        
        return false;
    }
    
    
    public static String trimRight(String value)
    {
        String result = value;
        
        if (isNullorEmpty(value))
        {
            result = "";
        }
        else
        {
            result = value.replaceAll("\\s+$", "");
        }
        
        return result;
    }
    
    
    public static String trimLeft(String value)
    {
        String result = value;
        
        if (isNullorEmpty(value))
        {
            result = "";
        }
        else
        {
            result = value.replaceAll("^\\s+", "");
        }
        
        return result;
    }
    
    
    public static String toPriceFormat(double lSource)
    {
        return toPriceFormat(lSource, false);
    }
    
    
    public static String toPriceFormat(double lSource, boolean hasWon)
    {
        String won = "원";
        
        // if((lSource != 0.0) && (lSource % 10000 == 0) && hasWon)
        // {
        // won = "만원";
        // lSource = lSource / 10000;
        // }
        String sPattern = "###,###,###,###,###,##0";
        DecimalFormat decimalformat = new DecimalFormat(sPattern);
        
        String result = decimalformat.format(lSource);
        result = hasWon ? result + won : result + "";
        return result;
    }
    
    
    public static String toPriceFormat(String sSource)
    {
        return toPriceFormat(sSource, false);
    }
    
    
    public static String toPriceFormat(String sSource, boolean hasWon)
    {
        if (isNullorEmpty(sSource)) return sSource;
        
        try
        {
            return toPriceFormat(Double.valueOf(sSource), hasWon);
        }
        catch (NumberFormatException e)
        {
            return sSource;
        }
        
    }
    
    
    public static String toTime(int second)
    {
        String time = "00:00";
        
        int h = second / 60 / 60;
        
        int m = second / 60;
        
        int s = second % 60;
        
        // LogUtil.d("h : " + h);
        // LogUtil.d("s : " + m);
        // LogUtil.d("s : " + m);
        
        if (h > 0) time = twoNumber(h) + ":" + twoNumber(m) + ":" + twoNumber(s);
        else time = twoNumber(m) + ":" + twoNumber(s);
        
        return time;
    }
    
    
    public static String twoNumber(int n)
    {
        String num = "0" + n;
        
        return num.substring(num.length() - 2, num.length());
    }
    
    
    public static String getKmData(String km)
    {
        String retVal = "";
        double dKm = 0.0;
        try
        {
            dKm = Double.parseDouble(km);
            retVal = getKmData(dKm);
        }
        catch (Exception e)
        {
            retVal = "??";
        }
        return retVal;
        
    }
    
    
    public static String getKmData(double km)
    {
        String retVal = "0";
        if (km < 0)
        {
            retVal = "";
        }
        else if (km < 1000.0)
        {
            retVal = (int) km + "m";
        }
        else
        {
            double value = 0;
            value = km / (distance);
            DecimalFormat format = new DecimalFormat("0.00");
            retVal = format.format(value) + "km";
        }
        
        return retVal;
        
    }
    
    
    /**
     * 기준 날짜 기준 날짜 이동
     * 
     * 
     **/
    public static String returnDate(String birth, int plusDate, String spliter)
    {
        String result = "";
        int year = Integer.parseInt(birth.substring(0, 4));
        int month = Integer.parseInt(birth.substring(5, 7));
        int day = Integer.parseInt(birth.substring(8, 10));
        
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        
        cal.add(Calendar.DATE, plusDate);
        
        int resultYear = cal.get(Calendar.YEAR);
        int resultMonth = cal.get(Calendar.MONTH);
        int resultDay = cal.get(Calendar.DAY_OF_MONTH);
        
        if (year != resultYear)
        {
            resultMonth = cal.get(Calendar.MONTH) + 1;
        }
        
        result = String.valueOf(resultYear) + spliter + String.valueOf(convertTwoNumber(resultMonth)) + spliter + String.valueOf(convertTwoNumber(resultDay));
        
        return result;
    }
    
    
    public static String convertTwoNumber(long day)
    {
        String temp = "0" + day;
        
        return temp.substring(temp.length() - 2, temp.length());
    }
    
    
    public static int convertOneNumber(long num)
    {
        String str = String.valueOf(num);
        if (str.startsWith("0"))
        {
            str.substring(str.length() - 1, str.length());
        }
        return Integer.parseInt(str);
    }
    
    
    public static String convertMD5(String str)
    {
        MessageDigest md5 = null;
        try
        {
            md5 = MessageDigest.getInstance("MD5");
        }
        catch (Exception e)
        {
            LogUtil.w(e);
        }
        
        String eip;
        byte[] bip;
        
        String temp = "";
        String tst = str;
        
        bip = md5.digest(tst.getBytes());
        
        for (int i = 0; i < bip.length; i++)
        {
            eip = "" + Integer.toHexString((int) bip[i] & 0x000000ff);
            if (eip.length() < 2) eip = "0" + eip;
            
            temp = temp + eip;
        }
        
        return temp;
        
    }
    
    
    /**
     * 천단위로 자동으로 콤마를 찍어준다.
     * 
     * @param et
     * @return
     */
    public static EditText setAutoPriceComma(final EditText et)
    {
        et.addTextChangedListener(new TextWatcher()
        {
            String strPrice = "";
            
            
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }
            
            
            public void afterTextChanged(Editable s)
            {
            }
            
            
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (!s.toString().equals(strPrice))
                {
                    strPrice = FormatUtil.toPriceFormat(s.toString().replace(",", ""), false);
                    et.setText(strPrice);
                    Editable e = et.getText();
                    Selection.setSelection(e, strPrice.length());
                }
            }
        });
        
        return et;
    }
    
    
    /**
     * 단어에 한글 포함 여부 검사
     */
    public static boolean isContainsKorean(String word)
    {
        if (word.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*"))
        {
            return true;
        }
        
        return false;
    }
    
    
    /**
     * 이메일 패턴검사
     * 
     * @param email
     * @return
     */
    public static boolean isEmailPattern(String email)
    {
        if (isNullorEmpty(email))
        {
            return false;
        }
        Pattern pattern = Pattern.compile("\\w+[@]\\w+\\.\\w+");
        Matcher match = pattern.matcher(email);
        return match.find();
    }
    
    
//    private static final String Password_PATTERN = "^(?=.*[a-zA-Z]+)(?=.*[!@#$%^*+=-]|.*[0-9]+).{8,20}$";
private static final String Password_PATTERN = "^(?=.*[a-zA-Z]+)(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,20}$";

    public static boolean isPasswordValidate(final String hex)
    {
        Pattern pattern = Pattern.compile(Password_PATTERN);
        Matcher matcher = pattern.matcher(hex);
        return matcher.matches();
    }
    
    
    private static final String NAME_ENG_PATTERN = "^[,_A-Za-z0-9+]*$";
//    private static final String NAME_ENG_PATTERN1 = "^(?=.*[a-zA-Z]+)(?=.*[0-9])$";
    
    public static boolean isNameEngValidate(final String hex)
    {
        Pattern pattern = Pattern.compile(NAME_ENG_PATTERN);
        Matcher matcher = pattern.matcher(hex);
        return matcher.matches();
    }
    
    
    public static Date FormatStringGetDate(String dateAndTimeStr)
    {
        return FormatStringGetDate(dateAndTimeStr, "yyyyMMddHHmmss");
    }
    
    
    public static Date FormatStringGetDate(String dateAndTimeStr, String formatStr)
    {
        SimpleDateFormat fromFormat = new SimpleDateFormat(formatStr);
        Date fromDate = null;
        try
        {
            fromDate = fromFormat.parse(dateAndTimeStr);
        }
        catch (Exception e)
        {
            fromDate = new Date(0);
            e.printStackTrace();
        }
        
        return fromDate;
    }
    
    
    public static String FormatGetDate(String dateAndTimeStr, String formatStr)
    {
        try
        {
            return FormatGetDate((new SimpleDateFormat(formatStr)).parse(dateAndTimeStr));
        }
        catch (Exception e)
        {
            return "";
        }
    }
    
    
    public static String FormatGetDate(Date date)
    {
        long nowTime = System.currentTimeMillis();
        
        long remainTime = nowTime - date.getTime();
        
        String result = "";
        
        if (remainTime < 24 * 60 * 60000)
        {
            result = (remainTime / (60 * 60000)) + "시간전";
        }
        else if (remainTime < 24 * 60 * 60000 * 7)
        {
            result = (remainTime / (24 * 60 * 60000)) + "일전";
        }
        else
        {
            String yyyy = getCurrentDate().substring(0, 4);
            String format = "yyyy.MM.dd";
            LogUtil.d("yyyy : " + yyyy);
            
            SimpleDateFormat fromFormat = new SimpleDateFormat("yyyy.MM.dd");
            try
            {
                result = fromFormat.format(date);
                result = result.replace(yyyy + ".", "");
            }
            catch (Exception e)
            {
            }
        }
        // if (remainTime < 60000)
        // {
        // result = "조금전";
        // }
        // else if (remainTime < 60 * 60000)
        // {
        // result = (remainTime / 60000) + "분전";
        // }
        // else if (remainTime < 24 * 60 * 60000)
        // {
        // result = (remainTime / (60 * 60000)) + "시간전";
        // }
        // else
        // {
        // SimpleDateFormat fromFormat = new SimpleDateFormat("yyyy.MM.dd");
        // try
        // {
        // result = fromFormat.format(date);
        // }
        // catch (Exception e)
        // {
        // }
        // }
        
        return result;
    }
    
    
    public static String FormatDateConvertString(String dateAndTimeStr, String formatStr, String retformatStr)
    {
        if (isNullorEmpty(dateAndTimeStr)) return "";
        
        SimpleDateFormat fromFormat = new SimpleDateFormat(formatStr);
        SimpleDateFormat toFormat = new SimpleDateFormat(retformatStr);
        
        Date fromDate = null;
        try
        {
            fromDate = fromFormat.parse(dateAndTimeStr);
            return toFormat.format(fromDate);
        }
        catch (Exception e)
        {
            return dateAndTimeStr;
        }
    }
    
    
    public static String FormatDateConvertString(String dateAndTimeStr, String retformatStr)
    {
        return FormatDateConvertString(dateAndTimeStr, "yyyy-MM-dd HH:mm:ss", retformatStr);
    }
    
    
    public static String FormatDateGetString(Date date, String formatStr)
    {
        String strDate = "";
        
        // LogUtil.e(formatStr);
        
        SimpleDateFormat fromFormat;
        fromFormat = new SimpleDateFormat(formatStr);
        try
        {
            strDate = fromFormat.format(date.getTime());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            date = new Date();
            strDate = fromFormat.format(date.getTime());
        }
        
        return strDate;
    }
    
    
    /**
     * input : dateString
     * 
     * @return calendar
     */
    public static Calendar FormatStringGetCalendar(String dateAndTimeStr, String formatStr)
    {
        SimpleDateFormat fromFormat = new SimpleDateFormat(formatStr);
        Calendar cal = new GregorianCalendar();
        Date fromDate = null;
        try
        {
            fromDate = fromFormat.parse(dateAndTimeStr);
        }
        catch (Exception e)
        {
            fromDate = new Date();
            e.printStackTrace();
        }
        cal.setTime(fromDate);
        
        return cal;
    }
    
    
    public static String FormatCalendarGetString(Calendar cal, String formatStr)
    {
        SimpleDateFormat fromFormat = new SimpleDateFormat(formatStr);
        String str = fromFormat.format(cal.getTime());
        
        return str;
    }
    
    
    public static String FormatCalendarGetString(Calendar cal)
    {
        return FormatCalendarGetString(cal, "yyyyMMddHHmmss");
    }
    
    
    public static String FormatCalendarGetDayOfWeek(Calendar cal)
    {
        String[] dayOfWeek =
        { "", "일", "월", "화", "수", "목", "금", "토" };
        return dayOfWeek[cal.get(Calendar.DAY_OF_WEEK)];
    }
    
    
    /**
     * 특정 시간의 오전/오후 정보를 가져온다.
     * 
     * @param strDate
     *            "hh:mm" 형태로 특정시간를 입력
     * @return 오전 or 오후 를 반환
     */
    public static String getAmPm(String strDate)
    {
        Locale locale = Locale.KOREA;
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", locale);
        try
        {
            Date date = sdf.parse(strDate);
            SimpleDateFormat sdf2 = new SimpleDateFormat("a", locale);
            return sdf2.format(date);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 특정 시간의 오전 정보만을 가져온다.
     *
     * @param
     *            "HH:mm" 형태로 특정시간를 입력
     * @return 12:00 이상일때 오전시간만을 반환
     */
    public static String getOnlyAM(String strTime)
    {
        Locale locale = Locale.KOREA;
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", locale);
        try
        {
            Date date = sdf.parse(strTime);
            return sdf.format(date);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        
        return null;
    }
    
    
    public static boolean isPhoneNumber(String phoneNum)
    {
        Pattern p = Pattern.compile("01[016789][1-9]{1}[0-9]{2,3}[0-9]{4}$");
        Matcher matcher = p.matcher(phoneNum);
        
        return matcher.matches();
    }
    
    
    /**
     * 하이픈 변환
     * 
     * @param phoneNum
     * @return
     */
    public static String toPhoneNumber(String phoneNum)
    {
        if (isNullorEmpty(phoneNum)) return "";
        
        String phone = PhoneNumberUtils.formatNumber(phoneNum);
        
        try
        {
            if (!phone.contains("-"))
            {
                if (phone.startsWith("02"))
                {
                    if (phone.length() == 9)
                    {
                        phone = phone.substring(0, 2) + "-" + phone.substring(2, 5) + "-" + phone.substring(5, 9);
                    }
                    else if (phone.length() == 10)
                    {
                        phone = phone.substring(0, 2) + "-" + phone.substring(2, 6) + "-" + phone.substring(6, 10);
                    }
                }
                else if (phone.length() == 10)
                {
                    phone = phone.substring(0, 3) + "-" + phone.substring(3, 6) + "-" + phone.substring(6, 10);
                }
                else if (phone.length() == 11)
                {
                    phone = phone.substring(0, 3) + "-" + phone.substring(3, 7) + "-" + phone.substring(7, 11);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return phone;
    }
    
    
    public static String nullStringToBlank(String str)
    {
        if (isNullorEmpty(str)) return "";
        
        return str;
    }
    
    
    public static int getStringNumber(String str)
    {
        int retVal = -1;
        if (isNullorEmpty(str))
        {
            return retVal;
        }
        else
        {
            try
            {
                retVal = Integer.parseInt(str);
            }
            catch (NumberFormatException e)
            {
                e.printStackTrace();
            }
            return retVal;
        }
    }
    
    
    public static int getByteLength(String str, String charset)
    {
        
        int len = -1;
        
        try
        {
            len = str.getBytes(charset).length;
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        
        return len;
    }
    
    
    public static String getStringOfBundle(Bundle bnd, String key)
    {
        return getStringOfBundle(bnd, key, "");
    }
    
    
    public static String getStringOfBundle(Bundle bnd, String key, String defVal)
    {
        if (bnd == null) return defVal;
        
        String value = bnd.getString(key);
        return FormatUtil.isNullorEmpty(value) ? defVal : value;
    }
    
    
    public static String getCurrentDate()
    {
        DecimalFormat df = new DecimalFormat("00");
        Calendar currentCal = Calendar.getInstance();
        
        currentCal.add(Calendar.DATE, 0);
        
        String year = Integer.toString(currentCal.get(Calendar.YEAR));
        String month = df.format(currentCal.get(Calendar.MONTH) + 1);
        String day = df.format(currentCal.get(Calendar.DAY_OF_MONTH));
        
        return year + "-" + month + "-" + day;
    }
    
    
    public static String getCurrentDateAndHour()
    {
        DecimalFormat df = new DecimalFormat("00");
        Calendar currentCal = Calendar.getInstance();
        
        currentCal.add(Calendar.DATE, 0);
        
        String year = Integer.toString(currentCal.get(Calendar.YEAR));
        String month = df.format(currentCal.get(Calendar.MONTH) + 1);
        String day = df.format(currentCal.get(Calendar.DAY_OF_MONTH));
        String hour = df.format(currentCal.get(Calendar.HOUR));
        
        return year + "-" + month + "-" + day + " " + hour;
    }
    
    
    /**
     * 금일 날짜는 삭제 후에 오후 02:10 식으로 리턴
     * 
     * 지난 날짜는 해당 날짜 그대로 리턴
     * 
     * @param date
     * @return
     */
    public static String convertTodayRemoveDate(String date)
    {
        String today = FormatUtil.getCurrentDate();
        
        if (date.contains(today))
        {
            date = date.replace(today, "").replace(" ", "");
            date = FormatUtil.getAmPm(date) + " " + date;
        }
        
        return date;
    }
    
    
    /**
     * 금일 날짜는 오늘 13:02
     * 
     * 지난 날짜는 해당 날짜 그대로 리턴
     * 
     * @param date
     * @return
     */
    public static String convertTodayDisplyDate(String date)
    {
        String today = FormatUtil.getCurrentDate();
        date = FormatDateConvertString(date, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm");
        LogUtil.d("date : " + date);
        LogUtil.d("today : " + today);
        if (date.contains(today))
        {
            date = date.replace(today, "").replace(" ", "");
            date = "오늘 " + date;
        }
        
        return date;
    }
    
    
    public static String toDateTime(String dateTime)
    {
        String today = FormatUtil.getCurrentDate();
        dateTime = FormatDateConvertString(dateTime, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd");
        LogUtil.d("dateTime : " + dateTime);
        LogUtil.d("today : " + today);
        if (dateTime.contains(today))
        {
            dateTime = "오늘";
        }
        return dateTime;
    }
    
    
    /**
     * 날짜 변환 (Calendar -> String)
     * 
     * @param cal
     * @param formatStr
     * @return
     */
    public static String getDateCalendarToString(Calendar cal, String formatStr)
    {
        SimpleDateFormat fromFormat = new SimpleDateFormat(formatStr);
        String str = "";
        try
        {
            str = fromFormat.format(cal.getTime());
        }
        catch (Exception e)
        {
        }
        
        return str;
    }
    
    
    public static String getContentsDateFormat(String date)
    {
        if (FormatUtil.isNullorEmpty(date)) return "";
        
        String today = FormatUtil.getCurrentDate();
        int todayDate = Integer.valueOf(today.substring(8, 10));
        String dateT = FormatDateConvertString(date, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm");
        int dateTDate = Integer.valueOf(dateT.substring(8, 10));
        if (dateT.startsWith(today))
        {
            dateT = "오늘";
        }
        else if (todayDate == dateTDate + 1)
        {
            dateT = "어제";
        }
        else
        {
            dateT = dateT.substring(0, 10);
        }
        
        return dateT;
    }
    
    
    public static String getReplyDateFormat(String date)
    {
        if (FormatUtil.isNullorEmpty(date)) return "";
        
        Calendar currentTime = Calendar.getInstance();
        String today = FormatUtil.getCurrentDate();
        int todayDate = Integer.valueOf(today.substring(8, 10));
        String today2 = FormatUtil.getCurrentDateAndHour(); // yyyy-MM-dd HH
        // int todayHour = Integer.valueOf(today2.substring(11));
        int todayMinute = 0;
        String dateT = FormatDateConvertString(date, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm");
        //LogUtil.e("dateT" + dateT);
        int dateTDate = Integer.valueOf(dateT.substring(8, 10));
        int dateTHour = Integer.valueOf(dateT.substring(11, 13));
        int dateTMinute = Integer.valueOf(dateT.substring(14));
        
        int todayHour = currentTime.get(Calendar.HOUR_OF_DAY);
        
        if (dateT.startsWith(today))
        {
            dateT = String.valueOf(todayHour - dateTHour);
            todayMinute = currentTime.get(Calendar.MINUTE);
            
        //    LogUtil.e("dateT" + dateT);
            
            if ("0".equals(dateT) && ((todayMinute - dateTMinute) < 4))
            {
                dateT = "방금전";
            }
            else if (("0".equals(dateT) && ((todayMinute - dateTMinute) > 3)))
            {
                int a=(todayMinute - dateTMinute);
                dateT = Integer.toString(a)+"분전";
            }
            else
            {
                dateT = String.valueOf(todayHour - dateTHour) + "시간전";
            }
            
        }
        else if (todayDate == dateTDate + 1)
        {
            dateT = "어제";
        }
        else
        {
            if (dateT.substring(5, 6).equals("0"))
            {
                dateT = dateT.substring(0, 4) + "년" + dateT.substring(6, 7) + "월" + dateT.substring(8, 10) + "일";
            }
            else
            {
                dateT = dateT.substring(0, 4) + "년" + dateT.substring(5, 7) + "월" + dateT.substring(8, 10) + "일";
            }
        }
        
        return dateT;
    }
    
    
    public static String getRegistDateFormat(String registDT)
    {
        String result = "";
        
        if (FormatUtil.isNullorEmpty(registDT)) return "";
        // String date = FormatDateConvertString(registDT, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm");
        
        Calendar cal = new GregorianCalendar();
        
        cal.set(Integer.parseInt(registDT.substring(0, 4)), Integer.parseInt(registDT.substring(5, 7)), Integer.parseInt(registDT.substring(8, 10)), Integer.parseInt(registDT.substring(11, 13)), Integer.parseInt(registDT.substring(14, 16)), Integer.parseInt(registDT.substring(17, 19)));
        
        String DayOfWeek = "";
        
        switch (cal.get(Calendar.DAY_OF_WEEK))
        {
            case 1:
                DayOfWeek = "일";
                break;
            case 2:
                DayOfWeek = "월";
                break;
            case 3:
                DayOfWeek = "화";
                break;
            case 4:
                DayOfWeek = "수";
                break;
            case 5:
                DayOfWeek = "목";
                break;
            case 6:
                DayOfWeek = "금";
                break;
            case 7:
                DayOfWeek = "토";
                break;
        }
        
        result = "최종수정일 : " + cal.get(Calendar.YEAR) + "." + cal.get(Calendar.MONTH) + "." + cal.get(Calendar.DATE) + "(" + DayOfWeek + ") " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.HOUR_OF_DAY);
        
        return result;
    }
    
    
    public static ArrayList<int[]> getSpans(String body, char prefix)
    {
        ArrayList<int[]> spans = new ArrayList<int[]>();
        
        Pattern pattern = Pattern.compile(prefix + "\\w+");
        Matcher matcher = pattern.matcher(body);
        
        // Check all occurrences
        while (matcher.find())
        {
            int[] currentSpan = new int[2];
            currentSpan[0] = matcher.start();
            currentSpan[1] = matcher.end();
            spans.add(currentSpan);
        }
        
        return spans;
    }


    private static class TIME_MAXIMUM {
        public static final int SEC = 60;
        public static final int MIN = 60;
        public static final int HOUR = 24;
        public static final int DAY = 30;
        public static final int MONTH = 12;
    }

    public static String calculateTime(Date date) {
        long curTime = System.currentTimeMillis();
        long regTime = date.getTime();
        long diffTime = (curTime - regTime) / 1000;

        String msg = null;

        if(diffTime < TIME_MAXIMUM.SEC) {
            // sec
            msg = diffTime + "초전";
        } else if ((diffTime /= TIME_MAXIMUM.SEC) < TIME_MAXIMUM.MIN) {
            // min
            System.out.println(diffTime);
            msg = diffTime + "분전";
        } else if ((diffTime /= TIME_MAXIMUM.MIN) < TIME_MAXIMUM.HOUR) {
            // hour
            msg = (diffTime ) + "시간전";
        } else if ((diffTime /= TIME_MAXIMUM.HOUR) < TIME_MAXIMUM.DAY) {
            // day
            msg = (diffTime ) + "일전";
        } else if ((diffTime /= TIME_MAXIMUM.DAY) < TIME_MAXIMUM.MONTH) {
            // day
            msg = (diffTime ) + "달전";
        } else {
            msg = (diffTime) + "년전";
        }

        return msg;
    }

    public static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }



}