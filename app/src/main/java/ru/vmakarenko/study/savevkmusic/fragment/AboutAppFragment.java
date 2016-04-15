package ru.vmakarenko.study.savevkmusic.fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ListFragment;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiAudio;
import com.vk.sdk.api.model.VKApiUser;

import org.json.JSONException;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ru.vmakarenko.study.savevkmusic.MainActivity;
import ru.vmakarenko.study.savevkmusic.R;
import ru.vmakarenko.study.savevkmusic.adapter.AudioListAdapter;
import ru.vmakarenko.study.savevkmusic.interfaces.TagFragment;
import ru.vmakarenko.study.savevkmusic.model.AudioItem;
import ru.vmakarenko.study.savevkmusic.task.DownloadSongTask;
import ru.vmakarenko.study.savevkmusic.task.DownloadSongTaskParams;

/**
 * Created by VMakarenko on 30.03.2016.
 */
public class AboutAppFragment extends DialogFragment implements TagFragment {
    public final static String TAG = "about_fragment_tag";

    @Override
    public String tag() {
        return TAG;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.about_dialog_layout, container, false);
        Button dismiss = (Button) rootView.findViewById(R.id.dismiss);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ((TextView)rootView.findViewById(R.id.about_text)).setText(R.string.about_text);
        getDialog().setTitle(R.string.about);
        return rootView;
    }

}
