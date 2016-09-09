package org.upm.pregon.almonaciddelasierra;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.upm.pregon.almonaciddelasierra.db.ws.EventWSGetAsyncTask;
import org.upm.pregon.almonaciddelasierra.db.ws.dataobjects.EventDO;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service {
	
	private String CLASSNAME = this.getClass().getName();
	
	
	PregonApplication pa;

	
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Query the database and show alarm if it applies


		pa = (PregonApplication)getApplication();
		
    	boolean bResult = isUpdated("N35231");
    	
    	System.out.println("Starting service with value "+bResult);
    	Log.e(CLASSNAME, "Starting service with value "+bResult);
    	
        // I don't want this service to stay in memory, so I stop it
        // immediately after doing what I wanted it to do.
        stopSelf();

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        // I want to restart this service again in...
    	// one hour System.currentTimeMillis() + (1000 * 60 * 60)
    	// one minute System.currentTimeMillis() + (1000 * 60 )
        AlarmManager alarm = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarm.set(
            alarm.RTC_WAKEUP,
            System.currentTimeMillis() + (1000 * 60),
            PendingIntent.getService(this, 0, new Intent(this, MyService.class), 0)
        );
    }
    
    
    
    
	private boolean isUpdated(String sCoursId) {
	
		List<EventDO> lista;		
		EventWSGetAsyncTask wsat = new EventWSGetAsyncTask();
		
		try {
			
			String sURL = pa.getConfig().getProperty(Constants.CP_WS_GET_EVENTS_PATH);
			sURL+=sCoursId;			
			
			lista = wsat.execute(sURL).get();
			
			if (lista != null){
				
				Toast.makeText(getApplicationContext(),
						"Backend[ "+lista.size()+"] Local["+pa.getEvents().size()+"] Equals["+(lista.size()==pa.getEvents().size())+"]", Toast.LENGTH_LONG)
						.show();
				
				
			}else{
				System.out.println("Backend returned empty list of events.");
			}
			
			return (lista.size()==pa.getEvents().size());
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		return false;
				
	}
	
	
}