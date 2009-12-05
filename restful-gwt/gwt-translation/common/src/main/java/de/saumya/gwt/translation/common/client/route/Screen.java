/**
 * 
 */
package de.saumya.gwt.translation.common.client.route;

import de.saumya.gwt.persistence.client.Resource;

public interface Screen<E extends Resource<E>> {

    Screen<?> child(String parentKey);

    PathFactory getPathFactory();

    void setup(String parentPath);

    void showRead(String key);

    void showAll();

    void showEdit(String key);

    void showNew();
}