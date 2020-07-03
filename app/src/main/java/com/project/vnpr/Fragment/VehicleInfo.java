package com.project.vnpr.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.vnpr.R;

import java.util.HashMap;
import java.util.Map;

public class VehicleInfo extends Fragment {

    private static final String TAG = "VehicleInfo";
    Handler handler=new Handler();
    public static final String KEY_VEHICLE_NUMBER = "VehicleNumber";
    public static final String KEY_VEHICLE_OWNER = "VehicleOwner";
    public static final String KEY_VEHICLE_TYPE = "VehicleType";
    public static final String KEY_PHONE_NUMBER  = "PhoneNumber";
    public static final String KEY_VEHICLE_MODEL =  "VehicleModel";
    public static final String KEY_FUEL_TYPE = "FuelType";

    private EditText etVehicleNumber;
    private EditText etVehicleOwner;
    private EditText etVehicleType;
    private EditText etPhoneNumber;
    private EditText etVehicleModel;
    private EditText etFuelType;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         View v = inflater.inflate(R.layout.fragment_vehicle_info1, container, false);
        Button btnSubmit = v.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener((v1)->{
            saveNote();
        });
        etVehicleNumber = v.findViewById(R.id.etVehicleNumber);
        etVehicleOwner = v.findViewById(R.id.etVehicleOwner);
        etVehicleType = v.findViewById(R.id.etVehicleType);
        etPhoneNumber = v.findViewById(R.id.etPhoneNumber);
        etVehicleModel = v.findViewById(R.id.etVehicleModel);
        etFuelType = v.findViewById(R.id.etFuelType);

        return v;
    }


    public void saveNote(){
        String VehicleNumber = etVehicleNumber.getText().toString();
        String VehicleOwner = etVehicleOwner.getText().toString();
        String VehicleType = etVehicleType.getText().toString();
        String PhoneNumber = etPhoneNumber.getText().toString();
        String VehicleModel = etVehicleModel.getText().toString();
        String FuelType = etFuelType.getText().toString();

        Map<String,Object> note = new HashMap<>();
        note.put(KEY_VEHICLE_NUMBER,VehicleNumber);
        note.put(KEY_VEHICLE_OWNER,VehicleOwner);
        note.put(KEY_VEHICLE_TYPE,VehicleType);
        note.put(KEY_PHONE_NUMBER,PhoneNumber);
        note.put(KEY_VEHICLE_MODEL,VehicleModel);
        note.put(KEY_FUEL_TYPE,FuelType);
        //db.document("Vehicle/Vehicle Information");
        db.collection("Vehicle").document(VehicleNumber).set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                       handler.post(()->{
                           Toast.makeText(getContext(),"Information Saved",Toast.LENGTH_LONG).show();
                       });
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        handler.post(()->{
                            Toast.makeText(getContext(),"error occurred",Toast.LENGTH_LONG).show();
                        });
                    }
                });
    }

    
}
