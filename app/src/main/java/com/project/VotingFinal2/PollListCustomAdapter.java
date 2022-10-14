package com.project.VotingFinal2;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class PollListCustomAdapter extends ArrayAdapter<PollMetaData> {

    private Activity context;
    private List<PollMetaData> pollMetaDataList;

    public PollListCustomAdapter(Activity context, List<PollMetaData> pollMetaDataList) {
        super(context, R.layout.poll_list_sample_layout, pollMetaDataList);
        this.context = context;
        this.pollMetaDataList = pollMetaDataList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.poll_list_sample_layout, null, true);

        PollMetaData pollMetaData = pollMetaDataList.get(position);

        TextView t1 = view.findViewById(R.id.PollNameInListID);
        TextView t2 = view.findViewById(R.id.AdminNameInListID);

        int x = pollMetaData.getPoll_switch();

        t1.setText("Poll: " + pollMetaData.getPollname());

        if (x == 1)
//        t2.setText("Admin: "+ pollMetaData.getAdminEmail());
            t2.setText("Poll: Off");
        else
            t2.setText("Poll: On");

        return view;
    }
}
