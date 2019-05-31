package DataSet_package;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DataPreprocess {
	ArrayList<ArrayList<Integer>> origDS;//Ϊ�˽�ʡ�ڴ�ռ䣬�����������ʱorigDS��ԭʼ���ݣ����÷���dataRatioSelect(double percent)���Ϊ��ʼ���ݡ�
	ArrayList<ArrayList<Integer>> addDS;
	
	public int get_origDSNum(){
		return origDS.size();
	}
	
	public DataPreprocess(String fileName){//���캯��
		origDS=readExcel(fileName);
		addDS=new ArrayList<ArrayList<Integer>>();
	}
	
	//������������addDS
	public void dataRatioSelect(double percent){
		int origNum=origDS.size();
		int num=(int)(origNum*percent);
		for(int i=num;i<origNum;i++){
			addDS.add(origDS.get(i));
		}
		
		for(int i=0;i<origNum-num;i++){
			origDS.remove(num);
		}		
	}
	
	
	public ArrayList<ArrayList<Integer>> get_origDS(){//�õ���ʼ����origDS
		return origDS;
	}
	
	public ArrayList<ArrayList<Integer>> get_addDS(){//�õ���������addDS
		return addDS;
	}
	// ��Excel�ķ���readExcel���÷�������ڲ���Ϊһ���ļ�·��  
    public static ArrayList<ArrayList<Integer>> readExcel(String fileName) {
    	ArrayList<ArrayList<Integer>> arrList=new ArrayList<ArrayList<Integer>>();
    	Sheet sheet = null;
		Row row = null;
		Workbook wb = null;

		String extString = fileName.substring(fileName.lastIndexOf("."));
		InputStream is = null;
		
        try {  
            // ��������������ȡExcel  
           is = new FileInputStream(fileName);  
           if (".xls".equals(extString)) {
				wb = new HSSFWorkbook(is);
			} else if (".xlsx".equals(extString)) {
				wb = new XSSFWorkbook(is);
			} else {
				wb = null;
			}
           
           if (wb != null) {
        	// ��ȡ��һ��sheet
				sheet = wb.getSheetAt(0);// ���ǵ�Ч�ʣ�ÿ���ļ�ֻ��һ��sheet��Ϊ�䴴��һ��Sheet����
				// ��ȡ�������
				int rownum = sheet.getPhysicalNumberOfRows();
				
				row = sheet.getRow(0);
				// ��ȡ�������
				int colnum = row.getLastCellNum();//System.out.println(colnum+"$$");
				for (int i = 0; i < rownum; i++) {
					ArrayList<Integer> temp_arrRow=new ArrayList<Integer>();
					row = sheet.getRow(i);
					if (row != null) {
						for (int j = 0; j < colnum; j++) {
							Cell cellinfo=row.getCell(j);//System.out.println(j);   
		                    if (cellinfo==null)
		                    	temp_arrRow.add(new Integer(-1));//�� -1 ��������ȱʧ
		                    else
		                    	temp_arrRow.add(new Integer((int) cellinfo.getNumericCellValue()));
						}
						
					} else {
						break;
					}
					arrList.add(temp_arrRow);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();				
     }

        return arrList;
    } 
    
//    //��ArrayList����ת��Ϊ��ά���鷵��
//    public int[][] arrListToArr(String fileName){
//    	ArrayList<Integer> dataArrList=readExcel(fileName);
//    	int n=dataArrList.size(),m=get_col();
//    	int arr[][]=new int[get_row()][m];
//    	for(int i=0,t=0,j=0;i<n;i++){
//    		arr[t][j%m]=dataArrList.get(i).intValue();
//     	   j++;
//     	   if (j%m==0)
//     		   t++;
//        }
//    	return arr;
//    }

}
