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
import com.markrap.dashboard.outlet.CategoryList;
import com.markrap.dashboard.outlet.ProductList;
import com.markrap.utility.AppConstants;

import org.json.JSONArray;
import org.json.JSONObject;


public class PsrSalesAdapter extends RecyclerView.Adapter<PsrSalesAdapter.MyViewHolder> {

    private MainActivity ira1;
    private JSONArray moviesList;
    Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtPsrSales,txtPsrName,txtPasStockist,txtRoc;

        public MyViewHolder(View view) {
            super(view);


            txtPsrSales=view.findViewById(R.id.txtPsrSales);
            txtPsrName=view.findViewById(R.id.txtPsrName);
            txtPasStockist=view.findViewById(R.id.txtPasStockist);
            txtRoc=view.findViewById(R.id.txtRoc);





        }
    }
    public PsrSalesAdapter(JSONArray moviesList, MainActivity ira) {
        this.moviesList = moviesList;
        ira1 = ira;



    }

    @Override
    public PsrSalesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.psr_list_row, parent, false);
        mContext = parent.getContext();
        return new PsrSalesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder( PsrSalesAdapter.MyViewHolder holder,  int position) {
        try {
            Log.i("---@@position",""+position);
            JSONObject jsonObject = moviesList.getJSONObject(position);
            Log.i("---@@jsonObject",""+jsonObject.toString());


             holder.txtPsrSales.setText(jsonObject.getString("mtd_value"));
             holder.txtPsrName.setText(jsonObject.getString("psr_name"));
             holder.txtPasStockist.setText(jsonObject.getString("stockiest"));
             holder.txtRoc.setText(jsonObject.getString("roc_name"));


        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        System.out.println("Leng==="+moviesList.length());
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