package DataSet_package;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class IDS_KGIRAV2 {
	int Un[][]=null;
	int Ux[]=null;

	public IDS_KGIRAV2(int a[][], int b[]){
		Un=a;Ux=b;
	}

	public IDS_KGIRAV2(){

	}

	//����ʵ������Un
	public int[][] getUn(){
		return Un;
	}

	//����ʵ������Ux
	public int[] getUx(){
		return Ux;
	}

	//����ʵ������Un
	public void setUn(ArrayList<ArrayList<Integer>> nData){
		int row=nData.size(),col=nData.get(0).size();
		Un=new int[row][col];
		for(int i=0;i<row;i++)
			for(int j=0;j<col;j++)
				Un[i][j]=nData.get(i).get(j);
	}

	//����ʵ������Ux
	public void setUx(ArrayList<Integer> xData){
		int col=xData.size();
		Ux=new int[col];
		for(int i=0;i<col;i++)
				Ux[i]=xData.get(i);
	}

	//���캯����������ʵ��������ֵ
	public IDS_KGIRAV2(ArrayList<ArrayList<Integer>> orgData, ArrayList<Integer> addData){
		setUn(orgData);
		setUx(addData);		
	}

	private static boolean getDiffrent2(List<Integer> list1, List<Integer> list2) {
		return !list1.retainAll(list2);
	}

	static int count = 0;
	static HashMap<int[][],HashMap<List<Integer>,int[][]>> toleranceMatrixs = new HashMap<>();
	//�����Լ�P�µ����ݾ���
	public static int[][] get_ToleranceMatrix(int[][] Un,List<Integer> P){
//		if(toleranceMatrixs.containsKey(Un)){
//			for (HashMap.Entry<List<Integer>,int[][]> map:toleranceMatrixs.get(Un).entrySet()
//				 ) {
//				if(map.getKey().containsAll(P)){
//					return map.getValue();
//				}
//			}
//		}
//		count++;
		int k=0,n=Un.length,m=P.size();
		int ToleranceMatrix[][]=new int[n][n];
		int attribute[]=new int[m];
		//����Iteratorʵ�ֱ���
		int s=0;
		for (Integer aP : P) {
			attribute[s] = aP;
			s++;
		}   
		for(int i=0;i<n;i++)
			ToleranceMatrix[i][i]=1;
		
		for(int i=0;i<n;i++){
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
		}
//		HashMap<List<Integer>,int[][]> toleranceMatrix = new HashMap<>();
//		toleranceMatrix.put(P, ToleranceMatrix);
//		toleranceMatrixs.put(Un, toleranceMatrix);
		return ToleranceMatrix;
	}
	
	//�����ϵͳDS�����Լ�P�µ�֪ʶ����GP
	public static double get_GP(int[][] Un,List<Integer> P){
		int ToleranceMatrix[][]=get_ToleranceMatrix(Un,P);
		int sum=0;
		for (int[] aToleranceMatrix : ToleranceMatrix) {
			for (int anAToleranceMatrix : aToleranceMatrix) {
				sum += anAToleranceMatrix;
			}
		}		
		return sum;///Math.pow(n,2);
	}
	
	//�����ϵͳ DS �����Լ� P �µ����֪ʶ���� GP(D|P)
	public static double get_GPRelative(int[][] Un,List<Integer> P){
		int m=Un[0].length;//
		List<Integer> PUD = new ArrayList<Integer>(P);
		PUD.add(m-1);
		return get_GP(Un,P) - get_GP(Un,PUD);//k1-k2;
	}
	
	//�������ϵͳDS����a�����Լ�P���ڲ���Ҫ��Sig_U_SigInner(a,P,D)=GP(D|P-{a})-GP(D|P)
	public double get_SigInner(int[][] Un,int a,List<Integer> P){
		List<Integer> P_D= new ArrayList<Integer>();
		for (Integer num : P) {
			if (num != a)
				P_D.add(num);
		}
		double k=get_GPRelative(Un,P_D);
		double s=get_GPRelative(Un,P);
		return(k-s);
	}
	
	//�������ϵͳDS����a�����Լ�P���ⲿ��Ҫ��Sig_U_Outer(a,P,D)=GP(D|P)-GP(D|PU{a})
	public static double get_SigOuter(int[][] Un,int a,List<Integer> P){
		double k=get_GPRelative(Un,P);

		List<Integer> PUa = new ArrayList<Integer>(P);
		PUa.add(a);
		
		double s=get_GPRelative(Un,PUa);
		return (k-s);
	}
	

	/**
	 * �������ϵͳDS���Ӷ���Ux�������Լ�P�£���������Ux �� DS ��Ԫ�ص��ݲ����Ʃ��|Ux|=1,|DS|=n,�������ݲ����ά��Ϊ1xn,��һά������
	 * @param Un ԭ����ϵͳ
	 * @param Ux ��ӵĶ���
	 * @param P ���Լ�
	 * @return ��ӵĶ���� �����Լ�P�µ��ݲ����
	 */
	public static int[] get_PTC(int Un[][],int Ux[],List<Integer> P){
		int k,n=Un.length,m=P.size();
		int val_Ux,val_Un;
		int ToleranceMatrix[]=new int[n];
		int attribute[]=new int[m];
		int w=0;
		for (Integer aP : P) {
			attribute[w] = aP;
			w++;
		}
		for(int j=0;j<n;j++){
			k=0;				
			for(int v=0;v<m;v++){
				val_Ux=Ux[attribute[v]];
				val_Un=Un[j][attribute[v]];
				if( (val_Ux!=val_Un) && (val_Ux!=-1) && (val_Un!=-1))
					break;
				else
					k=k+1;
			}
			if(k==m){
				ToleranceMatrix[j]=1;
			}
		}
		return ToleranceMatrix;
	}	

	public static List<Integer> get_UUUxRelativeP;
	public static double get_GPRelativeUnPResult;

	public static boolean checkPandP(List<Integer> P){
		return P.equals(get_UUUxRelativeP);
	}
	//�������ϵͳ DS ���������� Ux ���Լ� C ����ھ������� D ��֪ʶ���� GP_UUUx(D|C)
	public static double get_UUUxRelative(int Un[][],int Ux[],List<Integer> P,int flag){
		int n=Un.length, m=Un[0].length;
		List<Integer> D=new ArrayList<Integer>();
		D.add(m-1);//�����������
		int PTC[]=get_PTC(Un,Ux,P);//����DS����������Ux�����Լ�P�µ����ݾ���
		int PTD[]=get_PTC(Un,Ux,D);//����DS����������Ux�ھ�������D�µ����ݾ���
		int PTCUD[]=new int[n];//����DS����������Ux�����Լ� CUD �µ����ݾ���

		for(int j=0;j<n;j++)
			PTCUD[j]=PTC[j]&PTD[j];		

		double get_GPRelativeUnP ;
		if(flag == 1){		//�����ȫ����
			if(!checkPandP(P)){
				get_UUUxRelativeP = new ArrayList<>();
				get_UUUxRelativeP.addAll(P);
				get_GPRelativeUnPResult = get_GPRelative(Un,P);
				get_GPRelativeUnP = get_GPRelativeUnPResult;
			}else{
				get_GPRelativeUnP = get_GPRelativeUnPResult;
			}
			return (Math.pow(n,2)*get_GPRelativeUnP+2*(sum(PTC)-sum(PTCUD)));
		}else if(flag==3){
			get_UUUxRelativeP = new ArrayList<>();
			get_UUUxRelativeP.addAll(P);
			get_GPRelativeUnPResult = get_GPRelative(Un,P);
			get_GPRelativeUnP = get_GPRelativeUnPResult;
			return  (Math.pow(n,2)*get_GPRelativeUnP+2*(sum(PTC)-sum(PTCUD)));
		}else{//�����ճ�
			return  (Math.pow(n,2)*get_GPRelative(Un,P)+2*(sum(PTC)-sum(PTCUD)));
		}
//		return  (Math.pow(n,2)*get_GPRelative(Un,P)+2*(sum(PTC)-sum(PTCUD)));
	}
	
	static double sum(int a[]){
		double sum1=0;
		for(int i=0;i<a.length;i++)
				sum1+=a[i];		
		return sum1;
	}
	
	//�������ϵͳDS����������Ux������a�����Լ�P���ⲿ��Ҫ��Sig_UUUx_Outer(a,P,D)=GP(D|P)-GP(D|PU{a})
	public static double get_UUUxSigOuter(int Un[][],int Ux[],int a,List<Integer> P){
		int n=Un.length, m=Un[0].length;  
		List<Integer> D=new ArrayList<Integer>();
		D.add(m-1);//�����������
		
		int PTC[]=get_PTC(Un,Ux,P);//����DS����������Ux�����Լ�P�µ����ݾ���
		int PTD[]=get_PTC(Un,Ux,D);//����DS����������Ux�ھ�������D�µ����ݾ���
		int PTCUD[]=new int[n];//����DS����������Ux�����Լ�CUD�µ����ݾ���
		for(int j=0;j<n;j++)
			PTCUD[j]=PTC[j]&PTD[j];

		List<Integer> PUa = new ArrayList<Integer>(P);
		PUa.add(a);
		
		int PTPUa[]=get_PTC(Un,Ux,PUa);//����DS����������Ux�����Լ�PUa�µ����ݾ���
		int PTPUaUD[]=new int[n];//����DS����������Ux�����Լ�PUaUD�µ����ݾ���
		for(int j=0;j<n;j++)
			PTPUaUD[j]=PTPUa[j]&PTD[j];

		double UUUxSigOuter=(Math.pow(n,2)*get_SigOuter(Un,a,P)+2*(sum(PTC)-sum(PTCUD))-2*(sum(PTPUa)-sum(PTPUaUD)));///((n+1)^2);
		return UUUxSigOuter;
	}
	
	 public static List<Integer> C =null;
	
	//����ʽ������Լ��KGIRA
	public List<Integer> KGIRA(int Un[][],List<Integer> SReduct,int Ux[]){
//		int[][] t1 = {{-2},{-2}};
//		HashMap<List<Integer>,int[][]> t2 = new HashMap<>();
//		List<Integer> t3 = new ArrayList<>();
//		t3.add(-2);
//		t2.put(t3, t1);
//		toleranceMatrixs.put(t1, t2);
		List<Integer> B = new ArrayList<>(SReduct);
		if(C==null){
		int m=Un[0].length;
		C= new ArrayList<>();
		for(int i=0;i<m-1;i++)
			C.add(i);
		}
		double new_GPRelativeC=get_UUUxRelative(Un,Ux,C,1);	//����GP_UUUx(D|C)
		double new_GPRelativeB=get_UUUxRelative(Un,Ux,B,2);	//����GP_UUUx(D|B)
		if(new_GPRelativeC==new_GPRelativeB){//�Ƚ�GP_UUUx(D|C)��GP_UUUx(D|B)
			return B;
		}
		ArrayList<Integer> temp;
		new_GPRelativeC = get_UUUxRelative(Un,Ux,C,3);
		while (new_GPRelativeC!=get_UUUxRelative(Un,Ux,B,2)){
			temp = setDiff(C, B);
			Iterator<Integer> v1 = temp.iterator();
			double preSigOuter=Double.NEGATIVE_INFINITY;
		    int prenum = -1;//���Ա�Ŵ�0��ʼ���������Ա�Ŷ������-1
			while (v1.hasNext()) {
				int num=v1.next();
				double gm=get_UUUxSigOuter(Un,Ux,num,B);//System.out.println(num+"HH"+prenum+"*"+gm+"///");
				if(preSigOuter < gm){
					prenum=num;
					preSigOuter=gm;
				}
			}
			B.add(prenum);//System.out.println(B);
		    temp.clear();
		}

		//Լ����С��
		List<Integer> temp2 = new ArrayList<Integer>(B);
		Iterator<Integer> v2 = temp2.iterator();
		List<Integer> temp3= new ArrayList<Integer>();
		while(v2.hasNext()){
			Object objnum=v2.next();
			temp3.addAll(B);
			temp3.remove(objnum);			
			if(get_UUUxRelative(Un,Ux,temp3,2)==new_GPRelativeC)
				B.remove(objnum);
			temp3.clear();	
		}
//		System.out.println("count2:" + count);
		return B;

	}

	private static ArrayList<Integer> setDiff(List<Integer> A, List<Integer> B){
		ArrayList<Integer> diff = new ArrayList<>();
		for (int a:A
			 ) {
			int num = 0;
			for (int b : B
					) {
				if (a != b)
					num++;
			}
			if (num == B.size())
				diff.add(a);
		}
		return diff;
	}
}