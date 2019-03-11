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
public class Home_activityTest {
    @Rule
    public ActivityTestRule<Home_activity> maankd = new ActivityTestRule<Home_activity>(Home_activity.class);
    private Home_activity home_activity =null;
    @Before
    public void setUp() throws Exception {
        home_activity = maankd.getActivity();

    }
    @Test
    public  void Testay(){
        View v = home_activity.findViewById(R.id.auto);
        assertNotNull(v);
        v = home_activity.findViewById(R.id.iconsearch);
        assertNotNull(v);
    }

    @After
    public void tearDown() throws Exception {

    }

}