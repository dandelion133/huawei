package com.qian.utils;

import android.os.Environment;
import android.util.Log;
import android.util.Xml;

import com.qian.entity.Dish;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by QHF on 2016/7/4.
 */
public class XmlUtil {

    /**
     * 将List序列化  并将序列化文件内容返回
     * @param list  传入参数
     * @return  文件内容
     * @author 钱海峰
     */
    public static void writeXmlToLocal(List<Dish> list) {


      //  List<Dish> list = getDishList();

        //获取序列化对象
        XmlSerializer serializer = Xml.newSerializer();


        File file = null;

        try {

            file = new File(Environment.getExternalStorageDirectory(), "dish.xml");

            FileOutputStream fos = new FileOutputStream(file);
            //设置输出路径
            serializer.setOutput(fos, "utf-8");
            //开始写
            //写开始 <?xml version='1.0' encoding='utf-8' standalone='yes' ?>
            serializer.startDocument("utf-8", true);

            serializer.startTag(null, "dishs");//<persons>
            for(Dish dish : list) {
                serializer.startTag(null, "dish");//<dish>
                //写name
                serializer.startTag(null, "name");//<name>
                serializer.text(dish.getName());
                serializer.endTag(null, "name");//</name>
                //写image
                serializer.startTag(null, "image");//<image>
                serializer.text(dish.getImage() + "");
                serializer.endTag(null, "image");//</image>

                //写ID
                serializer.startTag(null, "id");//<id>
                serializer.text(dish.getId() + "");
                serializer.endTag(null, "id");//</id>

                //写Price
                serializer.startTag(null, "price");//<price>
                serializer.text(dish.getPrice() + "");
                serializer.endTag(null, "price");//</price>

                //写dishtype
                serializer.startTag(null, "dishtype");//<dishType>
                serializer.text(dish.getDishType() + "");
                Log.i("XmlUtil",dish.getDishType() + " ");
                serializer.endTag(null, "dishtype");//</dishType>

                //count
                serializer.startTag(null, "count");//<count>
                serializer.text(dish.getCount() + "");
                serializer.endTag(null, "count");//</count>

                serializer.endTag(null, "dish");//</dish>
            }


            serializer.endTag(null, "dishs");//</persons>
            serializer.endDocument();//文件结束



        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }


    public static String getStringXmlFromLocal() {

        File file = new File(Environment.getExternalStorageDirectory(), "dish.xml");
        String result = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
                result = result  + s;
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }




    public static ArrayList<Dish> parserXmlFromLocal() {
        try {
            File path = new File(Environment.getExternalStorageDirectory(), "dish.xml");
            FileInputStream fis = new FileInputStream(path);

            // 获得pull解析器对象
            XmlPullParser parser = Xml.newPullParser();
            // 指定解析的文件和编码格式
            parser.setInput(fis, "utf-8");
            //parser.
            //parser.set
            int eventType = parser.getEventType(); 		// 获得事件类型

            ArrayList<Dish> dishList = null;
            Dish dish = null;
            String id;
            while(eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();	// 获得当前节点的名称

                switch (eventType) {
                    case XmlPullParser.START_TAG: 		// 当前等于开始节点  <person>
                        if("dishs".equals(tagName)) {	// <dishs>
                            dishList = new ArrayList<>();
                        } else if("dish".equals(tagName)) { // dish>
                            dish = new Dish();
                        } else if("name".equals(tagName)) { // <name>
                            dish.setName(parser.nextText());
                        } else if("image".equals(tagName)) { // <image>
                            // dish.setAge(Integer.parseInt(parser.nextText()));
                            dish.setImage(Integer.parseInt(parser.nextText()));
                        } else if("id".equals(tagName)) { // <id>
                            // dish.setAge(Integer.parseInt(parser.nextText()));
                            dish.setId(Integer.parseInt(parser.nextText()));
                        } else if("price".equals(tagName)) { // <price>
                            // dish.setAge(Integer.parseInt(parser.nextText()));
                            dish.setPrice(Integer.parseInt(parser.nextText()));
                        } else if("dishtype".equals(tagName)) { // <dishtype>
                            // dish.setAge(Integer.parseInt(parser.nextText()));
                            dish.setDishType(Integer.parseInt(parser.nextText()));

                        } else if("count".equals(tagName)) { // <count>
                            // dish.setAge(Integer.parseInt(parser.nextText()));
                            dish.setCount(Integer.parseInt(parser.nextText()));
                        }
                        break;
                    case XmlPullParser.END_TAG:		// </dish>
                        if("dish".equals(tagName)) {
                            // 需要把上面设置好值的person对象添加到集合中
                            dishList.add(dish);
                        }
                        break;
                    default:
                        break;
                }

                eventType = parser.next();		// 获得下一个事件类型
            }
            return dishList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static InputStream getStringStream(String sInputString) {
        if (sInputString != null && !sInputString.trim().equals("")) {
            try {
                ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(sInputString.getBytes());
                return tInputStringStream;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }
    public static ArrayList<Dish> parserXmlFromString(String str) {
        try {
           // File path = new File(Environment.getExternalStorageDirectory(), "person.xml");
        //    FileInputStream fis = new FileInputStream(path);

            // 获得pull解析器对象
            XmlPullParser parser = Xml.newPullParser();
            // 指定解析的文件和编码格式
            parser.setInput(getStringStream(str), "utf-8");
            //parser.
            //parser.set
            int eventType = parser.getEventType(); 		// 获得事件类型

            ArrayList<Dish> dishList = null;
            Dish dish = null;
            String id;
            while(eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();	// 获得当前节点的名称

                switch (eventType) {
                    case XmlPullParser.START_TAG: 		// 当前等于开始节点
                        if("dishs".equals(tagName)) {	// <dishs>
                            dishList = new ArrayList<>();
                        } else if("dish".equals(tagName)) { // dish>
                              dish = new Dish();
                        } else if("name".equals(tagName)) { // <name>
                              dish.setName(parser.nextText());
                        } else if("image".equals(tagName)) { // <image>
                            dish.setImage(Integer.parseInt(parser.nextText()));
                        } else if("id".equals(tagName)) { // <id>
                            dish.setId(Integer.parseInt(parser.nextText()));
                        } else if("price".equals(tagName)) { // <price>
                            dish.setPrice(Integer.parseInt(parser.nextText()));
                        } else if("dishtype".equals(tagName)) { // <dishtype>
                            dish.setDishType(Integer.parseInt(parser.nextText()));
                        } else if("count".equals(tagName)) { // <count>
                            dish.setCount(Integer.parseInt(parser.nextText()));
                        }
                        break;
                    case XmlPullParser.END_TAG:		// </dish>
                        if("dish".equals(tagName)) {
                            // 需要把上面设置好值的person对象添加到集合中
                            dishList.add(dish);
                        }
                        break;
                    default:
                        break;
                }

                eventType = parser.next();		// 获得下一个事件类型
            }
            return dishList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}



