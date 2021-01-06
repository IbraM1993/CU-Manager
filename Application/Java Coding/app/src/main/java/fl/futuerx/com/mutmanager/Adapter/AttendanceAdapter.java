package fl.futuerx.com.mutmanager.Adapter;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import fl.futuerx.com.mutmanager.BackEnd.MUT_ATTENDANCE_API;
import fl.futuerx.com.mutmanager.Interfaces.DelegateCall;
import fl.futuerx.com.mutmanager.Models.Attendance;
import fl.futuerx.com.mutmanager.R;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {

    private List<Attendance> attendanceList;
    private final DelegateCall<Attendance> onRowClick;
    private final Context context;

    public AttendanceAdapter(Context context, List<Attendance> attendanceList, DelegateCall<Attendance> onRowClick) {
        this.attendanceList = attendanceList;
        this.onRowClick = onRowClick;
        this.context = context;
    }

    public Attendance getItemByTag(String tag) {
        for(Attendance a : getAttendanceList()){
            if(a.student_tag.equals(tag)){
                return a;
            }
        }
        return null;
    }

    final class AttendanceViewHolder extends RecyclerView.ViewHolder {
        public final ImageView icon;
        public final TextView name;
        public final TextView status;
        public AttendanceViewHolder(View itemView) {
            super(itemView);
            icon     = itemView.findViewById(R.id.row_student_img);
            name     = itemView.findViewById(R.id.row_student_code);
            status   = itemView.findViewById(R.id.row_student_status);
            if(onRowClick != null){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View v = view.findViewById(R.id.row_student_code);
                        if(v.getTag() != null && v.getTag() instanceof Attendance){
                            onRowClick.Invoke((Attendance) v.getTag());
                        }
                    }
                });
            }
        }
    }

    @Override
    public AttendanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AttendanceViewHolder(LayoutInflater.from(context).inflate(R.layout.row_student, parent , false));
    }

    @Override
    public void onBindViewHolder(AttendanceViewHolder holder, int position) {
        Attendance c = getAttendanceList().get(position);
        holder.name.setText(c.student_name);
        if(c.status == 1 ){
            holder.name.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.status.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.vc_is_here, 0,0,0);
            holder.status.setText(context.getString( R.string.attendance_in ));
        } else {
            holder.name.setTextColor(context.getResources().getColor(R.color.colorAccent));
            holder.status.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.vc_not_here, 0,0,0);
            holder.status.setText(context.getString(R.string.attendance_out));
        }
        Glide.with(context).load(MUT_ATTENDANCE_API.MUT_ATTENDANCE_SERVER_PRODUCTION+c.student_icon).error(R.drawable.ic_face_black_24dp).into(holder.icon);
        holder.name.setTag(c);
    }

    @Override
    public int getItemCount() {
        return getAttendanceList().size();
    }

    public List<Attendance> getAttendanceList() {
        if(attendanceList == null){
            attendanceList = new ArrayList<>();
        }
        return attendanceList;
    }

    public void updateItem(Attendance item) {
        for(Attendance s : getAttendanceList()){
            if(item.student_id == s.student_id){
                s.status = item.status;
                notifyDataSetChanged();
                return;
            }
        }
    }

}
