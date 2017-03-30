package com.sam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by MOHD IMTIAZ on 20-Mar-17.
 */

public class AttendanceListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<ListHeader> monthNames;

    public AttendanceListAdapter(Context context, ArrayList<ListHeader> monthNames) {
        this.context = context;
        this.monthNames = monthNames;
    }

    @Override
    public int getGroupCount() {
        return monthNames.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return monthNames.get(groupPosition).getDayAttendanceData().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return monthNames.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<ListChild> data = monthNames.get(groupPosition).getDayAttendanceData();
        return data.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ListHeader header = (ListHeader) getGroup(groupPosition);
        if (convertView==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_header,null);
        }
        TextView month = (TextView) convertView.findViewById(R.id.listHeaderMonth);
        TextView total = (TextView) convertView.findViewById(R.id.listHeaderTotal);
        TextView percent = (TextView) convertView.findViewById(R.id.listHeaderPercent);

        month.setText(header.getMonthName());
        total.setText(header.getTotalPresentDays());
        percent.setText(header.getPresentPercent());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ListChild childData = (ListChild) getChild(groupPosition, childPosition);
        if (convertView==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_child,null);
        }
        TextView date = (TextView) convertView.findViewById(R.id.listChildDate);
        TextView p1 = (TextView) convertView.findViewById(R.id.listChildP1);
        TextView p2 = (TextView) convertView.findViewById(R.id.listChildP2);
        TextView p3 = (TextView) convertView.findViewById(R.id.listChildP3);
        TextView p4 = (TextView) convertView.findViewById(R.id.listChildP4);
        TextView p5 = (TextView) convertView.findViewById(R.id.listChildP5);
        TextView p6 = (TextView) convertView.findViewById(R.id.listChildP6);
        TextView p7 = (TextView) convertView.findViewById(R.id.listChildP7);
        TextView total = (TextView) convertView.findViewById(R.id.listChildTotal);

        date.setText(childData.getDate());
        p1.setText(childData.getP1());
        p2.setText(childData.getP2());
        p3.setText(childData.getP3());
        p4.setText(childData.getP4());
        p5.setText(childData.getP5());
        p6.setText(childData.getP6());
        p7.setText(childData.getP7());
        total.setText(childData.getTotal());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
