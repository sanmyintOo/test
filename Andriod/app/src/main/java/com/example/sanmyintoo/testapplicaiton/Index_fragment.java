package com.example.sanmyintoo.testapplicaiton;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Index_fragment extends Fragment implements View.OnClickListener {


    public Index_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View inputFragmentView = inflater.inflate(R.layout.fragment_index_fragment, container, false);

        CardView greeting = (CardView) inputFragmentView.findViewById(R.id.eventDetail);
        greeting.setOnClickListener(
                new CardView.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getContext(), ChatRoom.class);
                        startActivity(i);
                    }
                }
        );
        ImageView addevent = (ImageView) inputFragmentView.findViewById(R.id.fab);
        addevent.setOnClickListener(
                new ImageView.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getContext(), Add_EventInformation.class);
                        startActivity(i);
                    }
                }
        );

        return inputFragmentView;
    }

//    public void EventDetail(View view) {
//        startActivity(new Intent(getActivity(), Confirm.class));
//    }
//
//    public void toAddevent(View view) {
//        startActivity(new Intent(getActivity(), Add_EventInformation.class));
//    }



    @Override
    public void onClick(View v) {

        }
    }

