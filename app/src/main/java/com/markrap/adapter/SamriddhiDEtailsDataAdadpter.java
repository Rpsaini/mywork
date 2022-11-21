package com.markrap.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.markrap.R;
import com.markrap.dashboard.samradhii_dashboard.SamraddhiActionDetailsActivity;
import com.markrap.dashboard.samradhii_dashboard.SamraddhiActionaleActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SamriddhiDEtailsDataAdadpter extends RecyclerView.Adapter<SamriddhiDEtailsDataAdadpter.MyViewHolder> {

    private SamraddhiActionDetailsActivity ira1;
    private JSONObject moviesList;
    Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtTgt, txtStkName, txtBgtTarget, txtThreshold, txtUnbilled;

        public MyViewHolder(View view) {
            super(view);
            txtTgt = (TextView) view.findViewById(R.id.txtTgt);
            txtStkName = (TextView) view.findViewById(R.id.txtStkName);
            txtBgtTarget = (TextView) view.findViewById(R.id.txtBgtTarget);
            txtThreshold = (TextView) view.findViewById(R.id.txtThreshold);
            txtUnbilled = (TextView) view.findViewById(R.id.txtUnbilled);


        }
    }

    public SamriddhiDEtailsDataAdadpter(JSONObject moviesList, SamraddhiActionDetailsActivity ira) {
        this.moviesList = moviesList;
        ira1 = ira;


    }

    @Override
    public SamriddhiDEtailsDataAdadpter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.samriddhi_details_listitem_row, parent, false);
        mContext = parent.getContext();
        return new SamriddhiDEtailsDataAdadpter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SamriddhiDEtailsDataAdadpter.MyViewHolder holder, int position) {
        Log.i("---@@position", "" + position);
        try {
            JSONObject jsonObject = moviesList.getJSONObject("BZ Target");
                holder.txtTgt.setText(jsonObject.getString("BZ Target"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public int getItemCount() {
        System.out.println("Leng===" + moviesList.length());
        return moviesList.length();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}