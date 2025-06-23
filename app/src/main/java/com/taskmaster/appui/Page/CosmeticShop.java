package com.taskmaster.appui.Page;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FieldValue;
import com.taskmaster.appui.entity.User;
import com.taskmaster.appui.R;
import com.taskmaster.appui.view.uimodule.CosmeticItemTemplate.CosmeticAdapter;
import com.taskmaster.appui.view.uimodule.CosmeticItemTemplate.CosmeticItem;
import com.taskmaster.appui.util.NavUtil;

import java.util.ArrayList;
import java.util.List;

public class CosmeticShop extends AppCompatActivity {
    private View[] confirmationViews;
    private CosmeticItem clicked;
    private User user;
    private int gold;
    private TextView currencyText;
    private CosmeticAdapter adapter;
    private List<CosmeticItem> displayItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //For frontend, search for getAllItems method, there you will find where the items are stored
        //Setup
        super.onCreate(savedInstanceState);
        NavUtil.hideSystemBars(this);
        setContentView(R.layout.cosmetic_shop);

        ImageView confirmationBox = findViewById(R.id.confirmationTextContainer);
        TextView confirmationText = findViewById(R.id.confirmationText);
        ImageView yesBox = findViewById(R.id.confirmationYesContainer);
        TextView yesText = findViewById(R.id.confirmationYesText);
        ImageView noBox = findViewById(R.id.confirmationNoContainer);
        TextView noText = findViewById(R.id.confirmationNoText);
        View blurOverlay = findViewById(R.id.blurOverlay);
        currencyText = findViewById(R.id.currencyText);
        RecyclerView recycler = findViewById(R.id.recyclerView);
        confirmationViews = new View[]{confirmationBox, confirmationText, yesBox, yesText, noBox, noText,blurOverlay};

//        View rootLayout = findViewById(R.id.main);      Fam I'll just wait for gab's dropdown service
//        DropdownService.dropdownSetup(this,rootLayout);

        user = User.getInstance();
        List<String> ownedItems =  (List<String>) user.getDocumentSnapshot().get("ownedItems");
        
        gold = user.getDocumentSnapshot().getDouble("Gold").intValue();

        //Filter out items that are already owned
        displayItems = filterItems(getAllItems(),ownedItems);

        adapter = new CosmeticAdapter(displayItems, pos -> {
            // handle click
            clicked = displayItems.get(pos);
            setVisibility(View.VISIBLE);
        });
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
        //Popup
        setUpButtons(yesBox,noBox,yesText,noText);
        currencyText.setText(gold +" G");
    }

    private List<CosmeticItem> getAllItems() { //Our log of all available items
        List<CosmeticItem> items = new ArrayList<>();
        items.add(new CosmeticItem("Silver Armor", "High-level protection", 2, R.drawable.placeholderavatar1));
        items.add(new CosmeticItem("Red Armor", "High-level protection", 3, R.drawable.placeholderavatar2));
        items.add(new CosmeticItem("Crusader Armor", "High-level protection", 4, R.drawable.placeholderavatar3));
        items.add(new CosmeticItem("Copper Crusader Armor", "High-level protection", 5, R.drawable.placeholderavatar4));
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

            user.getDocumentSnapshot().getReference().update("Gold", gold);
            user.getDocumentSnapshot().getReference().update("ownedItems", FieldValue.arrayUnion(clicked.name))
                    .addOnSuccessListener(callback->{
                        user.loadDocumentSnapshot(callback2->{
                            List<String> ownedItems =  (List<String>) user.getDocumentSnapshot().get("ownedItems");
                            displayItems=filterItems(getAllItems(),ownedItems);
                            adapter.updateItems(displayItems);
                            setVisibility(View.GONE);
                        });
                    });
            currencyText.setText(gold +" G");
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
    private List<CosmeticItem> filterItems(List<CosmeticItem> allItems, List<String> ownedItems){
        List<CosmeticItem> unownedItems = new ArrayList<>();
        for (CosmeticItem item : allItems) {
            if (!ownedItems.contains(item.name)) {
                unownedItems.add(item);
            }
        }
        return unownedItems;
    }
    private void setVisibility(int visibility) {
        for (View v : confirmationViews) {
            v.setVisibility(visibility);
        }
    }
}
