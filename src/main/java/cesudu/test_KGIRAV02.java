//�þ�̬��ά����ģ���������ʽ��
package cesudu;

import DataSet_package.DataPreprocess;
import DataSet_package.IDS_KGIRAV01;
import DataSet_package.IDS_KGIRAV02;
import DataSet_package.IDS_THAV02;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class test_KGIRAV02 {

    public static void doit(String fileName) {
        // TODO Auto-generated method stub
        System.out.println("****������ʽ-֪ʶ���ȣ��ģ�******" + fileName + "��KGIRA������ʱ��" + "***********");

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

        data1 = null;

        Iterator<ArrayList<Integer>> value = addData.iterator();

        IDS_THAV02 im1 = new IDS_THAV02(origData);
        preRED = im1.THA();

        IDS_KGIRAV02 im2 = new IDS_KGIRAV02();
        ArrayList<Integer> temp = new ArrayList<Integer>();
        long startTime = System.currentTimeMillis();   //��ȡ��ʼʱ��
        while (value.hasNext()) {

            temp.addAll(value.next());
            im2.setUn(origData);
            im2.setUx(temp);
            nowRED = im2.KGIRA(im2.getUn(), preRED, im2.getUx());

            preRED.clear();
            preRED.addAll(nowRED);
            k++;

            if (k % m == 0) {
                long endTime = System.currentTimeMillis(); //��ȡ����ʱ��
                time[t++] = endTime - startTime;//���ÿ���10%������Լ���ʱ��
//                System.out.println("����ӵĶ���" + temp);
                System.out.println("�����е�Լ��" + nowRED);
            }
            origData.add(temp);
            temp = new ArrayList<Integer>();//.clear();
        }

        for (int i = 0; i < 8; i++)
            System.out.print((double) time[i] / 1000 + " ");

        System.out.println("\n" + fileName + "��KGIRA��Լ��Ϊ��" + nowRED + "\n");
    }
}

