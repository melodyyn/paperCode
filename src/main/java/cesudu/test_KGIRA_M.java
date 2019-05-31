package cesudu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataSet_package.*;

public class test_KGIRA_M {

    public static void doit(String fileName) {
        // TODO Auto-generated method stub
        System.out.println("**********" + fileName + "��KGIRA_M������ʱ��" + "***********");

        long time[] = new long[8];
        int t = 0;//time�±�

        List<Integer> preRED = null;
        List<Integer> nowRED = null;
        // ����DataPreprocess����data1
        DataPreprocess data1 = new DataPreprocess(fileName);
        int k = 0, m1 = data1.get_origDSNum() / 10, m = m1 > 0 ? m1 : 1;
        data1.dataRatioSelect(0.2);
        preRED = new ArrayList<Integer>();
        nowRED = new ArrayList<Integer>();
        ArrayList<ArrayList<Integer>> origData = data1.get_origDS();//�õ���ʼ����
        ArrayList<ArrayList<Integer>> addData = data1.get_addDS();//�õ���������
        Iterator<ArrayList<Integer>> value = addData.iterator();
        IDS_THA_M im1 = new IDS_THA_M(origData, addData);
        preRED = im1.THA();//System.out.println(preRED+"###");
        IDS_KGIRA_M im2 = new IDS_KGIRA_M();
        long startTime = System.currentTimeMillis();   //��ȡ��ʼʱ��
        while (value.hasNext()) {
            ArrayList<ArrayList<Integer>> temp = new ArrayList<ArrayList<Integer>>();//��ȡÿ�ε��������ݣ�������10%��
            for(int i = 0; i < m; i++) {
                if(value.hasNext())
                    temp.add(value.next());//
                else
                    break;
                k++;
            }
            im2.setUn(origData);
            im2.setUx(temp);
//            System.out.println("addData=" + temp);
            nowRED = im2.KGIRA(im2.getUn(), preRED, im2.getUx());//
            preRED.clear();
            preRED.addAll(nowRED);
            if (k % m == 0) {
                long endTime = System.currentTimeMillis(); //��ȡ����ʱ��
                time[t++] = endTime - startTime;//���ÿ���10%������Լ���ʱ��
//                System.out.println("����ӵĶ���" + temp.get(temp.size() - 1));
                System.out.println("�����е�Լ��" + nowRED);
            }
            origData.addAll(temp);
//            System.out.println("originData�ĳ��ȣ�" + origData.size());
//            value.remove();//temp=null;
        }
        for (int i = 0; i < 8; i++)
            System.out.print((double) time[i] / 1000 + " ");
        System.out.println("\n" + fileName + "��KGIRA��Լ��Ϊ��" + nowRED + "\n");
    }
}
