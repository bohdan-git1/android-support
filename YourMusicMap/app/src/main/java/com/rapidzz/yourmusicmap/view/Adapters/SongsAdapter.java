package com.rapidzz.yourmusicmap.view.Adapters;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.creativedots.mylums.R;
import com.creativedots.mylums.model.retroFit.partnersDetails.Offer;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;

public class SongsAdapter extends RecyclerView.Adapter<DiscountsAdapter.ViewHolder> {

    private AppCompatActivity activity;
    private ArrayList<Offer> offers;

    public SongsAdapter(AppCompatActivity activity, ArrayList<Offer> offers) {
        this.activity = activity;
        this.offers = offers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.rv_item_discount_details, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int pos) {
        final Offer offer = offers.get(pos);

        holder.tvTitle.setText(offer.getAddress());
        holder.tvDescription.setText(offer.getDescription());

        holder.llHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.elDescription.setExpanded(!holder.elDescription.isExpanded(),true);
            }
        });

        holder.lnDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri.Builder directionsBuilder = new Uri.Builder()
                        .scheme("https")
                        .authority("www.google.com")
                        .appendPath("maps")
                        .appendPath("dir")
                        .appendPath("")
                        .appendQueryParameter("api", "1")
                        .appendQueryParameter("destination", offer.getLat() + "," + offer.getLng());

                activity.startActivity(new Intent(Intent.ACTION_VIEW, directionsBuilder.build()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return offers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout llHeader,lnDirection;
        private ExpandableLayout elDescription;
        private TextView tvTitle, tvDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            llHeader = itemView.findViewById(R.id.llHeader);
            lnDirection = itemView.findViewById(R.id.lnDirection);

            elDescription = itemView.findViewById(R.id.elDescription);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);

        }
    }
}
