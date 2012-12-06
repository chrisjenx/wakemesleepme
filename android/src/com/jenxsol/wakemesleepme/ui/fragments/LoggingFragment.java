/**
 * @project WakeMeSleepMee
 * @author chris.jenkins
 * @created Dec 1, 2012
 */
package com.jenxsol.wakemesleepme.ui.fragments;

import java.util.Map;
import java.util.Set;

import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jenxsol.wakemesleepme.R;

/**
 * @author chris.jenkins
 */
public class LoggingFragment extends BaseFragment
{

    private static StringBuilder sb = new StringBuilder();
    private static String logOutputFormat = "[%d] =: %s\n";
    private static int count = 0;
    private LruCache<Integer, String> consoleStrings = new LruCache<Integer, String>(30);

    private TextView mTV;

    /**
     * @see com.jenxsol.wakemesleepme.ui.fragments.BaseFragment#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        sBus.register(this, EventLogOutput.class);
    }

    /**
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
     *      android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.frag_log_view, container, false);
    }

    /**
     * @see android.support.v4.app.Fragment#onViewCreated(android.view.View, android.os.Bundle)
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        mTV = (TextView) view.findViewById(R.id.frag_logging_text_view);
        populate();
        sBus.post(new EventLogOutput("Log Started"));
    }

    /**
     * @see android.support.v4.app.Fragment#onDestroyView()
     */
    @Override
    public void onDestroyView()
    {
        sBus.unregister(this, EventLogOutput.class);
        super.onDestroyView();
    }

    public void onEventMainThread(final EventLogOutput event)
    {
        consoleStrings.put(count, event.newString);
        count++;
        populate();
    }

    /**
     * @author chris.jenkins
     */
    private void populate()
    {
        if (mTV == null)
            return;

        final Map<Integer, String> snapshot = consoleStrings.snapshot();
        Set<Integer> keySet = snapshot.keySet();
        mTV.setText("");
        sb.setLength(0);
        for (Integer key : keySet)
        {
            sb.append(String.format(logOutputFormat, key, snapshot.get(key)));
        }
        mTV.setText(sb);
    }

    public static final class EventLogOutput
    {

        public final String newString;

        /**
         * @author chris.jenkins
         * @param newString
         */
        public EventLogOutput(String newString)
        {
            this.newString = newString;
        }
    }

}
