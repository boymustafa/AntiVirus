package com.gamesterz.antivirus;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AppTaskActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {


    List<AppTaskModel> list = new ArrayList<>();
    String[] requestedPermissions;
    private static final String TAG = "AppTaskActivity";
    ListAdapter adapter;
    ListView listView;
    MultiSwipeRefreshLayout swipeLayout;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_task);
        initViews();
//        initList();


    }

    private void initList(){
        final PackageManager pm = getPackageManager();
        final List<ApplicationInfo> installedApps = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo app : installedApps){
            AppTaskModel model = new AppTaskModel();

            //permissions
            StringBuffer permissions = new StringBuffer();

            try{
                PackageInfo packageInfo = pm.getPackageInfo(app.packageName,PackageManager.GET_PERMISSIONS);
                requestedPermissions = packageInfo.requestedPermissions;

// Do not add System Packages
                if ((packageInfo.requestedPermissions == null || packageInfo.packageName.equals("android")) ||
                        (packageInfo.applicationInfo != null && (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ||
                        (packageInfo.packageName.equals("com.gamesterz.antivirus"))
                        )
                    continue;

                if(requestedPermissions!=null){

                    model.setPkgName(app.packageName);
                    model.setAppName(app.loadLabel(getPackageManager()).toString());
//                    Log.d(TAG, "application name: " + app.loadLabel(getPackageManager()).toString() + " ( " + requestedPermissions.length + " )");
                    model.setPermissionsTotal(requestedPermissions.length);
                }
            }catch (PackageManager.NameNotFoundException e){
                e.printStackTrace();
            }
            list.add(model);
            adapter = new ListAdapter(this,list);
            adapter.notifyDataSetChanged();
            // stopping swipe refresh
            swipeLayout.setRefreshing(false);

        }
    }

    private void initViews(){
        listView = (ListView) findViewById(android.R.id.list);
        adapter = new ListAdapter(this,list);
        listView.setAdapter(adapter);
        swipeLayout = (MultiSwipeRefreshLayout) findViewById(R.id.swiperefresh);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setSwipeableChildren(android.R.id.list);
        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        swipeLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeLayout.setRefreshing(true);

                                        initList();
                                    }
                                }
        );

        fab = (FloatingActionButton) findViewById(R.id.fabAppList);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(AppTaskActivity.this);
                builder1.setTitle(R.string.quick_help_title);
                builder1.setMessage(R.string.quick_help_msg);
                builder1.setCancelable(false);

                builder1.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AppTaskActivity.this,AppDetailActivity.class);
                intent.putExtra("appName",list.get(position).getAppName());
                intent.putExtra("pkgName",list.get(position).getPkgName());
                intent.putExtra("totalPermissions",list.get(position).getPermissionsTotal());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRefresh() {
        list.clear();
        initList();
//        listView.setAdapter(adapter);
    }


    public class ListAdapter extends ArrayAdapter<AppTaskModel>{
        private List<AppTaskModel> list = new ArrayList<>();
        private Context context;

        public ListAdapter(Context context, List<AppTaskModel> list){
            super(context,R.layout.item_app_task,list);

            this.context = context;
            this.list = list;

            Collections.sort(list, new Comparator<AppTaskModel>() {
                @Override
                public int compare(AppTaskModel l1, AppTaskModel l2) {
                    return l1.getAppName().compareTo(l2.getAppName());
                }
            });
        }


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public AppTaskModel getItem(int position) {
            return list.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;
            AppTaskHolder holder;

            if(v==null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.item_app_task,parent,false);
                holder = new AppTaskHolder(v);
                v.setTag(holder);
            } else {
                holder = (AppTaskHolder) v.getTag();
            }


            AppTaskModel model = list.get(position);


            try {
                holder.ivIcon.setImageDrawable(context.getPackageManager().getApplicationIcon(model.pkgName));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            holder.txtApp.setText(model.getAppName()+" ("+model.getPermissionsTotal()+")");

            return v;
        }

        class AppTaskHolder{
            TextView txtApp;
            ImageView ivIcon;

            public AppTaskHolder(View v){
                txtApp = (TextView) v.findViewById(R.id.appName);
                ivIcon = (ImageView) v.findViewById(R.id.appIcon);
            }
        }


    }
}
