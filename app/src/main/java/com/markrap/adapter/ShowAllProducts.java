package com.markrap.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.markrap.R;
import com.markrap.dashboard.outlet.ProductList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ShowAllProducts extends RecyclerView.Adapter<ShowAllProducts.MyViewHolder> {

    private ProductList ira1;
    private JSONArray moviesList;
    Context mContext;

   public Map<Integer, Integer> cartItems = new HashMap<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtCateName, txtCount,txtPrice;

        ImageView imgPlus, imgMinus;

        public MyViewHolder(View view) {
            super(view);

            txtCateName = view.findViewById(R.id.txtCateName);
            txtPrice = view.findViewById(R.id.txtPrice);
            imgPlus = view.findViewById(R.id.imgPlus);
            imgMinus = view.findViewById(R.id.imgMinus);
            txtCount = view.findViewById(R.id.txtCount);


        }
    }

    public ShowAllProducts(JSONArray moviesList, ProductList ira) {
        this.moviesList = moviesList;
        ira1 = ira;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_list_row, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {

            JSONObject jsonObject = moviesList.getJSONObject(position);
            holder.txtCateName.setText(jsonObject.getString("title"));
            holder.txtPrice.setText("Price : "+jsonObject.getString("price"));


//         [{"id":"1","title":"MIAM APPLE WATCH STRAP - BLACK","category":"Electronics","brand":"Apple","hsn":"APP254655","sku":"APP-005","supp_name":null,"price":"2400","cost":"2300","amount":null,"tax":"18","description"

            holder.imgMinus.setTag(position);
            holder.imgPlus.setTag(position);


            holder.imgMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int itemId = Integer.parseInt(v.getTag().toString());

                    if (cartItems.containsKey(itemId)) {
                        int cartCount = cartItems.get(itemId);
                        if (cartCount > 0) {
                            cartCount = cartCount - 1;
                            cartItems.put(itemId, cartCount);
                            holder.txtCount.setText(cartCount + "");
                        } else {

                            holder.txtCount.setText("0");
                        }

                        ira1.calculate(cartItems);
                    }

                }
            });


            holder.imgPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int itemId = Integer.parseInt(v.getTag().toString());
                    if(cartItems.containsKey(itemId))
                    {
                        int cartCount = cartItems.get(itemId) + 1;
                        cartItems.put(itemId, cartCount);
                        holder.txtCount.setText(cartCount + "");
                    }
                    else
                    {
                        cartItems.put(itemId, 1);
                        holder.txtCount.setText("1");
                    }
                    ira1.calculate(cartItems);

                }
            });
           }
        catch(Exception e) {
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


    private void showImage(String image_url, ImageView imageView)
    {
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.defaultimage)
                .error(R.drawable.defaultimage);
        Glide.with(ira1).load(image_url).apply(requestOptions).into(imageView);
    }




}
