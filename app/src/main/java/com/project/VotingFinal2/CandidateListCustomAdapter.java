package com.project.VotingFinal2;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CandidateListCustomAdapter extends ArrayAdapter<NewCandidateData> {

    private Activity context;
    private List<NewCandidateData> CandidateDataList;

    public CandidateListCustomAdapter(Activity context, List<NewCandidateData> CandidateDataList) {
        super(context, R.layout.candidate_sample_layout, CandidateDataList);
        this.context = context;
        this.CandidateDataList = CandidateDataList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.candidate_sample_layout, null, true);

        NewCandidateData newCandidateData = CandidateDataList.get(position);

        TextView t1 = view.findViewById(R.id.CandidateNameInListID);
        TextView t2 = view.findViewById(R.id.CandidateVotesInListID);
        ImageView i1 = view.findViewById(R.id.CandidateLogoInListViewID);

        t1.setText("Candidate: " + newCandidateData.getCandidateName());
        t2.setText("Votes: " + newCandidateData.getCandidateVotes());

        String logoUrl = newCandidateData.getCandidateLogoLocation();
        Picasso
                .get()
                .load(logoUrl)
                .placeholder(R.mipmap.ic_launcher_round)
                .fit()
                .into(i1);

        return view;
    }
}