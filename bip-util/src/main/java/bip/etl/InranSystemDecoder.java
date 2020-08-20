package bip.etl;

import java.util.*;

/**
 * Created by ramezani on 4/5/2018.
 */
public class InranSystemDecoder {
    static String iranSystemCharsAfter128="0123456789.-؟آئئااببپپتتثثججچچححخخدذرزژسسششصصضضط";
    static String iranSystemCharsAfter224="ظععععغغغغففققککگگلﻻلممننوهههييي ";
    static List<Integer> iranSystemCharsWithSpaceAfter = Arrays.asList(new Integer[]{
            146, // ب
            148, // پ
            150, // ت
            152, // ث
            154, // ج
            156, // چ
            158, // ح
            160, // خ
            167, // س
            169, // ش
            171, // ص
            173, // ض
            225, // ع
            229, // غ
            233, // ف
            235, // ق
            237, // ک
            239, // گ
            241, // ل
            244, // م
            246, // ن
            249, // ه
            252, //ﯽ
            253 // ی
    });
    static Hashtable<Integer,Character> iranSystemSpecialChars=new Hashtable<Integer, Character>();
    static {
        iranSystemSpecialChars.put(32,' ');
        iranSystemSpecialChars.put(8212,'ت');
        iranSystemSpecialChars.put(8216,'ا');
        iranSystemSpecialChars.put(8220,'ب');
    }

    public static String decode(String iranSystem){
        char[] isChars = iranSystem.toCharArray();
        StringBuilder sb = new StringBuilder();
        for(char isCh: isChars){
            char uCh=isCh;
            int i_isCh=(int)isCh;
            if(iranSystemCharsWithSpaceAfter.contains(i_isCh)
                    && sb.length()>0 && sb.charAt(sb.length()-1)!=' '){
                sb.append(" ");
            }
            if(i_isCh>=128 && i_isCh<=175){
                uCh=iranSystemCharsAfter128.charAt(i_isCh-128);
            }else if(i_isCh>=224 && i_isCh<=256) {
                uCh = iranSystemCharsAfter224.charAt((int) isCh - 224);
            }else if(iranSystemSpecialChars.containsKey(i_isCh)){
                uCh = iranSystemSpecialChars.get(i_isCh);
            }else{
                uCh=isCh;
                System.err.println("error in converting iranSystem encoding : isCh="+isCh+",uCh="+uCh+",i_isCh="+i_isCh);
            }
            sb.append(uCh);
        }
        String unicode=sb.reverse().toString();
        return unicode;
    }

    public static void main(String[] args) throws Exception {
        //String iranSystem = "ý\u0090ùþ£ç— ý‘úþ¤‘õþ“ ø ý¦¤ó\u008D";
        //String iranSystem = "ç";
        String iranSystem = "2000  ¡J \u0097\u001CI øìíßÎíäÌ ÏÈÌäíÌ éÑÎÑ ßÏíÌÎí98àäîÍßìãÏ- ìÌìãäÍ    \u000BZ  0 \u0002           ,20,999991449,531657,43729";

        String unicode= decode(iranSystem);

        System.out.println("iranSystem="+iranSystem);
        System.out.println("unicode="+unicode);
    }


}
