package cesudu;

import java.util.Timer;
import java.util.TimerTask;

public class threadRun implements Runnable{

	private String fileName;
	
	public threadRun(String fileName){
		this.fileName = fileName;
	}
	
	@Override
	public void run() {
		
		String name = Thread.currentThread().getName();
		new Timer().schedule(new TimerTask() {
			
			@Override
			public void run() {
				System.out.println(name+"ʱ�䵽�� ֹͣ����!");
				Thread.currentThread().setDaemon(true);
			}
		}, 100);
//		test_KGIRA.doit(fileName);	
		System.out.println("---------------------------------------------------------------------------------");
		
		System.out.println(name+" is over!");
		
		//�����˵�ǰ�߳�������һ
//		IDSCompare.down();
	}

}
