package com.markrap.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.markrap.R;
import com.markrap.adapter.TaskListAdapter;
import com.markrap.communication.CallBack;
import com.markrap.communication.ServerHandler;
import com.markrap.dashboard.MainActivity;
import com.markrap.utility.AppConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class TaskListFragments extends Fragment {
    public static final String TAG = "HomeFragments";
    private MainActivity mainActivity;

    View rootView;
    public static Fragment newInstance(Context context) {
        return Fragment.instantiate(context,
                TaskListFragments.class.getName());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tasklist_fragment, container, false);
        mainActivity = (MainActivity) getActivity();

        getMyTask();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("@@MainHome_ONResume---", "onResume---");
    }


    private void showTask(JSONArray dataAr) {
        RecyclerView recyclerlistview = rootView.findViewById(R.id.recyclerlistview);
        TaskListAdapter mAdapter = new TaskListAdapter(dataAr, (MainActivity) getActivity());
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerlistview.setLayoutManager(horizontalLayoutManagaer);
        recyclerlistview.setItemAnimator(new DefaultItemAnimator());
        recyclerlistview.setAdapter(mAdapter);
    }


    private void getMyTask() {
        LinkedHashMap<String, String> m = new LinkedHashMap<>();
        Map<String, String> headerMap = new HashMap<>();
        //+mainActivity.getLoginData("id")
        new ServerHandler().sendToServer(getActivity(), AppConstants.apiUlr + "mytask/"+mainActivity.getLoginData("id"), m, 0, headerMap, 20000, R.layout.loader_dialog, new CallBack() {
            @Override
            public void getRespone(String dta, ArrayList<Object> respons) {
                try {
                    System.out.println("mytask====" + AppConstants.apiUlr + "mytask/"+mainActivity.getLoginData("id"));
                    System.out.println("mytask====" + dta);
                    JSONObject obj = new JSONObject(dta);
                    if (obj.getInt("result") > 0) {

                        showTask(obj.getJSONArray("data"));

                    } else {
                        ((MainActivity) getActivity()).showErrorDialog(obj.getString("msg"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }


}

//data":[{"id":"4","assign_to":"16","title":"My Tak","description":"This is my description","priority":"high",
// "from_date":"2022-03-07","to_date":"2022-03-31","status":"assign","image":"","comments":"emplyoyee comment","add_date":null,"upd_date":null},{"id":"5","assign_to":"16","title":"Complete your job","description":"Go to all store and complete your sales target before 20 April. ","priority":"high","from_date":"2022-03-07","to_date":"2022-04-15","status":"assign","image":"https:\/\/neptunesolution.in\/marketing\/uploads\/task\/278956776.jpg","comments":"Hello this is emplee comment","add_date":null,"upd_date":null},{"id":"6","assign_to":"16","title":"Complete your job","description":"Go to all store and complete your sales target before 20 April. ","priority":"high","from_date":"2022-03-07","to_date":"2022-04-15","status":"assign","image":"https:\/\/neptunesolution.in\/marketing\/uploads\/task\/462260948","comments":"Hello this is emplee comment","add_date":null,"upd_date":null},{"id":"7","assign_to":"16","title":"Complete your job","description":"Go to all store and complete your sales target before 20 April. ","priority":"high","from_date":"2022-03-07","to_date":"2022-04-15","status":"assign","image":"https:\/\/neptunesolution.in\/marketing\/uploads\/task\/1713146585","comments":"Hello this is emplee comment","add_date":null,"upd_date":null},{"id":"8","assign_to":"16","title":"Complete your job","description":"Go to all store and complete your sales target before 20 April. ","priority":"high","from_date":"2022-03-07","to_date":"2022-04-15","status":"assign","image":"https:\/\/neptunesolution.in\/marketing\/uploads\/task\/2138168110","comments":"Hello this is emplee comment","add_date":null,"upd_date":null},{"id":"9","assign_to":"16","title":"Complete your job","description":"Go to all store and complete your sales target before 20 April. ","priority":"high","from_date":"2022-03-07","to_date":"2022-04-15","status":"assign","image":"https:\/\/neptunesolution.in\/marketing\/uploads\/task\/938928874","comments":"Hello this is emplee comment","add_date":null,"upd_date":null},{"id":"10","assign_to":"16","title":"Complete your job","description":"Go to all store and complete your sales target before 20 April. ","priority":"high","from_date":"2022-03-07","to_date":"2022-04-15","status":"assign","image":"https:\/\/neptunesolution.in\/marketing\/uploads\/task\/1531493246","comments":"Hello this is emplee comment","add_date":null,"upd_date":null},{"id":"11","assign_to":"16","title":"Complete your job","description":"Go to all store and complete your sales target before 20 April. ","priority":"high","from_date":"2022-03-07","to_date":"2022-04-15","status":"assign","image":"https:\/\/neptunesolution.in\/marketing\/uploads\/task\/712262924","comments":"Hello this is emplee comment","add_date":null,"upd_date":null}]}