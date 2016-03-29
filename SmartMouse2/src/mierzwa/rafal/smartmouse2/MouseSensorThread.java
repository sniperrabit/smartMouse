package mierzwa.rafal.smartmouse2;

public class MouseSensorThread implements Runnable {
	MainView mainView;
    public MouseSensorThread(MainView mainView) {

    	this.mainView=mainView;
	}
	@Override
    public void run() {
		int x=200,y=200;//not 0;0 in corner
		float axisX,axisY;
   	
   		double range=0.7;
	   	while(mainView.isSensorEnable){
	   		if(mainView.isSensorPause){
	   			
		   		axisX=mainView.axisX;
		   		axisY=mainView.axisY;
		   		
		   		if(axisX>range||axisX<-range||axisY>range||axisY<-range){
	
		   			if (mainView.isMultiscreen||(x-axisX>0 && y-axisY>0) ){
		   				x-=axisX;
				   		y-=axisY;
				   		System.out.println(x+" "+y);			   	 
				   		mainView.sendMessage("Mouse" + x + " " + y + "\n");
					}
			   				   			
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