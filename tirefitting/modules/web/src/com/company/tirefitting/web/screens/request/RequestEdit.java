package com.company.tirefitting.web.screens.request;

import com.company.tirefitting.entity.Request;
import com.company.tirefitting.entity.RequestType;
import com.company.tirefitting.entity.ServicePoint;
import com.company.tirefitting.service.RequestService;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.screen.*;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

;

@UiController("tirefitting_Request.edit")
@UiDescriptor("request-edit.xml")
@EditedEntityContainer("requestDc")
@LoadDataBeforeShow
public class RequestEdit extends StandardEditor<Request> {
    @Inject
    private TimeField<LocalTime> timeField;

    @Inject
    private Button commitAndCloseBtn;

    @Inject
    private DatePicker<LocalDateTime> dateField;

    @Inject
    private PickerField<ServicePoint> servicePointField;

    @Inject
    private DataManager dataManager;

    @Inject
    private RequestService requestService;

    @Inject
    private TextField<Integer> wheelRadiusField;

    @Inject
    private LookupField<RequestType> requestTypeField;

    @Subscribe
    private void onInit(InitEvent e) {
        timeField.addValidator(date -> {
            if (date == null) {
                throw new ValidationException("Time must be not null");
            }
        });

        commitAndCloseBtn.addClickListener(event -> {

            LocalTime time = timeField.getValue();
            if (time != null) {
                dateField.setValue(dateField.getValue().with(LocalTime.of(0, 0)).plusHours(time.getHour()));
                dateField.setValue(dateField.getValue().plusMinutes(time.getMinute()));
            }

            // There is an exception. It's to avoid an error message in UI.
            try{
                closeWithCommit();
            }
            catch (Exception ex){
            }
        });

        dateField.addValidator(dateTime -> {
            LocalTime timeValue = timeField.getValue();

            if (dateTime != null &&
                    timeValue != null &&
                    servicePointField != null &&
                    wheelRadiusField != null &&
                    requestTypeField != null
            ) {
                long workers = servicePointField.getValue().getCountOfStuff();
                List<Request> requests = dataManager.loadList(
                        LoadContext.create(Request.class).setQuery(LoadContext.createQuery(
                                "SELECT r FROM tirefitting_Request r WHERE r.servicePoint.id = :servicePointId"
                                ).setParameter("servicePointId", servicePointField.getValue().getId())
                        )
                );

                System.out.println(requests);

                LocalDateTime dateFieldValue = dateField.getValue().with(LocalTime.of(0, 0)).plusHours(timeValue.getHour());
                dateFieldValue = dateFieldValue.plusMinutes(timeValue.getMinute());
                LocalDateTime finalDateFieldValue = dateFieldValue;

                Request addedRequest = dataManager.create(Request.class);
                addedRequest.setTime(finalDateFieldValue);
                addedRequest.setWheelRadius(wheelRadiusField.getValue());
                addedRequest.setRequestType(requestTypeField.getValue());
                addedRequest.setServicePoint(servicePointField.getValue());
                long busyWorkers = requests.stream().filter(request -> {
                    return isBusy(request, finalDateFieldValue, requestService.getEndTime(addedRequest));
                }).count();

                System.out.println("Workers: " + workers);
                System.out.println("Busy workers: " + busyWorkers);
                if(workers - busyWorkers <= 0){
                    throw new ValidationException("There is no stuff at this time");
                }
            }
        });
    }

    private boolean isBusy(Request request, LocalDateTime addedRequestTime, LocalDateTime addedRequestEndTime){
        LocalDateTime requestTime = request.getTime();
        LocalDateTime requestEndTime = requestService.getEndTime(request);

        System.out.println("Request time: " + requestTime.toString());
        System.out.println("Request end time: " + requestEndTime.toString());
        System.out.println("Added request time: " + addedRequestTime.toString());
        System.out.println("Added request end time: " + addedRequestEndTime.toString());

        return (isAfterOrEqual(addedRequestTime, requestTime) && isBeforeOrEqual(addedRequestTime, requestEndTime)) ||
                (isAfterOrEqual(addedRequestEndTime, requestTime) && isBeforeOrEqual(addedRequestEndTime, requestEndTime)) ||
                (isBeforeOrEqual(addedRequestTime, requestTime) && isAfterOrEqual(addedRequestEndTime, requestEndTime));
    }

    private boolean isAfterOrEqual(LocalDateTime f, LocalDateTime s){
        return f.isAfter(s) || f.equals(s);
    }

    private boolean isBeforeOrEqual(LocalDateTime f, LocalDateTime s){
        return f.isBefore(s) || f.equals(s);
    }

}