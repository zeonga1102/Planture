package org.tensorflow.lite.examples.classification.view.community;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import org.tensorflow.lite.examples.classification.R;
import org.tensorflow.lite.examples.classification.databinding.ItemCommunityRecyclerviewBinding;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class CommunityRecyclerViewAdapter extends RecyclerView.Adapter {
    private ArrayList<CommunityDTO> communityDTOs;
    private ArrayList<String> communityUidList;

    private Context ctx;

    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;

    private Dialog dialog;

    CommunityRecyclerViewAdapter(Context ctx) {
        communityDTOs = new ArrayList<>();
        communityUidList = new ArrayList<>();

        this.ctx = ctx;

        // Firebase storage
        firebaseStorage = FirebaseStorage.getInstance();
        // Firebase Database
        firebaseDatabase = FirebaseDatabase.getInstance();
        // Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseDatabase.getReference().child("images").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                communityDTOs.clear();
                communityUidList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    communityDTOs.add(snapshot.getValue(CommunityDTO.class));
                    communityUidList.add(snapshot.getKey());
                }
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_community_recyclerview, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemCommunityRecyclerviewBinding binding = ((ViewHolder) holder).getBinding();

        binding.textComUserName.setText(communityDTOs.get(position).userId);

        Glide.with(holder.itemView.getContext())
                .load(communityDTOs.get(position).imageUrl)
                .thumbnail(0.2f)
                .into(binding.imgComPic);

        binding.textComExplain.setText(communityDTOs.get(position).explain);

        binding.imgComFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favoriteEvent(position);
            }
        });

        if (communityDTOs.get(position).favorites.containsKey(firebaseAuth.getCurrentUser().getUid()))
            binding.imgComFavorite.setImageResource(R.drawable.favorite);
        else
            binding.imgComFavorite.setImageResource(R.drawable.favorite_mid_border);

        binding.textComFavoriteCount.setText("좋아요 " + communityDTOs.get(position).favoriteCount + "개");

        //글을 작성한 유저와 현재 유저가 다르면 메뉴 보여주지 않음
        if (!communityDTOs.get(position).uid.equals(firebaseAuth.getCurrentUser().getUid())) {
            binding.imgBtnCom.setVisibility(View.INVISIBLE);
            binding.imgBtnCom.setEnabled(false);
        }

        binding.imgBtnCom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemMenuEvent(binding.imgBtnCom, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return communityDTOs.size();
    }

    private void itemMenuEvent(ImageButton imgBtn, int position) {
        PopupMenu popup = new PopupMenu(ctx, imgBtn);
        popup.inflate(R.menu.community_item_option_menu);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.delete_option_com:
                        dialog = new Dialog(ctx);
                        dialog.setContentView(R.layout.dialog_community_delete);
                        showDeleteDialog(position);
                        break;
                }
                return false;
            }
        });
        popup.show();
    }

    private void showDeleteDialog(int position) {

        dialog.show();

        Button btnDelete = dialog.findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //게시글 삭제
                firebaseStorage.getReference().child("images").child(communityDTOs.get(position).imgFileName).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        firebaseDatabase.getReference().child("images").child(communityUidList.get(position)).removeValue();
                        communityDTOs.remove(position);
                        communityUidList.remove(position);
                        Toast.makeText(ctx, "삭제 완료", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ctx, "삭제 실패", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.dismiss();
            }
        });

        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void favoriteEvent(int position) {
        firebaseDatabase.getReference("images").child(communityUidList.get(position)).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                CommunityDTO communityDTO = mutableData.getValue(CommunityDTO.class);

                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                if (communityDTO == null) {
                    return Transaction.success(mutableData);
                }
                if (communityDTO.favorites.containsKey(uid)) {
                    //좋아요 해제
                    communityDTO.favoriteCount = communityDTO.favoriteCount - 1;
                    communityDTO.favorites.remove(uid);
                } else {
                    //좋아요
                    communityDTO.favoriteCount = communityDTO.favoriteCount + 1;
                    communityDTO.favorites.put(uid, true);
                }
                mutableData.setValue(communityDTO);

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ItemCommunityRecyclerviewBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        ItemCommunityRecyclerviewBinding getBinding() {
            return binding;
        }
    }
}
