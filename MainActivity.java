package com.example.jens.ma_wifi_direct_01;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final IntentFilter mIntentFilter = new IntentFilter();
    WifiP2pManager.Channel mChannel;
    WifiP2pManager mManager;
    WiFiDirectBroadcastReceiver mReceiver;
    Button scanButton;
    Button createGroup;
    public ListView listPeers;
    public List<String> testlist;
    public ArrayAdapter<String> adapter;
    private WiFiDirectBroadcastReceiver mReceiverClass;


    public WifiP2pManager.PeerListListener peerListListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get ListView object from xml
        listPeers = (ListView) findViewById(R.id.listPeers);

        // Defined Array values to show in ListView
        String[] Testdaten = {"Android List View",
                "Adapter implementation",
                "Simple List View In Android",
                "Create List View Android",
                "Android Example",
                "List View Source Code",
                "List View Array Adapter",
                "BlaBla"
        };


        testlist = new ArrayList<>(Arrays.asList(Testdaten));

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, testlist);

        // Assign adapter to ListView
        listPeers.setAdapter(adapter);

        // ListView Item Click Listener
        listPeers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String itemValue = (String) listPeers.getItemAtPosition(position);

                System.out.println("Test Position: " + itemPosition);



                mReceiver.connect(itemPosition);



                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                        .show();

            }
        });

        scanButton = (Button) findViewById(R.id.button1);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                //listPeers.invalidateViews();
                System.out.println("Scan for peers");
                scanPeers();

            }
        });


        createGroup = (Button) findViewById(R.id.button2);
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                System.out.println("create Group");
                createGroup();

            }
        });

        //  Indicates a change in the Wi-Fi P2P status.
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        // Indicates a change in the list of available peers.
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        // Indicates the state of Wi-Fi P2P connectivity has changed.
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        // Indicates this device's details have changed.
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);

    }

    public void scanPeers() {

        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                System.out.println("Test1: DiscoverPeers success");
            }

            @Override
            public void onFailure(int reasonCode) {
                System.out.println("Test1: DiscoverPeers Failure");
            }
        });
    }

    public void createGroup() {

        mManager.createGroup(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                System.out.println("Test1: Create Group success");
            }

            @Override
            public void onFailure(int reasonCode) {
                System.out.println("Test1: Create Group Failure");
            }
        });
    }


    /* register the broadcast receiver with the intent values to be matched */
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);
    }

    /* unregister the broadcast receiver */
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }
}
