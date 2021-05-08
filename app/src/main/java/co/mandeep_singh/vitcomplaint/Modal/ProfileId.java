package co.mandeep_singh.vitcomplaint.Modal;

import com.google.firebase.firestore.Exclude;

import javax.annotation.Nonnull;

public class ProfileId {
    @Exclude
    public String ProfileId;

    public <T extends ProfileId> T withId(@Nonnull final String id){
        this.ProfileId= id;
        return (T) this;
    }
}
