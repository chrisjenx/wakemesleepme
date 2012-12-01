/**
 * @project WakeMeSleepMee
 * @author chris.jenkins
 * @created Dec 1, 2012
 */
package com.jenxsol.wakemesleepme.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import de.greenrobot.event.EventBus;


/**
 * @author chris.jenkins 
 */
public class BaseFragment extends Fragment
{

    protected static EventBus sBus = EventBus.getDefault();

    /**
     * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

}
