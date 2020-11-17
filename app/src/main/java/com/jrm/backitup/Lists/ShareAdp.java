package com.jrm.backitup.Lists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.jrm.backitup.R;
import com.jrm.backitup.Screens.Dashboard;
import com.jrm.backitup.Screens.ShareRoom;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/*
 *  @Author Jayesh (codeos)
 *  I hate comments but this is for you to understand my strange code.
 *  My function names will mostly explain the code.
 */
public class ShareAdp extends RecyclerView.Adapter<ShareHolder> {
    Context context;
    String currentUser = "";
    // this is basically a list of files shared by group by JSONObject form
    ArrayList<JSONObject> sharedList;

    public ShareAdp(Context context, String currentUser) {
        this.context = context;
        this.currentUser = currentUser;
        sharedList = new ArrayList<>();
    }

    @Override
    public ShareHolder onCreateViewHolder(ViewGroup parent, int type) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View layout = inflater.inflate(R.layout.share_list, parent, false);
        return new ShareHolder(layout);
    }

    @Override
    public void onBindViewHolder(ShareHolder holder, int position) {
        try {
            String sharedBy = sharedList.get(position).getString("firstName") + " shared";
            if(sharedList.get(position).getString("ownerCode").equals(currentUser)) {
                sharedBy = "You Shared";
            }
            holder.slName.setText(sharedBy);
            holder.slFile.setText(sharedList.get(position).getString("name"));
            holder.slDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*
                     * the function handles onClick on dash because we load the detail layout there.
                     * */
                    ((ShareRoom)context).downloadFile(sharedList.get(position));
                }
            });

        }catch(Exception error) {
            Toast.makeText(context.getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT);
        }
    }

    @Override
    public int getItemCount() { return sharedList.size(); }

    public void add(JSONArray data) throws Exception {
        sharedList.clear();
        for(int each = 0; each < data.length(); each++) {
            sharedList.add(data.getJSONObject(each));
        }
        this.notifyDataSetChanged();
    }
}

class ShareHolder extends RecyclerView.ViewHolder {

    // elements that are in share_list.xml
    public TextView slName, slFile;
    public ImageButton slDownload;

    public ShareHolder(View layout) {
        super(layout);
        slName = (TextView) layout.findViewById(R.id.slName);
        slFile = (TextView) layout.findViewById(R.id.slFile);
        slDownload = (ImageButton) layout.findViewById(R.id.slDownload);
    }
}