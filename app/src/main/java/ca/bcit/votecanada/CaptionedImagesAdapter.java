package ca.bcit.votecanada;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Image adapter for party logos
 * @author Jovan Sekhon, Kang Wang, Lawrence Zheng, 2019-11-20
 */
public class  CaptionedImagesAdapter extends RecyclerView.Adapter<CaptionedImagesAdapter.ViewHolder>
{
    // onclick listener
    private Listener listener;
    // caption for party
    private String[] captions;
    // img id
    private int[] imageIds;

    /**
     * listener interface
     */
    interface Listener {
        void onClick(String partyName);
    }

    /**
     * setter for listener
     * @param listener
     */
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    /**
     * view holder
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    /**
     * Img adapter
     * @param captions
     * @param imageIds
     */
    public CaptionedImagesAdapter(String[] captions, int[] imageIds) {
        this.captions = captions;
        this.imageIds = imageIds;
    }

    /**
     * nun of captions
     * @return
     */
    @Override
    public int getItemCount() {
        return captions.length;
    }

    /**
     * captioned img adapter and img holder
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public CaptionedImagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_captioned_image, parent, false);
        return new ViewHolder(cv);
    }

    /**
     * binded view holder
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final CardView cardView = holder.cardView;

        ImageView imageView = cardView.findViewById(R.id.item_image);
        Drawable drawable = ContextCompat.getDrawable(cardView.getContext(),imageIds[position]);
        imageView.setImageDrawable(drawable);
        imageView.setContentDescription(captions[position]);

        TextView textView = cardView.findViewById(R.id.item_text);
        textView.setText(captions[position]);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick(captions[position]);
                }
            }
        });

    }
}
