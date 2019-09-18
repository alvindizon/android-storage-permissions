/*
 * Copyright 2017 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.dataprivacy.page.viewimage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.samples.dataprivacy.Injection;
import com.google.samples.dataprivacy.R;

import java.io.File;
import java.net.URI;

public class ImageViewerActivity extends AppCompatActivity implements ImageSharer {

    public static final String ARGUMENT_IMAGE_PATH = "ARGUMENT_IMAGE_PATH";
    private ImageViewerFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        ImageViewerFragment fragment = (ImageViewerFragment) getSupportFragmentManager().findFragmentById(R.id.contentframe);
        String image = getIntent().getStringExtra(ARGUMENT_IMAGE_PATH);
        if (fragment == null) {

            fragment = ImageViewerFragment.newInstance();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.contentframe, fragment);
            transaction.commit();
        }

        new ImageViewerPresenter(fragment, Injection.getImageRepository(this), this, image);

    }

    /**
     * Share an image identified by its absolute path.
     *
     * @param path The absolute path to the image.
     */
    @Override
    public void shareImage(String path) {
        // generate a shareable URI for the given file (contentUri).
        // The path parameter in the method contains the absolute path to an image stored in the app.
        Uri contentUri = FileProvider.getUriForFile(this,
                "com.google.samples.dataprivacy.FileProvider", new File(path));

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);

        // Set the contentUri as data, and the data type as "image/png".
        // This allows receiving apps to interpret the data correctly.
        intent.setDataAndType(contentUri, "image/png");

        // Grant the receiving app read access to this content URI by using
        // Intent.FLAG_GRANT_READ_URI_PERMISSION.
        // This flag tells the FileProvider to make the file available only to the recipient of this intent.
        // If you do not specify this flag, the app that receives this intent will not be able to
        // open the URI or access the referenced file.
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }
}
