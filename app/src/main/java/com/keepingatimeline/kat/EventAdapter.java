package com.keepingatimeline.kat;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Dana on 5/19/2016.
 * Pretty much everything in this class was copied from online documentation or was automatically
 * generated by Android Studio. -- Dana
 */
public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ArrayList<Event> eventList;
    private final String timelineID;
    private Context ctx;

    // saves off the passed eventList, removing null events from it
    public EventAdapter(Context context, ArrayList<Event> eventListParam, String timelineID) {
        this.eventList = eventListParam;
        this.ctx = context;
        this.timelineID = timelineID;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolderPhoto extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public int position;
        public EventAdapter parent;
        public TextView photoTitle;
        public TextView photoDate;
        public TextView photoText;
        public ImageView photoPhoto;
        public View photoDivider;
        public ViewHolderPhoto(View v) {
            super(v);
            photoTitle = (TextView) v.findViewById(R.id.photo_title);
            photoDate = (TextView) v.findViewById(R.id.photo_date);
            photoText = (TextView) v.findViewById(R.id.photo_text);
            photoPhoto = (ImageView) v.findViewById(R.id.photo_photo);
            photoDivider = v.findViewById(R.id.photo_divider);

            Context context = v.getContext();
            Typeface photoTitleFont = Typeface.createFromAsset(context.getAssets(), context.getString(R.string.RobotoRegular));
            photoTitle.setTypeface(photoTitleFont);


            final ImageView editPhotoEvent = (ImageView) v.findViewById(R.id.editPhotoEvent);
            editPhotoEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v){

                    PopupMenu popup = new PopupMenu(v.getContext(), v);
                    popup.getMenuInflater().inflate(R.menu.event_settings_menu, popup.getMenu());
                    popup.show();

                    popup.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            LayoutInflater inflater = LayoutInflater.from(v.getContext());
                            View view = inflater.inflate(R.layout.dialog_edit_photo_event, null);
                            builder.setView(view);

                            final EditText title = (EditText) view.findViewById(R.id.changePhotoTitle);
                            final EditText text = (EditText) view.findViewById(R.id.changePhotoDescription);
                            final DatePicker date = (DatePicker) view.findViewById(R.id.changePhotoDate);

                            builder.setTitle("Edit Info");
                            builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Event newEvent = new Event();
                                    newEvent.setType("photo");
                                    newEvent.setTitle(title.getText().toString());
                                    newEvent.setString1(text.getText().toString());
                                    // Get the values from the DatePicker and combine into String
                                    // Month values are 0 indexed
                                    String dateString = (date.getMonth() + 1) + "/" + date.getDayOfMonth()
                                            + "/" + date.getYear();
                                    newEvent.setDate(dateString);

                                    // Get rid of dialog before database updates
                                    dialog.dismiss();
                                    parent.updateEvent(newEvent, position);
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                            // Parse the stored date string
                            Scanner scanner = new Scanner(photoDate.getText().toString()).useDelimiter("[^0-9]+");
                            int month = scanner.nextInt();
                            int day = scanner.nextInt();
                            int year = scanner.nextInt();
                            // Set the DatePicker to the stored date
                            date.updateDate(year, month - 1, day);
                            title.setText(photoTitle.getText());
                            text.setText(photoText.getText());

                            builder.create().show();
                            return true;
                        }
                    });

                    popup.getMenu().getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            builder.setTitle("Delete Event").setMessage("Are you sure you want to delete this event?");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    parent.deleteEvent(position);
                                }
                            });
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            builder.create().show();
                            return true;
                        }
                    });

                }
            });
        }
    }

    public static class ViewHolderQuote extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public int position;
        public EventAdapter parent;
        public TextView quoteTitle;
        public TextView quoteDate;
        public TextView quoteText;
        public TextView quoteSpeaker;
        public ImageView editQuote;
        public ViewHolderQuote(View v) {
            super(v);
            quoteTitle = (TextView) v.findViewById(R.id.quote_title);
            quoteDate = (TextView) v.findViewById(R.id.quote_date);
            quoteText = (TextView) v.findViewById(R.id.quote_text);
            quoteSpeaker = (TextView) v.findViewById(R.id.quote_speaker);
            editQuote = (ImageView) v.findViewById(R.id.editQuoteEvent);

            Context context = v.getContext();
            Typeface quoteTitleFont = Typeface.createFromAsset(context.getAssets(), context.getString(R.string.RobotoRegular));
            quoteTitle.setTypeface(quoteTitleFont);

            Typeface quoteTextFont = Typeface.createFromAsset(context.getAssets(), context.getString(R.string.RobotoSlabRegular));
            quoteText.setTypeface(quoteTextFont);

            Typeface quoteSpeakerFont = Typeface.createFromAsset(context.getAssets(), context.getString(R.string.RobotoMedium));
            quoteSpeaker.setTypeface(quoteSpeakerFont);


            final ImageView editQuoteEvent = (ImageView) v.findViewById(R.id.editQuoteEvent);
            editQuoteEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v){

                    PopupMenu popup = new PopupMenu(v.getContext(), v);
                    popup.getMenuInflater().inflate(R.menu.event_settings_menu, popup.getMenu());
                    popup.show();

                    popup.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            LayoutInflater inflater = LayoutInflater.from(v.getContext());
                            View view = inflater.inflate(R.layout.dialog_edit_quote_event, null);
                            builder.setView(view);

                            final EditText title = (EditText) view.findViewById(R.id.changeQuoteTitle);
                            final EditText quote = (EditText) view.findViewById(R.id.changeQuote);
                            final DatePicker date = (DatePicker) view.findViewById(R.id.changeQuoteDate);
                            final EditText speaker = (EditText) view.findViewById(R.id.changeQuoteSource);

                            builder.setTitle("Edit Info");
                            builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Event newEvent = new Event();
                                    newEvent.setType("quote");
                                    newEvent.setTitle(title.getText().toString());
                                    String qStr = quote.getText().toString();
                                    newEvent.setString1(parseQuote(qStr));
                                    // Get the date values and combine into String
                                    String dateString = (date.getMonth() + 1) + "/" + date.getDayOfMonth()
                                            + "/" + date.getYear();
                                    newEvent.setDate(dateString);
                                    newEvent.setString2(speaker.getText().toString());

                                    // If the parsed quote is empty, reuse old data
                                    if (parseQuote(qStr).equals("")) {
                                        String origStr = quoteText.getText().toString();
                                        newEvent.setString1(origStr.substring(1, origStr.length() - 1));
                                    }

                                    // Get rid of dialog before database updates
                                    dialog.dismiss();
                                    parent.updateEvent(newEvent, position);
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                            // Won't show quotation marks in the dialog
                            String quoteString = quoteText.getText().toString();
                            String editQuoteString = quoteString.substring(1, quoteString.length() - 1);

                            // Won't show hyphen in the dialog
                            String editSourceString = quoteSpeaker.getText().toString();

                            if (editSourceString.length() > 0) {
                                editSourceString = editSourceString.substring(1);
                            }

                            // Parse the stored date string
                            Scanner scanner = new Scanner(quoteDate.getText().toString()).useDelimiter("[^0-9]+");
                            int month = scanner.nextInt();
                            int day = scanner.nextInt();
                            int year = scanner.nextInt();
                            // Set the date picker to the stored date
                            date.updateDate(year, month - 1, day);
                            title.setText(quoteTitle.getText());
                            quote.setText(editQuoteString);
                            speaker.setText(editSourceString);

                            builder.create().show();
                            return true;
                        }
                    });

                    popup.getMenu().getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            builder.setTitle("Delete Event").setMessage("Are you sure you want to delete this event?");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    parent.deleteEvent(position);
                                }
                            });
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            builder.create().show();
                            return true;
                        }
                    });

                }
            });
        }

        // Helper method to parse and trim the quote
        private String parseQuote(String str) {
            str = str.trim();
            while (str.length() > 0 && str.charAt(0) == '"') {
                str = str.substring(1);
            }
            while (str.length() > 0 && str.charAt(str.length() - 1) == '"') {
                str = str.substring(0, str.length() - 1);
            }
            return str.trim();
        }
    }

    public static class ViewHolderText extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public int position;
        public EventAdapter parent;
        public TextView textTitle;
        public TextView textDate;
        public TextView textText;
        public ViewHolderText(View v) {
            super(v);
            textTitle = (TextView) v.findViewById(R.id.text_title);
            textDate = (TextView) v.findViewById(R.id.text_date);
            textText = (TextView) v.findViewById(R.id.text_text);

            Context context = v.getContext();
            Typeface textTitleFont = Typeface.createFromAsset(context.getAssets(), context.getString(R.string.RobotoRegular));
            textTitle.setTypeface(textTitleFont);

            Typeface textTextFont = Typeface.createFromAsset(context.getAssets(), context.getString(R.string.LiberationSansRegular));
            textText.setTypeface(textTextFont);


            final ImageView editTextEvent = (ImageView) v.findViewById(R.id.editTextEvent);
            editTextEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v){

                    PopupMenu popup = new PopupMenu(v.getContext(), v);
                    popup.getMenuInflater().inflate(R.menu.event_settings_menu, popup.getMenu());
                    popup.show();

                    popup.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            LayoutInflater inflater = LayoutInflater.from(v.getContext());
                            View view = inflater.inflate(R.layout.dialog_edit_text_event, null);
                            builder.setView(view);

                            final EditText title = (EditText) view.findViewById(R.id.changeTextTitle);
                            final EditText quote = (EditText) view.findViewById(R.id.changeText);
                            final DatePicker date = (DatePicker) view.findViewById(R.id.changeTextDate);

                            builder.setTitle("Edit Info");
                            builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Event newEvent = new Event();
                                    newEvent.setType("text");
                                    newEvent.setTitle(title.getText().toString());
                                    newEvent.setString1(quote.getText().toString());
                                    // Get the date values and combine into String
                                    String dateString = (date.getMonth() + 1) + "/" + date.getDayOfMonth()
                                            + "/" + date.getYear();
                                    newEvent.setDate(dateString);

                                    // If empty text was entered, use old data
                                    if (quote.getText().toString().equals("")) {
                                        newEvent.setString1(textText.getText().toString());
                                    }

                                    // Get rid of dialog before database updates
                                    dialog.dismiss();
                                    parent.updateEvent(newEvent, position);
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                            // Parse the stored date string
                            Scanner scanner = new Scanner(textDate.getText().toString()).useDelimiter("[^0-9]+");
                            int month = scanner.nextInt();
                            int day = scanner.nextInt();
                            int year = scanner.nextInt();
                            // Set the date picker to the stored date
                            date.updateDate(year, month - 1, day);
                            title.setText(textTitle.getText());
                            quote.setText(textText.getText());

                            builder.create().show();
                            return true;
                        }
                    });

                    popup.getMenu().getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            builder.setTitle("Delete Event").setMessage("Are you sure you want to delete this event?");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    parent.deleteEvent(position);
                                }
                            });
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            builder.create().show();
                            return true;
                        }
                    });

                }
            });
        }
    }

    @Override
    public int getItemViewType(int position){
        switch(eventList.get(position).getType()) {
            case "photo":
                return 0;
            case "quote":
                return 1;
            case "text":
            default:
                return 2;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        RecyclerView.ViewHolder view;
        switch(viewType) {
            case 0:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_event_layout, parent, false);
                view = new ViewHolderPhoto(itemView);
                break;
            case 1:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.quote_event_layout, parent, false);
                view = new ViewHolderQuote(itemView);
                break;
            case 2:
            default:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_event_layout, parent, false);
                view = new ViewHolderText(itemView);
                break;
        }

        return view;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Event event = eventList.get(position);
        switch(event.getType()) {
            case "photo":

                // Checks for empty photo title
                if (event.getTitle() == "") {
                    ((ViewHolderPhoto)holder).photoTitle.setVisibility(View.GONE);
                }
                else {
                    ((ViewHolderPhoto)holder).photoTitle.setVisibility(View.VISIBLE);
                }

                // Checks for empty photo description
                if (event.getString1() == "") {
                    ((ViewHolderPhoto)holder).photoText.setVisibility(View.GONE);
                    ((ViewHolderPhoto)holder).photoDivider.setVisibility(View.GONE);
                }
                else {
                    ((ViewHolderPhoto)holder).photoText.setVisibility(View.VISIBLE);
                    ((ViewHolderPhoto)holder).photoDivider.setVisibility(View.VISIBLE);
                }

                ((ViewHolderPhoto)holder).position = position;
                ((ViewHolderPhoto)holder).parent = this;
                ((ViewHolderPhoto)holder).photoTitle.setText(event.getTitle());
                ((ViewHolderPhoto)holder).photoDate.setText(event.getDate());
                ((ViewHolderPhoto)holder).photoText.setText(event.getString1());
                ((ViewHolderPhoto)holder).photoPhoto.setImageBitmap(BitmapCache.getBitmapFromMemCache(event.getKey()));
                break;
            case "quote":

                SpannableString fancyQuoteText = new SpannableString("\"" + event.getString1() + "\"");
                Drawable leftQuotation = ctx.getDrawable(R.drawable.quotation_left);
                Drawable rightQuotation = ctx.getDrawable(R.drawable.quotation_right);

                leftQuotation.setBounds(0, 0, leftQuotation.getIntrinsicWidth(), leftQuotation.getIntrinsicHeight());
                rightQuotation.setBounds(0, 0, rightQuotation.getIntrinsicWidth(), rightQuotation.getIntrinsicHeight());

                ImageSpan leftSpan = new ImageSpan(leftQuotation, ImageSpan.ALIGN_BASELINE);
                ImageSpan rightSpan = new ImageSpan(rightQuotation, ImageSpan.ALIGN_BASELINE);
                fancyQuoteText.setSpan(leftSpan, 0, 1, 0);
                fancyQuoteText.setSpan(rightSpan, (fancyQuoteText.length() - 1), fancyQuoteText.length(), 0);

                // Checks for empty quote title
                if (event.getTitle() == "") {
                    ((ViewHolderQuote)holder).quoteTitle.setVisibility(View.GONE);
                }
                else {
                    ((ViewHolderQuote)holder).quoteTitle.setVisibility(View.VISIBLE);
                }

                // Checks for empty quote source
                String quoteSourceText = "";
                if (event.getString2() == "") {
                    ((ViewHolderQuote)holder).quoteSpeaker.setVisibility(View.GONE);
                }
                else {
                    ((ViewHolderQuote)holder).quoteSpeaker.setVisibility(View.VISIBLE);
                    quoteSourceText = "–" + event.getString2();
                }

                ((ViewHolderQuote)holder).position = position;
                ((ViewHolderQuote)holder).parent = this;
                ((ViewHolderQuote)holder).quoteTitle.setText(event.getTitle());
                ((ViewHolderQuote)holder).quoteDate.setText(event.getDate());
                ((ViewHolderQuote)holder).quoteText.setText(fancyQuoteText);
                ((ViewHolderQuote)holder).quoteSpeaker.setText(quoteSourceText);

                break;
            case "text":
            default:

                // Checks for empty photo title
                if (event.getTitle() == "") {
                    ((ViewHolderText)holder).textTitle.setVisibility(View.GONE);
                }
                else {
                    ((ViewHolderText)holder).textTitle.setVisibility(View.VISIBLE);
                }

                ((ViewHolderText)holder).position = position;
                ((ViewHolderText)holder).parent = this;
                ((ViewHolderText)holder).textTitle.setText(event.getTitle());
                ((ViewHolderText)holder).textDate.setText(event.getDate());
                ((ViewHolderText)holder).textText.setText(event.getString1());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    private void deleteEvent(int position) {
        Vars.getTimeline(timelineID).child("LastModified").setValue(DateGen.getCurrentDate());
        Vars.getTimeline(timelineID).child("Events/" + eventList.get(position).getKey()).setValue(null);
    }

    private void updateEvent(Event event, int position) {
        event.setKey(eventList.get(position).getKey());
        if(!event.getType().equals("quote")) event.setString2(eventList.get(position).getString2());

        Vars.getTimeline(timelineID).child("LastModified").setValue(DateGen.getCurrentDate());
        Vars.getTimeline(timelineID).child("Events/" + event.getKey()).setValue(event);
    }
}
