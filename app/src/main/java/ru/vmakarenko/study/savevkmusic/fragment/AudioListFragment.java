package ru.vmakarenko.study.savevkmusic.fragment;

import android.app.ListFragment;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.vk.sdk.api.methods.VKApiAudio;

import java.util.ArrayList;
import java.util.List;

import ru.vmakarenko.study.savevkmusic.R;
import ru.vmakarenko.study.savevkmusic.list.AudioItem;
import ru.vmakarenko.study.savevkmusic.list.adapter.AudioListAdapter;

/**
 * Created by VMakarenko on 30.03.2016.
 */
public class AudioListFragment extends ListFragment {
    public List<AudioItem> getAudioList() {
        return audioList;
    }

    private List<AudioItem> audioList = new ArrayList<>();
    ArrayAdapter<AudioItem> audioAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        audioAdapter = new AudioListAdapter(inflater.getContext(), R.layout.audio_list_item_layout,R.id.song_title, audioList);

        /** Setting the list adapter for the ListFragment */
        setListAdapter(audioAdapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
