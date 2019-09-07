package com.evergreen.apps.tourguideapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import java.util.List;

@Entity(tableName = Category.TABLE_NAME)
public class Category implements Parcelable {

    public static final String COLUMN_ID   = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PLURAL_NAME = "plural_name";
    public static final String COLUMN_SHORT_NAME = "short_name";
    public static final String COLUMN_ICON_URL = "icon_url";
    public static final String COLUMN_CATEGORY_ID = "category_id";
    public static final String COLUMN_CATEGORY_DOWNLOADED = "category_downloaded";

    public static final String TABLE_NAME = "categories";

    @NonNull
    @PrimaryKey( autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
    private long category_id;

    @ColumnInfo( name = COLUMN_CATEGORY_ID)
    private String id;

    @ColumnInfo( name = COLUMN_NAME)
    private String name;

    @ColumnInfo( name = COLUMN_PLURAL_NAME)
    private String pluralName;

    @ColumnInfo( name = COLUMN_SHORT_NAME)
    private String shortName;

    @ColumnInfo( name = COLUMN_ICON_URL)
    private String iconUrl;

    @ColumnInfo( name = COLUMN_CATEGORY_DOWNLOADED )
    private boolean category_downloaded;

    @Ignore
    private CategoryIcon icon;

    @Ignore
    private String primary;

    @Ignore
    private List<Category> categories;

    public long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(long category_id) {
        this.category_id = category_id;
    }

    public static Category fromContentValues(ContentValues values){
        final Category category = new Category();

        if (values.containsKey(COLUMN_ID)){
            category.id = values.getAsString(COLUMN_CATEGORY_ID);
        }

        if (values.containsKey(COLUMN_NAME)){
            category.name = values.getAsString(COLUMN_NAME);
        }

        if (values.containsKey(COLUMN_PLURAL_NAME)){
            category.pluralName = values.getAsString(COLUMN_PLURAL_NAME);
        }

        if (values.containsKey(COLUMN_SHORT_NAME)){
            category.shortName = values.getAsString(COLUMN_SHORT_NAME);
        }

        if (values.containsKey(COLUMN_ICON_URL)){
            category.iconUrl = values.getAsString(COLUMN_ICON_URL);
        }

        if (values.containsKey(COLUMN_CATEGORY_DOWNLOADED)){
            category.category_downloaded = values.getAsBoolean(COLUMN_CATEGORY_DOWNLOADED);
        }

        return category;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }


    public String getPluralName() {
        return pluralName;
    }

    public void setPluralName(String pluralName) {
        this.pluralName = pluralName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public boolean isCategory_downloaded() {
        return category_downloaded;
    }

    public void setCategory_downloaded(boolean category_downloaded) {
        this.category_downloaded = category_downloaded;
    }

    public CategoryIcon getIcon() {
        return icon;
    }

    public void setIcon(CategoryIcon icon) {
        this.icon = icon;
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.pluralName);
        dest.writeString(this.shortName);
//        dest.writeParcelable(this.icon, flags);
        dest.writeString(this.primary);
    }

    public Category() {
    }

    protected Category(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.pluralName = in.readString();
        this.shortName = in.readString();
//        this.icon = in.readParcelable(CategoryIcon.class.getClassLoader());
        this.primary = in.readString();
    }

    public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel source) {
            return new Category(source);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };


    public class CategoryIcon {

        private String prefix;
        private String suffix;


        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        public String getSuffix() {
            return suffix;
        }

        public void setSuffix(String suffix) {
            this.suffix = suffix;
        }

        public Uri getFullIconUrl() {
            return Uri.parse(prefix + "100" +suffix);
        }
    }
}


