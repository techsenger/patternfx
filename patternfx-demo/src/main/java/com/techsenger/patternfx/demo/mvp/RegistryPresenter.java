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

import com.techsenger.patternfx.demo.DemoNames;
import com.techsenger.patternfx.demo.model.PersonService;
import com.techsenger.patternfx.mvp.AbstractParentPresenter;
import com.techsenger.patternfx.mvp.Descriptor;
import java.util.List;

/**
 *
 * @author Pavel Castornii
 */
public class RegistryPresenter<V extends RegistryView, C extends RegistryComposer>
        extends AbstractParentPresenter<V, C> {

    private final PersonService service;

    public RegistryPresenter(V view, PersonService service) {
        super(view);
        this.service = service;
    }

    @Override
    protected Descriptor createDescriptor() {
        return new Descriptor(DemoNames.PERSON_REGISTRY);
    }

    protected void handleAddAction() {
        var dialog = getComposer().addDialog();
        var newPerson = dialog.getResult();
        dialog.deinitialize();
        if (newPerson != null) {
            service.save(newPerson);
            getView().addPersons(List.of(newPerson));
        }
    }

    protected void handleRemoveAction() {

    }

    protected void handleRefreshAction() {
        getView().clearPersons();
        getView().addPersons(service.readAll());
    }

    protected void handleReportAction() {

    }

    protected void handleCloseRequest() {
        deinitialize();
    }

    @Override
    protected void postInitialize() {
        super.postInitialize();
        getView().showStage();
        handleRefreshAction();
    }
}
