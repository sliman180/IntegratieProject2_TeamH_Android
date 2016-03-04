package be.kdg.kandoe.kandoeandroid.login.cirkelsessie;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import be.kdg.kandoe.kandoeandroid.R;
import be.kdg.kandoe.kandoeandroid.login.helpers.Child;
import be.kdg.kandoe.kandoeandroid.login.helpers.Parent;



public class CirkelsessieActivity extends AppCompatActivity {

    CirkelsessieListAdapter cirkelsessieListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cirkelsessie);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ExpandableListView elv = (ExpandableListView) findViewById(R.id.list);
        Context context = getBaseContext();
        cirkelsessieListAdapter = new CirkelsessieListAdapter(context);
        elv.setAdapter(cirkelsessieListAdapter);

    }


    public void setTest(String a){
        Parent parent = cirkelsessieListAdapter.parentItems.get(0);
        parent.getChildren().add(new Child(a));
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
            dummyData();
        }

        private void dummyData(){
            for(int i = 1;i <5; i++){
                final Parent parent = new Parent();

                parent.setPosition("Position " + i);
                parent.setChildren(new ArrayList<Child>());

                parentItems.add(parent);
            }
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
        public Child getChild(int i, int i1) {
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

            String niveauName = getGroup(i).getPosition();
            if (view == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = infalInflater.inflate(R.layout.cirkel_header_list,
                        null);
            }
            TextView item = (TextView) view.findViewById(R.id.niveau);
            item.setTypeface(null, Typeface.BOLD);
            item.setText(niveauName);
            return view;
        }

        @Override
        public View getChildView(int groupPos, int childPos, boolean b, View view, ViewGroup viewGroup) {


            final String childText = getChild(groupPos,childPos).getChildText();

            if (view == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = infalInflater.inflate(R.layout.cirkel_list_child, null);
            }

            TextView txtListChild = (TextView) view
                    .findViewById(R.id.kaart);

            txtListChild.setText(childText);

            return view;

        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }

    }



}
