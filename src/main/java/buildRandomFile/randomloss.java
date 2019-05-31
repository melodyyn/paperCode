package buildRandomFile;

import java.io.FileInputStream; 
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.avalon.holygrail.excel.bean.SXSSFExcelSheetExport.FormatterCell;
import com.avalon.holygrail.excel.exception.ExcelException;
import com.avalon.holygrail.excel.norm.CellStyle;
import com.avalon.holygrail.excel.norm.ExcelWorkBookExport;
import com.avalon.holygrail.util.Export;

import ExcelTest2.standard.entity.commonValue;

public class randomloss {
	
	/**
	 * ��excel�ж�ȡ���ݵ�List<Map<String, Integer>> ��
	 * @param fileName
	 * @return
	 */
	public static int allRows;
	public static int allCols;
	public static int cutsize;
	
    public static List<Map<String, Integer>> readExcelRandom(String fileName) {     	 
    	List<Map<String, Integer>> arrList=new ArrayList<Map<String, Integer>>();
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
					Map<String, Integer> temp_arrRowm = new HashMap<String, Integer>();
					
					row = sheet.getRow(i);
					if (row != null) {
						for (int j = 0; j < colnum; j++) {
							Cell cellinfo=row.getCell(j);//System.out.println(j);   
		                    if (cellinfo==null){
		                    temp_arrRowm.put((j+1)+"", new Integer(-1));
		                    } else{
		                    	temp_arrRowm.put((j+1)+"", new Integer((int) cellinfo.getNumericCellValue()));
		                    }
						}
					} else {
						break;
					}
					arrList.add(temp_arrRowm);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();				
     }

        return arrList;
    } 
	/**
	 * ����excel 
	 * @param filePath ����·��
	 * @param results	���ݼ���
	 * @throws ExcelException
	 * @throws IOException
	 */
    public static void buildExcelFile(String filePath,List<Map<String, Integer>> results) throws ExcelException, IOException {

        
        System.out.println("lines is :"+allRows);
        System.out.println("width is :"+allCols);
        String[] titles = new String[allCols];
       
        for(int i =0;i<allCols;i++){
            titles[i]=(i+1)+"";
        }

        System.out.println("try my best to produce����");
        
        
       
        Export.buildSXSSFExportExcelWorkBook()
        .createSheet()
        .setColumnFields(titles)
        .importData(results)//��������,֧�ֶ�ε���
        .export(filePath); //������Excel

       
        System.out.println("success!");

    }
    
    /**
     * ������
     * @param contents
     */
    public static void displayAll(List<Map<String, Integer>> contents){
    	System.out.println("rows!��"+contents.size());
    	
    	for(int i=0;i<contents.size();i++){
    		System.out.println(contents.get(i));
    	} 
    }
    
    /**
     * �ɵ�Ԫ��
     * @param contents
     * @return
     */
    public static List<Map<String, Integer>> randomBuildList(List<Map<String, Integer>> contents){
    	List<Map<String, Integer>> contentsR = new ArrayList<Map<String, Integer>>();			//������
    	 
    	int rows = contents.size();
    	int cols = contents.get(0).size();
    	allCols = cols;
    	allRows = rows;
    	
    	System.out.println( "�У� "+rows+" �У� "+cols);
    	int counts = (int) (rows * cols *(commonValue.lossPercent/100.0));		//Ҫȥ���ĸ���
    	Set<Integer> cutSet = new HashSet<>();
    	int col,row;
    	int aim;
    	while(true){
    		col = (int) (Math.random()*cols)+1;	//1--->cols
    		row = (int)(Math.random()*rows)+1;    //1--->rows
    		aim = col * row;					
    		cutSet.add(aim);					//��ջ
    		if(cutSet.size()>=counts)break;		//�����ﵽ��ok
    		
    	}
    	//�ɵ�Ԫ��
    	int cutNumber;
    	int cutCol,cutRow;
    	Iterator<Integer> itero = cutSet.iterator();
    	cutsize = cutSet.size();
    	while(itero.hasNext()){
    		
    	cutNumber =	itero.next();
    	
    	cutRow =  cutNumber/cols;	//�õ�Ҫ�ɵ����Ǹ���������
    	 
    	//if(cutRow>=1)cutRow--;
    	cutCol = cutNumber%cols+1;	//�õ�Ҫ�ɵ����Ǹ��ĺ�����
    	//if(cutCol>=1)cutCol--;
    	//if(cutCol>1)cutRow++;		
    	
    	System.out.println("��"+cutRow+"�У���"+cutCol+"��");
    	contents.get(cutRow).remove(cutCol+"");		//�ɵ�
    	}
    	
    	return contents;
    	
    }
    //public static readContne
    
    /**
     * 
     * @param origFile	Ҫ������ļ�
     * @param savePath	�ɹ�������ļ� must be xxx.xlsx
     * @throws ExcelException
     * @throws IOException
     */
    public static void pointCut(String origFile,String savePath) throws ExcelException, IOException{
    	List<Map<String, Integer>> results;
		List<Map<String, Integer>> contents = readExcelRandom(origFile);
		
		List<Map<String, Integer>> contentscut = randomBuildList(contents);
		
//		displayAll(contentsR);
		buildExcelFile(savePath, contentscut);
		System.out.println("�ɵ�������Ϊ��"+cutsize);
    }
    
    /**
     * pointCut(Ҫ������ļ�λ�ã�������ɺ����ɵ��ļ���ŵ�λ��)
     * @param args
     * @throws ExcelException
     * @throws IOException
     */
	public static void main(String[] args) throws ExcelException, IOException {
		
		pointCut("lossReady\\connect-4dataReady.xlsx","saveLoss\\connect-4dataOver.xlsx");
		
	}
}
