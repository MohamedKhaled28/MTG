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
public class HelpActivityTest {
    @Rule
    public ActivityTestRule<HelpActivity> help = new ActivityTestRule<HelpActivity>(HelpActivity.class);
    HelpActivity helpActivity=null;
    @Before
    public void setUp() throws Exception {
        helpActivity = help.getActivity();
    }

    @Test
    public void checkkk(){
        View v = helpActivity.findViewById(R.id.btn_help);
        assertNotNull(v);
    }

    @After
    public void tearDown() throws Exception {

    }

}