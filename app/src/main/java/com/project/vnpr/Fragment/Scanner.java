package com.project.vnpr.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.project.vnpr.Activity.Details;
import com.project.vnpr.R;
import com.project.vnpr.ScView;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;
import static com.project.vnpr.Fragment.VehicleInfo.KEY_FUEL_TYPE;
import static com.project.vnpr.Fragment.VehicleInfo.KEY_PHONE_NUMBER;
import static com.project.vnpr.Fragment.VehicleInfo.KEY_VEHICLE_MODEL;
import static com.project.vnpr.Fragment.VehicleInfo.KEY_VEHICLE_NUMBER;
import static com.project.vnpr.Fragment.VehicleInfo.KEY_VEHICLE_OWNER;
import static com.project.vnpr.Fragment.VehicleInfo.KEY_VEHICLE_TYPE;

public class Scanner extends Fragment {
    SurfaceView surface;
    Retrofit retrofit;
    static public boolean detected=false;
    View main;
    String half = "";
    boolean working = false;
    SurfaceHolder.Callback callback;
    CameraSource source;
    ScView scView;
    FirebaseFirestore firestore;
    String home_url = "https://parivahan.gov.in/rcdlstatus/";
    String post_url = "https://parivahan.gov.in/rcdlstatus/vahan/rcDlHome.xhtml";
    Handler handler = new Handler();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        main = inflater.inflate(R.layout.fragment_scanner, container, false);
        ((CoordinatorLayout) main).addView(scView=new ScView(getContext()));
        surface = main.findViewById(R.id.surface);
        firestore = FirebaseFirestore.getInstance();

        callback = new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                TextRecognizer recognizer = new TextRecognizer.Builder(getContext())
                        .build();
                recognizer.setProcessor(new Detector.Processor<TextBlock>() {
                    @Override
                    public void release() {

                    }

                    @Override
                    public void receiveDetections(Detector.Detections<TextBlock> detections) {
                        SparseArray<TextBlock> array = detections.getDetectedItems();
                        Pattern p = Pattern.compile("^[A-Z]{2}[0-9]{2}[A-Z]{1,2}[0-9]{4}$");
                        Pattern p2 = Pattern.compile("^[A-Z]{2}[0-9]{2}[A-Z]{1,2}$");
                        Pattern p3 = Pattern.compile("^[0-9]{4}$");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                handler.post(()->{
                                    detected=true;
                                    scView.invalidate();
                                });
                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                handler.post(()->{
                                   detected=false;
                                   scView.invalidate();
                               });

                            }
                        }).start();

                        if (array != null)
                            for (int i = 0; i < array.size(); i++) {

                                if (array.get(i) != null) {

                                    //[A-Z]{2}[ -][0-9]{1,2}(?: [A-Z])?(?: [A-Z]*)? [0-9]{4}
                                    String s = array.get(i).getValue().trim();
                                    if (!s.startsWith("O"))
                                        s = s.replaceAll("[O]", "0");
                                    s = s.replaceAll("[-]", "");
                                    if (10 - half.length() >= s.length()) {
                                        System.out.println("Scan " + s);
                                        Matcher m = p.matcher(s);
                                        Matcher m2 = p2.matcher(s);
                                        Matcher m3 = p3.matcher(s);
                                        if (s.length() == 10 && m.find())
                                            findVehicle(s);

                                        else if (m2.find()) {
                                            half = s;
                                            System.out.println("scan half " + half);
                                        } else if (m3.find()) {
                                            findVehicle(half + s);
                                            half = "";
                                        }
                                    }
                                }
                            }

                    }
                });
                source = new CameraSource.Builder(getActivity(), recognizer).build();

                try {
                    source.start(holder);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                source.stop();
            }
        };
        surface.getHolder().addCallback(callback);
        return main;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 23 && resultCode == RESULT_OK) {

        } else if (requestCode == 100)
            working = false;
    }

    public void findVehicle(String s) {
        handler.post(() -> {
            Toast.makeText(getContext(), "Car num " + s, Toast.LENGTH_SHORT).show();
//                                                    });
//                                              Retrofit retrofit=new Retrofit.Builder()
//                                                      .baseUrl(home_url)
//                                                      .build();
//                                              Request request=new Request.Builder()
//                                                      .method("GET",null)
//                                                      .build();
//                                              retrofit.callFactory().newCall(request).enqueue(new Callback() {
//                                                  @Override
//                                                  public void onFailure(Call call, IOException e) {
//
//                                                  }
//
//                                                  @Override
//                                                  public void onResponse(Call call, Response response) throws IOException {
//
//                                                  }
        });
        firestore.collection("Vehicle").document(s).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                Map<String, Object> map = documentSnapshot.getData();
                if (working)
                    return;
                if (map != null) {
                    working = true;
                    Intent intent = new Intent(getContext(), Details.class);
                    intent.putExtra(KEY_VEHICLE_NUMBER, map.get(KEY_VEHICLE_NUMBER).toString());
                    intent.putExtra(KEY_VEHICLE_OWNER, map.get(KEY_VEHICLE_OWNER).toString());
                    intent.putExtra(KEY_VEHICLE_TYPE, map.get(KEY_VEHICLE_TYPE).toString());
                    intent.putExtra(KEY_PHONE_NUMBER, map.get(KEY_PHONE_NUMBER).toString());
                    intent.putExtra(KEY_FUEL_TYPE, map.get(KEY_FUEL_TYPE).toString());
                    intent.putExtra(KEY_VEHICLE_MODEL, map.get(KEY_VEHICLE_MODEL).toString());
                    startActivityForResult(intent, 100);
                }
            }
        });
        System.out.println("Scan mali " + s);
    }
}
