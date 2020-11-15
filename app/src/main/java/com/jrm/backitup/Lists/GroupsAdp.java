package com.jrm.backitup.Lists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.jrm.backitup.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/*
 *  @Author Jayesh (codeos)
 * I hate comments but this is for you to understand my strange code.
 * My function names will mostly explain the code.
 */
public class GroupsAdp extends RecyclerView.Adapter<GroupHolder>{
    Context context;
    // this is basically a list of the three tables (BUG, BG, BU) in jsonobject form
    ArrayList<JSONObject> grpList, searchList, userGroups;
    boolean isSearch = false;

    /*
    * the grpList holds the public groups.
    * the searchList will be loaded and used when search functionality is used,
    * this will filter grpList to load searchList.
    * the userGroups holds the groups in which the current user exists.
    *
    * now i know i should  have used sqlite or just called server again and again for search,
    * but i just wanted to try something different.
    */

    public GroupsAdp(Context context) {
        this.context = context;
        grpList = new ArrayList<>();
        searchList = new ArrayList<>();
        userGroups = new ArrayList<>();
    }

    @Override
    public GroupHolder onCreateViewHolder(ViewGroup parent, int type) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View layout = inflater.inflate(R.layout.grp_list, parent, false);
        return new GroupHolder(layout);
    }

    @Override
    public void onBindViewHolder(GroupHolder holder, int position) {
        try {
            JSONObject group = null;
            if(isSearch) {
                group = searchList.get(position);
            }else{
                group = userGroups.get(position);
            }
            holder.grpName.setText(group.getString("name"));
            holder.grpOwner.setText("Owner: " + group.getString("firstName"));
            holder.grpUserCount.setText("Total Users: " + group.getString("userCount"));

        }catch(Exception error) {
            Toast.makeText(context.getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() { return (isSearch) ? searchList.size() : userGroups.size(); }

    public void add(JSONArray userList, JSONArray publicList) throws Exception {
        userGroups.clear();
        grpList.clear();
        for(int each = 0; each < userList.length(); each++) {
            userGroups.add(userList.getJSONObject(each));
        }
        isSearch = false;
        this.notifyDataSetChanged();

        for(int each = 0; each < publicList.length(); each++) {
            grpList.add(publicList.getJSONObject(each));
        }
    }


    /*
    * search should have been with server request or database, i know ....
    * but i needed to try something out.
    */
    public void search(CharSequence value) throws Exception {
        searchList.clear();
        JSONObject each = null;
        for(int group = 0; group < grpList.size(); group++) {
            each = grpList.get(group);
            if(each.getString("name").contains(value) ||
                each.getString("firstName").contains(value)) {

                searchList.add(each);
            }
        }
        isSearch = true;
        this.notifyDataSetChanged();
    }

    public void refresh() {
        isSearch = false;
        this.notifyDataSetChanged();
    }

}

class GroupHolder extends RecyclerView.ViewHolder {

    // elements that are in grp_list.xml
    public LinearLayoutCompat grpParent;
    public TextView grpName, grpOwner, grpUserCount;

    public GroupHolder(View layout) {
        super(layout);
        grpParent = layout.findViewById(R.id.grpParent);
        grpName = layout.findViewById(R.id.grpName);
        grpOwner = layout.findViewById(R.id.grpOwner);
        grpUserCount = layout.findViewById(R.id.grpUserCount);
    }
}