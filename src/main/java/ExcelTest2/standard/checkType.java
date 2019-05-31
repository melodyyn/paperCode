package ExcelTest2.standard;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ���Ƿ��������ַ��� ����ƥ��Ľ��
 * 1.������
 * 2.����
 * 3.����
 * auth:lht
 * time: 2018��11��23��17:44:14
 */
public class checkType {

    public static boolean checkIsInteger(String index){
        Matcher m = Pattern.compile("[+|-]?[0-9]+").matcher(index);
        return m.matches();
    }

    /**
     * ��������
     */
    @Test
    public void t1(){
        System.out.println(checkIsInteger("1d"));
        System.out.println(checkIsInteger("1"));
        System.out.println(checkIsInteger("-1"));
        System.out.println(checkIsInteger("+1"));
        System.out.println(checkIsInteger("1.1"));
        System.out.println(checkIsInteger(".1"));
        System.out.println(checkIsInteger(""));

    }


    public static boolean checkIsDouble(String index){
        Matcher m = Pattern.compile("[+|-]?[0-9]+\\.?[0-9]*[e]?[-|+]?[0-9]+").matcher(index);
        return m.matches();
    }

    /**
     * ��������
     */
    @Test
    public void t2(){
        System.out.println(checkIsDouble("1.1e-10"));
        System.out.println(checkIsDouble("-1.10"));
        System.out.println(checkIsDouble("+1.10"));
        System.out.println(checkIsDouble("1.1"));
        System.out.println(checkIsDouble("-1.1"));
        System.out.println(checkIsDouble("+1.1"));
        System.out.println(checkIsDouble(".1"));
        System.out.println(checkIsDouble("1"));
        System.out.println(checkIsDouble("1.1d"));
    }

    @Test
    public void t3(){
        System.out.println(1>0.99);
    }


    @Test
    public void t4(){
        double min = 10.5;
        double me  = 13.4;
        double dValue = 1.3;
        System.out.println((int)((me-min)/1.3));

        System.out.println(Float.parseFloat("1"));
    }
}
