package ru.vmakarenko.study.savevkmusic.fragment.nested;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ResourceCursorAdapter;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.List;

import ru.vmakarenko.study.savevkmusic.R;
import ru.vmakarenko.study.savevkmusic.interfaces.TagFragment;

/**
 * Created by VMakarenko on 30.03.2016.
 */
public class FindAudioOuterFragment extends Fragment implements TagFragment {
    public final static String TAG = "find_audio_outer_fragment_tag";
    List<RadioButton> rbList = new ArrayList<>();
    TableRow groupRow, userRow, titleRow, authorRow;

    @Override
    public String tag() {
        return TAG;
    }

    protected void uncheckRadioButtons(){
        for(RadioButton rb : rbList){
            rb.setChecked(false);
        }
    }

    private void findAndSetViews(View view){
        rbList.add((RadioButton) view.findViewById(R.id.rb_find_by_group_id));
        rbList.add((RadioButton) view.findViewById(R.id.rb_find_by_user_id));
        rbList.add((RadioButton) view.findViewById(R.id.rb_find_by_title));
        rbList.add((RadioButton) view.findViewById(R.id.rb_find_by_author));

        authorRow = (TableRow) view.findViewById(R.id.find_by_author_row);
        titleRow = (TableRow) view.findViewById(R.id.find_by_title_row);
        groupRow = (TableRow) view.findViewById(R.id.find_by_group_row);
        userRow = (TableRow) view.findViewById(R.id.find_by_user_row);
    }

    private void setListeners(View view){
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.find_audio_outer_fragment, container, false);
        findAndSetViews(view);
        setListeners(view);

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


}
