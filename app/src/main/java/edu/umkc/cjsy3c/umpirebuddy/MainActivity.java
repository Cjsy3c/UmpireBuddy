package edu.umkc.cjsy3c.umpirebuddy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity{
    // strikes
    private TextView strikeCounter;
    private Integer strikeCount = 0;
    private final String strikePersist = "Strikes";

    // balls
    private TextView ballCounter;
    private Integer ballCount = 0;
    private final String ballPersist = "Balls";

    // total outs
    private TextView outCounter;
    private Integer outs = 0;
    private final String outPersist = "Outs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // save the text fields for later
        strikeCounter = (TextView) findViewById(R.id.StrikeDisplay);
        ballCounter = (TextView) findViewById(R.id.BallDisplay);
        outCounter = (TextView) findViewById(R.id.TotalDisplay);

        if (savedInstanceState != null)
        {
            // this searches the bundle for state info
            strikeCount = savedInstanceState.getInt(strikePersist);
            ballCount = savedInstanceState.getInt(ballPersist);
            outs = savedInstanceState.getInt(outPersist);
            doUpdate();
        }
        else {
            // if not icicle, look in persistent storage
            // this loads persistent storage and gets the outs
            outs = getPreferences(Context.MODE_PRIVATE).getInt(outPersist,0);
            doUpdate();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // create menu
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // menu item selected
        int id = item.getItemId();
        if (id == R.id.about)
        {   // launch about activity
            Intent intent = new Intent(this,About.class );
            startActivity(intent);
        }

        else if(id == R.id.reset)
        {   // reset strike and ball counters to 0
            ballCount = 0;
            strikeCount = 0;
            doUpdate();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // save to persistent storage
        SharedPreferences pref = getPreferences(Context.MODE_PRIVATE);
        pref.edit().putInt(outPersist, outs).commit();

    }

    public void strikeClicked(View v){
        setTitle("Hello StackOverflow");
        if ( strikeCount < 2) {
            strikeCount += 1;
            doUpdate();
        }
        else {
            // launch dialogue box to say player out
            AlertDialog.Builder strikeBox = new AlertDialog.Builder(this);
            strikeBox.setMessage("Strike Out");

            // undo button should do nothing
            strikeBox.setNegativeButton("Cancel", null);

            strikeBox.setPositiveButton("New Player",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // button click
                            strikeCount = 0;
                            ballCount = 0;
                            outs += 1;
                            doUpdate();
                        }
                    });

            strikeBox.setCancelable(false);
            strikeBox.create().show();
        }
    }

    public void doUpdate()
    {   // update screen
        strikeCounter.setText(strikeCount.toString());
        ballCounter.setText(ballCount.toString());
        outCounter.setText(outs.toString());
    }

    public void ballClicked(View v){
        if (ballCount < 3) {
            ballCount += 1;
            doUpdate();
        }
        else{
            // make dialogue box to say player walks
            AlertDialog.Builder ballBox = new AlertDialog.Builder(this);
            ballBox.setMessage(R.string.outMessage);
            ballBox.setPositiveButton(R.string.newPlayer,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // on button click - new player
                            strikeCount = 0;
                            ballCount = 0;
                            doUpdate();
                        }
                    });
            // undo button should do nothing
            ballBox.setNegativeButton("Cancel", null);
            ballBox.setCancelable(false);
            ballBox.create().show();
        }
    }

    protected void onSaveInstanceState(Bundle icicle){
        // save in bundle for screen change
        icicle.putInt(outPersist, (int)outs);
        icicle.putInt(strikePersist, (int)strikeCount);
        icicle.putInt(ballPersist, (int)ballCount);
    }

    protected void onRestoreInstanceState(Bundle icicle){
        if (icicle != null)
        {   // restore from bundle
            strikeCount = icicle.getInt(strikePersist);
            ballCount = icicle.getInt(ballPersist);
            outs = icicle.getInt(outPersist);
            doUpdate();
        }
    }
}
