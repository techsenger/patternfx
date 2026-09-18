/*
 * Copyright 2024-2026 Pavel Castornii.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.techsenger.patternfx.mvp;

import com.techsenger.annotations.Nullable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Tab;

/**
 *
 * @author Pavel Castornii
 */
public final class FxViewUtils {

    private static final Object VIEW_KEY = new Object();

    /**
     * Associates the given view with the specified JavaFX node, so it can be looked up later via the node tree.
     *
     * @param node the JavaFX node to associate with the component; must not be {@code null}
     * @param view the view to associate with the node; must not be {@code null}
     */
    public static void setView(Node node, FxView<?> view) {
        node.getProperties().put(VIEW_KEY, view);
    }

    /**
     * Returns the view associated with the given node, or {@code null} if none is associated.
     *
     * @param node the JavaFX node to look up; must not be {@code null}
     * @return the associated view, or {@code null} if none is associated
     */
    public static @Nullable FxView<?> getView(Node node) {
        return (FxView<?>) node.getProperties().get(VIEW_KEY);
    }

    /**
     * Removes the view associated with the specified node, if any.
     *
     * @param node the JavaFX node whose associated view should be removed; must not be {@code null}
     */
    public static void clearView(Node node) {
        node.getProperties().remove(VIEW_KEY);
    }

    /**
     * Associates the given root {@link FxView} with the specified JavaFX {@link Scene}.
     */
    public static void setView(Scene scene, FxView<?> root) {
        scene.getProperties().put(VIEW_KEY, root);
    }

    /**
     * Returns the root {@link FxView} associated with the given {@link Scene}, or {@code null} if no view
     * has been associated with it.
     */
    public static @Nullable FxView<?> getView(Scene scene) {
        return (FxView<?>) scene.getProperties().get(VIEW_KEY);
    }

    /**
     * Removes the {@link FxView} associated with the specified {@link Scene}.
     */
    public static void clearView(Scene scene) {
        scene.getProperties().remove(VIEW_KEY);
    }

    /**
     * Associates the given {@link FxView} view with the specified JavaFX {@link Tab}.
     */
    public static void setView(Tab tab, FxView<?> root) {
        tab.getProperties().put(VIEW_KEY, root);
    }

    /**
     * Returns the {@link FxView} view associated with the given {@link Tab}, or {@code null} if no view has been
     * associated with it.
     */
    public static @Nullable FxView<?> getView(Tab tab) {
        return (FxView<?>) tab.getProperties().get(VIEW_KEY);
    }

    /**
     * Removes the {@link FxView} associated with the specified {@link Tab}.
     */
    public static void clearView(Tab tab) {
        tab.getProperties().remove(VIEW_KEY);
    }

    /**
     * Searches the given node and its ancestors for the nearest view of the specified type.
     *
     * @param node      the JavaFX node to start the search from; must not be {@code null}
     * @param viewClass the class or interface of the view to search for; must not be {@code null}
     * @param <T>       the type of the view to search for
     * @return the nearest matching view, or {@code null} if none is found up to the root
     */
    public static <T extends FxView<?>> @Nullable T findView(Node node, Class<T> viewClass) {
        Node current = node;
        while (current != null) {
            FxView<?> view = getView(current);
            if (view != null && viewClass.isInstance(view)) {
                return viewClass.cast(view);
            }
            current = current.getParent();
        }
        return null;
    }

    private FxViewUtils() {
        // empty
    }
}
