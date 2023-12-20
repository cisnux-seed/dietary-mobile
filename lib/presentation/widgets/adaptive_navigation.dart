import 'package:dietary_flutter/presentation/navigation/navigation_item.dart';
import 'package:dietary_flutter/presentation/utils/extensions.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:go_router/go_router.dart';
import '../navigation/nav_destination.dart';
import '../navigation/navigation_provider.dart';

final class AdaptiveNavigation extends ConsumerWidget {
  const AdaptiveNavigation({
    required this.child,
    super.key,
  });

  final Widget child;

  int _getIndex(BuildContext context) {
    final GoRouter route = GoRouter.of(context);
    final String location = route.location;

    if (location == NavDestination.home.path) return 0;
    if (location == NavDestination.report.path) return 1;
    return 2;
  }

  bool _isFabShow(BuildContext context){
    final GoRouter route = GoRouter.of(context);
    final String location = route.location;
    return location == NavDestination.home.path;
  }

  void _onNavTapped(int index, BuildContext context) {
    final navigationActions = {
      0: () => GoRouter.of(context).go(NavDestination.home.path),
      1: () => GoRouter.of(context).go(NavDestination.report.path),
      2: () => GoRouter.of(context).go(NavDestination.myProfile.path),
    };
    navigationActions[index]?.call();
  }

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final shouldShowNavBar = ref.watch(navBarProvider);
    final navBarNotifier = ref.watch(navBarProvider.notifier);

    return Scaffold(
      body: child,
      floatingActionButton: _isFabShow(context)
          ? FloatingActionButton(
              onPressed: () {
                navBarNotifier.state = false;
                context.go(
                  '/${NavDestination.foodScanner.path}',
                );
              },
              tooltip: 'Food Scanner',
              child: const ImageIcon(
                  AssetImage('assets/images/ic_food_scanner_24dp.png')),
            )
          : null,
      bottomNavigationBar: shouldShowNavBar
          ? NavigationBar(
              selectedIndex: _getIndex(context),
              onDestinationSelected: (index) => _onNavTapped(index, context),
              destinations: navigationItems
                  .map(
                    (navigationItem) => NavigationDestination(
                      icon: (navigationItem is NavigationItemIcon)
                          ? Icon(navigationItem.icon)
                          : ImageIcon(
                              AssetImage((navigationItem as NavigationItemAsset)
                                  .asset),
                            ),
                      selectedIcon: (navigationItem is NavigationItemIcon)
                          ? Icon(navigationItem.selectedIcon)
                          : ImageIcon(
                              AssetImage((navigationItem as NavigationItemAsset)
                                  .asset),
                            ),
                      label: (navigationItem is NavigationItemIcon)
                          ? navigationItem.label
                          : (navigationItem as NavigationItemAsset).label,
                      tooltip: (navigationItem is NavigationItemIcon)
                          ? navigationItem.label
                          : (navigationItem as NavigationItemAsset).label,
                    ),
                  )
                  .toList(),
            )
          : null,
    );
  }
}
