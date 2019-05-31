package ExcelTest2.standard;

import ExcelTest2.standard.entity.TypeInfo;
import ExcelTest2.standard.entity.commonValue;
import com.alibaba.druid.sql.ast.statement.SQLIfStatement;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * auth:lht
 * time: 2018��11��23��17:44:14
 */
public class buildStandard {

    public static List<TypeInfo> standardList = null;
    public static List<String> meansNll = null ;



    @Test
    public void t1(){

        System.out.println(Double.parseDouble("1.2"));
    }

    /**
     * ��content �������黯 Ȼ��ȶ�ÿ��Ԫ�ص�������ȷ�� bean��type����;
     * @param content
     * @return
     */
    public static List<TypeInfo> confirmDataType(String content){

        List<TypeInfo> results = new ArrayList<TypeInfo>();  //�����ܱ�׼��Ϣ������

        String[] strs = content.split(commonValue.splitFlage);
        int length = strs.length;                       //��ȡ����

//        for(String s:strs) System.out.print(s+"|");
//        System.out.println();

        for(int i=0;i<strs.length;i++){
        String  current = strs[i];

        if(checkType.checkIsInteger(current)){          //�ж��Ƿ�������
            results.add(new TypeInfo("Integer",new ArrayList<String>()));

        }else if(checkType.checkIsDouble(current)){     //�ж��Ƿ��Ǹ�����
            results.add(new TypeInfo("Double",new ArrayList<String>()));

        }else{
            results.add(new TypeInfo("Other",new ArrayList<String>()));
        }
        }
        return results;
    }

    /**
     * ������н��
     */
    public static void displayList(){
        int i =1;
        for(TypeInfo p : standardList){
            if(p.getTypes()!=null){

                System.out.println(i+" "+p+"  "+p.getTypes().size());
            }else{
                System.out.println(i+" "+p+"  ");
            }
            i++;
        }
    }

    /**
     * ����Խ�����������ս��
     * @param MaxLength
     * @param myLength
     */
    public static List<TypeInfo> vertifiedResult(int MaxLength,int myLength){

            for(TypeInfo p : standardList){
                if(!p.getType().equals("Other")){
            	int size = p.getTypes().size();
                if(size>MaxLength){
                    p.setOver("yes");               //��ʾ������
                    Double Dvalue = (p.getMax()-p.getMin())/myLength;   //�õ�ÿһ�εĴ�С
                    p.setDvalue(Dvalue);            //��������ļ��ֵ Ȼ���õ�ǰ���ݼ�ȥ��С�ĳ�������������
                    p.setTypes(null);               //������Ķ���û�����˰��ڴ潻�����������
                }
                }
            }
            return standardList;
    }

    /**
     * �����������ݽ���ͳ��
     * @param content
     */
    public static  void dealWithData(String content,int maxLength){
        String[] strs = content.split(commonValue.splitFlage);
        int length = strs.length;                       //��ȡ����

        for(int i =0;i<strs.length;i++){
            String current = strs[i];                   //��ǰ���������

            if(current.equals(commonValue.lossFlage))continue;     //��ȱֵ�����д���
            	
            TypeInfo currentP = standardList.get(i);    //��ȡ��Ӧ�������ռ�bean

            if(currentP.getType().equals("Integer")){
                if(checkType.checkIsDouble(current)) {
                    Double nowInt = Double.parseDouble(current);         //תΪDouble
                    if (currentP.getTypes().size() == 0) {              //����Ϊ0 ˵���ǵ�һ��
                        currentP.getTypes().add(nowInt + "");         //ֱ����String��������
                        currentP.setMax(nowInt);
                        currentP.setMin(nowInt);
                    } else {
                        if (!currentP.getTypes().contains(current)) { //û�зŽ�����˵����Ҫ���д���
                            if (currentP.getTypes().size() < maxLength + 1)
                                currentP.getTypes().add(current); //����洢�ĸ��������˹涨ֵ ���ݾͲ�����������
                            if (nowInt < currentP.getMin()) currentP.setMin(nowInt);
                            if (nowInt > currentP.getMax()) currentP.setMax(nowInt);
                        }
                    }
                }else {
                    int nowInt = Integer.parseInt(current);         //תΪint
                    if (currentP.getTypes().size() == 0) {              //����Ϊ0 ˵���ǵ�һ��
                        currentP.getTypes().add(nowInt + "");         //ֱ����String��������
                        currentP.setMax(nowInt);
                        currentP.setMin(nowInt);
                    } else {
                        if (!currentP.getTypes().contains(current)) { //û�зŽ�����˵����Ҫ���д���
                            if (currentP.getTypes().size() < maxLength + 1)
                                currentP.getTypes().add(current); //����洢�ĸ��������˹涨ֵ ���ݾͲ�����������
                            if (nowInt < currentP.getMin()) currentP.setMin(nowInt);
                            if (nowInt > currentP.getMax()) currentP.setMax(nowInt);
                        }
                    }
                }
            }else if(currentP.getType().equals("Double")){
                if(checkType.checkIsInteger(current)){
                    int nowDouble = Integer.parseInt(current);      //תΪInt
                    if (currentP.getTypes().size() == 0) {              //����Ϊ0 ˵���ǵ�һ��
                        currentP.getTypes().add(nowDouble + "");         //ֱ����String��������
                        currentP.setMax(nowDouble);
                        currentP.setMin(nowDouble);
                    } else {
                        if (!currentP.getTypes().contains(current)) { //û�зŽ�����˵����Ҫ���д���
                            if (currentP.getTypes().size() < maxLength + 1)
                                currentP.getTypes().add(current);//����洢�ĸ��������˹涨ֵ ���ݾͲ�����������
                            if (nowDouble < currentP.getMin()) currentP.setMin(nowDouble);
                            if (nowDouble > currentP.getMax()) currentP.setMax(nowDouble);
                        }
                    }

                }else{
                    Double nowDouble = Double.parseDouble(current);      //תΪdouble
                    if (currentP.getTypes().size() == 0) {              //����Ϊ0 ˵���ǵ�һ��
                        currentP.getTypes().add(nowDouble + "");         //ֱ����String��������
                        currentP.setMax(nowDouble);
                        currentP.setMin(nowDouble);
                    } else {
                        if (!currentP.getTypes().contains(current)) { //û�зŽ�����˵����Ҫ���д���
                            if (currentP.getTypes().size() < maxLength + 1)
                                currentP.getTypes().add(current);//����洢�ĸ��������˹涨ֵ ���ݾͲ�����������
                            if (nowDouble < currentP.getMin()) currentP.setMin(nowDouble);
                            if (nowDouble > currentP.getMax()) currentP.setMax(nowDouble);
                        }
                    }
                }
            }else{      //˵������������
                if(!currentP.getTypes().contains(current))currentP.getTypes().add(current);

            }

        }

    }

    /**
     * ������Ϣ����
     * @param f
     * @param maxlength
     * @throws IOException
     */
    public static void buildStandardList(File f,int maxlength) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(f));

        String content = br.readLine();
//        System.out.println(content.trim());
        int counts = 0;

        if(content!=null){          //����ȷ�ϸ��еı�׼��Ϣ
            standardList =  confirmDataType(content.trim());        //ȫ�ֵľ�̬���� ��������������data������������Ϣ
            dealWithData(content.trim(),maxlength);           //�����һ��
        }
//        for(TypeInfo p :standardList){
//            System.out.println(p.getType());
//        }

        while(content!=null){
            content = br.readLine();
            if(content ==null )break;
//            System.out.println(content.trim());
            dealWithData(content.trim(),maxlength);           //����ÿһ��
        }

        br.close();

    }

    public static void checkFile(File f){
        if(!f.exists()){
            System.err.println(f.getPath()+"  �ļ������ڣ�\n ������� ���飡");
            System.exit(0);
        }
    }

    public static  List<TypeInfo> pointCut( String filePath,List<String> meansNull) throws IOException {

        System.out.println("Ŭ�����ɱ�׼��Ϣ�У����Ժ�\n ����\n ����");
        File f = new File(filePath);
        checkFile(f);

        meansNll  = meansNull;  //��ʾ����Ϊ�յ�ֵ���߲����д��������

        buildStandardList(f, commonValue.maxLength);
        List<TypeInfo> resultss =  vertifiedResult(commonValue.maxLength,commonValue.myLength);
        System.out.println("�ɹ����ɱ�׼��Ϣ ��\n");
        displayList();

        return resultss;

    }

    public static void main(String[] args) throws IOException {
//

//        //��ʶΪ�յķ���
//        //meansNll = new ArrayList<String>();
//        //meansNll.add("?");//��ζ�� Ϊ������ȱʧ
//        buildStandardList(f,7);
//        displayList();
//        vertifiedResult(7,5);
//        displayList();
        pointCut("C:\\Users\\Hi\\Desktop\\data2.txt",null);
    }

}
