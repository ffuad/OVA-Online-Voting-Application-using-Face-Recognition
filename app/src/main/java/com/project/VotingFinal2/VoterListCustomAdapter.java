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

public class VoterListCustomAdapter extends ArrayAdapter<NewVoterData> {

    private Activity context;
    private List<NewVoterData> VoterDataList;

    public VoterListCustomAdapter(Activity context,  List<NewVoterData> VoterDataList) {
        super(context, R.layout.voter_list_sample_layout, VoterDataList);
        this.context = context;
        this.VoterDataList = VoterDataList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.voter_list_sample_layout, null, true);

        NewVoterData voterData = VoterDataList.get(position);

        TextView t1 = view.findViewById(R.id.VoterNameInListID);
//        TextView t2 = view.findViewById(R.id.AdminNameInListID);

        t1.setText("Voter Name: "+voterData.getVoterName());
        t1.setText(voterData.getVoterName());
//        t2.setText("Admin: "+ pollMetaData.getAdminEmail());
        return view;
    }
}
