/**
 * 
 */
package de.saumya.gwt.translation.common.client.route;

import de.saumya.gwt.datamapper.client.Resource;

public interface Screen<E extends Resource<E>> {

    Screen<?> child(String key);

    PathFactory getPathFactory();

    void setupPathFactory(PathFactory pathFactory, String key);

    void showRead(String key);

    void showAll();

    void showEdit(String key);

    void showNew();
}