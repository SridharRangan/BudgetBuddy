package technology.thinkbench.budgetbuddy.data;

import android.net.Uri;
import android.content.ContentResolver;
import android.provider.BaseColumns;

public class DataContract {

    private DataContract(){}

    public static final String CONTENT_AUTHORITY = "technology.thinkbench.budgetbuddy";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_EXPENDITURES = "expenditures";

    public static final class ExpenditureEntry implements BaseColumns {

        /** The content URI to access the expenditure data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_EXPENDITURES);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of expenditures.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EXPENDITURES;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single expenditure.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EXPENDITURES;

        /** Name of database table for pets */
        public final static String TABLE_NAME = "expenditures";

        /**
         * Unique ID number for the expenditure (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the pet.
         *
         * Type: TEXT
         */
        public final static String COLUMN_EXPENDITURE_LABEL ="label";

        /**
         * a short desc. of the expenditure
         *
         * Type: TEXT
         */
        public final static String COLUMN_EXPENDITURE_AMOUNT = "amount";

        /**
         * how much did you spend?
         *
         *
         * Type: Dollar Value?
         */
        public final static String COLUMN_EXPENDITURE_TAG = "tag";

        /**
         * A tag to identify what kind of expenditure it was
         *
         * Type: TEXT
         */
        public final static String COLUMN_EXPENDITURE_DATE = "date";

        /**
         * date of purchase
         */

    }

}
