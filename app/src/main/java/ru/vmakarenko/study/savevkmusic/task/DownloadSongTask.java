package ru.vmakarenko.study.savevkmusic.task;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.FileOutputStream;
import java.io.InputStream;

import ru.vmakarenko.study.savevkmusic.fragment.AudioListFragment;

/**
 * Created by VMakarenko on 05.04.2016.
 */
public class DownloadSongTask extends AsyncTask<DownloadSongTaskParams, Void, Void> {
    AudioListFragment parentFragment;

    public DownloadSongTask(AudioListFragment parentFragment) {
        this.parentFragment = parentFragment;
    }

    DownloadSongTask task;
    @Override
    protected Void doInBackground(DownloadSongTaskParams... params) {
        try {
            Log.d(this.getClass().getName(), "Started downloading " + params[0].getName());
            InputStream in = new java.net.URL(params[0].getLink()).openStream();
            byte[] song = IOUtils.toByteArray(in);
            FileOutputStream fos = new FileOutputStream(params[0].getPath() + params[0].getName());
            fos.write(song);
            publishProgress();
            Log.d(this.getClass().getName(), "Finished downloading " + params[0].getName());
        } catch (Exception e) {
            Log.e(this.getClass().getName(), e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        parentFragment.incCurrentProgress();
    }
}
