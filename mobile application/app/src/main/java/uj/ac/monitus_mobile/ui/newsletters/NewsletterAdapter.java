package uj.ac.monitus_mobile.ui.newsletters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import uj.ac.monitus_mobile.R;
import uj.ac.monitus_mobile.data.model.NewsletterModel;

/* Java class sets data held in newsletterModel
   to items of newsletter Recyclerview
 */
public class NewsletterAdapter extends RecyclerView.Adapter<NewsletterAdapter.ViewHolder> {
    private Context context;
    private List<NewsletterModel> newsletterModelArrayList;

    public NewsletterAdapter(Context context, List<NewsletterModel> newsletterModelArrayList) {
        this.context = context;
        this.newsletterModelArrayList = newsletterModelArrayList;
    }

    //inner class initializes cardviews, textviews, button etc.
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView newsletterHeadlineTV;
        private TextView newsletterDateTV;
        private TextView newsletterMessageTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            newsletterHeadlineTV = itemView.findViewById(R.id.newsletter_name_textview);
            newsletterDateTV = itemView.findViewById(R.id.newsletter_date_textview);
            newsletterMessageTV = itemView.findViewById(R.id.newsletter_message_textview);
        }

    }

    @NonNull
    @Override
    public NewsletterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.newsletter_cardview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsletterAdapter.ViewHolder holder, int position) {
        //set data to textviews
        NewsletterModel newsletter = newsletterModelArrayList.get(position);
        holder.newsletterHeadlineTV.setText(newsletter.getHeadline());
        holder.newsletterDateTV.setText(newsletter.getDate_published());
        holder.newsletterMessageTV.setText(newsletter.getMessage());
    }

    @Override
    public int getItemCount() {
        int a;
        if(newsletterModelArrayList != null){
            a = newsletterModelArrayList.size();
        }else{
            a = 0;
        }
        //return total number card items
        return a;
    }

}
