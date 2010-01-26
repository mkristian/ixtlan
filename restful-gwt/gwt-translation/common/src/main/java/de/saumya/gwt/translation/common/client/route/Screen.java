/**
 * 
 */
package de.saumya.gwt.translation.common.client.route;

import java.util.Map;

import de.saumya.gwt.persistence.client.Resource;

public interface Screen<E extends Resource<E>> extends HasPathFactory {

    void setupPathFactory(String parentPath);

    Screen<?> child(String parentKey);

    void showRead(String key);

    void showAll(Map<String, String> query);

    void showEdit(String key);

    void showNew();
}