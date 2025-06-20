package com.example.ttlogexample.iccard;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;

import com.example.ttlogexample.R;
import com.example.ttlogexample.databinding.CardListItemBinding;
import com.example.ttlogexample.iccard.model.ICCardObj;

import java.util.ArrayList;

public class ICCardListAdapter  extends  RecyclerView.Adapter<ICCardListAdapter.CardViewHolder>{
    public ArrayList<ICCardObj> cardList = new ArrayList<ICCardObj>();
    private Activity context;
    onListenerClick event;
    public ICCardListAdapter(Activity context){
        this.context = context;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_list_item,parent);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
       ICCardObj cardOne = cardList.get(position);
       holder.onBind(cardOne);
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public interface onListenerClick{
        void onClick(ICCardObj cardObj);
    }
    public void setOnListener(onListenerClick event){
        this.event = event;
    }
    void updateData(ArrayList<ICCardObj> data){
        cardList.clear();
        cardList.addAll(data);
        notify();
    }
    class CardViewHolder extends RecyclerView.ViewHolder{
        CardListItemBinding binding;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CardListItemBinding.bind(itemView);
        }
        public void onBind(ICCardObj cardObj){
            binding.tvItem.setText(cardObj.getCardName());
            binding.tvItem.setOnClickListener(v->{
                 event.onClick(cardObj);
            });
        }
    }

}
