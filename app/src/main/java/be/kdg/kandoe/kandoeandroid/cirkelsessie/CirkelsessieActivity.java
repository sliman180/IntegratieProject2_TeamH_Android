package be.kdg.kandoe.kandoeandroid.cirkelsessie;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import be.kdg.kandoe.kandoeandroid.R;
import be.kdg.kandoe.kandoeandroid.api.CirkelsessieAPI;
import be.kdg.kandoe.kandoeandroid.api.SpelkaartenAPI;
import be.kdg.kandoe.kandoeandroid.authorization.Authorization;
import be.kdg.kandoe.kandoeandroid.helpers.Parent;
import be.kdg.kandoe.kandoeandroid.pojo.Cirkelsessie;
import be.kdg.kandoe.kandoeandroid.pojo.Kaart;
import be.kdg.kandoe.kandoeandroid.pojo.Spelkaart;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class CirkelsessieActivity extends AppCompatActivity {
    private String cirkelsessieId;
    private int maxAantalCirkels;

    private CirkelsessieListAdapter cirkelsessieListAdapter;
    private Activity mActivity;
    private ExpandableListView elv;
    private CirkelsessieFragment fragment;
    private TextView circleLengthTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();

        mActivity = this;
        cirkelsessieId = extras.getString("cirkelsessieId");

        setContentView(R.layout.activity_cirkelsessie);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        elv = (ExpandableListView) findViewById(R.id.card_list);
        cirkelsessieListAdapter = new CirkelsessieListAdapter(getBaseContext());
        elv.setAdapter(cirkelsessieListAdapter);

        fragment = (CirkelsessieFragment) getFragmentManager().findFragmentById(R.id.cirkelsessie_fragment);

        circleLengthTextView = (TextView) findViewById(R.id.circle_length);
    }


    public void addCard(View v)
    {
        final Dialog newCardDialog = new Dialog(this);
        newCardDialog.setContentView(R.layout.custom_dialog);
        newCardDialog.setTitle("Vul de kaart tekst in");

        final EditText newCardInput = (EditText) newCardDialog.findViewById(R.id.dialogtext);
        Button okButton = (Button) newCardDialog.findViewById(R.id.dialogButtonOK);
        Button cancelButton = (Button) newCardDialog.findViewById(R.id.dialogButtonAnnuleer);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = Authorization.authorize(mActivity);
                final CirkelsessieAPI cirkelsessieAPI = retrofit.create(CirkelsessieAPI.class);
                Kaart kaart = new Kaart(newCardInput.getText().toString(), newCardInput.getText().toString(), false);
                Call<Kaart> call = cirkelsessieAPI.createSpelKaart(cirkelsessieId, kaart);

                call.enqueue(new Callback<Kaart>() {
                    @Override
                    public void onResponse(Response<Kaart> response, Retrofit retrofit) {
                        fragment.getData();
                        newCardDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Toast.makeText(mActivity.getBaseContext(), "kaart niet aangemaakt",
                                Toast.LENGTH_SHORT).show();
                        newCardDialog.dismiss();
                    }
                });
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newCardDialog.cancel();
            }
        });

        newCardDialog.show();
    }


    public String getCirkelsessieId() {
        return cirkelsessieId;
    }

    public void changeCardPosition(Spelkaart spelkaart){
        final Spelkaart spelkaartToChange = spelkaart;
        final Parent parent = cirkelsessieListAdapter.parentItems.get(spelkaartToChange.getPositie());
        Retrofit retrofit = Authorization.authorize(mActivity);
        SpelkaartenAPI spelkaartenAPI = retrofit.create(SpelkaartenAPI.class);

        Call<Spelkaart> call = spelkaartenAPI.verschuif(String.valueOf(spelkaartToChange.getId()));
        call.enqueue(new Callback<Spelkaart>() {
            @Override
            public void onResponse(Response<Spelkaart> response, Retrofit retrofit) {
                Toast.makeText(mActivity.getBaseContext(), "kaart verplaatst",
                        Toast.LENGTH_SHORT).show();
                spelkaartToChange.setPositie(spelkaartToChange.getPositie() + 1);
                parent.getChildren().add(spelkaartToChange);
                cirkelsessieListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(mActivity.getBaseContext(), "kaart is niet verplaatst",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    public class CirkelsessieListAdapter extends BaseExpandableListAdapter {

        private Context context;

        private ArrayList<Parent> parentItems;

        public CirkelsessieListAdapter(Context context) {
            this.context = context;
            this.parentItems = new ArrayList<>();
            getData();
        }

        private void getData(){
            Retrofit retrofit = Authorization.authorize(mActivity);

            CirkelsessieAPI cirkelsessieAPI = retrofit.create(CirkelsessieAPI.class);

            Call<Cirkelsessie> call = cirkelsessieAPI.getCirkelsessie(cirkelsessieId);

            call.enqueue(new Callback<Cirkelsessie>() {

                @Override
                public void onResponse(Response<Cirkelsessie> response, Retrofit retrofit) {
                    if(response.body() !=null){
                        String circleLengthText = "Cirkel met grootte: "+ String.valueOf(response.body().getAantalCirkels());
                        circleLengthTextView.setText(circleLengthText);
                        maxAantalCirkels = response.body().getAantalCirkels();
                        int getal = maxAantalCirkels;
                        for(int i = 1;i < maxAantalCirkels+1; i++){
                            final Parent parent = new Parent();
                            ArrayList<Spelkaart> spelkaarten = new ArrayList<>();
                            for(int j = 0; j< response.body().getSpelkaarten().size();j++){
                                Spelkaart spelkaart = response.body().getSpelkaarten().get(j);
                                if(spelkaart.getPositie() == i){
                                    spelkaarten.add(spelkaart);
                                }
                            }
                            parent.setChildren(spelkaarten);
                            parent.setCircleLength(getal);
                            getal--;
                            parentItems.add(parent);
                        }
                    }
                    Toast.makeText(getBaseContext(), "data received",
                            Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(Throwable t) {
                    Toast.makeText(getBaseContext(), "failure",
                            Toast.LENGTH_SHORT).show();
                    Log.d("failure",t.getMessage());

                }

            });

        }

        @Override
        public int getGroupCount() {
            return parentItems.size();
        }


        @Override
        public int getChildrenCount(int i) {
            return parentItems.get(i).getChildren().size();
        }

        @Override
        public Parent getGroup(int i) {
            return parentItems.get(i);
        }

        @Override
        public Spelkaart getChild(int i, int i1) {
            return parentItems.get(i).getChildren().get(i1);
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

            Parent parent = getGroup(i);
            if (view == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = infalInflater.inflate(R.layout.cirkel_header_list,
                        null);

            }

            LinearLayout lL = (LinearLayout) view.findViewById(R.id.header_linear);

            if(lL.getChildCount() == 0){
                for(int j = 0; j < parent.getCircleLength();j++){
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    if(j == 0){
            lp.setMargins(dpToPx(25), 0, 0, 0);
                    }
            ImageView item = new ImageView(getBaseContext());
            item.setImageResource(R.drawable.cirkel);
            item.setVisibility(View.VISIBLE);
            item.setLayoutParams(lp);

            lL.addView(item);
                }
            }

             elv.expandGroup(i);

            return view;
        }

        @Override
        public View getChildView(final int groupPos, final int childPos, boolean b, View view, ViewGroup viewGroup) {
            final String childText = getChild(groupPos,childPos).getKaart().getTekst();

            if (view == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = infalInflater.inflate(R.layout.cirkel_list_child, null);
            }

            final TextView txtListChild = (TextView) view
                    .findViewById(R.id.kaart);

            txtListChild.setText(childText);

            txtListChild.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                    builder.setTitle("Verplaats kaart?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Spelkaart spelkaart = getChild(groupPos, childPos);
                            if (spelkaart.getPositie() == maxAantalCirkels) {
                                Toast.makeText(getBaseContext(), "max positie bereikt",
                                        Toast.LENGTH_SHORT).show();
                            }else{
                                Parent oldParent = getGroup(groupPos);
                                oldParent.getChildren().remove(spelkaart);

                                changeCardPosition(spelkaart);
                                notifyDataSetChanged();
                            }
                        }
                    });
                    builder.setNegativeButton("Annuleer", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }
            });

            return view;

        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }

    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }
}
