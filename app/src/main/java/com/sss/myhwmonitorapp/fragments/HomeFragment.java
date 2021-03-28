package com.sss.myhwmonitorapp.fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.sss.myhwmonitorapp.R;
import com.sss.myhwmonitorapp.db.SensorDb;
import com.sss.myhwmonitorapp.db.SensorModel;
import com.sss.myhwmonitorapp.services.SensorService;
import com.sss.myhwmonitorapp.viewmodels.HomeViewModel;

import java.text.DecimalFormat;

import static com.sss.myhwmonitorapp.utils.SensorPreference.IS_RUNNING;
import static com.sss.myhwmonitorapp.utils.SensorPreference.isServiceRunning;
import static com.sss.myhwmonitorapp.utils.SensorPreference.setServiceRunning;


public class HomeFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private TextView tlTv, trTv, blTv, brTv;
    private ConstraintLayout parent;
    private LinearLayout top, bottom;
    private Button startBtn;
    private Menu menu;
    private Context context;
    private SensorDb sensorDb;

    private Intent serviceIntent;

    private HomeViewModel viewModel;


    private boolean isBound = false;
    private SensorService sensorService;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            SensorService.LocalBinder binder = (SensorService.LocalBinder) service;
            sensorService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            sensorService = null;
            isBound = false;
        }
    };


    private static final String TAG = "HomeFragment";

    public HomeFragment() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        serviceIntent = new Intent(context, SensorService.class);
//        ContextCompat.startForegroundService(context, serviceIntent);
//        setServiceRunning(context,true);
    }

    @Override
    public void onStart() {
        super.onStart();

        getActivity().bindService(serviceIntent, mServiceConnection,
                Context.BIND_ADJUST_WITH_ACTIVITY);
        if (!isServiceRunning(context)) {
            context.startService(serviceIntent);
            setServiceRunning(context, true);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        viewModel = new ViewModelProvider(getActivity()).get(HomeViewModel.class);

        sensorDb = SensorDb.getInstance(context);


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        trTv = view.findViewById(R.id.trTV);
        tlTv = view.findViewById(R.id.tlTV);
        brTv = view.findViewById(R.id.brTV);
        blTv = view.findViewById(R.id.blTV);
        startBtn = view.findViewById(R.id.btn_start);
        parent = view.findViewById(R.id.parent);
        top = view.findViewById(R.id.top);
        bottom = view.findViewById(R.id.bottom);


        viewModel.getSingleSensorData().observe(getViewLifecycleOwner(), new Observer<SensorModel>() {
            @Override
            public void onChanged(SensorModel model) {

                if (model != null) {
                    setView(model);
                } else {
                    Toast.makeText(context, "no data found", Toast.LENGTH_SHORT).show();
                }

                Log.e(TAG, "onChanged: ");

            }
        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onStop() {
        if (isBound) {
            getActivity().unbindService(mServiceConnection);
            isBound = false;
        }
        super.onStop();
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        updateMenuTitles(isServiceRunning(context));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.option_menu, menu);
        this.menu = menu;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_monitor:
                if (isServiceRunning(context)) {
                    context.stopService(serviceIntent);
                    setServiceRunning(context, false);
                    updateMenuTitles(false);
                } else {
                    context.startService(serviceIntent);
                    setServiceRunning(context, true);
                    updateMenuTitles(true);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setView(SensorModel model) {
        tlTv.setText(new DecimalFormat("##.######").format(model.getxGyro()));
        trTv.setText("" + model.getLux());
        blTv.setText("" + model.getPrxmty_dis());
        brTv.setText(new DecimalFormat("##.######").format(model.getxAclmtr()));
    }

    private void updateMenuTitles(boolean isActive) {
        MenuItem bedMenuItem = menu.findItem(R.id.action_monitor);
        if (isActive) {
            bedMenuItem.setTitle("Stop Monitoring");
        } else {
            bedMenuItem.setTitle("Start Monitoring");
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
        int currentOrientation = getResources().getConfiguration().orientation;

        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE){
            Log.d(TAG,"Landscape !!!");
            top.setVisibility(View.INVISIBLE);
            bottom.setVisibility(View.INVISIBLE);
            parent.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 25f));
        }
        else {
            Log.d(TAG,"Portrait !!!");
            top.setVisibility(View.VISIBLE);
            bottom.setVisibility(View.VISIBLE);
            parent.setLayoutParams(new LinearLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, 0,4f));
        }
    }
}