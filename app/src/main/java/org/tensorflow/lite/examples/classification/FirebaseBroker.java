//package org.tensorflow.lite.examples.classification;
//
//import android.content.Context;
//import android.util.Log;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import org.tensorflow.lite.examples.classification.model.Plant;
//import org.tensorflow.lite.examples.classification.model.Post;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import androidx.annotation.NonNull;
//
//public class FirebaseBroker extends DatabaseBroker{
//    ArrayList<Post>  defaultPost = new ArrayList<>();
//    ArrayList<Plant>  defaultPlant = new ArrayList<>();
//
//    // post -----------------------------------------------------------------------
//    public void setPostOnDataBrokerListener(Context context, DatabaseBroker.OnDataBrokerListener onDataBrokerListener) {
//
//        postDataBrokerListener = onDataBrokerListener;
//
//        DatabaseReference databaseReferenceForPost = FirebaseDatabase.getInstance().getReference().child("post");
//        databaseReferenceForPost.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    Post post = snapshot.getValue(Post.class);
//                    postDatabase.add(post);
//                }
//
//                if (postDatabase == null) {
//                    postDatabase = defaultPost;
//                }
//
//                if (postDataBrokerListener != null) {
//                    postDataBrokerListener.onChange(postDatabase);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                postDatabase = defaultPost;
//            }
//        });
//    }
//
//    public ArrayList<String> loadPostDatabase(Context context) {
//
//        String[] postDatabase = postDatabase.split("@@@@");
//
//        ArrayList<String> arrayList = new ArrayList<>();
//        for (int i = 0; i < postDatabase.length; i++) {
//            if (postDatabase[i].length() == 0) continue;
//            arrayList.add(postDatabase[i]);
//        }
//
//        return arrayList;
//    }
//
//    public void savePostDatabase(Context context, ArrayList<String> postDatabase) {
//
//        postDatabase = "";
//        for (int i = 0; i < postDatabase.size(); i++) {
//            if (postDatabase.get(i).length() == 0) continue;
//            postDatabase += postDatabase.get(i);
//            if (i != postDatabase.size() - 1) {
//                postDatabase += "@@@@";
//            }
//        }
//
//        DatabaseReference databaseReferenceForPost = FirebaseDatabase.getInstance().getReference().child(rootPath).child("post");
//        databaseReferenceForPost.setValue(postDatabase);
//        databaseReferenceForPost.keepSynced(true);
//    }
//
//    // plant -----------------------------------------------------------------------
//    public void setPlantOnDataBrokerListener(Context context, DatabaseBroker.OnDataBrokerListener onDataBrokerListener){
//
//        plantOnDataBrokerListener = onDataBrokerListener;
//
//        DatabaseReference databaseReferenceForPost = FirebaseDatabase.getInstance().getReference().child(rootPath).child("plant");
//        databaseReferenceForPost.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.i("jmlee", "here1"+rootPath);
//                plantDatabase = dataSnapshot.getValue(Plant.class);
//                if (plantDatabase == null) {
//                    plantDatabase = defaultPlant;
//                }
//
//                if (plantOnDataBrokerListener != null) {
//                    plantOnDataBrokerListener.onChange(plantDatabase);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                Log.i("jmlee", "here2");
//                Log.i("jmlee", error.getMessage());
//                plantDatabase = defaultPlant;
//            }
//        });
//    }
//
//    public ArrayList<Plant> loadPlantDatabase(Context context){
//        String[] plantDatabase = plantDatabase.split("@@@@");
//        ArrayList<Plant>   arrayList = new ArrayList<>();
//
//
//        for(int i=0;i<plantDatabase.length;i++){
//            if(plantDatabase[i].length() == 0)  continue;
//            arrayList.add(new Plant(plantDatabase[i]));
//        }
//
//        return arrayList;
//    }
//
//    public void savePlantDatabase(Context context, ArrayList<Plant> plantDatabase){
//        plantDatabase = "";
//        for(int i=0;i<plantDatabase.size();i++){
//            plantDatabase += plantDatabase.get(i).toString();
//            if(i != plantDatabase.size()-1){
//                plantDatabase += "@@@@";
//            }
//        }
//        DatabaseReference databaseReferenceForPost = FirebaseDatabase.getInstance().getReference().child(rootPath).child("plant");
//        databaseReferenceForPost.setValue(plantDatabase);
//        databaseReferenceForPost.keepSynced(true);
//    }
//
//    public void setCheckDatabaseRoot(DatabaseBroker.OnDataBrokerListener onDataBrokerListener){
//
//        checkOnDataBrokerListener = onDataBrokerListener;
//        Log.i("jmlee", "here");
//        DatabaseReference databaseReferenceForPost = FirebaseDatabase.getInstance().getReference().child("databaseRoot");
//        databaseReferenceForPost.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                String databaseRootStr = dataSnapshot.getValue(String.class);
//
//                String[] databaseRoots = databaseRootStr.split("@@@@");
//                for (int i = 0; i < databaseRoots.length; i++) {
//                    if (rootPath.equals(databaseRoots[i])) {
//                        if (checkOnDataBrokerListener != null) {
//                            checkOnDataBrokerListener.onChange(databaseRoots[i]);
//                            return;
//                        }
//                    }
//                }
//
//                if (checkOnDataBrokerListener != null) {
//                    checkOnDataBrokerListener.onChange("");
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                if (checkOnDataBrokerListener != null) {
//                    checkOnDataBrokerListener.onChange("");
//                }
//            }
//        });
//    }
//
//
//    public void resetDatabase(Context context){
//
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(rootPath);
//        databaseReference.removeValue();
//
//    }
//
//}
