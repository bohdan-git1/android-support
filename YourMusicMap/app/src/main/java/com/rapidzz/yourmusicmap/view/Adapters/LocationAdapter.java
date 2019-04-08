package com.rapidzz.yourmusicmap.view.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.rapidzz.mymusicmap.datamodel.source.remote.ApiService;
import com.rapidzz.mymusicmap.datamodel.source.remote.RetrofitClientInstance;
import com.rapidzz.yourmusicmap.R;
import com.rapidzz.yourmusicmap.datamodel.model.PlacesAutoComplete;
import com.rapidzz.yourmusicmap.datamodel.model.Prediction;
import com.rapidzz.yourmusicmap.other.Event;
import com.rapidzz.yourmusicmap.other.util.Maps;
import com.rapidzz.yourmusicmap.other.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class LocationAdapter extends BaseAdapter implements Filterable {

    private Context mContext;
    private List<Prediction> mPredictions;

    private ListFilter listFilter = new ListFilter();

    public LocationAdapter(Context theContext) {
        this.mContext = theContext;
        mPredictions = new ArrayList<>();
    }


    @Override
    public int getCount() {
        return mPredictions.size();
    }

    @Override
    public Prediction getItem(int position) {
        return mPredictions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.tpl_place_prediction, parent, false);
        }
        Prediction placeItem = mPredictions.get(position);
        TextView placeName = convertView.findViewById(R.id.tv_place_name);
        TextView placeInfo = convertView.findViewById(R.id.tv_place_details);
        //placeName.setText(StringUtils.string(placeItem.getStructuredFormatting().getMainText()));
        //placeInfo.setText(StringUtils.string(placeItem.getStructuredFormatting().getSecondaryText()));
        convertView.setTag(placeItem.getDescription());

        return convertView;
    }


    @Override
    public Filter getFilter() {
        return listFilter;
    }

    public class ListFilter extends Filter {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            if (resultValue instanceof Prediction) {
                Prediction mPrediction = (Prediction) resultValue;
                return mPrediction.getDescription();
            }
            return super.convertResultToString(resultValue);
        }

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {


            //Event event = new Event();
            //    if()
            //event.setEventType(Event.SHOW_PROGRESS_BAR);
            //RxBus.defaultInstance().send(event);
            FilterResults results = new FilterResults();
            try {
             //   if (!StringUtils.isEmpty(prefix)) {
                    Double lat = 31.5546;
                    Double lon = 74.3572;
                    String url = Maps.getPlaceAutoCompleteUrl(mContext, StringUtils.string(prefix));
                 //   Timber.d(url);

                    /* =
                            ApiService.get().getPlacePredictions(url);*/

                retrofit2.Call<PlacesAutoComplete> autoCompleteCall =
                        RetrofitClientInstance.Companion.getInstance(mContext).getService()
                        .getPlacePredictions(url);

                    PlacesAutoComplete complete = autoCompleteCall.execute().body();
                    ArrayList<Prediction> predictions = new ArrayList<>(complete.getPredictions());

                    results.values = predictions;
                    results.count = predictions.size();
                //}
            } catch (Exception e) {
                e.printStackTrace();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //Event event = new Event();
            //event.setEventType(Event.HIDE_PROGRESS_BAR);
            //RxBus.defaultInstance().send(event);
            if (results != null && results.count > 0) {
                mPredictions.clear();
                mPredictions.addAll((Collection<Prediction>) results.values);
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }

        }

    }
}