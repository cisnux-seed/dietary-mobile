import 'package:camera/camera.dart';
import 'package:dietary_flutter/presentation/navigation/nav_destination.dart';
import 'package:dietary_flutter/presentation/pages/foodscanner/food_scanner_page.dart';
import 'package:dietary_flutter/presentation/pages/myprofile/my_profile.dart';
import 'package:dietary_flutter/presentation/pages/report/report_page.dart';
import 'package:dietary_flutter/presentation/pages/scannerresult/ScannerResultPage.dart';
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'pages/home/home_page.dart';
import 'styles/color_schemes.dart';
import 'styles/text_theme.dart';
import 'widgets/adaptive_navigation.dart';

final class DietaryApp extends StatelessWidget {
  const DietaryApp({super.key, required this.cameras});

  final List<CameraDescription> cameras;

  @override
  Widget build(BuildContext context) {
    return MaterialApp.router(
      debugShowCheckedModeBanner: false,
      title: 'Note App',
      theme: ThemeData(
        useMaterial3: true,
        colorScheme: kLightColorScheme,
        textTheme: textTheme,
        visualDensity: VisualDensity.adaptivePlatformDensity,
      ),
      darkTheme: ThemeData(
        useMaterial3: true,
        colorScheme: kDarkColorScheme,
        textTheme: textTheme,
        visualDensity: VisualDensity.adaptivePlatformDensity,
      ),
      routerConfig: routerConfig,
    );
  }

  RouterConfig<Object>? get routerConfig => GoRouter(
        initialLocation: NavDestination.home.path,
        routes: [
          ShellRoute(
            builder: (context, state, child) => AdaptiveNavigation(
              child: child,
            ),
            routes: <RouteBase>[
              GoRoute(
                path: NavDestination.home.path,
                builder: (_, __) => const HomePage(),
                routes: [
                  GoRoute(
                    path: NavDestination.foodScanner.path,
                    routes: [
                      GoRoute(
                        path: NavDestination.scannerResult.path,
                        builder: (_, state) {
                          return ScannerResultPage(
                            foodPicture: state.pathParameters['foodPicture'],
                          );
                        },
                      ),
                    ],
                    builder: (_, __) => FoodScannerPage(cameras: cameras),
                  ),
                ],
              ),
              GoRoute(
                path: NavDestination.report.path,
                builder: (_, __) => const ReportPage(),
                routes: const [],
              ),
              GoRoute(
                path: NavDestination.myProfile.path,
                builder: (_, __) => const MyProfilePage(),
                routes: const [],
              ),
            ],
          )
        ],
      );
}
