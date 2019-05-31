package DataSet_package;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Test;

import java.util.*;

public class IDS_PRIRA {
    static double[][] testArr = {{1.0, 1.0, 1.0, 0.0, 1.0}, {0.0, Double.NaN, 1.0, 0.0, 1.0}, {Double.NaN, Double.NaN, 0, 0, 0},
            {1.0, Double.NaN, 1, 1, 1}, {Double.NaN, Double.NaN, 1, 1, 2}, {0, 1, 1, Double.NaN, 1}};
    static RealMatrix DS = new Array2DRowRealMatrix(testArr);

    public static ArrayList<Integer> shuExperiment1(RealMatrix origDS , RealMatrix addDS){
        int numberAdd = addDS.getRowDimension();
        ArrayList<Integer> posReduct = getPosReduct(origDS);
        System.out.println("��ʼԼ��:" + posReduct);
        ArrayList<Integer> originalReduct = new ArrayList<>(posReduct);
        ArrayList<Integer> originalPos = getPos(origDS,posReduct);
        RealMatrix originalSys = origDS;
        long[] time = new long[8];
        int K = 0;
        int k1 = (origDS.getRowDimension()+numberAdd)/10;
//        k1 = k1==0?1:k1;
        int timeCount = 0;
        int counts = 0;
        ShuIarsParam shuIarsParam = null;
        long timeStart = System.currentTimeMillis();
        for(int i = 0; i< numberAdd; i++){
            RealMatrix addtionalObj = addDS.getRowMatrix(i);
            shuIarsParam = shuIARS(originalSys,originalReduct,originalPos,addtionalObj);
            K = K + shuIarsParam.NM;
            originalSys = addARow(originalSys,addtionalObj);
            originalReduct = shuIarsParam.increPosReduct;
            originalPos = shuIarsParam.newCPos;
            counts++;
            if(counts % k1 == 0){
                long timeTemp = System.currentTimeMillis();
                time[timeCount] = timeTemp - timeStart;
                timeCount++;
            }
        }
        for (long t:time)
            System.out.print((double) t/1000+"s ");
        return shuIarsParam.increPosReduct;
    }

    /**
     * ��������ʽ��������Լ��,�����Ķ���Ϊһ����
     * @param originalSys   ��ʼ����ϵͳ
     * @param originalReduct    ��ʼԼ��
     * @param originalPos   ��ʼ����
     * @param addtionalObj  ��ӵĶ���
     * @return һ��������õ�����Ӧ Լ���������ظ������������װ�Ķ���
     */
    public static ShuIarsParam shuIARS(RealMatrix originalSys, ArrayList<Integer> originalReduct, ArrayList<Integer> originalPos, RealMatrix addtionalObj){

        ArrayList<Integer> increPosReduct;
        ArrayList<Integer> newCPos;
        int NM;

        int n = originalSys.getRowDimension();
        int m = originalSys.getColumnDimension();
        ArrayList<Integer> c = new ArrayList<>();
        for(int i = 0; i < m - 1; i++){
            c.add(i);
        }
        ArrayList<Integer> p = new ArrayList<>(originalReduct);

        RealMatrix newSys = addARow(originalSys,addtionalObj);

        ArrayList cToleranceClass = IRISSingleTolerance(newSys, c, n);//�������Ķ��� �����Լ�c�µ� �ݲ���
        ArrayList<Integer> pToleranceClass = IRISSingleTolerance(newSys, p, n);//���������� ��ԭʼԼ���µ� �ݲ���

        int LC = cToleranceClass.size();
        int LP = pToleranceClass.size();
        ArrayList<Integer> genaralDecisionP = new ArrayList<>();
        for(int i = 0; i < LP; i++){
            genaralDecisionP.add((int)newSys.getEntry(pToleranceClass.get(i),m -1));
        }
        int lengthDecision = getUnique(genaralDecisionP).size();

        //��һ�����
        if(LC == 1 && lengthDecision == 1){
            increPosReduct = p;
            NM = 0;
        }

        //�ڶ������
        else {
            NM = 1;
            ArrayList<Integer> newPPos = getNewPos(originalSys, p, originalPos, addtionalObj);
            newCPos = getNewPos(originalSys, c, originalPos, addtionalObj);
            int numberPPos = newPPos.size();
            int numberCPos = newCPos.size();
            //***********************************************************************************************
            while (numberPPos != numberCPos){
                ArrayList<Integer> temp = setDiff(c, p);
                int lengthT = temp.size();
                int maxSig = 0;
                int inAttribute = -1;
                for(int i = 0; i < lengthT; i++){
                    ArrayList<Integer> currAtt = new ArrayList<>(p);
                    currAtt.add(temp.get(i));
                    ArrayList<Integer> currPos = getPos(newSys, currAtt);
                    int insignificance = setDiff(currPos, newPPos).size();
                    if(insignificance > maxSig){
                        inAttribute = temp.get(i);
                        maxSig = insignificance;
                    }
                }
                if(inAttribute == -1){
                    inAttribute = temp.get(lengthT - 1);
                }
                p.add(inAttribute);

                newPPos = getPos(newSys, p);
                numberPPos = newPPos.size();
            }
            //*************************************************************************************************
            increPosReduct = p;
            //ȥ��reduct�ж��������
            int v = 0;
            while (v < increPosReduct.size()){
                ArrayList<Integer> t = new ArrayList<>(increPosReduct);
                t.remove(v);
                ArrayList<Integer> posT = getPos(newSys, t);
                int outSignificance = setDiff(newCPos, posT).size();
                if(outSignificance == 0){
                    increPosReduct = t;
                    v = 0;
                }else {
                    v++;
                }
            }
//            Collections.sort(increPosReduct);
        }
        newCPos = getPos(newSys, increPosReduct);
        return new ShuIarsParam(increPosReduct, newCPos, NM);
    }

    /**
     * ʹ��������Ҫ�ȼ����������Լ��Ĵ�ͳ�㷨
     * @param DS ����ϵͳDS
     * @return ����ϵͳ������Լ��POS_Reduct
     */
    public static ArrayList<Integer> getPosReduct(RealMatrix DS){
        int m = DS.getColumnDimension();
        //get core of C
        ArrayList<Integer> c = new ArrayList<>();
        for(int i = 0; i < m - 1; i++){
            c.add(i);
        }
        ArrayList<Integer> posC = getPos(DS, c);
        int lengthC = posC.size();
        ArrayList<Integer> coreSet = new ArrayList<>();
        for(int i = 0; i < m - 1; i++){
            ArrayList<Integer> t = new ArrayList<>(c);
            t.remove(i);
            ArrayList<Integer> posQ = getPos(DS, t);
            ArrayList diffCQ = setDiff(posC, posQ);
            int outSignificance = diffCQ.size();
            if(outSignificance != 0){
                coreSet.add(i);
            }
        }
        //�Ӻ˳�������������һ�����������Լ�
        ArrayList<Integer> posReduct = new ArrayList<>(coreSet);
        ArrayList<Integer> posR = getPos(DS, posReduct);
        int lengthR = posR.size();
//        System.out.println(lengthC+"=="+lengthR);
        //***********************************************************************************************
        while(lengthR != lengthC){
            ArrayList<Integer> t = setDiff(c, posReduct);
            int lengthT = t.size();
            int maxSignificance = 0;
            int inAttribute = -1;
            for(int j = 0; j < lengthT; j++){
                ArrayList<Integer> temp = new ArrayList<>(posReduct);
                temp.add(t.get(j));
                ArrayList<Integer> posT = getPos(DS, temp);
                int inSignificance = setDiff(posT, posR).size();
                if(inSignificance > maxSignificance){
                    maxSignificance = inSignificance;
                    inAttribute = t.get(j);
                }
            }
            if(inAttribute == -1)
                inAttribute = t.get(lengthT - 1);
            posReduct.add(inAttribute);
            posR = getPos(DS, posReduct);
            lengthR = posR.size();
        }
        //***********************************************************************************************
        //�������Լ����Ƿ��ж��������
        int v = 0;
        while (v < posReduct.size()){
            ArrayList<Integer> temp = new ArrayList<>(posReduct);
            temp.remove(v);
            ArrayList<Integer> posT = getPos(DS, temp);
            int outSignificance  = setDiff(posR, posT).size();
            if(outSignificance == 0){
                posReduct = temp;
                v = 0;
            }else {
                v++;
            }
        }
//        Collections.sort(posReduct);
        return posReduct;
    }

    /**
     * �������ϵͳ DS �����Լ�P �µ�����
     * @param DS ����ϵͳDS
     * @param p ���Լ�P
     * @return ����ϵͳ�����Լ�P�µ�����POS
     */
    public static ArrayList<Integer> getPos(RealMatrix DS , ArrayList<Integer> p){
        ArrayList<Integer> posList = new ArrayList<>();
        if(p.size() == 0)
            return posList;
        else {
            ArrayList<ArrayList<Integer>> decisionClass = findDecisionClass(DS);
            ArrayList<ArrayList<Integer>> toleranceClass = IRISSingleTolerance(DS, p);
            int n = DS.getRowDimension();
            for (int i = 0; i < n; i++) {
                int sum = 0;
                for (int obj:
                     toleranceClass.get(i)) {
                    if(!decisionClass.get(i).contains(obj))//��� decisionClass û�и�Ԫ�ؾ� +1 �� ���� toleranceClass �������sum���� t��Ԫ����d�в����ڸ��������sumΪ0,���������
                        sum++;
                }
                if (sum == 0){
                    posList.add(i);
                }
            }
        }
        return posList;
    }

    @Test
    public void testGetPos(){
        RealMatrix testDs = DS;
        ArrayList<Integer> p = new ArrayList<>();
//        for(int i = 0; i < 4; i++){
//            p.add(i);
//        }
//        p.add(2);
        p.add(3);
        ArrayList<Integer> pos = getPos(testDs, p);
        System.out.println(pos);
    }

    /**
     * ����������obj���Լ�P�µ��ݲ���
     * @param DS ����ϵͳ
     * @param p ���Լ�
     * @param obj ָ���Ķ���
     * @return �ö����ڸ����Լ��µ��ݲ���
     */
    private static ArrayList<Integer> IRISSingleTolerance(RealMatrix DS, ArrayList<Integer> p, int obj){
        int n = DS.getRowDimension();
        int m = p.size();
        ArrayList<Integer> ToleranceClass = new ArrayList<Integer>();


        for (int j = 0; j < n; j++) {
            int k = 0;
            for (int v = 0; v < p.size(); v++) {
                if (DS.getEntry(j, p.get(v)) != DS.getEntry(obj, p.get(v)) && !Double.isNaN(DS.getEntry(j, p.get(v)))
                        && !Double.isNaN(DS.getEntry(obj, p.get(v))))
                    break;
                else
                    k = k + 1;

                //�����ȫһ�£���ô��һ���ݲ��ࡷ
                if (k == m) {
                    ToleranceClass.add(j);
                }
            }
        }
        return ToleranceClass;
    }

    /**
     * �������ϵͳ���Լ�P�µ��ݲ���
     * @param DS ����ϵͳ
     * @param p ���Լ�
     * @return ���ж����ڸ����Լ��µ��ݲ��������
     */
    private static ArrayList<ArrayList<Integer>> IRISSingleTolerance(RealMatrix DS, ArrayList<Integer> p){
        int n = DS.getRowDimension();
        int m = p.size();
        ArrayList<ArrayList<Integer>> ToleranceClass = new ArrayList<>();
        for(int i = 0; i < n; i++){
            ArrayList<Integer> temp = new ArrayList<>();
            temp.add(i);
            ToleranceClass.add(i,temp);
        }
        for(int i = 0 ; i < n ; i++) {
            for (int j = i + 1; j < n; j++) {
                int k = 0;
                for (int v = 0; v < m; v++) {
                    if (DS.getEntry(j, p.get(v)) != DS.getEntry(i, p.get(v)) && !Double.isNaN(DS.getEntry(j, p.get(v)))
                            && !Double.isNaN(DS.getEntry(i, p.get(v))))
                        break;
                    else
                        k = k + 1;

                    //�����ȫһ�£���ô��һ���ݲ��ࡷ
                    if (k == m) {
                        ToleranceClass.get(i).add(j);
                        ToleranceClass.get(j).add(i);
                    }
                }
            }
        }
        return ToleranceClass;
    }

    @Test
    public void tesGetToleranceClass(){
        ArrayList<Integer> p = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            p.add(i);
        }
        ArrayList<ArrayList<Integer>> test = IRISSingleTolerance(DS,p);
        for (ArrayList<Integer> a:test
             ) {
            System.out.println(a);
        }
    }


    /**
     * ���ĺ�������㷨�е�������·���
     * @param originalSys ԭʼ����ϵͳoriginal_sys
     * @param originalReduct ԭʼ����Լ��original_reduct
     * @param originalPos ԭʼ����original_POS
     * @param addtionalObj ��һ��������additional_obj
     * @return �¾���ϵͳ������ new_POS
     */
    public static ArrayList<Integer> getNewPos(RealMatrix originalSys, ArrayList<Integer> originalReduct, ArrayList<Integer> originalPos,
                                       RealMatrix addtionalObj){
        int n = originalSys.getRowDimension();
        int m = originalSys.getColumnDimension();
        ArrayList<Integer> p = new ArrayList<>(originalReduct);
        RealMatrix newSys = addARow(originalSys,addtionalObj);

        ArrayList<Integer> PToleranceClass = IRISSingleTolerance(newSys,p,n);//���� ����Ķ�����ݲ��� ��n�����Ǿ���newSys�ĵ�(n+1)�� Ҳ�����¼ӵĶ���
        int LP = PToleranceClass.size();


        //�� P �ݲ����¶� D �Ļ��� === ��̫��
        ArrayList<Integer> genaralDecisionP = new ArrayList<>(LP);
        for(int i = 0; i < LP; i++){
            genaralDecisionP.add(i, (int)newSys.getEntry(PToleranceClass.get(i),m - 1));
        }

        //�ж������Ӷ����Ƿ���������
        int lengthDecisionP = getUnique(genaralDecisionP).size();
        ArrayList<Integer> inPos = new ArrayList<>();
        if(lengthDecisionP == 1)
            inPos.add(n+1);


        //�ж���������� P-�ݲ��� ���Ƿ���� ԭ�������е� ���� ������
        ArrayList<Integer> outPos = new ArrayList<>();
        for(int j = 0; j < LP; j++){
            ArrayList<Integer> qToleranceClass = IRISSingleTolerance(newSys, p, PToleranceClass.get(j));
            int LQ = qToleranceClass.size();

            ArrayList<Integer> generalDecisionQ = new ArrayList<>(LQ);
            for(int i = 0; i < LQ; i++){
                generalDecisionQ.add(i,(int)newSys.getEntry(qToleranceClass.get(i),m - 1));
            }
            int lengthDecisionQ = getUnique(generalDecisionQ).size();
            if(lengthDecisionQ != 1)
                outPos.add(PToleranceClass.get(j));
        }
        ArrayList<Integer> newPos;
        originalPos.addAll(inPos);
        newPos = setDiff(originalPos, outPos);
        return newPos;
    }

    @Test
    public void testGetNewPos(){
        ArrayList<Integer> originalReduct = new ArrayList<>();
        originalReduct.add(2);
        originalReduct.add(3);

        ArrayList<Integer> originalPos = getPos(DS, originalReduct);
        double[][] addtionalArr = {{1 ,1 ,1 ,1 ,2}};
        RealMatrix addtionalObj = new Array2DRowRealMatrix(addtionalArr);

        ArrayList<Integer> newPos = getNewPos(DS, originalReduct, originalPos, addtionalObj);
        System.out.println(newPos);
    }

    /**
     * �������ϵͳds�ľ��ߵȼ��� decisionClassEachObj
     * @param DS ����ϵͳDS
     * @return ����ϵͳ�ľ��ߵȼ��� decisionClassEachObj
     */
    private static ArrayList<ArrayList<Integer>> findDecisionClass(RealMatrix DS){
        int n = DS.getRowDimension();
        int m = DS.getColumnDimension();
        RealMatrix decisionColumn = DS.getColumnMatrix(m-1).transpose();
        double[] decisionArr = decisionColumn.getRowMatrix(0).getData()[0];//ȡ���������� ת��Ϊ����

        LinkedHashSet<Double> decisionType = new LinkedHashSet<>();
        for (double d:
             decisionArr) {
            decisionType.add(d);
        }
        ArrayList<ArrayList<Integer>> decisionClass = new ArrayList<>();
        for (double d:
             decisionType) {
            ArrayList<Integer> tempDecisionSet = new ArrayList<>();
            decisionClass.add(tempDecisionSet);
        }
        for (int i = 0; i < decisionArr.length; i++){
            int k = 0;
            for (double d:
                 decisionType) {
                if(decisionArr[i]==d){
                    decisionClass.get(k).add(i);
                    break;
                }
                k++;
            }
        }
        ArrayList<ArrayList<Integer>> decisionClassEachObj = new ArrayList<>(n);
        for(int i = 0; i < n; i++){
            for (ArrayList<Integer> h:
                 decisionClass) {
                if(h.contains(i)){
                    decisionClassEachObj.add(h);
                    break;
                }
            }
        }
        return decisionClassEachObj;
    }

    /**
     * ÿ�����һ�� obj ���õ��� �����Լ�� ��װ��һ�����󷵻�
     */
    public static class ShuIarsParam{
        ArrayList<Integer> increPosReduct;
        ArrayList<Integer> newCPos;
        int NM;

        public ArrayList<Integer> getIncrePosReduct() {
            return increPosReduct;
        }
        public ArrayList<Integer> getNewCPos() {
            return newCPos;
        }
        public int getNM(){
            return NM;
        }
        ShuIarsParam(ArrayList<Integer> increPosReduct, ArrayList<Integer> newCPos, int NM){
            this.increPosReduct = increPosReduct;
            this.newCPos = newCPos;
            this.NM = NM;
        }
    }

    /**
     * ���� A B����������еĲ���� A���ж��� B��û�е�������У�
     * @param A �������A
     * @param B �������B
     * @return A - B
     */
    private static ArrayList<Integer> setDiff(ArrayList<Integer> A, ArrayList<Integer> B){
        ArrayList<Integer> diff = new ArrayList<>();
        for (int a:
                A) {
            int num = 0;
            for (int b:
                    B) {
                if(a != b)
                    num++;
            }
            if(num == B.size())
                diff.add(a);
        }
        diff = getUnique(diff);
//        Collections.sort(diff);
        return diff;
    }

    /**
     * ����ʼ�������һ�з���һ���µľ���
     * @param originMatrix ��ʼ����
     * @param addedRow ��ӵľ���
     * @return ��ʼ�������һ�����ɵ��µľ���
     */
    private static RealMatrix addARow(RealMatrix originMatrix, RealMatrix addedRow){
        RealMatrix newMatrix = new Array2DRowRealMatrix(originMatrix.getRowDimension()+addedRow.getRowDimension(),
                originMatrix.getColumnDimension());
        for(int i = 0; i < originMatrix.getRowDimension(); i++){
            newMatrix.setRow(i , originMatrix.getRow(i));
        }
        int j = 0;
        for(int i = originMatrix.getRowDimension(); i < newMatrix.getRowDimension(); i++){
            newMatrix.setRow(i , addedRow.getRow(j));
            j++;
        }
        return newMatrix;
    }

    /**
     * ���� �������ȥ�غ�� �������
     * @param arrayList ��ȥ�صĶ���
     * @return ȥ�غ�Ķ���
     */
    private static ArrayList<Integer> getUnique(ArrayList<Integer> arrayList){
        HashSet<Integer> temp = new HashSet<>(arrayList);
        return new ArrayList<>(temp);
    }

}
