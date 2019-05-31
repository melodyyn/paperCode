//�þ�̬��ά����ģ���������ʽ��
package DataSet_package;

import java.util.*;

public class IDS_KGIRAV02 {
    int Un[][] = null;
    int Ux[] = null;
    int flag = 0;
    double tempUUUxRelative;

    public IDS_KGIRAV02(int a[][], int b[]) {
        Un = a;
        Ux = b;
    }

    public IDS_KGIRAV02() {

    }

    //���캯����������ʵ��������ֵ
    public IDS_KGIRAV02(ArrayList<ArrayList<Integer>> orgData, ArrayList<Integer> addData) {
        setUn(orgData);
        setUx(addData);
    }

    //����ʵ������Un
    public int[][] getUn() {
        return Un;
    }

    //����ʵ������Un
    public void setUn(ArrayList<ArrayList<Integer>> nData) {
        int row = nData.size(), col = nData.get(0).size();
        Un = new int[row][col];
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++)
                Un[i][j] = nData.get(i).get(j);
    }

    //����ʵ������Ux
    public int[] getUx() {
        return Ux;
    }

    //����ʵ������Ux
    public void setUx(ArrayList<Integer> xData) {
        int col = xData.size();
        Ux = new int[col];
        for (int i = 0; i < col; i++)
            Ux[i] = xData.get(i);
    }

    //�����Լ�P�µ����ݾ���
    public int[][] get_ToleranceMatrix(int[][] Un, List<Integer> P) {
        int k = 0, n = Un.length, m = P.size();
        int ToleranceMatrix[][] = new int[n][n];
        int attribute[] = new int[m];
        //����Iteratorʵ�ֱ���
        int s = 0;
        Iterator<Integer> value = P.iterator();
        while (value.hasNext()) {
            attribute[s] = value.next();
            //System.out.print(attribute[s]+" ");
            s++;
        }

        for (int i = 0; i < n; i++)
            ToleranceMatrix[i][i] = 1;

        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++) {
                k = 0;
                for (int v = 0; v < m; v++)
                    if ((Un[i][attribute[v]] != Un[j][attribute[v]]) && (Un[i][attribute[v]] != -1) && (Un[j][attribute[v]] != -1))
                        break;
                    else
                        k = k + 1;

                if (k == m) {
                    ToleranceMatrix[i][j] = 1;
                    ToleranceMatrix[j][i] = 1;
                }
            }

        return ToleranceMatrix;
    }

    //�����ϵͳDS�����Լ�P�µ�֪ʶ����GP
    public double get_GP(int[][] Un, List<Integer> P) {
        int n = Un.length;
        int ToleranceMatrix[][] = get_ToleranceMatrix(Un, P);
        long sum = 0;
        for (int i = 0; i < ToleranceMatrix.length; i++) {
            for (int j = 0; j < ToleranceMatrix[i].length; j++) {
                sum += ToleranceMatrix[i][j];
            }
        }
        double gp = sum;// Math.pow(n, 2);
        return gp;///Math.pow(n,2);
    }

    //�����ϵͳDS�����Լ�P�µ����֪ʶ����GP(D|P)
    public double get_GPRelative(int[][] Un, List<Integer> P) {
        int m = Un[0].length;//
        List<Integer> PUD = new ArrayList<Integer>();
        Iterator<Integer> value = P.iterator();
        while (value.hasNext()) {
            PUD.add(value.next());
        }
        PUD.add(m - 1);
        double GPRelative = Arith.sub(get_GP(Un, P), get_GP(Un, PUD));
        return GPRelative;//k1-k2;
    }

    //�������ϵͳDS����a�����Լ�P���ڲ���Ҫ��Sig_U_SigInner(a,P,D)=GP(D|P-{a})-GP(D|P)
    public double get_SigInner(int[][] Un, int a, List<Integer> P) {
        List<Integer> P_D = new ArrayList<Integer>();
        Iterator<Integer> value = P.iterator();
        while (value.hasNext()) {
            int num = value.next();
            if (num != a)
                P_D.add(num);
        }
        double k = get_GPRelative(Un, P_D);
        double s = get_GPRelative(Un, P);
        return (k - s);
    }

    //�������ϵͳDS����a�����Լ�P���ⲿ��Ҫ��Sig_U_Outer(a,P,D)=GP(D|P)-GP(D|PU{a})
    public double get_SigOuter(int[][] Un, int a, List<Integer> P) {
        double k = get_GPRelative(Un, P);

        List<Integer> PUa = new ArrayList<Integer>();
        PUa.addAll(P);
        PUa.add(a);

        double s = get_GPRelative(Un, PUa);
        return (k - s);
    }

    //�������ϵͳDS���Ӷ���Ux�������Լ�P�£���������Ux��DS��Ԫ�ص��ݲ����Ʃ��|Ux|=1,|DS|=n,�������ݲ����ά��Ϊ1xn,��һά������
    public int[] get_PTC(int Un[][], int Ux[], List<Integer> P) {
        int k = 0, n = Un.length, m = P.size();
        int val_Ux = 0, val_Un = 0;
        int[] ToleranceMatrix = new int[n];
        int[] attribute = new int[m];
        //����Iteratorʵ�ֱ���
        int w = 0;
        Iterator<Integer> value = P.iterator();
        while (value.hasNext()) {
            attribute[w] = value.next();
            w++;
        }
        for (int j = 0; j < n; j++) {
            k = 0;
            for (int v = 0; v < m; v++) {
                val_Ux = Ux[attribute[v]];
                val_Un = Un[j][attribute[v]];
                if ((val_Ux != val_Un) && (val_Ux != -1) && (val_Un != -1))
                    break;
                else
                    k = k + 1;
            }
            if (k == m) {
                ToleranceMatrix[j] = 1;
            }
        }
        return ToleranceMatrix;
    }

    //�������ϵͳDS����������Ux���Լ�C����ھ�������D��֪ʶ����GP_UUUx(D|C)
    public double get_UUUxRelative(int[][] Un, int[] Ux, List<Integer> P) {
        int n = Un.length, m = Un[0].length;
        List<Integer> D = new ArrayList<Integer>();
        D.add(m - 1);//�����������
        int[] PTC = get_PTC(Un, Ux, P);//����DS����������Ux�����Լ�P�µ����ݾ���
        int[] PTD = get_PTC(Un, Ux, D);//����DS����������Ux�ھ�������D�µ����ݾ���
        int[] PTCUD = new int[n];//����DS����������Ux�����Լ�CUD�µ����ݾ���
        for (int j = 0; j < n; j++)
            PTCUD[j] = PTC[j] & PTD[j];
        List<Integer> PUD = new ArrayList<Integer>();
        PUD.addAll(P);
        PUD.add(m - 1);
        double UUUxRelative = (get_GP(Un, P) - get_GP(Un, PUD)) + 2 * (sum(PTC) - sum(PTCUD));///Math.pow(n+1,2);
//        double UUUxRelative = (Math.pow(n, 2) * (get_GP(Un, P) - get_GP(Un, PUD)) / Math.pow(n + 1, 2) + 2 * (sum(PTC) - sum(PTCUD)) / Math.pow(n + 1, 2));///Math.pow(n+1,2);
        return UUUxRelative;
    }

    public double get_UUUxRelativeC(int[][] Un, int[] Ux, List<Integer> P) {
        int n = Un.length, m = Un[0].length;
        List<Integer> D = new ArrayList<Integer>();
        D.add(m - 1);//�����������
        int[] PTC = get_PTC(Un, Ux, P);//����DS����������Ux�����Լ�P�µ����ݾ���
        int[] PTD = get_PTC(Un, Ux, D);//����DS����������Ux�ھ�������D�µ����ݾ���
        int[] PTCUD = new int[n];//����DS����������Ux�����Լ�CUD�µ����ݾ���
        for (int j = 0; j < n; j++)
            PTCUD[j] = PTC[j] & PTD[j];
        List<Integer> PUD = new ArrayList<Integer>();
        PUD.addAll(P);
        PUD.add(m - 1);
        if (flag == 0) {
            double UUUxRelative = (get_GP(Un, P) - get_GP(Un, PUD)) + 2 * (sum(PTC) - sum(PTCUD));///Math.pow(n+1,2);
//            double UUUxRelative = (Math.pow(n, 2) * (get_GP(Un, P) - get_GP(Un, PUD)) / Math.pow(n + 1, 2) + 2 * (sum(PTC) - sum(PTCUD)) / Math.pow(n + 1, 2));///Math.pow(n+1,2);
            tempUUUxRelative = UUUxRelative;
            flag++;
            return tempUUUxRelative;
        }
        double UUUxRelative = tempUUUxRelative + 2 * (sum(PTC) - sum(PTCUD));///Math.pow(n+1,2);
        tempUUUxRelative = UUUxRelative;
        return tempUUUxRelative;
    }

    List<Integer> tempB = new ArrayList<>();
    int flagB = 0;
    double tempUUUxRelativeB = 0;
    public double get_UUUxRelativeB(int[][] Un, int[] Ux, List<Integer> P) {
//        System.out.println("B:" + P);
        int n = Un.length, m = Un[0].length;
        List<Integer> D = new ArrayList<Integer>();
        D.add(m - 1);//�����������
        int[] PTC = get_PTC(Un, Ux, P);//����DS����������Ux�����Լ�P�µ����ݾ���
        int[] PTD = get_PTC(Un, Ux, D);//����DS����������Ux�ھ�������D�µ����ݾ���
        int[] PTCUD = new int[n];//����DS����������Ux�����Լ�CUD�µ����ݾ���
        for (int j = 0; j < n; j++)
            PTCUD[j] = PTC[j] & PTD[j];
        List<Integer> PUD = new ArrayList<Integer>();
        PUD.addAll(P);
        PUD.add(m - 1);
        double UUUxRelative = 0;
        if(flag == 0 ||!P.equals(tempB)){//��� P �� B ���
            UUUxRelative = (get_GP(Un, P) - get_GP(Un, PUD)) + 2 * (sum(PTC) - sum(PTCUD));
            flag++;
            tempB = P;
            tempUUUxRelativeB = UUUxRelative;
            return tempUUUxRelativeB;
        }else if (P.equals(tempB)){
            UUUxRelative = tempUUUxRelativeB + 2 * (sum(PTC) - sum(PTCUD));
            tempUUUxRelativeB = UUUxRelative;
            return tempUUUxRelativeB;
        }
//        UUUxRelative = (get_GP(Un, P) - get_GP(Un, PUD)) + 2 * (sum(PTC) - sum(PTCUD));///Math.pow(n+1,2);
//        double UUUxRelative = (Math.pow(n, 2) * (get_GP(Un, P) - get_GP(Un, PUD)) / Math.pow(n + 1, 2) + 2 * (sum(PTC) - sum(PTCUD)) / Math.pow(n + 1, 2));///Math.pow(n+1,2);
        return UUUxRelative;
    }

    double sum(int a[]) {
        double sum1 = 0;
        for (int i = 0; i < a.length; i++)
            sum1 += a[i];
        return sum1;
    }

//    ArrayList<Double> tempUUUxSigOuters = new ArrayList<>();
    HashMap<Integer, Double> tempUUUxSigOuters = new HashMap<>();
    int count = 0;
    //�������ϵͳDS����������Ux������a�����Լ�P���ⲿ��Ҫ��Sig_UUUx_Outer(a,P,D)=GP(D|P)-GP(D|PU{a})
    public double get_UUUxSigOuter(int Un[][], int Ux[], int a, List<Integer> P) {
        int n = Un.length, m = Un[0].length;
        List<Integer> D = new ArrayList<Integer>();
        D.add(m - 1);//�����������

        int PTC[] = get_PTC(Un, Ux, P);//����DS����������Ux�����Լ�P�µ����ݾ���
        int PTD[] = get_PTC(Un, Ux, D);//����DS����������Ux�ھ�������D�µ����ݾ���
        int PTCUD[] = new int[n];//����DS����������Ux�����Լ�CUD�µ����ݾ���
        for (int j = 0; j < n; j++)
            PTCUD[j] = PTC[j] & PTD[j];

        List<Integer> PUa = new ArrayList<Integer>();
        PUa.addAll(P);
        PUa.add(a);

        int PTPUa[] = get_PTC(Un, Ux, PUa);//����DS����������Ux�����Լ�PUa�µ����ݾ���
        int PTPUaUD[] = new int[n];//����DS����������Ux�����Լ�PUaUD�µ����ݾ���
        for (int j = 0; j < n; j++)
            PTPUaUD[j] = PTPUa[j] & PTD[j];

        double UUUxSigOuter;

//        if(!tempUUUxSigOuters.containsKey(a)){
        UUUxSigOuter = (Math.pow(n, 2) * get_SigOuter(Un, a, P) + 2 * (sum(PTC) - sum(PTCUD)) - 2 * (sum(PTPUa) - sum(PTPUaUD)));
//            tempUUUxSigOuters.put(a,UUUxSigOuter);
//        }else {
//            UUUxSigOuter = (Math.pow(n, 2) * tempUUUxSigOuters.get(a) + 2 * (sum(PTC) - sum(PTCUD)) - 2 * (sum(PTPUa) - sum(PTPUaUD)));// Math.pow(n + 1, 2);
//            tempUUUxSigOuters.put(a,UUUxSigOuter);
//        }
        return UUUxSigOuter;
    }

    //����ʽ������Լ��KGIRA
    public List<Integer> KGIRA(int Un[][], List<Integer> SReduct, int Ux[]) {
        List<Integer> B = new ArrayList<Integer>();
        B.addAll(SReduct);
        int m = Un[0].length;

        List<Integer> P = new ArrayList<Integer>();
        List<Integer> C = new ArrayList<Integer>();
        for (int i = 0; i < m - 1; i++)
            P.add(i);

        C.addAll(P);
        double new_GPRelativeC = get_UUUxRelativeC(Un, Ux, C);//����GP_UUUx(D|C)  ʹ����һ�ε�֪ʶ���ȼ���������֪ʶ���ȣ����̼���ʱ��
        double new_GPRelativeB = get_UUUxRelativeB(Un, Ux, B);//����GP_UUUx(D|B)   ���� B�������������һ�εļ��������̼���ʱ��
        if (new_GPRelativeC == new_GPRelativeB) {//�Ƚ�GP_UUUx(D|C)��GP_UUUx(D|B)
            return B;
        }


//        while (new_GPRelativeC - get_UUUxRelative(Un, Ux, B) > 0.0000000000001 || get_UUUxRelative(Un, Ux, B)  - new_GPRelativeC > 0.0000000000001) {
        while (new_GPRelativeC != get_UUUxRelative(Un, Ux, B)) {
            List<Integer> temp = new ArrayList<Integer>();
            temp.addAll(P);
            temp.removeAll(B);// temp = P - B; ����B����ļ��ϣ��ҳ��ⲿ����������
            Iterator<Integer> v1 = temp.iterator();
            double preSigOuter = Double.NEGATIVE_INFINITY;
            int prenum = -1;//���Ա�Ŵ�0��ʼ���������Ա�Ŷ������-1
            while (v1.hasNext()) {
                int num = v1.next();
                double gm = get_UUUxSigOuter(Un, Ux, num, B);//System.out.println(num+"HH"+prenum+"*"+gm+"///");
                if (preSigOuter < gm) {
                    prenum = num;
                    preSigOuter = gm;
                }
            }
            B.add(prenum);//System.out.println(B);
        }
//        System.out.println("�˴�����������Լ��" + B);
        //Լ����С��
        List<Integer> temp2 = new ArrayList<Integer>();
        temp2.addAll(B);
        Iterator<Integer> v2 = temp2.iterator();
        List<Integer> temp3 = new ArrayList<Integer>();
        while (v2.hasNext()) {
            Object objnum = v2.next();
            temp3.addAll(B);
            temp3.remove(objnum);
            if (get_UUUxRelative(Un, Ux, temp3) == new_GPRelativeC) {
                System.out.println("�Ƴ����ԣ�" + objnum);
                B.remove(objnum);
            }
            temp3.clear();
            //System.out.println(B);
        }
        return B;
    }
}