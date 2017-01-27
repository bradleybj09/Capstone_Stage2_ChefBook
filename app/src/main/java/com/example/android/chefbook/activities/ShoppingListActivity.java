package com.example.android.chefbook.activities;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.android.chefbook.R;
import com.example.android.chefbook.database.MyRecipesContract;
import com.example.android.chefbook.database.MyRecipesDatabaseHelper;
import com.example.android.chefbook.objects.Ingredient;
import com.example.android.chefbook.utilities.ListIngredientAdapter;
import com.example.android.chefbook.utilities.ListRecipeAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Ben on 1/2/2017.
 */

public class ShoppingListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    final static int LOADER_ID = 1;
    ContentResolver contentResolver;
    ListRecipeAdapter listRecipeAdapter;
    ListIngredientAdapter listIngredientAdapter;
    FrameLayout emptyLayout;
    ListView recipeListView;
    GoogleApiClient mGoogleApiClient;
    final int MY_PERMISSION_REQUEST_LOCATION = 1;
    InterstitialAd mInterstitialAd;
    ImageView upButton;

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, MyRecipesContract.TableMyRecipes.LIST_RECIPE_CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        listRecipeAdapter.swapCursor(null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        listRecipeAdapter.swapCursor(data);
        if (data.getCount() > 0) {
            populateIngredientList();
            recipeListView.setAdapter(listRecipeAdapter);
        }
        else {
            emptyLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        contentResolver = getContentResolver();
        setContentView(R.layout.shopping_list_activity);
        emptyLayout = (FrameLayout)findViewById(R.id.empty_list_layout);
        listRecipeAdapter = new ListRecipeAdapter(this, null, 0);
        upButton = (ImageView)findViewById(R.id.list_action_up);
        upButton.setColorFilter(ContextCompat.getColor(this, R.color.main_yellow));
        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSupportNavigateUp();
            }
        });
        recipeListView = (ListView)findViewById(R.id.list_recipe_listview);
     //   Cursor rCursor = contentResolver.query(MyRecipesContract.TableMyRecipes.LIST_RECIPE_CONTENT_URI, null, null, null, null);
        setSupportActionBar((Toolbar)findViewById(R.id.list_toolbar));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .build();
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.ad_unit_id));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });

        requestNewInterstitial();
        super.onCreate(savedInstanceState);
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(getString(R.string.ad_test_id_phone))
                .addTestDevice("4B36A9FBD18ADF42523E969BDDF50955")
                .build();
        mInterstitialAd.loadAd(adRequest);
    }

    public ShoppingListActivity() {

    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_menu, menu);
        Drawable mapIcon = menu.getItem(0).getIcon();
        mapIcon.mutate();
        mapIcon.setColorFilter(getResources().getColor(R.color.main_yellow), PorterDuff.Mode.SRC_ATOP);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_map:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        Toast toast = Toast.makeText(this,getString(R.string.access_location),Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},MY_PERMISSION_REQUEST_LOCATION);
                    }
                } else {
                    Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    double lat = lastLocation.getLatitude();
                    double lon = lastLocation.getLongitude();
                    Uri gmmIntentUri= Uri.parse("geo:" + String.valueOf(lat) + "," + String.valueOf(lon) + "?q=grocery stores");
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(mapIntent);
                    }
                    else {
                        Toast noMapToast = Toast.makeText(this,getString(R.string.no_map),Toast.LENGTH_SHORT);
                        noMapToast.show();
                    }
                    return true;
                }
            default: return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    double lat = lastLocation.getLatitude();
                    double lon = lastLocation.getLongitude();
                    Uri gmmIntentUri = Uri.parse("geo:" + String.valueOf(lat) + "," + String.valueOf(lon) + "?q=grocery stores");
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(mapIntent);
                    } else {
                        Toast noMapToast = Toast.makeText(this, getString(R.string.no_map), Toast.LENGTH_SHORT);
                        noMapToast.show();
                    }
                }

                } else {

                }
                return;
            }
        }
    }

    private void populateIngredientList() {
        MyRecipesDatabaseHelper myRecipesDatabaseHelper = new MyRecipesDatabaseHelper(this);
        SQLiteDatabase sqLiteDatabase = myRecipesDatabaseHelper.getReadableDatabase();
        ListView ingredientListView = (ListView)findViewById(R.id.list_ingredient_listview);

        String sql = "SELECT " + BaseColumns._ID + ", "
                + MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_ID + ", "
                + MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_NAME + ", "
                + "sum(" + MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_AMOUNT + ") AS " + MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_AMOUNT + ", "
                + MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_UNIT + ", "
                + MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_UNIT_LONG + " FROM "
                + MyRecipesContract.TableMyRecipes.LIST_INGREDIENTS_TABLE_NAME + " GROUP BY "
                + MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_UNIT + ", "
                + MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_NAME + " ORDER BY "
                + MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_NAME + " ASC";

        Cursor iCursor = sqLiteDatabase.rawQuery(sql, null);
        iCursor.moveToFirst();
        listIngredientAdapter = new ListIngredientAdapter(this, iCursor, 0);
        ingredientListView.setAdapter(listIngredientAdapter);
        sqLiteDatabase.close();
    }

    public void removeListRecipe(View view) {
        RelativeLayout relativeLayout = (RelativeLayout)((ViewGroup)view.getParent()).findViewById(R.id.shopping_list_recipe);
        int recipeID = (int)relativeLayout.getTag();
        contentResolver.delete(MyRecipesContract.TableMyRecipes.LIST_RECIPE_CONTENT_URI, MyRecipesContract.TableMyRecipes.LIST_RECIPE_ID + " = " + String.valueOf(recipeID),null);
        contentResolver.delete(MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_CONTENT_URI, MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_JOIN_ID + " = " + String.valueOf(recipeID),null);
        Cursor rCursor = contentResolver.query(MyRecipesContract.TableMyRecipes.LIST_RECIPE_CONTENT_URI, null, null, null, null);
        if (rCursor.getCount() > 0) {
            ListView recipeListView = (ListView) findViewById(R.id.list_recipe_listview);
            listRecipeAdapter = new ListRecipeAdapter(this, rCursor, 0);
            recipeListView.setAdapter(listRecipeAdapter);
            populateIngredientList();
        }
        else {
            onListEmptied();
            emptyLayout.setVisibility(View.VISIBLE);
            populateIngredientList();
        }
    }

    public void removeListIngredient(View view) {
        RelativeLayout relativeLayout = (RelativeLayout)((ViewGroup)view.getParent()).findViewById(R.id.shopping_list_ingredient);
        Ingredient ingredient = (Ingredient)relativeLayout.getTag();
        String where = MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_ID + "=? AND " + MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_UNIT + "=?";
        String[] args = new String[] { String.valueOf(ingredient.getIngredientID()), ingredient.getUnit()};
        contentResolver.delete(MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_CONTENT_URI, where, args);

        populateIngredientList();
    }

    public void finishShopping(View view) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.clear_shopping)
                .setMessage(getResources().getString(R.string.clear_message))
                .setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onListEmptied();
                        contentResolver.delete(MyRecipesContract.TableMyRecipes.LIST_INGREDIENT_CONTENT_URI, null, null);
                        contentResolver.delete(MyRecipesContract.TableMyRecipes.LIST_RECIPE_CONTENT_URI, null, null);
                        recreate();
                    }
                })
                .setNegativeButton(getString(R.string.no), null)
                .show();
    }

    public void onListEmptied() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }
}
