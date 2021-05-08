package co.mandeep_singh.vitcomplaint.Modal;

import com.google.firebase.firestore.Exclude;

import javax.annotation.Nonnull;

public class RequestId {
    @Exclude
    public String RequestId;

    public <T extends RequestId> T withId(@Nonnull final String id){
        this.RequestId = id;
        return (T) this;
    }
}
