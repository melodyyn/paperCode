//�þ�̬��ά����ģ���������ʽ��
package cesudu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import DataSet_package.DataPreprocess;
import DataSet_package.IDS_THAV00;

public class test_THAV00 {
	public static void doit(String fileName) {
		long time[]=new long[8];
		int t=0;//time�±�
		System.out.println("****������ʽ******"+fileName+"��THA������ʱ��"+"***********");
		
		List<Integer> RED=null;

		// ����DataPreprocess����data1        
       DataPreprocess data1=new DataPreprocess(fileName);
       
       int k=0,m1=data1.get_origDSNum()/10,m=m1>0?m1:1;
       data1.dataRatioSelect(0.2);
       
       RED=new ArrayList<Integer>();
             
       ArrayList<ArrayList<Integer>> origData=data1.get_origDS();//�õ���ʼ����
       ArrayList<ArrayList<Integer>> addData=data1.get_addDS();//�õ���������
       
       data1=null;
       Iterator<ArrayList<Integer>> value = addData.iterator();    
       
       long startTime = System.currentTimeMillis();   //��ȡ��ʼʱ��
       IDS_THAV00 im1=new IDS_THAV00(origData);
       RED=im1.THA();//System.out.println("\n"+fileName+"��20%����("+origData.size()+")THA��Լ��Ϊ��"+RED+"\n");
	   while (value.hasNext()) {
			origData.add(value.next());       
			im1.setUn(origData);
			RED=im1.THA();	//System.out.println(RED);
			
			k++;
			if(k%m==0){
				
				long endTime=System.currentTimeMillis(); //��ȡ����ʱ��             
				time[t++]=endTime-startTime;//���ÿ���10%������Լ���ʱ��
			}						
		}      
		
	for(int j=0;j<8;j++)
				System.out.print((double)time[j]/1000+" ");
	
	//Collections.sort(RED);
	System.out.println("\n"+fileName+"��THA��Լ��Ϊ��"+RED+"\n");
 
	}
}
