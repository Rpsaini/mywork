package com.markrap.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.markrap.R;
import com.markrap.dashboard.outlet.CategoryList;
import com.markrap.dashboard.outlet.ProductList;
import com.markrap.dashboard.outlet.ViewOutLets;
import com.markrap.utility.AppConstants;

import org.json.JSONArray;
import org.json.JSONObject;


public class CategegoryAdapter extends RecyclerView.Adapter<CategegoryAdapter.MyViewHolder> {

    private CategoryList ira1;
    private JSONArray moviesList;
    Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtCateName;
        LinearLayout mainLinear;
        public MyViewHolder(View view) {
            super(view);


            txtCateName=view.findViewById(R.id.txtCateName);
            mainLinear=view.findViewById(R.id.mainLinear);





        }
    }
    public CategegoryAdapter(JSONArray moviesList, CategoryList ira) {
        this.moviesList = moviesList;
        ira1 = ira;



    }

    @Override
    public CategegoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_list_row_new, parent, false);
        mContext = parent.getContext();
        return new CategegoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CategegoryAdapter.MyViewHolder holder, final int position) {
        try {

            JSONObject jsonObject = moviesList.getJSONObject(position);
            holder.txtCateName.setText(jsonObject.getString("name"));


//            [{"id":"4","name":"Electronics","parent_id":"0","created_at":"2021-03-23 18:03:45"},{"id":"5","name":"Robotic Automation","parent_id":"0","created_at":"2021-06-03 16:51:4

            holder.mainLinear.setTag(jsonObject+"");
            holder.mainLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        JSONObject dataObj=new JSONObject(v.getTag().toString());
                        Intent intent = new Intent(ira1, ProductList.class);
                        intent.putExtra(AppConstants.category, dataObj.getString("name"));
                        intent.putExtra(AppConstants.id, dataObj.getString("id"));
                        ira1.startActivity(intent);
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
