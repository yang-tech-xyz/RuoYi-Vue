package com.ruoyi.admin.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;

import java.util.Date;
import java.util.Random;

/**
 * 生成单号工具
 *
 * @author feng
 */
public class NumbersUtils {


    private static String  genCodes(int length){
       String val = "";
       Random random = new Random();
       for(int i = 0; i < length; i++){
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num"; // 输出字母还是数字
            if("char".equalsIgnoreCase(charOrNum))  {  // 字符串
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; //取得大写字母还是小写字母
                val += (char) (choice + random.nextInt(26));
            }else if("num".equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        val=val.toLowerCase();
        return val;
    }





    public static String createInvite(){

        return genCodes(6);
    }

    /**
     * 生成UID
     * @return
     */
    public static  String createUid(){
        String s = RandomUtil.randomNumbers(6);
        return s;
    }
    public static void main(String[] args) {

        for(int i = 0 ;i < 1000;i++){
            System.out.println();
        }

    }



}
