package co.mandeep_singh.vitcomplaint.Modal;
import com.google.firebase.firestore.Exclude;
import javax.annotation.Nonnull;

public class HomeId {
    @Exclude
    public String HomeId;

    public <T extends HomeId> T withId(@Nonnull final String id){
        this.HomeId = id;
        return (T) this;
    }
}
