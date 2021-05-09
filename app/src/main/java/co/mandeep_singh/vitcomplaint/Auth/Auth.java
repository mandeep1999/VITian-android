package co.mandeep_singh.vitcomplaint.Auth;

import android.app.Activity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.HashMap;
import java.util.Map;
import co.mandeep_singh.vitcomplaint.Modal.EmailModel;
import co.mandeep_singh.vitcomplaint.Modal.UserModel;


public class Auth{

    public String errorMessage = "";
    final FirebaseFirestore _firestore = FirebaseFirestore.getInstance();
    final FirebaseAuth _firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user;
    public static String id = "";

    public String SignUp(String email, String password, boolean warden, Activity activity) {
        try {
        final boolean[] ans = new boolean[1];
        _firebaseAuth.signInAnonymously().addOnCompleteListener(activity, task -> {
            user = _firebaseAuth.getCurrentUser();
           ans[0] = checkEmail(email);  //check email is of warden if true then its of warden
            user.delete();
        });
            if (warden == ans[0]) {
                _firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = _firebaseAuth.getCurrentUser();
                                    user.sendEmailVerification();
                                    setUser(warden, email, user.getUid());
                                    id = user.getUid();
                                    errorMessage = "";
                                }
                            }
                        });
                return id;
            }
            else
                {
                errorMessage = "You are not authorized";
            }
        }
        catch (Exception e){
            errorMessage = e.getMessage();
        }
    return errorMessage;
    }

    public String SignIn(String email, String password, boolean warden, Activity activity) {
        _firebaseAuth.signInAnonymously();
        user = _firebaseAuth.getCurrentUser();
        boolean ans = checkAdmin(email); //check email is of warden if true then its of warden
        user.delete();
        try {

            if (warden == ans) {
                _firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener( activity, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = _firebaseAuth.getCurrentUser();
                                    user.sendEmailVerification();
                                    setUser(warden, email, user.getUid());
                                    id = user.getUid();
                                    errorMessage = "";
                                }
                            }
                        });
                return id;
            }
            else
                {
                errorMessage = "You are not authorized";
            }
        }
        catch (Exception e){
            errorMessage = e.getMessage();
        }
        return errorMessage;
    }


    void signOut(){
        _firebaseAuth.signOut();
    }

    void resetPassword(String email){
        _firebaseAuth.sendPasswordResetEmail(email);
    }

    void setUser(boolean warden, String email, String uid){
        Map<String, Object> UserMap = new HashMap<>();
        UserMap.put("email",email);
        UserMap.put("warden",warden);
        UserMap.put("uid",uid);
        _firestore.collection("users").add(UserMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {

            }
        });

    }

    boolean checkAdmin(String email){
        final boolean[] returnValue = {false};
        _firestore.collection("users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange documentChange : value.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        UserModel userModel = documentChange.getDocument().toObject(UserModel.class);
                        if (email.trim().equals(userModel.getEmail().trim())) {
                            returnValue[0] = userModel.isWarden();
                            break;
                        }
                    }
                }
            }
        });
        return  returnValue[0];
    }

    public  boolean  checkEmail(@org.jetbrains.annotations.NotNull String email) {
        final Boolean returnValue[] = {false};
        _firestore.collection("emails").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange documentChange : value.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        EmailModel emailModel = documentChange.getDocument().toObject(EmailModel.class);
                        returnValue[0] = emailModel.getEmail().trim().equals(email.trim());
                        break;
                    }
                }
            }
        });
        return  returnValue[0];
    }
}
