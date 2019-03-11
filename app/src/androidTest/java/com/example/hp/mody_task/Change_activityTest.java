package com.example.hp.mody_task;

import android.support.test.rule.ActivityTestRule;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;

/**
 * Created by HP on 7/2/2017.
 */
public class Change_activityTest {
    @Rule
    public ActivityTestRule<Change_activity> change_ = new ActivityTestRule<Change_activity>(Change_activity.class);
    public Change_activity change_activity;
    @Before
    public void setUp() throws Exception {
        change_activity = change_.getActivity();

    }


    @Test
    public void check(){
        View v = change_activity.findViewById(R.id.spinner_change);
        assertNotNull(v);
        v = change_activity.findViewById(R.id.txt);
        assertNotNull(v);
    }

    @After
    public void tearDown() throws Exception {

    }

}