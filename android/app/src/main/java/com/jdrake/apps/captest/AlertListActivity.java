package com.jdrake.apps.captest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

/**
 * An activity representing a list of Alerts. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link AlertDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class AlertListActivity extends ActionBarActivity implements
        AlertListFragment.Callbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (BuildConfig.DEBUG)
        {
            try
            {
                Class<?> StrictMode = Class.forName("android.os.StrictMode");
                Class<?> ThreadPolicy = Class.forName("android.os.StrictMode$ThreadPolicy");
                Class<?> ThreadPolicyBuilder = Class.forName("android.os.StrictMode$ThreadPolicy$Builder");

                // StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
                Object threadBuilder = ThreadPolicyBuilder.newInstance();
                ThreadPolicyBuilder.getMethod("detectAll").invoke(threadBuilder);
                ThreadPolicyBuilder.getMethod("penaltyLog").invoke(threadBuilder);
                StrictMode.getMethod("setThreadPolicy", ThreadPolicy).invoke(null, ThreadPolicyBuilder.getMethod("build").invoke(threadBuilder));

                Class<?> VmPolicy = Class.forName("android.os.StrictMode$VmPolicy");
                Class<?> VmPolicyBuilder = Class.forName("android.os.StrictMode$VmPolicy$Builder");

                // StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().penaltyDeath().build());
                Object vmBuilder = VmPolicyBuilder.newInstance();
                VmPolicyBuilder.getMethod("detectAll").invoke(vmBuilder);
                VmPolicyBuilder.getMethod("penaltyLog").invoke(vmBuilder);
                // VmPolicyBuilder.getMethod("penaltyDeath").invoke(vmBuilder);
                StrictMode.getMethod("setVmPolicy", VmPolicy).invoke(null, VmPolicyBuilder.getMethod("build").invoke(vmBuilder));
            }
            catch (Exception e)
            {
                Log.w("AlertListActivity", "Unable to set strict mode (unsupported on this os version?)", e);
            }

            FragmentManager.enableDebugLogging(true);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_list);

        if (findViewById(R.id.alert_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((AlertListFragment) getSupportFragmentManager().findFragmentById(
                    R.id.alert_list)).setActivateOnItemClick(true);
        }
    }
    /**
     * Callback method from {@link AlertListFragment.Callbacks} indicating that
     * the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(AlertDetailFragment.ARG_ITEM_ID, id);
            AlertDetailFragment fragment = new AlertDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.alert_detail_container, fragment).commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, AlertDetailActivity.class);
            detailIntent.putExtra(AlertDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }
}
