package com.example.csanchez.applirss;

import android.os.AsyncTask;

/**
 * Created by csanchez on 22/09/15.
 */
public class DownloadRssTask extends AsyncTask<MyRSSsaxHandler, Void, MyRSSsaxHandler> {

    private MainActivity monActivity;
    public DownloadRssTask(MainActivity monActivity) {
        this.monActivity = monActivity;
    }

    @Override
    protected MyRSSsaxHandler doInBackground(MyRSSsaxHandler... handler) {
        handler[0].processFeed();
        return handler[0];
    }

    //The system calls this to perform work in the UI thread and delivers
    //the result from doInBackground()
    protected void onPostExecute(MyRSSsaxHandler handler) {
    // Mise à jours des données de notre vue à partir
    // du titre, date, image et description lus
        monActivity.resetDisplay(handler.getTitle(), handler.getDate(), handler.getImage(), handler.getDescription());
    }
}
