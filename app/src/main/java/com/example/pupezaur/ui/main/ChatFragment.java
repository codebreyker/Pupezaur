package com.example.pupezaur.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pupezaur.R;
import com.example.pupezaur.Util.Message;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Context;

import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {

    RecyclerView recycler_view;
    Intent intent;
    ImageButton btn_send;
    EditText text_send;
    FirebaseUser firebaseUser;
    RecyclerView.ItemAnimator.AdapterChanges adapter;
    Context context;
    List<Message> messageList;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public ChatFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment2.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
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
//        Intent intent = new Intent(getActivity(), MessageActivity.class);
//        startActivity(intent);
//        btn_send = getView().findViewById(R.id.btn_send);
//        text_send = getView().findViewById(R.id.text_send);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.frag2_layout, container, false);
        return v;
    }
//        btn_send.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View view, int i, KeyEvent keyEvent) {
//                String msg = text_send.getText().toString();
//                if (!msg.equals("")) {
//                    sendMessage(firebaseUser.getDisplayName(), msg);
//                    return true;
//                } else {
//                    Toast.makeText(getContext(), "You can not send blank message", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//
//    public void sendMessage(String sender, String message) {
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("Userame", sender);
//        hashMap.put("message", message);
//
//        reference.child("Chats").push().setValue(hashMap);
//    }

}

