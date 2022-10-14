package com.project.VotingFinal2;

public class NewVoterData {

    private String voterEmail, voterName, voterFrontImage;
//    , voterRightImage, voterLeftImage;

    public NewVoterData() {
    }

    public NewVoterData(String voterEmail, String voterName, String voterFrontImage) {
        this.voterEmail = voterEmail;
        this.voterName = voterName;
        this.voterFrontImage = voterFrontImage;
//        this.voterRightImage = voterRightImage;
//        this.voterLeftImage = voterLeftImage;
    }

    public String getVoterEmail() {
        return voterEmail;
    }

    public void setVoterEmail(String voterEmail) {
        this.voterEmail = voterEmail;
    }

    public String getVoterName() {
        return voterName;
    }

    public void setVoterName(String voterName) {
        this.voterName = voterName;
    }

    public String getVoterFrontImage() {
        return voterFrontImage;
    }

    public void setVoterFrontImage(String voterFrontImage) {
        this.voterFrontImage = voterFrontImage;
    }

//    public String getVoterRightImage() {
//        return voterRightImage;
//    }
//
//    public void setVoterRightImage(String voterRightImage) {
//        this.voterRightImage = voterRightImage;
//    }
//
//    public String getVoterLeftImage() {
//        return voterLeftImage;
//    }
//
//    public void setVoterLeftImage(String voterLeftImage) {
//        this.voterLeftImage = voterLeftImage;
//    }
}
