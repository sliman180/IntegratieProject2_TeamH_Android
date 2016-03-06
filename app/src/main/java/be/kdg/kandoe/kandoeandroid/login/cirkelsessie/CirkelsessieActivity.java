package be.kdg.kandoe.kandoeandroid.login.cirkelsessie;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.util.ArrayList;

import be.kdg.kandoe.kandoeandroid.R;
import be.kdg.kandoe.kandoeandroid.login.api.AuthAPI;
import be.kdg.kandoe.kandoeandroid.login.api.CirkelsessieAPI;
import be.kdg.kandoe.kandoeandroid.login.helpers.Parent;
import be.kdg.kandoe.kandoeandroid.login.pojo.Cirkelsessie;
import be.kdg.kandoe.kandoeandroid.login.pojo.Credentials;
import be.kdg.kandoe.kandoeandroid.login.pojo.Spelkaart;
import be.kdg.kandoe.kandoeandroid.login.pojo.Token;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.http.Path;


public class CirkelsessieActivity extends AppCompatActivity {

    CirkelsessieListAdapter cirkelsessieListAdapter;
    private String token;
    private String cirkelsessieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();

        token = extras.getString("token");
        cirkelsessieId = extras.getString("cirkelsessieId");

        setContentView(R.layout.activity_cirkelsessie);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ExpandableListView elv = (ExpandableListView) findViewById(R.id.list);
        Context context = getBaseContext();
        cirkelsessieListAdapter = new CirkelsessieListAdapter(context);
        elv.setAdapter(cirkelsessieListAdapter);



    }


    public String getToken() {
        return token;
    }

    public String getCirkelsessieId() {
        return cirkelsessieId;
    }

    public void setPosition(Spelkaart spelkaart){
        Parent parent = cirkelsessieListAdapter.parentItems.get(spelkaart.getPositie());

        parent.getChildren().add(spelkaart);
        cirkelsessieListAdapter.notifyDataSetChanged();
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
            OkHttpClient client = new OkHttpClient();
            client.interceptors().add(new Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                    Request newRequest = chain.request().newBuilder().addHeader("Authorization", "Bearer " + token).build();
                    return chain.proceed(newRequest);
                }
            });
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://teamh-spring.herokuapp.com")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            CirkelsessieAPI cirkelsessieAPI = retrofit.create(CirkelsessieAPI.class);

            Call<Cirkelsessie> call = cirkelsessieAPI.getCirkelsessie(cirkelsessieId);

            call.enqueue(new Callback<Cirkelsessie>() {

                @Override
                public void onResponse(Response<Cirkelsessie> response, Retrofit retrofit) {
                    if(response.body() !=null){
                        int aantalCirkels = response.body().getAantalCirkels();

                        int getal = aantalCirkels-1;
                        for(int i = 1;i < aantalCirkels; i++){
                            final Parent parent = new Parent();
                            parent.setChildren(new ArrayList<Spelkaart>());
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

            ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.list);
            expandableListView.expandGroup(i);

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
                    final Dialog dialog = new Dialog(CirkelsessieActivity.this);
                    dialog.setContentView(R.layout.custom_dialog);
                    dialog.setTitle("Verplaats kaart ?");

                    Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                    Button annuleerButton = (Button) dialog.findViewById(R.id.dialogButtonAnnuleer);
                    // if button is clicked, close the custom dialog
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Spelkaart spelkaart = getChild(groupPos, childPos);
                            spelkaart.setPositie(spelkaart.getPositie() + 1);
                            Parent oldParent = getGroup(groupPos);
                            oldParent.getChildren().remove(spelkaart);

                            Parent newParent = getGroup(spelkaart.getPositie());
                            newParent.getChildren().add(spelkaart);
                            notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    });

                    annuleerButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();

                }
            });

            return view;

        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }


    }

    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }



}
