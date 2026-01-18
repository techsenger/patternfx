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

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;
import javafx.util.Pair;

/**
 *
 * @author Pavel Castornii
 */
public abstract class AbstractDepthFirstIterator<T, S> implements TreeIterator<T> {

    private final Stack<Pair<S, Integer>> stack = new Stack<>();

    private int currentDepth = -1;

    protected AbstractDepthFirstIterator(S root) {
        stack.push(new Pair<>(root, 0));
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
         return !stack.isEmpty();
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        var pair = stack.pop();
        S node = pair.getKey();
        this.currentDepth = pair.getValue();
        var children = getChildren(node);
        for (int i = children.size() - 1; i >= 0; i--) {
            S child =  children.get(i);
            stack.push(new Pair<>(child, currentDepth + 1));
        }
        return map(node);
    }

    protected abstract List<S> getChildren(S parent);

    protected abstract T map(S value);
}
