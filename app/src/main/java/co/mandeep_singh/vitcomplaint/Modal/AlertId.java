package co.mandeep_singh.vitcomplaint.Modal;

import com.google.firebase.firestore.Exclude;

import javax.annotation.Nonnull;

public class AlertId {
    @Exclude
    public String AlertId;

    public <T extends AlertId> T withId(@Nonnull final String id){
        this.AlertId = id;
        return (T) this;
    }
}
