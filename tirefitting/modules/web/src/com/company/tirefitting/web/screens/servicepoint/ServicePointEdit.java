package com.company.tirefitting.web.screens.servicepoint;

import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.gui.screen.*;
import com.company.tirefitting.entity.ServicePoint;

import javax.inject.Inject;

@UiController("tirefitting_ServicePoint.edit")
@UiDescriptor("service-point-edit.xml")
@EditedEntityContainer("servicePointDc")
@LoadDataBeforeShow
public class ServicePointEdit extends StandardEditor<ServicePoint> {
    @Inject
    private TextField<String> cityField;

    @Inject
    private TextField<String> streetField;

    @Inject
    private TextField<String> buildingField;

    @Inject
    private Button commitAndCloseBtn;

    @Inject
    private TextField<String> addressField;

    @Subscribe
    private void onInit(InitEvent e){
        commitAndCloseBtn.addClickListener(event -> {
            String address = getAddress();
            addressField.setValue(address);

            closeWithCommit();
        });
    }

    private String getAddress(){
        String city = cityField.getValue();
        String street = streetField.getValue();
        String building = buildingField.getValue();

        final String delimiter = ", ";
        String address = city +
                delimiter +
                street +
                delimiter +
                building +
                delimiter;

        return address;
    }
}