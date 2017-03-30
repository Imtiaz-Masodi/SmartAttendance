package com.sam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.sam.R.id.ll;

public class AttendanceActivity extends AppCompatActivity implements View.OnClickListener {

    String branch, year, semister, section, periods[];
    FirebaseAuth auth;
    DatabaseReference mDatabase;
    Menu menu;
    int todayWeek;
    int[] sundaysDates = {0, 0, 0, 0, 0};
    String[] rollno, names, sunday = {"S", "U", "N", "D", "A", "Y"};
    int today,month, classAttendId = 6001;
    int days = 31, totalStudents = 70, studentJoiningYear, branchNo;
    int cbHeight, totalPeriods;
    LinearLayout main, llRoll, llDate;
    HorizontalScrollView svDate, hsv;
    ScrollView svMain;
    FloatingActionButton uploadAttendance;
    ProgressDialog mDialog;

    @Override
    protected void onStart() {
        super.onStart();
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle bundle = getIntent().getExtras();
        branch = bundle.getString("branch");
        year = bundle.getString("year");
        semister = bundle.getString("semister");
        section = bundle.getString("section");
        periods = bundle.getStringArray("periods");
        //Toast.makeText(this, "Branch : "+branch, Toast.LENGTH_SHORT).show();

        for (int i = 0; i < 7; i++) {
            if (periods[i].equals("1")) {
                periods[totalPeriods] = (i + 1) + "";
                totalPeriods++;
            }
        }

        mDialog = new ProgressDialog(AttendanceActivity.this);
        mDialog.setMessage("Loading Contents");

        final int sundayCounter[] = {0};
        sundaysDates = new int[5]; //To store sundays date of current month.
        today = 2;
        studentJoiningYear = 14;
        branchNo = 733;
        names = new String[totalStudents];
        rollno = new String[totalStudents];

        String weekDay;
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        weekDay = dayFormat.format(calendar.getTime());
        String[] mDate = weekDay.split("/");
        today = Integer.parseInt(mDate[0]); //Todays date
        month= Integer.parseInt(mDate[1]);
        days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); //Total days in current month
        int week = todayWeek = calendar.get(Calendar.DAY_OF_WEEK);//Todays's Day i.e Sunday,Monday,... etc


        for (int x = 0; x < totalStudents; x++) {
            //Load Student Names
            names[x] = "Student Name " + (x + 1);
        }
        for (int x = 0; x < totalStudents; x++) {
            //Load Rollno's
            if (x < 60)
                rollno[x] = "1604-" + studentJoiningYear + "-" + branchNo + "-" + (x + 1);
            else
                rollno[x] = "1604-" + studentJoiningYear + "-" + branchNo + "-" + (300 + (x - 59));
        }

        if (Integer.parseInt(mDate[0]) != 1) {
            int i = Integer.parseInt(mDate[0]);
            while (i > 1) {
                week--;
                if (week < 1) {
                    week = 7;
                }
                i--;
            }
        }
        int d = 1, tweek = week;
        while (tweek < 8 && tweek > 1) {
            d++;
            tweek++;
        }
        for (int i = 0; d <= days; i++) {
            sundaysDates[i] = d;
            d += 7;
        }

        svDate = (HorizontalScrollView) findViewById(R.id.horizontalScrollView2);
        hsv = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
        hsv.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                svDate.scrollTo(scrollX, 0);
            }
        });

        CheckBox tempCB = new CheckBox(this);
        tempCB.setVisibility(View.GONE);
        tempCB.setChecked(true);
        cbHeight = tempCB.getMinHeight();

        main = (LinearLayout) findViewById(ll);
        llRoll = (LinearLayout) findViewById(R.id.llRoll);
        llDate = (LinearLayout) findViewById(R.id.llDate);
        uploadAttendance = (FloatingActionButton) findViewById(R.id.fabUploadAttendance);
        uploadAttendance.setOnClickListener(this);

        LinearLayout roll = new LinearLayout(this);
        roll.setOrientation(LinearLayout.VERTICAL);
        for (int i = 0; i < totalStudents; i++) {
            TextView sno = new TextView(this);
            sno.setGravity(Gravity.CENTER_HORIZONTAL);
            sno.setTextColor(Color.WHITE);
            sno.setMinHeight(cbHeight);
            sno.setMaxHeight(cbHeight);
            sno.setMaxWidth(195);
            sno.setMinWidth(195);
            sno.setTextColor(Color.rgb(79, 130, 40));
            sno.setPadding(5, 8, 5, 8);
            sno.setText(rollno[i]);
            roll.addView(sno);
        }

        llRoll.addView(roll);
        int rbCounter = 0;
        for (int i = 0; i <= days; i++) {
            boolean cbTobeEnable = true;
            if (i == today)
                cbTobeEnable = true;
            else
                cbTobeEnable = false;
            if (i == 0) {
                //Adding Students Name
                LinearLayout nCol = new LinearLayout(this);
                nCol.setId(10000);
                nCol.setOrientation(LinearLayout.VERTICAL);
                nCol.setGravity(Gravity.CENTER);
                main.addView(nCol);

                TextView name = new TextView(this);
                name.setMaxWidth(150);
                name.setMinWidth(150);
                name.setTextColor(Color.WHITE);
                name.setGravity(Gravity.CENTER);
                name.setSingleLine(true);
                name.setText("Names");
                llDate.addView(name);

                for (int j = 1; j <= totalStudents; j++) {
                    TextView sname = new TextView(this);
                    sname.setMaxWidth(150);
                    sname.setMinWidth(150);
                    sname.setTextColor(Color.GREEN);
                    sname.setPadding(5, 8, 5, 8);
                    sname.setSingleLine(true);
                    sname.setGravity(Gravity.CENTER);
                    sname.setTextColor(Color.rgb(79, 130, 40));
                    sname.setText(names[j - 1]);
                    nCol.addView(sname);
                }
                name.setMaxWidth(150);
            } else {
                LinearLayout col = new LinearLayout(this);
                col.setId(10000 + i);
                col.setOrientation(LinearLayout.VERTICAL);
                col.setGravity(Gravity.CENTER);
                main.addView(col);

                TextView date = new TextView(this);
                date.setMinWidth(48);
                date.setTextColor(Color.WHITE);
                date.setGravity(Gravity.CENTER);
                date.setText(i + "");
                llDate.addView(date);

                if (week != 1) {
                    //Working Days...
                    for (int j = 1; j <= totalStudents; j++) {
                        final CheckBox cb = new CheckBox(this);
                        rbCounter++;
                        cb.setEnabled(cbTobeEnable);
                        cb.setId(rbCounter);

                        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                calculateStudentAttendance(buttonView.getId());
                                calculateClassAttendance(buttonView.getId());
                            }
                        });
                        col.addView(cb);

                    }
                    TextView tv = new TextView(this);
                    tv.setMinWidth(48);
                    tv.setTextSize(20);
                    tv.setTextColor(Color.rgb(79, 130, 40));
                    tv.setId(classAttendId++);
                    tv.setGravity(Gravity.CENTER);
                    tv.setText("0");
                    col.addView(tv);
                } else {
                    //Sunday...
                    for (int j = 0; j < 6; j++) {
                        //int[] aa = {401, 402, 403, 404, 405};
                        TextView tv = new TextView(this);
                        tv.setMinWidth(48);
                        tv.setTextSize(20);
                        tv.setPadding(0, 20, 0, 0);
                        tv.setTextColor(Color.rgb(206, 7, 11));
                        tv.setGravity(Gravity.CENTER);
                        tv.setText(sunday[j]);
                        col.addView(tv);
                    }
                    sundayCounter[0]++;
                }
                week++;
                if (week > 7) {
                    week = 1;
                }
            }
        }

        LinearLayout total = new LinearLayout(this);
        total.setGravity(Gravity.CENTER);
        total.setOrientation(LinearLayout.VERTICAL);
        TextView tvTotal = new TextView(this);
        tvTotal.setText("TOTAL");
        tvTotal.setTextColor(Color.WHITE);
        tvTotal.setPadding(5, 8, 5, 8);
        llDate.addView(tvTotal);
        main.addView(total);

        for (int i = 1; i <= totalStudents; i++) {
            int count = 0;
            TextView tv = new TextView(this);
            tv.setId(5000 + i);
            tv.setTextSize(20);
            tv.setTextColor(Color.rgb(79, 130, 40));
            tv.setGravity(Gravity.CENTER);
            tv.setMinWidth(50);
            tv.setMaxHeight(cbHeight);
            tv.setMinHeight(cbHeight);
            tv.setMinWidth(80);
            tv.setText("" + count);
            total.addView(tv);
        }

        uploadAttendance.setFocusable(true);
        mDialog.dismiss();
    }

    private void calculateClassAttendance(int id) {
        int col = getColumnIndex(id);
        int classAttendance = 0;
        for (int i = 1; i <= totalStudents; i++) {
            CheckBox scb = (CheckBox) findViewById((totalStudents * col) + i);
            if (scb.isChecked()) {
                classAttendance++;
            }
        }
        TextView classAttendanceId = (TextView) findViewById(6001 + col);
        classAttendanceId.setText("" + classAttendance);
    }

    private int getColumnIndex(int id) {
        if (id > totalStudents) {
            float fcol = id / (float) totalStudents;
            int icol = (int) fcol;
            if (fcol == icol) {
                icol--;
            }
            return icol;
        }
        return 0;
    }

    public int getRollNo(int id) {
        if (id > totalStudents) {
            float rtemp = id / (float) totalStudents;
            int temp = (int) rtemp;
            if (temp == rtemp)
                temp--;
            int sid = id - (temp * totalStudents);
            return sid;
        }
        return id;
    }

    public void calculateStudentAttendance(int rno) {
        int rollno = getRollNo(rno), attendance = 0;
        for (int i = 1, j = 1; j <= days; j++) {
            if (j != sundaysDates[0] && j != sundaysDates[1] && j != sundaysDates[2] && j != sundaysDates[3] && j != sundaysDates[4]) {
                CheckBox cb = (CheckBox) findViewById((totalStudents * (i - 1)) + rollno);
                if (cb.isChecked())
                    attendance++;
                i++;
            }
        }
        TextView t = (TextView) findViewById(5000 + rollno);
        t.setText(attendance + "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.menu = menu;
        getMenuInflater().inflate(R.menu.attendance_menu, menu);
        if (todayWeek == 1) {
            menu.findItem(R.id.action_present_all).setVisible(false);
            menu.findItem(R.id.action_absent_all).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_enable) {
            toggleAttendanceView(true);
            menu.findItem(R.id.action_enable).setVisible(false);
            menu.findItem(R.id.action_disable).setVisible(true);
            return true;
        } else if (id == R.id.action_disable) {
            toggleAttendanceView(false);
            menu.findItem(R.id.action_disable).setVisible(false);
            menu.findItem(R.id.action_enable).setVisible(true);
            return true;
        } else if (id == R.id.action_present_all) {
            toggleTodaysAttendance(true);
            return true;
        } else if (id == R.id.action_absent_all) {
            toggleTodaysAttendance(false);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleTodaysAttendance(final boolean b) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int pastSundays = calculatePastSundays();
                if (b) {
                    for (int x = 1; x <= totalStudents; x++) {
                        CheckBox cb = (CheckBox) findViewById((totalStudents * ((today - pastSundays) - 1)) + x);
                        if (!cb.isChecked()) {
                            cb.setChecked(true);
                            calculateStudentAttendance(cb.getId());
                        }
                    }
                } else {
                    for (int x = 1; x <= totalStudents; x++) {
                        CheckBox cb = (CheckBox) findViewById((totalStudents * ((today - pastSundays) - 1)) + x);
                        if (cb.isChecked()) {
                            cb.setChecked(false);
                            calculateStudentAttendance(cb.getId());
                        }
                    }
                }
                calculateClassAttendance((totalStudents * (today - 1)) + 1);
            }
        });
    }

    private int calculatePastSundays() {
        int sundayCounter = 0;
        for (int i = 0; today >= sundaysDates[i]; i++) {
            sundayCounter++;
        }
        return sundayCounter;
    }

    private void toggleAttendanceView(final boolean b) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (int x = 1; x <= days; x++) {
                    if (x != today) {
                        LinearLayout ll = (LinearLayout) findViewById(10000 + x);
                        for (int y = 0; y < ll.getChildCount(); y++) {
                            ll.getChildAt(y).setEnabled(b);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        new BackgroundActivity().execute();
    }

    private String getAttendanceMark(int rollno) {
        CheckBox cb = (CheckBox) findViewById((((today - 1) - calculatePastSundays()) * totalStudents) + rollno);
        if (cb.isChecked())
            return "P";
        else
            return "A";
    }

    private String getClassRollNo(String rollno) {
        String[] temp = rollno.split("-");
        return temp[temp.length - 1];
    }

    class BackgroundActivity extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog.setTitle("Uploading Attendance");
            mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mDialog.setIndeterminate(false);
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mDialog.setProgress(values[0]);
            mDialog.setMessage(values[0] + "% uploaded");
        }

        @Override
        protected String doInBackground(String... params) {
            mDatabase = FirebaseDatabase.getInstance().getReference();
            Map<String, Object> map = new HashMap<>();
            for (int i = 1; i <= totalStudents; i++) {
                for (int j = 0; j < totalPeriods; j++) {
                    map.put(periods[j]+"", getAttendanceMark(i));
                    mDatabase.child("MJCET/attendance/" + branch + "/" + year + "/" + section + "/" + getClassRollNo(rollno[i - 1]) + "/"+month+"/" + today+"/").updateChildren(map);
                }
                publishProgress((((i * 100) / totalStudents)));
                try {
                    Thread.sleep(70);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return "done";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(AttendanceActivity.this, "Attendance Uploaded", Toast.LENGTH_SHORT).show();
            Snackbar.make(AttendanceActivity.this.findViewById(R.id.content_attendance), "Attendance Uploaded", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            mDialog.dismiss();
        }
    }
}
