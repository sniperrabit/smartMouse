package mierzwa.rafal.smartmouse2;

public class MouseSensorThread implements Runnable {
	MainView mainView;
    public MouseSensorThread(MainView mainView) {

    	this.mainView=mainView;
	}
	@Override
    public void run() {
		int x=0,y=0;
		float axisX,axisY;
   	
   		double range=0.7;
	   	while(mainView.isSensorEnable){
	   		if(mainView.isSensorPause){
	   			
		   		axisX=mainView.axisX;
		   		axisY=mainView.axisY;
		   		
		   		if(axisX>range||axisX<-range||axisY>range||axisY<-range){
	
	//		   		x=mainView.x;
	//		   		y=mainView.y;
			   		x-=axisX;
			   		y-=axisY;
			
			   		mainView.sendMessage("Mouse" + x + " " + y + "\n");
	//		   		mainView.x=x;
	//		   		mainView.y=y;
			    	 try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		   		}
	   		}
	   	}
	}
}