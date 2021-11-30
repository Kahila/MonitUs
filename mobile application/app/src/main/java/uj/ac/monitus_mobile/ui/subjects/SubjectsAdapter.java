package uj.ac.monitus_mobile.ui.subjects;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import uj.ac.monitus_mobile.R;
import uj.ac.monitus_mobile.data.model.SubjectModel;

/* Java class sets data held in SubjectModel
   to items of Subject Recyclerview
 */
public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.ViewHolder> {

    private Context context;
    private List<SubjectModel> subjectModelArrayList;
    private OnSubjectListener oneSubjectListener;

    public SubjectsAdapter(List<SubjectModel> subjectModelArrayList, Context context, OnSubjectListener onSubjectListener) {
        this.subjectModelArrayList = subjectModelArrayList;
        this.context = context;
        this.oneSubjectListener = onSubjectListener;
    }

    //inner class initializes cardviews, textview and imageview
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView subjectIV;
        private TextView subjectNameTV;
        private OnSubjectListener onSubjectListener;

        public ViewHolder(@NonNull View itemView, OnSubjectListener onSubjectListener) {
            super(itemView);
            subjectIV = itemView.findViewById(R.id.subject_imageview);
            subjectNameTV = itemView.findViewById(R.id.subject_name_textview);
            this.onSubjectListener = onSubjectListener;
            //set OnClickListener to viewholder
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onSubjectListener.onSubjectClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public SubjectsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_cardview, parent, false);
        return new ViewHolder(view, oneSubjectListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //set data to imageview and textview of cardview
        SubjectModel subject = subjectModelArrayList.get(position);
        holder.subjectNameTV.setText(subject.getSubject_name());
        holder.subjectIV.setImageResource(subject.getSubject_image());
    }

    @Override
    public int getItemCount() {
        int a;
        if(subjectModelArrayList != null){
            a = subjectModelArrayList.size();
        }else{a = 0;}
        //return total number of card items
        return a;
    }

    //method to detect clicks on subjectview
    public interface OnSubjectListener{
        //pass position of view clicked
        void onSubjectClick(int position);
    }
}
