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
     * <p>
     * This method should be called during initialization.
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
     * Traverses the JavaFX node tree upward from the given {@link Node}, searching for the nearest node that has
     * an associated {@link ComponentFxView} component.
     * <p>
     * The search starts at the given node itself and walks up the parent chain until a component is found or the
     * root is reached.
     *
     * @param node the JavaFX node to start the search from; must not be {@code null}
     * @return the nearest {@link ComponentFxView} component found in the parent chain,
     *         or {@code null} if no component is associated with any node up to the root
     */
    public static @Nullable ComponentFxView<?> findComponent(Node node) {
        Node current = node;
        while (current != null) {
            FxView<?> view = getView(current);
            if (view instanceof ParentFxView<?> component) {
                return component;
            }
            current = current.getParent();
        }
        return null;
    }

    private FxViewUtils() {
        // empty
    }
}
