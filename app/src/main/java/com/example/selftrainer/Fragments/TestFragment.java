package com.example.selftrainer.Fragments;

import static androidx.core.content.ContextCompat.getMainExecutor;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.selftrainer.Interview;
import com.example.selftrainer.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.FirebaseApp;
import com.google.firebase.vertexai.FirebaseVertexAI;
import com.google.firebase.vertexai.GenerativeModel;
import com.google.firebase.vertexai.java.GenerativeModelFutures;
import com.google.firebase.vertexai.type.Content;
import com.google.firebase.vertexai.type.GenerateContentResponse;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TestFragment extends Fragment {
TextInputEditText role,exp;
Button genrate, start;
    private static final String MARKER_START = "*Start*";
    private static final String MARKER_END = "*End*";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TestFragment newInstance(String param1, String param2) {
        TestFragment fragment = new TestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        if (FirebaseApp.getApps(requireContext()).isEmpty()) {
            FirebaseApp.initializeApp(getContext());
            Log.d("Firebase", "Firebase initialized.");
        } else {
            Log.d("Firebase", "Firebase already initialized.");
        }

        View view =inflater.inflate(R.layout.fragment_test, container, false);

        role = view.findViewById(R.id.role_in_job);
        exp = view.findViewById(R.id.experince);
        genrate = view.findViewById(R.id.Generate_question);
        start = view.findViewById(R.id.Start_test);
        genrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Make sure Firebase is initialized
                if (FirebaseApp.getApps(requireContext()).isEmpty()) {
                    FirebaseApp.initializeApp(requireContext());
                    Log.d("Firebase", "Firebase initialized.");
                }

                String roletext = role.getText().toString();
                String exptext = exp.getText().toString();
                GenerativeModel gm = FirebaseVertexAI.getInstance()
                        .generativeModel("gemini-1.5-flash-preview-0514");
                GenerativeModelFutures model = GenerativeModelFutures.from(gm);

                // Provide a prompt to generate text
                Content prompt = new Content.Builder()
                        .addText("give me the one question on " + roletext + " for experinece of "+exptext+" and also highlight the question using *Start*and*End*")
                        .build();

                // Call to generate content asynchronously
                ListenableFuture<GenerateContentResponse> response = model.generateContent(prompt);
                Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
                    @Override
                    public void onSuccess(GenerateContentResponse result) {
                        start.setVisibility(View.VISIBLE);
                        if (result.getText().isEmpty()) {
                            Log.d("data", "empty data");
                        }
                        String resultText = result.getText();
                        String a = trim(resultText);
                        Log.d("data", "onSuccess: " + resultText);
                        Log.d("data", "onSuccess: " + a);
                        start.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getContext(), Interview.class);
                                intent.putExtra("Question",a);
                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        t.printStackTrace();
                        Log.e("data", "Error in generating content", t);
                    }
                },getMainExecutor(getContext()));
            }
        });


        return view;
    }
    public static String trim(String q) {
        int startIndex = q.indexOf(MARKER_START);
        int endIndex = q.indexOf(MARKER_END);

        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            return q.substring(startIndex + MARKER_START.length(), endIndex).trim();
        }
        return q; // Return original string if markers are not found or invalid
    }

}