package com.company.tirefitting.web.screens.request;

import com.company.tirefitting.service.RequestService;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.screen.*;
import com.company.tirefitting.entity.Request;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.time.LocalDateTime;

@UiController("tirefitting_Request.browse")
@UiDescriptor("request-browse.xml")
@LookupComponent("requestsTable")
@LoadDataBeforeShow
public class RequestBrowse extends StandardLookup<Request> {
    @Inject
    private RequestService requestService;

    @Inject
    private UiComponents components;

    @Install(to = "requestsTable.endTime", subject = "columnGenerator")
    private Component requestsTableEndTimeColumnGenerator(Request request) {
        Label<LocalDateTime> result = components.create(Label.NAME);
        result.setValue(requestService.getEndTime(request));

        return result;
    }
}