package ExcelTest;

import com.avalon.holygrail.excel.exception.ExcelException;
import com.avalon.holygrail.excel.exception.ExportException;
import com.avalon.holygrail.util.Export;

import ExcelTest2.standard.entity.commonValue;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * auth:lht
 * time: 2018��11��23��17:44:14
 */
public class buildExcel {

    //�������б�׼������
    public static List<String[]> standards;
    public static List<String> noExitst;

    /**
     * ������ʽ�ж��ǲ������� ����+1�� 1�� 1.1�� -1�� -1.1��
     * @param indexS
     * @return
     */
     public static boolean checkIsNumber(String indexS){
         Matcher m = Pattern.compile("[-|+]?[0-9]+\\.?[0-9]*").matcher(indexS);
        return m.matches();             //��������ƥ��Ľ��
     }

        @Test
        public void t12(){
            System.out.println(checkIsNumber("+1"));
        }

    /**
     * ����ƥ����
     * @param aim
     * @param arg
     * @return
     */
    public static int findIndex(String[] aim,String arg){
        int result =-1;
        int shut = 0;
        for(int i=0;i<aim.length;i++){

            if(noExitst.contains(aim[i])){
                shut++;
                continue;
            }

            //������������������ǲ�����������
            if(aim[i].equals("continuous")){
                if(checkIsNumber(arg))return i+1;       //�������򷵻�
                else continue;                          //����������ʲô������
            }

            if(arg.equals(aim[i])){
                result = i+1-shut;               //��1��ʼ�����±��һ
                break;
            }
        }
        return result;
    }


    public static Map<String,Integer> buildMap(String dataContent){

//        System.out.println(standards.size());
        String[] datas = dataContent.split(commonValue.conSplitFlage);
//        for(String s :datas){
//            System.out.print(s+"|");
//        }

        //������ݺͼ���׼������ƥ�����˳�����
        if(standards.size()!=datas.length){
            System.err.println("���ݵ������ǣ�"+datas.length+" ������ ����׼��������"+standards.size()+" ���������ļ��Ƿ���ȷ");
            System.exit(0);
        }

        Map<String,Integer> m = new HashMap<String,Integer>();      //��¼�����map
        String[] indexs;
        int result ;
        for(int i=0;i<datas.length;i++){
            if(datas[i].trim().equals("?"))continue;  //������� ,������Ĭ���ǿ�

            indexs = standards.get(i);                  //��ȡ��ǰ�����ı�׼ ������0��ʼ

            result = findIndex(indexs,datas[i].trim());

            if(result!=-1){                 //����-1���ʾ�ҵ���

                m.put((i+1)+"",result);   //���������
            }

        }
        return m;
        //System.out.println("\n"+m);

    }



    public static List<Map<String, Integer>> getDataContent(String filePath) throws IOException {
        List<Map<String, Integer>> results = new ArrayList<>();    //���������洢�ȶ���Ϣ

        File f = new File(filePath);                            //���б�׼��Ϣ���ļ�
        ReadStandard.checkFileIsExist(f);                       //����ļ��Ƿ����
        BufferedReader br = new BufferedReader(new FileReader(f));
        String flage ="";
//        int counts = 1;                     //��¼��ǰ��׼����
        while(flage!=null){

            flage = br.readLine();          //һ��һ�ж�ȡ
            if(flage==null)break;
//            System.out.println(flage);
            results.add(buildMap(flage));            //�ŵ���������
//            counts++;
        }

        br.close();
        System.out.println(results.size());
        return results;
    }

    public static void buildExcelFile(String filePath,List<Map<String, Integer>> results) throws ExcelException, IOException {

        int size = standards.size();
        String[] titles = new String[size];
        //������
        for(int i =0;i<size;i++){
            titles[i]=(i+1)+"";
        }

        System.out.println("Ŭ�������С���");

        Export.buildSXSSFExportExcelWorkBook()//������ѯ����
                .createSheet("SheetTest")//����һ��Sheet,���Դ�������Sheet��
                .setColumnFields(titles)//�������ֶ�,��ӦMap���ϵ�key
                .importData(results, (value, record, cellHandler, field, rowCursor, index) -> {

                    
                	   
                    //��ֵ��ʽ������ֵ����
                    return Integer.parseInt(value == null ? "" : value.toString());
             
              
                })//��������,֧�ֶ�ε���
                .export(filePath);//������Excel

        System.out.println("�������");

    }
    
    static void pointCut(String satandard,String datas,String savePath) throws IOException, ExcelException{
    	 noExitst = new ArrayList<String>();
       //  noExitst.add("-");
       //  noExitst.add("--");

         //filepath��ָ��׼������Ϣ���ڵ��ļ� ��������3. steel:	-,R,A,U,K,M,S,W,V
         //һ��Ҫ�� : ������� Ȼ�������,�ָ�ÿ�����
         standards = ReadStandard.getContents(satandard);   //��ȡ��׼

         //filepath��ָҪ������������ڵ��ļ�
         //����Ӧ���Ǵ�����
         List<Map<String, Integer>> results =getDataContent(datas);    //���ݶԱȷ�������Map����

         //filepath ��ָexcel�ļ������λ��
         buildExcelFile(savePath,results);
    	
    }

    public static void main(String[] args) throws ExcelException, IOException {
    	pointCut("standards\\connect-4Standards.txt","data\\connect-4data.txt","saveResult\\connect-4data.xlsx");
    }

}
