package cesudu;

import ExcelTest2.standard.entity.commonValue;

public class timerThread implements Runnable{

	
	timeCounter timer;
	public timerThread(timeCounter timer){
		this.timer = timer;
	}
	
	@Override
	public void run() {
//		Thread.currentThread().setDaemon(true);	//����Ϊ��̨���� ��Ӱ��main������
		try {
			Thread.sleep(commonValue.maxRunTime);		//˯���ҹ涨�����ʱ��Сʱ�Ժ�Ѷ����ֵ��Ϊfalse����ʱ
			timer.setNowRun(false);						//	
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
}
