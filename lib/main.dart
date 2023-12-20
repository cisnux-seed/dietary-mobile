import 'package:camera/camera.dart';
import 'package:dietary_flutter/presentation/dietary_app.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  final cameras = await availableCameras();

  runApp(
    ProviderScope(
      child: DietaryApp(
        cameras: cameras,
      ),
    ),
  );
}
