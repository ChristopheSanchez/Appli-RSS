package com.example.csanchez.applirss;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private MyRSSsaxHandler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        handler = new MyRSSsaxHandler();
        handler.setUrl("http://www.nasa.gov/rss/image_of_the_day.rss");
        Toast.makeText(this, "Chargement image: " + handler.getNumber(), Toast.LENGTH_LONG).show();
        new DownloadRssTask(this).execute(handler);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void resetDisplay(String title, String date, Bitmap image, String description) {
        TextView titre = (TextView) findViewById(R.id.imageTitle);
        TextView imDate = (TextView) findViewById(R.id.imageDate);
        TextView imDescription = (TextView) findViewById(R.id.imageDescription);
        ImageView images = (ImageView) findViewById(R.id.imageDisplay);

        titre.setText(title);
        imDate.setText(date);
        imDescription.setText(description);
        images.setImageBitmap(image);
    }

    public void onClickPrecedent(View w) {
        int i, j;
        j = handler.getNumItemMax();
        if (handler.getNumber() > 1) {
            i = handler.getNumber() - 1;
        } else {
            i = j;
        }
        handler = new MyRSSsaxHandler();
        handler.setNumber(i);
        handler.processFeed();
    }

    public void onClickSuivant(View v) {
        int i, j;
        j = handler.getNumItemMax();
        if (handler.getNumber() < j) {
            i = handler.getNumber() + 1;
        } else {
            i = 1;
        }
        handler = new MyRSSsaxHandler();
        handler.setNumber(i);
        handler.setNumItemMax(j);
        handler.processFeed();
    }

    public void onClickPremier(View v) {
        handler = new MyRSSsaxHandler();
        handler.setNumber(1);
        handler.processFeed();
    }

    public void onClickDernier(View v) {
        int j = handler.getNumItemMax();
        handler = new MyRSSsaxHandler();
        handler.setNumber(j);
        handler.processFeed();
    }
}
