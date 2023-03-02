package com.example.shiftcalendar.ui.shifts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.shiftcalendar.R;
import com.example.shiftcalendar.Shift;
import com.example.shiftcalendar.databinding.FragmentShiftsBinding;

import java.util.ArrayList;

public class ShiftsFragment extends Fragment {

    private FragmentShiftsBinding binding;

    private ArrayList<Shift> shiftList;

    private ListView shiftListView;

    public ShiftsFragment(ArrayList<Shift> sL){
        this.shiftList = sL;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentShiftsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //initializing elements
        this.shiftListView = (ListView) root.findViewById(R.id.shiftListView);

        ShiftAdapter shiftAdapter = new ShiftAdapter(this.getContext(), this.shiftList);

        this.shiftListView.setAdapter(shiftAdapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}