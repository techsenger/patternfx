/*
 * Copyright 2024-2025 Pavel Castornii.
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
     * Associates the given {@link FxView} view with the specified JavaFX {@link Node}.
     * <p>
     * This method stores a reference to the view in the node's property map, allowing the view to be
     * retrieved later by traversing the JavaFX node tree. This is useful in scenarios where only a node is available
     * (e.g., during focus traversal or event handling) and the associated view needs to be identified.
     *
     * @param node the JavaFX node to associate with the component; must not be {@code null}
     * @param view the view to associate with the node; must not be {@code null}
     */
    public static void setView(Node node, FxView<?> view) {
        node.getProperties().put(VIEW_KEY, view);
    }

    /**
     * Returns the {@link FxView} view associated with the given {@link Node}, or {@code null} if no view
     * has been associated with it.
     * <p>
     * This method is intended for use when traversing the JavaFX node tree — for example, when walking up the parent
     * chain from a focused node to find the nearest component boundary.
     *
     * @param node the JavaFX node to look up; must not be {@code null}
     * @return the associated view, or {@code null} if none is associated
     */
    public static @Nullable FxView<?> getView(Node node) {
        return (FxView<?>) node.getProperties().get(VIEW_KEY);
    }

    /**
     * Removes the {@link FxView} associated with the specified {@link Node}.
     * <p>
     * This method clears the reference previously stored via {@link #setView(Node, FxView)} from the node's property
     * map. After calling this method, {@link #getView(Node)} will return {@code null} for the given node.
     * <p>
     * This can be useful during cleanup or when the association between a node and its view
     * is no longer valid.
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
     * Traverses the JavaFX node tree upward from the given {@link Node}, searching for the nearest node that has
     * an associated view of the specified type.
     * <p>
     * The search starts at the given node itself and walks up the parent chain until a matching view is found
     * or the root is reached.
     *
     * @param node      the JavaFX node to start the search from; must not be {@code null}
     * @param viewClass the class or interface of the view to search for; must not be {@code null}
     * @param <T>       the type of the view to search for
     * @return the nearest {@link FxView} of the specified type found in the parent chain,
     *         or {@code null} if no matching view is associated with any node up to the root
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
