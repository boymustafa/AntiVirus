package com.gamesterz.antivirus;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gamesterz.antivirus.Models.InstalledAppModel;
import com.gamesterz.antivirus.Utilities.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Boy Mustafa on 13/06/16.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.MyViewHolder> {
    private List<InstalledAppModel> appList = new ArrayList<>();
    private List<String> appName = new ArrayList<>();
    private Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView txtSize, txtappName;
        public ImageView ivIcon;

        /*
        txtApp = (TextView) v.findViewById(R.id.appTitle);
                txtAppCacheSize = (TextView) v.findViewById(R.id.cacheSize);
                ivIcon = (ImageView) v.findViewById(R.id.iconApp);
         */

        public MyViewHolder(View itemView) {
            super(itemView);
            txtSize = (TextView) itemView.findViewById(R.id.cacheSize);
            txtappName = (TextView) itemView.findViewById(R.id.appTitle);
            ivIcon = (ImageView) itemView.findViewById(R.id.iconApp);
        }
    }

    public RVAdapter(Context context,List<InstalledAppModel> appList, List<String> appName){
        this.appList = appList;
        this.appName = appName;
        this.context = context;

        Collections.sort(appName, new Comparator<String>() {
            @Override
            public int compare(String l1, String l2) {
                return l1.compareTo(l2);
            }
        });
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        InstalledAppModel model = appList.get(position);
        holder.txtappName.setText(appName.get(position));
//        holder.txtAppCacheSize.setText(String.valueOf(model.getAppSize()));
        holder.txtSize.setText(Utils.humanReadableByteCount(model.getAppSize(),true));
        try {
            holder.ivIcon.setImageDrawable(context.getPackageManager().getApplicationIcon(model.getPkgName()));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }
}
