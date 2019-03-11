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
public class Install_activityTest {
    @Rule
    public ActivityTestRule<Install_activity> kd = new ActivityTestRule<Install_activity>(Install_activity.class);
    public Install_activity install_activity = null;
    @Before
    public void setUp() throws Exception {
        install_activity = kd.getActivity();

    }


    @Test
    public  void check(){
        View v = install_activity.findViewById(R.id.Txt_mail);
        assertNotNull(v);
        v = install_activity.findViewById(R.id.spinner);
        assertNotNull(v);
        v = install_activity.findViewById(R.id.button);
        assertNotNull(v);
    }

    @After
    public void tearDown() throws Exception {

    }

}