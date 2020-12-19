import 'package:tire_fitting/requestTypes/RequestType.dart';

class TireDismount extends RequestType {
  @override
  double getDuration(int radius) {
    return 60 * 60 * (1 + (radius - 13) / 5);
  }

  @override
  String name = "TIRE_DISMOUNT";
}
