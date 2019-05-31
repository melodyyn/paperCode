package cesudu;

import DataSet_package.DataPreprocess;
import DataSet_package.IDS_POSD;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class test_POSD {
	public static void doit(String fileName) {
		// TODO Auto-generated method stub
		System.out.println("****������ʽ-���򣨼���******"+fileName+"��POSD������ʱ��"+"***********");
		long time[]=new long[8];
		int t=0;//time�±�
		
		List<Integer> preRED=null;
	    List<Integer> nowRED = null;

	 // ����DataPreprocess����data1        
	       DataPreprocess data1=new DataPreprocess(fileName);
	       
	       int k=0,sn=data1.get_origDSNum();
	       int m1=sn/10,m=m1>0?m1:1;
	       int count=sn-(int)(0.2*sn);//���������Ӷ����Լ����һ��
	       data1.dataRatioSelect(0.2);
	       
	       preRED=new ArrayList<Integer>();
	       nowRED=new ArrayList<Integer>();
	             
	       ArrayList<ArrayList<Integer>> addData=data1.get_origDS();//����λ�ã���ԭ����ǰ��20%���õ����棬Ϊ�˱������������һ��
	       ArrayList<ArrayList<Integer>> origData=data1.get_addDS();
	       origData.addAll(addData);	       	       
	       
	       data1=null;
		       
	       Iterator<ArrayList<Integer>> value = origData.iterator();
	       
	       ArrayList<ArrayList<Integer>> result_POS=new ArrayList<ArrayList<Integer>>();
	       ArrayList<Integer> POS_Reduct=new ArrayList<Integer>();
	       ArrayList<Integer> original_POS=new ArrayList<Integer>();
	       ArrayList<Integer> original_reduct=new ArrayList<Integer>();
	       int[][] original_sys;
	       ArrayList<Integer> P=new ArrayList<Integer>();
	       IDS_POSD im1=new IDS_POSD(origData);
	       
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

	       IDS_POSD im2=new IDS_POSD();
	       
	       ArrayList<Integer> temp=new ArrayList<Integer>();
	       long startTime = System.currentTimeMillis();   //��ȡ��ʼʱ��
		   while (value.hasNext()) {
			 	
			    temp.addAll(value.next());//System.out.println(temp);
				im2.setUn(origData);
				im2.setUx(temp);	
				result_POS=im2.SHU_IARS(im2.getUn(),original_reduct,original_POS,im2.getUx());	
								
			    original_reduct=result_POS.get(0);
			    original_POS=result_POS.get(1);
			     
				k++;
				count--;
				if(k%m==0){
					
					long endTime=System.currentTimeMillis(); //��ȡ����ʱ��             
					time[t++]=endTime-startTime;//���ÿ���10%������Լ���ʱ��
					if(t==8)
						t--;
				}
				value.remove();
				temp=new ArrayList<Integer>();//.clear();
				if (count==0)
					break;
			}
		   
		   for(int i=0;i<8;i++)				
			   System.out.print((double)time[i]/1000+" ");   

		   System.out.println("\n"+fileName+"��POSD��Լ��Ϊ��"+result_POS.get(0)+"\n");		
	}

}
