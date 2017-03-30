package com.sam;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class AttendanceDialog extends DialogFragment implements CompoundButton.OnCheckedChangeListener {
    DialogCommunicator communicator;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        communicator = (DialogCommunicator) context;
    }


    RadioGroup rgSem, rgSec;
    String[] mBranch = {"CSE", "ECE", "EEE", "MEC", "IT", "CIV"};
    String[] mYear = {"I", "II", "III", "IV"};
    String branch, year, sem="1", sec="A", periods[] = new String[7];
    CheckBox p1, p2, p3, p4, p5, p6, p7;
    Button next,cancel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View dialog = inflater.inflate(R.layout.activity_attendance_dialog, null);
        dialog.setCameraDistance(50f);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        final Spinner branch = (Spinner) dialog.findViewById(R.id.spinnerBranch);
        final Spinner year = (Spinner) dialog.findViewById(R.id.spinnerYear);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, mBranch);
        branch.setAdapter(adapter);
        adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, mYear);
        year.setAdapter(adapter);
        for (int i=0;i<7;i++)
            periods[i]="0";
        rgSem = (RadioGroup) dialog.findViewById(R.id.rgSem);
        rgSec = (RadioGroup) dialog.findViewById(R.id.rgSec);
        p1 = (CheckBox) dialog.findViewById(R.id.cbPeriod1);
        p2 = (CheckBox) dialog.findViewById(R.id.cbPeriod2);
        p3 = (CheckBox) dialog.findViewById(R.id.cbPeriod3);
        p4 = (CheckBox) dialog.findViewById(R.id.cbPeriod4);
        p5 = (CheckBox) dialog.findViewById(R.id.cbPeriod5);
        p6 = (CheckBox) dialog.findViewById(R.id.cbPeriod6);
        p7 = (CheckBox) dialog.findViewById(R.id.cbPeriod7);
        p1.setOnCheckedChangeListener(this);
        p2.setOnCheckedChangeListener(this);
        p3.setOnCheckedChangeListener(this);
        p4.setOnCheckedChangeListener(this);
        p5.setOnCheckedChangeListener(this);
        p6.setOnCheckedChangeListener(this);
        p7.setOnCheckedChangeListener(this);
        next= (Button) dialog.findViewById(R.id.bNext);
        cancel= (Button) dialog.findViewById(R.id.bCancel);

        rgSec.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbSecA) {
                    sec = "A";
                } else {
                    sec = "B";
                }
            }
        });

        rgSem.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbSem1) {
                    sem = "1";
                } else {
                    sem = "2";
                }
            }
        });

        branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(AttendanceDialog.this.getActivity(), mBranch[position], Toast.LENGTH_SHORT).show();
                AttendanceDialog.this.branch = mBranch[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(AttendanceDialog.this.getActivity(), mYear[position], Toast.LENGTH_SHORT).show();
                AttendanceDialog.this.year = mYear[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i;
                for (i=0;i<7;i++) {
                    if (periods[i].equals("1"))
                        break;
                }
                if (i<7) {
                    communicator.sendData(AttendanceDialog.this.branch,AttendanceDialog.this.year,AttendanceDialog.this.sem,AttendanceDialog.this.sec,AttendanceDialog.this.periods);
                    dismiss();
                } else {
                    //Periods has not been selected...
                    Toast.makeText(AttendanceDialog.this.getActivity(), "Please Check Period(s)", Toast.LENGTH_LONG).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return dialog;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cbPeriod1:
                if (p1.isChecked())
                    periods[0]="1";
                else
                    periods[0]=null;
                break;
            case R.id.cbPeriod2:
                if (p2.isChecked())
                    periods[1]="1";
                else
                    periods[1]="0";
                break;
            case R.id.cbPeriod3:
                if (p3.isChecked())
                    periods[2]="1";
                else
                    periods[2]="0";
                break;
            case R.id.cbPeriod4:
                if (p4.isChecked())
                    periods[3]="1";
                else
                    periods[3]="0";
                break;
            case R.id.cbPeriod5:
                if (p5.isChecked())
                    periods[4]="1";
                else
                    periods[4]="0";
                break;
            case R.id.cbPeriod6:
                if (p6.isChecked())
                    periods[5]="1";
                else
                    periods[5]="0";
                break;
            case R.id.cbPeriod7:
                if (p7.isChecked())
                    periods[6]="1";
                else
                    periods[6]="0";
                break;
        }
    }

    interface DialogCommunicator {
        void sendData(String branch,String year,String semister,String section,String[] periods);
    }
}
