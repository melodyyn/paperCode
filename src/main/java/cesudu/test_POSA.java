package cesudu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import DataSet_package.DataPreprocess;
import DataSet_package.IDS_KGIRA;
import DataSet_package.IDS_POSA;
import DataSet_package.IDS_THA;

public class test_POSA {
	public static void doit(String fileName) {
		// TODO Auto-generated method stub
		System.out.println("****������ʽ-��������******"+fileName+"��POSA������ʱ��"+"***********");
		
		long time[]=new long[8];
		int t=0;//time�±�
		
		// ����DataPreprocess����data1        
	       DataPreprocess data1=new DataPreprocess(fileName);
	       int k=0,m1=data1.get_origDSNum()/10,m=m1>0?m1:1;
	       data1.dataRatioSelect(0.2);	       
	             
	       ArrayList<ArrayList<Integer>> origData=data1.get_origDS();//�õ���ʼ����
	       ArrayList<ArrayList<Integer>> addData=data1.get_addDS();//�õ���������
	       
	       data1=null;
	       
	       Iterator<ArrayList<Integer>> value = addData.iterator();       
	       
	       ArrayList<ArrayList<Integer>> result_POS=new ArrayList<ArrayList<Integer>>();
	       ArrayList<Integer> POS_Reduct=new ArrayList<Integer>();
	       ArrayList<Integer> original_POS=new ArrayList<Integer>();
	       ArrayList<Integer> original_reduct=new ArrayList<Integer>();
	       int[][] original_sys;
	       ArrayList<Integer> P=new ArrayList<Integer>();
	       IDS_POSA im1=new IDS_POSA(origData);
	       
	       POS_Reduct=im1.getPOS_Reduct(im1.getUn());	       
	       original_POS=im1.getPOS(im1.getUn(),POS_Reduct);
	       
	       original_reduct.addAll(POS_Reduct);
	       
	       //original_sys=im1.getUn();
	       int n1=im1.getUn().length;//ԭ����ϵͳUn����n������
	       int s1=im1.getUn()[0].length;//m�����ԣ�������������	
	       original_sys=new int[n1][s1];
	       for(int i=0;i<n1;i++)
	    	   for(int j=0;j<s1;j++)
	    		   original_sys[i][j]=im1.getUn()[i][j];
	       
	       IDS_POSA im2=new IDS_POSA();
	       
	       ArrayList<Integer> temp=new ArrayList<Integer>();
	       long startTime = System.currentTimeMillis();   //��ȡ��ʼʱ��
		   while (value.hasNext()) {
			 	
			    temp.addAll(value.next());
//				im2.setUn(origData);
//				im2.setUx(temp);	
				result_POS=im2.SHU_IARS(original_sys,original_reduct,original_POS,temp);	
				
				//�ѵ��������������ԭ����ϵͳ�õ��¾���ϵͳnew_sys
				int n=original_sys.length;//ԭ����ϵͳUn����n������
				int s=original_sys[0].length;//m�����ԣ�������������	
				int[][] sys_temp=new int[n+1][s];
				for(int i=0;i<n;i++)
					for(int j=0;j<s;j++)
						sys_temp[i][j]=original_sys[i][j];
				for(int i=0;i<s;i++)
					sys_temp[n][i]=temp.get(i);
			    original_sys=sys_temp;			     
			    original_reduct=result_POS.get(0);
			    original_POS=result_POS.get(1);
			     
				k++;				
				if(k%m==0){
					
					long endTime=System.currentTimeMillis(); //��ȡ����ʱ��             
					time[t++]=endTime-startTime;//���ÿ���10%������Լ���ʱ��
				}
	
				temp=new ArrayList<Integer>();//.clear();
			}
		   
		   for(int i=0;i<8;i++)				
			   System.out.print((double)time[i]/1000+" ");

		   Collections.sort(result_POS.get(0));
		   System.out.println("\n"+fileName+"��POSA��Լ��Ϊ��"+result_POS.get(0)+"\n");		
	}
}
