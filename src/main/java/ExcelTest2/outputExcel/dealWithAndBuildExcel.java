package ExcelTest2.outputExcel;

import ExcelTest2.standard.buildStandard;
import ExcelTest2.standard.entity.TypeInfo;
import ExcelTest2.standard.entity.commonValue;
import com.avalon.holygrail.excel.exception.ExcelException;
import com.avalon.holygrail.excel.norm.ExcelWorkBookExport;
import com.avalon.holygrail.util.Export;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * auth:lht
 * time: 2018��11��23��17:44:14
 */
public class dealWithAndBuildExcel {



    public static List<TypeInfo> standardsList = null;
    public static List<String> dontUse = null;
    public static List<Map<String,Integer>> excelDatas;

    public static int width = 0;
    /**
     * ���ڵ�ǰ���ݽ��з���
     * @param data
     * @param index
     * @return
     */
    public static int findAndDeal(String data,int index){
        TypeInfo p = standardsList.get(index);              //��ȡ��׼


        if(p.getOver()!=null && p.getOver().equals("yes")){                      //˵���Ǹ������˵� �������ɢ��
            double dataD = Double.parseDouble(data);         //ת��double
            double min = p.getMin();
            double dValue = p.getDvalue();
            int result = (int)((dataD-min)/dValue)+1;
            
            if(result>commonValue.myLength)result=commonValue.myLength;
            
            return result;
        }else{
            int indexof = p.getTypes().indexOf(data);
            indexof++;

            return indexof;
        }

    }

    public static boolean lossDeal(){
        int  a = (int)(Math.random()*100)+1; //1-100
        return a<commonValue.lossPercent?true:false;        //true����ʧ
    }

    /**
     * ��һ�����ݽ��д���
     * @param content
     * @return
     */
    public static Map<String,Integer> dealWith(String content){
        Map<String,Integer> mp = new HashMap<String,Integer>();//����һ�еĽ��

        String[] strs = content.split(commonValue.splitFlage);
        int length = strs.length;
        if(width == 0){
            width = length;
        }
//        System.out.println(length);
//        for(String s :strs) System.out.print(s+"|");
//        System.out.println();

        for(int i =0;i<length;i++){
//            if(lossDeal())continue;             //��ʧ������
            String current = strs[i];
            if(dontUse!=null && dontUse.contains(current)) continue;

            int result = findAndDeal(current.trim(),i);
            mp.put(i+1+"",result);
        }
        return mp;
    }

    /**
     * ��ȡ����
     * @param f
     * @throws IOException
     */
    public static List<Map<String,Integer>> inputDatas(File f) throws IOException {
        excelDatas = new ArrayList<Map<String,Integer>>();

        BufferedReader br = new BufferedReader(new FileReader(f));

        String content = "";
        int n =1;
        while(content!=null){

            content = br.readLine();
            if(content == null)break;

//            System.out.println(content.trim());

            excelDatas.add(dealWith(content.trim()));
        }

        br.close();
        return excelDatas;

    }

    public static void display(List<Map<String,Integer>> mp){
        int i =0;
        for(Map<String,Integer> p :mp){
            System.out.println(p);
            i++;
        }
        System.out.println("��������"+i);

    }

    public static void buildExcelFile(String filePath,List<Map<String, Integer>> results) throws ExcelException, IOException {

        int size = width;
        System.out.println("lines is :"+results.size());
        System.out.println("width is :"+width);
        String[] titles = new String[size];
        //������
        for(int i =0;i<size;i++){
            titles[i]=(i+1)+"";
        }

        System.out.println("Ŭ�������С���");

        Export.buildSXSSFExportExcelWorkBook()
        .createSheet()
        .setColumnFields(titles)
        .importData(results, (value, record, cellHandler, field, rowCursor, index) -> {

          
   
                //��ֵ��ʽ������ֵ����
                return Integer.parseInt(value == null ? "" : value.toString());
         
          
        })//��������,֧�ֶ�ε���
        .export(filePath); //������Excel

        System.out.println("�������");

    }
    
    public static void pointCut(String origFila,String overPath) throws IOException, ExcelException{
    	// buildStandard.
        //���ɱ�׼ filePath�������ļ�·�����������ɱ�׼��
        standardsList = buildStandard.pointCut(origFila,null);

        //����ƥ�� filapath�������ļ�·������������Excel)
        //filepath��ָҪ������������ڵ��ļ�
        //����Ӧ���Ǵ�����
        File f = new File(origFila);
        System.out.println("\n\n");

        List<Map<String,Integer>> results = inputDatas(f);  //��ȡ���ս������
        display(results);
        //����excel���  filepath �Ǳ���excel�ļ���·��
        buildExcelFile(overPath,results);
    	
    }
    
	/**
	 * * pointCut(Ҫ������ļ�λ�ã�������ɺ����ɵ��ļ���ŵ�λ��)
	 * @param args
	 * @throws IOException
	 * @throws ExcelException
	 */
    public static void main(String[] args) throws IOException, ExcelException {

        
        pointCut("data\\continousAdult.txt","saveResult\\continousAdult.xlsx");
    }
}
