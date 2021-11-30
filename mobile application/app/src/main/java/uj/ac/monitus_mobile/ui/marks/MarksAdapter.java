package uj.ac.monitus_mobile.ui.marks;

import android.content.pm.LabeledIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import uj.ac.monitus_mobile.R;
import uj.ac.monitus_mobile.data.model.MarksModel;

public class MarksAdapter extends RecyclerView.Adapter<MarksAdapter.ViewHolder> {
    private List<MarksModel> marksModelList;
    private OnMarksListener onMarksListener;

    public MarksAdapter(List<MarksModel> marksModelList, OnMarksListener onMarksListener) {
        this.marksModelList = marksModelList;
        this.onMarksListener = onMarksListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView assessmentTV, marksTV;
        private OnMarksListener onMarksListener;

        public ViewHolder(@NonNull View itemView, OnMarksListener onMarksListener) {
            super(itemView);
            assessmentTV = itemView.findViewById(R.id.marks_assessment_name);
            marksTV = itemView.findViewById(R.id.assessment_mark);
            this.onMarksListener = onMarksListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onMarksListener.onMarkClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public MarksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.marks_cardview,parent,false);
        return new ViewHolder(view, onMarksListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MarksAdapter.ViewHolder holder, int position) {
        MarksModel marksModel = marksModelList.get(position);
        int mark= (int) marksModel.getScore();
        holder.assessmentTV.setText(marksModel.getAssessment_name());
        holder.marksTV.setText(String.valueOf(marksModel.getScore()+"/"+marksModel.getNum_marks()+" | "+marksModel.getWeighting()+"%"));
    }

    @Override
    public int getItemCount() {
        int a;
        if(marksModelList != null) {
            a = marksModelList.size();
        }else{
            a=0;
        }
        return a;
    }

    //interface to detect clicks
    public interface OnMarksListener {
        void onMarkClick(int position);
    }
}
