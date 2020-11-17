package com.jrm.backitup.Lists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.jrm.backitup.Models.BU;
import com.jrm.backitup.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/*
 *  @Author Jayesh (codeos)
 * I hate comments but this is for you to understand my strange code.
 * My function names will mostly explain the code.
 */
public class MembersAdp extends RecyclerView.Adapter<MembersHolder> {
    Context context;
    // this is basically a list of members in a group
    ArrayList<JSONObject> memberList;

    public MembersAdp(Context context) {
        this.context = context;
        memberList = new ArrayList<>();
    }

    @Override
    public MembersHolder onCreateViewHolder(ViewGroup parent, int type) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View layout = inflater.inflate(R.layout.member_list, parent, false);
        return new MembersHolder(layout);
    }

    @Override
    public void onBindViewHolder(MembersHolder holder, int position) {
        try {
            holder.memberName.setText(memberList.get(position).getString("firstName") + " " + memberList.get(position).getString("lastName"));
        }catch(Exception error) {
            Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    public void add(JSONArray data) throws Exception {
        memberList.clear();
        for(int each = 0; each < data.length(); each++) {
            memberList.add(data.getJSONObject(each));
        }

        this.notifyDataSetChanged();
    }
}

class MembersHolder extends RecyclerView.ViewHolder {

    // elements that are in member_list.xml
    public TextView memberName;
    public LinearLayoutCompat parent;

    public MembersHolder(View layout) {
        super(layout);
        parent = (LinearLayoutCompat) layout.findViewById(R.id.memParent);
        memberName = (TextView) layout.findViewById(R.id.memberName);
    }
}