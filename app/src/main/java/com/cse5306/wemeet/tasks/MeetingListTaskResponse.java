package com.cse5306.wemeet.tasks;

import com.cse5306.wemeet.objects.MeetingDetails;

import java.util.List;

/**
 * Created by Sathvik on 16/04/15.
 */
public interface MeetingListTaskResponse {
    public void processFinish(List<MeetingDetails> output);
}
