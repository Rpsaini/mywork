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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.markrap.R;
import com.markrap.dashboard.myorders.MyOrdersDetailActivity;
import com.markrap.dashboard.outlet.ProductList;
import com.markrap.utility.AppConstants;

import org.json.JSONArray;
import org.json.JSONObject;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyViewHolder> {

    private AppCompatActivity ira1;
    private JSONArray moviesList;
    Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtOderBy,txtOrderdate,txtOrdername,txtOrderAmount,txtOderComment,txtOrderstatus,txtOrderQty,outlatname,productname;
        ImageView orderImg;
        LinearLayout mainLinear;
        public MyViewHolder(View view) {
            super(view);

            txtOderBy=view.findViewById(R.id.txtOderBy);
            txtOrdername=view.findViewById(R.id.txtOrdername);
            txtOrderAmount=view.findViewById(R.id.txtOrderAmount);
            txtOrderdate=view.findViewById(R.id.txtOrderdate);
            orderImg=view.findViewById(R.id.orderImg);
            txtOderComment=view.findViewById(R.id.txtOderComment);
            txtOrderstatus=view.findViewById(R.id.txtOrderstatus);
            txtOrderQty=view.findViewById(R.id.txtOrderQty);
            productname=view.findViewById(R.id.productname);
            outlatname=view.findViewById(R.id.outlatname);

            mainLinear= view.findViewById(R.id.mainLinear);

        }
    }
    public MyOrderAdapter(JSONArray moviesList, AppCompatActivity ira) {
        this.moviesList = moviesList;
        ira1 = ira;



    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.myorder_list_row, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {

            JSONObject jsonObject = moviesList.getJSONObject(position);




            holder.txtOderBy.setText("Order Id : "+jsonObject.getString("order_id"));
         //   holder.txtOrderQty.setText("Qty : "+jsonObject.getString("quantaties"));
            holder.txtOrderAmount.setText("Amount : "+jsonObject.getString("amount"));
            holder.txtOrderdate.setText("Date : "+jsonObject.getString("order_date"));
            holder.txtOrdername.setText("Order ID :"+jsonObject.getString("order_id"));
//            holder.txtOderBy.setText("Sup Id : "+jsonObject.getString("supplier_id"));
            holder.txtOrderstatus.setText("Status : "+jsonObject.getString("status"));
            holder.txtOderComment.setText(jsonObject.getString("comments"));


            holder.outlatname.setText(jsonObject.getString("outlet_name"));
          //  holder.productname.setText(jsonObject.getString("prod_name"));
            holder.mainLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        Intent intent = new Intent(ira1, MyOrdersDetailActivity.class);
                        //     intent.putExtra(AppConstants.category, dataObj.getString("name"));
                        intent.putExtra(AppConstants.id, jsonObject.getString("id"));
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



    private void showImage(String image_url,ImageView imageView)
    {
        RequestOptions requestOptions=new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.defaultimage)
                .error(R.drawable.defaultimage);
        Glide.with(ira1).load(image_url).apply(requestOptions).into(imageView);
    }

}