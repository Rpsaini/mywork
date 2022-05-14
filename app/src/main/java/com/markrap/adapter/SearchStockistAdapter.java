package com.markrap.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.markrap.R;
import com.markrap.dashboard.outlet.CategoryList;
import com.markrap.dashboard.outlet.ProductList;
import com.markrap.dashboard.outlet.SearchStockist;
import com.markrap.utility.AppConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class SearchStockistAdapter extends RecyclerView.Adapter<SearchStockistAdapter.MyViewHolder> {

    private SearchStockist ira1;
    private ArrayList<JSONObject> moviesList;
    Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtStorename;
        LinearLayout mainLinear;
        public MyViewHolder(View view) {
            super(view);


            txtStorename=view.findViewById(R.id.txtStorename);
            mainLinear=view.findViewById(R.id.mainLinear);

        }
    }
    public SearchStockistAdapter(ArrayList<JSONObject> moviesList, SearchStockist ira) {
        this.moviesList = moviesList;
        ira1 = ira;



    }

    @Override
    public SearchStockistAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_stockist_layout, parent, false);
        mContext = parent.getContext();
        return new SearchStockistAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SearchStockistAdapter.MyViewHolder holder, final int position) {
        try {

            JSONObject jsonObject = moviesList.get(position);
            holder.txtStorename.setText(jsonObject.getString("store_name"));

            holder.mainLinear.setTag(jsonObject+"");
            holder.mainLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        JSONObject dataOb=new JSONObject(v.getTag().toString());

                        Intent intent=new Intent();
                        intent.putExtra(AppConstants.storeName,dataOb.getString("store_name"));
                        intent.putExtra(AppConstants.id,dataOb.getString("id"));
                        ira1.setResult(ira1.RESULT_OK,intent);
                        ira1.finish();


                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });


        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
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
