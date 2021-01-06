package fl.futuerx.com.mutmanager.BackEnd;


import java.util.List;

import fl.futuerx.com.mutmanager.Models.Attendance;
import fl.futuerx.com.mutmanager.Models.Course;
import fl.futuerx.com.mutmanager.Models.Instructor;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface API_INTERFACE {

    @POST("login.php")//("latest_changed")
    Call<Instructor> loginWithCredential(@Body loginRequest request);

    @POST("get_courses.php")//("latest_changed")
    Call<List<Course>> getClasses(@Body apiRequestObject request);

    @POST("get_class_students.php")//("latest_changed")
    Call<List<Attendance>> getClassStudents(@Body apiRequestObject request);

    @POST("start_class.php")//("latest_changed")
    Call<List<Attendance>> startClass(@Body apiRequestObject request);

    @POST("update_attendance.php")//("latest_changed")
    Call<apiResponseObject> updateAttendace(@Body apiRequestObject request);

    @Multipart
    @POST("add_student.php")
    Call<apiResponseObject> submitOrderAllPart(
            @Part("id") RequestBody id
            , @Part("name") RequestBody name
            , @Part("email") RequestBody email
            , @Part("phone") RequestBody phone
            , @Part("tag_id") RequestBody tag_id
            , @Part MultipartBody.Part image);

}