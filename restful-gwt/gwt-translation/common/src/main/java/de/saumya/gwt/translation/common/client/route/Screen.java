/**
 *
 */
package de.saumya.gwt.translation.common.client.route;

import java.util.Map;

import de.saumya.gwt.persistence.client.AbstractResource;

public interface Screen<E extends AbstractResource<E>> extends HasPathFactory {

    void setupPathFactory(String parentPath);

    Screen<?> child(int parentKey);

    void showRead(int key);

    void showAll(Map<String, String> query);

    void showEdit(int key);

    void showNew();
}
