package com.taskmaster.appui.view.child;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.taskmaster.appui.data.ChildData;
import com.taskmaster.appui.entity.CurrentUser;
import com.taskmaster.appui.R;
import com.taskmaster.appui.view.uimodule.CosmeticItemTemplate.CosmeticAdapter;
import com.taskmaster.appui.view.uimodule.CosmeticItemTemplate.CosmeticItem;
import com.taskmaster.appui.util.NavUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CosmeticShop extends ChildPage {

    ChildData childData;
    private View[] confirmationViews;
    private CosmeticItem clicked;
    private int gold;
    private CosmeticAdapter adapter;
    List<String> OwnedItems;
    private List<CosmeticItem> displayItems;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) { //For frontend, search for getAllItems method, there you will find where the items are stored
        //Setup
        super.onCreate(savedInstanceState);
        NavUtil.hideSystemBars(this);
        setContentView(R.layout.cosmetic_shop);

        initNavigationMenu(this, CosmeticShop.class);

        ImageView confirmationBox = findViewById(R.id.confirmationTextContainer);
        TextView confirmationText = findViewById(R.id.confirmationText);
        ImageView yesBox = findViewById(R.id.confirmationYesContainer);
        TextView yesText = findViewById(R.id.confirmationYesText);
        ImageView noBox = findViewById(R.id.confirmationNoContainer);
        TextView noText = findViewById(R.id.confirmationNoText);
        TextView noAvailableItems = findViewById(R.id.noAvailableItems);
        View blurOverlay = findViewById(R.id.blurOverlay);
        RecyclerView recycler = findViewById(R.id.recyclerView);
        confirmationViews = new View[]{confirmationBox, confirmationText, yesBox, yesText, noBox, noText,blurOverlay};

        CurrentUser currentUser = CurrentUser.getInstance();
        childData = currentUser.getUserData().getUserSnapshot().toObject(ChildData.class);
        childData.updateData(cd->{

            gold = cd.getGold();
            topBar.getGoldAmount().setText(gold + " G");
            if (gold < 0) {
                topBar.getGoldAmount().setTextColor(Color.RED);
            }

            displayItems = filterItems(getAllItems(),cd.getOwnedItems());
            adapter = new CosmeticAdapter(displayItems, pos -> {
                // handle click
                clicked = displayItems.get(pos);
                setVisibility(View.VISIBLE);
            });

            recycler.setLayoutManager(new LinearLayoutManager(this));
            recycler.setAdapter(adapter);

            setUpButtons(yesBox,noBox,yesText,noText);

            if (displayItems.isEmpty()) {
                noAvailableItems.setVisibility(View.VISIBLE);
                noAvailableItems.setText("No Available Items. Come back later!");
            }
        });
    }


    private List<CosmeticItem> getAllItems() { //Our log of all available items
        List<CosmeticItem> items = new ArrayList<>();
        items.add(new CosmeticItem(1,"Silver Armor", "High-level protection", 2, R.drawable.placeholderavatar1));
        items.add(new CosmeticItem(2,"Red Armor", "High-level protection", 3, R.drawable.placeholderavatar2));
        items.add(new CosmeticItem(3,"Crusader Armor", "High-level protection", 4, R.drawable.placeholderavatar3));
        items.add(new CosmeticItem(4,"Copper Crusader Armor", "High-level protection", 5, R.drawable.placeholderavatar4));
        return items;
    }


    private void setUpButtons(ImageView yesBox, ImageView noBox, TextView yesText, TextView noText){
        yesBox.setOnClickListener(v -> isConfirm(true));
        yesText.setOnClickListener(v -> isConfirm(true));
        noBox.setOnClickListener(v -> isConfirm(false));
        noText.setOnClickListener(v -> isConfirm(false));
    }


    private void isConfirm(boolean confirm) {
        if (confirm) {
            if(!hasEnoughGold()) {return;}
            gold -= clicked.price;
            childData.setGold(gold);
            childData.getOwnedItems().add(clicked);
            childData.uploadData();
            childData.updateData(cd -> {
                displayItems = filterItems(getAllItems(),cd.getOwnedItems());
                adapter.updateItems(displayItems);
                setVisibility(View.GONE);
            });
            topBar.getGoldAmount().setText(gold + " G");
        } else {
            setVisibility(View.GONE);
        }
    }


    private boolean hasEnoughGold(){
        if(gold < clicked.price){
            Toast.makeText(this, "Not enough gold", Toast.LENGTH_SHORT).show();
            setVisibility(View.GONE);
            return false;
        }
        return true;
    }


    @SuppressLint("NewApi")
    private List<CosmeticItem> filterItems(List<CosmeticItem> allItems, List<CosmeticItem> OwnedItems){
        Set<String> ownedSet = new HashSet<>(OwnedItems.stream().map(CosmeticItem::getName).toList());
        return allItems.stream().filter(item -> !ownedSet.contains(item.getName())).toList();
    }


    private void setVisibility(int visibility) {
        for (View v : confirmationViews) {
            v.setVisibility(visibility);
        }
    }
}
