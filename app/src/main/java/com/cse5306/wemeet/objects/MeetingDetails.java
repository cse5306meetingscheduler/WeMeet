package com.cse5306.wemeet.objects;

/**
 * Created by Sathvik on 16/04/15.
 */

/*
* Meeting details object that has info of a meeting
* */

public class MeetingDetails {

    int groupId;
    String meetingTime;
    String meetingDate;
    String finalDestination;
    boolean host;
    String midpoint;
    int maxPpl;

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(String meetingTime) {
        this.meetingTime = meetingTime;
    }

    public String getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(String meetingDate) {
        this.meetingDate = meetingDate;
    }

    public String getFinalDestination() {
        return finalDestination;
    }

    public void setFinalDestination(String finalDestination) {
        this.finalDestination = finalDestination;
    }

    public boolean isHost() {
        return host;
    }

    public void setHost(boolean host) {
        this.host = host;
    }

    public String getMidpoint() {
        return midpoint;
    }

    public void setMidpoint(String midpoint) {
        this.midpoint = midpoint;
    }

    public int getMaxPpl() {
        return maxPpl;
    }

    public void setMaxPpl(int maxPpl) {
        this.maxPpl = maxPpl;
    }

}
