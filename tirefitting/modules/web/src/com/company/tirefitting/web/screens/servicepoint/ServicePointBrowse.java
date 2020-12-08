package com.company.tirefitting.web.screens.servicepoint;

import com.haulmont.cuba.gui.screen.*;
import com.company.tirefitting.entity.ServicePoint;

@UiController("tirefitting_ServicePoint.browse")
@UiDescriptor("service-point-browse.xml")
@LookupComponent("servicePointsTable")
@LoadDataBeforeShow
public class ServicePointBrowse extends StandardLookup<ServicePoint> {
}