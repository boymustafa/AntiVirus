package com.gamesterz.antivirus;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gamesterz.antivirus.Models.AppDetails;
import com.gamesterz.antivirus.Models.InstalledAppModel;
import com.gamesterz.antivirus.Utilities.DividerItemDecoration;
import com.gamesterz.antivirus.Utilities.Utils;
import com.twotoasters.jazzylistview.JazzyListView;
import com.twotoasters.jazzylistview.effects.SlideInEffect;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Semaphore;

import at.grabner.circleprogress.AnimationState;
import at.grabner.circleprogress.AnimationStateChangedListener;
import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.TextMode;

public class ScanningActivity extends AppCompatActivity {

    private final static String TAG = "ScanningActivity";

    CircleProgressView circleProgressView;
    public static final int FETCH_PACKAGE_SIZE_COMPLETED = 100;
    public static final int ALL_PACKAGE_SIZE_COMPLETED = 200;
    long packageSize = 0, size = 0, dataSize = 0, data = 0;
    AppDetails cAppDetails;
    public ArrayList<AppDetails.PackageInfoStruct> res;
    final Semaphore packageSizeSemaphore = new Semaphore(1,true);
    ArrayList<InstalledAppModel> listApp = new ArrayList<>();
    List<String> appName = new ArrayList<>();
    Toolbar toolbar;
    Animation animFadeIn,animFadeOut;
    JazzyListView listView;
    FloatingActionButton fab;
    RecyclerView rv;
    int totalAppsInstalled;
    ProgressDialog pdScan;
    private Handler updateBarHandler;
    int progressStatus = 0;
    AppListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);

        setViews();
//        updateBarHandler = new Handler();

//        circleProgressView.spin();

        pdScan = new ProgressDialog(ScanningActivity.this);
        pdScan.setTitle("Loading...");
        pdScan.setCancelable(false);
        pdScan.setMessage("Please Wait");
        pdScan.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(500);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getPackageSize();
                            }
                        });

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                getPackageSize();
            }
        }).start();

    }

    private void getPackageSize() {
        cAppDetails = new AppDetails(this);
        res = cAppDetails.getPackages();
        if (res == null)
            return;
        totalAppsInstalled = res.size();
        for (int m = 0; m < res.size(); m++) {
            try {
                packageSizeSemaphore.acquire();
                Log.d("List smua app","ke  "+m+" = "+res.get(m).pname);
                appName.add(res.get(m).appname);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            PackageManager pm = getPackageManager();
            Method getPackageSizeInfo;
            try {
                getPackageSizeInfo = pm.getClass().getMethod(
                        "getPackageSizeInfo", String.class,
                        IPackageStatsObserver.class);
                getPackageSizeInfo.invoke(pm, res.get(m).pname,
                        new cachePackState());
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        }
        handle.sendEmptyMessage(ALL_PACKAGE_SIZE_COMPLETED);
        Log.v("Total Cache Size", " " + packageSize);
        Log.v("getPackageSize", " " + "ini dipanggil bro");

    }

    private void setViews(){
//        circleProgressView = (CircleProgressView) findViewById(R.id.circleView);
//        circleProgressView.setOnProgressChangedListener(new CircleProgressView.OnProgressChangedListener() {
//            @Override
//            public void onProgressChanged(float value) {
//                Log.d(TAG,"Progress Changed: "+value);
//            }
//        });
//
//
//        circleProgressView.setShowTextWhileSpinning(true);
////        circleProgressView.setText("Scanning...");
//        circleProgressView.setOnAnimationStateChangedListener(new AnimationStateChangedListener() {
//            @Override
//            public void onAnimationStateChanged(AnimationState _animationState) {
//                switch (_animationState){
//                    case IDLE:
//                    case ANIMATING:
//                    case START_ANIMATING_AFTER_SPINNING:
//                        break;
//                    case SPINNING:
////                        circleProgressView.setTextMode(TextMode.TEXT);
//                        circleProgressView.setTextMode(TextMode.PERCENT);
////                        circleProgressView.setUnitVisible(false);
//                        circleProgressView.setUnitVisible(true);
//                    case END_SPINNING:
//                    case END_SPINNING_START_ANIMATING:
//                        break;
//                }
//            }
//        });
//
//        circleProgressView.setUnitVisible(false);
        animFadeIn = AnimationUtils.loadAnimation(this,R.anim.fade_in);
        animFadeOut = AnimationUtils.loadAnimation(this,R.anim.fade_out);

        animFadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
//                circleProgressView.setVisibility(View.GONE);
            }
        });

        listView = (JazzyListView) findViewById(R.id.appList);
//        rv = (RecyclerView) findViewById(R.id.recyvler_View);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//        rv.setLayoutManager(mLayoutManager);
//        rv.setItemAnimator(new DefaultItemAnimator());
        fab = (FloatingActionButton) findViewById(R.id.FAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ScanningActivity.this);

                builder1.setTitle(R.string.clear_cache_title);
                builder1.setMessage(R.string.clear_cache_desc);
                builder1.setCancelable(false);

                builder1.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();

                                new BackgroundTask(ScanningActivity.this).execute();
                            }
                        });

                builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
    }

    private Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FETCH_PACKAGE_SIZE_COMPLETED:

                    break;
                case ALL_PACKAGE_SIZE_COMPLETED:
                    ((TextView)findViewById(R.id.junkSize)).setText(Utils.humanReadableByteCount(packageSize,true));
                    Log.d("Total Cache coy"," adalah "+ Utils.humanReadableByteCount(packageSize,true));
//                    RVAdapter adapter = new RVAdapter(getApplicationContext(),listApp,appName);
//                    rv.addItemDecoration(new DividerItemDecoration(getApplicationContext(),LinearLayoutManager.VERTICAL));
//                    rv.setAdapter(adapter);
                    adapter = new AppListAdapter(ScanningActivity.this,listApp,appName);
                    listView.setAdapter(adapter);
                    listView.setTransitionEffect(new SlideInEffect());
//                    circleProgressView.stopSpinning();
//                    circleProgressView.startAnimation(animFadeOut);
                    ((LinearLayout)findViewById(R.id.resultLayout)).setVisibility(View.VISIBLE);
                    ((LinearLayout)findViewById(R.id.resultLayout)).startAnimation(animFadeIn);
                    if(pdScan.isShowing())
                        pdScan.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    private class cachePackState extends IPackageStatsObserver.Stub {
        @Override
        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
            InstalledAppModel appModel = new InstalledAppModel();
            appModel.setPkgName(pStats.packageName);
            appModel.setAppSize(pStats.cacheSize);
            listApp.add(appModel);

            Log.d("Package Size", pStats.packageName + " " );
            Log.i("Cache Size", pStats.cacheSize + "");
            Log.w("Data Size", pStats.dataSize + "");
            packageSize = packageSize + pStats.cacheSize;
            Log.v("Total Cache Size", " " + packageSize );
            packageSizeSemaphore.release();
            handle.sendEmptyMessage(FETCH_PACKAGE_SIZE_COMPLETED);
        }
    }

    private class BackgroundTask extends AsyncTask<Void, Integer, Void> {
        private ProgressDialog pd;
        boolean running;

        public BackgroundTask(ScanningActivity activity) {
            pd = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            running = true;
            pd.setTitle(getResources().getString(R.string.clear_cache_proccessing));
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.setProgress(0);
            pd.setMax(totalAppsInstalled);
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            if (pd.isShowing()) {

//                int currentapiVersion = android.os.Build.VERSION.SDK_INT;
//                if(currentapiVersion!= Build.VERSION_CODES.M)
                    deleteCache();
//                else
//                    clearApplicationData();
////                    ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE))
////                            .clearApplicationUserData();
                pd.dismiss();
                startActivity(new Intent(ScanningActivity.this,CongratsActivity.class));
                finish();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            int i = totalAppsInstalled;
            while(running){
                try {
                    Thread.sleep(1000);
                    pd.incrementProgressBy(40);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(i-- == 0 || pd.getProgress() == pd.getMax()){
                    running = false;
                }

                publishProgress(i);

            }



            return null;
        }
    }

    private void deleteCache() {
        PackageManager pm = getPackageManager();
        //Get all methods on the packageManager
        Method[] methods = pm.getClass().getDeclaredMethods();
        for (Method m : methods) {
            if (m.getName().equals("freeStorageAndNotify")) {
                //Found the method I want to use
                try {
                    long desiredFreeStorage = Long.MAX_VALUE;
//                    m.invoke(pm, desiredFreeStorage, null);
                    m.invoke(pm, desiredFreeStorage, null);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }


    public void clearApplicationData()
    {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));Log.i("TAG", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
                }
            }
        }
    }

    public static boolean deleteDir(File file)
    {
        boolean deletedAll = true;

        if (file != null) {

            if (file.isDirectory()) {

                String[] children = file.list();

                for (int i = 0; i < children.length; i++) {

                    deletedAll = deleteDir(new File(file, children[i])) && deletedAll;

                }

            } else {

                deletedAll = file.delete();

            }

        }

        return deletedAll;

    }

    public class AppListAdapter extends ArrayAdapter<InstalledAppModel>{
        private List<InstalledAppModel> appList = new ArrayList<>();
        private Context context;
        private List<String> appName = new ArrayList<>();

        public AppListAdapter(final Context context, final List<InstalledAppModel> appList, List<String> appName){
            super(context,R.layout.item_list,appList);

            this.context = context;
            this.appList = appList;
            this.appName = appName;

        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getCount() {
            return appList.size();
        }

        @Override
        public InstalledAppModel getItem(int position) {
            return listApp.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            AppHolder holder;

            if(view == null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.item_list,parent,false);
                holder = new AppHolder(view);
                view.setTag(holder);
            } else {
                holder = (AppHolder) view.getTag();
            }

            InstalledAppModel model = appList.get(position);

//            holder.txtApp.setText(model.getPkgName());
            holder.txtApp.setText(appName.get(position));
//        holder.txtAppCacheSize.setText(String.valueOf(model.getAppSize()));
            holder.txtAppCacheSize.setText(Utils.humanReadableByteCount(model.getAppSize(),true));
            try {
                holder.ivIcon.setImageDrawable(context.getPackageManager().getApplicationIcon(model.getPkgName()));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            return view;
        }

        private class AppHolder{
            TextView txtApp,txtAppCacheSize;
            ImageView ivIcon;

            private AppHolder(View v){
                txtApp = (TextView) v.findViewById(R.id.appTitle);
                txtAppCacheSize = (TextView) v.findViewById(R.id.cacheSize);
                ivIcon = (ImageView) v.findViewById(R.id.iconApp);
            }
        }
    }
}
