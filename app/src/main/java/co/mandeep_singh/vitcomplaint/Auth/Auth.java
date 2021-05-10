package co.mandeep_singh.vitcomplaint.Auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.HashMap;
import java.util.Map;
import co.mandeep_singh.vitcomplaint.HomeActivity;
import co.mandeep_singh.vitcomplaint.HomeActivityWarden;
import co.mandeep_singh.vitcomplaint.MainActivity;
import co.mandeep_singh.vitcomplaint.Modal.ProfileModel;
import co.mandeep_singh.vitcomplaint.Modal.UserModel;
import co.mandeep_singh.vitcomplaint.SplashActivity;


public class Auth{

    static final FirebaseFirestore _firestore = FirebaseFirestore.getInstance();
    final FirebaseAuth _firebaseAuth = FirebaseAuth.getInstance();
    public static FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public static String id = user!=null ?  user.getUid() : null ;
    public static int wardenAuth = 0;
    public static String block = null;
    public static  String roomNo = null;

    public void SignUp(String email, String password, boolean warden, Activity activity, ProgressBar progressBar) {
        try {
                _firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    user = _firebaseAuth.getCurrentUser();
                                    user.sendEmailVerification();
                                    setUser(warden, email, user.getUid());
                                    id = user.getUid();
                                    Toast.makeText(activity, "Please check your email" ,Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(activity, task.getException().getMessage() ,Toast.LENGTH_LONG).show();
                                }
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });

        }
        catch (Exception e){
            Toast.makeText(activity, e.getMessage() ,Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.INVISIBLE);
        }

    }

    public void SignIn(String email, String password, boolean warden, Activity activity, ProgressBar progressBar) {
        try {
                _firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener( activity, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    user = _firebaseAuth.getCurrentUser();
                                    id = user.getUid();
                                    if(user.isEmailVerified()){
                                    Intent i;
                                    if (warden) {
                                        i = new Intent(activity, HomeActivityWarden.class);
                                        wardenAuth = 1;
                                    } else {
                                        i = new Intent(activity, HomeActivity.class);
                                        wardenAuth = -1;
                                    }
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    activity.startActivity(i);
                                }else {
                                        Toast.makeText(activity, "Verify your email" ,Toast.LENGTH_LONG).show();
                                        user.sendEmailVerification();
                                    }
                                }
                                else {
                                    Toast.makeText(activity, task.getException().getMessage() ,Toast.LENGTH_LONG).show();
                                }
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });
        }
        catch (Exception e){
            Toast.makeText(activity, e.getMessage() ,Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.INVISIBLE);
        }
    }


    public void signOut(){
        _firebaseAuth.signOut();
        id = null;
        user = null;
        wardenAuth = 0;
        block = null;
        roomNo = null;
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
                if(user != null && user.isEmailVerified()){
                    if(returnValue[0] == 0){
                        splashActivity.startActivity(new Intent(splashActivity, MainActivity.class));
                        wardenAuth = 0;
                    }
                    if(returnValue[0] == 1){
                        wardenAuth = 1;
                        splashActivity.startActivity(new Intent(splashActivity, HomeActivityWarden.class));
                    }
                    if(returnValue[0] == -1){
                        wardenAuth = -1;
                        splashActivity.startActivity(new Intent(splashActivity, HomeActivity.class));
                    }
                }
                else{
                    splashActivity.startActivity(new Intent(splashActivity,MainActivity.class));
                    wardenAuth = 0;
                }
                splashActivity.finish();
            }
        },4000);

    }


}
