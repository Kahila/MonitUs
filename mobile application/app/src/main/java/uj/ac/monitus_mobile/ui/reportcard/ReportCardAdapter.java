package uj.ac.monitus_mobile.ui.reportcard;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import uj.ac.monitus_mobile.R;
import uj.ac.monitus_mobile.data.model.ReportModel;

public class ReportCardAdapter extends RecyclerView.Adapter<ReportCardAdapter.ViewHolder> {
    private Context context;
    private OnReportListener onReportListener;
    private List<ReportModel> reportModelList;
    private int studentAverage;

    public ReportCardAdapter(List<ReportModel> reportModelList, Context context, OnReportListener onReportListener, int studentAverage) {
        this.reportModelList = reportModelList;
        this.context = context;
        this.onReportListener = onReportListener;
        this.studentAverage = studentAverage;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView subjectNameTV, markTV, indicatorTV;
        private OnReportListener onReportListener;

        public ViewHolder(@NonNull View itemView, OnReportListener onReportListener) {
            super(itemView);
            subjectNameTV = itemView.findViewById(R.id.report_subject_name);
            markTV = itemView.findViewById(R.id.report_mark);
            indicatorTV = itemView.findViewById(R.id.report_indicator);
            this.onReportListener = onReportListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onReportListener.onReportClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public ReportCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reportcard_cardview,parent,false);
        return new ViewHolder(view, onReportListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportCardAdapter.ViewHolder holder, int position) {
        ReportModel subjectReport = reportModelList.get(position);
        double mark = Math.round(subjectReport.getMark());
        int weighting = subjectReport.getAssessment_weighting();
        int percentage = (int) subjectReport.getPercentage();
        holder.subjectNameTV.setText(subjectReport.getSubject());
        holder.markTV.setText(String.valueOf(mark)+"/"+String.valueOf(weighting)+" | "+String.valueOf(percentage)+"%");
        if(percentage < studentAverage){
            holder.indicatorTV.setTextColor(Color.parseColor("#ff0000"));
            holder.indicatorTV.setText("\u25bc " + String.valueOf(studentAverage - percentage));
        }else if(percentage >  studentAverage){
            holder.indicatorTV.setTextColor(Color.parseColor("#00ff00"));
            holder.indicatorTV.setText("\u25b2");
        }
    }

    @Override
    public int getItemCount() {
        int a;
        if(reportModelList != null){
            a = reportModelList.size();
        }else{
            a=0;
        }
        return a;
    }

    //interface to detect clicks
    public interface OnReportListener {
        void onReportClick(int position);
    }
}
