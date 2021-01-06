package fl.futuerx.com.mutmanager.BackEnd;

public class Constants {
    private static final Constants ourInstance = new Constants();

    public static Constants getInstance() {
        return ourInstance;
    }

    private Constants() {
    }

    public final int PLACE_STATUS_PENDING  = 1;
    public final int PLACE_STATUS_APPROVED = 2;
    public final int PLACE_STATUS_REJECTED = 3;
}
