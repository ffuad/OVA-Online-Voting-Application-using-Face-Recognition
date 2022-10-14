package com.project.VotingFinal2;

public class PollMetaData {

    private String pollname, uniquePollID, adminEmail;
    private int poll_switch;

    public PollMetaData() {

    }

    public PollMetaData(String pollname, String uniquePollID, String adminEmail, int poll_switch) {
        this.pollname = pollname;
        this.uniquePollID = uniquePollID;
        this.adminEmail = adminEmail;
        this.poll_switch = poll_switch;
    }

    public String getPollname() {
        return pollname;
    }

    public void setPollname(String pollname) {
        this.pollname = pollname;
    }

    public String getUniquePollID() {
        return uniquePollID;
    }

    public void setUniquePollID(String uniquePollID) {
        this.uniquePollID = uniquePollID;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public int getPoll_switch() {
        return poll_switch;
    }

    public void setPoll_switch(int poll_switch) {
        this.poll_switch = poll_switch;
    }
}
