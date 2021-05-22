//package org.tensorflow.lite.examples.classification;
//
//import android.content.Context;
//
//import org.tensorflow.lite.examples.classification.model.Plant;
//import org.tensorflow.lite.examples.classification.model.Post;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public abstract class DatabaseBroker {
//
//    public interface OnDataBrokerListener{
//        public void onChange(ArrayList<Object> database);
//    }
//
//    // post -----------------------------------------------------------------------
//    protected ArrayList<Post> postDatabase = new ArrayList<>();
//    protected OnDataBrokerListener postDataBrokerListener = null;
//
//    // plant -----------------------------------------------------------------------
//    protected ArrayList<Plant> plantDatabase = new ArrayList<>();
//    protected OnDataBrokerListener plantOnDataBrokerListener = null;
//
//    // database -----------------------------------------------------------------------
//    OnDataBrokerListener checkOnDataBrokerListener = null;
//
//    public abstract  void setPostOnDataBrokerListener(Context context, OnDataBrokerListener onDataBrokerListener);
//    public abstract ArrayList<Post> loadPostDatabase(Context context);
//    public abstract void savePostDatabase(Context context, ArrayList<Post> postDatabase);
//
//    public abstract  void setPlantOnDataBrokerListener(Context context, OnDataBrokerListener onDataBrokerListener);
//    public abstract ArrayList<Plant> loadPlantDatabase(Context context);
//    public abstract void savePlantDatabase(Context context, ArrayList<Plant> userDatabase);
//
//    public abstract void setCheckDatabaseRoot(OnDataBrokerListener onDataBrokerListener);
//    public abstract void resetDatabase(Context context);
//
//
//    public  String rootPath = "";
//    public static DatabaseBroker createDatabaseObject(String rootPath){
//        DatabaseBroker databaseBroker = new FirebaseBroker();    // it will be replaced as FirebaseBroker
//
//        databaseBroker.rootPath = rootPath;
//        return databaseBroker;
//    }
//}
//
