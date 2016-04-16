package ru.vmakarenko.study.savevkmusic.fragment;

import android.app.ListFragment;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.vk.sdk.api.model.VKApiAudio;
import com.vk.sdk.api.model.VKApiUser;

import org.json.JSONException;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import ru.vmakarenko.study.savevkmusic.MainActivity;
import ru.vmakarenko.study.savevkmusic.R;
import ru.vmakarenko.study.savevkmusic.interfaces.TagFragment;
import ru.vmakarenko.study.savevkmusic.model.AudioItem;
import ru.vmakarenko.study.savevkmusic.adapter.AudioListAdapter;
import ru.vmakarenko.study.savevkmusic.task.DownloadSongTask;
import ru.vmakarenko.study.savevkmusic.task.DownloadSongTaskParams;

/**
 * Created by VMakarenko on 30.03.2016.
 */
public class AudioListFragment extends ListFragment implements TagFragment {
    public final static String TAG = "audio_list_fragment_tag";

    @Override
    public String tag() {
        return TAG;
    }

    private List<AudioItem> audioList = new ArrayList<>();
    private AudioListAdapter<AudioItem> audioAdapter;
    private VKApiUser currentUser = null;


    private String id = null;
    private String searchString;

    private TextView progressTextView = null;
    private LinearLayout progressLayout = null;

    private int currentProgress = 0;
    private int total = 0;
    ListType type = ListType.USER;

    public List<AudioItem> getAudioList() {
        return audioList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        audioAdapter = new AudioListAdapter<>(inflater.getContext(), R.layout.audio_list_item_layout, R.id.song_title, audioList);

        /** Setting the list adapter for the ListFragment */
        setListAdapter(audioAdapter);
        progressLayout = (LinearLayout) getActivity().findViewById(R.id.progressLayout);
        progressTextView = (TextView) getActivity().findViewById(R.id.progressText);

        ((MainActivity) getActivity()).setFragmentMode(MainActivity.FragmentsAvailable.AUDIO);

        VKRequest req = VKApi.users().get();
        if (id != null) {
            req.addExtraParameter(VKApiConst.USER_ID, id);
        }

        req.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                try {
                    currentUser = new VKApiUser().parse(response.json.optJSONArray("response").getJSONObject(0));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        updateList();

        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void updateList() {
        VKRequest req = null;
        if(type == ListType.GROUP || type==ListType.USER) {
            req = VKApi.audio().get();
            if (id != null) {
                int localId = Integer.parseInt(id);
                if (type == ListType.GROUP) {
                    localId = -localId;
                }
                req.addExtraParameters(VKParameters.from(VKApiConst.OWNER_ID, localId));
            }
        }else{
            req = VKApi.audio().search(VKParameters.from(VKApiConst.Q, searchString));
            if (type == ListType.AUTHOR){
                req.addExtraParameter("performer_only", "1");
            }
            if (type == ListType.TITLE){
                req.addExtraParameter("performer_only", "0");
            }
        }
        req.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                try {
                    audioList.clear();
                    Type type = new TypeToken<List<AudioItem>>() {
                    }.getType();
                    audioList.addAll(
                            (List<AudioItem>) new Gson().fromJson(response.json.getJSONObject("response").getJSONArray("items").toString(), type));

                    Log.d(this.getClass().getName(), "audio loaded");
                    audioAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    Log.e(this.getClass().getName(), "Error parsing audio response");
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VKError error) {
                Log.e(this.getClass().getName(),
                        error.errorCode + " " + error.errorMessage + " " + error.errorReason);
                if (error.apiError != null && error.apiError.errorCode == 201) {
                    Toast.makeText(getActivity(), R.string.audio_for_user_is_not_available,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public AudioListFragment setUserId(String id) {
        this.id = id;
        type = ListType.USER;
        updateList();
        return this;
    }

    public AudioListFragment setGroupId(String userId) {
        this.id = userId;
        type = ListType.GROUP;
        updateList();
        return this;
    }

    public AudioListFragment setTitleSearchString(String searchString){
        this.searchString = searchString;
        type = ListType.TITLE;
        updateList();
        return this;
    }

    public AudioListFragment setAuthorSearchString(String searchString){
        this.searchString = searchString;
        type = ListType.AUTHOR;
        updateList();
        return this;
    }

    private boolean shouldAskPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    public void downloadAllReq() {
        if (shouldAskPermission()) {
            String[] perms = {"android.permission.WRITE_EXTERNAL_STORAGE"};
            int permsRequestCode = 200;
            requestPermissions(perms, permsRequestCode);
        } else {
            downloadAll();
        }
    }

    public void downloadAll() {
        int i = 0;
        progressLayout.setVisibility(View.VISIBLE);
        String basicPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/";
        String localPath = "VK/" + currentUser.last_name + "_" + currentUser.first_name + "/";
        File dir = new File(basicPath + localPath);
        boolean success = true;
        if (!dir.exists()) {
            success = dir.mkdir();
        }
        if (success) {
            Log.d(this.getClass().getName(), "Started creating loading music tasks");
            total = audioList.size();
            for (AudioItem item : audioList) {
                DownloadSongTaskParams params = DownloadSongTaskParams.from(item);
                params.setPath(dir.getAbsolutePath() + "/");
                params.setName(item.getArtist() + "-" + item.getTitle() + ".mp3");
                params.setLink(item.getUrl());
                new DownloadSongTask(this).execute(params);
//                if (i++ > 0) {
//                    return;
//                }
            }
            Log.d(this.getClass().getName(), "Finished creating loading music tasks");
        } else {
            Toast.makeText(getActivity(), R.string.illegal_folder_name_for_music, Toast.LENGTH_SHORT).show();
        }
        progressLayout.setVisibility(View.GONE);
    }

    private void refreshProgressLayout() {
        progressTextView.setText(String.format(getResources().getString(R.string.ready_msg), currentProgress, audioList.size()));
        if (currentProgress == total) {
            total = currentProgress = 0;
            progressLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {

        switch (permsRequestCode) {
            case 200:
                downloadAll();
                break;
        }
    }


    public void incCurrentProgress() {
        this.currentProgress++;
        refreshProgressLayout();
    }

    enum ListType {
        GROUP, USER, AUTHOR, TITLE
    }
}
