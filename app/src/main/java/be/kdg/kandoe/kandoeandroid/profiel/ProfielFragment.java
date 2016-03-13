package be.kdg.kandoe.kandoeandroid.profiel;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.kdg.kandoe.kandoeandroid.R;
import be.kdg.kandoe.kandoeandroid.helpers.SharedPreferencesMethods;


public class ProfielFragment extends Fragment {

    private String token;

    private Activity mActivity;
    private Intent intent;

    public ProfielFragment() {
        // Required empty public constructor
    }


    public static ProfielFragment newInstance() {

        return new ProfielFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            mActivity = getActivity();
            token = SharedPreferencesMethods.getFromSharedPreferences(mActivity, getString(R.string.token));
        }
//        intent = new Intent(mActivity.getBaseContext(), SubthemaActivity.class);
        getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profiel, container, false);
    }

    public void getData(){

    }

}
