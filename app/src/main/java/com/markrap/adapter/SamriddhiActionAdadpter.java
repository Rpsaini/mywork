package com.markrap.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.markrap.R;
import com.markrap.dashboard.MainActivity;
import com.markrap.dashboard.myorders.MyOrdersDetailActivity;
import com.markrap.dashboard.samradhii_dashboard.SamraddhiActionDetailsActivity;
import com.markrap.dashboard.samradhii_dashboard.SamraddhiActionaleActivity;
import com.markrap.utility.AppConstants;
import com.markrap.utility.PrefHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SamriddhiActionAdadpter extends RecyclerView.Adapter<SamriddhiActionAdadpter.MyViewHolder> {

    private SamraddhiActionaleActivity ira1;
    private JSONArray moviesList;
    Context mContext;
    // Define listener member variable
/*
    private static OnItemClickListener listener;

    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
*/

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtStkId, txtStkName, txtBgtTarget, txtThreshold, txtUnbilled;
        LinearLayout MainLinearView;

        public MyViewHolder(View view) {
            super(view);
            MainLinearView = view.findViewById(R.id.MainLinearView);
            txtStkId = view.findViewById(R.id.txtStkId);
            txtStkName = view.findViewById(R.id.txtStkName);
            txtBgtTarget = view.findViewById(R.id.txtBgtTarget);
            txtThreshold = view.findViewById(R.id.txtThreshold);
            txtUnbilled = view.findViewById(R.id.txtUnbilled);


        }
    }

    public SamriddhiActionAdadpter(JSONArray moviesList, SamraddhiActionaleActivity ira) {
        this.moviesList = moviesList;
        ira1 = ira;


    }

    @Override
    public SamriddhiActionAdadpter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.samriddhi_listitem_row, parent, false);
        mContext = parent.getContext();
        return new SamriddhiActionAdadpter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SamriddhiActionAdadpter.MyViewHolder holder, int position) {
        Log.i("---@@position", "" + position);
        JSONObject jsonObject = null;
        try {
            jsonObject = moviesList.getJSONObject(position);
            Log.i("---@@jsonObject", "" + jsonObject.toString());

            holder.txtStkId.setText(jsonObject.getString("sifi_id"));
            holder.txtStkName.setText(jsonObject.getString("stockist_name"));
            holder.txtBgtTarget.setText(jsonObject.getString("ba_target"));

            holder.txtThreshold.setText(jsonObject.getString("threshold"));

            holder.txtUnbilled.setText(jsonObject.getString("achieve"));
            holder.MainLinearView.setTag(jsonObject + "");
            holder.MainLinearView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                       // PrefHelper.getInstance().storeSharedValue("selectedItemStr", String.valueOf(selectedItemStr));

                        JSONObject dataOb = new JSONObject(v.getTag().toString());
                        Intent intent = new Intent(mContext, SamraddhiActionDetailsActivity.class);
                        intent.putExtra(AppConstants.stockiest_id, dataOb.getString("stockist_id"));
                        intent.putExtra("stockist_name", dataOb.getString("stockist_name"));
                        intent.putExtra("ba_target", dataOb.getString("ba_target"));
                        intent.putExtra("threshold", dataOb.getString("threshold"));

                        intent.putExtra("achieve", dataOb.getString("achieve"));


                        mContext.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        System.out.println("@@Leng===" + moviesList.length());
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