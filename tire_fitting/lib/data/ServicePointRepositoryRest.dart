import 'dart:async';
import 'dart:convert';
import 'dart:io';

import 'package:http/http.dart';
import 'package:tire_fitting/data/ServicePointRepository.dart';
import 'package:tire_fitting/entity/ServicePoint.dart';
import 'package:http/http.dart' as http;
import 'package:tire_fitting/main.dart';

class ServicePointRepositoryRest extends ServicePointRepository {
  String entityUrl;
  String baseAddress;

  ServicePointRepositoryRest() : super.create(){
    this.baseAddress = url + '/app/rest/v2';
    this.entityUrl = baseAddress + '/entities/tirefitting_ServicePoint';
  }

  @override
  Future<List<ServicePoint>> getAll() async {
    try {
      Response response = await http.get(
          entityUrl,
          headers: {
            'Authorization': 'Bearer ' + token
          }
      ).timeout(Duration(seconds: 10));


      List<ServicePoint> servicePoints = (jsonDecode(response.body) as List).map((e) => ServicePoint.fromJson(e)).toList();
      return servicePoints;
    }
    on TimeoutException {
      return super.getAll();
    }
  }

  @override
  Future<ServicePoint> getById(String id) async {
    try{
      Response response = await http.get(
        entityUrl + '?id=' + id,
        headers: {
          'Authorization': 'Bearer ' + token
        }
      ).timeout(Duration(seconds: 10));

      return ServicePoint.fromJson(jsonDecode(response.body));
    }
    on TimeoutException{
      return super.getById(id);
    }
  }

  @override
  Future<void> add(ServicePoint servicePoint){
    try{
      return http.post(
        entityUrl,
        headers: {
          'Authorization': 'Bearer ' + token,
          'Content-Type': 'application/json',

        },
        body: jsonEncode(servicePoint.toMap())
      ).timeout(Duration(seconds: 10));

    }
    on TimeoutException{
      return super.add(servicePoint);
    }
  }

  Future<void> remove(ServicePoint servicePoint) async {
    try{
      http.Response response = await http.get(
          baseAddress + "/services/tirefitting_ServicePointService/delete?id=" + servicePoint.id,
          headers: {
            'Authorization': 'Bearer ' + token
          }
      ).timeout(Duration(seconds: 10));

      return response;
    }
    on TimeoutException{
      return super.remove(servicePoint);
    }
  }
}