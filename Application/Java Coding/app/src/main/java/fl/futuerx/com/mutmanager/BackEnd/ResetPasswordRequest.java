package fl.futuerx.com.mutmanager.BackEnd;


public class ResetPasswordRequest {

    public int user_id;
    public String old_password;
    public String new_password;

    public ResetPasswordRequest(int user_id, String old_password, String new_password) {
        this.user_id = user_id;
        this.old_password = old_password;
        this.new_password = new_password;
    }
}
