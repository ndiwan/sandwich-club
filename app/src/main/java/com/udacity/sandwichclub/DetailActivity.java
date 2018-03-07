package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;


    ImageView ingredientsIv;
    TextView alsoKnownAsTv;
    TextView placeOfOriginTv;
    TextView descriptionTv;
    TextView ingredientsTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ingredientsIv = findViewById(R.id.image_iv);
        alsoKnownAsTv = findViewById(R.id.also_known_tv);
        placeOfOriginTv = findViewById(R.id.origin_tv);
        descriptionTv = findViewById(R.id.description_tv);
        ingredientsTv = findViewById(R.id.ingredients_tv);


        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {

        //set text for placeOfOrigin
        placeOfOriginTv.setText(getStringValue(sandwich.getPlaceOfOrigin()));

        //set text for description
        descriptionTv.setText(getStringValue(sandwich.getDescription()));

        //set text for alsoKnownAs list
        alsoKnownAsTv.setText(getStringWithSeparator(sandwich.getAlsoKnownAs()));

        //set text for ingredients list
        ingredientsTv.setText(getStringWithSeparator(sandwich.getIngredients()));

    }

    private String getStringValue(String str) {
        if (!str.isEmpty()) {
            return str;
        } else {
            return getString(R.string.data_not_available);
        }
    }

    private String getStringWithSeparator(List<String> list) {

        if (!list.isEmpty()) {

            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                builder.append((list.get(i)));
                if (i < list.size() - 1) {
                    builder.append(", ");
                }
            }
            return builder.toString();
        } else
            return getString(R.string.data_not_available);
    }

}
