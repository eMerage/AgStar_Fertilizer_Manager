package project.emarge.fertilizer_manager.views.adaptor.dealer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import project.emarge.fertilizer_manager.R;
import project.emarge.fertilizer_manager.model.datamodel.Rep;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteRepsAdapter extends ArrayAdapter<Rep> {

    Context context;
    int resource, textViewResourceId;
    List<Rep> items, tempItems, suggestions;

    public AutoCompleteRepsAdapter(Context context, int resource, int textViewResourceId, List<Rep> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<Rep>(items); // this makes the difference.
        suggestions = new ArrayList<Rep>();






    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_autocomplete_reps, parent, false);
        }
        Rep people = items.get(position);
        if (people != null) {
            TextView lblName = (TextView) view.findViewById(R.id.lbl_name);
            if (lblName != null)
                lblName.setText(people.getName());
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((Rep) resultValue).getName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Rep people : tempItems) {
                    if (people.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(people);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<Rep> filterList = (ArrayList<Rep>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Rep people : filterList) {
                    add(people);
                    notifyDataSetChanged();
                }
            }
        }
    };
}
