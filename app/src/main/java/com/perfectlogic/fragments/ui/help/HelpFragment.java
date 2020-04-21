package com.perfectlogic.fragments.ui.help;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.perfectlogic.fragments.R;


public class HelpFragment extends Fragment {
    private HelpViewModel helpViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        helpViewModel = ViewModelProviders.of(this).get(HelpViewModel.class);
        View root = inflater.inflate(R.layout.fragment_help, container, false);

        return root;
    }
}
