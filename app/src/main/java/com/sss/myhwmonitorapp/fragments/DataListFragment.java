package com.sss.myhwmonitorapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sss.myhwmonitorapp.R;
import com.sss.myhwmonitorapp.adapters.DataListAdapter;
import com.sss.myhwmonitorapp.db.SensorModel;
import com.sss.myhwmonitorapp.viewmodels.DataViewModel;

import java.util.ArrayList;
import java.util.List;


public class DataListFragment extends Fragment {

    private RecyclerView recyclerView;
    private DataListAdapter adapter;
    private List<SensorModel> sensorModelList;
    private Context context;
    private DataViewModel dataViewModel;

    private static final String TAG = "DataListFragment";

    public DataListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
        sensorModelList =new ArrayList<>();
        adapter=new DataListAdapter(sensorModelList,context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dataViewModel = new ViewModelProvider(getActivity()).get(DataViewModel.class);
        return inflater.inflate(R.layout.fragment_data_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.dataRV);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        dataViewModel.getAllSensorData().observe(getViewLifecycleOwner(), new Observer<List<SensorModel>>() {
            @Override
            public void onChanged(List<SensorModel> sensorModels) {
                if (sensorModels!=null){
                    sensorModelList=sensorModels;
                    adapter.updateList(sensorModelList);
                    Log.e(TAG, "onChanged: "+sensorModels.size() );
                }else {
                    Log.d(TAG, "onChanged: data not found");
                }
            }
        });
    }
}