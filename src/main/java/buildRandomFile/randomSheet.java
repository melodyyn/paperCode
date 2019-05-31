package buildRandomFile;

import java.io.FileInputStream; 
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class randomSheet {
	
	/**
	 * ��excel�ж�ȡ���ݵ�List<Map<String, Integer>> ��
	 * @param fileName
	 * @return
	 */
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

        int rows = results.get(0).size();	//����
        
        System.out.println("lines is :"+results.size());
        System.out.println("width is :"+rows);
        String[] titles = new String[rows];
       
        for(int i =0;i<rows;i++){
            titles[i]=(i+1)+"";
        }

        System.out.println("try my best to produce����");
        
        
       
        Export.buildSXSSFExportExcelWorkBook()
        .createSheet()
        .setColumnFields(titles)
        .importData(results, (value, record, cellHandler, field, rowCursor, index) -> {

          
   
                //��ֵ��ʽ������ֵ����
                return Integer.parseInt(value == null ? "" : value.toString());
         
          
        })//��������,֧�ֶ�ε���
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
     * �����
     * @param contents
     * @return
     */
    public static List<Map<String, Integer>> randomBuildList(List<Map<String, Integer>> contents){
    	List<Map<String, Integer>> contentsR = new ArrayList<Map<String, Integer>>();			//������
    	int size = contents.size();
    	int now;
    	for(int i=0;i<size;i++){
    		now = (int)(Math.random()*contents.size());
    		contentsR.add(contents.get(now));
    		contents.remove(now);
    	}
    	
    	return contentsR;
    	
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
		
		List<Map<String, Integer>> contentsR = randomBuildList(contents);
		displayAll(contentsR);
		buildExcelFile(savePath, contentsR);
    }
    
    /**
     * pointCut(Ҫ������ļ�λ�ã�������ɺ����ɵ��ļ���ŵ�λ��)
     * @param args
     * @throws ExcelException
     * @throws IOException
     */
	public static void main(String[] args) throws ExcelException, IOException {
		
		pointCut("saveResult\\SCADI-Dataset.xlsx","SaveRandom\\RSCADI-Dataset.xlsx");
		
	}
}
