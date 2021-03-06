package com.example.camera_test2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.widget.Toast;

//peerListListener in setting activity, in setting activity, the WifiDirectBroadcastReceiver get initial in the initial function
//waiting for intent information, if get, go into if loop and using function go back to setting activity

public class WifiDirectBroadcastReceiver extends BroadcastReceiver {
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel; //A channel that connects the application to the Wifi p2p framework.
    // Most p2p operations require a Channel as an argument. An instance of Channel is obtained by doing a call on WifiP2pManager.initialize(Context, Looper, WifiP2pManager.ChannelListener)
    private setting mActivity;

    public WifiDirectBroadcastReceiver(WifiP2pManager mManager, WifiP2pManager.Channel mChannel, setting mActivity)
    {
        this.mManager = mManager; //mMangaer from this activity = mManager from main activity
        this.mChannel = mChannel;
        this.mActivity = mActivity;
    }

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction(); //get action
        //Toast.makeText(context, "in here", Toast.LENGTH_SHORT).show();
        if(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) //if action equal to action capture
        {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);

            if(state == WifiP2pManager.WIFI_P2P_STATE_ENABLED)
            {
                Toast.makeText(context, "Wifi is on", Toast.LENGTH_SHORT).show();
                //mManager.requestPeers(mChannel, mActivity.peerListListener);
            }
            else
            {
                Toast.makeText(context, "Wifi is off", Toast.LENGTH_SHORT).show();
            }
        }
        else if(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action))
        {
            if(mManager != null)
            {
                //Toast.makeText(context, "in here", Toast.LENGTH_SHORT).show();
                mManager.requestPeers(mChannel, mActivity.peerListListener);
                //discoverPeers???????????????????????????????????????????????????WIFI_P2P_PEERS_CHANGED_ACTION??????????????????????????????????????????????????????requestPeers????????????????????????????????????
            }
        }

        //jump from here to setting activity, connectionInfoListener, check connection info
        else if(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action))
        {
            //do something
            if(mManager == null)
            {
                return;
            }
            //Describes the status of a network interface.
            NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            if(networkInfo.isConnected()) //indicates whether network connectivity exists and it is possible to establish connections and pass data.
            {
                mManager.requestConnectionInfo(mChannel,mActivity.connectionInfoListener);
                Toast.makeText(context, "Device Connect", Toast.LENGTH_SHORT).show();

            }
            else
            {
                mActivity.connectionStatus.setText("Device Disconnected");
            }
        }

        else if(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action))
        {
            //do something
        }


    }
}
