package ru.vmakarenko.study.savevkmusic.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.vmakarenko.study.savevkmusic.R;

/**
 * Created by VMakarenko on 30.03.2016.
 */
public class FindAudioFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.extra_audio_fragment, null);
    }
}
