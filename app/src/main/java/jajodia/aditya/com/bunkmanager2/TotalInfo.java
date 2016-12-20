package jajodia.aditya.com.bunkmanager2;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.w3c.dom.Text;

public class TotalInfo extends FragmentActivity {

    private static final String TAG = "TotalInfo";
    RelativeLayout li;
    static String subjects[];
    WindowManager windowManager ;
    public boolean exit = false;
    @Override
    public void onBackPressed() {
       // super.onBackPressed();

        if(exit){
            finish();
        }else{

            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
            exit = true;

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            },3*1000);

        }
    }

    @Override
    protected void onResume() {
        invalidateOptionsMenu();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        Log.d(TAG,"onCreateOptionsMenu"+" " +"came in menu");
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.main_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        return super.onOptionsItemSelected(item);
    }

    //   MainActivity ma = new MainActivity();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_total_info);

       //     ActionBar actionBar = getSupportActionBar();




        Cursor cursor = DatabaseOpenHelperTwo.readData(this,2);
        if(cursor.getCount()<=0){
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
        }else {

            windowManager = getWindowManager();
            generateAdd();


            SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.MY_FILE, 0);
            String s = sharedPreferences.getString("Subjects", MainActivity.getSubjects());
            final String subject = s; // subjects as a String separated by $
            int size = sharedPreferences.getInt("Size", MainActivity.size);
            Log.d(TAG, s + " " + "12" + " " + size);
            subjects = new String[size];
            int l = subject.length();
            int j = 0;
            int k = 0;
            // Log.d(TAG,subject);
            for (int i = 0; i < l; i++) {
                Log.d(TAG, "SEE" + " " + i);
                if (subject.charAt(i) == '$') {
                    subjects[k++] = subject.substring(j, i);
                    j = i + 1;
                    Log.d(TAG, "string came" + " " + subjects[k - 1]);

                }
            }

            li = (RelativeLayout) findViewById(R.id.activity_total_info);
            RelativeLayout.LayoutParams params[] = new RelativeLayout.LayoutParams[size];
            final Button btn[] = new Button[size];
            for (int i = 0; i < size; i++) {
                //   Log.d(TAG,"sss"+" " +i);
                btn[i] = new Button(this);
                btn[i].setId((i + 1) * 4);
                final int w = getWidth();
                Log.d(TAG, "width : " + w);
                params[i] = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                if (i % 4 == 0 && i > 0) {
                    //    Log.d(TAG,"came "+" "+i);
                    params[i].addRule(RelativeLayout.BELOW, btn[i - 4].getId());

                } else if (i > 0 && i > 3) {
                    params[i].addRule(RelativeLayout.RIGHT_OF, btn[i - 1].getId());
                    params[i].addRule(RelativeLayout.BELOW, btn[i - 4].getId());
                } else if (i > 0) {
                    params[i].addRule(RelativeLayout.RIGHT_OF, btn[i - 1].getId());
                } else {
                    params[i].addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                }
                if (subjects[i].length() <= 5)
                    btn[i].setText(subjects[i]);
                else {
                    btn[i].setText(subjects[i].substring(0, 3) + "..");
                }

                btn[i].setTextSize(12);
                boolean b = percentattendance(subjects[i]);
                if (b)
                    btn[i].setTextColor(Color.parseColor("#FF5722"));
                btn[i].setLayoutParams(params[i]);
                li.addView(btn[i], params[i]);
                final int finalI = i;
                btn[i].setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        Log.d(TAG, "action : " + event.getAction());
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_UP:
                                Log.d(TAG, "came in case 2");
                                btn[finalI].setElevation(0);
                                btn[finalI].setWidth(w / 4);
                                if(subjects[finalI].length()<=5)
                                    btn[finalI].setText(subjects[finalI]);
                                else
                                    btn[finalI].setText(subjects[finalI].substring(0, 3) + "..");

                                return true;
                            case MotionEvent.ACTION_MOVE:
                                Log.d(TAG, "came in case 1");
                                btn[finalI].setElevation(20);
                                btn[finalI].setWidth(RelativeLayout.LayoutParams.WRAP_CONTENT);
                                btn[finalI].setText(subjects[finalI]);
                                return true;
                            default:
                                btn[finalI].setPressed(false);
                                Cursor c = DatabaseOpenHelper.readData(TotalInfo.this, subjects[finalI]);

                                c.moveToFirst();

                                FragmentManager fragmentManager = getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                                if (c.getCount() <= 0) {
                                    Log.d(TAG, "came in 1");
                                    ButtonFragment fragment = new ButtonFragment();
                                    fragment.setSubject(subjects[finalI]);
                                    //   Log.d(TAG,"button called"+" "+finalI);
                                    fragmentTransaction.replace(R.id.frame_layout, fragment, null);
                                    fragmentTransaction.commit();
                                } else {
                                    boolean b = percentattendance(subjects[finalI]);
                                    if (b)
                                        btn[finalI].setTextColor(Color.parseColor("#FF5722"));

                                    Log.d(TAG, "came in 2");
                                    ButtonFragmentTwo fragmentTwo = new ButtonFragmentTwo();
                                    fragmentTwo.setSubject(subjects[finalI]);
                                    fragmentTransaction.replace(R.id.frame_layout, fragmentTwo, null);
                                    fragmentTransaction.commit();
                                }
                                c.close();
                                return true;
                        }

                    }
                });
            }


            // adding fragments for each buttons
            final FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame_layout);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.BELOW, btn[size - 1].getId());
            frameLayout.setLayoutParams(layoutParams);
            if (frameLayout.getParent() != null) {
                ((ViewGroup) frameLayout.getParent()).removeView(frameLayout);
            }
            li.addView(frameLayout);

           // DialogFragment dialogFragment = DialogFragmentOne.newInstance();
            //dialogFragment.show(getSupportFragmentManager(),"check");
        }
    }

public static String[] getSubjects(){
    return subjects;
}


    public void generateAdd(){

        MobileAds.initialize(getApplicationContext(),"ca-app-pub-4601836836916539~2310735402");

        AdView adView = (AdView)findViewById(R.id.ad_view);
        AdRequest adRequest;
        adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    public int getWidth(){
        Point size = new Point();
        windowManager.getDefaultDisplay().getSize(size);
        return size.x;
    }

    public boolean percentattendance(String sub){

        Cursor cursor = DatabaseOpenHelper.readData(this,sub);
        cursor.moveToFirst();

        if(cursor.getCount()<=0)
            return false;
        else{

            int t = cursor.getInt(2);
            int p = cursor.getInt(3);

            float att = ((float)p/t)*100;

            if(att>=75.0)
                return false;
            else
                return true;
        }

    }


}


