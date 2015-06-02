package org.ounl.noisenotifier;

import java.util.Calendar;
import java.util.Date;

import org.ounl.noisenotifier.fcube.Constants;
import org.ounl.noisenotifier.fcube.DatabaseHandler;
import org.ounl.noisenotifier.fcube.NoiseSample;
import org.ounl.noisenotifier.fcube.commands.FCColor;
import org.ounl.noisenotifier.fcube.commands.FCOff;
import org.ounl.noisenotifier.fcube.commands.FCOn;
import org.ounl.noisenotifier.fcube.config.FeedbackCubeColor;
import org.ounl.noisenotifier.fcube.config.FeedbackCubeConfig;
import org.ounl.noisenotifier.fcube.config.FeedbackCubeManager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class NoiseAlertActivity extends Activity {
	/* constants */

	/** running state **/
	private boolean mRunning = false;

	/** config state **/

	private PowerManager.WakeLock mWakeLock;

	private Handler mHandler = new Handler();

	private DatabaseHandler db;

	/* References to view elements */
	private TextView mStatusView, tv_noice;

	/* sound data source */
	private Detect_noise mSensor;
	ProgressBar bar;
	EditText etIP;
	EditText etTAG;
	EditText etThresMin;
	EditText etThresMax;
	LinearLayout llTecla;

	/****************** Define runnable thread again and again detect noise *********/

	private Runnable mSleepTask = new Runnable() {
		public void run() {
			// Log.i("Noise", "runnable mSleepTask");

			start();
		}
	};

	// Create runnable thread to Monitor Voice
	private Runnable mPollTask = new Runnable() {
		public void run() {

			try {
				// Read input data
				String sIp = etIP.getText().toString();
				FeedbackCubeConfig.getSingleInstance().setIp(sIp);

				String sTag = etTAG.getText().toString();

				// Current value returned by the sensor
				double amp = mSensor.getAmplitude();

				if (!Double.isInfinite(amp)) {

					// Average value for the last NUM_POLLS meaures
					double ampAVG = 0;
					// Log.i("Noise", "runnable mPollTask");
					

					Date dNow = new Date();
					int iLevel = Constants.NOISE_LEVEL_INIT;

					// Add noise item to buffer and return position where it was
					// inserted
					int iNum = FeedbackCubeConfig.getSingleInstance()
							.addNoiseItem(amp);

					// Return color for average values in buffeer
					// Commented for calibration
					readThreshold();
					FeedbackCubeColor color = FeedbackCubeConfig.getSingleInstance().getBufferColor();

					// Launch color in the cube
					FCColor fcc = new FCColor(FeedbackCubeConfig
							.getSingleInstance().getIp(), "" + color.getR(), ""
							+ color.getG(), "" + color.getB());
					new FeedbackCubeManager().execute(fcc);

					// Prepare log
					ampAVG = FeedbackCubeConfig.getSingleInstance().getAverageNoise();
					
					// Show average color in mobile display
					updateCurrentNoiseAndProgressbar("Monitoring on..." + sIp, amp);
					updateAvgTextAndBackground(ampAVG, amp, color);

					// Insert noise item into database
					db.addNoiseSample(new NoiseSample(amp, ampAVG, dNow.getTime(), sTag));					

					// Added for callibration
					// FeedbackCubeColor color = new FeedbackCubeColor(0,0,0);
					// callForHelp(ampAVG, amp, color);

					if (iNum == FeedbackCubeConfig.NUM_POLLS) {

						FeedbackCubeConfig.getSingleInstance().resetPollIndex();
					}

				}

				// Runnable(mPollTask) will again execute after POLL_INTERVAL
				mHandler.postDelayed(mPollTask,
						FeedbackCubeConfig.POLL_INTERVAL);

			} catch (Exception e) {
				updateCurrentNoiseAndProgressbar("" + e.getMessage(), 0.0);
				e.printStackTrace();
			}
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Defined SoundLevelView in main.xml file
		setContentView(R.layout.main);

		mStatusView = (TextView) findViewById(R.id.status);
		tv_noice = (TextView) findViewById(R.id.tv_noice);
		bar = (ProgressBar) findViewById(R.id.progressBar1);

		etIP = (EditText) findViewById(R.id.editTextIP);
		etTAG = (EditText) findViewById(R.id.editTextTAG);
		etTAG.setText(Calendar.getInstance().get(Calendar.YEAR) + "_"
				+ (Calendar.getInstance().get(Calendar.MONTH) + 1) + "_"
				+ Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "_"
				+ Calendar.getInstance().get(Calendar.HOUR_OF_DAY));

		llTecla = (LinearLayout) findViewById(R.id.llTeclas);
		etThresMin = (EditText) findViewById(R.id.etMimThreshold);
		etThresMax = (EditText) findViewById(R.id.etMaxThreshold);
		readThreshold();

		// Used to record voice
		mSensor = new Detect_noise();
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK,
				"NoiseAlertActivity");

		db = new DatabaseHandler(getApplicationContext());


	}

	private void readThreshold() {
		// Configure thresholds
		// etThresMin = (EditText) findViewById(R.id.etMimThreshold);
		try {
			FeedbackCubeConfig.getSingleInstance().setmThresholdMin(
					new Double(etThresMin.getText().toString()));

			// etThresMax = (EditText) findViewById(R.id.etMaxThreshold);
			FeedbackCubeConfig.getSingleInstance().setmThresholdMax(
					new Double(etThresMax.getText().toString()));
		} catch (Exception e) {
			updateCurrentNoiseAndProgressbar("Threshold have invalid values", 0.0);
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		// Log.i("Noise", "==== onResume ===");

// AAAAA esto lo has comentado para que no inicie atizándoles		
//		if (!mRunning) {
//			mRunning = true;
//			start();
//		}
	}

	@Override
	public void onStop() {
		super.onStop();
		// Log.i("Noise", "==== onStop ===");
		// Stop noise monitoring
		stop();
	}

	private void start() {
		try {

			// Log.i("Noise", "==== start ===");
			mSensor.start();
			if (!mWakeLock.isHeld()) {
				mWakeLock.acquire();
			}
			// Noise monitoring start
			// Runnable(mPollTask) will execute after POLL_INTERVAL
			mHandler.postDelayed(mPollTask, FeedbackCubeConfig.POLL_INTERVAL);
		} catch (Exception e) {
			updateCurrentNoiseAndProgressbar("Error starting activity", 0.0);
			e.printStackTrace();
		}
	}

	private void stop() {
		try {
			// Switch cube off
			Log.i("FC", "... closing feedback cube.");
			FCOff f = new FCOff(FeedbackCubeConfig.getSingleInstance().getIp());
			new FeedbackCubeManager().execute(f);

			Log.i("Noise", "==== Stop Noise Monitoring===");
			if (mWakeLock.isHeld()) {
				mWakeLock.release();
			}
			mHandler.removeCallbacks(mSleepTask);
			mHandler.removeCallbacks(mPollTask);
			mSensor.stop();
			bar.setProgress(0);
			updateCurrentNoiseAndProgressbar("stopped...", 0.0);
			mRunning = false;
		} catch (Exception e) {
			updateCurrentNoiseAndProgressbar("Error stopping activity", 0.0);
			e.printStackTrace();
		}

	}

	private void updateCurrentNoiseAndProgressbar(String status, double signalEMA) {
		mStatusView.setText(status);
		bar.setProgress((int) signalEMA);
		// Log.d("SONUND", String.valueOf(signalEMA));
		// tv_noice.setText(signalEMA + "dB");
	}

	private void updateAvgTextAndBackground(double signalAVG, double signal,
			FeedbackCubeColor co) {

		// stop();

		// Show alert when noise thersold crossed
		// Toast.makeText(getApplicationContext(),
		// "Noise Thersold Crossed!!!!!", Toast.LENGTH_LONG).show();
		Log.d("PULSE",
				"Color [" + co.getR() + ", " + co.getG() + ", " + co.getB()
						+ "] dB:[" + String.valueOf(signal) + "] dB_AVG:["
						+ String.valueOf(signalAVG) + "]");
		tv_noice.setText("[" + co.getR() + ", " + co.getG() + ", " + co.getB()
				+ "] RGB \n[" + String.valueOf(signal) + "] dB \n["
				+ String.valueOf(signalAVG) + "] dbAVG");

		llTecla.setBackgroundColor(Color.rgb(co.getR(), co.getG(), co.getB()));
	}

	
	/**
	 * Clicked Power button
	 * 
	 * @param v
	 */
	public void onPower(View v) {
		// Boot cube on
		
		
		String sIp = etIP.getText().toString();
		FeedbackCubeConfig.getSingleInstance().setIp(sIp);
		Log.i("FC", "Starting feedback cube ..."+sIp);
		
		FCOn f = new FCOn(FeedbackCubeConfig.getSingleInstance().getIp());
		new FeedbackCubeManager().execute(f);

	}
	
	/**
	 * Clicked ON button
	 * 
	 * @param v
	 */
	public void onOn(View v) {
		start();
	}

	/**
	 * Clicked OFF button
	 * 
	 * @param v
	 */
	public void onOff(View v) {
		stop();
	}

};
