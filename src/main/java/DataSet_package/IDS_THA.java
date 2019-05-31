package DataSet_package;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import cesudu.staticResultMine;
import org.junit.Test;

public class IDS_THA {
	int Un[][]=null;
	
	public IDS_THA(int a[][]){
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
	public IDS_THA(ArrayList<ArrayList<Integer>> orgData){
		setUn(orgData);
	}
	
	//�����Լ�P�µ����ݾ���
	public int[][] get_ToleranceMatrix(int[][] Un,List<Integer> P){
		int k,n=Un.length,m=P.size();
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
		int sum=0;
		for (int i = 0; i < ToleranceMatrix.length; i++) {
			for (int j = 0; j < ToleranceMatrix[i].length; j++) {
          	sum+=ToleranceMatrix[i][j];
			}
		}		
		//System.out.println("sum= "+sum);
		return sum;///Math.pow(n,2);
	}

	@Test
	public void testGetGP(){
		int[][] testArr = {{1, 1, 1, 0, 1}, {0, -1, 1, 0, 1}, {-1, -1, 0, 0, 0},
				{1, -1, 1, 1, 1}, {-1, -1, 1, 1, 2}, {0, 1, 1, -1, 1}};

		List<Integer> p = new ArrayList<>();
		for(int i = 0; i < 4; i++){
			p.add(i);
		}
		int[][] testTolerance = get_ToleranceMatrix(testArr,p);
		for (int[] a:testTolerance){
			for (int i:a
				 ) {
				System.out.print(i + " ");
			}
			System.out.println();
		}
		double testGP = get_GP(testArr, p);
		System.out.println(testGP);
	}

	
	//�����ϵͳDS�����Լ�P�µ����֪ʶ����GP(D|P)
	public double get_GPRelative(int[][] Un,List<Integer> P){
			int m=Un[0].length;//
			List<Integer> PUD= new ArrayList<Integer>();
			PUD.addAll(P);
			PUD.add(m-1);
			return get_GP(Un,P) - get_GP(Un,PUD);
	}

	@Test
	public void testGPRelative(){
		int[][] testArr = {{1, 1, 1, 0, 1}, {0, -1, 1, 0, 1}, {-1, -1, 0, 0, 0},
				{1, -1, 1, 1, 1}, {-1, -1, 1, 1, 2}, {0, 1, 1, -1, 1}};

		List<Integer> p = new ArrayList<>();
		for(int i = 0; i < 4; i++){
			p.add(i);
		}
		double gpr = get_GPRelative(testArr, p);
		System.out.println(gpr);
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

	@Test
	public void testGetSigIn(){
		int[][] testArr = {{1, 1, 1, 0, 1}, {0, -1, 1, 0, 1}, {-1, -1, 0, 0, 0},
				{1, -1, 1, 1, 1}, {-1, -1, 1, 1, 2}, {0, 1, 1, -1, 1}};

		List<Integer> p = new ArrayList<>();
		for(int i = 0; i < 4; i++){
			p.add(i);
		}
		double sigIn = get_SigInner(testArr, 2, p);
		System.out.println(sigIn);
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
	
	//�������ϵͳ DS Լ��Ĵ�ͳ����THA
	public List<Integer> THA( ){
		List<Integer> RED=new ArrayList<Integer>();
			int m=Un[0].length;//System.out.println(Un.length +"@@@");
			
	//		System.out.println("20% is :"+Un.length);
			
	//		staticResultMine.percent20 = Un.length;					//��ǰ�ٷ�֮��ʮ�Ľ�������ȥstaticResultMine
			
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
//			System.out.println("�ˣ�" + RED);
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
			    temp.clear();
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

	static ArrayList<ArrayList<Integer>> arr2List(int[][] arr){
		ArrayList<ArrayList<Integer>> result = new ArrayList<>();
		for (int[] i:arr
			 ) {
			ArrayList<Integer> temp = new ArrayList<>();
			for (int j:i
				 ) {
				temp.add(j);
			}
			result.add(temp);
		}
		return result;
	}
}