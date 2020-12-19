import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_i18n/flutter_i18n.dart';
import 'package:intl/intl.dart';
import 'package:sqflite/sqflite.dart';
import 'package:tire_fitting/data/Repository.dart';
import 'package:tire_fitting/data/RequestRepository.dart';
import 'package:tire_fitting/data/RequestRepositoryRest.dart';
import 'package:tire_fitting/data/ServicePointRepository.dart';
import 'package:tire_fitting/data/ServicePointRepositoryRest.dart';
import 'package:tire_fitting/requestTypes/RequestType.dart';
import 'package:tire_fitting/ui/RequestCalendar.dart';
import 'file:///C:/TireFitting/tire_fitting/lib/main.dart';
import '../entity/Request.dart';
import '../entity/ServicePoint.dart';

class RequestCreate extends StatefulWidget {
  @override
  _RequestCreateState createState() => _RequestCreateState();
}

class _RequestCreateState extends State<RequestCreate> {
  ServicePointRepositoryRest servicePointRepository = ServicePointRepositoryRest();
  RequestRepositoryRest requestRepository = RequestRepositoryRest();
  Future<List<ServicePoint>> servicePoints;
  ServicePoint currentServicePoint;
  RequestType currentRequestType;

  Key key = GlobalKey();

  _RequestCreateState() {
    try {
      servicePoints = servicePointRepository.getAll();
    } catch (e) {
      if (e is DatabaseException) {
        Repository.initDb();
      }
    }
    //currentServicePoint = servicePoints.length > 0 ? servicePoints[0] : null;
  }

  List<RequestType> requestTypes = RequestType.requestTypes;
  TextEditingController wheelRadiusController = TextEditingController();
  DateTime time;
  TimeOfDay timeOfDay;

  bool isFull = false;

  TextFormField dateField (context) => TextFormField(
    enabled: false,
    controller: TextEditingController(),
    validator: (value) {
      return value == null ? FlutterI18n.translate(context, 'choose_day') : null;
    },
  );

  @override
  Widget build(BuildContext context) {
    FlatButton okButton = FlatButton(
        onPressed: () {
          if (isFull) {
            requestRepository
                .addRequest(Request(
                    requestType: currentRequestType,
                    wheelRadius: int.parse(wheelRadiusController.value.text),
                    time: DateTime(time.year, time.month, time.day,
                        timeOfDay.hour, timeOfDay.minute),
                    servicePoint: currentServicePoint))
                .then((value) {
              if (value) {
                Navigator.pop(context);
              } else {
                showDialog(
                    context: context,
                    builder: (context) {
                      return AlertDialog(
                        content: Row(
                          children: [
                            Expanded(child: Text(FlutterI18n.translate(context, 'time_is_busy'))),
                            Expanded(
                              child: FlatButton(
                                  onPressed: (){
                                    Navigator.push(context, MaterialPageRoute(builder: (context) => RequestCalendar(servicePoint: currentServicePoint)));
                                  },
                                  child: Text(FlutterI18n.translate(context, 'go_to_all_requests') + currentServicePoint.address)),
                            )
                          ],
                        ),
                      );
                    });
              }
            });
          }
        },
        child: Text(
          FlutterI18n.translate(context, "ok"),
          style: getMainStyle(context),
        ));

    return Scaffold(
        appBar: getAppBar(FlutterI18n.translate(context, 'create_request'), context),
        body: Padding(
          padding: const EdgeInsets.all(30.0),
          child: Form(
            key: key,
            child: Column(
              children: [
                Expanded(
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      FutureBuilder(
                        future: servicePoints,
                        builder: (context, snapshot) {
                          if (snapshot.hasData) {
                            List<ServicePoint> servicePoints =
                                snapshot.data as List<ServicePoint>;
                            return Expanded(
                              child: DropdownButtonFormField<ServicePoint>(
                                decoration: InputDecoration(
                                    labelText: FlutterI18n.translate(context, 'service_point'),
                                    labelStyle: getMainStyle(context)),
                                validator: (value) {
                                  return value == null
                                      ? FlutterI18n.translate(context, 'enter_service_point')
                                      : null;
                                },
                                value: currentServicePoint,
                                items: servicePoints
                                    .map((e){
                                      String address = e.address.length < 30 ? e.address : e.address.substring(0, 30) + "...";
                                  return DropdownMenuItem<ServicePoint>(
                                    child: Text(address),
                                    value: e,
                                  );
                                })
                                    .toList(),
                                onChanged: (value) {
                                  setState(() {
                                    currentServicePoint = value;
                                  });
                                },
                              ),
                            );
                          } else {
                            return CircularProgressIndicator();
                          }
                        },
                      ),
                    ],
                  ),
                ),
                Expanded(
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Expanded(
                        child: DropdownButtonFormField(
                            decoration: InputDecoration(
                                labelText: FlutterI18n.translate(context, 'request_type'),
                                labelStyle: getMainStyle(context)),
                            validator: (value) {
                              return value == null
                                  ? FlutterI18n.translate(context, 'enter_request_type')
                                  : null;
                            },
                            value: currentRequestType,
                            items: requestTypes
                                .map((e) => DropdownMenuItem(
                                    child: Text(e.getName(context)), value: e))
                                .toList(),
                            onChanged: (value) {
                              setState(() {
                                currentRequestType = value;
                              });
                            }),
                      ),
                    ],
                  ),
                ),
                Expanded(
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text(FlutterI18n.translate(context, 'day'), style: getMainStyle(context)),
                      FlatButton(
                        onPressed: () {
                          showModalBottomSheet(context: context, builder: (builder){
                            var time = DateTime.now();
                            return Column(
                              children: [
                                Expanded(
                                  child: CupertinoDatePicker(onDateTimeChanged: (value){
                                    time = value;
                                  },
                                    mode: CupertinoDatePickerMode.date,
                                    initialDateTime: time,
                                  ),
                                ),
                                Expanded(
                                  child: FlatButton(
                                    child: Text(FlutterI18n.translate(context, 'ok')),
                                    onPressed: () => Navigator.pop(context, time),
                                  ),
                                )
                              ],
                            );
                          }).then((value){
                            setState(() {
                              time = value;
                            });
                          });
                        },


                          child: Text(
                              time == null ? FlutterI18n.translate(context, 'enter_day') : DateFormat("yyyy-MM-dd").format(time))),
                    ],
                  ),
                ),
                Expanded(
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text(FlutterI18n.translate(context, 'time'), style: getMainStyle(context)),
                      FlatButton(
                          onPressed: () => {
                            showModalBottomSheet(context: context, builder: (builder){
                              var start = TimeOfDay(hour: 0, minute: 0);
                              var dateNow = DateTime.now();
                              if(time != null && time.difference(dateNow).inDays < 1){
                                start = TimeOfDay(hour: dateNow.hour, minute: dateNow.minute);
                              }

                              var timeOfDay = TimeOfDay(hour: dateNow.hour, minute: dateNow.minute);

                              return Column(
                                children: [
                                  CupertinoTimerPicker(
                                    mode: CupertinoTimerPickerMode.hm,
                                    minuteInterval: 1,
                                    initialTimerDuration: Duration(hours: dateNow.hour, minutes: dateNow.minute),
                                    onTimerDurationChanged: (value){
                                        timeOfDay = TimeOfDay(hour: value.inHours, minute: value.inMinutes % 60);
                                    },
                                  ),
                                  FlatButton(onPressed: (){
                                    Navigator.pop(context, timeOfDay);
                                  }, child: Text(FlutterI18n.translate(context, 'ok')))
                                ],
                              );
                            }).then((value){
                              setState(() {
                                timeOfDay = value;
                              });
                            })

                              },
                          child: Text(timeOfDay == null
                              ? FlutterI18n.translate(context, 'enter_time')
                              : timeOfDay.format(context))),
                    ],
                  ),
                ),
                Expanded(
                  child: TextFormField(
                    keyboardType: TextInputType.number,
                    controller: wheelRadiusController,
                    decoration: InputDecoration(
                        labelText: FlutterI18n.translate(context, 'wheel_radius'),
                        labelStyle: getMainStyle(context)),
                    validator: (value) {
                      final String message = FlutterI18n.translate(context, 'wheel_radius_range');
                      try {
                        return int.parse(value) < 13 || int.parse(value) > 18
                            ? message
                            : null;
                      } catch (e) {
                        return message;
                      }
                    },
                  ),
                ),
                Expanded(child: okButton)
              ],
            ),
            autovalidateMode: AutovalidateMode.always,
            onChanged: () {
              if (time == null ||
                  timeOfDay == null ||
                  currentRequestType == null ||
                  currentServicePoint == null) {
                isFull = false;
              } else {
                isFull = true;
              }
            },
          ),
        ));
  }
}
