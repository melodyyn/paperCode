package ExcelTest;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * auth:lht
 * time: 2018��11��23��17:44:14
 * 1. ����.txt�ļ��������ɱ�׼���� ���������ݷ���
 * 2. ����ļ��Ƿ���ڲ�������رճ���
 */
public class ReadStandard {

    public static void dispalyArray(String[] args){
        for(String s :args){
            System.out.print(s+"|");
        }
        System.out.println();
    }

    /**
     * ��鴫��Ĳ����Ƿ���ڲ�������ֹͣ����
     * @param f
     */
    public static void checkFileIsExist(File f){
        if(!f.exists()){
            System.err.println("�ļ������ڣ���");
            System.exit(0);                 //��������
        }
    }



    /**
     * �Ӵ���Ĳ��ֽ�ȡ��׼�洢��������
     * @param content
     * @return
     */
    public static  String[] buildStandardList(String content){

        int start = content.indexOf(":");               //��ȡ ������Ĳ���
        content = content.substring(start+1);
        String[] standards = content.split(",");
        for(int i =0;i<standards.length;i++){
            standards[i]=standards[i].trim();           //ÿ��Ԫ��ȥ���ո�
        }


        return standards;
    }

    /**
     * ��ȡ�ļ����÷������ɱ�׼ Ȼ���������
     * @param filePath
     * @return
     * @throws IOException
     */
    public static  List<String[]> getContents(String filePath) throws IOException {
        List<String[]> standardsList = new ArrayList<String[]>();        //������б�׼����������

        File f = new File(filePath);//���б�׼��Ϣ���ļ�
        checkFileIsExist(f);            //����ļ��Ƿ����
        BufferedReader br = new BufferedReader(new FileReader(f));
        String flage ="";
        int counts = 0;                     //��¼��ǰ��׼����
        while(flage!=null){

            flage = br.readLine();          //һ��һ�ж�ȡ
            if(flage==null)break;
            standardsList.add(buildStandardList(flage));

        }
//      //����list
//        for(String[] ddd:standardsList){
//            dispalyArray(ddd);
//        }
//        System.out.println(standardsList.size());

        br.close();
        return standardsList;
    }

//    public static void main(String[] args) throws IOException {
//
//        getContents("C:\\Users\\Hi\\Desktop\\standard.txt");
//
//    }
}
