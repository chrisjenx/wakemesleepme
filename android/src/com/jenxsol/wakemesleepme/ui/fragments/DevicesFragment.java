/**
 * @project WakeMeSleepMee
 * @author chris.jenkins
 * @created Dec 2, 2012
 */
package com.jenxsol.wakemesleepme.ui.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.commonsware.cwac.merge.MergeAdapter;
import com.jenxsol.wakemesleepme.AppPreferences;
import com.jenxsol.wakemesleepme.AppPreferences.EventPrefsUpdated;
import com.jenxsol.wakemesleepme.R;
import com.jenxsol.wakemesleepme.models.WrappedPacket;
import com.jenxsol.wakemesleepme.server.UdpServer.EventPacketReceived;

/**
 * @author chris.jenkins
 */
public class DevicesFragment extends BaseFragment implements OnItemClickListener
{

    private ListView mListView;
    private MergeAdapter mAdapter;

    private TextView mAliveHeader;
    private AliveAdapter mAliveAdapter;
    private TextView mEnrolledHeader;
    private EnroledAdapter mEnrolledAdapter;

    private Handler h = new Handler();
    private Runnable rRefresh = new Runnable() {

        @Override
        public void run()
        {
            if (mListView != null)
            {
                mListView.invalidateViews();
                h.postDelayed(rRefresh, 2500);
            }
        }
    };

    /**
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
     *      android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mAliveHeader = (TextView) inflater.inflate(R.layout.element_header, container, false);
        mAliveHeader.setText("Alive computers");
        mEnrolledHeader = (TextView) inflater.inflate(R.layout.element_header, container, false);
        mEnrolledHeader.setText("Wake computers");
        return inflater.inflate(R.layout.frag_devices, container, false);
    }

    /**
     * @see android.support.v4.app.Fragment#onViewCreated(android.view.View, android.os.Bundle)
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        mListView = (ListView) view.findViewById(R.id.frag_devices_listview);
    }

    /**
     * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mAliveAdapter = new AliveAdapter(getActivity());
        mEnrolledAdapter = new EnroledAdapter(getActivity());


        mAdapter = new MergeAdapter();

        mAdapter.addView(mAliveHeader, false);
        mAdapter.addAdapter(mAliveAdapter);
        mAdapter.addView(mEnrolledHeader, false);
        mAdapter.addAdapter(mEnrolledAdapter);
        sBus.register(mAliveAdapter, EventPacketReceived.class);
        sBus.register(mEnrolledAdapter, EventPrefsUpdated.class);

        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

    }

    /**
     * @see android.support.v4.app.Fragment#onStart()
     */
    @Override
    public void onStart()
    {
        super.onStart();
        h.postDelayed(rRefresh, 2500);

    }

    /**
     * @see android.support.v4.app.Fragment#onStop()
     */
    @Override
    public void onStop()
    {
        super.onStop();
        h.removeCallbacks(rRefresh);
    }

    /**
     * @see android.support.v4.app.Fragment#onDestroyView()
     */
    @Override
    public void onDestroyView()
    {
        sBus.unregister(mAliveAdapter);
        sBus.unregister(mEnrolledAdapter);
        super.onDestroyView();
    }

    /**
     * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView,
     *      android.view.View, int, long)
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Object o = mAdapter.getItem(position);
        if(o instanceof WrappedPacket) {
        }

    }

    static class AliveAdapter extends BaseAdapter
    {

        // private static final LruCache<String, DatagramPacket> mLastReceivedData = new
        // LruCache<String, DatagramPacket>(
        // 25);
        // private static LinkedHashMap<String, DatagramPacket> mData = new LinkedHashMap<String,
        // DatagramPacket>();
        private static List<WrappedPacket> mPackets = new ArrayList<WrappedPacket>();

        private final LayoutInflater mInflater;

        /**
         * @author chris.jenkins
         */
        public AliveAdapter(Context ctx)
        {
            mInflater = LayoutInflater.from(ctx);
        }

        /**
         * @see android.widget.Adapter#getCount()
         */
        @Override
        public int getCount()
        {
            return mPackets.size();
        }

        /**
         * @see android.widget.Adapter#getItem(int)
         */
        @Override
        public Object getItem(int position)
        {
            return mPackets.get(position);
        }

        /**
         * @see android.widget.Adapter#getItemId(int)
         */
        @Override
        public long getItemId(int position)
        {
            return 0;
        }

        /**
         * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            final View v;
            final AliveViewHolder vh;
            if (convertView == null)
            {
                v = mInflater.inflate(R.layout.element_item_device_alive, parent, false);
                vh = new AliveViewHolder();
                vh.mac = (TextView) v.findViewById(android.R.id.text1);
                vh.time = (TextView) v.findViewById(android.R.id.text2);
                vh.status = v.findViewById(R.id.device_status);
                v.setTag(vh);
            }
            else
            {
                v = convertView;
                vh = (AliveViewHolder) v.getTag();
            }

            final WrappedPacket p = mPackets.get(position);

            vh.mac.setText(new String(p.getPacket().getData()));

            if (p.getTime() + 5 * 1000 > System.currentTimeMillis())
            {
                vh.status.setBackgroundColor(mInflater.getContext().getResources()
                        .getColor(R.color.holo_green_light));
            }
            else
            {
                vh.status.setBackgroundColor(mInflater.getContext().getResources()
                        .getColor(R.color.holo_red_light));
            }
            vh.time.setText(String.valueOf(p.getTime()));

            return v;
        }

        public void onEventMainThread(final EventPacketReceived event)
        {
            WrappedPacket pkt = event.packet;
            // String data = new String(pkt.getData());
            // AlivePacket p = mGson.fromJson(data, AlivePacket.class);
            // mLastReceivedData.put(p.getMac(), pkt);
            if (!mPackets.contains(pkt))
            {
                mPackets.add(pkt);
            }
            else
            {
                mPackets.remove(pkt);
                mPackets.add(pkt);
            }
            processPackets();
        }

        private final void processPackets()
        {
            // mData.clear();
            // mData.putAll(mLastReceivedData.snapshot());
            // mPackets = mData.values();
            // TODO set timers etc
            notifyDataSetChanged();
        }

    }

    static class EnroledAdapter extends BaseAdapter
    {

        // private static final Gson mGson = new GsonBuilder().create();
        private AppPreferences mPrefs = AppPreferences.get();

        private final LayoutInflater mInflater;

        private final List<String> mAddresses;

        /**
         * @author chris.jenkins
         */
        public EnroledAdapter(Context ctx)
        {
            mInflater = LayoutInflater.from(ctx);

            mAddresses = new ArrayList<String>();
            mAddresses.addAll(mPrefs.getWakeAddresses());
        }

        /**
         * @see android.widget.Adapter#getCount()
         */
        @Override
        public int getCount()
        {
            return mAddresses.size();
        }

        /**
         * @see android.widget.Adapter#getItem(int)
         */
        @Override
        public Object getItem(int position)
        {
            return null;
        }

        /**
         * @see android.widget.Adapter#getItemId(int)
         */
        @Override
        public long getItemId(int position)
        {
            return 0;
        }

        /**
         * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            final View v;
            final AliveViewHolder vh;
            if (convertView == null)
            {
                v = mInflater.inflate(R.layout.element_item_device_alive, parent, false);
                vh = new AliveViewHolder();
                vh.mac = (TextView) v.findViewById(android.R.id.text1);
                vh.time = (TextView) v.findViewById(android.R.id.text2);
                vh.status = v.findViewById(R.id.device_status);
                v.setTag(vh);
            }
            else
            {
                v = convertView;
                vh = (AliveViewHolder) v.getTag();
            }

            vh.mac.setText(mAddresses.get(position));

            vh.status.setVisibility(View.GONE);
            vh.time.setVisibility(View.GONE);

            return v;
        }

        public void onEventMainThread(final EventPrefsUpdated event)
        {
            mAddresses.clear();
            mAddresses.addAll(mPrefs.getWakeAddresses());
            notifyDataSetChanged();
        }


    }

    static class AliveViewHolder
    {

        TextView mac;
        TextView time;
        View status;
    }

}
