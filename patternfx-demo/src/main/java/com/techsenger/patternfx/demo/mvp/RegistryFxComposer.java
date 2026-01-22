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

package com.techsenger.patternfx.demo.mvp;

import com.techsenger.patternfx.mvp.DefaultParentFxComposer;

/**
 *
 * @author Pavel Castornii
 */
public class RegistryFxComposer<V extends RegistryFxView<?, ?>>
        extends DefaultParentFxComposer<V> implements RegistryComposer {

    public RegistryFxComposer(V view) {
        super(view);
    }

    @Override
    public DialogPort showDialog() {
        var v = new DialogFxView(getView().getStage());
        var p = new DialogPresenter<>(v);
        p.initialize();
        v.getDialog().showAndWait();
        return p.getPort();
    }

    @Override
    public ReportPort getReport() {
        if (getView().getReport() == null) {
            return null;
        }
        return getView().getReport().getPresenter().getPort();
    }

    @Override
    public void addReport() {
        if (getView().getReport() != null) {
            throw new IllegalStateException("Report has been added");
        }
        var v = new ReportFxView();
        var p = new ReportPresenter(v);
        p.initialize();
        getView().addReport(v);
    }

    @Override
    public void removeReport() {
        var report = getView().getReport();
        if (report == null) {
            throw new IllegalStateException("Report hasn't been added");
        }
        getView().removeReport();
        report.getPresenter().deinitialize();
    }

}
