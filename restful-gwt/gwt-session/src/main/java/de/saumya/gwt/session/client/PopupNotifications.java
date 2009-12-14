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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.persistence.client.ResourceNotification;

public class PopupNotifications implements Notifications, ResourceNotification {

    private static class Note implements Comparable<Note> {
        private final Timestamp timestamp;
        private final boolean   isInfo;
        private final String[]  messages;

        private Note(final String[] messages, final boolean isInfo) {
            this.timestamp = new Timestamp(System.currentTimeMillis());
            this.isInfo = isInfo;
            this.messages = messages;
        }

        private Note(final String message, final boolean isInfo) {
            this.timestamp = new Timestamp(System.currentTimeMillis());
            this.isInfo = isInfo;
            this.messages = new String[] { message };
        }

        String toHTML() {
            final StringBuilder builder = new StringBuilder();
            appendHTML(builder);
            return builder.toString();
        }

        private void appendHTML(final StringBuilder builder) {
            if (this.messages.length == 1) {
                builder.append(this.messages[0]);
            }
            else {
                builder.append("<ul>");
                for (final String msg : this.messages) {
                    builder.append("<li>").append(msg).append("</li>");
                }
                builder.append("</ul>");
            }
        }

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder(this.timestamp.toString()
                    .replaceFirst("\\.[0-9]*$", ""));
            builder.append(" [")
                    .append(this.isInfo ? "info" : "warn")
                    .append("] ");
            appendHTML(builder);
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
    private final HTML         message = new HTML();
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

    @Override
    public void info(final String[] messages) {
        this.popup.setStyleName("info-notification");
        show(new Note(messages, true));
    }

    private void show(final PopupNotifications.Note note) {
        this.notes.add(note);
        if (note.messages.length == 1) {
            this.message.setText(note.toHTML());
        }
        else {
            this.message.setHTML(note.toHTML());
        }

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
            final HTML noteLabel = new HTML(note.toString());
            noteLabel.setStyleName(note.isInfo ? "info-note" : "warn-note");
            this.all.add(noteLabel);
        }
        this.popup.setPopupPosition(0, Window.getScrollTop());
        this.popup.show();
    }

    @Override
    public void error(final int status, final String message,
            final Resource<? extends Resource<?>> resource) {
        // TODO make the resource clickable inside the text
        warn(status + ": " + message + " " + resource.display());
    }

    @Override
    public void info(final String message,
            final Resource<? extends Resource<?>> resource) {
        // TODO make the resource clickable inside the text
        info(message + " " + resource.display());
    }
}