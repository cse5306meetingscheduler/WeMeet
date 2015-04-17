package com.cse5306.wemeet.tasks;

import com.cse5306.wemeet.objects.Restaurant;

import java.util.List;

/**
 * Created by Sathvik on 17/04/15.
 */
public interface GetRestaurantListTaskResponse {
    public void processFinish(List<Restaurant> output);
}
