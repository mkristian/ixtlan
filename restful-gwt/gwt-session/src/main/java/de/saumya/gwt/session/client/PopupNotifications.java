/**
 *
 */
package de.saumya.gwt.session.client;

import java.sql.Timestamp;
import java.util.PriorityQueue;
import java.util.Queue;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.saumya.gwt.persistence.client.AbstractResource;
import de.saumya.gwt.persistence.client.ResourceNotifications;

public class PopupNotifications implements Notifications, ResourceNotifications {

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
            this.messages = message.split("\n");
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
                if (!this.isInfo) {
                    for (final String msg : this.messages) {
                        builder.append(msg).append("<br/>");
                    }
                }
                else {
                    builder.append("<ul>");
                    for (final String msg : this.messages) {
                        builder.append("<li>").append(msg).append("</li>");
                    }
                    builder.append("</ul>");
                }
            }
        }

        public String toLine() {
            return toString().replace("<br/>", " ");
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

    private final Queue<Note>  notes      = new PriorityQueue<Note>();
    private final ComplexPanel all        = new VerticalPanel();
    private final PopupPanel   popup      = new PopupPanel(true, false); // autohide,not_modal
    private final HTML         message    = new HTML();
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
    private final Label        closeLabel = new Label("(X) close");
    {
        this.closeLabel.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                PopupNotifications.this.popup.hide();
            }
        });
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
            final HTML noteLabel = new HTML(note.toLine());
            noteLabel.setStyleName(note.isInfo ? "info-note" : "warn-note");
            this.all.add(noteLabel);
        }
        this.all.add(this.closeLabel);
        this.popup.setPopupPosition(0, Window.getScrollTop());
        this.popup.show();
    }

    protected Label closeLabel() {
        return this.closeLabel;
    }

    @Override
    public void error(final int status, final String statusMessage,
            final String message, final AbstractResource<?> resource) {
        // TODO make the resource clickable inside the text
        warn(message + " "
                + (resource.isNew() ? "--none--" : resource.display()) + "\n"
                + status + ": " + statusMessage);
    }

    @Override
    public void info(final String message, final AbstractResource<?> resource) {
        // TODO make the resource clickable inside the text
        info(message + " " + resource.display());
    }

}
