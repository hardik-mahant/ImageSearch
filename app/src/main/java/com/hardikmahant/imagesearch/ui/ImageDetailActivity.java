package com.hardikmahant.imagesearch.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.hardikmahant.imagesearch.R;
import com.hardikmahant.imagesearch.db.ImageDatabase;
import com.hardikmahant.imagesearch.models.Data;
import com.hardikmahant.imagesearch.repository.ImagesRepository;

public class ImageDetailActivity extends AppCompatActivity {

    private ImageView ivImage;
    private ImagesViewModel viewModel;

    private EditText edtComment;

    private Data imageData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_details);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ImagesRepository repository = new ImagesRepository(ImageDatabase.Companion.invoke(this));
        ImageSearchViewModelProviderFactory viewModelProviderFactory = new ImageSearchViewModelProviderFactory(getApplication(), repository);
        viewModel = new ViewModelProvider(this, viewModelProviderFactory).get(ImagesViewModel.class);
        ivImage = findViewById(R.id.ivImage);
        edtComment = findViewById(R.id.edtComment);
        Button btnSave = findViewById(R.id.btnSave);

        Intent intent = getIntent();
        imageData = (Data) intent.getSerializableExtra("imageData");

        if (imageData != null) {
            setTitle(imageData.getTitle());
            loadImage(imageData.getImages().get(0).getLink());
            if (imageData.getUsr_comment() != null && !imageData.getUsr_comment().isEmpty()) {
                edtComment.setText(imageData.getUsr_comment());
            } else {
                LiveData<String> comment = viewModel.getCommentForImage(imageData.getId());
                comment.observe(this, s -> edtComment.setText(s));
            }
        }

        btnSave.setOnClickListener(v -> {
            String comment = edtComment.getText().toString().trim();
            imageData.setUsr_comment(comment);
            viewModel.saveImage(imageData);
        });
    }

    private void loadImage(String url) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(ContextCompat.getDrawable(this, R.drawable.ic_image))
                .error(ContextCompat.getDrawable(this, R.drawable.ic_broken_image));

        Glide.with(this)
                .load(url)
                .apply(requestOptions)
                .into(ivImage);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
