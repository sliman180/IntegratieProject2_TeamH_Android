package be.kdg.kandoe.kandoeandroid.cirkelsessie;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import be.kdg.kandoe.kandoeandroid.R;
import be.kdg.kandoe.kandoeandroid.api.CirkelsessieAPI;
import be.kdg.kandoe.kandoeandroid.authorization.Authorization;
import be.kdg.kandoe.kandoeandroid.helpers.adaptermodels.ChatModel;
import be.kdg.kandoe.kandoeandroid.pojo.Bericht;
import be.kdg.kandoe.kandoeandroid.pojo.Gebruiker;
import be.kdg.kandoe.kandoeandroid.pojo.request.BerichtRequest;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {


    private Button chatButton;
    private EditText chatEditText;
    private String cirkelsessieId;
    private Gebruiker gebruiker;
    private ListView chatListview;
    private Handler handler;
    private ChatAdapter adapter = null;
    private Activity mActivity;
    private int aantalMessages = 0;
    private int maxAantalMessages = 0;
    private ArrayList<ChatModel> list = new ArrayList<>();
    private SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yyyy HH:mm");


    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        // Inflate the layout for this fragment


        chatButton = (Button) v.findViewById(R.id.btSend);
        chatEditText = (EditText) v.findViewById(R.id.etChat);
        chatListview = (ListView) v.findViewById(R.id.listview_chat);
        chatButtonClick();
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity= getActivity();
        cirkelsessieId = ((CirkelsessieActivity) getActivity()).getCirkelsessieId();
        gebruiker = ((CirkelsessieActivity) getActivity()).getGebruiker();
        handler = new Handler();
        createAdapter();
        getData();
    }
    private void chatButtonClick(){
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chatText = chatEditText.getText().toString();
                Retrofit retrofit = Authorization.authorize(getActivity());
                CirkelsessieAPI cirkelsessieAPI = retrofit.create(CirkelsessieAPI.class);
                BerichtRequest berichtRequest = new BerichtRequest(chatText, gebruiker.getId());
                Call<Void> call = cirkelsessieAPI.addBericht(cirkelsessieId, berichtRequest);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Response<Void> response, Retrofit retrofit) {
                        Toast.makeText(getActivity().getBaseContext(), "Bericht verzonden", Toast.LENGTH_SHORT).show();
                        chatEditText.setText("");
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(getActivity().getBaseContext(), "Bericht niet verzonden", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }

    public void getData(){
        CirkelsessieAPI cirkelsessieAPI =
                Authorization.authorize(getActivity()).create(CirkelsessieAPI.class);
        Call<List<Bericht>> call = cirkelsessieAPI.getBerichten(cirkelsessieId);
        call.enqueue(new Callback<List<Bericht>>() {
            @Override
            public void onResponse(Response<List<Bericht>> response, Retrofit retrofit) {
                addData(response);
//                adapter.notifyDataSetChanged();
//                buttonOpen.setBackgroundResource(R.color.colorAccent);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("chat", t.getMessage());
                Toast.makeText(getActivity().getBaseContext(), "failure",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addData(Response<List<Bericht>> response){
        list.clear();
        for (int i = 0; i < response.body().size(); ++i) {
            ChatModel chatModel;
            if(response.body().get(i).getGebruiker().getId() == gebruiker.getId()){
                chatModel = new ChatModel(true
                        ,response.body().get(i).getTekst(),response.body().get(i).getGebruiker().getGebruikersnaam()
                        ,response.body().get(i).getDatum());
            }else {
                chatModel = new ChatModel(false
                        ,response.body().get(i).getTekst(),response.body().get(i).getGebruiker().getGebruikersnaam()
                        ,response.body().get(i).getDatum());
            }
            list.add(chatModel);
        }
        if(response!=null){
            aantalMessages = response.body().size();
        }
        if((maxAantalMessages < aantalMessages) && adapter !=null){
            adapter.notifyDataSetChanged();
            maxAantalMessages = aantalMessages;
        }
    }
    public void createAdapter(){
        ListView listview = null;
        if (getView() != null)
            listview = (ListView) getView().findViewById(R.id.listview_chat);

        if (mActivity != null && adapter ==null) {
            adapter = new ChatAdapter(mActivity.getBaseContext(),
                    R.layout.item_list_chat_left, list);
        }

        if (listview != null) {
            listview.setAdapter(adapter);
        }

    }

    private final Runnable chatRunnable = new Runnable() {
        @Override
        public void run()
        {
            getData();
            //Do task
            handler.postDelayed(chatRunnable, 1000);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(chatRunnable);

    }

    @Override
    public void onResume() {
        super.onResume();

        handler.postDelayed(chatRunnable, 1000);
    }

    private class ChatAdapter extends ArrayAdapter<ChatModel> {
        private Context context;
        private ArrayList<ChatModel> modelsArrayList;

        public ChatAdapter(Context context,int textViewResourceId, ArrayList<ChatModel> modelsArrayList) {

            super(context, textViewResourceId, modelsArrayList);

            this.context = context;
            this.modelsArrayList = modelsArrayList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // 1. Create inflater
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


            // 2. Get rowView from inflater

            View rowView = null;
            if(!modelsArrayList.get(position).isGroupHeader()){
                if(modelsArrayList.get(position).isLeft()){
                    rowView = inflater.inflate(R.layout.item_list_chat_left, parent, false);
                }
                else {
                    rowView = inflater.inflate(R.layout.item_list_chat_right, parent, false);
                }

                // 3. Get icon,title & counter views from the rowView
                TextView titleView = (TextView) rowView.findViewById(R.id.item_title);
                TextView deelnemerView = (TextView) rowView.findViewById(R.id.item_deelnemer);
                TextView datumView = (TextView) rowView.findViewById(R.id.item_datum);


                // 4. Set the text for textView
                titleView.setText(modelsArrayList.get(position).getTitle());
                deelnemerView.setText(modelsArrayList.get(position).getOrganisator());
                Date date = new Date(modelsArrayList.get(position).getDate());
                datumView.setText(String.valueOf(ft.format(date)));

            }

            // 5. retrn rowView
            return rowView;
        }
    }

}
