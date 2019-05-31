package cesudu;

import DataSet_package.DataPreprocess;
import DataSet_package.IDS_KGIRAV2;
import DataSet_package.IDS_THA;
import ExcelTest2.standard.entity.commonValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class test_KGIRAV2 {

	public static void doit(String fileName,timeCounter timer) {
		//����һ����̨���� ���м�ʱ
		Thread timerListen = new Thread(new timerThread(timer));
		timerListen.setDaemon(true);
		timerListen.start();


		// TODO Auto-generated method stub
		System.out.println("**********"+fileName+"��KGIRA������ʱ��"+"***********");

		long time[]=new long[8];
		int t=0;//time�±�

		List<Integer> preRED=null;
	    List<Integer> nowRED = null;

		// ����DataPreprocess����data1
	       DataPreprocess data1=new DataPreprocess(fileName);
	       int k=0,m1=data1.get_origDSNum()/10,m=m1>0?m1:1;
	       data1.dataRatioSelect(0.2);

	       preRED=new ArrayList<Integer>();
	       nowRED=new ArrayList<Integer>();

	       ArrayList<ArrayList<Integer>> origData=data1.get_origDS();//�õ���ʼ����
	       ArrayList<ArrayList<Integer>> addData=data1.get_addDS();//�õ���������

	       data1=null;

	       Iterator<ArrayList<Integer>> value = addData.iterator();

	       IDS_THA im1=new IDS_THA(origData);
	       preRED=im1.THA();
	       System.out.println("��ʼԼ��"+preRED);

	       IDS_KGIRAV2 im2=new IDS_KGIRAV2();
	       ArrayList<Integer> temp=new ArrayList<Integer>();
	       long startTime = System.currentTimeMillis();   //��ȡ��ʼʱ��

		   IDS_KGIRAV2.C = null; //ȫ����ʼ��
		  int idd = 0;
		   IDS_KGIRAV2.get_UUUxRelativeP = new ArrayList<>();		//Լ������ʼ��
	       	while (value.hasNext()) {
//
			   if(!timer.isNowRun()){	//timer.isNowRun=false ��ζ�ų�ʱ��
				break;
				}
			   idd++;
//			   System.out.println("��:"+idd);
			    temp.addAll(value.next());
			  //  System.out.println(temp);
				im2.setUn(origData);
				im2.setUx(temp);
				nowRED=im2.KGIRA(im2.getUn(), preRED, im2.getUx());
				preRED.clear();
				preRED.addAll(nowRED);
				k++;
				if(k%m==0){
					long endTime=System.currentTimeMillis(); //��ȡ����ʱ��
					time[t++]=endTime-startTime;//���ÿ��� 10% ������Լ���ʱ��
				}
				origData.add(temp);
				temp = new ArrayList<Integer>();
				if(!timer.isNowRun()){	//timer.isNowRun=false ��ζ�ų�ʱ��
					break;
				}

			}
		   if(!timer.isNowRun()){
			   System.out.println("���������Ѿ��������� "+(commonValue.maxRunTime/1000)+"��,�Ѿ���ʱ ����������һ������");
		   }else{
		   for(int i=0;i<8;i++)
		   	System.out.print((double)time[i]/1000+"s ");
		   Collections.sort(nowRED);
		   System.out.println("\n"+fileName+"��KGIRA��Լ��Ϊ��"+nowRED+"\n");
		   }
	}
}
