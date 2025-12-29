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

package com.techsenger.patternfx.core;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import javafx.util.Pair;

/**
 *
 * @author Pavel Castornii
 */
public abstract class AbstractBreadthFirstIterator<T> implements TreeIterator<T> {

    private final Queue<Pair<T, Integer>> queue = new LinkedList<>();

    private int currentDepth = -1;

    protected AbstractBreadthFirstIterator(T root) {
        queue.offer(new Pair<>(root, 0));
    }

    @Override
    public int getDepth() {
        if (currentDepth == -1) {
            throw new IllegalStateException("next() hasn't been called yet");
        }
        return currentDepth;
    }

    @Override
    public boolean hasNext() {
        return !queue.isEmpty();
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        var pair = queue.poll();
        var node = pair.getKey();
        currentDepth = pair.getValue();
        List<T> children = getChildren(node);
        for (T child : children) {
            queue.offer(new Pair<>(child, currentDepth + 1));
        }
        return node;
    }

    protected abstract List<T> getChildren(T parent);
}
