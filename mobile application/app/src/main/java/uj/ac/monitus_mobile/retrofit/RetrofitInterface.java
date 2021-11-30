package uj.ac.monitus_mobile.retrofit;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import uj.ac.monitus_mobile.data.model.MarksModel;
import uj.ac.monitus_mobile.data.model.NewsletterModel;
import uj.ac.monitus_mobile.data.model.PDFassessmentModel;
import uj.ac.monitus_mobile.data.model.ReportModel;
import uj.ac.monitus_mobile.data.model.StatsModel;
import uj.ac.monitus_mobile.data.model.SubjectModel;
import uj.ac.monitus_mobile.ui.login.LoginResult;

/**
 * Retrofit endpoint: encode the details the
 * of the parameters and request methods (POST,GET,PUT,POST).
 * Each call from the implementation of this interface can
 * make a synchronous or asynchronous HTTP request to the
 * webserver(API/webservice)
 * check: 'https://square.github.io/retrofit/' for more info
 */
public interface RetrofitInterface {
    /**
     * POST request: login. method prototype to login a specific
     * user using inputted data into API
     * @return LoginResult object: stores the returned user info
     * @param username : username, user inputted string value for username
     * @param password  : password, user inputted string value for password
     */
    @POST("/api/login/{username}/{password}")
    Call<LoginResult> executeLogin(@Path("username") String username,
                                   @Path("password") String password);

    /**
     * GET request: retrieve all newsletters. method prototype to get
     * @return List<NewsletterModel> : list of all newsletters
     */
    @GET("/api/newsletters")
    Call<List<NewsletterModel>> listNewsletters();

    /**
     * GET request: retrieves all subjects for student
     * @return List<SubjectModel> return a list of subjects with specific grade
     */
    @GET("/api/subjects/student/{student_id}")
    Call<List<SubjectModel>> listSubjects(@Path("student_id") int student_id);

    /**
     * GET request: retrieves all marks for assessments belonging
     * to particular subject
     * @param student_id the student id
     * @param subject_id the subject that will retrieve all marks for
     * @return
     */
    @GET("/api/submissions/{student_id}/{subject_id}")
    Call<List<MarksModel>> listMarks(@Path("student_id") int student_id,
                                     @Path("subject_id") int subject_id);

    /**
     * GET request: retrieves all assignment for particular subject
     * @return List<AssessmentModel> return a list of assessments for specific subject
     */
    @GET("/api/pdfassessments/{subject_id}")
    Call<List<PDFassessmentModel>> listAssessments(@Path("subject_id") int subject_id);

    /**
     * GET request: retrieves specific pdf assessment for particular subject
     * @return binary pdf file in with assessment information
     */
    @GET("/api/downloadPDF/{PDF_File}")
    Call<ResponseBody> downloadAssessment(@Path("PDF_File") String pdf_file);

    /**
     * POST request method: to upload pdf document to api
     * @param file the pdf file to be uploaded
     * @param name the name of the control on the api
     * @param student_id the student currently submitting
     * @param assessment_id the assessment to submit to
     * @return response code (200 success) or (400 failed)
     */
    @Multipart
    @POST("/upload")
    Call<ResponseBody> submit(
            @Part MultipartBody.Part file,
            @Part("upload") RequestBody name,
            @Part("student_id") int student_id,
            @Part("assessment_id") int assessment_id
    );

    /**
     * GET request method: to retrieve the comparative calculated student averages in assessments
     * @param subject_id the subject to retrieve data for
     * @return a list of calculated averages
     */
    @GET("/api/report/{subject_id}")
    Call<List<StatsModel>> reportStats(@Path("subject_id") int subject_id);

    @GET("/api/reportcard/{student_id}")
    Call<List<ReportModel>> getreportCard(@Path("student_id") int student_id);
}
