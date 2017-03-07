package com.prettymobileapp.liu.bombtimer;

import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

	private TextView tv_time,tv_points;
	private Button bt_defuse,bt_restart;
	private EditText et_max_time;
	private final String TAG = "checkError";
	private boolean boolean_game_on = false;
	private int max_time,random_time,current_time,total_points,counter;
	private MediaPlayer mp_tick;
	Handler timerHandler_tick = new Handler();
	Handler timerHandler_blowup = new Handler();
	Runnable timerRunnable_tick = new Runnable() {
		@Override
		public void run() {
			timerHandler_tick.postDelayed(this, 1000);
			Log.v(TAG, "start ticking");
			mp_tick.start();
			current_time++;
			update_UI();
		}};

	Runnable timerRunnable_blowup = new Runnable() {
		@Override
		public void run() {
			timerHandler_blowup.postDelayed(this, random_time*1000);
			counter++;
			Log.v(TAG, "blow up...times " + counter);
			if(counter>1) { blowup();}
		}};

	protected void onPause(){
		super.onPause();
		timerHandler_tick.removeCallbacks(timerRunnable_tick);
		timerHandler_blowup.removeCallbacks(timerRunnable_blowup);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tv_time = (TextView) findViewById(R.id.textView);
		tv_points = (TextView) findViewById(R.id.textView2);
		bt_defuse = (Button) findViewById(R.id.button_defuse);
		bt_restart = (Button) findViewById(R.id.button_restart);
		et_max_time = (EditText) findViewById(R.id.editText);
		mp_tick = MediaPlayer.create(getApplicationContext(), R.raw.tick);
		total_points = 0;
		update_UI();

		bt_restart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				max_time = Integer.parseInt( et_max_time.getText().toString() );
				Random randomGenerator = new Random();
				random_time = randomGenerator.nextInt(max_time-5)+5;
				Log.v(TAG, "random time is " + random_time);
				current_time = 0;
				counter = 0;

				timerHandler_tick.postDelayed(timerRunnable_tick, 0);
				timerHandler_blowup.postDelayed(timerRunnable_blowup, 0);

				boolean_game_on = !boolean_game_on;
				update_UI();
			}
		});

		bt_defuse.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				boolean_game_on = !boolean_game_on;
				timerHandler_tick.removeCallbacks(timerRunnable_tick);
				timerHandler_blowup.removeCallbacks(timerRunnable_blowup);
				total_points += current_time;
				Toast.makeText(getApplicationContext(), "Bomb has been defused. Well-Done!", Toast.LENGTH_SHORT).show();
				update_UI();
			}
		});
	}

	public void blowup(){
		timerHandler_tick.removeCallbacks(timerRunnable_tick);
		timerHandler_blowup.removeCallbacks(timerRunnable_blowup);
		boolean_game_on = false;
		Toast.makeText(getApplicationContext(), "TimeUp... Bomb blowing up...!", Toast.LENGTH_SHORT).show();
		update_UI();
	}

	public void update_UI(){
		bt_defuse.setEnabled(boolean_game_on);
		bt_restart.setEnabled(!boolean_game_on);

		tv_time.setText("Time: " + String.valueOf(current_time));
		tv_points.setText("Total Points: " + String.valueOf(total_points));

	}

}
