package com.sss.myhwmonitorapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sss.myhwmonitorapp.R;
import com.sss.myhwmonitorapp.db.SensorModel;

import java.text.DecimalFormat;
import java.util.List;

public class DataListAdapter extends RecyclerView.Adapter<DataListAdapter.DataVH> {

    private List<SensorModel> modelList;
    private Context context;

    private static final String TAG = "DataListAdapter";

    public DataListAdapter(List<SensorModel> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;
    }

    @NonNull
    @Override
    public DataVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_data, parent, false);
        return new DataVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataVH holder, int position) {
        SensorModel model=modelList.get(position);
        holder.pr.setText(String.valueOf(model.getPrxmty_dis()));
        holder.lg.setText(String.valueOf(model.getLux()));
        holder.gx.setText(new DecimalFormat("##.######").format(model.getxGyro()));
        holder.gy.setText(new DecimalFormat("##.######").format(model.getyGyro()));
        holder.gz.setText(new DecimalFormat("##.######").format(model.getzGyro()));
        holder.ax.setText(new DecimalFormat("##.######").format(model.getxAclmtr()));
        holder.ay.setText(new DecimalFormat("##.######").format(model.getyAclmtr()));
        holder.az.setText(new DecimalFormat("##.######").format(model.getzAclmtr()));

    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: "+modelList.size());
        return modelList.size();
    }

    public void updateList(List<SensorModel> sensorModels) {
        this.modelList=sensorModels;
        notifyDataSetChanged();
    }

    public class DataVH extends RecyclerView.ViewHolder {

        private TextView gx, gy,gz,ax,ay,az, pr,lg;

        public DataVH(@NonNull View itemView) {
            super(itemView);
            pr=itemView.findViewById(R.id.pr);
            lg=itemView.findViewById(R.id.lg);
            az=itemView.findViewById(R.id.az);
            ay=itemView.findViewById(R.id.ay);
            ax=itemView.findViewById(R.id.ax);
            gx=itemView.findViewById(R.id.gx);
            gy=itemView.findViewById(R.id.gy);
            gz=itemView.findViewById(R.id.gz);
        }
    }
}
