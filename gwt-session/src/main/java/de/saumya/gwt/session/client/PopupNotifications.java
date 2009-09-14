/**
 * 
 */
package de.saumya.gwt.session.client;

import java.sql.Timestamp;
import java.util.PriorityQueue;
import java.util.Queue;

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;


public class PopupNotifications implements Notifications {

    private static class Note implements Comparable<Note> {
        private final Timestamp timestamp;
        private final boolean   isInfo;
        private final String    message;

        private Note(final String message, final boolean isInfo) {
            this.timestamp = new Timestamp(System.currentTimeMillis());
            this.isInfo = isInfo;
            this.message = message;
        }

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder(this.timestamp.toString());
            builder.append(" [")
                    .append(this.isInfo ? "info" : "warn")
                    .append("] ")
                    .append(this.message);
            return builder.toString();
        }

        @Override
        public int compareTo(final Note o) {
            return this.timestamp.compareTo(o.timestamp);
        }
    }

    private final Queue<Note>  notes   = new PriorityQueue<Note>();
    private final ComplexPanel all     = new VerticalPanel();
    private final PopupPanel   popup   = new PopupPanel(true, false); // autohide,not_modal
    private final Label        message = new Label();
    {
        this.popup.add(this.message);
        final CloseHandler<PopupPanel> closeHandler = new CloseHandler<PopupPanel>() {

            @Override
            public void onClose(final CloseEvent<PopupPanel> event) {
                PopupNotifications.this.popup.clear();
                PopupNotifications.this.popup.add(PopupNotifications.this.message);
            }
        };
        this.popup.addCloseHandler(closeHandler);
    }

    @Override
    public void clear() {
        this.notes.clear();
    }

    @Override
    public void info(final String message) {
        this.popup.setStyleName("info-notification");
        show(new Note(message, true));
    }

    private void show(final PopupNotifications.Note note) {
        this.notes.add(note);
        this.message.setText(note.message);
        this.popup.setPopupPosition(0, Window.getScrollTop());
        this.popup.show();
    }

    @Override
    public void warn(final String message) {
        this.popup.setStyleName("warn-notification");
        show(new Note(message, false));
    }

    @Override
    public void showAll() {
        this.popup.setStyleName("showall-notification");
        this.popup.clear();
        this.popup.add(this.all);
        this.all.clear();
        for (final PopupNotifications.Note note : this.notes) {
            final Label noteLabel = new Label(note.toString());
            noteLabel.setStyleName(note.isInfo ? "info-note" : "warn-note");
            this.all.add(noteLabel);
        }
        this.popup.setPopupPosition(0, Window.getScrollTop());
        this.popup.show();
    }
}