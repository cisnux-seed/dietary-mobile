import 'package:camera/camera.dart';
import 'package:dietary_flutter/presentation/navigation/nav_destination.dart';
import 'package:dietary_flutter/presentation/navigation/navigation_provider.dart';
import 'package:dietary_flutter/presentation/styles/material_shapes.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';
import 'package:image_picker/image_picker.dart';
import 'package:lottie/lottie.dart';

final isBackCamera = StateProvider.autoDispose((_) => true);

final class FoodScannerPage extends ConsumerStatefulWidget {
  const FoodScannerPage({super.key, required this.cameras});

  final List<CameraDescription> cameras;

  @override
  ConsumerState<FoodScannerPage> createState() => _FoodScannerPageState();
}

class _FoodScannerPageState extends ConsumerState<FoodScannerPage> {
  late CameraController _controller;
  late Future<void> _initializeControllerFuture;

  @override
  void initState() {
    super.initState();
    _controller = CameraController(widget.cameras.first, ResolutionPreset.high);
    _initializeControllerFuture = _controller.initialize();
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  Future<void> _initializeCamera() async {
    CameraDescription selectedCamera = ref.watch(isBackCamera)
        ? widget.cameras.firstWhere(
            (camera) => camera.lensDirection == CameraLensDirection.front)
        : widget.cameras.firstWhere(
            (camera) => camera.lensDirection == CameraLensDirection.back);
    _controller = CameraController(selectedCamera, ResolutionPreset.high);
    _initializeControllerFuture = _controller.initialize();
  }

  @override
  Widget build(BuildContext context) {
    final cameraNotifier = ref.watch(isBackCamera.notifier);

    return Scaffold(
      body: FutureBuilder<void>(
        future: _initializeControllerFuture,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.done) {
            // If the Future is complete, display the camera preview
            return Stack(
              children: <Widget>[
                Positioned.fill(
                  child: AspectRatio(
                    aspectRatio: _controller.value.aspectRatio,
                    child: CameraPreview(_controller),
                  ),
                ),
                Positioned(
                  left: 12,
                  top: 36,
                  child: CircleAvatar(
                    backgroundColor: Colors.black.withOpacity(0.5),
                    child: IconButton(
                      onPressed: () {
                        ref.read(navBarProvider.notifier).state = true;
                        context.pop();
                      },
                      icon: const Icon(
                        Icons.arrow_back_rounded,
                        color: Colors.white,
                      ),
                    ),
                  ),
                ),
                Positioned.fill(
                  child: Center(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.center,
                      mainAxisAlignment: MainAxisAlignment.center,
                      mainAxisSize: MainAxisSize.max,
                      children: [
                        SizedBox(
                          height: 220,
                          width: 205,
                          child: Lottie.asset('assets/focusable_anim.json'),
                        ),
                        Container(
                          decoration: BoxDecoration(
                              color: Colors.black.withOpacity(0.4),
                              borderRadius: BorderRadius.circular(kMedium)),
                          child: Padding(
                            padding: const EdgeInsets.all(8.0),
                            child: SizedBox(
                              width: 165,
                              child: Text(
                                "Keep a 30cm distance from food to camera",
                                style: Theme.of(context)
                                    .textTheme
                                    .labelLarge
                                    ?.copyWith(color: Colors.white),
                                textAlign: TextAlign.center,
                              ),
                            ),
                          ),
                        )
                      ],
                    ),
                  ),
                ),
                Positioned.fill(
                  child: Align(
                    alignment: Alignment.bottomCenter,
                    child: Container(
                      color: Colors.black.withOpacity(0.5),
                      width: double.infinity,
                      height: 120,
                      child: Row(
                        crossAxisAlignment: CrossAxisAlignment.center,
                        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                        children: [
                          TextButton(
                            onPressed: () async {
                              try {
                                final image = await ImagePicker()
                                    .pickImage(source: ImageSource.gallery);
                                if (image == null) return;
                                if (!context.mounted) return;
                                context.go("/${NavDestination.foodScanner.path}/${NavDestination.scannerResult.path}?foodPicture=${image.path}");
                              } on PlatformException catch (e) {
                                debugPrint(e.message);
                              }
                            },
                            child: Text(
                              "Gallery",
                              style: Theme.of(context)
                                  .textTheme
                                  .labelLarge
                                  ?.copyWith(color: Colors.white),
                            ),
                          ),
                          IconButton(
                            onPressed: () async {
                              showDialog<String>(
                                context: context,
                                builder: (BuildContext context) => AlertDialog(
                                  title: const Text('Health Profile'),
                                  content: Column(
                                    mainAxisSize: MainAxisSize.min,
                                    children: <Widget>[
                                      Text(
                                        "Review your health profile: verify and update your information for accuracy.",
                                        style: Theme.of(context)
                                            .textTheme
                                            .bodyMedium
                                            ?.copyWith(
                                              color: Theme.of(context)
                                                  .colorScheme
                                                  .onSurfaceVariant,
                                            ),
                                      ),
                                      const SizedBox(height: 24),
                                      Row(
                                        crossAxisAlignment:
                                            CrossAxisAlignment.center,
                                        mainAxisAlignment:
                                            MainAxisAlignment.spaceBetween,
                                        children: [
                                          Row(
                                            crossAxisAlignment:
                                                CrossAxisAlignment.center,
                                            children: [
                                              const ImageIcon(
                                                AssetImage(
                                                    'assets/images/ic_age_100dp.png'),
                                                size: 24,
                                              ),
                                              const SizedBox(width: 8),
                                              Text(
                                                "Age",
                                                style: Theme.of(context)
                                                    .textTheme
                                                    .titleSmall
                                                    ?.copyWith(
                                                      color: Theme.of(context)
                                                          .colorScheme
                                                          .onSurface,
                                                      fontWeight:
                                                          FontWeight.bold,
                                                    ),
                                              )
                                            ],
                                          ),
                                          Text(
                                            "40 years old",
                                            style: Theme.of(context)
                                                .textTheme
                                                .bodyMedium
                                                ?.copyWith(
                                                    color: Theme.of(context)
                                                        .colorScheme
                                                        .onSurface),
                                          )
                                        ],
                                      ),
                                      const SizedBox(height: 8),
                                      Divider(
                                          color: Theme.of(context)
                                              .colorScheme
                                              .onSurface),
                                      const SizedBox(height: 8),
                                      Row(
                                        crossAxisAlignment:
                                            CrossAxisAlignment.center,
                                        mainAxisAlignment:
                                            MainAxisAlignment.spaceBetween,
                                        children: [
                                          Row(
                                            crossAxisAlignment:
                                                CrossAxisAlignment.center,
                                            children: [
                                              const ImageIcon(
                                                AssetImage(
                                                    'assets/images/ic_height_100dp.png'),
                                                size: 24,
                                              ),
                                              const SizedBox(width: 8),
                                              Text(
                                                "Height",
                                                style: Theme.of(context)
                                                    .textTheme
                                                    .titleSmall
                                                    ?.copyWith(
                                                      color: Theme.of(context)
                                                          .colorScheme
                                                          .onSurface,
                                                      fontWeight:
                                                          FontWeight.bold,
                                                    ),
                                              )
                                            ],
                                          ),
                                          Text(
                                            "170 cm",
                                            style: Theme.of(context)
                                                .textTheme
                                                .bodyMedium
                                                ?.copyWith(
                                                    color: Theme.of(context)
                                                        .colorScheme
                                                        .onSurface),
                                          )
                                        ],
                                      ),
                                      const SizedBox(height: 8),
                                      Divider(
                                          color: Theme.of(context)
                                              .colorScheme
                                              .onSurface),
                                      const SizedBox(height: 8),
                                      Row(
                                        crossAxisAlignment:
                                            CrossAxisAlignment.center,
                                        mainAxisAlignment:
                                            MainAxisAlignment.spaceBetween,
                                        children: [
                                          Row(
                                            crossAxisAlignment:
                                                CrossAxisAlignment.center,
                                            children: [
                                              const ImageIcon(
                                                AssetImage(
                                                    'assets/images/ic_scale_100dp.png'),
                                                size: 24,
                                              ),
                                              const SizedBox(width: 8),
                                              Text(
                                                "Weight",
                                                style: Theme.of(context)
                                                    .textTheme
                                                    .titleSmall
                                                    ?.copyWith(
                                                      color: Theme.of(context)
                                                          .colorScheme
                                                          .onSurface,
                                                      fontWeight:
                                                          FontWeight.bold,
                                                    ),
                                              )
                                            ],
                                          ),
                                          Text(
                                            "70 kg",
                                            style: Theme.of(context)
                                                .textTheme
                                                .bodyMedium
                                                ?.copyWith(
                                                    color: Theme.of(context)
                                                        .colorScheme
                                                        .onSurface),
                                          )
                                        ],
                                      ),
                                      const SizedBox(height: 8),
                                      Divider(
                                          color: Theme.of(context)
                                              .colorScheme
                                              .onSurface),
                                      const SizedBox(height: 8),
                                      Row(
                                        crossAxisAlignment:
                                            CrossAxisAlignment.center,
                                        mainAxisAlignment:
                                            MainAxisAlignment.spaceBetween,
                                        children: [
                                          Row(
                                            crossAxisAlignment:
                                                CrossAxisAlignment.center,
                                            children: [
                                              const ImageIcon(
                                                AssetImage(
                                                    'assets/images/ic_male_100dp.png'),
                                                size: 24,
                                              ),
                                              const SizedBox(width: 8),
                                              Text(
                                                "Gender",
                                                style: Theme.of(context)
                                                    .textTheme
                                                    .titleSmall
                                                    ?.copyWith(
                                                      color: Theme.of(context)
                                                          .colorScheme
                                                          .onSurface,
                                                      fontWeight:
                                                          FontWeight.bold,
                                                    ),
                                              )
                                            ],
                                          ),
                                          Text(
                                            "Man",
                                            style: Theme.of(context)
                                                .textTheme
                                                .bodyMedium
                                                ?.copyWith(
                                                    color: Theme.of(context)
                                                        .colorScheme
                                                        .onSurface),
                                          )
                                        ],
                                      ),
                                    ],
                                  ),
                                  actions: <Widget>[
                                    TextButton(
                                      onPressed: () => context.pop(),
                                      child: const Text('Update'),
                                    ),
                                    TextButton(
                                      onPressed: () async {
                                        try {
                                          // Ensure that the camera is initialized.
                                          await _initializeControllerFuture;

                                          final image =
                                              await _controller.takePicture();

                                          if (!mounted) return;

                                          context.go("/${NavDestination.foodScanner.path}/${NavDestination.scannerResult.path}?foodPicture=${image.path}");
                                        } catch (e) {
                                          // If an error occurs, log the error to the console.
                                          // print(e);
                                        } finally {
                                          context.pop();
                                        }
                                      },
                                      child: const Text('Done'),
                                    ),
                                  ],
                                ),
                              );
                            },
                            icon: const Icon(
                              Icons.camera_alt_rounded,
                              color: Colors.white,
                              size: 40,
                            ),
                          ),
                          TextButton(
                              onPressed: () async {
                                await _initializeCamera();
                                cameraNotifier.state = !cameraNotifier.state;
                              },
                              child: Text(
                                "Rotate",
                                style: Theme.of(context)
                                    .textTheme
                                    .labelLarge
                                    ?.copyWith(color: Colors.white),
                              ))
                        ],
                      ),
                    ),
                  ),
                )
              ],
            );
          } else {
            return const Center(child: CircularProgressIndicator());
          }
        },
      ),
    );
  }
}
