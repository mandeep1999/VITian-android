package co.mandeep_singh.vitcomplaint.Modal;
import com.google.firebase.firestore.Exclude;
import javax.annotation.Nonnull;

public class StudentId {
    @Exclude
    public String FriendsId;

    public <T extends StudentId> T withId(@Nonnull final String id){
        this.FriendsId = id;
        return (T) this;
    }
}
