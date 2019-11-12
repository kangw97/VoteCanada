package ca.bcit.votecanada;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class VotingOfficeAdapter extends ArrayAdapter<VotingOffice> {
    // activity instance
    private Activity context;
    // list of offices
    private List<VotingOffice> officeList;

    /**
     * Constructor for list view adapter
     * @param context
     * @param officeList
     */
    public VotingOfficeAdapter(Activity context, List<VotingOffice> officeList) {
        super(context, R.layout.list_layout, officeList);
        this.context = context;
        this.officeList = officeList;
    }

    /**
     * Constructor
     * @param context
     * @param resource
     * @param objects
     * @param context1
     * @param officeList
     */
    public VotingOfficeAdapter(Context context, int resource, List<VotingOffice> objects, Activity context1, List<VotingOffice> officeList) {
        super(context, resource, objects);
        this.context = context1;
        this.officeList = officeList;
    }

    /**
     * get view
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        // view of list items
        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);
        // textview to hold office name
        TextView tvOfficeName = listViewItem.findViewById(R.id.textViewOfficeName);
        // textview to hold office address
        TextView tvOfficeAddress = listViewItem.findViewById(R.id.textViewAddresses);
        // textview to hold office distance
        TextView tvOfficeDistance = listViewItem.findViewById(R.id.textViewDistance);
        // get each officeList
        VotingOffice vf = officeList.get(position);
        // set the office name
        tvOfficeName.setText(vf.getOfficeName());
        // set the office address
        tvOfficeAddress.setText(vf.getAddress());
        // set the office distance
        tvOfficeDistance.setText(Double.toString(vf.getDistance()));
        return listViewItem;
    }

}
