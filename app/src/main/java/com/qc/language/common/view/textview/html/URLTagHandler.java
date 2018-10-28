package com.qc.language.common.view.textview.html;

import android.graphics.Color;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;

import com.blankj.utilcode.util.StringUtils;

import org.xml.sax.XMLReader;

import java.lang.reflect.Field;
import java.util.HashMap;

public class URLTagHandler implements Html.TagHandler  {

    private static final String TAG_BLUE_FONT = "bluefont";

    private int startIndex = 0;
    private int stopIndex = 0;
    final HashMap<String, String> attributes = new HashMap<String, String>();

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        processAttributes(xmlReader);

        if(tag.equalsIgnoreCase(TAG_BLUE_FONT)){
            if(opening){
                startFont(tag, output, xmlReader);
            }else{
                endFont(tag, output, xmlReader);
            }
        }
    }

    public void startFont(String tag, Editable output, XMLReader xmlReader) {
        startIndex = output.length();
    }

    //<font style="font-family:arial;color:red;font-size:20px;">A paragraph.</font>
    //<font style="background-color: yellow;">
    public void endFont(String tag, Editable output, XMLReader xmlReader){
        stopIndex = output.length();
        String style = attributes.get("style");
        if(!TextUtils.isEmpty(style) ){
            String[] temp = null;
            String color="";
            String backgroundColor = "";
            temp = style.split(";");
            for(int i=0;i<temp.length;i++){
                if(temp[i].contains("background-color")&&temp[i].contains(":")){
                    backgroundColor = temp[i].substring(temp[i].indexOf(":") + 1).trim(); //背景色
                    color = "Black";
                }else  if(temp[i].contains("color")&&temp[i].contains(":")){
                    color = temp[i].substring(temp[i].indexOf(":") + 1).trim(); //以防颜色前后有空格
                }
            }
            //判断空及对颜色解析异常抛出
            try{
                if(!StringUtils.isEmpty(color)){
                    output.setSpan(new ForegroundColorSpan(Color.parseColor(color)), startIndex, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                if(!StringUtils.isEmpty(backgroundColor)){
                    output.setSpan(new BackgroundColorSpan(Color.parseColor(backgroundColor)), startIndex, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }catch (IllegalArgumentException e){

            }
        }
    }

    private void processAttributes(final XMLReader xmlReader) {
        try {
            Field elementField = xmlReader.getClass().getDeclaredField("theNewElement");
            elementField.setAccessible(true);
            Object element = elementField.get(xmlReader);
            Field attsField = element.getClass().getDeclaredField("theAtts");
            attsField.setAccessible(true);
            Object atts = attsField.get(element);
            Field dataField = atts.getClass().getDeclaredField("data");
            dataField.setAccessible(true);
            String[] data = (String[])dataField.get(atts);
            Field lengthField = atts.getClass().getDeclaredField("length");
            lengthField.setAccessible(true);
            int len = (Integer)lengthField.get(atts);

            for(int i = 0; i < len; i++){
                attributes.put(data[i * 5 + 1], data[i * 5 + 4]);
            }
        }
        catch (Exception e) {

        }
    }

 }