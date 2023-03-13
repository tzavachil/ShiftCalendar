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
import com.example.shiftcalendar.ui.ShiftDetailsBottomSheet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ShiftsFragment extends Fragment {

    private FragmentShiftsBinding binding;

    private ArrayList<Shift> shiftList;

    private FloatingActionButton fab;
    private ListView shiftListView;

    public ShiftsFragment(ArrayList<Shift> sL){
        this.shiftList = sL;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentShiftsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //initializing elements
        this.fab = (FloatingActionButton) root.findViewById(R.id.fab);
        this.shiftListView = (ListView) root.findViewById(R.id.shiftListView);

        ArrayList<Shift> newShiftList = new ArrayList<>(this.shiftList);
        if(newShiftList.get(0).getName().equals(""))
            newShiftList.remove(0);
        ShiftAdapter shiftAdapter = new ShiftAdapter(this, newShiftList);
        this.setUpListeners(this, shiftAdapter);

        this.shiftListView.setAdapter(shiftAdapter);

        return root;
    }

    private void setUpListeners(Fragment thisFragment, ShiftAdapter currShiftAdapter){
        this.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShiftDetailsBottomSheet shiftDetailsBottomSheet = new ShiftDetailsBottomSheet(binding.getRoot().getContext(), currShiftAdapter);
                shiftDetailsBottomSheet.show(thisFragment.getActivity().getSupportFragmentManager(), "TAG");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}