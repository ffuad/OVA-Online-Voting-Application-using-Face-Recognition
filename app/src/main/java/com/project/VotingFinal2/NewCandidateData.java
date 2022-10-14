package com.project.VotingFinal2;

public class NewCandidateData {

    private String candidateName, candidateLogoLocation;
    private int candidateVotes;

    public NewCandidateData() {
    }

    public NewCandidateData(String candidateName, String candidateLogoLocation, int candidateVotes) {
        this.candidateName = candidateName;
        this.candidateLogoLocation = candidateLogoLocation;
        this.candidateVotes = candidateVotes;
    }


    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public String getCandidateLogoLocation() {
        return candidateLogoLocation;
    }

    public void setCandidateLogoLocation(String candidateLogoLocation) {
        this.candidateLogoLocation = candidateLogoLocation;
    }

    public int getCandidateVotes() {
        return candidateVotes;
    }

    public void setCandidateVotes(int candidateVotes) {
        this.candidateVotes = candidateVotes;
    }
}
