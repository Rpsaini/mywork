package com.markrap.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.app.preferences.SavePreferences;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.markrap.R;
import com.markrap.communication.CallBack;
import com.markrap.communication.ServerHandler;
import com.markrap.dashboard.MainActivity;
import com.markrap.login.LoginActivity;
import com.markrap.utility.AppConstants;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.MyViewHolder> {

    private MainActivity ira1;
    private JSONArray moviesList;
    Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView taskDate,taskDescription,txtTaskName,taskPriority,taskstatus,taskCommnet;
        ImageView taskImage,edit;
        public MyViewHolder(View view) {
            super(view);

            taskDate=view.findViewById(R.id.taskDate);
            taskDescription=view.findViewById(R.id.taskDescription);
            taskImage=view.findViewById(R.id.taskImage);
            txtTaskName=view.findViewById(R.id.txtTaskName);
            taskPriority=view.findViewById(R.id.taskPriority);
            taskstatus=view.findViewById(R.id.taskstatus);
            taskCommnet=view.findViewById(R.id.taskCommnet);
            edit=view.findViewById(R.id.edit);


        }
    }
    public TaskListAdapter(JSONArray moviesList, MainActivity ira) {
        this.moviesList = moviesList;
        ira1 = ira;



    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemview_store_list_selection, parent, false);
        mContext = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {

            JSONObject jsonObject = moviesList.getJSONObject(position);
            String fromToDate=jsonObject.getString("from_date")+" to "+jsonObject.getString("to_date");


            holder.taskCommnet.setText("Comments : "+jsonObject.getString("comments"));
            holder.taskDescription.setText(jsonObject.getString("description"));
            holder.taskDate.setText("Date : "+fromToDate);
            holder.txtTaskName.setText(jsonObject.getString("title"));
            holder.taskPriority.setText("Priority : "+jsonObject.getString("priority"));
            holder.taskstatus.setText("Status : "+jsonObject.getString("status"));


//        {"result":1,"msg":"My Orders","data":[{"id":"3","order_by":"16","order_id":"ORD-100003","supplier_id":"1","product_ids":"1","order_date":"2022-03-08","quantaties":"100","amount":"100","status":"new","image":"","comments":"this is order conment","add_date":null,"upd_date":null}]}
            holder.edit.setTag(jsonObject);
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    try {
                        showTaskDialog(new JSONObject(v.getTag() + ""));
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            showImage(jsonObject.getString("image"),holder.taskImage);

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



    private void showTaskDialog(JSONObject data) {
        try {

            final Dialog dialog = new Dialog(ira1);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.layout_task);
            dialog.show();
            String taskID = data.getString("id");
            EditText editText =dialog.findViewById(R.id.edmytask);


            dialog.findViewById(R.id.txtNext).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(editText.getText().toString().length()==0)
                    {
                        ira1.showErrorDialog("Enter task comment");
                        return;
                    }

                    LinkedHashMap<String, String> m = new LinkedHashMap<>();
                    m.put("task_id", taskID);
                    m.put("comments", editText.getText().toString());


                    Map<String, String> headerMap = new HashMap<>();

                    new ServerHandler().sendToServer(ira1, AppConstants.apiUlr + "updatetaskcomment", m, 0, headerMap, 20000, R.layout.loader_dialog, new CallBack() {
                        @Override
                        public void getRespone(String dta, ArrayList<Object> respons) {
                            try {

                                JSONObject obj = new JSONObject(dta);
                                System.out.println("task data=="+obj);
                                if (obj.getInt("result") > 0)
                                {
                                    dialog.dismiss();
                                    ira1.showErrorDialog(obj.getString("msg"));
                                } else {
                                    ira1.showErrorDialog(obj.getString("errors"));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
