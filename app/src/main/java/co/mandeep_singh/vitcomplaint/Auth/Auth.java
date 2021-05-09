package co.mandeep_singh.vitcomplaint.Auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;

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

import co.mandeep_singh.vitcomplaint.HomeActivity;
import co.mandeep_singh.vitcomplaint.HomeActivityWarden;
import co.mandeep_singh.vitcomplaint.MainActivity;
import co.mandeep_singh.vitcomplaint.Modal.UserModel;
import co.mandeep_singh.vitcomplaint.SplashActivity;


public class Auth{

    public String errorMessage = "";
    final FirebaseFirestore _firestore = FirebaseFirestore.getInstance();
    final FirebaseAuth _firebaseAuth = FirebaseAuth.getInstance();
    public static FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public static String id = user!=null ?  user.getUid() : null ;

    public String SignUp(String email, String password, boolean warden, Activity activity) {
        try {
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
        catch (Exception e){
            errorMessage = e.getMessage();
        }
    return errorMessage;
    }

    public String SignIn(String email, String password, boolean warden, Activity activity) {
        try {
                _firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener( activity, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = _firebaseAuth.getCurrentUser();
                                    id = user.getUid();
                                    errorMessage = "";
                                }
                            }
                        });
                return id;
        }
        catch (Exception e){
            errorMessage = e.getMessage();
        }
        return errorMessage;
    }


    public void signOut(){
        _firebaseAuth.signOut();
        id = null;
        user = null;
    }

    public void resetPassword(String email){
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

    public void CheckWardenOrStudent(SplashActivity splashActivity){
        final int[] returnValue = new int[1];
        returnValue[0] = 0;
        _firestore.collection("users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {
                        UserModel userModel = documentChange.getDocument().toObject(UserModel.class);
                        System.out.println(userModel.getUid() + " +++++++++++ " + id);
                        if(userModel.getUid().equals(id)){
                            returnValue[0] = userModel.isWarden() ? 1 : -1;
                            break;
                        }
                    }
                }
            }
        });
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(user != null ){
                    if(returnValue[0] == 0)
                        splashActivity.startActivity(new Intent(splashActivity, MainActivity.class));
                    if(returnValue[0] == 1)
                        splashActivity.startActivity(new Intent(splashActivity, HomeActivityWarden.class));
                    if(returnValue[0] == -1)
                        splashActivity.startActivity(new Intent(splashActivity, HomeActivity.class));
                }
                else{
                    splashActivity.startActivity(new Intent(splashActivity,MainActivity.class));
                }
                splashActivity.finish();
            }
        },4000);

    }

}
