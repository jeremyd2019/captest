package com.jdrake.apps.captest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jdrake.apps.captest.xml.Alert;
import com.jdrake.apps.captest.xml.DateFormatTransformer;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.transform.RegistryMatcher;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A fragment representing a single Alert detail screen.
 * This fragment is either contained in a {@link AlertListActivity}
 * in two-pane mode (on tablets) or a {@link AlertDetailActivity}
 * on handsets.
 */
public class AlertDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Alert mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AlertDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            // TODO load alert
            //mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            try {
                new AsyncTask<URL, Void, Alert>() {

                    @Override
                    protected Alert doInBackground(URL... urls) {
                        try {
                            RegistryMatcher m = new RegistryMatcher();
                            // note we should support more formats than this for iso8601
                            m.bind(Date.class, new DateFormatTransformer(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.US)));
                            Serializer serializer = new Persister(m);

                            InputStream is = urls[0].openStream();
                            try {
                                return serializer.read(Alert.class, is, false);
                            } finally {
                                is.close();
                            }
                        } catch (IOException e) {
                            Log.e("AlertDetailFragment", String.format("IOException getting alert detail for %s", urls[0].toString()), e);
                            return null;
                        } catch (Exception e) {
                            Log.e("AlertDetailFragment", String.format("Exception getting alert detail for %s", urls[0].toString()), e);
                            return null;
                        }
                    }

                    @Override
                    protected void onPostExecute(Alert alert) {
                        mItem = alert;
                        if (getView() != null) {
                            ((TextView) getView().findViewById(R.id.alert_detail)).setText(alert.getInfo().get(0).getDescription());
                        }
                    }
                }.execute(new URL(getArguments().getString(ARG_ITEM_ID)));
            }
            catch (MalformedURLException e)
            {
                Log.e("AlertDetailFragment", String.format("Malformed url: %s", getArguments().getString(ARG_ITEM_ID)), e);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.alert_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.alert_detail)).setText(mItem.getInfo().get(0).getDescription());
        }

        return rootView;
    }
}
