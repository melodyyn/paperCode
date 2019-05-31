//�þ�̬��ά����ģ���������ʽ��
package DataSet_package;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class IDS_THAV00 {
	int Un[][]=null;
	
	public IDS_THAV00(int a[][]){
		Un=a;
	}
	
	//����ʵ������Un
	public int[][] getUn(){
		return Un;
	}				
	
	//����ʵ������Un
	public void setUn(ArrayList<ArrayList<Integer>> nData){
		int row=nData.size(),col=nData.get(0).size();
		Un=new int[row][col];
		for(int i=0;i<row;i++)
			for(int j=0;j<col;j++)
				Un[i][j]=nData.get(i).get(j);
	}
	
	//���캯����������ʵ��������ֵ
	public IDS_THAV00(ArrayList<ArrayList<Integer>> orgData){
		setUn(orgData);	
	}
	
	//�����Լ�P�µ����ݾ���
	public int[][] get_ToleranceMatrix(int[][] Un,List<Integer> P){
		int k=0,n=Un.length,m=P.size();
		int ToleranceMatrix[][]=new int[n][n];
		int attribute[]=new int[m];
		//����Iteratorʵ�ֱ���
		int s=0;
		Iterator<Integer> value = P.iterator();
		while (value.hasNext()) {
			attribute[s]= value.next();			
		    
		    s++;
		}   
		
		for(int i=0;i<n;i++)
			ToleranceMatrix[i][i]=1;	    

		
		for(int i=0;i<n;i++)
			for(int j=i+1;j<n;j++){
				k=0;
				for(int v=0;v<m;v++)            
					if( (Un[i][attribute[v]]!=Un[j][attribute[v]]) && (Un[i][attribute[v]]!=-1) && (Un[j][attribute[v]]!=-1))
						break;
					else
						k=k+1;
               
				if(k==m){
					ToleranceMatrix[i][j]=1;
					ToleranceMatrix[j][i]=1;
				}
			}

		return ToleranceMatrix;
	}
	
	//�����ϵͳDS�����Լ�P�µ�֪ʶ����GP
	public double get_GP(int[][] Un,List<Integer> P){
		int n=Un.length;
  
		int ToleranceMatrix[][]=get_ToleranceMatrix(Un,P);
		long sum=0;
		for (int i = 0; i < ToleranceMatrix.length; i++) {
			for (int j = 0; j < ToleranceMatrix[i].length; j++) {
          	
          	sum+=ToleranceMatrix[i][j];

			}
		}		
		//System.out.println("sum= "+sum);
		return sum;///Math.pow(n,2);
	}

	public double get_GPn2(int[][] Un,List<Integer> P){
		int n=Un.length;

		int ToleranceMatrix[][]=get_ToleranceMatrix(Un,P);
		long sum=0;
		for (int i = 0; i < ToleranceMatrix.length; i++) {
			for (int j = 0; j < ToleranceMatrix[i].length; j++) {

				sum+=ToleranceMatrix[i][j];

			}
		}
		//System.out.println("sum= "+sum);
		return sum/Math.pow(n,2);///Math.pow(n,2);
	}
	
	//�����ϵͳDS�����Լ�P�µ����֪ʶ����GP(D|P)
	public double get_GPRelative(int[][] Un,List<Integer> P){
		int m=Un[0].length;//
		List<Integer> PUD= new ArrayList<Integer>();
		Iterator<Integer> value = P.iterator();
		while (value.hasNext()) {
			PUD.add(value.next());
		}

		PUD.add(m-1);
//		System.out.println(get_GP(Un,P));
//		System.out.println(get_GP(Un,PUD));
//		System.out.println(get_GPn2(Un,P));
//		System.out.println(get_GPn2(Un,PUD));
		return get_GP(Un,P)-get_GP(Un,PUD);
	}
	
	//�������ϵͳDS����a�����Լ�P���ڲ���Ҫ��Sig_U_SigInner(a,P,D)=GP(D|P-{a})-GP(D|P)
	public double get_SigInner(int[][] Un,int a,List<Integer> P){
		List<Integer> P_D= new ArrayList<Integer>();
		
		Iterator<Integer> value = P.iterator();
		while (value.hasNext()) {
			int num=value.next();
			if(num!=a)
				P_D.add(num);
		}
		double k=get_GPRelative(Un,P_D);
		double s=get_GPRelative(Un,P);
		return(k-s);
	}
	
	//�������ϵͳDS����a�����Լ�P���ⲿ��Ҫ��Sig_U_Outer(a,P,D)=GP(D|P)-GP(D|PU{a})
	public double get_SigOuter(int[][] Un,int a,List<Integer> P){
		double k=get_GPRelative(Un,P);
		
		List<Integer> PUa= new ArrayList<Integer>();
		PUa.addAll(P);			
		PUa.add(a);
		
		double s=get_GPRelative(Un,PUa);
		return (k-s);
	}
	
	//�������ϵͳDSԼ��Ĵ�ͳ����THA
	public List<Integer> THA( ){
		List<Integer> RED=new ArrayList<Integer>();
			int m=Un[0].length;//System.out.println(Un.length +"@@@");
			List<Integer> P= new ArrayList<Integer>();
			for(int i=0;i<m-1;i++)//��������������
				P.add(i);
			//System.out.println(P);
			double gp_relativeC=get_GPRelative(Un,P);//GP(D|C)
			
			//System.out.println("�׶�1���");
			Iterator<Integer> value = P.iterator();
			while (value.hasNext()) {
				int num=value.next();
				if(get_SigInner(Un,num,P)>0)
					RED.add(num);
			}
			
			List<Integer> B= new ArrayList<Integer>();
			B.addAll(RED);
			List<Integer> temp= new ArrayList<Integer>(); 

			while (get_GPRelative(Un,B)!=gp_relativeC){		
				temp.addAll(P);				
				temp.removeAll(B);
			    Iterator<Integer> v1 = temp.iterator();
			    double preSigOuter=Double.NEGATIVE_INFINITY;
			    int prenum=-1;//���Ա�Ŵ�0��ʼ���������Ա�Ŷ������-1
				while (v1.hasNext()) {
					int num=v1.next();
					double gm=get_SigOuter(Un,num,B);//System.out.print(prenum+"///");
					if(preSigOuter < gm){
						prenum=num;
						preSigOuter=gm;
					}
				}
				
				B.add(prenum);//System.out.println(B);
				//System.out.println("�׶�3-"+(i++)+"���");
			    temp= new ArrayList<Integer>(); //.clear();
			}
			//Collections.sort(B);
			
			//Լ����С��
			List<Integer> temp2= new ArrayList<Integer>(); 
			temp2.addAll(B);
			Iterator<Integer> v2 = temp2.iterator();
			List<Integer> temp3= new ArrayList<Integer>();
			while(v2.hasNext()){
				Object objnum=v2.next();
				temp3.addAll(B);
				temp3.remove(objnum);			
				if(get_GPRelative(Un,temp3)==gp_relativeC)
					B.remove(objnum);
				temp3.clear();
				
			}

			return B;
	}
}