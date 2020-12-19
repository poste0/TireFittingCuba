import 'dart:async';
import 'dart:convert';
import 'dart:io';

import 'package:tire_fitting/data/RequestRepository.dart';
import 'package:tire_fitting/entity/Request.dart';
import 'package:tire_fitting/entity/ServicePoint.dart';

import 'package:http/http.dart' as http;
import 'package:tire_fitting/main.dart';

class RequestRepositoryRest extends RequestRepository{
  String entitiyUrl = 'http://192.168.0.58:8080/app/rest/v2/entities/tirefitting_Request';
  String baseAddress = url + '/app/rest/v2';

  RequestRepositoryRest() : super.create(){

  }


  @override
  Future<List<Request>> getRequests(ServicePoint servicePoint) async {
    try{
      http.Response response = await http.get(
        baseAddress + "/queries/tirefitting_Request/requestByServicePoint?id=" + servicePoint.id + "&view=request",
        headers: {
          'Authorization': 'Bearer ' + token
        }
      );


      (jsonDecode(response.body) as List).forEach((element) {print(Request.fromJson(element));});

      List<Request> requests = (jsonDecode(response.body) as List).map((e) => Request.fromJson(e)).toList();

      return requests;

    }
    on TimeoutException{
      super.getRequests(servicePoint);
    }
  }

  Future<bool> addRequest(Request request) async{
    try{
      http.Response response = await http.post(
        baseAddress + "/services/tirefitting_RequestService/add",
        headers: {
          'Authorization': 'Bearer ' + token,
          'Content-Type': 'application/json',

        },
        body: jsonEncode(request.toMapPost())
      ).timeout(Duration(seconds: 10));

      print(response.body);

      if(response.body == ""){
        return false;
      }

      return true;
    }
    on TimeoutException{
      super.add(request);
    }
  }
}