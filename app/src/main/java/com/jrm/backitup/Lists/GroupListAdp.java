package com.jrm.backitup.Lists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
public class GroupListAdp extends RecyclerView.Adapter<GroupListHolder> {
    Context context;
    // this is basically the half BG class in JSONObject form
    ArrayList<JSONObject> grpList;

    public GroupListAdp(Context context) {
        this.context = context;
        grpList = new ArrayList<>();
    }

    @Override
    public GroupListHolder onCreateViewHolder(ViewGroup parent, int type) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View layout = inflater.inflate(R.layout.grp_list, parent, false);
        return new GroupListHolder(layout);
    }

    @Override
    public void onBindViewHolder(GroupListHolder holder, int position) {
        try {

        }catch(Exception error) {
            Toast.makeText(context.getApplicationContext(), "Could not load grp name", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() { return grpList.size(); }

    public void add(JSONArray data) throws Exception {
        grpList.clear();
        for(int each = 0; each < data.length(); each++) {
            grpList.add(data.getJSONObject(each));
        }
        this.notifyDataSetChanged();
    }

}

class GroupListHolder extends RecyclerView.ViewHolder {

    //elements that are in grp_list
    TextView grpCode, grpName, grpUserCount;

    public GroupListHolder(View layout) {
        super(layout);
    }
}

