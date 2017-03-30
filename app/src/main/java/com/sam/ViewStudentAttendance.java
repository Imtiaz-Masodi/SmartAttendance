package com.sam;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class ViewStudentAttendance extends AppCompatActivity {

    String attendance[][][] = new String[3][31][7],daysPresent;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    EditText rollno;
    TextView name, branch;
    Button getAttendance;
    LinearLayout attendanceView;
    ExpandableListView attendanceListView;
    AttendanceListAdapter adapter;
    ArrayList<ListHeader> listHeaders = new ArrayList<>();
    ProgressDialog mDialog;
    boolean isClicked=false;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student_attendance);
        setTitle("View Attendance");

        mDialog = new ProgressDialog(this);
        mDialog.setTitle("Connecting To Database");
        mDialog.setMessage("Getting Attendance Data.Please Wait. . .");
        rollno = (EditText) findViewById(R.id.etAttendanceRollno);
        name = (TextView) findViewById(R.id.tvAttendanceSName);
        branch = (TextView) findViewById(R.id.tvAttendanceBranch);
        getAttendance = (Button) findViewById(R.id.bGetAttendance);
        attendanceView = (LinearLayout) findViewById(R.id.llAttendanceView);
        attendanceListView = (ExpandableListView) findViewById(R.id.elvAttendance);

        getAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClicked)
                    return;
                isClicked=true;
                mDialog.show();
                attendanceView.setVisibility(View.GONE);
                listHeaders.clear();
                if (validateRollno()) {
                    mDatabase = FirebaseDatabase.getInstance().getReference("MJCET/attendance/");
                    mDatabase.child(getBranchId(rollno.getText().toString().split("-")[2])+"/"+getYear(rollno.getText().toString().split("-")[1])+"/A/"+Integer.parseInt(rollno.getText().toString().split("-")[3])).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Iterator<DataSnapshot> monthIterator = dataSnapshot.getChildren().iterator();
                            String[] mData=new String[3];
                            for (int i=0;monthIterator.hasNext();i++) {
                                int periodsInMonth=0,periodsAttended=0;
                                DataSnapshot monthDS = monthIterator.next();
                                Iterator<DataSnapshot> dayIterator = monthDS.getChildren().iterator();
                                for (int j=0;dayIterator.hasNext();j++) {
                                    DataSnapshot dayDS = dayIterator.next();
                                    Iterator<DataSnapshot> periodsIterator = dayDS.getChildren().iterator();
                                    ListChild periods = new ListChild();
                                    int dayPresentCount=0;
                                    mData[0]=monthDS.getKey();
                                    for (int k=0;periodsIterator.hasNext();k++){
                                        periodsInMonth++;
                                        DataSnapshot periodsDS = periodsIterator.next();
                                        attendance[i][Integer.parseInt(dayDS.getKey())-1][Integer.parseInt(periodsDS.getKey())-1]= (String) periodsDS.getValue();
                                        if (periodsDS.getValue().equals("P")) {
                                            periodsAttended++;
                                            dayPresentCount++;
                                        }
                                    }
                                    periods.setDate(dayDS.getKey());
                                    periods.setP1(attendance[i][Integer.parseInt(dayDS.getKey())-1][0]);
                                    periods.setP2(attendance[i][Integer.parseInt(dayDS.getKey())-1][1]);
                                    periods.setP3(attendance[i][Integer.parseInt(dayDS.getKey())-1][2]);
                                    periods.setP4(attendance[i][Integer.parseInt(dayDS.getKey())-1][3]);
                                    periods.setP5(attendance[i][Integer.parseInt(dayDS.getKey())-1][4]);
                                    periods.setP6(attendance[i][Integer.parseInt(dayDS.getKey())-1][5]);
                                    periods.setP7(attendance[i][Integer.parseInt(dayDS.getKey())-1][6]);
                                    periods.setTotal(dayPresentCount+"");
                                    mData[1]="---";
                                    mData[2]="---";
                                    addAttendanceData(mData,periods);
                                }
                                float per = ((float) periodsAttended/(float)periodsInMonth)*100f;
                                mData[1]=periodsAttended+"/"+periodsInMonth;
                                mData[2]=per+"";
                                updateAttendanceData(mData);
                            }

                            if (attendanceListView.getAdapter()==null) {
                                adapter = new AttendanceListAdapter(ViewStudentAttendance.this, listHeaders);
                            }
                            attendanceListView.setAdapter(adapter);
                            /*adapter.notifyDataSetChanged();
                            expandAll();
                            collapseAll();*/
                            attendanceView.setVisibility(View.VISIBLE);
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    rollno.setError("Enter Valid Rollno.\n1604-14-733-301");
                    rollno.setFocusable(true);
                }
                mDialog.dismiss();
                isClicked=false;
            }
        });
        //addAttendanceData();
    }

    private void collapseAll() {
        int count = adapter.getGroupCount();
        for (int i=0;i<count;i++)
            attendanceListView.collapseGroup(i);
    }

    private void expandAll() {
        int count = adapter.getGroupCount();
        for (int i=0;i<count;i++)
            attendanceListView.expandGroup(i);
    }

    private String getYear(String s) {
        int courseYear=0;
        int year = Integer.parseInt(s);
        String weekDay;
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd/MM/yy");
        Calendar calendar = Calendar.getInstance();
        weekDay = dayFormat.format(calendar.getTime());
        String[] mDate = weekDay.split("/");
        for (int i=year;i<Integer.parseInt(mDate[2]);i++) {
            courseYear++;
        }
        if (Integer.parseInt(mDate[1])>6)
            courseYear++;
        switch (courseYear) {
            case 1:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            default:
                return "I";
        }

    }

    private String getBranchId(String branch) {
        switch (branch) {
            case "733" :
                return "CSE";
            case "736":
                return "IT";
            default:
                return "CSE";
        }
    }

    private boolean validateRollno() {
        String[] rollno = this.rollno.getText().toString().trim().split("-");
        if (rollno[0].length()==4 && rollno[1].length()==2 && rollno[2].length()==3 && rollno[3].length()==3)
            return true;
        else
            return false;
    }

    private void updateAttendanceData(String[] mData) {
        int len = listHeaders.size();
        int i;
        for (i=0;i<len;i++) {
            if (listHeaders.get(i).getMonthName().equals(getMonthName(Integer.parseInt(mData[0]))))
                break;
        }
        listHeaders.get(i).setTotalPresentDays(mData[1]);
        listHeaders.get(i).setPresentPercent(mData[2]);
    }

    private void addAttendanceData(String[] headerData, ListChild childData) {
        int len = listHeaders.size();
        int i=0;
        for (i=0;i<len;i++) {
            if (listHeaders.get(i).getMonthName().equals(getMonthName(Integer.parseInt(headerData[0]))))
                break;
        }
        if (i==(len)) {
            //Need to add a month. . .
            ListHeader data = new ListHeader(getMonthName(Integer.parseInt(headerData[0])),headerData[1],headerData[2]);
            data.dayAttendanceData.add(childData);
            listHeaders.add(data);
        } else {
            listHeaders.get(i).dayAttendanceData.add(childData);
        }
    }

    private String getMonthName(int monthNumber) {
        switch(monthNumber) {
            case 1:
                return "January";
            case 2:
                return "February";

            case 3:
                return "March";

            case 4:
                return "April";

            case 5:
                return "May";

            case 6:
                return "June";

            case 7:
                return "July";

            case 8:
                return "August";

            case 9:
                return "September";

            case 10:
                return "October";

            case 11:
                return "November";

            case 12:
                return "December";

            default:
                return "N/A";

        }
    }
}
