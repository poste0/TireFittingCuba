import 'package:flutter/material.dart';
import 'package:flutter_i18n/flutter_i18n.dart';
import 'package:syncfusion_flutter_calendar/calendar.dart';
import 'package:tire_fitting/data/RequestDataSource.dart';
import 'package:tire_fitting/data/RequestRepository.dart';
import 'package:tire_fitting/data/RequestRepositoryRest.dart';
import 'package:tire_fitting/entity/Request.dart';
import 'package:tire_fitting/entity/ServicePoint.dart';
import 'package:tire_fitting/main.dart';

class RequestCalendar extends StatefulWidget {
  ServicePoint servicePoint;
  String token;

  RequestCalendar({Key key, ServicePoint servicePoint, String token}) : super(key: key) {
    this.servicePoint = servicePoint;
    this.token = token;
  }

  @override
  _RequestCalendarState createState() => _RequestCalendarState(servicePoint);
}

class _RequestCalendarState extends State<RequestCalendar> {
  ServicePoint servicePoint;

  _RequestCalendarState([this.servicePoint]);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: getAppBar('calendar', context),
      body: Padding(
        padding: const EdgeInsets.all(30.0),
        child: Column(
          children: [
            Expanded(flex: 10, child: Text(FlutterI18n.translate(context, 'calendar_for_service') + " " + servicePoint.address, style: getMainStyle(context))),
            Expanded(
              flex: 90,
              child: FutureBuilder(
                future: RequestRepositoryRest().getRequests(servicePoint),
                builder: (context, snapshot){
                  if(snapshot.hasError){
                    return Text(snapshot.error.toString());
                  }
                  else if(snapshot.hasData){
                    List<Request> requests = snapshot.data as List<Request>;

                    return Container(
                        child: SfCalendar(
                          view: CalendarView.month,
                          dataSource: RequestDataSource(requests),
                          monthViewSettings: MonthViewSettings(showAgenda: true),
                          initialSelectedDate: DateTime.now(),
                        ));
                  }
                  else{
                    return CircularProgressIndicator();
                  }
                }
              ),
            ),
          ],
        ),
      ),
    );
  }
}
