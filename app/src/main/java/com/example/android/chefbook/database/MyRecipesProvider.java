package com.example.android.chefbook.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Ben on 12/8/2016.
 */

public class MyRecipesProvider extends ContentProvider {

    MyRecipesDatabaseHelper mOpenHelper;
    static final int RECIPES = 1;
    static final int INGREDIENTS = 2;
    static final int LIST_RECIPES = 3;
    static final int LIST_INGREDIENTS = 4;
    private static final UriMatcher uriMatcher = getUriMatcher();

    static UriMatcher getUriMatcher() {
        final String PROVIDER_NAME = MyRecipesContract.CONTENT_AUTHORITY;
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, MyRecipesContract.PATH_RECIPES, RECIPES);
        uriMatcher.addURI(PROVIDER_NAME, MyRecipesContract.PATH_INGREDIENTS, INGREDIENTS);
        uriMatcher.addURI(PROVIDER_NAME, MyRecipesContract.PATH_LIST_RECIPES, LIST_RECIPES);
        uriMatcher.addURI(PROVIDER_NAME, MyRecipesContract.PATH_LIST_INGREDIENTS, LIST_INGREDIENTS);
        return uriMatcher;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return MyRecipesContract.TableMyRecipes.CONTENT_TYPE;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MyRecipesDatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor c;
        switch (uriMatcher.match(uri)) {
            case RECIPES: {
                c = mOpenHelper.getReadableDatabase().query(MyRecipesContract.TableMyRecipes.RECIPES_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case INGREDIENTS: {
                c = mOpenHelper.getReadableDatabase().query(MyRecipesContract.TableMyRecipes.INGREDIENTS_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case LIST_RECIPES: {
                c = mOpenHelper.getReadableDatabase().query(MyRecipesContract.TableMyRecipes.LIST_RECIPES_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case LIST_INGREDIENTS: {
                c = mOpenHelper.getReadableDatabase().query(MyRecipesContract.TableMyRecipes.LIST_INGREDIENTS_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase sqLiteDatabase = mOpenHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        Uri returnUri;

        switch (match){
            case RECIPES: {
                long _id = sqLiteDatabase.insert(MyRecipesContract.TableMyRecipes.RECIPES_TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = MyRecipesContract.TableMyRecipes.buildRecipesUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                } break;
            }

            case INGREDIENTS: {
                long _id = sqLiteDatabase.insert(MyRecipesContract.TableMyRecipes.INGREDIENTS_TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = MyRecipesContract.TableMyRecipes.buildRecipesUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                } break;
            }
            case LIST_RECIPES: {
                long _id = sqLiteDatabase.insert(MyRecipesContract.TableMyRecipes.LIST_RECIPES_TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = MyRecipesContract.TableMyRecipes.buildRecipesUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                } break;
            }
            case LIST_INGREDIENTS: {
                long _id = sqLiteDatabase.insert(MyRecipesContract.TableMyRecipes.LIST_INGREDIENTS_TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = MyRecipesContract.TableMyRecipes.buildRecipesUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                } break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
        getContext().getContentResolver().notifyChange(uri,null);
        sqLiteDatabase.close();
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        final SQLiteDatabase sqLiteDatabase = mOpenHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsDeleted;

        if (s == null) {
            s = "1";
        }
        switch (match) {
            case RECIPES:{
                rowsDeleted = sqLiteDatabase.delete(MyRecipesContract.TableMyRecipes.RECIPES_TABLE_NAME, s, strings);
                break;
            }
            case INGREDIENTS: {
                rowsDeleted = sqLiteDatabase.delete(MyRecipesContract.TableMyRecipes.INGREDIENTS_TABLE_NAME, s, strings);
                break;
            }
            case LIST_RECIPES:{
                rowsDeleted = sqLiteDatabase.delete(MyRecipesContract.TableMyRecipes.LIST_RECIPES_TABLE_NAME, s, strings);
                break;
            }
            case LIST_INGREDIENTS:{
                rowsDeleted = sqLiteDatabase.delete(MyRecipesContract.TableMyRecipes.LIST_INGREDIENTS_TABLE_NAME, s, strings);
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        final SQLiteDatabase sqLiteDatabase = mOpenHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case RECIPES: {
                rowsUpdated = sqLiteDatabase.update(MyRecipesContract.TableMyRecipes.RECIPES_TABLE_NAME, contentValues, s, strings);
                break;
            }
            case INGREDIENTS: {
                rowsUpdated = sqLiteDatabase.update(MyRecipesContract.TableMyRecipes.INGREDIENTS_TABLE_NAME, contentValues, s, strings);
                break;
            }
            case LIST_RECIPES: {
                rowsUpdated = sqLiteDatabase.update(MyRecipesContract.TableMyRecipes.LIST_RECIPES_TABLE_NAME, contentValues, s, strings);
                break;
            }
            case LIST_INGREDIENTS: {
                rowsUpdated = sqLiteDatabase.update(MyRecipesContract.TableMyRecipes.LIST_INGREDIENTS_TABLE_NAME, contentValues, s, strings);
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
