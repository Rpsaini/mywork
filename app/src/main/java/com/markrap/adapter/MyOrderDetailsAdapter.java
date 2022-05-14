package com.markrap.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.markrap.R;
import com.markrap.dashboard.myorders.MyOrdersDetailActivity;
import com.markrap.dashboard.outlet.CategoryList;
import com.markrap.dashboard.outlet.ViewOutLets;
import com.markrap.utility.AppConstants;

import org.json.JSONArray;
import org.json.JSONObject;

public class MyOrderDetailsAdapter extends RecyclerView.Adapter<MyOrderDetailsAdapter.MyViewHolder> {

private AppCompatActivity ira1;
private JSONObject moviesList;
Context mContext;

public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView taskDescription,txtTaskName,address;
    ImageView taskImage;
    LinearLayout mainLinear;
    public MyViewHolder(View view) {
        super(view);

/*

        taskDescription=view.findViewById(R.id.taskDescription);
        taskImage=view.findViewById(R.id.taskImage);
        txtTaskName=view.findViewById(R.id.txtTaskName);
        address=view.findViewById(R.id.address);
        mainLinear=view.findViewById(R.id.mainLinear);
*/




    }
}
    public MyOrderDetailsAdapter(JSONObject moviesList, MyOrdersDetailActivity ira1) {
        this.moviesList = moviesList;
        ira1 = ira1;



    }

    @Override
    public MyOrderDetailsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_orderdetails_listrow, parent, false);
        mContext = parent.getContext();
        return new MyOrderDetailsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyOrderDetailsAdapter.MyViewHolder holder, final int position) {
        try {
         ///   System.out.println("--order_date--"+moviesList.getString("order_date"));
          /*  JSONObject jsonObject = moviesList.getJSONObject(position);
            holder.taskDescription.setText(jsonObject.getString("owner_name"));
            holder.txtTaskName.setText(jsonObject.getString("outlet_name"));
            holder.address.setText(jsonObject.getString("address"));


            holder.mainLinear.setTag(jsonObject+"");
            holder.mainLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {


                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });



//            "id": "1",
//                    "supplier_id": "1",
//                    "user_id": null,
//                    "outlet_name": "Jeet Elect",
//                    "owner_name": "Jeet Kumar",
//                    "mobile": "8888888",
//                    "latitude": "20.2544",
//                    "longitude": "40.26444",
//                    "address": "asf dfd fd dfdf",
//                    "publish": "1",
//                    "created_at": "2022-03-12 16:18:50"

          //  showImage(jsonObject.getString("image"),holder.taskImage);
*/
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
