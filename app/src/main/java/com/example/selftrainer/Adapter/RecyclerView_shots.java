package com.example.selftrainer.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.selftrainer.Model.Posts;
import com.example.selftrainer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RecyclerView_shots extends RecyclerView.Adapter<RecyclerView_shots.MyViewHolder> {
    private ArrayList<Posts> arrayList;
    private Context context;
    private ArrayList<DocumentSnapshot> documentSnapshots;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    String UserId = user.getUid();

    public RecyclerView_shots(ArrayList<Posts> arrayList, Context context, ArrayList<DocumentSnapshot> documentSnapshots) {
        this.documentSnapshots = documentSnapshots;
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shot,null,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Posts data = arrayList.get(position);
        String document = documentSnapshots.get(position).getId();

        holder.Question.setText(data.getQuestion());
        holder.videoView.setVideoPath(data.getVideoUri());


        holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                float videoRatio = mp.getVideoWidth()/(float) mp.getVideoHeight();
                float screenratio = holder.videoView.getWidth()/(float) holder.videoView.getHeight();
                float scale = videoRatio/screenratio;
                if (scale>=1f){
                    holder.videoView.setScaleX(scale);
                }
                else{
                    holder.videoView.setScaleY(1f/scale);
                }
            }
        });
        holder.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
            }
        });

       holder.dislike.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Map<String, Object> likeData = new HashMap<>();
               likeData.put("liked", true); // Add appropriate fields and values

               // Set the data in Firestore
               db.collection("Posts")
                       .document(document)
                       .collection("Like")
                       .document(UserId)
                       .set(likeData) // Use the map or object
                       .addOnSuccessListener(aVoid -> {
                           holder.dislike.setImageResource(R.drawable.like); // Update UI on success
                       })
                       .addOnFailureListener(e -> {
                           e.printStackTrace();
                       });
           }
       });
        db.collection("Posts").document(document).collection("Like").get().addOnSuccessListener(querySnapshot -> {
                    ArrayList<DocumentSnapshot> likeby = new ArrayList<>(querySnapshot.getDocuments());
                    int count=0;
                    for(DocumentSnapshot doc:likeby){
                        String likebyuser = doc.getId();
                        count++;
                        if(Objects.equals(UserId, likebyuser)){
                            holder.dislike.setImageResource(R.drawable.like);
                        }
                    }
                    holder.likecount.setText(String.valueOf(count));

                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        VideoView videoView;
        TextView Question,likecount;
        ImageView like, dislike;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoView);
            Question = itemView.findViewById(R.id.Question_re);
            likecount =itemView.findViewById(R.id.likecount);
            dislike = itemView.findViewById(R.id.dislike);
        }
    }
}
