package ru.vmakarenko.study.savevkmusic.fragment.nested;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.List;

import ru.vmakarenko.study.savevkmusic.R;
import ru.vmakarenko.study.savevkmusic.fragment.AudioListFragment;
import ru.vmakarenko.study.savevkmusic.interfaces.TagFragment;

/**
 * Created by VMakarenko on 30.03.2016.
 */
public class FindAudioOuterFragment extends Fragment implements TagFragment {
    public final static String TAG = "find_audio_outer_fragment_tag";
    RadioButton rbGroup, rbUser, rbTitle, rbAuthor;
    List<RadioButton> rbList = new ArrayList<>();
    TableRow groupRow, userRow, titleRow, authorRow;
    AudioListFragment audioListFragment = null;
    EditText etGroup, etUser, etTitle, etAuthor;

    @Override
    public String tag() {
        return TAG;
    }

    protected void uncheckRadioButtons() {
        for (RadioButton rb : rbList) {
            rb.setChecked(false);
        }
    }

    private void findAndSetViews(View view) {
        rbGroup = (RadioButton) view.findViewById(R.id.rb_find_by_group_id);
        rbUser = (RadioButton) view.findViewById(R.id.rb_find_by_user_id);
        rbTitle = (RadioButton) view.findViewById(R.id.rb_find_by_title);
        rbAuthor = (RadioButton) view.findViewById(R.id.rb_find_by_author);

        etGroup = (EditText) view.findViewById(R.id.et_find_by_group_id);
        etUser = (EditText) view.findViewById(R.id.et_find_by_user_id);
        etTitle = (EditText) view.findViewById(R.id.et_find_by_title);
        etAuthor = (EditText) view.findViewById(R.id.et_find_by_author);

        rbList.add(rbGroup);
        rbList.add(rbUser);
        rbList.add(rbTitle);
        rbList.add(rbAuthor);

        authorRow = (TableRow) view.findViewById(R.id.find_by_author_row);
        titleRow = (TableRow) view.findViewById(R.id.find_by_title_row);
        groupRow = (TableRow) view.findViewById(R.id.find_by_group_row);
        userRow = (TableRow) view.findViewById(R.id.find_by_user_row);
    }

    private void setListeners(View view) {
        RowLayoutListener rowLayoutListener = new RowLayoutListener();
        EditTextListener editTextListener = new EditTextListener();

        authorRow.setOnClickListener(rowLayoutListener);
        titleRow.setOnClickListener(rowLayoutListener);
        groupRow.setOnClickListener(rowLayoutListener);
        userRow.setOnClickListener(rowLayoutListener);

        view.findViewById(R.id.et_find_by_group_id).setOnFocusChangeListener(editTextListener);
        view.findViewById(R.id.et_find_by_user_id).setOnFocusChangeListener(editTextListener);
        view.findViewById(R.id.et_find_by_author).setOnFocusChangeListener(editTextListener);
        view.findViewById(R.id.et_find_by_title).setOnFocusChangeListener(editTextListener);

        view.findViewById(R.id.outer_search_btn).setOnClickListener(new SearchClickListener());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.find_audio_outer_fragment, container, false);
        findAndSetViews(view);
        setListeners(view);
        audioListFragment = new AudioListFragment();
        getChildFragmentManager().beginTransaction().
                replace(R.id.audio_outer_nested_list, audioListFragment, audioListFragment.getTag()).commit();

        return view;
    }

    class RowLayoutListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            uncheckRadioButtons();
            if (v instanceof TableRow) {
                TableRow tr = (TableRow) v;
                ((RadioButton) tr.getChildAt(0)).setChecked(true);
            }
        }
    }

    class EditTextListener implements View.OnFocusChangeListener {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            uncheckRadioButtons();
            if (hasFocus) {
                if (v instanceof EditText) {
                    TableRow tr = (TableRow) v.getParent();
                    ((RadioButton) tr.getChildAt(0)).setChecked(true);
                }
            }
        }
    }

    class SearchClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (rbUser.isChecked()) {
                audioListFragment.setUserId(etUser.getText().toString());
            }
            if (rbGroup.isChecked()) {
                audioListFragment.setGroupId(etGroup.getText().toString());
            }
            if (rbTitle.isChecked()) {
                audioListFragment.setTitleSearchString(etTitle.getText().toString());
            }
            if (rbAuthor.isChecked()) {
                audioListFragment.setAuthorSearchString(etAuthor.getText().toString());
            }
        }
    }


}
